package it.meucci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManagerDB 
{
	private Connection conn;
	
	
	public ManagerDB(String host, String port, String database, String user, String password) 
	{
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public Utente login(String email, String password)
	{
		Utente temp = null;
		
		
		try 
		{
			String query = "SELECT * FROM utenti WHERE email = ? AND password = md5(?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				temp = new Utente(rs.getInt("idUtente"), rs.getString("email"), rs.getString("password"), rs.getString("nome"), rs.getString("cognome"), rs.getString("indirizzo"), rs.getString("telefono"), rs.getInt("tipoUtente"), rs.getInt("idZona"));
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void chiudiConnessione()
	{
		try 
		{
			conn.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}

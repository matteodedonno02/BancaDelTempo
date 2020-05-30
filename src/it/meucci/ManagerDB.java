package it.meucci;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class ManagerDB 
{
	private Connection conn;
	
	
	public ManagerDB(String host, String port, String database, String user, String password) 
	{
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=utf8", user, password);
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
				temp = new Utente(rs.getInt("idUtente"), rs.getString("email"), rs.getString("password"), rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono"), rs.getInt("tipoUtente"), rs.getInt("idZona"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public ArrayList<String> listaProvince()
	{
		ArrayList<String> province = new ArrayList<String>();
		
		
		try 
		{
			String query = "SELECT * FROM italy_provincies";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				province.add(rs.getString("sigla"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return province;
	}
	
	
	public ArrayList<String> comuneDaProvincia(String provincia)
	{
		ArrayList<String> comuni = new ArrayList<String>();
		
		
		try 
		{
			String query = "SELECT * FROM italy_cities WHERE provincia LIKE ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, provincia);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				if(rs.getString("comune").endsWith("à"))
				{
					comuni.add(rs.getString("comune").substring(0, rs.getString("comune").length() - 1) + "&agrave;");
				}
				else if(rs.getString("comune").endsWith("è"))
				{
					comuni.add(rs.getString("comune").substring(0, rs.getString("comune").length() - 1) + "&egrave;");
				}
				else if(rs.getString("comune").endsWith("ì"))
				{
					comuni.add(rs.getString("comune").substring(0, rs.getString("comune").length() - 1) + "&igrave;");
				}
				else if(rs.getString("comune").endsWith("ò"))
				{
					comuni.add(rs.getString("comune").substring(0, rs.getString("comune").length() - 1) + "&ograve;");
				}
				else if(rs.getString("comune").endsWith("ù"))
				{
					comuni.add(rs.getString("comune").substring(0, rs.getString("comune").length() - 1) + "&ugrave;");
				}
				else
				{
					comuni.add(rs.getString("comune"));
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return comuni;
	}
	
	
	public int registrazione(Utente temp) 
	{
		try 
		{
			String query = "SELECT * FROM zone WHERE descrizione = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, temp.getIndirizzo().split(",")[2].substring(1));
			
			
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
			{
				query = "INSERT INTO zone (descrizione) VALUES (?)";
				ps = conn.prepareStatement(query);
				ps.setString(1, temp.getIndirizzo().split(",")[2].substring(1));
				ps.execute();
			}
			
			
			query = "SELECT * FROM zone WHERE descrizione = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, temp.getIndirizzo().split(",")[2].substring(1));
			rs = ps.executeQuery();
			int idZona = 0;
			if(rs.next())
			{
				idZona = rs.getInt("idZona");
			}
			
			
			query = "INSERT INTO utenti VALUES (0, ?, md5(?), ?, ?, ?, 0, ?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, temp.getEmail());
			ps.setString(2, temp.getPassword());
			ps.setString(3, temp.getNominativo());
			ps.setString(4, temp.getIndirizzo());
			ps.setString(5, temp.getTelefono());
			ps.setInt(6, idZona);
			ps.execute();
			
			return 1;
		}
		catch (SQLIntegrityConstraintViolationException e) 
		{
			return 0;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return -1;
		}
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

package it.meucci;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

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
	
	
	public ArrayList<Utente> sociInDebito()
	{
		ArrayList<Utente> temp = new ArrayList<Utente>();
		
		
		try 
		{
			String query = "SELECT u.nominativo, u.telefono, t1.oreFruite, t2.oreErogate FROM "
					+ "utenti u "
					+ "LEFT JOIN "
					+ "(SELECT u.idUtente as idFruitore, SUM(p.ore) as oreFruite FROM utenti u INNER JOIN prestazioni p ON u.idUtente = p.idFruitore WHERE p.statoPrestazione = 2 GROUP BY u.idUtente) t1 ON u.idUtente = t1.idFruitore "
					+ "LEFT JOIN "
					+ "(SELECT u.idUtente as idErogatore, SUM(p.ore) as oreErogate FROM utenti u INNER JOIN prestazioni p ON u.idUtente = p.idErogatore WHERE p.statoPrestazione = 2 GROUP BY u.idUtente) t2 ON u.idUtente = t2.idErogatore "
					+ "WHERE oreFruite > oreErogate OR (oreErogate IS NULL AND oreFruite IS NOT NULL)";
			
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Utente(rs.getString("nominativo"), rs.getString("telefono"), rs.getInt("oreFruite"), rs.getInt("oreErogate")));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public Zona zonaDaId(int idZona)
	{
		Zona temp = null;
		
		
		try 
		{
			String query = "SELECT * FROM zone WHERE idZona = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idZona);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				temp = new Zona(rs.getInt("idZona"), rs.getString("descrizione"), rs.getString("htmlMappa"));
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public ArrayList<Categoria> categorie()
	{
		ArrayList<Categoria> categorie = new ArrayList<Categoria>();
		
		
		try 
		{
			String query = "SELECT * FROM categorie";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				categorie.add(new Categoria(rs.getInt("idCategoria"), rs.getString("descrizione")));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return categorie;
	}
	
	
	public ArrayList<Utente> sociDaCategoriaEZona(int idCategoria, int idZona, int idUtente)
	{
		ArrayList<Utente> temp = new ArrayList<Utente>();
		
		
		try 
		{
			String query = "SELECT * FROM ((utenti u INNER JOIN zone z ON u.idZona = z.idZona) "
					+ "INNER JOIN categorie_utenti cu ON u.idUtente = cu.IdUtente) "
					+ "INNER JOIN categorie c ON c.idCategoria = cu.idCategoria "
					+ "WHERE c.idCategoria = ? AND z.idZona = ? AND u.idUtente <> ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idCategoria);
			ps.setInt(2, idZona);
			ps.setInt(3, idUtente);
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Utente(rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono")));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public ArrayList<Utente> sociSegreteria()
	{
		ArrayList<Utente> temp = new ArrayList<Utente>();
		
		
		try 
		{
			String query = "SELECT * FROM utenti u "
					+ "INNER JOIN "
					+ "categorie_utenti cu ON u.idUtente = cu.IdUtente "
					+ "WHERE u.idUtente IN "
					+ "(SELECT idUtente FROM categorie_utenti WHERE idCategoria = 6) "
					+ "GROUP BY u.idUtente "
					+ "HAVING COUNT(*) > 1 ";
			PreparedStatement ps = conn.prepareStatement(query);
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				ArrayList<Categoria> categorie = new ArrayList<Categoria>();
				String query2 = "SELECT * FROM (utenti u INNER JOIN categorie_utenti cu ON u.idUtente = cu.IdUtente) "
						+ "INNER JOIN categorie c ON c.idCategoria = cu.idCategoria "
						+ "WHERE u.idUtente = ? AND c.idCategoria <> 6";
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idUtente"));
				
				
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next())
				{
					categorie.add(new Categoria(rs2.getInt("idCategoria"), rs2.getString("descrizione")));
				}
				
				
				temp.add(new Utente(rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono"), categorie));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public int oreErogate(int idUtente)
	{
		int oreErogate = 0;
		
		
		try 
		{
			String query = "SELECT SUM(p.ore) oreErogate FROM utenti u "
					+ "INNER JOIN "
					+ "prestazioni p ON u.idUtente = p.idErogatore WHERE u.idUtente = ? GROUP BY u.idUtente AND p.statoPrestazione = 2";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				oreErogate = rs.getInt("oreErogate");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return oreErogate;
	}
	
	
	public int oreFruite(int idUtente)
	{
		int oreFruite = 0;
		
		
		try 
		{
			String query = "SELECT SUM(p.ore) oreFruite FROM utenti u "
					+ "INNER JOIN "
					+ "prestazioni p ON u.idUtente = p.idFruitore WHERE u.idUtente = ? GROUP BY u.idUtente AND p.statoPrestazione = 2";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				oreFruite = rs.getInt("oreFruite");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return oreFruite;
	}
	
	
	public ArrayList<Prestazione> prestazioniErogate(int idUtente)
	{
		ArrayList<Prestazione> temp = new ArrayList<Prestazione>();
		
		
		try 
		{
			String query = "SELECT * FROM utenti u "
					+ "INNER JOIN "
					+ "prestazioni p ON u.idUtente = p.idErogatore "
					+ "WHERE u.idUtente = ? AND p.statoPrestazione = 2 ORDER BY p.ore DESC";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Prestazione(rs.getDate("data"), rs.getInt("ore"), rs.getString("descrizione")));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public ArrayList<Utente> listaUtenti()
	{
		ArrayList<Utente> temp = new ArrayList<Utente>();
		
		
		try 
		{
			String query = "SELECT * FROM utenti";
			PreparedStatement ps = conn.prepareStatement(query);
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Utente(rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono")));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public ArrayList<Categoria> categorieUtente(int idUtente)
	{
		ArrayList<Categoria> temp = new ArrayList<Categoria>();
		
		
		try 
		{
			String query = "SELECT * FROM "
					+ "(utenti u INNER JOIN categorie_utenti cu ON u.idUtente = cu.idUtente) "
					+ "INNER JOIN "
					+ "categorie c ON c.idCategoria = cu.idCategoria "
					+ "WHERE u.idUtente = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Categoria(rs.getInt("idCategoria"), rs.getString("descrizione")));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void aggiungiCategoria(int idUtente, int idCategoria)
	{
		try 
		{
			String query = "INSERT INTO categorie_utenti VALUES(?, ?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idCategoria);
			ps.setInt(2, idUtente);
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void rimuoviCategoria(int idUtente, int idCategoria)
	{
		try 
		{
			String query ="DELETE FROM categorie_utenti "
					+ "WHERE idCategoria = ? AND idUtente = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idCategoria);
			ps.setInt(2, idUtente);
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
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

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
				temp.add(new Utente(rs.getInt("idUtente"), rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono"), rs.getString("email")));
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
					+ "prestazioni p ON u.idUtente = p.idFruitore "
					+ "WHERE p.idErogatore = ? AND p.statoPrestazione = 2 ORDER BY p.ore DESC";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Prestazione(rs.getDate("data"), rs.getInt("ore"), rs.getString("descrizione"), new Utente(rs.getString("email"), rs.getString("password"), rs.getString("nominativo"), rs.getString("telefono"))));
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
				temp.add(new Utente(rs.getInt("idUtente"), rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono"), rs.getString("email")));
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
	
	
	public ArrayList<Utente> utentiConCategorie()
	{
		ArrayList<Utente> temp = new ArrayList<Utente>();
		
		
		try 
		{
			String query = "SELECT * FROM utenti u";
			PreparedStatement ps = conn.prepareStatement(query);
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				ArrayList<Categoria> categorie = new ArrayList<Categoria>();
				String query2 = "SELECT * FROM "
						+ "(utenti u INNER JOIN categorie_utenti cu ON u.idUtente = cu.IdUtente) "
						+ "INNER JOIN categorie c ON c.idCategoria = cu.idCategoria "
						+ "WHERE u.idUtente = ?";
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idUtente"));
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next())
				{
					categorie.add(new Categoria(rs2.getInt("idCategoria"), rs2.getString("descrizione")));
				}
				
				
				temp.add(new Utente(rs.getInt("idUtente"), rs.getString("email"), rs.getString("password"), rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono"), rs.getInt("tipoUtente"), rs.getInt("idZona"), categorie));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void cancellaUtente(int idUtente)
	{
		try 
		{
			String query = "DELETE FROM categorie_utenti WHERE idUtente = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			ps.execute();
			
			
			query = "DELETE FROM prestazioni WHERE idErogatore = ? OR idFruitore = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			ps.setInt(2, idUtente);
			ps.execute();
			
			
			query = "DELETE FROM utenti WHERE idUtente = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public Utente utente(int idUtente)
	{
		Utente temp = null;
		
		
		try 
		{
			String query = "SELECT * FROM utenti WHERE idUtente = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				ArrayList<Categoria> categorie = new ArrayList<Categoria>();
				String query2 = "SELECT * FROM (utenti u INNER JOIN categorie_utenti cu ON u.idUtente = cu.idUtente) "
						+ "INNER JOIN categorie c ON c.idCategoria = cu.idCategoria "
						+ "WHERE u.idUtente = ?";
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, idUtente);
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next())
				{
					categorie.add(new Categoria(rs2.getInt("idCategoria"), rs2.getString("descrizione")));
				}
				
				
				temp = new Utente(idUtente, rs.getString("email"), rs.getString("password"), rs.getString("nominativo"), rs.getString("indirizzo"), rs.getString("telefono"), rs.getInt("tipoUtente"), rs.getInt("idZona"), categorie);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void modificaUtente(Utente temp)
	{
		try 
		{
			String query = "UPDATE utenti SET "
					+ "nominativo = ?,"
					+ "email = ?, "
					+ "telefono = ? "
					+ "WHERE idUtente = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, temp.getNominativo());
			ps.setString(2, temp.getEmail());
			ps.setString(3, temp.getTelefono());
			ps.setInt(4, temp.getIdUtente());
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void cancellaCategoria(int idCategoria)
	{
		try 
		{
			String query = "DELETE FROM categorie_utenti WHERE idCategoria = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idCategoria);
			ps.execute();
			
			
			query = "DELETE FROM categorie WHERE idCategoria = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, idCategoria);
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public Categoria categoria(int idCategoria)
	{
		Categoria temp = null;
		
		
		try 
		{
			String query ="SELECT * FROM categorie "
					+ "WHERE idCategoria = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idCategoria);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				temp = new Categoria(rs.getInt("idCategoria"), rs.getString("descrizione"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void modificaCategoria(Categoria temp)
	{
		try 
		{
			String query = "UPDATE categorie "
					+ "SET descrizione = ? "
					+ "WHERE idCategoria = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, temp.getDescrizione());
			ps.setInt(2, temp.getIdCategoria());
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public int aggiungiCategoria(String descrizione)
	{
		try 
		{
			descrizione = descrizione.toLowerCase();
			descrizione = descrizione.replace(descrizione.charAt(0), Character.toUpperCase(descrizione.charAt(0)));
			
			
			String query = "INSERT INTO categorie (descrizione) VALUES (?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, descrizione);
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
	
	
	public ArrayList<Prestazione> prestazioni()
	{
		ArrayList<Prestazione> temp = new ArrayList<Prestazione>();
		
		
		try 
		{
			String query = "SELECT *, DATE_FORMAT(data, '%d/%m/%Y') dataFormattata FROM prestazioni";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Utente fruitore = null;
				Utente erogatore = null;
				Categoria categoria = null;
				String query2 = "SELECT * FROM utenti u "
						+ "INNER JOIN "
						+ "prestazioni p ON u.idUtente = p.idFruitore "
						+ "WHERE p.idPrestazione = ?";
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idPrestazione"));
				ResultSet rs2 = ps2.executeQuery();
				if(rs2.next())
				{
					fruitore = new Utente(rs2.getInt("idUtente"), rs2.getString("nominativo"), rs2.getString("indirizzo"), rs2.getString("telefono"), rs2.getString("email"));
				}
				
				
				query2 = "SELECT * FROM utenti u "
						+ "INNER JOIN "
						+ "prestazioni p ON u.idUtente = p.idErogatore "
						+ "WHERE p.idPrestazione = ?";
				ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idPrestazione"));
				rs2 = ps2.executeQuery();
				if(rs2.next())
				{
					erogatore = new Utente(rs2.getInt("idUtente"), rs2.getString("nominativo"), rs2.getString("indirizzo"), rs2.getString("telefono"), rs2.getString("email"));
				}
				
				
				query2 = "SELECT * FROM categorie c "
						+ "INNER JOIN prestazioni p ON c.idCategoria = p.idCategoria "
						+ "WHERE p.idPrestazione = ?";
				ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idPrestazione"));
				rs2 = ps2.executeQuery();
				if(rs2.next())
				{
					categoria = new Categoria(rs2.getInt("idCategoria"), rs2.getString("descrizione"));
				}
				
				
				temp.add(new Prestazione(rs.getInt("idPrestazione"), rs.getString("dataFormattata"), rs.getInt("ore"), rs.getString("descrizione"), rs.getInt("statoPrestazione"), categoria, fruitore, erogatore));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void cancellaPrestazione(int idPrestazione)
	{
		try 
		{
			String query = "DELETE FROM prestazioni "
					+ "WHERE idPrestazione = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idPrestazione);
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public Prestazione prestazione(int idPrestazione)
	{
		Prestazione temp = null;
		
		
		try 
		{
			String query = "SELECT * FROM prestazioni "
					+ "WHERE idPrestazione = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idPrestazione);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				Utente fruitore = null;
				Utente erogatore = null;
				Categoria categoria = null;
				String query2 = "SELECT * FROM utenti u "
						+ "INNER JOIN "
						+ "prestazioni p ON u.idUtente = p.idFruitore "
						+ "WHERE p.idPrestazione = ?";
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idPrestazione"));
				ResultSet rs2 = ps2.executeQuery();
				if(rs2.next())
				{
					fruitore = new Utente(rs2.getInt("idUtente"), rs2.getString("nominativo"), rs2.getString("indirizzo"), rs2.getString("telefono"), rs2.getString("email"));
				}
				
				
				query2 = "SELECT * FROM utenti u "
						+ "INNER JOIN "
						+ "prestazioni p ON u.idUtente = p.idErogatore "
						+ "WHERE p.idPrestazione = ?";
				ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idPrestazione"));
				rs2 = ps2.executeQuery();
				if(rs2.next())
				{
					erogatore = new Utente(rs2.getInt("idUtente"), rs2.getString("nominativo"), rs2.getString("indirizzo"), rs2.getString("telefono"), rs2.getString("email"));
				}
				
				
				query2 = "SELECT * FROM categorie c "
						+ "INNER JOIN prestazioni p ON c.idCategoria = p.idCategoria "
						+ "WHERE p.idPrestazione = ?";
				ps2 = conn.prepareStatement(query2);
				ps2.setInt(1, rs.getInt("idPrestazione"));
				rs2 = ps2.executeQuery();
				if(rs2.next())
				{
					categoria = new Categoria(rs2.getInt("idCategoria"), rs2.getString("descrizione"));
				}
				
				
				temp = new Prestazione(rs.getInt("idPrestazione"), rs.getDate("data"), rs.getInt("ore"), rs.getString("descrizione"), rs.getInt("statoPrestazione"), categoria, fruitore, erogatore);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void modificaPrestazione(Prestazione temp)
	{
		try 
		{
			String query = "UPDATE prestazioni SET "
					+ "data = ?, ore = ?, descrizione = ?, statoPrestazione = ?, idFruitore = ?, idErogatore = ?, idCategoria = ? "
					+ "WHERE idPrestazione = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDate(1, temp.getData());
			ps.setInt(2, temp.getOre());
			ps.setString(3, temp.getDescrizione());
			ps.setInt(4, temp.getStatoPrestazione());
			ps.setInt(5, temp.getIdFruitore());
			ps.setInt(6, temp.getIdErogatore());
			ps.setInt(7, temp.getIdCategoria());
			ps.setInt(8, temp.getIdPrestazione());
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void aggiungiPrestazione(Prestazione temp)
	{
		try 
		{
			String query = "INSERT INTO prestazioni "
					+ "(data, ore, descrizione, statoPrestazione, idCategoria, idErogatore, idFruitore) VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, temp.getDataFormattata());
			ps.setInt(2, temp.getOre());
			ps.setString(3, temp.getDescrizione());
			ps.setInt(4, temp.getStatoPrestazione());
			ps.setInt(5, temp.getIdCategoria());
			ps.setInt(6, temp.getIdErogatore());
			ps.setInt(7, temp.getIdFruitore());
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public ArrayList<Prestazione> prestazioniDaApprovare(int idUtente)
	{
		ArrayList<Prestazione> temp = new ArrayList<Prestazione>();
		
		
		try 
		{
			String query = "SELECT *, DATE_FORMAT(data, '%d/%m/%Y') as dataFormattata, p.descrizione as descrizionePrestazione, c.descrizione as descrizioneCategoria FROM "
					+ "(utenti u INNER JOIN prestazioni p ON u.idUtente = p.idFruitore) "
					+ "INNER JOIN "
					+ "categorie c ON c.idCategoria = p.idCategoria "
					+ "WHERE p.idErogatore = ? AND p.statoPrestazione = 0 ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Prestazione(rs.getInt("idPrestazione"), rs.getString("dataFormattata"), rs.getInt("ore"), rs.getString("descrizionePrestazione"), rs.getInt("statoPrestazione"), new Categoria(rs.getInt("idCategoria"), rs.getString("descrizioneCategoria")), new Utente(rs.getString("email"), null, rs.getString("nominativo"), rs.getString("telefono")), null));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public ArrayList<Prestazione> prestazioniDaConcludere(int idUtente)
	{
		ArrayList<Prestazione> temp = new ArrayList<Prestazione>();
		
		
		try 
		{
			String query = "SELECT *, DATE_FORMAT(data, '%d/%m/%Y') as dataFormattata, p.descrizione as descrizionePrestazione, c.descrizione as descrizioneCategoria FROM "
					+ "(utenti u INNER JOIN prestazioni p ON u.idUtente = p.idFruitore) "
					+ "INNER JOIN "
					+ "categorie c ON c.idCategoria = p.idCategoria "
					+ "WHERE p.idErogatore = ? AND p.statoPrestazione = 1 ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Prestazione(rs.getInt("idPrestazione"), rs.getString("dataFormattata"), rs.getInt("ore"), rs.getString("descrizionePrestazione"), rs.getInt("statoPrestazione"), new Categoria(rs.getInt("idCategoria"), rs.getString("descrizioneCategoria")), new Utente(rs.getString("email"), null, rs.getString("nominativo"), rs.getString("telefono")), null));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void approvaPrestazione(int idPrestazione)
	{
		try 
		{
			String query = "UPDATE prestazioni "
					+ "SET statoPrestazione = 1 "
					+ "WHERE idPrestazione = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idPrestazione);
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void concludiPrestazione(int idPrestazione)
	{
		try 
		{
			String query = "UPDATE prestazioni "
					+ "SET statoPrestazione = 2 "
					+ "WHERE idPrestazione = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idPrestazione);
			ps.execute();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public ArrayList<Prestazione> prestazioniFruite(int idUtente)
	{
		ArrayList<Prestazione> temp = new ArrayList<Prestazione>();
		
		
		try 
		{
			String query = "SELECT * ,DATE_FORMAT(data, '%d/%m/%Y') as dataFormattata, p.descrizione as descrizionePrestazione, c.descrizione as descrizioneCategoria "
					+ "FROM "
					+ "(utenti u INNER JOIN prestazioni p ON u.idUtente = p.idErogatore) "
					+ "INNER JOIN "
					+ "categorie c ON c.idCategoria = p.idCategoria "
					+ "WHERE p.idFruitore = ? "
					+ "ORDER BY p.data DESC";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idUtente);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				temp.add(new Prestazione(rs.getInt("idPrestazione"), rs.getString("dataFormattata"), rs.getInt("ore"), rs.getString("descrizionePrestazione"), rs.getInt("statoPrestazione"), new Categoria(rs.getInt("idCategoria"), rs.getString("descrizioneCategoria")), null, new Utente(rs.getString("email"), null, rs.getString("nominativo"), rs.getString("telefono"))));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	
	public void rifiutaPrestazione(int idPrestazione)
	{
		try 
		{
			String query = "DELETE FROM prestazioni "
					+ "WHERE idPrestazione = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idPrestazione);
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

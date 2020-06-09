package it.meucci;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/gestionePrestazioni")
public class GestionePrestazioniServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    
    public GestionePrestazioniServlet() 
    {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String cmd = request.getParameter("cmd");
		
		
		switch (cmd) 
		{
			case "cancellaPrestazione":
			{
				if(((Utente)request.getSession().getAttribute("LOGGED_USER")).getTipoUtente() == 0)
				{
					response.sendRedirect("index.jsp");
					return;
				}
				
				
				int idPrestazione = Integer.parseInt(request.getParameter("idPrestazione"));
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				db.cancellaPrestazione(idPrestazione);
				db.chiudiConnessione();
				response.sendRedirect("admin/visualizza.jsp?elemento=prestazioni");
			}
			break;
			
			
			case "approvaPrestazione":
			{
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				db.approvaPrestazione(Integer.parseInt(request.getParameter("idPrestazione")));
				db.chiudiConnessione();
				response.sendRedirect("profilo.jsp#richieste");
			}
			break;
			
			
			case "concludiPrestazione":
			{
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				db.concludiPrestazione(Integer.parseInt(request.getParameter("idPrestazione")));
				db.chiudiConnessione();
				response.sendRedirect("profilo.jsp#richieste");
			}
			break;
			
			
			case "rifiutaPrestazione":
			{
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				db.rifiutaPrestazione(Integer.parseInt(request.getParameter("idPrestazione")));
				db.chiudiConnessione();
				response.sendRedirect("profilo.jsp#richieste");
			}
			break;
	
			default:
			break;
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String cmd = request.getParameter("cmd");
		Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
		ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
		
		
		switch (cmd) 
		{
			case "selezioneCategoria":
			{
				String categoria = request.getParameter("txtCategoria");
				ArrayList<Categoria> categorie = (ArrayList<Categoria>)getServletContext().getAttribute("CATEGORIE");
				int idCategoria = 0;
				
				
				for(int i = 0; i < categorie.size(); i ++)
				{
					if(categorie.get(i).getDescrizione().equals(categoria))
					{
						idCategoria = categorie.get(i).getIdCategoria();
						break;
					}
				}
				
				
				ArrayList<Utente> temp = db.sociDaCategoriaEZona(idCategoria, ((Utente)request.getSession().getAttribute("LOGGED_USER")).getIdZona(), ((Utente)request.getSession().getAttribute("LOGGED_USER")).getIdUtente());
				db.chiudiConnessione();
				
				
				String html = "<div class='card'>" + 
						"<div class='card-body'>" + 
						"<table id='example2' class='table table-bordered table-hover'>" + 
						"<thead>" + 
						"<tr>" + 
						"<th>Nominativo</th>" + 
						"<th>Indirizzo</th>" + 
						"<th>Telefono</th>" + 
						"<th>Richiedi</th>" + 
						"</tr>" + 
						"</thead>" + 
						"<tbody>";
				for(int i = 0; i < temp.size(); i ++)
				{
					html += "<tr>" + "<td>" + temp.get(i).getNominativo() + "</td>" + "<td><a target='_blank' href='https://www.google.it/maps/place/" + temp.get(i).getIndirizzo().replace(" ", "+") + "'>" + temp.get(i).getIndirizzo() + "</a></td>" + "<td>" + temp.get(i).getTelefono() + "</td>" + "<td style='text-align: center;'><form id='risultato" + i + "' action='gestionePrestazioni' method='post'><input type='hidden' name='cmd' value='richiediPrestazione'><input type='hidden' name='idCategoria' value='" + idCategoria + "'><input type='hidden' name='idErogatore' value='" + temp.get(i).getIdUtente() + "'><input type='hidden' name='idFruitore' value='" + ((Utente)request.getSession().getAttribute("LOGGED_USER")).getIdUtente() + "'></form><a href='' class='richiediPrestazione' id='richiediPrestazione" + i + "'><i class=\"fas fa-check-circle\"></i></a></td>" + "</tr>";
				}
				
				
				html += "</tbody>" + 
						"</table>" + 
						"</div>" + 
						"<!-- /.card-body -->" + 
						"</div>";
				
				
				response.getWriter().write(html);
			}
			break;
			
			
			case "modificaPrestazione":
			{
				if(((Utente)request.getSession().getAttribute("LOGGED_USER")).getTipoUtente() == 0)
				{
					response.sendRedirect("index.jsp");
					return;
				}
				
				
				int idPrestazione = Integer.parseInt(request.getParameter("idPrestazione"));
				String descrizione = request.getParameter("txtDescrizione");
				String temp = request.getParameter("txtData");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date data = null;
				try 
				{
					data = new Date(formatter.parse(temp).getTime());
				} 
				catch (ParseException e) 
				{
					e.printStackTrace();
				}
				
				
				int ore = Integer.parseInt(request.getParameter("txtOre"));
				int statoPrestazione = Integer.parseInt(request.getParameter("statoPrestazione"));
				int idFruitore = Integer.parseInt(request.getParameter("idFruitore"));
				int idErogatore = Integer.parseInt(request.getParameter("idErogatore"));
				int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
				
				
				db.modificaPrestazione(new Prestazione(idPrestazione, data, ore, descrizione, statoPrestazione, idCategoria, idErogatore, idFruitore));
				db.chiudiConnessione();
				response.sendRedirect("admin/visualizza.jsp?elemento=prestazioni");
			}
			break;
			
			
			case "richiediPrestazione":
			{
				int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
				int idFruitore = Integer.parseInt(request.getParameter("idFruitore"));
				int idErogatore = Integer.parseInt(request.getParameter("idErogatore"));
				int ore = Integer.parseInt(request.getParameter("txtOre"));
				String data = request.getParameter("txtData");
				String descrizione = request.getParameter("txtDescrizione");
				Prestazione temp = new Prestazione(0, data, ore, descrizione, 0, idCategoria, idErogatore, idFruitore);
				db.aggiungiPrestazione(temp);
				db.chiudiConnessione();
				response.sendRedirect("index.jsp");
			}
			break;
	
			default:
			break;
		}
	}

}

package it.meucci;

import java.io.IOException;
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
				
				
				ArrayList<Utente> temp = db.sociDaCategoriaEZona(idCategoria, ((Utente)request.getSession().getAttribute("LOGGED_USER")).getIdZona());
				
				
				String html = "<div class='card'>" + 
						"<div class='card-body'>" + 
						"<table id='example2' class='table table-bordered table-hover'>" + 
						"<thead>" + 
						"<tr>" + 
						"<th>Nominativo</th>" + 
						"<th>Indirizzo</th>" + 
						"<th>Telefono</th>" + 
						"</tr>" + 
						"</thead>" + 
						"<tbody>";
				for(int i = 0; i < temp.size(); i ++)
				{
					html += "<tr>" + "<td>" + temp.get(i).getNominativo() + "</td>" + "<td><a target='_blank' href='https://www.google.it/maps/place/" + temp.get(i).getIndirizzo().replace(" ", "+") + "'>" + temp.get(i).getIndirizzo() + "</a></td>" + "<td>" + temp.get(i).getTelefono() + "</td>" + "</tr>";
				}
				
				
				html += "</tbody>" + 
						"</table>" + 
						"</div>" + 
						"<!-- /.card-body -->" + 
						"</div>";
				
				
				response.getWriter().write(html);
			}
			break;
	
			default:
			break;
		}
	}

}

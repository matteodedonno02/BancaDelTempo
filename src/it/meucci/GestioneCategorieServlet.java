package it.meucci;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/gestioneCategorie")
public class GestioneCategorieServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    
    public GestioneCategorieServlet() 
    {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String cmd = request.getParameter("cmd");
		
		
		switch (cmd) 
		{
			case "cancellaCategoria":
			{
				if(((Utente)request.getSession().getAttribute("LOGGED_USER")).getTipoUtente() == 0)
				{
					response.sendRedirect("index.jsp");
					return;
				}
				
				
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				
				
				int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
				db.cancellaCategoria(idCategoria);
				db.chiudiConnessione();
				
				
				response.sendRedirect("admin/visualizza.jsp?elemento=categorie");
			}
			break;
	
			default:
			break;
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String cmd = request.getParameter("cmd");
		
		
		switch (cmd) 
		{
			case "modificaCategoria":
			{
				if(((Utente)request.getSession().getAttribute("LOGGED_USER")).getTipoUtente() == 0)
				{
					response.sendRedirect("index.jsp");
					return;
				}
				
				
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				
				
				String descrizione = request.getParameter("txtDescrizione");
				int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
				db.modificaCategoria(new Categoria(idCategoria, descrizione));
				db.chiudiConnessione();
				response.sendRedirect("admin/visualizza.jsp?elemento=categorie");
			}
			break;
			
			
			case "aggiungiCategoria":
			{
				if(((Utente)request.getSession().getAttribute("LOGGED_USER")).getTipoUtente() == 0)
				{
					response.sendRedirect("index.jsp");
					return;
				}
				
				
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				int risultato = db.aggiungiCategoria(request.getParameter("txtDescrizione"));	
				if(risultato == 1)
				{
					response.sendRedirect("admin/visualizza.jsp?elemento=categorie&aggiunta=successo");
				}
				else if(risultato == 0)
				{
					response.sendRedirect("admin/visualizza.jsp?elemento=categorie&aggiunta=errore");
				}
			}
			break;
		
			default:
			break;
		}
	}

}

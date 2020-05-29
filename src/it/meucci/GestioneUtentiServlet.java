package it.meucci;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/gestioneUtenti")
public class GestioneUtentiServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
    
	
    public GestioneUtentiServlet()
    {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String cmd = request.getParameter("cmd");
		
		
		switch (cmd) 
		{
			case "login":
				String email = request.getParameter("txtEmail");
				String password = request.getParameter("txtPassword");
				
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				Utente temp = db.login(email, password);
				db.chiudiConnessione();
				
				
				if(temp != null)
				{
					response.sendRedirect("index.jsp");
				}
				else
				{
					response.sendRedirect("login.jsp?errore=erroreLogin");
				}
			break;
	
			default:
			break;
		}
	}

}

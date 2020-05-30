package it.meucci;

import java.io.IOException;
import java.util.ArrayList;
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
		Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
		ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
		
		
		switch (cmd) 
		{
			case "login":
			{
				String email = request.getParameter("txtEmail");
				String password = request.getParameter("txtPassword");
				
				
				Utente temp = db.login(email, password);
				db.chiudiConnessione();
				
				
				if(temp != null)
				{
					request.getSession().setAttribute("LOGGED_USER", temp);
					response.sendRedirect("index.jsp");
				}
				else
				{
					response.sendRedirect("login.jsp?errore=erroreLogin");
				}
			}
			break;
			
			
			case "sceltaComune":
			{
				ArrayList<String> comuni = db.comuneDaProvincia(request.getParameter("provincia"));
				db.chiudiConnessione();
				
				
				String html = "";
				for(String comune : comuni)
				{
					html += "<option>" + comune + "</option>";
				}
				
				
				response.getWriter().write(html);
			}
			break;
			
			
			case "registrazione":
			{
				String nominativo = request.getParameter("txtNominativo");
				String telefono = request.getParameter("txtTelefono");
				String indirizzo = request.getParameter("txtIndirizzo");
				String numeroCivico = request.getParameter("txtNumeroCivico");
				String provincia = request.getParameter("txtProvincia");
				String comune = request.getParameter("txtComune");
				String email = request.getParameter("txtEmail");
				String password = request.getParameter("txtPassword");
				
				
				indirizzo = indirizzo + ", " + numeroCivico + ", " + comune + " " + provincia;
				
				
				Utente temp = new Utente(email, password, nominativo, indirizzo, telefono);
				int risultato = db.registrazione(temp);
				db.chiudiConnessione();
				
				
				if(risultato == 1)
				{
					response.sendRedirect("login.jsp");
				}
				else if(risultato == 0)
				{
					response.sendRedirect("register.jsp?errore=erroreRegistrazione");
				}
			}
			break;
	
			
			default:
			break;
		}
	}

}

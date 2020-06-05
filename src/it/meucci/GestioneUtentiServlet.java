package it.meucci;

import java.io.IOException;
import java.text.Normalizer;
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
		String cmd = request.getParameter("cmd");
		
		
		switch (cmd) 
		{
			case "logout":
			{
				request.getSession().removeAttribute("LOGGED_USER");
				response.sendRedirect("index.jsp");
			}
			break;
			
			
			case "cancellaUtente":
			{
				if(((Utente)request.getSession().getAttribute("LOGGED_USER")).getTipoUtente() == 0)
				{
					response.sendRedirect("index.jsp");
					return;
				}
				
				
				Properties prop = (Properties)getServletContext().getAttribute("PROPERTIES");
				ManagerDB db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
				
				
				int idUtente = Integer.parseInt(request.getParameter("idUtente"));
				db.cancellaUtente(idUtente);
				db.chiudiConnessione();
				
				
				response.sendRedirect("admin/visualizza.jsp?elemento=utenti");
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
			
			
			case "aggiungiCategoria":
			{
				int idUtente = Integer.parseInt(request.getParameter("idUtente"));
				int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
				
				
				db.aggiungiCategoria(idUtente, idCategoria);
				ArrayList<Categoria> categorie;
				ArrayList<Categoria> categorieUtente;
				categorie = db.categorie();
				categorieUtente = db.categorieUtente(idUtente);
				db.chiudiConnessione();
				
				
				for(int i = 0; i < categorieUtente.size(); i ++)
				{
					for(int j = 0; j < categorie.size(); j ++)
					{
						if(categorieUtente.get(i).getIdCategoria() == categorie.get(j).getIdCategoria())
						{
							categorie.remove(j);
						}
					}
				}
				
				
				String html = "<h5>Categorie disponibili</h5>";
				for(int i = 0; i < categorie.size(); i ++)
				{
					html += "<div class='row' style='margin-bottom: 10px;'>"
							+ "<div class='col-2'>"
							+ "<button id='" + categorie.get(i).getIdCategoria() + "' type='button' class='aggiungiCategoria btn btn-primary btn-block'><i class='fas fa-plus-circle'></i></button>"
							+ "</div>"
							+ "<div class='col-10'>"
							+ categorie.get(i).getDescrizione()
							+ "</div>"
							+ "</div>";
				}
          		html += "<h5>Le tue categorie</h5>";
          		for(int i = 0; i < categorieUtente.size(); i ++)
          		{
          			html += "<div class='row' style='margin-bottom: 10px;'>"
          					+ "<div class='col-2'>"
          					+ "<button id='" + categorieUtente.get(i).getIdCategoria() + "' type='button' class='rimuoviCategoria btn btn-primary btn-block'><i class='fas fa-minus-circle'></i></button>"
          					+ "</div>"
          					+ "<div class='col-10'>"
          					+ categorieUtente.get(i).getDescrizione()
          					+ "</div>"
          					+ "</div>";
          		}
				
				
				response.getWriter().write(html);
			}
			break;
			
			
			case "rimuoviCategoria":
			{
				int idUtente = Integer.parseInt(request.getParameter("idUtente"));
				int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
				
				
				db.rimuoviCategoria(idUtente, idCategoria);
				ArrayList<Categoria> categorie;
				ArrayList<Categoria> categorieUtente;
				categorie = db.categorie();
				categorieUtente = db.categorieUtente(idUtente);
				db.chiudiConnessione();
				
				
				for(int i = 0; i < categorieUtente.size(); i ++)
				{
					for(int j = 0; j < categorie.size(); j ++)
					{
						if(categorieUtente.get(i).getIdCategoria() == categorie.get(j).getIdCategoria())
						{
							categorie.remove(j);
						}
					}
				}
				
				
				String html = "<h5>Categorie disponibili</h5>";
				for(int i = 0; i < categorie.size(); i ++)
				{
					html += "<div class='row' style='margin-bottom: 10px;'>"
							+ "<div class='col-2'>"
							+ "<button id='" + categorie.get(i).getIdCategoria() + "' type='button' class='aggiungiCategoria btn btn-primary btn-block'><i class='fas fa-plus-circle'></i></button>"
							+ "</div>"
							+ "<div class='col-10'>"
							+ categorie.get(i).getDescrizione()
							+ "</div>"
							+ "</div>";
				}
          		html += "<h5>Le tue categorie</h5>";
          		for(int i = 0; i < categorieUtente.size(); i ++)
          		{
          			html += "<div class='row' style='margin-bottom: 10px;'>"
          					+ "<div class='col-2'>"
          					+ "<button id='" + categorieUtente.get(i).getIdCategoria() + "' type='button' class='rimuoviCategoria btn btn-primary btn-block'><i class='fas fa-minus-circle'></i></button>"
          					+ "</div>"
          					+ "<div class='col-10'>"
          					+ categorieUtente.get(i).getDescrizione()
          					+ "</div>"
          					+ "</div>";
          		}
          		
				
				response.getWriter().write(html);
			}
			break;
			
			
			case "modificaUtente":
			{
				if(((Utente)request.getSession().getAttribute("LOGGED_USER")).getTipoUtente() == 0)
				{
					response.sendRedirect("index.jsp");
					return;
				}
				
				
				int idUtente = Integer.parseInt(request.getParameter("idUtente"));
				String nominativo = request.getParameter("txtNominativo");
				String email = request.getParameter("txtEmail");
				String telefono = request.getParameter("txtTelefono");
				db.modificaUtente(new Utente(idUtente, email, null, nominativo, null, telefono, 0, 0));
				db.chiudiConnessione();
				response.sendRedirect("admin/visualizza.jsp?elemento=utenti");
			}
			break;
	
			
			default:
			break;
		}
	}

}

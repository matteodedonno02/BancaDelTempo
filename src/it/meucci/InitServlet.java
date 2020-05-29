package it.meucci;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = "/init", loadOnStartup = 1, asyncSupported = true)
public class InitServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    
    public InitServlet() 
    {
        super();
    }

	
	public void init(ServletConfig config) throws ServletException 
	{
		try 
		{
			String filePath = config.getServletContext().getRealPath("configfolder/config.properties");
			Properties prop = new Properties();
			prop.load(new FileInputStream(filePath));
			config.getServletContext().setAttribute("PROPERTIES", prop);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
//	{
//		
//	}
//
//	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
//	{
//		doGet(request, response);
//	}
}

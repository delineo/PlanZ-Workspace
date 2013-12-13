package planz.core.log;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.xml.*;

/**
 * Servlet implementation class Log4jConfig
 */
public class Log4jInit extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public  void init()
	{
	     String prefix = getServletContext().getRealPath("/");
	     String file   = getInitParameter("log4j-init-file");

	     System.out.println("Log4j Init Configuration File : " + file);
	     // if the log4j-init-file is not set, then no point in trying
	     if (file != null)
	     {
	    	 DOMConfigurator.configure(prefix+file);
	     }
	}	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{	}
}

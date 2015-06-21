package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beans.Ticket;

@SuppressWarnings("serial")
public class ListeTickets extends HttpServlet {
			
	private static final String VUE = "/WEB-INF/listerTickets.jsp";	
	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	public static final String ATT_SESSION_TICKETS = "mapTickets";
	
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<String, Ticket> mapTickets = (HashMap<String, Ticket>) session.getAttribute( ATT_SESSION_TICKETS );
		
		mapTickets = new HashMap<String, Ticket>();
			
	    // Ajout du Bean à l'objet requête
	   	request.setAttribute( ATT_SESSION_TICKETS, mapTickets );
	   	
		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}
}

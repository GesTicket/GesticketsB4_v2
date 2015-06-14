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

public class ListeTickets extends HttpServlet {
	
		
	private static final String VUE = "/WEB-INF/listerTickets.jsp";
	//private static final String ATT_FORM = "form";
	
	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";
	
	public static final String SESSION_UTILISATEURS = "utilisateurs"; 
	
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<String, Ticket> tickets = (HashMap<String, Ticket>) session.getAttribute( SESSION_UTILISATEURS );
		
		tickets = new HashMap<String, Ticket>();
		
		
	    // Ajout du Bean à l'objet requête
	   	request.setAttribute( SESSION_UTILISATEURS, tickets );
	   	
		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}
}

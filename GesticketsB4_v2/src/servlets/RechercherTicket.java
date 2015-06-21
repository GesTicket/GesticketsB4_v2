package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAOFactory;
import forms.TicketForm;
import beans.Ticket;
//import dao.TicketDao;

@SuppressWarnings("serial")
public class RechercherTicket extends HttpServlet {
	
		
	private static final String FORM_VUE = "/WEB-INF/rechercherTicket.jsp";
	//private static final String VUE_AFFICH_TICKET      = "/WEB-INF/rechercherTicket.jsp";
	private static final String ATT_FORM = "form";
	
	private static final String ATT_TICKET = "tickets";
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";
	public static final String ATT_SESSION_TICKETS = "ticket";
	
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// R�cup�ration de la fabrique dans la port�e application
    	DAOFactory daoFactory = (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID );
				
		
    	// instanciation d'un objet m�tier de validation des saisies du formulaire d'ajout d'un ticket
    	TicketForm form = new TicketForm( daoFactory );
    	
		// traitement de la requ�te POST par la m�thode ajouterTicket de l'objet m�tier
		// et r�cup�ration au retour du bean Ticket cr��
		Ticket ticket = form.rechercherTicket( request );
		
		// stockage de l'objet de traitement et du bean dans l'objet requ�te, pour la JSP
		request.setAttribute( ATT_FORM, form );
		request.setAttribute( ATT_TICKET, ticket );
		
		
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<String, Ticket> tickets = (HashMap<String, Ticket>) session.getAttribute( ATT_SESSION_TICKETS );
		
		tickets = new HashMap<String, Ticket>();
		
		
	    // Ajout du Bean � l'objet requ�te
	   	request.setAttribute( ATT_SESSION_TICKETS, tickets );
	   	
		this.getServletContext().getRequestDispatcher( FORM_VUE ).forward( request, response );
	}
}

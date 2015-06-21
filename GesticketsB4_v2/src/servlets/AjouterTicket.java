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

@SuppressWarnings("serial")
public class AjouterTicket extends HttpServlet {
	
	private static final String FORM_AJOUTER       = "/WEB-INF/ajouterTicket.jsp";
	private static final String VUE_AFFICH_TICKET  = "/WEB-INF/listerTickets.jsp";
	private static final String ATT_FORM           = "form";
	private static final String ATT_TICKET         = "ticket";
	public static final String ATT_SESSION_TICKETS = "mapTickets";
	// identifiant de l'attribut de scope Application donnant la r�f�rence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		getServletContext().getRequestDispatcher( FORM_AJOUTER ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		// R�cup�ration de la fabrique dans la port�e application
    	DAOFactory daoFactory = (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID );
				
		// instanciation d'un objet m�tier de validation des saisies du formulaire d'ajout d'un ticket
		TicketForm form = new TicketForm( daoFactory );
		
		// traitement de la requ�te POST par la m�thode ajouterTicket de l'objet m�tier
		// et r�cup�ration au retour du bean Ticket cr��
		Ticket ticket = form.ajouterTicket( request );
		
		// stockage de l'objet de traitement et du bean dans l'objet requ�te, pour la JSP
		request.setAttribute( ATT_FORM, form );
		request.setAttribute( ATT_TICKET, ticket );
		
		if ( form.getErreurs().isEmpty()) {
        	// r�cup�ration de la map des utilisateurs dans la session
            HttpSession session = request.getSession();
        	@SuppressWarnings("unchecked")
			Map<Long, Ticket> mapTickets = (HashMap<Long, Ticket>) session.getAttribute( ATT_SESSION_TICKETS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( mapTickets == null ) {
            	mapTickets = new HashMap<Long, Ticket>();
            }
            /* Puis ajout de l'utilisateur courant dans la map */
            mapTickets.put( ticket.getId(), ticket );
            /* Et enfin (r�)enregistrement de la map en session */
            session.setAttribute( ATT_SESSION_TICKETS, mapTickets );
        	// affichage de la fiche r�capitulative du nouvel utilisateur enregistr�
        	getServletContext().getRequestDispatcher( VUE_AFFICH_TICKET ).forward( request, response);
        } else {
        	// erreurs : r�affichage du formulaire avec les erreurs
        	getServletContext().getRequestDispatcher( FORM_AJOUTER ).forward( request, response );
        }
		
	}
}
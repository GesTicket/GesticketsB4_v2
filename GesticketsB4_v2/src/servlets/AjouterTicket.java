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
	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		getServletContext().getRequestDispatcher( FORM_AJOUTER ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		// Récupération de la fabrique dans la portée application
    	DAOFactory daoFactory = (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID );
				
		// instanciation d'un objet métier de validation des saisies du formulaire d'ajout d'un ticket
		TicketForm form = new TicketForm( daoFactory );
		
		// traitement de la requête POST par la méthode ajouterTicket de l'objet métier
		// et récupération au retour du bean Ticket créé
		Ticket ticket = form.ajouterTicket( request );
		
		// stockage de l'objet de traitement et du bean dans l'objet requête, pour la JSP
		request.setAttribute( ATT_FORM, form );
		request.setAttribute( ATT_TICKET, ticket );
		
		if ( form.getErreurs().isEmpty()) {
        	// récupération de la map des utilisateurs dans la session
            HttpSession session = request.getSession();
        	@SuppressWarnings("unchecked")
			Map<Long, Ticket> mapTickets = (HashMap<Long, Ticket>) session.getAttribute( ATT_SESSION_TICKETS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( mapTickets == null ) {
            	mapTickets = new HashMap<Long, Ticket>();
            }
            /* Puis ajout de l'utilisateur courant dans la map */
            mapTickets.put( ticket.getId(), ticket );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( ATT_SESSION_TICKETS, mapTickets );
        	// affichage de la fiche récapitulative du nouvel utilisateur enregistré
        	getServletContext().getRequestDispatcher( VUE_AFFICH_TICKET ).forward( request, response);
        } else {
        	// erreurs : réaffichage du formulaire avec les erreurs
        	getServletContext().getRequestDispatcher( FORM_AJOUTER ).forward( request, response );
        }
		
	}
}
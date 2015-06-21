package servlets;

import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import dao.DAOFactory;

import forms.TicketForm;

@SuppressWarnings("serial")
public class RechercherTicketsMotCle extends HttpServlet {

	public static final String VUE_RECHERCHE       = "/WEB-INF/rechercherTicketsMotCle.jsp";
	private static final String VUE_RESULTAT       = "/WEB-INF/ticketsRecherches.jsp";
	private static final String ATT_FORM           = "form";
	//private static final String ATT_MOT_CLE        = "motCle";
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";
	public static final String ATT_SESSION_TICKETS = "tickets";

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        // requ�te GET : simple affichage du formulaire JSP de connexion
		getServletContext().getRequestDispatcher( VUE_RECHERCHE ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// requ�te POST : validation des saisies, cr�ation de la MAP Erreurs et utilisation de variables de session

		// acc�s aux attributs de scope session
		//HttpSession session = request.getSession();

		// R�cup�ration de la fabrique dans la port�e application
    	DAOFactory daoFactory = (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID );

    	// instanciation d'un objet m�tier de validation des saisies du formulaire d'ajout d'un ticket
    	TicketForm form = new TicketForm( daoFactory );

		// traitement de la requ�te POST par la m�thode rechercherTickets de l'objet m�tier
		// et r�cup�ration au retour du bean Ticket cr��
    	form.rechercherTicketsMotCle( request );

	    // Ajout de la liste � l'objet session
	   	//session.setAttribute( ATT_SESSION_TICKETS, mapRechercheTickets );

    	// stockage de la MAP des erreurs en attribut de la requ�te, pour les pages JSP
    	request.setAttribute( ATT_FORM, form );

        // aiguillage
        if ( form.getErreurs().isEmpty() ) {
        	getServletContext().getRequestDispatcher( VUE_RESULTAT ).forward( request, response );
        } else {
        	getServletContext().getRequestDispatcher( VUE_RECHERCHE ).forward( request, response );
        }

	}
}

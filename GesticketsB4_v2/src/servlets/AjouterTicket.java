package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import forms.TicketForm;
import beans.Ticket;

public class AjouterTicket extends HttpServlet {
	
	private static final String FORM_AJOUTER = "/WEB-INF/ajouterTicket.jsp";
	private static final String ATT_FORM = "form";
	
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
		
		
		// affichage du r�sultat par la JSP
		getServletContext().getRequestDispatcher( FORM_AJOUTER ).forward( request, response );
	}
}
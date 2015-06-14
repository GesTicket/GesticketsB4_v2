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
		
		
		// affichage du résultat par la JSP
		getServletContext().getRequestDispatcher( FORM_AJOUTER ).forward( request, response );
	}
}
package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import forms.InscriptionForm;
import beans.Utilisateur;

public class Inscription extends HttpServlet {
	
	private static final String VUE_CREER_UTIL     = "/WEB-INF/Inscription.jsp";
	private static final String VUE_AFFICH_UTIL    = "/WEB-INF/afficherUtilisateur.jsp";
	private static final String ATT_FORM           = "form";
	private static final String ATT_USER           = "utilisateur";
	// identifiant de l'attribut de scope Application donnant la r�f�rence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		getServletContext().getRequestDispatcher( VUE_CREER_UTIL ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		// R�cup�ration de la fabrique dans la port�e application
    	DAOFactory daoFactory = (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID );
				
		// instanciation d'un objet m�tier de validation des saisies du formulaire d'inscription
		InscriptionForm form = new InscriptionForm( daoFactory );
		
		// traitement de la requ�te POST par la m�thode inscrireUtilisateur de l'objet m�tier
		// et r�cup�ration au retour du bean Utilisateur cr��
		Utilisateur utilisateur = form.inscrireUtilisateur( request );
		
		// stockage de l'objet de traitement et du bean dans l'objet requ�te, pour la JSP
		request.setAttribute( ATT_FORM, form );
		request.setAttribute( ATT_USER, utilisateur );
		
        // aiguillage
        if ( form.getErreurs().isEmpty()) { // pas d'erreur : affichage de l'utilisateur
        	// affichage de la fiche r�capitulative du nouvel utilisateur enregistr�
        	getServletContext().getRequestDispatcher( VUE_AFFICH_UTIL ).forward( request, response);
        } else { // erreurs : r�affichage du formulaire avec les erreurs
    	getServletContext().getRequestDispatcher( VUE_CREER_UTIL ).forward( request, response );
        }

	}
}
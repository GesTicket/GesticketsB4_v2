package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DAOFactory;
import forms.ConnexionForm;

@SuppressWarnings("serial")
public class Connexion extends HttpServlet {

 	// identifiants des attributs
	// scope REQUEST
    public static final String ATT_FORM            = "form";
	// chemin des pages JSP
	public static final String VUE_CONNEXION       = "/WEB-INF/Connexion.jsp";
    public static final String VUE_ACCUEIL         = "/index.jsp";
	// identifiant de l'attribut de scope Application donnant la r�f�rence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        // requ�te GET : simple affichage du formulaire JSP de connexion
		getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	// requ�te POST : validation des saisies, cr�ation du bean Connect� et de la MAP Erreurs

		// R�cup�ration de la fabrique dans la port�e application
    	DAOFactory daoFactory = (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID );

    	// instanciation de l'objet m�tier de traitement du formulaire
    	ConnexionForm form = new ConnexionForm( daoFactory );

    	// validation, maj de la MAP des erreurs : initialise la cha�ne resultat
    	form.connecterUtilisateur( request );

    	// stockage de la MAP des erreurs en attribut de la requ�te, pour les pages JSP
    	request.setAttribute( ATT_FORM, form );

        // aiguillage
        if ( form.getErreurs().isEmpty() ) {
        	getServletContext().getRequestDispatcher( VUE_ACCUEIL ).forward( request, response );
        } else {
        	getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
        }
	}
}
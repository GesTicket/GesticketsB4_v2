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
	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        // requête GET : simple affichage du formulaire JSP de connexion
		getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	// requête POST : validation des saisies, création du bean Connecté et de la MAP Erreurs

		// Récupération de la fabrique dans la portée application
    	DAOFactory daoFactory = (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID );

    	// instanciation de l'objet métier de traitement du formulaire
    	ConnexionForm form = new ConnexionForm( daoFactory );

    	// validation, maj de la MAP des erreurs : initialise la chaîne resultat
    	form.connecterUtilisateur( request );

    	// stockage de la MAP des erreurs en attribut de la requête, pour les pages JSP
    	request.setAttribute( ATT_FORM, form );

        // aiguillage
        if ( form.getErreurs().isEmpty() ) {
        	getServletContext().getRequestDispatcher( VUE_ACCUEIL ).forward( request, response );
        } else {
        	getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
        }
	}
}
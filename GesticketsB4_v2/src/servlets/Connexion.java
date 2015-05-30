package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forms.ConnexionForm;
import beans.Utilisateur;

public class Connexion extends HttpServlet {

 	// identifiants des attributs 
	//	 scope REQUEST
    public static final String ATT_USER          = "utilisateur";
    public static final String ATT_FORM          = "form";
	//	 scope SESSION
	public static final String ATT_SESSION_USER  = "sessionUtilisateur";
	// chemin des pages JSP
	public static final String VUE_CONNEXION     = "/WEB-INF/Connexion.jsp";
    public static final String VUE_ACCUEIL       = "/index.jsp";

	// identifiant de l'attribut de scope Application donnant la r�f�rence de la fabrique de DAOs
	//private static final String ATT_DAO_FACTORY_ID = "daoFactory";

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        // requ�te GET : simple affichage du formulaire JSP de connexion
		getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	// requ�te POST : validation des saisies, cr�ation du bean Connect� et de la MAP Erreurs

    	// instanciation de l'objet m�tier de traitement du formulaire
    	ConnexionForm form = new ConnexionForm();
 
    	// validation, maj de la MAP des erreurs : initialise la cha�ne resultat, 
 		// intancie un bean Client et retourne sa r�f�rence en sortie
    	Utilisateur utilisateur = form.connecterUtilisateur( request );
    	
    	// stockage des beans en attribut de la requ�te, pour les pages JSP
    	request.setAttribute( ATT_FORM, form );
    	request.setAttribute( ATT_USER, utilisateur );

        /* R�cup�ration de la session depuis la requ�te */
        HttpSession session = request.getSession();
        
        // aiguillage
        if ( form.getErreurs().isEmpty() ) { 
        	// pas d'erreur : ajout du bean Utilisateur � la session et affichage de la page d'accueil    	
        	session.setAttribute( ATT_SESSION_USER, utilisateur ) ;
        	getServletContext().getRequestDispatcher( VUE_ACCUEIL ).forward( request, response );
        } else {
        	// erreurs : suppression du bean de la session et r�affichage du formulaire avec les erreurs
        	session.setAttribute( ATT_SESSION_USER, null );
        	getServletContext().getRequestDispatcher( VUE_CONNEXION ).forward( request, response );
        }

	}
}
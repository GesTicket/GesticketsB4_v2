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
import dao.UtilisateurDao;
import forms.CreationUtilisateurForm;
import beans.Utilisateur;

@SuppressWarnings("serial")
public class CreationUtilisateur extends HttpServlet {
	
	private static final String VUE_CREER_UTIL       = "/WEB-INF/creerUtilisateur.jsp";
	private static final String VUE_AFFICH_UTIL      = "/WEB-INF/afficherUtilisateur.jsp";
	private static final String ATT_FORM             = "form";
	private static final String ATT_USER             = "utilisateur";
	public static final String SESSION_UTILISATEURS  = "mapUtilisateurs";
	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID   = "daoFactory";

	private UtilisateurDao utilisateurDao;
	
	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
	    this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID ) ).getUtilisateurDao();
    }
	
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		getServletContext().getRequestDispatcher( VUE_CREER_UTIL ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
				
		// instanciation d'un objet métier de validation des saisies du formulaire d'inscription
		CreationUtilisateurForm form = new CreationUtilisateurForm( utilisateurDao );
		
		// traitement de la requête POST par la méthode creerUtilisateur de l'objet métier
		// et récupération au retour du bean Utilisateur créé
		Utilisateur utilisateur = form.creerUtilisateur( request );
		
		// stockage de l'objet de traitement et du bean dans l'objet requête, pour la JSP
		request.setAttribute( ATT_FORM, form );
		request.setAttribute( ATT_USER, utilisateur );
		
        // aiguillage
		// si aucune erreur
        if ( form.getErreurs().isEmpty()) {
        	// récupération de la map des utilisateurs dans la session
            HttpSession session = request.getSession();
        	@SuppressWarnings("unchecked")
			Map<Long, Utilisateur> mapUtilisateurs = (HashMap<Long, Utilisateur>) session.getAttribute( SESSION_UTILISATEURS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( mapUtilisateurs == null ) {
                mapUtilisateurs = new HashMap<Long, Utilisateur>();
            }
            /* Puis ajout de l'utilisateur courant dans la map */
            mapUtilisateurs.put( utilisateur.getId(), utilisateur );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_UTILISATEURS, mapUtilisateurs );
        	// affichage de la fiche récapitulative du nouvel utilisateur enregistré
        	getServletContext().getRequestDispatcher( VUE_AFFICH_UTIL ).forward( request, response);
        } else {
        	// erreurs : réaffichage du formulaire avec les erreurs
        	getServletContext().getRequestDispatcher( VUE_CREER_UTIL ).forward( request, response );
        }
	}
}
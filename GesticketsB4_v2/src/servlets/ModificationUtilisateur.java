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
import forms.ModificationUtilisateurForm;
import beans.Utilisateur;

@SuppressWarnings("serial")
public class ModificationUtilisateur extends HttpServlet {
	
	private static final String VUE_MODIF_UTIL       = "/WEB-INF/modifierUtilisateur.jsp";
	private static final String VUE_AFFICH_UTIL      = "/WEB-INF/afficherUtilisateur.jsp";
	private static final String PARAM_ID_USER        = "idUtilisateur";
	private static final String ATT_FORM             = "form";
	private static final String ATT_USER             = "utilisateur";
	public static final String SESSION_UTILISATEURS  = "mapUtilisateurs";
	// identifiant de l'attribut de scope Application donnant la r�f�rence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID   = "daoFactory";

	private UtilisateurDao utilisateurDao;
	
	public void init() throws ServletException {
		/* R�cup�ration d'une instance de notre DAO Utilisateur */
	    this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID ) ).getUtilisateurDao();
    }
	
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

	    /* R�cup�ration du param�tre */
        String idUtilisateur = getValeurParametre( request, PARAM_ID_USER );
        System.out.println( "n� d'utilisateur � modifier : " + idUtilisateur );
        Long id = Long.parseLong( idUtilisateur );

        // trouver utilisateur
		if ( utilisateurDao.trouverUtilisateur( id ) == null ) {
			// si utilisateur inconnu
			System.out.println( "utilisateur inconnu" );
		} else {
			// sinon utilisateur connu
			System.out.println( "utilisateur OK" );
	 		// intancie un bean Client et retourne sa r�f�rence en sortie
	    	Utilisateur utilisateur = utilisateurDao.trouverUtilisateur( id );
	        request.setAttribute(ATT_USER, utilisateur);
		}
		
        getServletContext().getRequestDispatcher( VUE_MODIF_UTIL ).forward( request, response );
	}

	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		// instanciation d'un objet m�tier de validation des saisies du formulaire d'inscription
		ModificationUtilisateurForm form = new ModificationUtilisateurForm( utilisateurDao );
		
		// traitement de la requ�te POST par la m�thode inscrireUtilisateur de l'objet m�tier
		// et r�cup�ration au retour du bean Utilisateur cr��
		Utilisateur utilisateur = form.modifierUtilisateur( request );
		
		// stockage de l'objet de traitement et du bean dans l'objet requ�te, pour la JSP
		request.setAttribute( ATT_FORM, form );
		request.setAttribute( ATT_USER, utilisateur );
		
        // aiguillage
		// si aucune erreur
        if ( form.getErreurs().isEmpty()) {
        	// r�cup�ration de la map des utilisateurs dans la session
            HttpSession session = request.getSession();
        	@SuppressWarnings("unchecked")
			Map<Long, Utilisateur> mapUtilisateurs = (HashMap<Long, Utilisateur>) session.getAttribute( SESSION_UTILISATEURS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( mapUtilisateurs == null ) {
                mapUtilisateurs = new HashMap<Long, Utilisateur>();
            }
            /* Puis ajout de l'utilisateur courant dans la map */
            mapUtilisateurs.put( utilisateur.getId(), utilisateur );
            /* Et enfin (r�)enregistrement de la map en session */
            session.setAttribute( SESSION_UTILISATEURS, mapUtilisateurs );
        	// affichage de la fiche r�capitulative du nouvel utilisateur enregistr�
        	getServletContext().getRequestDispatcher( VUE_AFFICH_UTIL ).forward( request, response);
        } else {
        	// erreurs : r�affichage du formulaire avec les erreurs
        	getServletContext().getRequestDispatcher( VUE_MODIF_UTIL ).forward( request, response );
        }
	}
	
    // M�thode utilitaire qui retourne null si un param�tre est vide, et son contenu sinon.
	
    private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}
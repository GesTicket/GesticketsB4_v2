package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAOException;
import dao.DAOFactory;
import dao.UtilisateurDao;
import beans.Utilisateur;

@SuppressWarnings("serial")
public class SuppressionUtilisateur extends HttpServlet {

	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID   = "daoFactory";
	private static final String PARAM_ID_USER        = "idUtilisateur";
	public static final String SESSION_UTILISATEURS  = "utilisateurs";
	private static final String VUE_LISTE_UTIL       = "/listeUtilisateurs";

	private UtilisateurDao utilisateurDao;

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
	    this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( ATT_DAO_FACTORY_ID ) ).getUtilisateurDao();
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /* Récupération du paramètre */
        String idUtilisateur = getValeurParametre( request, PARAM_ID_USER );
        System.out.println( "n° d'utilisateur à supprimer : " + idUtilisateur );
        Long id = Long.parseLong( idUtilisateur );

        /* Récupération de la Map des utilisateurs enregistrés en session */
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
		Map<Long, Utilisateur> utilisateurs = (HashMap<Long, Utilisateur>) session.getAttribute( SESSION_UTILISATEURS );

        /* Si l'id de l'utilisateur et la Map des utilisateurs ne sont pas vides */
        if ( id != null && utilisateurs != null ) {
            try {
                /* Alors suppression de l'utilisateur de la BDD */
            	System.out.println( "suppression" );
                utilisateurDao.supprimerUtilisateur( utilisateurs.get( id ) );
                /* Puis suppression de l'utilisateur de la Map */
                utilisateurs.remove( id );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_UTILISATEURS, utilisateurs );
        }

        /* Redirection vers la fiche récapitulative */
        response.sendRedirect( request.getContextPath() + VUE_LISTE_UTIL );
	}

    // Méthode utilitaire qui retourne null si un paramètre est vide, et son contenu sinon.
	
    private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}

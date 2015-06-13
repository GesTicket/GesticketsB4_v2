package filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Utilisateur;
import dao.UtilisateurDao;
import dao.DAOFactory;

public class PrechargementFilter implements Filter {
	private static final String ATT_DAO_FACTORY_ID        = "daoFactory";
    public static final String ATT_SESSION_UTILISATEURS   = "utilisateurs";

    private UtilisateurDao utilisateurDao;
    // ticket

    public void init( FilterConfig config ) throws ServletException {
        // R�cup�ration d'une instance de notre DAO Utilisateur /* et du DAO Ticket
        this.utilisateurDao = ( (DAOFactory) config.getServletContext().getAttribute( ATT_DAO_FACTORY_ID ) ).getUtilisateurDao();
        //ticket
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
            ServletException {
        /* Cast de l'objet request */
        HttpServletRequest request = (HttpServletRequest) req;

        /* R�cup�ration de la session depuis la requ�te */
        HttpSession session = request.getSession();

        /*
         * Si la map des utilisateurs n'existe pas en session, alors l'utilisateur se
         * connecte pour la premi�re fois et nous devons pr�charger en session
         * les infos contenues dans la BDD.
         */
        if ( session.getAttribute( ATT_SESSION_UTILISATEURS ) == null ) {

            // R�cup�ration de la liste des clients existants, et enregistrement en session

            List<Utilisateur> listeUtilisateurs = utilisateurDao.listerUtilisateurs();
            Map<Long, Utilisateur> mapUtilisateurs = new HashMap<Long, Utilisateur>();
            for ( Utilisateur utilisateur : listeUtilisateurs ) {
                mapUtilisateurs.put( utilisateur.getId(), utilisateur );
            }
            session.setAttribute( ATT_SESSION_UTILISATEURS, mapUtilisateurs );
        }


        /* Pour terminer, poursuite de la requ�te en cours */
        chain.doFilter( request, res );
    }

    public void destroy() {
    }

}

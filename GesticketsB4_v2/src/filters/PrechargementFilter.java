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
import javax.servlet.http.HttpSession;

import beans.Utilisateur;
import dao.UtilisateurDao;
import dao.DAOFactory;
import dao.TicketDao;
import beans.Ticket;

public class PrechargementFilter implements Filter {
	private static final String ATT_DAO_FACTORY_ID        = "daoFactory";
    public static final String ATT_SESSION_UTILISATEURS   = "mapUtilisateurs";
    public static final String ATT_SESSION_TICKETS = "tickets";

    private UtilisateurDao utilisateurDao;

    private TicketDao ticketDao;

    public void init( FilterConfig config ) throws ServletException {
        // Récupération d'une instance de notre DAO Utilisateur /* et du DAO Ticket
        this.utilisateurDao = ( (DAOFactory) config.getServletContext().getAttribute( ATT_DAO_FACTORY_ID ) ).getUtilisateurDao();
        
        this.ticketDao = ((DAOFactory) config.getServletContext().getAttribute(ATT_DAO_FACTORY_ID) ).getTicketDao();
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
            ServletException {
        /* Cast de l'objet request */
        HttpServletRequest request = (HttpServletRequest) req;

        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();

        /*
         * Si la map des utilisateurs n'existe pas en session, alors l'utilisateur se
         * connecte pour la première fois et nous devons précharger en session
         * les infos contenues dans la BDD.
         */
        if ( session.getAttribute( ATT_SESSION_UTILISATEURS ) == null ) {

            // Récupération de la liste des clients existants, et enregistrement en session

            List<Utilisateur> listeUtilisateurs = utilisateurDao.listerUtilisateurs();
            Map<Long, Utilisateur> mapUtilisateurs = new HashMap<Long, Utilisateur>();
            for ( Utilisateur utilisateur : listeUtilisateurs ) {
                mapUtilisateurs.put( utilisateur.getId(), utilisateur );
            }
            session.setAttribute( ATT_SESSION_UTILISATEURS, mapUtilisateurs );
        }

        if ( session.getAttribute( ATT_SESSION_TICKETS ) == null ) {

            List<Ticket> listeTickets = ticketDao.listerTickets();
            Map<Long, Ticket> mapTickets = new HashMap<Long, Ticket>();
            for ( Ticket ticket : listeTickets ) {
                mapTickets.put( ticket.getId(), ticket );
            }
            session.setAttribute( ATT_SESSION_TICKETS, mapTickets );
        }
        /* Pour terminer, poursuite de la requête en cours */
        chain.doFilter( request, res );
    }

    public void destroy() {
    }

}

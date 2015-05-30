package dao;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitialisationDaoFactory implements ServletContextListener{

	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";
	// la fabrique, pour toutes les servlets de  l'appli stockée en scope APPLICATION
	private DAOFactory daoFactory;

	@Override
	public void contextInitialized( ServletContextEvent event ) {
		// instanciation de la fabrique
		daoFactory = DAOFactory.getInstance();
		
		// récupération du contexte APPLICATION
		ServletContext servletContext = event.getServletContext();
		
		// stockage de la fabrique dans un attribut du scope APPLICATION
		servletContext.setAttribute( ATT_DAO_FACTORY_ID, daoFactory );
	}

	@Override
	public void contextDestroyed( ServletContextEvent arg0 ) {		
	}
}

package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory {
	
	private static final String FICHIER_PROPERTIES = "/dao/dao.properties";
	private static final String PROPERTY_URL       = "url";
	private static final String PROPERTY_DRIVER    = "driver";
	private static final String PROPERTY_USER      = "nomUtilisateur"; 
	private static final String PROPERTY_PASSWD    = "motDePasse";

	private String url;
	private String userName;
	private String password;
	
	// instance unique de l'objet Connection à la base (singleton)
	private Connection connection;

	// instance unique de l'objet DAO de la table Utilisateur de la base (singleton)
	private UtilisateurDao utilisateurDAO;
	
	// constructeur privé de la fabrique => la fabrique respecte une part du Design Pattern Singleton
	private DAOFactory( String url, String userName, String password ) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	// méthode principale de la fabrique : lit le fichier de configuration (properties) et
	// fournit une instance de la fabrique pouvant délivrer un objet DAO pour chaque table de la BD
	// NOTE :	la fabrique de DAOs correspond bien à un singleton : 
    // 		    UNE SEULE FABRIQUE créée au lancement de l'appli par cette méthode 
    //		    getInstance() appelée par le traitement de la notification de démarrage
    //		    implémenté dans la classe InitialisationDaoFactory, méthode contextInitialized().
	public static DAOFactory getInstance() throws DAOConfigurationException {
		
		Properties properties = new Properties();
		String url;
		String driver;
		String nomUtilisateur;
		String motDePasse;
		
		// lecture du fichier de configuration
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );
		
		if ( fichierProperties == null ) {
			throw new DAOConfigurationException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
		}
		try {
			properties.load( fichierProperties ) ;
			url = properties.getProperty( PROPERTY_URL );
			driver =  properties.getProperty( PROPERTY_DRIVER );
			nomUtilisateur = properties.getProperty( PROPERTY_USER );
			motDePasse = properties.getProperty( PROPERTY_PASSWD );
		} catch ( IOException e ) {
			throw new DAOConfigurationException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES , e );
		}
		
		// contrôle du chargement du driver JDBC
		try {
			Class.forName( driver );
		} catch ( ClassNotFoundException e ) {
			throw new DAOConfigurationException( "Le driver JDBC est introuvable dans le classpath.", e );
		}
		
		// instanciation privée, retourne la référence de l'instance de la Factory
		System.out.println( "Instanciation de la fabrique DAOfactory" );
		DAOFactory instance = new DAOFactory( url, nomUtilisateur, motDePasse );
		return instance;
	}
	
	// méthode privée qui fournit une connection à la BD
	// 		pas de mot-clé private <=> private au niveau PACKAGE
	//		<=> public pour les classes du package
	//		<=> et private pour toutes les autres classes
	Connection getConnection() throws SQLException {
		// La méthode getConnection() est appelée par les méthodes CRUD des différents singletons
		//		DAO de l'appli (un par table) : les méthodes creer() et trouver()
		//		de la classe UtilisateurDaoImpl  
		if ( connection == null ) {
			System.out.println( "instanciation de la ressource Connection à la base : " + url );
			connection = DriverManager.getConnection( url, userName, password );
		}
		return connection;
	}
	
	// méthode publique fournissant une instance unique de DAO pour la table Utilisateur
	// => l'objet DAO fourni est un singleton
	public UtilisateurDao getUtilisateurDao() {
		if ( utilisateurDAO == null ) {
			System.out.println( "instanciation du DAO table Utilisateur" );
			utilisateurDAO = new UtilisateurDaoImpl( this);
		}
		return utilisateurDAO;
	}
	
	// méthodes fournissant une instance de DAO pour les autres tables
 // une véritable fabrique de DAOs devrait disposer d'un type abstrait DAO<T> qui définit des
	// méthodes CRUD abstraites, dérivé dans les classes DAO spécifiques de l'accès à chaque table
// ...

}

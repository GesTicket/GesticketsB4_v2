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
	
	// instance unique de l'objet Connection � la base (singleton)
	private Connection connection;

	// instance unique de l'objet DAO de la table Utilisateur de la base (singleton)
	private UtilisateurDao utilisateurDAO;
	
	// constructeur priv� de la fabrique => la fabrique respecte une part du Design Pattern Singleton
	private DAOFactory( String url, String userName, String password ) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	// m�thode principale de la fabrique : lit le fichier de configuration (properties) et
	// fournit une instance de la fabrique pouvant d�livrer un objet DAO pour chaque table de la BD
	// NOTE :	la fabrique de DAOs correspond bien � un singleton : 
    // 		    UNE SEULE FABRIQUE cr��e au lancement de l'appli par cette m�thode 
    //		    getInstance() appel�e par le traitement de la notification de d�marrage
    //		    impl�ment� dans la classe InitialisationDaoFactory, m�thode contextInitialized().
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
		
		// contr�le du chargement du driver JDBC
		try {
			Class.forName( driver );
		} catch ( ClassNotFoundException e ) {
			throw new DAOConfigurationException( "Le driver JDBC est introuvable dans le classpath.", e );
		}
		
		// instanciation priv�e, retourne la r�f�rence de l'instance de la Factory
		System.out.println( "Instanciation de la fabrique DAOfactory" );
		DAOFactory instance = new DAOFactory( url, nomUtilisateur, motDePasse );
		return instance;
	}
	
	// m�thode priv�e qui fournit une connection � la BD
	// 		pas de mot-cl� private <=> private au niveau PACKAGE
	//		<=> public pour les classes du package
	//		<=> et private pour toutes les autres classes
	Connection getConnection() throws SQLException {
		// La m�thode getConnection() est appel�e par les m�thodes CRUD des diff�rents singletons
		//		DAO de l'appli (un par table) : les m�thodes creer() et trouver()
		//		de la classe UtilisateurDaoImpl  
		if ( connection == null ) {
			System.out.println( "instanciation de la ressource Connection � la base : " + url );
			connection = DriverManager.getConnection( url, userName, password );
		}
		return connection;
	}
	
	// m�thode publique fournissant une instance unique de DAO pour la table Utilisateur
	// => l'objet DAO fourni est un singleton
	public UtilisateurDao getUtilisateurDao() {
		if ( utilisateurDAO == null ) {
			System.out.println( "instanciation du DAO table Utilisateur" );
			utilisateurDAO = new UtilisateurDaoImpl( this);
		}
		return utilisateurDAO;
	}
	
	// m�thodes fournissant une instance de DAO pour les autres tables
 // une v�ritable fabrique de DAOs devrait disposer d'un type abstrait DAO<T> qui d�finit des
	// m�thodes CRUD abstraites, d�riv� dans les classes DAO sp�cifiques de l'acc�s � chaque table
// ...

}

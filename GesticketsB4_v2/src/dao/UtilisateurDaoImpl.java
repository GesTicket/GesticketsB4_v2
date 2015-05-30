package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {

	// requêtes SQL
	private static final String SQL_SELECT_PAR_EMAIL = 
			"SELECT id, email, nom, mot_de_passe, date_inscription "
	        + "FROM Utilisateur "
			+ "WHERE email = ?";
	private static final String SQL_INSERT = 
			"INSERT INTO Utilisateur "
			+ "(email, mot_de_passe, nom, date_inscription) "
			+ "VALUES (?, ?, ?, NOW())";


	// référence de la fabrique de DAOs
	private DAOFactory daoFactory;
	
	public UtilisateurDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

    /* Implémentation de la méthode trouver() définie dans l'interface UtilisateurDao */

    @Override

    public Utilisateur trouver( String email ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
			System.out.println( "preparedStatement OK" );
            // exécution de la requête SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "executeQuery OK" );
			// mapping du ResultSet dans un bean Utilisateur
			if ( resultSet.next() ) { // positionnement sur la première entité obtenue
				utilisateur = new Utilisateur();
				utilisateur.setId( resultSet.getLong( "id" ) );
				utilisateur.setEmail( resultSet.getString( "email" ) );
				utilisateur.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
				utilisateur.setNom( resultSet.getString( "nom" ) );
				utilisateur.setDateInscription( resultSet.getTimestamp( "date_inscription" ) );
			}
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }

        return utilisateur;
        
    }


    /* Implémentation de la méthode creer() définie dans l'interface UtilisateurDao */

    @Override

    public void creer( Utilisateur utilisateur ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getNom() );
            // exécution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "INSERT " + utilisateur.getNom() + " statut=" + statut );
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
			// le traitement qui suit n'est effectué que si pas d'exception (<=> else <=> statut != 0)
			// récupération de l'id auto-généré pour cet utilisateur
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                // Puis initialisation de la propriété id du bean Utilisateur avec sa valeur
                utilisateur.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermetureRessourcesSQL( connexion, preparedStatement, valeursAutoGenerees );
        }
        
    }

    // Initialise la requête préparée basée sur la connexion passée en argument,
    //avec la requête SQL et les objets donnés.
    public static PreparedStatement initialisationRequetePreparee( Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets ) throws SQLException {
        PreparedStatement preparedStatement = connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
        for ( int i = 0; i < objets.length; i++ ) {
            preparedStatement.setObject( i + 1, objets[i] );
        }
        return preparedStatement;
    }

     // Simple méthode utilitaire permettant de faire la correspondance (le mapping)
     // entre une ligne issue de la table des utilisateurs (un ResultSet) et un bean Utilisateur.
    private static Utilisateur map( ResultSet resultSet ) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId( resultSet.getLong( "id" ) );
        utilisateur.setEmail( resultSet.getString( "email" ) );
        utilisateur.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
        utilisateur.setNom( resultSet.getString( "nom" ) );
        utilisateur.setDateInscription( resultSet.getTimestamp( "date_inscription" ) );
        return utilisateur;
    }

	private void fermetureRessourcesSQL( Connection connection, PreparedStatement preparedStatement, ResultSet resultSet ) {
		if ( resultSet != null ) {
			try {
				resultSet.close();
			} catch ( SQLException e ) {
				System.out.println( "Echec de la fermeture du ResultSet : "+ e.getMessage() );
			}
		}
		if ( preparedStatement != null ) {
			try {
				preparedStatement.close();
			} catch ( SQLException e ) {
				System.out.println( "Echec de fermeture du PreparedStatement : "+ e.getMessage() );
			}
		}
		// Pas de fermeture de la ressource Connection si OK
		try {
			if ( connection.isValid(1) ) { // 1 seconde
				System.out.println( "Maintien de la ressource Connection." );
			} else {
				connection.close();
			}
		} catch ( SQLException e ) {
			System.out.println( "Echec du maintien de la Connection : "+ e.getMessage() );
		}		
	}

}
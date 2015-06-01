package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.Ticket;

public class TicketDaoImpl implements TicketDao {

	// requêtes SQL
	private static final String SQL_SELECT_PAR_TITRE = 
			"SELECT id, titre, description, dateCreation "
	        + "FROM Ticket "
			+ "WHERE titre = ?";
	private static final String SQL_INSERT = 
			"INSERT INTO Ticket "
			+ "(titre, description, dateCreation) "
			+ "VALUES (?, ?, NOW())";


	// référence de la fabrique de DAOs
	private DAOFactory daoFactory;
	
	public TicketDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

    /* Implémentation de la méthode trouver() définie dans l'interface TicketDao */

    @Override

    public Ticket trouver( String titre ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Ticket ticket = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_TITRE, false, titre );
			System.out.println( "preparedStatement OK" );
            // exécution de la requête SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "executeQuery OK" );
			// mapping du ResultSet dans un bean Ticket
			if ( resultSet.next() ) { // positionnement sur la première entité obtenue
				ticket = new Ticket();
				ticket.setId( resultSet.getLong( "id" ) );
				ticket.setTitre( resultSet.getString( "titre" ) );
				ticket.setDescription( resultSet.getString( "description" ) );
				ticket.setDateCreation( resultSet.getTimestamp( "dateCreation" ) );
			}
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }

        return ticket;
        
    }


    /* Implémentation de la méthode creer() définie dans l'interface UtilisateurDao */

    @Override

    public void creer( Ticket ticket ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, ticket.getTitre(), ticket.getDescription());
            // exécution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "INSERT " + ticket.getTitre() + " statut=" + statut );
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création du ticket, aucune ligne ajoutée dans la table." );
            }
			// le traitement qui suit n'est effectué que si pas d'exception (<=> else <=> statut != 0)
			// récupération de l'id auto-généré pour cet utilisateur
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                // Puis initialisation de la propriété id du bean Utilisateur avec sa valeur
                ticket.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création du ticket en base, aucun ID auto-généré retourné." );
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
     // entre une ligne issue de la table des tickets (un ResultSet) et un bean Ticket.
    private static Ticket map( ResultSet resultSet ) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId( resultSet.getLong( "id" ) );
        ticket.setTitre( resultSet.getString( "titre" ) );
        ticket.setDescription( resultSet.getString( "description" ) );
        ticket.setDateCreation( resultSet.getTimestamp( "dateCreation" ) );
        return ticket;
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
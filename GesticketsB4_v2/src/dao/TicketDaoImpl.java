package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.Ticket;
//import beans.Utilisateur;

public class TicketDaoImpl implements TicketDao {

	// requêtes SQL
	private static final String SQL_SELECT = 
			"SELECT * FROM Ticket";
	
	private static final String SQL_SELECT_PAR_ATT = 
			"SELECT id, titre, description, dateCreation FROM Ticket WHERE id like '%contentSearch%' or titre like '%contentSearch%' or descritpion like '%contentSearch%' or dateCreation like '%contentSearch%'";
	
	private static final String SQL_SELECT_PAR_MOT_CLE = 
			//"SELECT id, titre, description, dateCreation FROM Ticket WHERE titre like ? or descritpion like ? or dateCreation like ?";
			"SELECT id, titre, description, dateCreation FROM Ticket WHERE titre like '%?%' or descritpion like '%?%'";

	private static final String SQL_INSERT = 
			"INSERT INTO Ticket (titre, description, dateCreation) VALUES (?, ?, NOW())";
	
	private static final String SQL_DELETE = 
			"DELETE FROM Ticket WHERE id = ?";
			
	// référence de la fabrique de DAOs
	private DAOFactory daoFactory;
	
	public TicketDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

    /* Implémentation de la méthode trouver() définie dans l'interface TicketDao */

    /* Implémentation de la méthode creer() définie dans l'interface UtilisateurDao */

    @Override

    public void creerTicket( Ticket ticket ) throws IllegalArgumentException, DAOException {

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
    
    private Ticket trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Ticket ticket = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
            	ticket = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }

        return ticket;
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
	
		
	@Override
	public Ticket rechercherTicket (Ticket ticket) throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        
        try {
        	connexion = daoFactory.getConnection();
        	preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_ATT, false, ticket );
			System.out.println( "initialisation requête 'trouverTicket()'" );
			resultSet = preparedStatement.executeQuery();
			System.out.println( "execution requête 'trouverTicket()'" );
			if ( resultSet.next() ) { // positionnement sur la première entité obtenue
				ticket = map( resultSet );
			}
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return ticket;
	}

	
@Override
public List<Ticket> rechercherTicketsMotCle ( String motCle ) throws DAOException {
	
	Connection connexion = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    List<Ticket> mapRechercheTickets = new ArrayList<Ticket>();
   
    
    try {
    	connexion = daoFactory.getConnection();
    	preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_MOT_CLE, false, motCle, motCle );
		System.out.println( "initialisation requête 'rechercheTickets()'" );
		resultSet = preparedStatement.executeQuery();
		System.out.println( "execution requête 'rechercheTickets()'" );
		if ( resultSet.next() ) { // positionnement sur la première entité obtenue
        	mapRechercheTickets.add( map( resultSet ) );
		}
    } catch ( SQLException e ) {
        throw new DAOException( e );
    } finally {
		fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
    }
    return mapRechercheTickets;
}
	
	
	@Override
	public List<Ticket> listerTickets() throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Ticket> mapTickets = new ArrayList<Ticket>();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
            	mapTickets.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
        	fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return mapTickets;
	}
	
	public void supprimerTicket( Ticket ticket ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête DELETE
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE, true, ticket.getId() );
			System.out.println( "initialisation requête 'SupprimerTicket()'" );
            // exécution de la requête DELETE
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requête 'SupprimerTicket()'" );
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression de le ticket, aucune ligne supprimée de la table." );
            } else {
            	ticket.setId( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
	}

}
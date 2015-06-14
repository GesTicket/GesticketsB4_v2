package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.Ticket;
import beans.Utilisateur;

public class TicketDaoImpl implements TicketDao {

	// requ�tes SQL
	private static final String SQL_SELECT = 
			"SELECT * FROM Ticket";
	
	private static final String SQL_SELECT_PAR_ID = 
			"SELECT id, titre, description, dateCreation FROM Ticket WHERE id = ?";
	
	private static final String SQL_SELECT_PAR_TITRE = 
			"SELECT id, titre, description, dateCreation FROM Ticket WHERE titre = ?";
	    
	private static final String SQL_SELECT_PAR_DESCRIPTION =
			"SELECT id, titre, description, dateCreation FROM Ticket WHERE description= ?";
	
	private static final String SQL_INSERT = 
			"INSERT INTO Ticket (titre, description, dateCreation) VALUES (?, ?, NOW())";
	
	private static final String SQL_DELETE = 
			"DELETE FROM Ticket WHERE id = ?";
			
	// r�f�rence de la fabrique de DAOs
	private DAOFactory daoFactory;
	
	public TicketDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

    /* Impl�mentation de la m�thode trouver() d�finie dans l'interface TicketDao */

    @Override

    public Ticket trouverTitre( String titre ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Ticket ticket = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_TITRE, false, titre );
			System.out.println( "preparedStatement OK" );
            // ex�cution de la requ�te SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "executeQuery OK" );
			// mapping du ResultSet dans un bean Ticket
			if ( resultSet.next() ) { // positionnement sur la premi�re entit� obtenue
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
    
    

    /* Impl�mentation de la m�thode creer() d�finie dans l'interface UtilisateurDao */

    @Override

    public void creerTicket( Ticket ticket ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, ticket.getTitre(), ticket.getDescription());
            // ex�cution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "INSERT " + ticket.getTitre() + " statut=" + statut );
            /* Analyse du statut retourn� par la requ�te d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la cr�ation du ticket, aucune ligne ajout�e dans la table." );
            }
			// le traitement qui suit n'est effectu� que si pas d'exception (<=> else <=> statut != 0)
			// r�cup�ration de l'id auto-g�n�r� pour cet utilisateur
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                // Puis initialisation de la propri�t� id du bean Utilisateur avec sa valeur
                ticket.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "�chec de la cr�ation du ticket en base, aucun ID auto-g�n�r� retourn�." );
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
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de donn�es retourn�e dans le ResultSet */
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
    // Initialise la requ�te pr�par�e bas�e sur la connexion pass�e en argument,
    //avec la requ�te SQL et les objets donn�s.
    public static PreparedStatement initialisationRequetePreparee( Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets ) throws SQLException {
        PreparedStatement preparedStatement = connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
        for ( int i = 0; i < objets.length; i++ ) {
            preparedStatement.setObject( i + 1, objets[i] );
        }
        return preparedStatement;
    }

     // Simple m�thode utilitaire permettant de faire la correspondance (le mapping)
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
	public Ticket trouverDescription(String description) throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Ticket ticket = null;
        
        try {
        	connexion = daoFactory.getConnection();
        	preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_DESCRIPTION, false, description );
			System.out.println( "initialisation requ�te 'trouverDescription()'" );
			resultSet = preparedStatement.executeQuery();
			System.out.println( "execution requ�te 'trouverDescription()'" );
			if ( resultSet.next() ) { // positionnement sur la premi�re entit� obtenue
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
	public Ticket trouverTicket(long id) throws DAOException {
		// TODO Auto-generated method stub
		return trouver(SQL_SELECT_PAR_ID);
	}
	
	@Override
	public List<Ticket> listerTickets() throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Ticket> tickets = new ArrayList<Ticket>();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
            	tickets.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
        	fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return tickets;
	}
	
	public void supprimerTicket( Ticket ticket ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te DELETE
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE, true, ticket.getId() );
			System.out.println( "initialisation requ�te 'SupprimerTicket()'" );
            // ex�cution de la requ�te DELETE
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requ�te 'SupprimerTicket()'" );
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la suppression de le ticket, aucune ligne supprim�e de la table." );
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
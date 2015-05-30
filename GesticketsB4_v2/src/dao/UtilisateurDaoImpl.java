package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {

	// requ�tes SQL
	private static final String SQL_SELECT_PAR_EMAIL = 
			"SELECT id, email, nom, mot_de_passe, date_inscription "
	        + "FROM Utilisateur "
			+ "WHERE email = ?";
	private static final String SQL_INSERT = 
			"INSERT INTO Utilisateur "
			+ "(email, mot_de_passe, nom, date_inscription) "
			+ "VALUES (?, ?, ?, NOW())";


	// r�f�rence de la fabrique de DAOs
	private DAOFactory daoFactory;
	
	public UtilisateurDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

    /* Impl�mentation de la m�thode trouver() d�finie dans l'interface UtilisateurDao */

    @Override

    public Utilisateur trouver( String email ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
			System.out.println( "preparedStatement OK" );
            // ex�cution de la requ�te SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "executeQuery OK" );
			// mapping du ResultSet dans un bean Utilisateur
			if ( resultSet.next() ) { // positionnement sur la premi�re entit� obtenue
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


    /* Impl�mentation de la m�thode creer() d�finie dans l'interface UtilisateurDao */

    @Override

    public void creer( Utilisateur utilisateur ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getNom() );
            // ex�cution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "INSERT " + utilisateur.getNom() + " statut=" + statut );
            /* Analyse du statut retourn� par la requ�te d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la cr�ation de l'utilisateur, aucune ligne ajout�e dans la table." );
            }
			// le traitement qui suit n'est effectu� que si pas d'exception (<=> else <=> statut != 0)
			// r�cup�ration de l'id auto-g�n�r� pour cet utilisateur
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                // Puis initialisation de la propri�t� id du bean Utilisateur avec sa valeur
                utilisateur.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "�chec de la cr�ation de l'utilisateur en base, aucun ID auto-g�n�r� retourn�." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermetureRessourcesSQL( connexion, preparedStatement, valeursAutoGenerees );
        }
        
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
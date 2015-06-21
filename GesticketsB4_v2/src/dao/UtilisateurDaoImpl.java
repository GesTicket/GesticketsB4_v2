package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {

	// requ�tes SQL
	private static final String SQL_SELECT_PAR_LOGIN = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur WHERE login = ?";
	private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur WHERE email = ?";
	private static final String SQL_SELECT           = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur ORDER BY id";
	private static final String SQL_SELECT_PAR_ID    = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur WHERE id = ?";
	private static final String SQL_INSERT           = "INSERT INTO Utilisateur (nom, prenom, email, login, mot_de_passe, date_inscription, profil) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
	private static final String SQL_UPDATE_PAR_ID    = "UPDATE Utilisateur set nom=?, prenom=?, email=?, login=?, profil=? WHERE id = ?";
	private static final String SQL_DELETE_PAR_ID    = "DELETE FROM Utilisateur WHERE id = ?";
	// scope SESSION
	public static final String ATT_SESSION_USER      = "sessionUtilisateur";

	// r�f�rence de la fabrique de DAOs
	private DAOFactory daoFactory;

	public UtilisateurDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

    /* Impl�mentation de la m�thode trouverLogin() d�finie dans l'interface UtilisateurDao */

    @Override
    public Utilisateur trouverLogin( String login ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_LOGIN, false, login );
			System.out.println( "initialisation requ�te 'trouverLogin()'" );
            // ex�cution de la requ�te SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "execution requ�te 'trouverLogin()'" );
			// mapping du ResultSet dans un bean Utilisateur
			if ( resultSet.next() ) { // positionnement sur la premi�re entit� obtenue
				utilisateur = map( resultSet );
			}
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return utilisateur;
    }

    /* Impl�mentation de la m�thode trouverEmail() d�finie dans l'interface UtilisateurDao */

    @Override
    public Utilisateur trouverEmail( String email ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
			System.out.println( "initialisation requ�te 'trouverEmail()'" );
            // ex�cution de la requ�te SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "execution requ�te 'trouverEmail()'" );
			// mapping du ResultSet dans un bean Utilisateur
			if ( resultSet.next() ) { // positionnement sur la premi�re entit� obtenue
				utilisateur = map( resultSet );
			}
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return utilisateur;
    }

    /* Impl�mentation de la m�thode creerUtilisateur() d�finie dans l'interface UtilisateurDao */

    @Override
    public void creerUtilisateur( Utilisateur utilisateur ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, 
            		utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), 
            		utilisateur.getLogin(), utilisateur.getMotDePasse(), utilisateur.getProfil() );
            System.out.println( "initialisation requ�te 'creer(utilisateur)'" );
            // ex�cution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requ�te 'creer(utilisateur)'" );
			System.out.println( "INSERT " + utilisateur.getNom() + " statut=" + statut );
            // Analyse du statut retourn� par la requ�te d'insertion
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

    /* Impl�mentation de la m�thode modifierUtilisateur() d�finie dans l'interface UtilisateurDao */

    @Override
    public void modifierUtilisateur( Utilisateur utilisateur ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_PAR_ID, true, 
            		utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), 
            		utilisateur.getLogin(), utilisateur.getProfil(), utilisateur.getId() );
            System.out.println( "initialisation requ�te 'modifier(utilisateur)'" );
            // ex�cution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requ�te 'modifier(utilisateur)'" );
			System.out.println( "UPDATE " + utilisateur.getNom() + " statut=" + statut );
            // Analyse du statut retourn� par la requ�te d'insertion
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la modification de l'utilisateur." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermetureRessourcesSQL( connexion, preparedStatement, valeursAutoGenerees );
        }
    }
    
    /* Impl�mentation de la m�thode trouverUtilisateur() d�finie dans l'interface UtilisateurDao */

    @Override
    public Utilisateur trouverUtilisateur(long id) throws DAOException {
    	return trouver( SQL_SELECT_PAR_ID, id );
    }
	
    /* Impl�mentation de la m�thode listerUtilisateurs() d�finie dans l'interface UtilisateurDao */

    @Override
	public List<Utilisateur> listerUtilisateurs() throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Utilisateur> mapUtilisateurs = new ArrayList<Utilisateur>();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                mapUtilisateurs.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
        	fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return mapUtilisateurs;
    }
	
    /* Impl�mentation de la m�thode supprimerUtilisateur() d�finie dans l'interface UtilisateurDao */

    @Override
	public void supprimerUtilisateur( Utilisateur utilisateur ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
        	// r�cup�ration de la connexion � la base, via la fabrique
            connexion = daoFactory.getConnection();
            // pr�paration de la requ�te DELETE
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, utilisateur.getId() );
			System.out.println( "initialisation requ�te 'supprimer(Utilisateur)'" );
            // ex�cution de la requ�te DELETE
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requ�te 'supprimer(Utilisateur)'" );
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la suppression de l'utilisateur, aucune ligne supprim�e de la table." );
            } else {
                utilisateur.setId( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
	}

    // M�thode g�n�rique utilis�e pour retourner un utilisateur depuis la base de donn�es,
    // correspondant � la requ�te SQL donn�e prenant en param�tres les objets pass�s en argument.
    
    private Utilisateur trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /* Pr�paration de la requ�te avec les objets pass�s en arguments (ici, uniquement un id) et ex�cution. */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de donn�es retourn�e dans le ResultSet */
            if ( resultSet.next() ) {
                utilisateur = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }

        return utilisateur;
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
        utilisateur.setNom( resultSet.getString( "nom" ) );
        utilisateur.setPrenom( resultSet.getString( "prenom" ) );
        utilisateur.setEmail( resultSet.getString( "email" ) );
        utilisateur.setLogin( resultSet.getString( "login" ) );
        utilisateur.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
        utilisateur.setProfil(resultSet.getString( "profil" ) );
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
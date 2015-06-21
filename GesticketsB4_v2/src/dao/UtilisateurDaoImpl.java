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

	// requêtes SQL
	private static final String SQL_SELECT_PAR_LOGIN = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur WHERE login = ?";
	private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur WHERE email = ?";
	private static final String SQL_SELECT           = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur ORDER BY id";
	private static final String SQL_SELECT_PAR_ID    = "SELECT id, nom, prenom, email, login, mot_de_passe, date_inscription, profil FROM Utilisateur WHERE id = ?";
	private static final String SQL_INSERT           = "INSERT INTO Utilisateur (nom, prenom, email, login, mot_de_passe, date_inscription, profil) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
	private static final String SQL_UPDATE_PAR_ID    = "UPDATE Utilisateur set nom=?, prenom=?, email=?, login=?, profil=? WHERE id = ?";
	private static final String SQL_DELETE_PAR_ID    = "DELETE FROM Utilisateur WHERE id = ?";
	// scope SESSION
	public static final String ATT_SESSION_USER      = "sessionUtilisateur";

	// référence de la fabrique de DAOs
	private DAOFactory daoFactory;

	public UtilisateurDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

    /* Implémentation de la méthode trouverLogin() définie dans l'interface UtilisateurDao */

    @Override
    public Utilisateur trouverLogin( String login ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_LOGIN, false, login );
			System.out.println( "initialisation requête 'trouverLogin()'" );
            // exécution de la requête SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "execution requête 'trouverLogin()'" );
			// mapping du ResultSet dans un bean Utilisateur
			if ( resultSet.next() ) { // positionnement sur la première entité obtenue
				utilisateur = map( resultSet );
			}
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return utilisateur;
    }

    /* Implémentation de la méthode trouverEmail() définie dans l'interface UtilisateurDao */

    @Override
    public Utilisateur trouverEmail( String email ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête SELECT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
			System.out.println( "initialisation requête 'trouverEmail()'" );
            // exécution de la requête SELECT
            resultSet = preparedStatement.executeQuery();
			System.out.println( "execution requête 'trouverEmail()'" );
			// mapping du ResultSet dans un bean Utilisateur
			if ( resultSet.next() ) { // positionnement sur la première entité obtenue
				utilisateur = map( resultSet );
			}
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
        return utilisateur;
    }

    /* Implémentation de la méthode creerUtilisateur() définie dans l'interface UtilisateurDao */

    @Override
    public void creerUtilisateur( Utilisateur utilisateur ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, 
            		utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), 
            		utilisateur.getLogin(), utilisateur.getMotDePasse(), utilisateur.getProfil() );
            System.out.println( "initialisation requête 'creer(utilisateur)'" );
            // exécution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requête 'creer(utilisateur)'" );
			System.out.println( "INSERT " + utilisateur.getNom() + " statut=" + statut );
            // Analyse du statut retourné par la requête d'insertion
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

    /* Implémentation de la méthode modifierUtilisateur() définie dans l'interface UtilisateurDao */

    @Override
    public void modifierUtilisateur( Utilisateur utilisateur ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête INSERT
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_PAR_ID, true, 
            		utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), 
            		utilisateur.getLogin(), utilisateur.getProfil(), utilisateur.getId() );
            System.out.println( "initialisation requête 'modifier(utilisateur)'" );
            // exécution de l'insertion
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requête 'modifier(utilisateur)'" );
			System.out.println( "UPDATE " + utilisateur.getNom() + " statut=" + statut );
            // Analyse du statut retourné par la requête d'insertion
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la modification de l'utilisateur." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermetureRessourcesSQL( connexion, preparedStatement, valeursAutoGenerees );
        }
    }
    
    /* Implémentation de la méthode trouverUtilisateur() définie dans l'interface UtilisateurDao */

    @Override
    public Utilisateur trouverUtilisateur(long id) throws DAOException {
    	return trouver( SQL_SELECT_PAR_ID, id );
    }
	
    /* Implémentation de la méthode listerUtilisateurs() définie dans l'interface UtilisateurDao */

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
	
    /* Implémentation de la méthode supprimerUtilisateur() définie dans l'interface UtilisateurDao */

    @Override
	public void supprimerUtilisateur( Utilisateur utilisateur ) throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
        	// récupération de la connexion à la base, via la fabrique
            connexion = daoFactory.getConnection();
            // préparation de la requête DELETE
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, utilisateur.getId() );
			System.out.println( "initialisation requête 'supprimer(Utilisateur)'" );
            // exécution de la requête DELETE
            int statut = preparedStatement.executeUpdate();
			System.out.println( "execution requête 'supprimer(Utilisateur)'" );
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression de l'utilisateur, aucune ligne supprimée de la table." );
            } else {
                utilisateur.setId( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
			fermetureRessourcesSQL( connexion, preparedStatement, resultSet );
        }
	}

    // Méthode générique utilisée pour retourner un utilisateur depuis la base de données,
    // correspondant à la requête SQL donnée prenant en paramètres les objets passés en argument.
    
    private Utilisateur trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /* Préparation de la requête avec les objets passés en arguments (ici, uniquement un id) et exécution. */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
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
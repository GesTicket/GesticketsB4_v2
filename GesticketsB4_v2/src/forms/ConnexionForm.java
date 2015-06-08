package forms;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import dao.DAOException;
import dao.DAOFactory;
import dao.UtilisateurDao;
import beans.Utilisateur;

public final class ConnexionForm {

    private static final String CHAMP_LOGIN      = "login";
    private static final String CHAMP_PASS       = "motdepasse";
	private final static String ALGO_CHIFFREMENT = "SHA-256";

	// une cha�ne donnant le r�sultat de l'inscription
    private String resultat;

    // stockage de la r�f�rence de l'objet DAO pour acc�der � la table utilisateur de la base
	private UtilisateurDao utilisateurDao;

    // MAP (champ, message) des erreurs de saisie d�tect�es en validant les saisies
    private Map<String, String> erreurs = new HashMap<String, String>();

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

	public ConnexionForm( DAOFactory daoFactory ) {
		// r�cup�ration de l'objet DAO � partir de la fabrique donn�e au constructeur
		utilisateurDao = daoFactory.getUtilisateurDao();
	}

    public void connecterUtilisateur( HttpServletRequest request ) {

    	// R�cup�ration de la session depuis la requ�te
    	HttpSession session = request.getSession(true);

		// enregistrement dans une variable de session
		session.setAttribute("sessionLogin", getValeurChamp( request, CHAMP_LOGIN ) );
		session.setAttribute("sessionPass", getValeurChamp( request, CHAMP_PASS ) );

		String login = getValeurChamp( request, CHAMP_LOGIN );
		String motdepasse = getValeurChamp( request, CHAMP_PASS );
		
		String password_crypte = null;
		// chiffrement du mdp avec la librairie Jasypt
		// fichier JAR dans WEB-INF\lib
		// algo SHA-256, retourne une cha�ne de 56 caract�res en Base64
		ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
		passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
		passwordEncryptor.setPlainDigest(false);

		if ( login == null ) {
			// si login vide
		   erreurs.put( "login", "Le login est obligatoire." );
		   System.out.println( "Login non fourni" );
		} else {
			if ( login.length() < 3 ) {
				// si login incorrect < 3 caract�res
				erreurs.put( "login", "Le login doit avoir au moins 3 caract�res." );
				System.out.println( "Login trop court" );
			} else {
				   if ( motdepasse == null ) {
						// si mot de passe vide
					   erreurs.put( "motdepasse", "Le mot de passe est obligatoire." );
					   System.out.println( "MDP non fourni" );
				   } else {
						if ( motdepasse.length() < 3 ) {
							// si mot de passe incorrect < 3 caract�res
							erreurs.put( "motdepasse", "Le mot de passe doit avoir au moins 3 caract�res." );
							System.out.println( "MDP trop court" );
						} else {
							// recherche du login dans la table
							if ( utilisateurDao.trouverLogin( login ) == null ) {
								// si login inconnu
								erreurs.put( "login", "le login est inconnu." );
								System.out.println( "login inconnu" );
							} else {
								// sinon login connu
								System.out.println( "login OK" );
						 		// intancie un bean Client et retourne sa r�f�rence en sortie
						    	Utilisateur utilisateur = utilisateurDao.trouverLogin( login );
								// comparaison mot de passe saisi et mot de passe chiffr� stock� dans la table
								password_crypte = utilisateur.getMotDePasse();
								if ( passwordEncryptor.checkPassword( motdepasse, password_crypte ) ) {
									//si mots de passe identiques
									System.out.println( "login et mot de passe OK" );
									// enregistrement du bean dans une variable de session
									session.setAttribute("sessionUtilisateur", utilisateur );
								} else {
									// sinon mots de passe diff�rents
									erreurs.put( "motdepasse", "le mot de passe est �ronn�" );
									System.out.println( "mot de passe erron�" );
								}
						   }
					  }
				 }
			}
		}

		try { // l'acc�s en BD peut g�n�rer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				// enregistrement dans une variable de session
				resultat = "Succ�s de la connexion.";
			} else {
				resultat = "Echec de la connexion.";
			}
		
		} catch (DAOException e) {
			resultat = "Echec de la connexion, une erreur est survenue, r�essayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// diff�rencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		
		System.out.println( "R�sultat : " + resultat );
	}

    // M�thode utilitaire qui retourne null si un champ est vide, et son contenu sinon.
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}
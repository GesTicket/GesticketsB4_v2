package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import dao.DAOException;
import dao.UtilisateurDao;
import beans.Utilisateur;

public class ModificationUtilisateurForm {
	// classe m�tier qui traite les saisies du formulaire d'inscription
	
	private final static String ALGO_CHIFFREMENT = "SHA-256";
	
    private static final String CHAMP_ID     = "id";
    private static final String CHAMP_NOM    = "nom";
    private static final String CHAMP_PRENOM = "prenom";
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_LOGIN  = "login";
    private static final String CHAMP_PASS   = "motdepasse";
    private static final String CHAMP_CONF   = "confirmation";
    private static final String CHAMP_PROFIL = "profil";

    // le bean qui va �tre cr�� si tout est correct
    private Utilisateur utilisateur;
    
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

	public ModificationUtilisateurForm( UtilisateurDao utilisateurDao ) {
		// r�cup�ration de l'objet DAO � partir de la fabrique donn�e au constructeur
		this.utilisateurDao = utilisateurDao;
	}

	public Utilisateur modifierUtilisateur( HttpServletRequest request ) {
				
		utilisateur = new Utilisateur();
		
		// r�cup�ration et validation des champs de saisie, maj du bean Utilisateur	
		Long id = Long.parseLong( request.getParameter( CHAMP_ID ) );
		utilisateur.setId( id );
		validerNom( getValeurChamp( request, CHAMP_NOM ) );
		validerPrenom( getValeurChamp( request, CHAMP_PRENOM ) );
		validerEmail( getValeurChamp( request, CHAMP_EMAIL ) );
		validerLogin( getValeurChamp (request, CHAMP_LOGIN ) );
		validerMotDePasse( getValeurChamp( request, CHAMP_PASS ), getValeurChamp( request, CHAMP_CONF ) );
		validerProfil( getValeurChamp (request, CHAMP_PROFIL ) );
		
		try { // l'acc�s en BD peut g�n�rer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				System.out.println( "id � modifier : " + getValeurChamp( request, CHAMP_ID ) );
				utilisateurDao.modifierUtilisateur( utilisateur ); // dans la base, via le DAO
				resultat = "Succ�s de la modification.";
			} else {
				resultat = "Echec de la modification.";
			}
		} catch (DAOException e) {
			resultat = "Echec de l'inscription, une erreur est survenue, r�essayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// diff�rencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		System.out.println( "R�sultat : " + resultat );
		return utilisateur;
	}

	private void validerNom( String nom ) {
		if ( nom != null && nom.length() >=3 ) { // param�tre non vide et au moins 3 car
			utilisateur.setNom( nom );
			System.out.println( "Nom User OK" );
		} else {
			erreurs.put( "nom", "Le nom d'utlisateur doit avoir au moins 3 caract�res." );
			System.out.println( "Nom User trop court" );
		}
	}
	
	private void validerPrenom( String prenom ) {
		if ( prenom != null ) { // param�tre non vide
			utilisateur.setPrenom( prenom );
			System.out.println( "Pr�nom User OK" );
		}
	}
	private void validerEmail( String email) {
		if ( email != null) { // param�tre non vide

// expression rationnelle d'une adresse mail correcte : prenom.nom.xxx@aaa.bbb.xxx
//			caract�res significatifs : le point, l'arobas
//			groupes de car : (prenom) . (nom) @ (sous-domaine) . (sous-domaine) ... (domaine)  
//			caract�res dans une exp reg : 
//					.   <=> n'importe quel car
//					[ ] <=> un car de l'ensemble de car, [^  ] tout car diff�rent des car de l'ensemble
//					*   <=> 0 ou plusieurs fois le car ou le groupe pr�c�dent
//					+	<=> au moins une fois le car ou le groupe pr�c�dent
//					c	<=> un car ou groupe de car obligatoire
//			ens de car E1 = [^.@] <=> tout car sauf . et @
//			email correct commence par au moins un groupe de car de type E1 : G1 = ([^.@]+)
//			suivi �ventuellement d'une r�p�tition de s�quences point-G1 
//				([^.@]+)  ( . [^.@]+ )*
//					attention : le point (en dehors d'un ensemble) doit �tre �chapp� par \
//					re-attention : le caract�re \ dans une cha�ne java doit aussi �tre �chapp� par \
//					et pas d'espace car l'espace est un caract�re significatif
//					=> ([^.@]+)(\\.[^.@]+)*
//			suivi par l'arobas ([^.@]+)(\\.[^.@]+)*  @
//			suivi par une ou plusieurs suites G1-point et une derni�re suite G1 
//				([^.@]+)(\\.[^.@]+)* @ ( G1 . )+ <=> au moins une suite G1 suivie d'un point
//				([^.@]+)(\\.[^.@]+)* @ ([^.@]+\\.)+ ( G1 )
//			=> ([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)
			
			String regExp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
			if ( email.matches( regExp ) ) { // adresse mail OK
				utilisateur.setEmail( email ); // enregistrement dans le bean Utilisateur
		    } else { // adresse mail mal form�e
		    	erreurs.put( "email", "Saisir une adresse mail valide" );
		    	System.out.println( "adresse mail mal form�e" );
		    }
		} else { // param�tre obligatoire mais vide 
		    erreurs.put( "email", "Saisir une adresse mail." );
		    System.out.println( "adresse mail vide" );
		}
	}

	private void validerLogin( String login ) {
		if ( login != null && login.length() >=3 ) { // param�tre non vide et au moins 3 car
			utilisateur.setLogin( login );
			System.out.println( "Login OK" );
		} else {
			erreurs.put( "login", "Le login doit avoir au moins 3 caract�res." );
			System.out.println( "Login trop court" );
		}
	}
	
	private void validerMotDePasse( String motdepasse, String confirmation ) {
	   if ( motdepasse != null && confirmation != null ) { // param�tres non vides
		   utilisateur.setMotDePasse( confirmation );
		   if ( motdepasse.equals( confirmation ) ) {
			if ( motdepasse.length() >=3 ) { // mdp confirm� et + de 3 carcact�res : OK
				// chiffrement du mdp avec la librairie Jasypt
				// fichier JAR dans WEB-INF\lib
				// algo SHA-256, retourne une cha�ne de 56 caract�res en Base64
				ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
				passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
				passwordEncryptor.setPlainDigest(false);
				String motDePasseChiffre = passwordEncryptor.encryptPassword( motdepasse );
				utilisateur.setMotDePasse( motDePasseChiffre );
				System.out.println( "MDP chiffr� : " + motDePasseChiffre );
			} else { // mdp trop court
				erreurs.put( "motdepasse", "Le mdp doit avoir au moins 3 caract�res." );
				System.out.println( "MDP trop court" );
			}
		   } else { // pas confirm�
				erreurs.put( "confirmation", "Le mot de passe et la confirmation doivent �tre identiques." );
				System.out.println( "Confirmation diff�rente de MDP" );
		   }
	   } else { // mdp non fourni
		   erreurs.put( "motdepasse", "Le mdp et sa confirmation sont obligatoires." );
		   System.out.println( "MDP non fourni" );
	   }
	}

	private void validerProfil( String profil ) {
		if ( profil != null ) { // param�tre non vide
			utilisateur.setProfil( profil );
			System.out.println( "Profil OK" );
		} else {
			erreurs.put( "profil", "Le profil doit �tre renseign�." );
			System.out.println( "Profil non fourni" );
		}
	}

	// r�cup�ration d'un param�tre de la requ�te
	private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }    
}

package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import dao.DAOException;
import dao.UtilisateurDao;
import beans.Utilisateur;

public final class ConnexionForm {

	private final static String ALGO_CHIFFREMENT = "SHA-256";

    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";

    // le bean qui va être créé si tout est correct
    private Utilisateur utilisateur;

	// une chaîne donnant le résultat de l'inscription
    private String resultat;

    // stockage de la référence de l'objet DAO pour accéder à la table utilisateur de la base
	private UtilisateurDao utilisateurDao;
	
    // MAP (champ, message) des erreurs de saisie détectées en validant les saisies
    private Map<String, String> erreurs = new HashMap<String, String>();

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

    public Utilisateur connecterUtilisateur( HttpServletRequest request ) {

    	utilisateur = new Utilisateur();

		// récupération et validation des champs de saisie, maj du bean Utilisateur
        validerEmail( getValeurChamp( request, CHAMP_EMAIL ) );
		validerMotDePasse( getValeurChamp( request, CHAMP_PASS ) );

		try { // l'accès en BD peut générer des erreurs SQL
			if ( erreurs.isEmpty() ) {
			//utilisateurDao.trouver( CHAMP_EMAIL ); // dans la base, via le DAO
				resultat = "Succès de la recherche.";
				// ici test de mot de passe
				String pass_comparaison = "admin";
				if ( getValeurChamp( request, CHAMP_PASS ).equals(pass_comparaison) ) {
					resultat = "mot de passe correct '" + getValeurChamp( request, CHAMP_PASS ) + "' = '" + pass_comparaison + "'";
				} else {
					resultat = "mot de passe érroné '" + getValeurChamp( request, CHAMP_PASS ) + "' <> '" + pass_comparaison + "'";
				}
			} else {
				resultat = "Echec de la recherche.";
			}
		} catch (DAOException e) {
			resultat = "Echec de la recherche, une erreur est survenue, réessayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// différencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		System.out.println( "Résultat : " + resultat );
		return utilisateur;
	}
		

	private void validerEmail( String email ) {
		if ( email != null ) { // paramètre non vide
			
/*
// expression rationnelle d'une adresse mail correcte : prenom.nom.xxx@aaa.bbb.xxx
//			caractères significatifs : le point, l'arobas
//			groupes de car : (prenom) . (nom) @ (sous-domaine) . (sous-domaine) ... (domaine)  
//			caractères dans une exp reg : 
//					.   <=> n'importe quel car
//					[ ] <=> un car de l'ensemble de car, [^  ] tout car différent des car de l'ensemble
//					*   <=> 0 ou plusieurs fois le car ou le groupe précédent
//					+	<=> au moins une fois le car ou le groupe précédent
//					c	<=> un car ou groupe de car obligatoire
//			ens de car E1 = [^.@] <=> tout car sauf . et @
//			email correct commence par au moins un groupe de car de type E1 : G1 = ([^.@]+)
//			suivi éventuellement d'une répétition de séquences point-G1 
//				([^.@]+)  ( . [^.@]+ )*
//					attention : le point (en dehors d'un ensemble) doit être échappé par \
//					re-attention : le caractère \ dans une chaîne java doit aussi être échappé par \
//					et pas d'espace car l'espace est un caractère significatif
//					=> ([^.@]+)(\\.[^.@]+)*
//			suivi par l'arobas ([^.@]+)(\\.[^.@]+)*  @
//			suivi par une ou plusieurs suites G1-point et une dernière suite G1 
//				([^.@]+)(\\.[^.@]+)* @ ( G1 . )+ <=> au moins une suite G1 suivie d'un point
//				([^.@]+)(\\.[^.@]+)* @ ([^.@]+\\.)+ ( G1 )
//			=> ([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)
*/
			
			String regExp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";
			if ( email.matches( regExp ) ) { // adresse mail OK
				utilisateur.setEmail( email ); // enregistrement dans le bean Utilisateur
		    } else { // adresse mail mal formée
		    	erreurs.put( "email", "Saisir une adresse mail valide" );
		    	System.out.println( "   ...adresse mail mal formée" );
		    }
		} else { // paramètre obligatoire mais vide 
		    erreurs.put( "email", "Saisir une adresse mail." );
		    System.out.println( "   ...adresse mail vide" );
		}
	}

	private void validerMotDePasse( String motdepasse ) {
	   if ( motdepasse != null ) { // paramètre non vide
		   utilisateur.setMotDePasse( motdepasse );
			if ( motdepasse.length() >=3 ) { // mdp + de 3 carcactères : OK
				// chiffrement du mdp avec la librairie Jasypt
				// fichier JAR dans WEB-INF\lib
				// algo SHA-256, retourne une chaîne de 56 caractères en Base64
				ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
				passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
				passwordEncryptor.setPlainDigest(false);
				String motDePasseChiffre = passwordEncryptor.encryptPassword( motdepasse );
				utilisateur.setMotDePasse( motDePasseChiffre );
				System.out.println("   ... MDP chiffré : " + motDePasseChiffre );
			} else { // mdp trop court
				erreurs.put( "motdepasse", "Le mdp doit avoir au moins 3 caractères." );
				System.out.println( "   ...MDP trop court" );
			}
	   } else { // mdp non fourni
		   erreurs.put( "motdepasse", "Le mdp est obligatoire." );
		   System.out.println( "   ... MDP non fourni" );
	   }
	}

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu sinon.
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}

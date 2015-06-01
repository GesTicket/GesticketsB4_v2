package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import dao.DAOException;
import dao.DAOFactory;
import dao.TicketDao;
import beans.Ticket;

public class AjouterForm {
	// classe métier qui traite les saisies du formulaire d'ajout d'un ticket
	
		
    private static final String CHAMP_TITRE  = "titre";
    private static final String CHAMP_DESCRIP   = "description";
    
    // le bean qui va être créé si tout est correct
    private Ticket ticket;
    
	// une chaîne donnant le résultat de l'ajout
	private String resultat;

	// stockage de la référence de l'objet DAO pour accéder à la table ticket de la base
	private TicketDao ticketDao;
    
	// MAP (champ, message) des erreurs de saisie détectées en validant les saisies
	private Map<String, String> erreurs = new HashMap<String, String>();

	public String getResultat() {
		return resultat;
	}

 	public Map<String, String> getErreurs() {
		return erreurs;
	}
	
	public AjouterForm( DAOFactory daoFactory) {
		// récupération de l'objet DAO à partir de la fabrique donnée au constructeur
		ticketDao = daoFactory.getTicketDao();
	}

	public Ticket ajouterTicket( HttpServletRequest request ) {
				
		ticket = new Ticket();
		
		// récupération et validation des champs de saisie, maj du bean Ticket		
		validerTitre( getValeurChamp( request, CHAMP_TITRE ) );
		validerDescription( getValeurChamp( request, CHAMP_DESCRIP ));
		
		
		try { // l'accès en BD peut générer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				ticketDao.creer( ticket ); // dans la base, via le DAO
				resultat = "Succès de l'ajout.";
			} else {
				resultat = "Echec de l'ajout.";
			}
		} catch (DAOException e) {
			resultat = "Echec de l'ajout, une erreur est survenue, réessayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// différencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		System.out.println( "Résultat : " + resultat );
		return ticket;
	}

	private void validerTitre( String titre) {
		if ( titre != null) { // paramètre non vide
			ticket.setTitre (titre);
		} else {
			erreurs.put("titre", "Saisir un titre.");
			System.out.println( "   ...adresse mail vide" );
		}
	}

	private void validerDescription( String description) {
	   if ( description != null) { // paramètres non vides
		   
		   ticket.setDescription( description );
	   } else { // mdp non fourni
		   erreurs.put( "description", "La description est obligatoire." );
	   }
	}

	// récupération d'un paramètre de la requète
	private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }    
}

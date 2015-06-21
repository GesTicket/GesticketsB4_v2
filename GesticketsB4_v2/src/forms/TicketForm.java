package forms;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.DAOException;
import dao.DAOFactory;
import dao.TicketDao;
import beans.Ticket;

public class TicketForm {
	// classe métier qui traite les saisies du formulaire d'ajout d'un ticket
		
    private static final String CHAMP_TITRE     = "titre";
    private static final String CHAMP_DESCRIP   = "description";
    private static final String CHAMP_MOT_CLE   = "motCle";
    
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
	
	public TicketForm( DAOFactory daoFactory) {
		// récupération de l'objet DAO à partir de la fabrique donnée au constructeur
		ticketDao = daoFactory.getTicketDao();
	}

	public Ticket ajouterTicket( HttpServletRequest request ) {
				
		ticket = (Ticket) new Ticket();
		
		// récupération et validation des champs de saisie, maj du bean Ticket		
		validerTitre( getValeurChamp( request, CHAMP_TITRE ) );
		validerDescription( getValeurChamp( request, CHAMP_DESCRIP ));
		
		
		try { // l'accès en BD peut générer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				java.util.Date date= new java.util.Date();
				ticket.setDateCreation( new Timestamp( date.getTime() ) );
				ticketDao.creerTicket( ticket ); // dans la base, via le DAO
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
			
		}
	}

	private void validerDescription( String description) {
	   if ( description != null) { // paramètres non vides
		   
		   ticket.setDescription( description );
	   } else { 
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

	public Ticket rechercherTicket(HttpServletRequest request) {
		ticket = new Ticket();
		
		// récupération et validation des champs de saisie, maj du bean Utilisateur	
		
		trouverTitre( getValeurChamp( request, CHAMP_TITRE ) );
		trouverDescription( getValeurChamp( request, CHAMP_DESCRIP ) );
		
		
		try { // l'accès en BD peut générer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				
				ticketDao.rechercherTicket( ticket ); // dans la base, via le DAO
				resultat = "Succès de la recherche.";
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
		return ticket;
	}

	public void rechercherTicketsMotCle( HttpServletRequest request ) {

    	// Récupération de la session depuis la requête
    	HttpSession session = request.getSession(true);
    	
		// enregistrement dans une variable de session
    	String motCle = getValeurChamp( request, CHAMP_MOT_CLE );
		session.setAttribute("sessionMotCle", getValeurChamp( request, CHAMP_MOT_CLE ) );

		// récupération et validation des champs de saisie		
		validerMotCle( getValeurChamp( request, CHAMP_MOT_CLE ) );		
		
		try { // l'accès en BD peut générer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				List<Ticket> listeTickets = ticketDao.rechercherTicketsMotCle( motCle ); // dans la base, via le DAO
	            Map<Long, Ticket> mapRechercheTickets = new HashMap<Long, Ticket>();
	            for ( Ticket ticket : listeTickets ) {
	            	System.out.println( "remplissage mapRechercheTickets n° " + ticket.getId() );
	                mapRechercheTickets.put( ticket.getId(), ticket );
	            }
	            session.setAttribute("mapRechercheTickets", mapRechercheTickets );
				resultat = "Succès de la recherche par mot clé.";
			} else {
				resultat = "Echec de la recherche par mot clé.";
			}
		} catch (DAOException e) {
			resultat = "Echec de la recherche par mot clé, une erreur est survenue, réessayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// différencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		System.out.println( "Résultat : " + resultat );
	}
	private void trouverDescription(String descrip) {
		if ( descrip != null) { // paramètre non vide
			ticket.setDescription (descrip);
		}
		
	}

	private void trouverTitre(String titre) {
		if ( titre != null) { // paramètre non vide
			ticket.setTitre(titre);
		}
		
	}
	
	private void validerMotCle( String motCle ) {
		if ( motCle != null ) { // paramètre non vide
			System.out.println( "mot clé OK" );
		} else {
			erreurs.put( "motCle", "un mot clé doit être saisi." );
			System.out.println( "mot clé non saisi" );
		}
	}
}

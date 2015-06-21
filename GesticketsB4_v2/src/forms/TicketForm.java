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
	// classe m�tier qui traite les saisies du formulaire d'ajout d'un ticket
		
    private static final String CHAMP_TITRE     = "titre";
    private static final String CHAMP_DESCRIP   = "description";
    private static final String CHAMP_MOT_CLE   = "motCle";
    
    // le bean qui va �tre cr�� si tout est correct
    private Ticket ticket;
    
	// une cha�ne donnant le r�sultat de l'ajout
	private String resultat;

	// stockage de la r�f�rence de l'objet DAO pour acc�der � la table ticket de la base
	private TicketDao ticketDao;
    
	// MAP (champ, message) des erreurs de saisie d�tect�es en validant les saisies
	private Map<String, String> erreurs = new HashMap<String, String>();

	public String getResultat() {
		return resultat;
	}

 	public Map<String, String> getErreurs() {
		return erreurs;
	}
	
	public TicketForm( DAOFactory daoFactory) {
		// r�cup�ration de l'objet DAO � partir de la fabrique donn�e au constructeur
		ticketDao = daoFactory.getTicketDao();
	}

	public Ticket ajouterTicket( HttpServletRequest request ) {
				
		ticket = (Ticket) new Ticket();
		
		// r�cup�ration et validation des champs de saisie, maj du bean Ticket		
		validerTitre( getValeurChamp( request, CHAMP_TITRE ) );
		validerDescription( getValeurChamp( request, CHAMP_DESCRIP ));
		
		
		try { // l'acc�s en BD peut g�n�rer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				java.util.Date date= new java.util.Date();
				ticket.setDateCreation( new Timestamp( date.getTime() ) );
				ticketDao.creerTicket( ticket ); // dans la base, via le DAO
				resultat = "Succ�s de l'ajout.";
			} else {
				resultat = "Echec de l'ajout.";
			}
		} catch (DAOException e) {
			resultat = "Echec de l'ajout, une erreur est survenue, r�essayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// diff�rencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		System.out.println( "R�sultat : " + resultat );
		return ticket;
	}

	private void validerTitre( String titre) {
		if ( titre != null) { // param�tre non vide
			ticket.setTitre (titre);
		} else {
			erreurs.put("titre", "Saisir un titre.");
			
		}
	}

	private void validerDescription( String description) {
	   if ( description != null) { // param�tres non vides
		   
		   ticket.setDescription( description );
	   } else { 
		   erreurs.put( "description", "La description est obligatoire." );
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

	public Ticket rechercherTicket(HttpServletRequest request) {
		ticket = new Ticket();
		
		// r�cup�ration et validation des champs de saisie, maj du bean Utilisateur	
		
		trouverTitre( getValeurChamp( request, CHAMP_TITRE ) );
		trouverDescription( getValeurChamp( request, CHAMP_DESCRIP ) );
		
		
		try { // l'acc�s en BD peut g�n�rer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				
				ticketDao.rechercherTicket( ticket ); // dans la base, via le DAO
				resultat = "Succ�s de la recherche.";
			} else {
				resultat = "Echec de la recherche.";
			}
		} catch (DAOException e) {
			resultat = "Echec de la recherche, une erreur est survenue, r�essayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// diff�rencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		System.out.println( "R�sultat : " + resultat );
		return ticket;
	}

	public void rechercherTicketsMotCle( HttpServletRequest request ) {

    	// R�cup�ration de la session depuis la requ�te
    	HttpSession session = request.getSession(true);
    	
		// enregistrement dans une variable de session
    	String motCle = getValeurChamp( request, CHAMP_MOT_CLE );
		session.setAttribute("sessionMotCle", getValeurChamp( request, CHAMP_MOT_CLE ) );

		// r�cup�ration et validation des champs de saisie		
		validerMotCle( getValeurChamp( request, CHAMP_MOT_CLE ) );		
		
		try { // l'acc�s en BD peut g�n�rer des erreurs SQL
			if ( erreurs.isEmpty() ) {
				List<Ticket> listeTickets = ticketDao.rechercherTicketsMotCle( motCle ); // dans la base, via le DAO
	            Map<Long, Ticket> mapRechercheTickets = new HashMap<Long, Ticket>();
	            for ( Ticket ticket : listeTickets ) {
	            	System.out.println( "remplissage mapRechercheTickets n� " + ticket.getId() );
	                mapRechercheTickets.put( ticket.getId(), ticket );
	            }
	            session.setAttribute("mapRechercheTickets", mapRechercheTickets );
				resultat = "Succ�s de la recherche par mot cl�.";
			} else {
				resultat = "Echec de la recherche par mot cl�.";
			}
		} catch (DAOException e) {
			resultat = "Echec de la recherche par mot cl�, une erreur est survenue, r�essayer";
			// si la MAP des erreurs est vide, on enregistre l'erreur DAO pour
			// diff�rencier la couleur d'affichage dans la JSP de retour
			if ( erreurs.isEmpty() ) {
				erreurs.put( "dao", resultat );
			}
//				e.printStackTrace();
		}
		System.out.println( "R�sultat : " + resultat );
	}
	private void trouverDescription(String descrip) {
		if ( descrip != null) { // param�tre non vide
			ticket.setDescription (descrip);
		}
		
	}

	private void trouverTitre(String titre) {
		if ( titre != null) { // param�tre non vide
			ticket.setTitre(titre);
		}
		
	}
	
	private void validerMotCle( String motCle ) {
		if ( motCle != null ) { // param�tre non vide
			System.out.println( "mot cl� OK" );
		} else {
			erreurs.put( "motCle", "un mot cl� doit �tre saisi." );
			System.out.println( "mot cl� non saisi" );
		}
	}
}

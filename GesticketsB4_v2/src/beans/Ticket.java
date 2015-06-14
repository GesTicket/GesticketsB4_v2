package beans;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class Ticket implements Serializable {

	private Long id;
	private String titre;
	private String description;
	private Timestamp dateCreation;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Timestamp dateCreation) {
		this.dateCreation = dateCreation;
	}
	public Ticket() {
		
	}
	public Ticket( int id, String titre, String description) {
		setId((long) id);
		setTitre(titre);
		setDescription(description);
	}	
    
}

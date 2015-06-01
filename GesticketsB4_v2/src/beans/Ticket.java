package beans;

import java.sql.Timestamp;

public class Ticket {

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
	public void setDateCreation(Timestamp dateCréation) {
		this.dateCreation = dateCréation;
	}
	

}

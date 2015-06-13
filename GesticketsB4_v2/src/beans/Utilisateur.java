package beans;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class Utilisateur implements Serializable {
    private Long      id;
    private String    email;
    private String    motDePasse;
    private String    nom;
    private Timestamp dateInscription;
    private String    prenom;
    private String    profil;
    private String    login;   

    public Long getId() {
        return id;
    }
    public void setId( Long id ) {
        this.id = id;
    }

    public void setEmail( String email ) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setMotDePasse( String motDePasse ) {
        this.motDePasse = motDePasse;
    }
    public String getMotDePasse() {
        return motDePasse;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }
    public String getNom() {
        return nom;
    }

    public Timestamp getDateInscription() {
        return dateInscription;
    }
    public void setDateInscription( Timestamp dateInscription ) {
        this.dateInscription = dateInscription;
    }
    
    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }
    public String getPrenom() {
        return prenom;
    }

    public void setProfil( String profil ) {
        this.profil = profil;
    }
    public String getProfil() {
        return profil;
    }

    public void setLogin( String login ) {
        this.login = login;
    }
    public String getLogin() {
        return login;
    }

    // constructeur par défaut
    public Utilisateur() {
    
    }
    
    // constructeur avec initialisation des champs de l'utilisateur
    public Utilisateur( int id, String email, /*String motDePasse,*/ String nom ) {
    	setId( (long) id );
    	setEmail( email );
    	//setMotDePasse( motDePasse );
    	setNom( nom );
    }
    
}

package dao;

import java.util.List;
import beans.Utilisateur;

public interface UtilisateurDao {

	Utilisateur trouverLogin( String login ) throws DAOException;

	Utilisateur trouverEmail( String email ) throws DAOException;

	void creerUtilisateur( Utilisateur utilisateur ) throws DAOException;
	
	void modifierUtilisateur( Utilisateur utilisateur ) throws DAOException;

	Utilisateur trouverUtilisateur(long id) throws DAOException;
	
	List<Utilisateur> listerUtilisateurs() throws DAOException;

	void supprimerUtilisateur ( Utilisateur utilisateur ) throws DAOException;
	
}
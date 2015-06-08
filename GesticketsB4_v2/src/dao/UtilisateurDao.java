package dao;

import beans.Utilisateur;

public interface UtilisateurDao {
	
	void creer( Utilisateur utilisateur ) throws DAOException;
	
	Utilisateur trouverLogin( String login ) throws DAOException;

	Utilisateur trouverEmail( String email ) throws DAOException;

	void supprimerUtilisateur ( int id ) throws DAOException;
	
}
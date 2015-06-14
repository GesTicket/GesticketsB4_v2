package dao;

import java.util.List;

import beans.Ticket;



public interface TicketDao {
	
	void creerTicket( Ticket ticket ) throws DAOException;
	
	Ticket trouverTitre( String titre ) throws DAOException;
	
	Ticket trouverDescription(String description) throws DAOException;
	
	Ticket trouverTicket(long id) throws DAOException;
	
	List<Ticket> listerTickets() throws DAOException;
	
	void supprimerTicket(Ticket ticket) throws DAOException;
}


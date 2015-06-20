package dao;

import java.util.List;

import beans.Ticket;



public interface TicketDao {
	
	void creerTicket( Ticket ticket ) throws DAOException;
	
	Ticket rechercherTicket( Ticket ticket ) throws DAOException;
		
	List<Ticket> listerTickets() throws DAOException;
	
	void supprimerTicket(Ticket ticket) throws DAOException;
}


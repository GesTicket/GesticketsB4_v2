package dao;

import beans.Ticket;


public interface TicketDao {
	void creer( Ticket ticket ) throws DAOException;
	
	Ticket trouver( String titre ) throws DAOException;
}


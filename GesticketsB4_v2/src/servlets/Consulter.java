package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Consulter extends HttpServlet {
	
	private static final String FORM_CONSULTER = "/WEB-INF/Consulter.jsp";
	private static final String ATT_FORM = "form";
	
	// identifiant de l'attribut de scope Application donnant la référence de la fabrique de DAOs
	private static final String ATT_DAO_FACTORY_ID = "daoFactory";
	
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		getServletContext().getRequestDispatcher( FORM_CONSULTER ).forward( request, response );
	}
}

package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Utilisateur;

@SuppressWarnings("serial")
public class ListeUtilisateurs extends HttpServlet {

	public static final String VUE = "/WEB-INF/listerUtilisateurs.jsp";
	// scope SESSION
	public static final String SESSION_UTILISATEURS = "mapUtilisateurs";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// à réception d'une requête GET, affichage de la liste des utilisateurs
		// accès aux attributs de scope session
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<String, Utilisateur> mapUtilisateurs = (HashMap<String, Utilisateur>) session.getAttribute( SESSION_UTILISATEURS );
		mapUtilisateurs = new HashMap<String, Utilisateur>();
		
	    // Ajout du Bean à l'objet requête
	   	request.setAttribute( SESSION_UTILISATEURS, mapUtilisateurs );
	   	
		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}

}

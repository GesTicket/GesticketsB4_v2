package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Utilisateur;

public class ListeUtilisateurs extends HttpServlet {

	public static final String VUE = "/WEB-INF/listerUtilisateurs.jsp";
	// scope SESSION
	public static final String SESSION_UTILISATEURS = "utilisateurs";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// à réception d'une requête GET, affichage de la liste des utilisateurs
		
		// accès aux attributs de scope session
		HttpSession session = request.getSession();
		
		Map<String, Utilisateur> utilisateurs = (HashMap<String, Utilisateur>) session.getAttribute( SESSION_UTILISATEURS );
		utilisateurs = new HashMap<String, Utilisateur>();
		
		// instanciation du bean Utilisateur
	    Utilisateur u1 = new Utilisateur(1, "admin@mail.fr", "administrateur" );
	    Utilisateur u2 = new Utilisateur(2, "util01@mail.fr", "utilisateur 01" );
	    Utilisateur u3 = new Utilisateur(3, "util02@mail.fr", "utilisateur 02" );
		
	    // Ajout du Bean à l'objet requête
	    //request.setAttribute( "utilisateur", u1 );
	    
		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}

}

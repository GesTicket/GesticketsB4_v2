<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!--
    ici, pas d'attribut transmis par la servlet de contrôle ListeUtilisateurs
-->
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des utilisateurs existants</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    </head>
<body>
    <div id="corps">
    <c:choose>
        <%-- si aucun utilisateurn'existe en session, message par défaut --%>
        <c:when test="${ empty sessionScope.mapUtilisateurs }">
            <p class="erreur">Aucun utilisateur enregistré.</p>
        </c:when>
        <%-- sinon, génération d'une table des utilisateurs --%>
        <c:otherwise>
        <table class="table table-bordered">
            <tr>
                <th>id</th>
                <th>Email</th>
                <th>Login</th>
                <th>nom</th>
                <th>prénom</th>
                <th>profil</th>
                <th>Date inscription</th>
                <th class="action">Action</th>
            </tr>
            <%-- parcours de la MAP des utilisateurs en session et  utilisation de l'objet varStatus. --%>
            <c:forEach items="${ sessionScope.mapUtilisateurs }" var="mapUtilisateurs" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                 <tr class="${boucle.index % 2 == 0 ? 'active' : ''}">
                    <%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapUtilisateurs.value.id }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.email }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.login }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.nom }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.prenom }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.profil}"/></td>
                    <td><fmt:formatDate pattern="dd/MM/yyyy - hh:mm:ss" value="${ mapUtilisateurs.value.dateInscription }" /></td>
                    <td><a class="btn btn-default btn-xs" href="<c:url value="/modificationUtilisateur">
                    <c:param name="idUtilisateur" value="${ mapUtilisateurs.key }" /></c:url>" role="button">Modifier</a>
                    <a class="btn btn-default btn-xs" href="<c:url value="/suppressionUtilisateur">
                    <c:param name="idUtilisateur" value="${ mapUtilisateurs.key }" /></c:url>" role="button">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        </c:otherwise>
    </c:choose>
    </div>
    <a class="btn btn-default" href="<c:url value="/creationUtilisateur"/>" role="button">créer un utilisateur</a>
    <a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">retour à l'accueil</a>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>

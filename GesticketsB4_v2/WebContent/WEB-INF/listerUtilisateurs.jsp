<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<!--
    ici, pas d'attribut transmis par la servlet de contrôle ListeUtilisateurs
-->
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des utilisateurs existants</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
<body>
    <div id="corps">
    <c:choose>
        <%-- si aucun utilisateurn'existe en session, message par défaut --%>
        <c:when test="${ empty sessionScope.utilisateurs }">
            <p class="erreur">Aucun utilisateur enregistré.</p>
        </c:when>
        <%-- sinon, génération d'une table des utilisateurs --%>
        <c:otherwise>
        <table>
            <tr>
                <th>id</th>
                <th>Email</th>
                <th>nom</th>
                <th>prénom</th>
                <th>profil</th>
                <th>Date inscription</th>
                <th class="action">Action</th>
            </tr>
            <%-- parcours de la MAP des utilisateurs en session et  utilisation de l'objet varStatus. --%>
            <c:forEach items="${ sessionScope.utilisateurs }" var="mapUtilisateurs" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                 <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapUtilisateurs.value.id }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.email }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.nom }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.prenom }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.profil}"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.dateInscription }"/></td>
                    <td>Modifier 
                    <a href="<c:url value="/suppressionUtilisateur"><c:param name="idUtilisateur" value="${ mapUtilisateurs.key }" /></c:url>">
                    Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        </c:otherwise>
    </c:choose>
    </div>
    <p><a href="<c:url value="/index.jsp"/>">retour à l'accueil</a></p>
</body>
</html>

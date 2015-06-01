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
    <div>
    <c:choose>
        <%-- si aucun client dans la MAP en session, message par défaut --%>
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
                <th>Date inscription</th>
                <th></th>
                <th class="action">Action</th>
            </tr>
            <%-- parcours de la MAP des utilisateurs (scope SESSION) --%>
            <c:forEach items="${ sessionScope.utilisateurs }" var="mapUtilisateurs">
                <tr>
                    <td><c:out value="${ mapUtilisateurs.value.id }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.email }"/></td>
                    <td><c:out value="${ mapUtilisateurs.value.nom }"/></td>
                    <td><c:out value=""/></td>
                    <td><c:out value=""/></td>
                    <td>Modifier Supprimer</td>
                </tr>
            </c:forEach>
        </table>
        </c:otherwise>
    </c:choose>
    </div>
    <p><a href="<c:url value="/index.jsp"/>">accueil</a></p>
</body>
</html>

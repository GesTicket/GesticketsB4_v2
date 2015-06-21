<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des tickets saisis</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    </head>
<body>
    <div id="corps">
    <c:choose>
        <%-- si aucun utilisateurn'existe en session, message par défaut --%>
        <c:when test="${ empty sessionScope.mapTickets }">
            <p class="erreur">Aucun ticket saisi.</p>
        </c:when>
       	<c:otherwise>
        <table class="table">
            <tr>
                <th>id</th>
                <th>Titre</th>
                <th>Description</th>
                <th>Date de création</th>
            </tr>
          	<c:forEach items="${ sessionScope.mapTickets }" var="mapTickets" varStatus="boucle">
                <tr class="${boucle.index % 2 == 0 ? 'active' : ''}">
                    <td><c:out value="${ mapTickets.value.id }"/></td>
                    <td><c:out value="${ mapTickets.value.titre }"/></td>
                    <td><c:out value="${ mapTickets.value.description }"/></td>
                    <td><fmt:formatDate pattern="dd/MM/yyyy - hh:mm:ss" value="${ mapTickets.value.dateCreation }"/></td>
                </tr>
            </c:forEach>
        </table>
        </c:otherwise>
    </c:choose>
    </div>
    <a class="btn btn-default" href="<c:url value="/rechercherTicketsMotCle"/>" role="button">Rechercher par mot clé</a>
    <a class="btn btn-default" href="<c:url value="/ajouterTicket"/>" role="button">Ajouter un ticket</a>
    <a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">Retour à l'accueil</a>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>

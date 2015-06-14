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
        <c:when test="${ empty sessionScope.tickets }">
            <p class="erreur">Aucun ticket saisi.</p>
        </c:when>
       	<c:otherwise>
        <table class="table">
            <tr>
                <th>id</th>
                <th>Titre</th>
                <th>Description</th>
                <th>Date de création</th>
                <th class="action">Action</th>
            </tr>
          	<c:forEach items="${ sessionScope.tickets }" var="maptickets" varStatus="boucle">
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <td><c:out value="${ maptickets.value.id }"/></td>
                    <td><c:out value="${ maptickets.value.titre }"/></td>
                    <td><c:out value="${ maptickets.value.description }"/></td>
                    <td><c:out value="${ maptickets.value.dateCreation }"/></td>
                    <td>Modifier</td>
                </tr>
            </c:forEach>
        </table>
        </c:otherwise>
    </c:choose>
    </div>
    <p><a href="<c:url value="/index.jsp"/>">retour à l'accueil</a></p>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>

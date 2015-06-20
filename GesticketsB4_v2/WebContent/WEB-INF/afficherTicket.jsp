<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'un ticket</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
    <body>
         <div id="corps">
            <p class="info">${ form.resultat }</p>
            <p>id : <c:out value="${ ticket.id }"/></p>
            <p>Titre : <c:out value="${ ticket.titre }"/></p>
            <p>Description : <c:out value="${ ticket.description }"/></p>
            <p>Date de création : <c:out value="${ ticket.dateCreation }"/></p>
        </div>
        <p><a href="<c:url value="/index.jsp"/>">accueil</a></p>
    </body>
</html>
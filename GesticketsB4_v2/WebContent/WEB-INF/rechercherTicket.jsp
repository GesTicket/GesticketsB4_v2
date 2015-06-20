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
    <form  method="get" action="rechercherTicket">
    
    	<fieldset>
              
            <label for="titre">Titre</label>
            <input type="text" id="titre" name="titre" 
                    value="<c:out value="${ ticket.titre }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['titre'] }</span>
            <br />
            <label for="description">Description </label>
            <input type="text" id="description" name="description" 
                    value="<c:out value="${ ticket.description }"/>" size="20" maxlength="200" />
            		<span class="erreur">${ form.erreurs['description'] }</span>
            
            <br />
             <label for="dateCreation">Date de création </label>
            <input type="text" id="dateCreation" name="dateCreation" 
                    value="<c:out value="${ ticket.dateCreation }"/>" size="20" maxlength="200" />
            <br />
            <input type="submit" value="Rechercher" class="${ empty form.resultat ? 'sansLabel' : '' }" />
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
            
        </fieldset>
    </form>
    
    <c:choose>
        <%-- si aucun utilisateurn'existe en session, message par défaut --%>
        <c:when test="${ empty sessionScope.ticket }">
            <p class="erreur">Aucun ticket ne correspond.</p>
        </c:when>
       	<c:otherwise>
        <table class="table">
            <tr>
                <th>id</th>
                <th>Titre</th>
                <th>Description</th>
                <th>Date de création</th>
            </tr>
          	<c:forEach items="${ sessionScope.ticket }" var="ticket" varStatus="boucle">
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <td><c:out value="${ ticket.value.id }"/></td>
                    <td><c:out value="${ ticket.value.titre }"/></td>
                    <td><c:out value="${ ticket.value.description }"/></td>
                    <td><c:out value="${ ticket.value.dateCreation }"/></td>
                </tr>
            </c:forEach>
        </table>
        </c:otherwise>
    </c:choose>
    </div>
    <a class="btn btn-default" href="<c:url value="/rechercherTicket"/>" role="button">Nouvelle recherche</a>
    <a class="btn btn-default" href="<c:url value="/ajouterTicket"/>" role="button">Ajouter un ticket</a>
    <a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">retour à l'accueil</a>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>

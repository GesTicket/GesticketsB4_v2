<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Recherche des tickets saisis par mot clé</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    </head>
<body>
    <div id="corps">
    <form  method="post" action="rechercherTicketsMotCle">
    
    	<fieldset>
              
            <label for="motCle">Mot clé</label>
            <input type="text" id="motCle" name="motCle" 
                    value="<c:out value="${ sessionMotCle }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['motCle'] }</span>
            <br />
            <input type="submit" value="Rechercher" class="${ empty form.resultat ? 'sansLabel' : '' }" />
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
            
        </fieldset>
    </form>
    
    </div>
    <a class="btn btn-default" href="<c:url value="/listeTickets"/>" role="button">Retour à la liste</a>
    <a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">Retour à l'accueil</a>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <title>Ajouter</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
</head>

<body>
    
     <form  method="post" action="ajouterTicket">
    
    	<fieldset>
            <legend>Ajout d'un ticket</legend>
            
            <label for="titre">titre</label>
            <input type="text" id="titre" name="titre" 
                    value="<c:out value="${ ticket.titre }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['titre'] }</span>
            <br />
            <label for="description">description </label>
            <input type="text" id="description" name="description" 
                    value="<c:out value="${ ticket.description }"/>" size="20" maxlength="200" />
            		<span class="erreur">${ form.erreurs['description'] }</span>
            
            <br />
            <input type="submit" value="Valider" class="${ empty form.resultat ? 'sansLabel' : '' }" />
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
            <p>
            	<a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">Retour à l'accueil</a>
            </p>
        </fieldset>
    </form>
</body>
</html>
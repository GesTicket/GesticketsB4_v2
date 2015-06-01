<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <title>Consulter</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
</head>

<body>
    
    <form  method="post" action="consulter">
    
    	<fieldset>
            <legend>Recherche d'un ticket</legend>
            
            <label for="titre">titre</label>
            <input type="titre" id="titre" name="titre" 
                    value="<c:out value="${ ticket.titre }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['titre'] }</span>
            <br />
            <label for="description">description </label>
            <input type="description" id="description" name="description" 
                    value="<c:out value="${ ticket.description }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['description'] }</span>
            <br />
            <label for="dateCr�ation">dateCr�ation </label>
            <input type="dateCr�ation" id="dateCr�ation" name="dateCr�ation" 
                    value="<c:out value="${ ticket.dateCr�ation }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['dateCr�ation'] }</span>
            <br />
            <input type="submit" value="Valider" class="${ empty form.resultat ? 'sansLabel' : '' }" />
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
            <p>
            	<a href="/GesticketsB4_v2/">Retour page accueil</a>
            </p>
        </fieldset>
    </form>
</body>
</html>
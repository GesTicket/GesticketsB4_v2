<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <title>Ajouter</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
</head>

<body>
    <!-- la page est affich�e en retour de GET /inscription ainsi qu'en retour de POST /inscription -->

     <form  method="post" action="ajouter">
    
    	<fieldset>
            <legend>Ajout d'un ticket</legend>
            
            <label for="titre">titre</label>
            <input type="text" id="titre" name="titre" 
                    value="<c:out value="${ ticket.titre }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['titre'] }</span>
            <br />
            <label for="description">description </label>
            <input type="text" id="description" name="description" 
                    value="<c:out value="${ ticket.description }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['description'] }</span>
            
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
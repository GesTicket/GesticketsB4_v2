<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <title>Connexion</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
</head>

<body>
    <!-- la page est affichée en retour de GET /connexion ainsi qu'en retour de POST /connexion -->

    <form method="post" action="connexion">

     <!-- si un bean identifié par 'utilisateur' existe, la valeur de chacun de ses attributs -->
     <!-- est affichée dans les différents champs de saisie de la page -->
     <!-- chaque champ est suivi d'une zone d'affichage : erreur de saisie éventuelle -->
     <!-- les messages d'erreur sont extraits de la MAP 'erreurs' du bean 'verifUser' -->
     <!-- le résultat du traitement (succès ou échec) est extrait de l'attribut 'resultat' -->
    
        <fieldset>
            <legend>Connexion</legend>
            <p>Vous pouvez vous connecter via ce formulaire.</p>
            <label for="email">Adresse email <span class="requis">*</span></label>
            <input type="text" id="email" name="email" 
                    value="<c:out value="${ utilisateur.email }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['email'] }</span>
            <br />
            <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
            <input type="password" id="motdepasse" name="motdepasse" 
                    value="<c:out value="${ utilisateur.motDePasse }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['motdepasse'] }</span>
            <br />
           <input type="submit" value="Valider" class="${ empty form.resultat ? 'sansLabel' : '' }" />
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
        </fieldset>
        
    </form>
    
    <p><a href="<c:url value="/index.jsp"/>">accueil</a></p>

</body>
</html>

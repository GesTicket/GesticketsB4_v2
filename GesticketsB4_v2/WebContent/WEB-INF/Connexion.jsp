<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Connexion</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    
</head>
<body>
    <!-- la page est affichée en retour de GET /connexion ainsi qu'en retour de POST /connexion -->
    <form method="post" action="connexion">
     <!-- si les variables de session existent, la valeur des attributs                         -->
     <!-- sont affichées dans les champs de saisie correspondants                               -->
     <!-- chaque champ est suivi d'une zone d'affichage : erreur de saisie éventuelle           -->
     <!-- les messages d'erreur sont extraits de la MAP 'erreurs'                               -->
     <!-- le résultat du traitement (succès ou échec) est extrait de l'attribut 'resultat'      -->
        <fieldset>
            <legend>Connexion</legend>
            <p>Vous pouvez vous connecter via ce formulaire.</p>
            <label for="login">Login <span class="requis">*</span></label>
            <input type="text" id="login" name="login"
                    value="<c:out value="${ sessionLogin }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['login'] }</span>
            <br />
            <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
            <input type="password" id="motdepasse" name="motdepasse"
                    value="<c:out value="${ sessionPass }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['motdepasse'] }</span>
            <br />
           <input type="submit" value="Valider" class="sansLabel" />
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
        </fieldset>
    </form>
    <p><a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">Retour à l'accueil</a></p>
</body>
</html>
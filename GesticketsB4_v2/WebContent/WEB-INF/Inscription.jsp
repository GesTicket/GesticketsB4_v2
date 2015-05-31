<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <title>Inscription</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
</head>

<body>
    <!-- la page est affichée en retour de GET /inscription ainsi qu'en retour de POST /inscription -->

    <form method="post" action="inscription">

     <!-- si un bean identifié par 'utilisateur' existe, la valeur de chacun de ses attributs -->
     <!-- est affichée dans les différents champs de saisie de la page -->
     <!-- chaque champ est suivi d'une zone d'affichage : erreur de saisie éventuelle -->
     <!-- les messages d'erreur sont extraits de la MAP 'erreurs' du bean 'InscriptionForm' -->
     <!-- le résultat du traitement (succès ou échec) est extrait de l'attribut 'resultat' -->
    
        <fieldset>
            <legend>Inscription</legend>
            <p>Vous pouvez vous inscrire via ce formulaire.</p>
            <label for="email">Adresse email <span class="requis">*</span></label>
            <input type="email" id="email" name="email" 
                    value="<c:out value="${ utilisateur.email }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['email'] }</span>
            <br />
            <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
            <input type="password" id="motdepasse" name="motdepasse" 
                    value="<c:out value="${ utilisateur.motDePasse }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['motdepasse'] }</span>
            <br />
            <label for="confirmation">Confirmation du mot de passe <span class="requis">*</span></label>
            <input type="password" id="confirmation" name="confirmation" 
                    value="<c:out value="${ utilisateur.motDePasse }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['confirmation'] }</span>
            <br />
            <label for="nom">Nom d'utilisateur</label>
            <input type="text" id="nom" name="nom" 
                    value="<c:out value="${ utilisateur.nom }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['nom'] }</span>
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
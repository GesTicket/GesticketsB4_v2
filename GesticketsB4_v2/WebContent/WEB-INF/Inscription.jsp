<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <title>Création d'un utilisateur</title>
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
            <legend>Création</legend>
            <p>Vous pouvez vous créer un utilisateur via ce formulaire.</p>
            <label for="nom">Nom <span class="requis">*</label>
            <input type="text" id="nom" name="nom" 
                    value="<c:out value="${ utilisateur.nom }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['nom'] }</span>
            <br />
            <label for="prenom">Prénom </label>
            <input type="text" id="prenom" name="prenom" 
                    value="<c:out value="${ utilisateur.prenom }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['prenom'] }</span>
            <br />
            <label for="email">Adresse email <span class="requis">*</span></label>
            <input type="email" id="email" name="email" 
                    value="<c:out value="${ utilisateur.email }"/>" size="20" maxlength="60" />
            		<span class="erreur">${ form.erreurs['email'] }</span>
            <br />
            <label for="login">Login <span class="requis">*</span></label>
            <input type="text" id="login" name="login" 
                    value="<c:out value="${ utilisateur.login }"/>" size="20" maxlength="20" />
            		<span class="erreur">${ form.erreurs['login'] }</span>
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
            <label for="profil">Profil <span class="requis">*</span></label>
			<select name="profil" id="profil">
				<option value="Utilisateur">Utilisateur</option>
				<option value="Intervenant" selected>Intervenant</option>
				<option value="Administrateur">Administrateur</option>
			</select>
			<span class="erreur">${ form.erreurs['profil'] }</span>
            <!-- value="<c:out value="${ utilisateur.profil }"/>" -->
            <br />

           <input type="submit" value="Valider" class="${ empty form.resultat ? 'sansLabel' : '' }" />
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
        </fieldset>
    </form>
    <p><a href="<c:url value="/index.jsp"/>">retour à l'accueil</a></p>
</body>
</html>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <title>Création d'un utilisateur</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
</head>

<body>
    <!-- la page est affichée en retour de GET /inscription ainsi qu'en retour de POST /inscription -->

    <form method="post" action="creationUtilisateur">

     <!-- si un bean identifié par 'utilisateur' existe, la valeur de chacun de ses attributs -->
     <!-- est affichée dans les différents champs de saisie de la page -->
     <!-- chaque champ est suivi d'une zone d'affichage : erreur de saisie éventuelle -->
     <!-- les messages d'erreur sont extraits de la MAP 'erreurs' du bean 'InscriptionForm' -->
     <!-- le résultat du traitement (succès ou échec) est extrait de l'attribut 'resultat' -->
    
        <fieldset>
            <legend>Création</legend>
            <p>Vous pouvez créer un utilisateur via ce formulaire.</p>
            <label for="nom">Nom <span class="requis">*</span></label>
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
				<c:set var="profil01" value="utilisateur"/>
				<c:set var="profil02" value="intervenant"/>
				<c:set var="profil03" value="administrateur"/>
				<option value="utilisateur" <c:if test="${ utilisateur.profil == profil01 }"> selected </c:if>>utilisateur</option>
				<option value="intervenant" <c:if test="${ utilisateur.profil == profil02 }"> selected </c:if>>intervenant</option>
				<option value="administrateur" <c:if test="${ utilisateur.profil == profil03 }"> selected </c:if>>administrateur</option>
			</select>
			<span class="erreur">${ form.erreurs['profil'] }</span>
            <!-- value="<c:out value="${ utilisateur.profil }"/>" -->
            <br />

           <input type="submit" value="Valider" class="sansLabel"/>
            <br />
            <p class="${ empty form.erreurs ? 'succes' : 'erreur' }">${ form.resultat }</p>
        </fieldset>
    </form>
    <p><a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">retour à l'accueil</a></p>
</body>
</html>
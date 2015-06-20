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

    <form method="post" action="creationUtilisateur" class="form-horizontal">

     <!-- si un bean identifié par 'utilisateur' existe, la valeur de chacun de ses attributs -->
     <!-- est affichée dans les différents champs de saisie de la page -->
     <!-- chaque champ est suivi d'une zone d'affichage : erreur de saisie éventuelle -->
     <!-- les messages d'erreur sont extraits de la MAP 'erreurs' du bean 'InscriptionForm' -->
     <!-- le résultat du traitement (succès ou échec) est extrait de l'attribut 'resultat' -->
    
        <fieldset>
            <legend>Création</legend>
            <p>Vous pouvez créer un utilisateur via ce formulaire.</p>
			<div class="form-group form-group-sm">
            	<label for="nom" class="col-sm-2 control-label">Nom <span class="requis">*</span></label>
	            <div class="col-sm-5">
	            <input type="text" id="nom" name="nom" class="form-control"
	            	value="<c:out value="${ utilisateur.nom }"/>" size="20" maxlength="20" />
	            </div>
	            <span class="col-sm-5 erreur">${ form.erreurs['nom'] }</span>
           </div>
			<div class="form-group form-group-sm">
            	<label for="prenom" class="col-sm-2 control-label">Prénom </label>
	            <div class="col-sm-5">
            	<input type="text" id="prenom" name="prenom" class="form-control"
                    value="<c:out value="${ utilisateur.prenom }"/>" size="20" maxlength="20" />
	            </div>
           		<span class="col-sm-5 erreur">${ form.erreurs['prenom'] }</span>
            </div>
			<div class="form-group form-group-sm">
	            <label for="email" class="col-sm-2 control-label">Adresse email <span class="requis">*</span></label>
	            <div class="col-sm-5">
	            <input type="email" id="email" name="email" class="form-control"
                    value="<c:out value="${ utilisateur.email }"/>" size="20" maxlength="60" />
	            </div>
            	<span class="col-sm-5 erreur">${ form.erreurs['email'] }</span>
            </div>
			<div class="form-group form-group-sm">
            	<label for="login" class="col-sm-2 control-label">Login <span class="requis">*</span></label>
	            <div class="col-sm-5">
            	<input type="text" id="login" name="login" class="form-control"
                    value="<c:out value="${ utilisateur.login }"/>" size="20" maxlength="20" />
	            </div>
            	<span class=" col-sm-5 erreur">${ form.erreurs['login'] }</span>
            </div>
			<div class="form-group form-group-sm">
            	<label for="motdepasse" class="col-sm-2 control-label">Mot de passe <span class="requis">*</span></label>
	            <div class="col-sm-5">
            	<input type="password" id="motdepasse" name="motdepasse" class="form-control"
                    value="<c:out value="${ utilisateur.motDePasse }"/>" size="20" maxlength="20" />
	            </div>
            	<span class="col-sm-5 erreur">${ form.erreurs['motdepasse'] }</span>
            </div>
			<div class="form-group form-group-sm">
            	<label for="confirmation" class="col-sm-2 control-label">Confirmation du mot de passe <span class="requis">*</span></label>
	            <div class="col-sm-5">
	            <input type="password" id="confirmation" name="confirmation" class="form-control"
                    value="<c:out value="${ utilisateur.motDePasse }"/>" size="20" maxlength="20" />
	            </div>
            	<span class="col-sm-5 erreur">${ form.erreurs['confirmation'] }</span>
            </div>
			<div class="form-group form-group-sm">
            	<label for="profil" class="col-sm-2 control-label">Profil <span class="requis">*</span></label>
	            <div class="col-sm-5">
				<select name="profil" id="profil" class="form-control">
					<c:set var="profil01" value="utilisateur"/>
					<c:set var="profil02" value="intervenant"/>
					<c:set var="profil03" value="administrateur"/>
					<option value="utilisateur" <c:if test="${ utilisateur.profil == profil01 }"> selected </c:if>>utilisateur</option>
					<option value="intervenant" <c:if test="${ utilisateur.profil == profil02 }"> selected </c:if>>intervenant</option>
					<option value="administrateur" <c:if test="${ utilisateur.profil == profil03 }"> selected </c:if>>administrateur</option>
				</select>
	            </div>
				<span class="col-sm-5 erreur">${ form.erreurs['profil'] }</span>
            </div>
    	    <div class="col-sm-offset-2 col-sm-5">
           		<input type="submit" value="Valider" />
            </div>
			<span class="col-sm-5 erreur">${ form.resultat }</span>
        </fieldset>
    </form>
    <br />
    <p><a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">retour à l'accueil</a></p>
</body>
</html>
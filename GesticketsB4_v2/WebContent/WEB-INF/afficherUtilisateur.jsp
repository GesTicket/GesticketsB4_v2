<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'un utilisateur</title>
        <link type="text/css" rel="stylesheet" href="inc/style.css" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    </head>
    <body>
        <form class="form-horizontal">
            <fieldset>
            <legend>Modification</legend>
			<p class="info">${ form.resultat }</p>
 			<div class="form-group">
	            <label class="col-sm-2 control-label">Id</label>
	            <div class="col-sm-10">
	            	<p class="form-control-static"><c:out value="${ utilisateur.id }"/></p>
	            </div>
            </div>
			<div class="form-group">
	            <label class="col-sm-2 control-label">Nom</label>
	            <div class="col-sm-10">
	            	<p class="form-control-static"><c:out value="${ utilisateur.nom }"/></p>
	            </div>
	        </div>
			<div class="form-group">
	            <label class="col-sm-2 control-label">Prénom</label>
	            <div class="col-sm-10">
	            	<p class="form-control-static"><c:out value="${ utilisateur.prenom }"/></p>
	            </div>
            </div>
			<div class="form-group">
	            <label class="col-sm-2 control-label">Adresse email</label>
	            <div class="col-sm-10">
	            	<p class="form-control-static"><c:out value="${ utilisateur.email }"/></p>
	            </div>
            </div>
			<div class="form-group">
	            <label class="col-sm-2 control-label">Login</label>
	            <div class="col-sm-10">
	            	<p class="form-control-static"><c:out value="${ utilisateur.login }"/></p>
	            </div>
            </div>
			<div class="form-group">
	            <label class="col-sm-2 control-label">Profil</label>
	            <div class="col-sm-10">
					<p class="form-control-static"><c:out value="${ utilisateur.profil }"/></p>
	            </div>					
            </div>
        </fieldset>
	    </form>
        <p><a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">retour à l'accueil</a></p>
    </body>
</html>
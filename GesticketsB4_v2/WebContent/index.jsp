<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>GesTickets</title>
    <link type="text/css" rel="stylesheet" href="inc/style.css" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
</head>
<body>
<div class="jumbotron">
<%-- V�rification de la pr�sence d'un objet connect� en session --%>
<c:choose>
<c:when test="${ !empty sessionScope.sessionUtilisateur }">
<p class="succes">Bonjour ${ sessionScope.sessionUtilisateur.nom } ${ sessionScope.sessionUtilisateur.prenom }</p>
<br />
<c:set var="profil" value="administrateur"/>
<c:if test="${sessionScope.sessionUtilisateur.profil == profil}">
	<%-- fonctions accessibles uniquement au profil administrateur --%>
	<p><a class="btn btn-default" href="<c:url value="/creationUtilisateur"/>" role="button">cr�er un utilisateur</a></p>
	<br />
	<p><a class="btn btn-default" href="<c:url value="/listeUtilisateurs"/>" role="button">lister les utilisateurs</a></p>
	<br />
</c:if>
<p><a class="btn btn-default" href="<c:url value="/ajouterTicket"/>" role="button">ajouter un ticket</a></p>
<br />
<p><a class="btn btn-default" href="<c:url value="/listeTickets"/>" role="button">Liste des tickets</a></p>
<br />
<p><a class="btn btn-default" href="<c:url value="/deconnexion"/>" role="button">se d�connecter</a></p>
</c:when>
<c:otherwise>
<p><a class="btn btn-default" href="<c:url value="/connexion"/>" role="button">se connecter</a></p>
</c:otherwise>
</c:choose>
</div>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>
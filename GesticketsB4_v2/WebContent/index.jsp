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
<%-- Vérification de la présence d'un objet connecté en session --%>
<c:choose>
<c:when test="${ !empty sessionScope.sessionUtilisateur }">
<p class="succes">Bonjour ${ sessionScope.sessionUtilisateur.nom } ${ sessionScope.sessionUtilisateur.prenom }</p>
<br />
<c:set var="profil" value="administrateur"/>
<c:if test="${sessionScope.sessionUtilisateur.profil == profil}">
	<%-- fonctions accessibles uniquement au profil administrateur --%>
	<p><a href="<c:url value="/creationUtilisateur"/>">créer un utilisateur</a></p>
	<br />
	<p><a href="<c:url value="/listeUtilisateurs"/>">lister les utilisateurs</a></p>
	<br />
</c:if>
<p><a href="<c:url value="/ajouterTicket"/>">ajouter un ticket</a></p>
<br />
<p><a href="<c:url value="/listeTickets"/>">Liste des tickets</a></p>
<br />
<p><a href="<c:url value="/deconnexion"/>">se déconnecter</a></p>
</c:when>
<c:otherwise>
<p><a href="<c:url value="/connexion"/>">se connecter</a></p>
</c:otherwise>
</c:choose>
</div>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>
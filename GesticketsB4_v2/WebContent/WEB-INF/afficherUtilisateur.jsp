<%@ page pageEncoding="UTF-8" %>
<!--
    attributs transmis par la servlet de contrôle (Inscription) :
    - form   : bean de traitement du formulaire (uniquement pour le resultat)
    - utilisateur : bean de stockage des données de l'utilisateur
 -->
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'un utilisateur</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    </head>
    <body>
         <div id="corps">
            <p class="info">${ form.resultat }</p>
            <p>id : <c:out value="${ utilisateur.id }"/></p>
            <p>Nom : <c:out value="${ utilisateur.nom }"/></p>
            <p>Prénom : <c:out value="${ utilisateur.prenom }"/></p>
            <p>Email : <c:out value="${ utilisateur.email }"/></p>
            <p>Login: <c:out value="${ utilisateur.login }"/></p>
            <p>Mot de passe : <c:out value="${ utilisateur.motDePasse }"/></p>
            <p>Profil : <c:out value="${ utilisateur.profil }"/></p>
            <p>Date d'inscription : <c:out value="${ utilisateur.dateInscription }"/></p>
        </div>
        <p><a class="btn btn-default" href="<c:url value="/index.jsp"/>" role="button">retour à l'accueil</a></p>
    </body>
</html>
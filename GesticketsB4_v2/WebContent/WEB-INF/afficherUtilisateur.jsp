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
    </head>
    <body>
         <div id="corps">
            <p class="info">${ form.resultat }</p>
            <p>id : <c:out value="${ utilisateur.id }"/></p>
            <p>Email : <c:out value="${ utilisateur.email }"/></p>
            <p>Mot de passe : <c:out value="${ utilisateur.motDePasse }"/></p>
            <p>Nom : <c:out value="${ utilisateur.nom }"/></p>
            <p>Date d'inscription : <c:out value="${ utilisateur.dateInscription }"/></p>
        </div>
        <p><a href="<c:url value="/index.jsp"/>">accueil</a></p>
    </body>
</html>
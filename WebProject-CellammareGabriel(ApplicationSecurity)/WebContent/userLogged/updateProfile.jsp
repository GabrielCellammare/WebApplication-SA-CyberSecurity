<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
<title>Homepage Web Application</title>
    
        <style>
         	<%@include file="/WEB-INF/css/styleIndexLogged.css"%>
         </style>

</head>
<body>
    <div class="container">
        <h1>Modifica Profilo</h1>
        <form action="modificaProfilo" method="post" enctype="multipart/form-data">
            <div class="field">
                <label for="immagine">Immagine di Profilo:</label>
                <input type="file" id="immagine" name="immagine">
            </div>
            <div class="field">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome">
            </div>
            <div class="field">
                <label for="cognome">Cognome:</label>
                <input type="text" id="cognome" name="cognome">
            </div>
            <div class="field">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" readonly>
            </div>
            <div class="field">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password">
            </div>
            <div class="field">
                <label for="confermaPassword">Conferma Password:</label>
                <input type="password" id="confermaPassword" name="confermaPassword">
            </div>
            <input type="submit" value="Salva Modifiche">
        </form>
    </div>
</body>
</html>
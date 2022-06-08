<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Homepage</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
	<ul class="navbar">
		<li><a class="active" href="/Aerodrom">Pocetna</a></li>
		<li><a href="/Aerodrom/letovi/pretragaLetova"><b>Pretrazi letove</b></a></li>
		<li><a href="/Aerodrom/destinacije/prikazDestinacija"><b>Prikaz destinacija</b></a></li>
		<security:authorize access="hasRole('MANAGER')"> 
			<li><a href="/Aerodrom/avioni/preusmeri"><b>Dodaj novi avion</b></a></li>
			<li><a href="/Aerodrom/letovi/unosPodataka"><b>Dodaj novi let</b></a></li>
			<li><a href="/Aerodrom/SuspendovanjeLeta.jsp"><b>Suspenduj let</b></a></li>
			<li><a href="/Aerodrom/Izvestaj.jsp"><b>Izvestaji</b></a></li>
		</security:authorize>
		<security:authorize access="isAuthenticated()"> 
			<li><a href="/Aerodrom/auth/logout"><b>Odjava</b></a></li>
		</security:authorize>
	</ul>
	
	<div class="main">
		
	<c:url var="loginUrl" value="/login" />
	<c:if test="${not empty param.error}">
		<div class="alert">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Greska!</strong>Pogresni podaci
			</div>
	</c:if>
	
	<c:if test="${not empty porukaUspeh}">
		<div class="succes">
			<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
			<strong>Uspeh!</strong>${porukaUspeh}			
		</div>
	</c:if>
	
	<form action="${loginUrl}" method="post">
		<table>
			<tr>
				<td>Korisnicko ime</td>
				<td><input type="text" name="username" placeholder="Unesite korisnicko ime" required></td>
			</tr>
			<tr>
				<td>Sifra</td>
				<td><input type="password" name="password" placeholder="Unesite sifru" required></td>
			</tr>
			 <tr>
                <td>Zapamti me:</td>
                <td><input type="checkbox" name="remember-me" /></td>
            </tr>
			<tr>
				<td><input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /></td>
				<td><input class="btn" type="submit" value="Prijava"></td>
			</tr>
		</table><br/><br/>
		Nemate nalog? <a style="text-decoration: none; color: blue;" href="/Aerodrom/auth/registerUser">Registrujte se</a>
	</form>
	</div>
	
</body>
</html>
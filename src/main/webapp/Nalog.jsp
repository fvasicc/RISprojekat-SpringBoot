<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Nalog</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
	<c:url var="loginUrl" value="/login" />
	<ul class="navbar">
		<li><a href="/Aerodrom">Pocetna</a></li>
		<li><a href="/Aerodrom/letovi/pretragaLetova"><b>Pretrazi letove</b></a></li>
		<li><a href="/Aerodrom/destinacije/prikazDestinacija"><b>Prikaz destinacija</b></a></li>
		<security:authorize access="hasRole('MANAGER')"> 
			<li><a href="/Aerodrom/avioni/preusmeri"><b>Dodaj novi avion</b></a></li>
			<li><a href="/Aerodrom/letovi/unosPodataka"><b>Dodaj novi let</b></a></li>
			<li><a href="/Aerodrom/SuspendovanjeLeta.jsp"><b>Suspenduj let</b></a></li>
			<li><a href="/Aerodrom/Izvestaj.jsp"><b>Izvestaji</b></a></li>
		</security:authorize>
		<security:authorize access="!isAuthenticated()"> 
			<div class="login">
	    		<form action="${loginUrl}" method="post">
			      <input type="text" name="username" placeholder="Unesite korisnicko ime" required>
			      <input type="password" name="password" placeholder="Unesite sifru" required>
			      <input type="checkbox" name="remember-me" />
			      <input type="submit" value="Prijava">
			    </form>
			</div>
		</security:authorize>
		<security:authorize access="isAuthenticated()"> 
			<li style="float: right;"><a href="/Aerodrom/auth/logout"><b>Odjava</b></a></li>
		</security:authorize>
		<security:authorize access="hasRole('USER')">	
			<li style="float:right;"><a class="active" href="/Aerodrom/user/account"><b>Moj nalog</b></a></li>
		</security:authorize>
	</ul>
	
	<div class="main" style="padding-left: 0px; padding-right: 0px;">
		<table>
			<tr>
				<td>Ime:</td>
				<td>${acc.ime}</td>
			</tr>
			<tr>
				<td>Prezime:</td>
				<td>${acc.prezime}</td>
			</tr>
			<tr>
				<td>JMBG:</td>
				<td>${acc.jmbg}</td>
			</tr>
			<tr>
				<td>email adresa:</td>
				<td>${acc.email}</td>
			</tr>
			<tr>
				<td>Broj telefona:</td>
				<td>${acc.telefon}</td>
			</tr>
		</table>
		<ul class="accmenu">
			<li><a href="/Aerodrom/user/rezervacijeKorisnika">Prikazi rezervacije</a></li>
			<li><a href="/Aerodrom/user/promeniLozinku">Promeni lozinku</a></li>
		</ul>
		</div>
	
</body>
</html>
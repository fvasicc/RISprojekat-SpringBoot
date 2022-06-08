<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Unos novog aviona</title>
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
			<li><a class="active" href="/Aerodrom/avioni/preusmeri"><b>Dodaj novi avion</b></a></li>
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
			<li style="float:right;"><a href="/Aerodrom/user/account"><b>Moj nalog</b></a></li>
		</security:authorize>
	</ul>
	
	<div class="main">
		<sf:form action="/Aerodrom/avioni/dodavanjeAviona" method="post" modelAttribute="avion">
			<table>
				<tr>
					<td>Unesi model aviona: </td>
					<td><sf:input path="model" type="text"/></td>
				</tr>
				<tr>
					<td>Unesi broj mesta: </td>
					<td><sf:input path="brojSedista" type="number"/></td>
				</tr>
				<tr><td><input type="submit" value="Sacuvaj"/></td></tr>
			</table>
		</sf:form>
		<c:if test="${not empty poruka}">
			<div class="succes">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Uspeh!${poruka}</strong>
			</div>
		</c:if>
	</div>
</body>
</html>
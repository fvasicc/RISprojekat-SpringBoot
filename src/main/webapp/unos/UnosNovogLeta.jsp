<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Dodavanje novog leta</title>
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
			<li><a class="active" href="/Aerodrom/letovi/unosPodataka"><b>Dodaj novi let</b></a></li>
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
		<sf:form action="/Aerodrom/letovi/slobodniAvioni" method="get" modelAttribute="let">
			<table>
				<tr>
					<td>Izaberite polazni grad: </td>
					<td>
						<sf:select path="polazniGrad" items="${gradovi}" itemValue="id" itemLabel="grad"></sf:select>
					</td>
				</tr>
				<tr>
					<td>Izaberite grad destinaciju: </td>
					<td>
						<sf:select path="destinacija" items="${gradovi}" itemValue="id" itemLabel="grad"></sf:select>
					</td>
				</tr>
				<tr>
					<td>Izaberi datum i vreme polaska: </td>
					<td><sf:input name="poletanje" path="vremePolaska" type="datetime-local" ></sf:input></td>
				</tr>
				<tr>
					<td>Izaberi datum i vreme sletanja: </td>
					<td><sf:input name="sletanje" path="vremeSletanja" type="datetime-local"></sf:input></td>
				</tr>
				<tr>
					<td>Unesi bazicnu cenu leta: </td>
					<td><sf:input path="cenaLet" type="number" step="0.01"></sf:input></td>
				</tr>
			</table>
			<input type="submit" value="Prikazi avione"> 
		</sf:form>
		<br>
		<form action="/Aerodrom/letovi/sacuvajLet" method="post" >
			Izaberite avion: 
			<select name="idAviona">
				<c:forEach var="a" items="${avioni}">
					<option value="${a.id}">${a.id} - ${a.model}</option>
				</c:forEach>
			</select>
			<input type="submit" value="Sacuvaj let"> 
		</form>
		
		<c:if test="${not empty poruka}">
			<div class="succes">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Uspeh!${poruka}</strong>
			</div>
		</c:if>
	</div>
</body>
</html>
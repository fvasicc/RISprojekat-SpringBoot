<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Pretraga letova</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
	<c:url var="loginUrl" value="/login" />
	<ul class="navbar">
		<li><a href="/Aerodrom">Pocetna</a></li>
		<li><a class="active" href="/Aerodrom/letovi/pretragaLetova"><b>Pretrazi letove</b></a></li>
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
			<li style="float:right;"><a href="/Aerodrom/user/account"><b>Moj nalog</b></a></li>
		</security:authorize>
	</ul>
	
	<div class="main">
		<form action="/Aerodrom/letovi/prikaziLetove" method="get">
			<select name="idGradOd">
				<c:forEach items="${gradovi}" var="gr">
					<option value="${gr.id}">${gr.grad}</option>
				</c:forEach>
			</select>
			<select name="idGradDo">
				<c:forEach items="${gradovi}" var="gr">
					<option value="${gr.id}">${gr.grad}</option>
				</c:forEach>
			</select>
			<input name="datumOd" type="datetime-local" placeholder="najraniji datum polaska"/>
			<input name="datumDo" type="datetime-local" placeholder="najkasniji datum polaska"/>
			<input type="submit" value="Prikazi">
		</form>
		<br>
		<c:if test="${not empty letovi}">
			<table border="1" class="fixedheader prikazLetova">
				<tr>
					<th>Polazna stanica</th>
					<th>Destinacija</th>
					<th>Vreme polaska</th>
					<th>Ocekivano vreme sletanja</th>
					<th>Avion</th>
					<th>Status leta</th>
					<th></th>
				</tr>
				<c:forEach items="${letovi}" var="let">
					<tr>
						<td>${let.polazniGrad.grad}</td>
						<td>${let.destinacija.grad}</td>
						<td><fmt:formatDate value="${let.vremePolaska}" type="both" timeStyle="short"/></td>
						<td><fmt:formatDate value="${let.vremeSletanja}" type="both" timeStyle="short"/></td>
						<td>${let.aavion.model}</td>
						<td>${let.astatusleta.status }</td>
						<td><a href="/Aerodrom/rezervacija/rezervisi?idLet=${let.id}">Rezervisi</a></td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</div>
</body>
</html>
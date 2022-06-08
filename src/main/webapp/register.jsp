<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/style.css">
<title>Registracija</title>
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
		<security:authorize access="isAuthenticated()"> 
			<li style="float: right;"><a href="/Aerodrom/auth/logout"><b>Odjava</b></a></li>
		</security:authorize>
		<security:authorize access="hasRole('USER')">	
			<li style="float:right;"><a href="/Aerodrom/user/account"><b>Moj nalog</b></a></li>
		</security:authorize>
	</ul>
	
	<div class="main">
		<sf:form modelAttribute="user" action="register" method="post">
			<table style="margin-left: auto; margin-right: auto;">
				<tr>
					<td>Ime:</td>
					<td style="width: 250px;"><sf:input path="ime" required="required"/></td>
				</tr>
				<tr>
					<td>Prezime:</td>
					<td style="width: 250px;"><sf:input path="prezime" required="required"/></td>
				</tr>
				<tr>
					<td>Korisnicko ime:</td>
					<td style="width: 250px;"><input name="korisnickoIme" required/></td>
				</tr>
				<tr>
					<td>Sifra:</td>
					<td style="width: 250px;"><input type="password" name="sifra" required/></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td style="width: 250px;"><sf:input path="email" required="required"/></td>
				</tr>
				<tr>
					<td>JMBG:</td>
					<td style="width: 250px;"><sf:input path="jmbg" size="13" required="required"/></td>
				</tr>
				<tr>
					<td>telefon:</td>
					<td style="width: 250px;"><sf:input path="telefon" required="required" /></td>
				</tr>
				<tr>
					<td />
					<td style="width: 250px;"><input type="submit" value="Sacuvaj">
				</tr>
			</table>
		</sf:form>
		
		<c:if test="${not empty porukaNeuspeh}">
			<div class="alert">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Greska!</strong>${porukaNeuspeh}
			</div>
		</c:if>
	</div>
</body>
</html>
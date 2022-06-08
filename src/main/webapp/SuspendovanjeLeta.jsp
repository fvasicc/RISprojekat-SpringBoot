<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Suspenduj let</title>
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
			<li><a class="active" href="/Aerodrom/SuspendovanjeLeta"><b>Suspenduj let</b></a></li>
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
		<form action="/Aerodrom/manager/suspendujLet" method="post">
			<input type="number" name="letZaSuspendovanje" min="1" placeholder="Unesite id leta koji zelite da suspendujete" required>
			<input type="submit" value="Suspenduj">
		</form>
		
		<c:if test="${not empty porukaUspeh}">
			<div class="succes">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Uspeh!</strong>${porukaUspeh}
			</div>
		</c:if>
		
		<c:if test="${not empty porukaNeuspeh}">
			<div class="alert">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Greska!</strong>${porukaNeuspeh}
			</div>
		</c:if>
	</div>
</body>
</html>
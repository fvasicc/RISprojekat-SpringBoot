<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Rezervacije</title>
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
			<li style="float:right;"><a href="/Aerodrom/user/account"><b>Moj nalog</b></a></li>
		</security:authorize>
	</ul>
	
	<div class="main">
		<c:if test="${not empty rezervacijePutnika}">
			<table class="prikazLetova">
				<tr><th>Datum rezervacije</th><th>Broj karata</th><th>Let</th><th>Datum leta</th></tr>
				<c:forEach var="r" items="${rezervacijePutnika}">
					<tr>
						<td><fmt:formatDate value="${r.datumRezervacije}" pattern="dd/MM/yyyy"/></td>
						<td style="text-align: center;">${fn:length(r.akartas)}</td> 
						<td>${r.akartas[0].alet.polazniGrad.grad} - ${r.akartas[0].alet.destinacija.grad}</td>
						<td><fmt:formatDate value="${r.akartas[0].alet.vremePolaska}" pattern="dd/MM/yyyy"/></td>
						<td><a href="/Aerodrom/user/otkazivanjeRezervacije?rezervacijaZaUklanjanje=${r.id}">Ponisti rezervaciju</a></td>
						<td><a href="/Aerodrom/user/rezervacija.pdf?izabranaRezervacija=${r.id}">Preuzmi rezervaciju</a></td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		<c:if test="${not empty porukaUspeh}">
			<div class="succes">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Uspeh!</strong>${porukaUspeh}
			</div>
			<%
				request.getSession().removeAttribute("porukaUspeh");
			%>
		</c:if>
		
		<c:if test="${not empty porukaNeuspeh}">
			<div class="alert">
				<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
				<strong>Greska!</strong>${porukaNeuspeh}
			</div>
			<%
				request.getSession().removeAttribute("porukaNeuspeh");
			%>
		</c:if>
	</div>
</body>
</html>
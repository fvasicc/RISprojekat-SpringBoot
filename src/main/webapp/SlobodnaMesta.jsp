<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Rezervacija</title>
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
	
	<div id="prikazKarata" class="main">
		<table border="1">
			<tr>
				<td>Biznis klasa</td><td><a href="/Aerodrom/klase/info" target="_blank">INFO</a></td>
			</tr>
			<tr>	
				<td>Slobodnih mesta: ${brBiznis}</td>
				<td><a href="/Aerodrom/rezervacija/rezervisiLet?idLet=${idLet}&idKlase=${idBiznis}">Rezervisi</a></td>
			</tr>
		</table>
		
		<table border="1">
			<tr>
				<td>Economy comfort klasa</td><td><a href="/Aerodrom/klase/info" target="_blank">INFO</a></td>
			</tr>
			<tr>
				<td>Slobodnih mesta: ${brEcoComfort}</td>
				<td><a href="/Aerodrom/rezervacija/rezervisiLet?idLet=${idLet}&idKlase=${idComfort}">Rezervisi</a></td>
			</tr>
		</table>
		
		<table border="1">
			<tr>
				<td>Economy standard klasa</td><td><a href="/Aerodrom/klase/info" target="_blank">INFO</a></td>
			</tr>
			<tr>
				<td>Slobodnih mesta: ${brEcoStandard}</td>
				<td><a href="/Aerodrom/rezervacija/rezervisiLet?idLet=${idLet}&idKlase=${idStandard}">Rezervisi</a></td>
			</tr>
		</table>
		
		<table border="1">
			<tr>
				<td>Economy light klasa</td><td><a href="/Aerodrom/klase/info" target="_blank">INFO</a></td>
			</tr>
			<tr>
				<td>Slobodnih mesta: ${brEcoLight}</td>
				<td><a href="/Aerodrom/rezervacija/rezervisiLet?idLet=${idLet}&idKlase=${idLight}">Rezervisi</a></td>	
			</tr>
		</table>
		
		<form action="/Aerodrom/rezervacija/zavrsiRezervaciju" method="post">
			<c:if test="${not empty cena}">
				Cena karte za izabrani let je: ${cena} <br>
				<security:authorize access="!hasRole('USER')" > 
				<!-- ako nije ulogovan korisnik-->
					<p>Da biste rezervisali kartu morate biti ulogovani. Ulogujte se na <a href="/Aerodrom/auth/loginPage">linku</a></p>
				</security:authorize>
				<security:authorize access="hasRole('USER')" > 
					Unesite broj karata za rezervaciju: <input name="brojKarata" type="number" max="5" min="1" required="required"/> <br>
					<td><input type="submit" class="btn" value="Sacuvaj"/>
				</security:authorize>
			</c:if>
		</form>
		
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
			</div><%
				request.getSession().removeAttribute("porukaNeuspeh");
			%>
		</c:if>
	</div>
</body>
</html>
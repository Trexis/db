<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="includes/directives.jspf" %>
<!DOCTYPE html>
<html xmlns:social="http://spring.io/springsocial">
	<head>
		<title>DBX</title>
		<%@ include file="includes/head.jsp" %>
	</head>
	<body>
		<h3>Your Connections</h3>
		Providers: ${providerIds}
		ConnectionMap: ${connectionMap}
		
		<c:forEach items="${providerIds}" var="providerId">
		test
		</c:forEach>
		
		
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="includes/directives.jspf" %>
<!DOCTYPE html>
<html xmlns:social="http://spring.io/springsocial">
	<head>
		<title>DBX Administrative Login</title>
		<%@ include file="includes/head.jsp" %>
	</head>
	<body>
		<div class="header">
			<h1><a>DBX Administrative Login</a></h1>
			<span>You are seeing this page because you are required to have elevated permissions for the URL you requested.</span>
		</div>
		<div class="content">
			<form class="signin" action="${contextPath}/security/authenticate" method="post">
				<input type="hidden" name="_csrf" value="${_csrf.token}"></input>
				<div class="formInfo">
					<c:if test="${param.error eq 'bad_credentials'}">
			  		<div class="error">
			  			Your sign in information was incorrect.
			  			Please try again or <a href="${contextPath}/security/enroll">enroll</a>.
			  		</div>
			  		</c:if>
			  		<c:if test="${param.error eq 'multiple_users'}">
			  		<div class="error">
			  			Multiple local accounts are connected to the provider account.
			  			Try again with a different provider or with your username and password.
			  		</div>
			  		</c:if>
				</div>
				<fieldset>
					<label for="login">Username</label>
					<input class="login" name="username" type="text" size="25"></input>
					<label for="password">Password</label>
					<input class="password" name="password" type="password" size="25"></input>
				</fieldset>
				<button type="submit">Sign In</button>
				
				<p>Or you can <a href="${contextPath}/security/enroll">enroll</a> with a new account.  This will not grant you administrative access, but will create your user account in DBX.</p>
			</form>

			<!-- TWITTER SIGNIN -->
			<form class="tw_signin" action="${contextPath}/security/twitter" method="POST">
				<input type="hidden" name="_csrf" value="${_csrf.token}"></input>
				<button type="submit"><img src="${contextPath}/resources/system/media/social/twitter/sign-in-with-twitter-d.png"></img></button>
			</form>
		
			<!-- FACEBOOK SIGNIN -->
			<form name="fb_signin" class="fb_signin" action="${contextPath}/security/facebook" method="POST">
				<input type="hidden" name="_csrf" value="${_csrf.token}"></input>
				<input type="hidden" name="scope" value="publish_stream,user_photos,offline_access"></input>
				<button type="submit"><img src="${contextPath}/resources/system/media/social/facebook/sign-in-with-facebook.png"></img></button>
			</form>
		
			<!-- LINKEDIN SIGNIN -->
			<form name="li_signin" class="li_signin" action="${contextPath}/security/linkedin" method="POST">
				<input type="hidden" name="_csrf" value="${_csrf.token}"></input>
				<button type="submit">Sign In with LinkedIn</button>
			</form>
		</div>		
	</body>
</html>

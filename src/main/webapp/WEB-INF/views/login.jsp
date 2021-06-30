<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: zbigniew
  Date: 30.06.2021
  Time: 09:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/views/includes/headContent.jsp"/>
</head>
<body>

<div class="container-fluid">

    <div class="row">

        <div class="col-4"></div>

        <div class="col-4 d-flex justify-content-center align-item-center" style="min-height: 50vh">

            <form class="form-signin d-flex flex-column justify-content-center align-item-center" method="post" action="/login">
                <h2 class="form-signin-heading">Witaj w Nox Hours</h2>
                <p>
                    <label for="username" class="sr-only">Username</label>
                    <input type="text" id="username" name="username" class="form-control" placeholder="adres e-mail"
                           required autofocus>
                </p>
                <p>
                    <label for="password" class="sr-only">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="hasło"
                           required>
                </p>
                <c:if test="${!empty param.error}">
                    <p>błędne dane logowania</p>
                </c:if>
                <c:if test="${!empty param.logout}">
                    <p>zostałeś poprawnie wylogowany</p>
                </c:if>
<%--                <input name="_csrf" type="hidden" value="8bcd1181-ad6f-4d11-b3fb-058ab4ceb2f8"/>--%>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Zaloguj</button>
            </form>

        </div>

        <div class="col-4"></div>

    </div>

</div>

</body>
</html>

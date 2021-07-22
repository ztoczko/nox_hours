<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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

            <form class="form-signin d-flex flex-column justify-content-center align-item-center" method="post"
                  action="/login" novalidate>
                <h2 class="form-signin-heading"><spring:message code="login.hello.msg"/></h2>
                <p>
                    <label for="username" class="sr-only"><spring:message code="login.login"/></label>
                    <input type="text" id="username" name="username" class="form-control"
                           placeholder="<spring:message code="login.login"/>"
                           required autofocus>
                </p>
                <p>
                    <label for="password" class="sr-only"><spring:message code="login.password"/></label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="<spring:message code="login.password"/>"
                           required>
                </p>
                <c:if test="${!empty param.passwordResetSuccess}">
                    <p><spring:message code="login.reset.success.msg"/></p>
                </c:if>
                <c:if test="${!empty param.resetRequest}">
                    <p><spring:message code="login.reset.request.msg"/></p>
                </c:if>
                <c:if test="${!empty param.error}">
                    <p><spring:message code="login.error.msg"/></p>
                </c:if>
                <c:if test="${!empty param.logout}">
                    <p><spring:message code="login.logout.msg"/></p>
                </c:if>
                <%--                <input name="_csrf" type="hidden" value="8bcd1181-ad6f-4d11-b3fb-058ab4ceb2f8"/>--%>
                <button class="btn btn-lg btn-primary btn-block mb-3" type="submit"><spring:message
                        code="login.login.msg"/></button>
                <button class="btn btn btn-block border-primary" type="button" onclick="location.href='/reset'"><spring:message
                        code="login.password.forgotten"/></button>
            </form>

        </div>

        <div class="col-4"></div>

    </div>

</div>

</body>
</html>

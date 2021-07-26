<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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

            <form:form modelAttribute="user"
                       cssClass="form-signin d-flex flex-column justify-content-center align-item-center" method="post">
                <form:hidden path="resetKey" value="${user.resetKey}"/>
                <p><spring:message code="login.login"/></p>
                <form:input path="email" cssClass="form-control"
                            cssErrorClass="form-control text-danger border-danger"/>
                <form:errors path="email" cssClass="text-danger mx-2"/>
                <p><spring:message code="settings.password.new"/></p>
                <form:password path="newPassword" cssClass="form-control"
                               cssErrorClass="form-control text-danger border-danger"/>
                <form:errors path="newPassword" cssClass="text-danger mx-2"/>
                <p><spring:message code="settings.password.new.repeat"/></p>
                <form:password path="newPasswordRepeated" cssClass="form-control"
                               cssErrorClass="form-control text-danger border-danger"/>
                <form:errors path="newPasswordRepeated" cssClass="text-danger mx-2"/>
                <button class="btn btn-lg btn-primary btn-block m-3" type="button" onclick="location.href='/login'">
                    <spring:message
                            code="app.back"/></button>
                <button class="btn btn-lg btn-primary btn-block m-3" type="submit"><spring:message
                        code="app.save"/></button>

            </form:form>

        </div>

        <div class="col-4"></div>
    </div>
</div>

</body>
</html>

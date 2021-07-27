<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
                  action="/reset" novalidate>
                <h4 class="form-signin-heading"><spring:message code="login.reset.request"/></h4>
                <p>
                    <label for="username" class="sr-only"><spring:message code="login.login"/></label>
                    <input type="text" id="username" name="email" class="form-control"
                           placeholder="<spring:message code="login.login"/>"
                           required autofocus>
                </p>

                <div class="d-flex">
                    <button class="btn btn-lg btn-primary btn-block m-3" type="button" onclick="location.href='/login'">
                        <spring:message
                                code="app.back"/></button>
                    <button class="btn btn-lg btn-primary btn-block m-3" type="submit"><spring:message
                            code="app.send"/></button>
                </div>
            </form>

        </div>

        <div class="col-4"></div>

    </div>
</div>

</body>
</html>

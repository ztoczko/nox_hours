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

        <div class="col-4 d-flex justify-content-center align-item-center flex-column" style="min-height: 50vh">

            <div class="m-3">
                <h3><spring:message code="error.title"/></h3>
            </div>
            <div class="m-3">
                <c:if test="${!empty code}">
                    <h5><spring:message code="error.code"/>: ${code}</h5>
                </c:if>
                <h5>
                    <c:choose>
                        <c:when test="${empty code}">
                            <spring:message code="error.general.message"/>
                        </c:when>
                        <c:when test="${code == 403}">
                            <spring:message code="error.403.message"/>
                        </c:when>
                        <c:when test="${code == 404}">
                            <spring:message code="error.404.message"/>
                        </c:when>
                        <c:when test="${code == 500}">
                            <spring:message code="error.500.message"/>
                        </c:when>
                    </c:choose>
                </h5>
            </div>
            <div class="m-3 d-flex justify-content-center">
                <button type="button" class="btn btn-primary" onclick="location.href='/dashboard'"><spring:message code="app.back"/> </button>
            </div>

        </div>

        <div class="col-4"></div>

    </div>

    <c:if test="${!empty errorMsg}">

        <div class="row">
            <div class="col">
                ${errorMsg}
            </div>
        </div>

    </c:if>

</div>

</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: zbigniew
  Date: 29.06.2021
  Time: 19:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="/WEB-INF/views/includes/headContent.jsp"/>
<body class="p-0 m-0" style="width: 100vw; overflow-x: hidden;">
<div class="containter-fluid">

    <!--    HEADER START-->
    <jsp:include page="/WEB-INF/views/includes/header.jsp"/>
    <!--    HEADER END-->

    <div class="row">
        <div class="col-2 p-0 m-0 sideMenu">

            <!--            SIDE MENU START-->
            <jsp:include page="/WEB-INF/views/includes/sidemenu.jsp"/>
            <!--            SIDE MENU END-->

        </div>
        <div class="col-10 p-4 mainBody">

            <div class="mainContentDiv">

                <!--            CASE DETAILS START-->
                <form:form modelAttribute="aCase" method="post" cssClass="editForm">

                    <div>
                        <div><spring:message code="case.name"/>:</div>
                        <div><form:input path="name" cssClass="form-control"
                                         cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"/>
                            <form:errors path="name" cssClass="text-danger mx-2"/></div>
                    </div>
                    <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                        <div><spring:message code="case.closed"/>:</div>
                        <div><form:checkbox path="closed" cssClass="form-check-input p-0 m-0"/></div>
                    </div>

                    <div>
                        <button type="button" class="button mx-2" onclick="location.href='/clients/show/${aCase.client.id}'">
                            <spring:message code="app.back"/>
                        </button>
                        <button type="submit" class="button mx-2"><spring:message code="app.save"/></button>
                    </div>
                </form:form>
                <!--            CASE DETAILS END-->
            </div>
        </div>
    </div>

    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>
</body>
</html>
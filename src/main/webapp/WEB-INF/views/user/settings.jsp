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

                <!--            USER DETAILS START-->
                <form:form modelAttribute="user" action="/settings/show" method="post" cssClass="editForm mb-0">
                    <form:hidden path="id" value="${user.id}"/>

                    <div>
                        <div>
                            <spring:message code="user.firstName"/>
                        </div>
                        <div>
                            <form:input path="firstName" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"
                                        disabled="true"/>
                            <form:errors path="firstName" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="user.lastName"/>
                        </div>
                        <div>
                            <form:input path="lastName" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"
                                        disabled="true"/>
                            <form:errors path="lastName" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="user.email"/>
                        </div>
                        <div>
                            <form:input path="email" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"
                                        disabled="true"/>
                            <form:errors path="email" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="mb-2">
                        <button type="button" class="button mx-2" onclick="location.href='/dashboard'">
                            <spring:message code="app.back"/>
                        </button>

                        <button type="button" class="button mx-2 ${edit ? "d-none" : ""}" id="toggleEdit">
                            <spring:message code="app.edit"/>
                        </button>
                        <button type="submit" class="button mx-2 ${edit ? "" : "d-none"}" id="saveEdit">
                            <spring:message code="app.save"/>
                        </button>
                        <button type="button" class="button mx-2" onclick="location.href='/settings/changePassword'">
                            <spring:message code="app.change.password"/>
                        </button>

                    </div>

                </form:form>
                <!-- USER DETAILS END-->

                <%--                    MESSAGES START--%>
                <c:if test="${!empty param.editSuccess}">
                    <div class="text-success"><spring:message code="settings.updated.msg"/></div>
                </c:if>
                <%--                    MESSAGES END--%>

            </div>

        </div>

    </div>

    <c:if test="${forceLogout}">
        <div class="modal fade" id="logoutModal" tabindex="-1" aria-hidden="true" data-bs-backdrop="static"
             data-bs-keyboard="false">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body">
                        <p><spring:message code="settings.force.logout.msg"/>
                            <span id="timer"></span><spring:message code="settings.second"/></p>
                    </div>
                    <div class="modal-footer d-flex justify-content-around">
                        <button type="button" class="button" style="min-width: 25%"
                                onclick="location.href='/logout'"><spring:message
                                code="sidemenu.logout"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>
<script src="/js/settingsScript.js" defer>
</script>
</body>
</html>
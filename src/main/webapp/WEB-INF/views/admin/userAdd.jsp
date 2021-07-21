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
                <form:form modelAttribute="user" action="/admin/add" method="post" cssClass="editForm mb-0"
                           data-superadmin="${loggedUserSuperAdminStatus}">

                    <div>
                        <div>
                            <spring:message code="user.firstName"/>:
                        </div>
                        <div>
                            <form:input path="firstName" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="width: 50%"/>
                            <form:errors path="firstName" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="user.lastName"/>:
                        </div>
                        <div>
                            <form:input path="lastName" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="width: 50%"/>
                            <form:errors path="lastName" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="user.email"/>:
                        </div>
                        <div>
                            <form:input path="email" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="width: 50%"/>
                            <form:errors path="email" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="user.rank"/>:
                        </div>
                        <div>
                            <form:select path="rank" cssClass="form-select"
                                         cssErrorClass="form-select text-danger border-danger"
                                         cssStyle="width: 50%">
                                <form:option value="1"><spring:message code="user.rank.student"/></form:option>
                                <form:option value="2"><spring:message code="user.rank.applicant"/></form:option>
                                <form:option value="3"><spring:message code="user.rank.attorney"/></form:option>
                                <form:option value="4"><spring:message code="user.rank.partner"/></form:option>
                            </form:select>
                            <form:errors path="rank" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="user.privileges"/>:
                        </div>

                        <div class="checkBoxGroup">
                            <spring:message code="user.role.user" var="roleUser"/>
                            <spring:message code="user.role.rates" var="roleRates"/>
                            <spring:message code="user.role.admin" var="roleAdmin"/>
                            <spring:message code="user.role.superadmin" var="roleSuperadmin"/>
                            <div>
                                <form:checkbox path="privileges" value="U" cssClass="form-check-input" checked="true"
                                               onclick="return false;" label="${roleUser}"/><br>
                            </div>
                            <div>
                                <form:checkbox path="privileges" value="R" cssClass="form-check-input"
                                               label="${roleRates}"/><br>
                            </div>
                            <div>
                                <form:checkbox path="privileges" value="A" cssClass="form-check-input"
                                               label="${roleAdmin}"/><br>
                            </div>
                            <div>
                                <form:checkbox path="privileges" value="S" cssClass="form-check-input"
                                               label="${roleSuperadmin}"/>
                            </div>
                            <form:errors path="privileges" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                        <div><spring:message code="user.locked"/>:</div>
                        <div><form:checkbox path="isLocked" cssClass="form-check-input p-0 m-0"/></div>
                    </div>

                    <div class="mb-2">
                        <button type="button" class="button mx-2" onclick="location.href='/admin/list/'">
                            <spring:message code="app.back"/>
                        </button>

                        <button type="submit" class="button mx-2" id="saveEdit">
                            <spring:message code="app.save"/>
                        </button>
                    </div>

                </form:form>
                <!-- USER DETAILS END-->

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

<script>
    if (document.querySelector("[data-superadmin]").dataset.superadmin === "false") {
        document.getElementById("privileges4").setAttribute("onclick", "return false;");
    }
</script>
</body>
</html>
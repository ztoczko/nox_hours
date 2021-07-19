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
                <form:form modelAttribute="user" action="/admin/edit" method="post" cssClass="editForm mb-0"
                           data-user-permission="${userPermission}" data-superadmin="${loggedUserSuperAdminStatus}">
                    <form:hidden path="id" value="${user.id}"/>

                    <div>
                        <div>
                            <spring:message code="user.firstName"/>:
                        </div>
                        <div>
                            <form:input path="firstName" disabled="true" cssClass="form-control"
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
                            <form:input path="lastName" disabled="true" cssClass="form-control"
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
                            <form:input path="email" disabled="true" cssClass="form-control"
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
                            <form:select path="rank" disabled="true" cssClass="form-control"
                                         cssErrorClass="form-control text-danger border-danger"
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
                                <form:checkbox path="privileges" value="U" cssClass="form-check-input" disabled="true"
                                               onclick="return false;" label="${roleUser}"/><br>
                            </div>
                            <div>
                                <form:checkbox path="privileges" value="R" cssClass="form-check-input" disabled="true"
                                               label="${roleRates}"/><br>
                            </div>
                            <div>
                                <form:checkbox path="privileges" value="A" cssClass="form-check-input" disabled="true"
                                               label="${roleAdmin}"/><br>
                            </div>
                            <div>
                                <form:checkbox path="privileges" value="S" cssClass="form-check-input" disabled="true"
                                               label="${roleSuperadmin}"/>
                            </div>
                            <form:errors path="privileges" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                        <div><spring:message code="user.locked"/>:</div>
                        <div><form:checkbox path="isLocked" cssClass="form-check-input p-0 m-0" disabled="true"/></div>
                    </div>

                    <div class="mb-2">
                        <button type="button" class="button mx-2" onclick="location.href='/admin/list/'">
                            <spring:message code="app.back"/>
                        </button>
                        <c:if test="${userPermission}">
                            <button type="button" class="button mx-2 ${edit ? "d-none" : ""}" id="toggleEdit">
                                <spring:message code="app.edit"/>
                            </button>
                            <button type="submit" class="button mx-2 ${edit ? "" : "d-none"}" id="saveEdit">
                                <spring:message code="app.save"/>
                            </button>
                            <button type="button" class="button mx-2"
                                    onclick="${"location.href='/admin/resetPassword/".concat(user.id).concat("'")}">
                                <spring:message code="app.reset.password"/>
                            </button>
                        </c:if>
                        <c:if test="${loggedUserSuperAdminStatus}">
                            <button type="button" class="button mx-2" data-bs-toggle="modal"
                                    data-bs-target="#deleteModal"><spring:message code="app.delete"/>
                            </button>
                        </c:if>

                    </div>

                </form:form>
                <!-- USER DETAILS END-->

                <%--                    MESSAGES START--%>
                <c:if test="${!empty param.editSuccess}">
                    <div class="text-success"><spring:message code="admin.show.updated.msg"/></div>
                </c:if>
                <%--                    MESSAGES END--%>

            </div>
        </div>
    </div>

    <%--    USER DELETE MODAL START--%>
    <c:if test="${loggedUserSuperAdminStatus}">
        <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body">
                        <p><spring:message code="admin.show.delete.confirmation.msg"
                                           arguments="${user.firstName}, ${user.lastName}"/></p>
                        <p><spring:message code="admin.show.delete.confirmation.msg2"/></p>
                        <c:if test="${user.id == loggedUserId}">
                            <p><spring:message code="admin.show.delete.confirmation.msg3"/></p>
                        </c:if>
                    </div>
                    <div class="modal-footer d-flex justify-content-around">
                        <button type="button" class="button" style="min-width: 25%"
                                onclick="location.href='/admin/delete/${user.id}'"><spring:message
                                code="app.yes"/>
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal">
                            <spring:message code="app.no"/></button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <%--    USER DELETE MODAL END--%>

    <%--FORCE LOGOUT MODAL START--%>
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
    <%--FORCE LOGOUT MODAL END--%>

    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>

<script src="/js/userShowScript.js" defer>
</script>
</body>
</html>
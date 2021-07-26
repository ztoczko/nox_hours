<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
                <form:form modelAttribute="aCase" method="post" cssClass="editForm mb-0">
                    <form:hidden path="id" value="${aCase.id}"/>
                    <form:hidden path="created" value="${aCase.createdString}"/>
                    <form:hidden path="client" value="${aCase.client.id}"/>

                    <div>
                        <div>
                            <spring:message code="case.created"/>:
                        </div>
                        <div>
                            <div style="padding: .375rem .75rem;">
                                    ${aCase.createdString}
                            </div>

                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="case.client"/>:
                        </div>
                        <div>
                            <div style="padding: .375rem .75rem;">
                                    ${aCase.client.name}
                            </div>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="case.name"/>:
                        </div>
                        <div>
                            <form:input path="name" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"
                                        disabled="true"/>
                            <form:errors path="name" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                        <div>
                            <spring:message code="case.closed"/>
                        </div>
                        <div>
                            <form:checkbox path="closed" cssClass="form-check-input p-0 m-0" disabled="true"/>
                        </div>
                    </div>

                    <div class="mb-2">
                        <button type="button" class="button mx-2"
                                onclick="${"location.href='/clients/show/".concat(aCase.client.id).concat("'")}">
                            <spring:message code="app.back"/>
                        </button>

                        <button type="button" class="button mx-2 ${edit ? "d-none" : ""}" id="toggleEdit">
                            <spring:message code="app.edit"/>
                        </button>
                        <button type="submit" class="button mx-2 ${edit ? "" : "d-none"}" id="saveEdit">
                            <spring:message code="app.save"/>
                        </button>
                        <sec:authorize access="hasAuthority('ADMIN')">
                            <button type="button" class="button mx-2" data-bs-toggle="modal"
                                    data-bs-target="#deleteModal"><spring:message code="app.delete"/>
                            </button>
                        </sec:authorize>
                    </div>

                </form:form>
                <!-- CASE DETAILS END-->

                <%--                    MESSAGES START--%>
                <c:if test="${!empty param.editSuccess}">
                    <div class="text-success"><spring:message code="case.show.updated.msg"/></div>
                </c:if>
                <%--                    MESSAGES END--%>
            </div>
        </div>
    </div>

    <%--    CASE DELETE MODAL START--%>
    <sec:authorize access="hasAuthority('ADMIN')">
        <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body">
                        <p><spring:message code="case.show.delete.confirmation.msg"
                                           arguments="${aCase.name}, ${aCase.client.name}"/></p>
                    </div>
                    <div class="modal-footer d-flex justify-content-around">
                        <button type="button" class="button" style="min-width: 25%"
                                onclick="location.href='/clients/${aCase.client.id}/case/delete/${aCase.id}'">
                            <spring:message
                                    code="app.yes"/>
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal">
                            <spring:message code="app.no"/></button>
                    </div>
                </div>
            </div>
        </div>
    </sec:authorize>>
    <%--    CASE DELETE MODAL END--%>

    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>

<script src="/js/caseShowScript.js" defer>
</script>
</body>
</html>
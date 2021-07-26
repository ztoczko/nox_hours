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

                <!--            TIMESHEET DETAILS START-->
                <form:form modelAttribute="timesheet" action="/timesheet/edit" method="post" cssClass="editForm mb-0">
                    <form:hidden path="id" value="${timesheet.id}"/>
                    <form:hidden path="user" value="${timesheet.userNameDTO.id}"/>
                    <form:hidden path="created" value="${timesheet.createdString}"/>
                    <form:hidden path="rankWhenCreated" value="${timesheet.rankWhenCreated}"/>

                    <div>
                        <div>
                            <spring:message code="timesheet.created"/>:
                        </div>
                        <div>
                            <div style="padding: .375rem .75rem;">
                                    ${timesheet.createdString}
                            </div>

                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.user"/>:
                        </div>
                        <div>
                            <form:input path="userNameDTO.fullName" disabled="true" cssClass="form-control"
                                        cssStyle="width: 50%"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.rank.when.created"/>:
                        </div>
                        <div>
                            <div class="${timesheet.rankWhenCreated == 1 ? "" : "d-none"}"
                                 style="padding: .375rem .75rem;" data-rank-id-1><spring:message
                                    code="user.rank.student"/></div>
                            <div class="${timesheet.rankWhenCreated == 2 ? "" : "d-none"}"
                                 style="padding: .375rem .75rem;" data-rank-id-2><spring:message
                                    code="user.rank.applicant"/></div>
                            <div class="${timesheet.rankWhenCreated == 3 ? "" : "d-none"}"
                                 style="padding: .375rem .75rem;" data-rank-id-3><spring:message
                                    code="user.rank.attorney"/></div>
                            <div class="${timesheet.rankWhenCreated == 4 ? "" : "d-none"}"
                                 style="padding: .375rem .75rem;" data-rank-id-4><spring:message
                                    code="user.rank.partner"/></div>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.client"/>:
                        </div>

                        <div id="clientDiv">
                            <form:select path="client" cssClass="form-select"
                                         cssErrorClass="form-select text-danger border-danger"
                                         cssStyle="min-width:50%; max-width: 50%" disabled="true">
                                <form:options items="${clients}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <form:errors path="client" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.case"/>:
                        </div>

                        <div id="caseDiv" data-case-id="${empty timesheet.clientCase ? "-1" : timesheet.clientCase.id}">
                            <form:select path="clientCase" cssClass="form-select"
                                         cssErrorClass="form-select text-danger border-danger"
                                         cssStyle="min-width:50%; max-width: 50%" disabled="true">
                                <form:option value="-1"><spring:message code="case.no.case"/></form:option>
                            </form:select>
                            <form:errors path="clientCase" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.date.executed"/>
                        </div>
                        <div class="input-group date datepicker" data-date-format="${dateFormat}">
                            <form:input path="dateExecuted" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="min-width:20%; max-width: 50%"
                                        readonly="true" disabled="true"/>
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            <form:errors path="dateExecuted" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.hours"/>
                        </div>
                        <div>
                            <form:input path="hours" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"
                                        disabled="true"/>
                            <form:errors path="hours" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.minutes"/>
                        </div>
                        <div>
                            <form:input path="minutes" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"
                                        disabled="true"/>
                            <form:errors path="minutes" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.description"/>
                        </div>
                        <div>
                            <form:textarea path="description" cssClass="form-control"
                                           cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"
                                           disabled="true" rows="2"/>
                            <form:errors path="description" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="mb-2">
                        <button type="button" class="button mx-2"
                                onclick="${"location.href='/clients/show/".concat(timesheet.client.id).concat("'")}">
                            <spring:message code="app.back"/>
                        </button>
                        <c:if test="${timesheetPermission}">
                            <button type="button" class="button mx-2 ${edit ? "d-none" : ""}" id="toggleEdit">
                                <spring:message code="app.edit"/>
                            </button>
                            <button type="submit" class="button mx-2 ${edit ? "" : "d-none"}" id="saveEdit">
                                <spring:message code="app.save"/>
                            </button>
                            <button type="button" class="button mx-2" data-bs-toggle="modal"
                                    data-bs-target="#deleteModal"><spring:message code="app.delete"/>
                            </button>
                        </c:if>
                    </div>

                </form:form>
                <!-- TIMESHEET DETAILS END-->

                <%--                    MESSAGES START--%>
                <c:if test="${!empty param.timesheetEditSuccess}">
                    <div class="text-success"><spring:message code="timesheet.show.updated.msg"/></div>
                </c:if>
                <%--                    MESSAGES END--%>

            </div>
        </div>
    </div>

    <div id="casesList" class="d-none">

        <c:forEach var="aCase" items="${cases}">
            <div data-client-id="${aCase.client.id}" data-case-id="${aCase.id}" data-case-name="${aCase.name}"></div>
        </c:forEach>

    </div>

    <%--    TIMESHEET DELETE MODAL START--%>
    <c:if test="${timesheetPermission}">
        <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body">
                        <p><spring:message code="timesheet.show.delete.confirmation.msg"
                                           arguments="${timesheet.client.name}, ${timesheet.dateExecutedString}"/></p>
                    </div>
                    <div class="modal-footer d-flex justify-content-around">
                        <button type="button" class="button" style="min-width: 25%"
                                onclick="location.href='/timesheet/delete/${timesheet.id}'"><spring:message
                                code="app.yes"/>
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal">
                            <spring:message code="app.no"/></button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <%--    TIMESHEET DELETE MODAL END--%>

    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>
<script src="/js/bootstrap-datepicker.js"></script>
<script src="/js/timesheetShowScript.js" defer>
</script>
</body>
</html>
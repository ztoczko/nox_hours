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
                <form:form modelAttribute="timesheet" action="/timesheet/add" method="post"
                           cssClass="editForm">
                    <form:hidden path="rankWhenCreated" value="${userRank}"/>

                    <c:choose>
                        <c:when test="${loggedUserAdminStatus}">
                            <div>
                                <div>
                                    <spring:message code="timesheet.user"/>
                                </div>
                                <div>
                                    <form:select path="user" items="${users}" itemValue="id" itemLabel="fullName"
                                                 cssClass="form-select"
                                                 cssErrorClass="form-select text-danger border-danger"
                                                 cssStyle="max-width: 50%" data-user-id="${timesheet.user.id}"/>
                                    <form:errors path="user" cssClass="text-danger mx-2"/>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form:hidden path="user" value="${userId}"/>
                        </c:otherwise>

                    </c:choose>
                    <div>
                        <div>
                            <spring:message code="timesheet.client"/>:
                        </div>

                        <div id="clientDiv" data-client-id="${!empty client ? client.id : "0"}">
                            <form:select path="client" cssClass="form-select"
                                         cssErrorClass="form-select text-danger border-danger"
                                         cssStyle="min-width:50%; max-width: 50%">
                                <c:if test="${empty client}">
                                    <form:option value="-1"><spring:message code="app.choose"/></form:option>
                                </c:if>
                                <form:options items="${clients}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <form:errors path="client" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div id="caseDiv" ${empty timesheet.client ? "class=\"d-none\"" : ""}
                         data-case-id="${empty timesheet.clientCase ? "-1" : timesheet.clientCase.id}">
                        <div>
                            <spring:message code="timesheet.case"/>:
                        </div>

                        <div>
                            <form:select path="clientCase" cssClass="form-select"
                                         cssErrorClass="form-select text-danger border-danger"
                                         cssStyle="min-width:50%; max-width: 50%">
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
                                        readonly="true"/>
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            <form:errors path="dateExecuted" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div><spring:message code="timesheet.description"/>:</div>
                        <div>
                            <form:textarea path="description" cssClass="form-control"
                                           cssErrorClass="form-control text-danger border-danger"
                                           cssStyle="max-width: 50%" rows="3"/>
                            <form:errors path="description" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div><spring:message code="timesheet.hours"/>:</div>
                        <div>
                            <form:input path="hours" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="max-width: 50%"/>
                            <form:errors path="hours" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div><spring:message code="timesheet.minutes"/>:</div>
                        <div>
                            <form:input path="minutes" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="max-width: 50%" value="0"/>
                            <form:errors path="minutes" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <button type="button" class="button mx-2" onclick="location.href='/dashboard'"><spring:message
                                code="app.back"/>
                        </button>
                        <button type="submit" class="button mx-2"><spring:message code="app.save"/></button>
                    </div>
                </form:form>
            </div>

            <div id="casesList" class="d-none">

                <c:forEach var="aCase" items="${cases}">
                    <div data-client-id="${aCase.client.id}" data-case-id="${aCase.id}"
                         data-case-name="${aCase.name}"></div>
                </c:forEach>

            </div>

            <!--            TIMESHEET DETAILS END-->
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

<script src="/js/bootstrap-datepicker.js"></script>

<script src="/js/timesheetAddScript.js"></script>


</body>
</html>
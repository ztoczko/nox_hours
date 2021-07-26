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

                <!--            REPORT DETAILS START-->
                <form:form modelAttribute="report" action="/reports/add" method="post"
                           cssClass="editForm">
                    <form:hidden path="creator" value="${loggedUserId}"/>

                    <div>
                        <div>
                            <spring:message code="report.dateFrom"/>
                        </div>
                        <div class="input-group date datepicker" data-date-format="${dateFormat}">
                            <form:input path="dateFrom" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="min-width:20%; max-width: 50%"
                                        readonly="true"/>
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            <form:errors path="dateFrom" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="report.dateTo"/>
                        </div>
                        <div class="input-group date datepicker" data-date-format="${dateFormat}">
                            <form:input path="dateTo" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="min-width:20%; max-width: 50%"
                                        readonly="true"/>
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            <form:errors path="dateTo" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="form-check form-switch p-0 m-0"
                         style="min-height: 2rem" ${empty report.basedOnUser ? "" : "data-based-on-user"}>
                        <div>
                            <spring:message code="report.basedOnUser"/>:
                        </div>
                        <div>
                            <form:checkbox path="basedOnUser" cssClass="form-check-input p-0 m-0"/>
                        </div>
                    </div>

                    <div id="baseUserDiv" class="d-none"
                         data-base-user-id="${empty report.baseUserDTO ? "-1" : report.baseUserDTO.id}">
                        <div>
                            <spring:message code="report.baseUser"/>
                        </div>
                        <div>
                            <form:select path="baseUser" cssClass="form-select js-example-basic-single"
                                         cssErrorClass="form-control text-danger border-danger js-example-basic-single"
                                         cssStyle="max-width: 50%">
                                <form:option value="-1"><spring:message code="app.choose"/></form:option>
                                <form:options itemValue="id" itemLabel="fullName" items="${users}"/>
                            </form:select>
                            <form:errors path="baseUser" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="form-check form-switch p-0 m-0"
                         style="min-height: 2rem" ${empty report.basedOnClient ? "" : "data-based-on-client"}>
                        <div>
                            <spring:message code="report.basedOnClient"/>:
                        </div>
                        <div>
                            <form:checkbox path="basedOnClient" cssClass="form-check-input p-0 m-0"/>
                        </div>
                    </div>

                    <div id="baseClientDiv" class="d-none">
                        <div>
                            <spring:message code="report.baseClient"/>
                        </div>
                        <div>
                            <form:select path="baseClient" cssClass="form-select js-example-basic-single"
                                         cssErrorClass="form-control text-danger border-danger js-example-basic-single"
                                         cssStyle="max-width: 50%">
                                <form:option value="-1"><spring:message code="app.choose"/></form:option>
                                <form:options itemValue="id" itemLabel="name" items="${clients}"/>
                            </form:select>
                            <form:errors path="baseClient" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div id="basedOnCaseDiv" class="form-check form-switch p-0 m-0 d-none"
                         style="min-height: 2rem">
                        <div>
                            <spring:message code="report.basedOnCase"/>:
                        </div>
                        <div>
                            <form:checkbox path="basedOnCase" cssClass="form-check-input p-0 m-0"/>
                        </div>
                    </div>

                    <div id="baseCaseDiv" class="d-none"
                         data-case-id="${empty report.baseCase ? "-1" : report.baseCase.id}">
                        <div>
                            <spring:message code="report.baseCase"/>
                        </div>
                        <div>
                            <form:select path="baseCase" cssClass="form-select js-example-basic-single"
                                         cssErrorClass="form-control text-danger border-danger js-example-basic-single"
                                         cssStyle="max-width: 50%">
                                <form:option value="-1"><spring:message code="app.choose"/></form:option>
                            </form:select>
                            <form:errors path="baseCase" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                        <div>
                            <spring:message code="report.showDetails"/>:
                        </div>
                        <div>
                            <form:checkbox path="showDetails" cssClass="form-check-input p-0 m-0"/>
                        </div>
                    </div>

                    <div id="showNamesDiv" class="form-check form-switch p-0 m-0 d-none" style="min-height: 2rem">
                        <div>
                            <spring:message code="report.showNames"/>:
                        </div>
                        <div>
                            <form:checkbox path="showNames" cssClass="form-check-input p-0 m-0"/>
                        </div>
                    </div>

                    <sec:authorize access="hasAuthority('RATES')">
                        <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                            <div>
                                <spring:message code="report.showRates"/>:
                            </div>
                            <div>
                                <form:checkbox path="showRates" cssClass="form-check-input p-0 m-0"/>
                            </div>
                        </div>
                    </sec:authorize>

                    <div>
                        <button type="button" class="button mx-2" onclick="location.href='/reports/list'">
                            <spring:message
                                    code="app.back"/>
                        </button>
                        <button type="submit" class="button mx-2"><spring:message code="app.generate"/></button>
                    </div>

                </form:form>

            </div>

            <div id="casesList" class="d-none">

                <c:forEach var="aCase" items="${cases}">
                    <div data-client-id="${aCase.client.id}" data-case-id="${aCase.id}"
                         data-case-name="${aCase.name}"></div>
                </c:forEach>

            </div>
            <!--            REPORT DETAILS END-->
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

<script src="/js/reportAddScript.js"></script>


</body>
</html>
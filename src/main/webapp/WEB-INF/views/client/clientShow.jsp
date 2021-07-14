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

                <!--            CLIENT DETAILS START-->
                <form:form modelAttribute="client" action="/clients/edit" method="post" cssClass="editForm mb-0">
                    <form:hidden path="id" value="${client.id}"/>
                    <div>
                        <div><spring:message code="client.created"/>:</div>
                        <div><form:input path="createdString" disabled="true" cssClass="form-control"
                                         cssStyle="width: 50%"/></div>
                    </div>
                    <div>
                        <div><spring:message code="client.name"/>:</div>
                        <div><form:input path="name" disabled="true" cssClass="form-control"
                                         cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"/>
                            <form:errors path="name" cssClass="text-danger mx-2"/></div>
                    </div>
                    <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                        <div><spring:message code="client.closed"/>:</div>
                        <div><form:checkbox path="closed" cssClass="form-check-input p-0 m-0" disabled="true"/></div>
                    </div>
                    <%--                    <sec:authorize access="hasAuthority('RATES')">--%>
                    <%--                        <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">--%>
                    <%--                            <div>Klient ma ustawione stawki:</div>--%>
                    <%--                            <div><form:checkbox path="ratesSet" cssClass="form-check-input p-0 m-0" disabled="true"/></div>--%>
                    <%--                        </div>--%>
                    <%--                    </sec:authorize>--%>
                    <div class="mb-2">
                        <button type="button" class="button mx-2" onclick="location.href='/clients/list'">
                            <spring:message code="app.back"/>
                        </button>
                        <button type="button" class="button mx-2 ${edit ? "d-none" : ""}" id="toggleEdit">
                            <spring:message code="app.edit"/>
                        </button>
                        <button type="submit" class="button mx-2 ${edit ? "" : "d-none"}" id="saveEdit"><spring:message
                                code="app.save"/></button>
                        <sec:authorize access="hasAuthority('ADMIN')">
                            <button type="button" class="button mx-2" data-bs-toggle="modal"
                                    data-bs-target="#deleteModal"><spring:message code="app.delete"/>
                            </button>

                        </sec:authorize>

                    </div>
                    <%--                    <div class="radio-group">--%>
                    <%--                        <input type="radio" id="option-one" name="selector"><label for="option-one">Stawki</label><input type="radio" id="option-two" name="selector"><label for="option-two">Rozliczenia</label>--%>
                    <%--                    </div>--%>
                    <sec:authorize access="hasAuthority('RATES')">
                        <div class="toggleTableButtonGroup m-2 p-0">
                            <div class="toggleTableButtonLeft m-0 p-0 ${!empty param.showRates ? "buttonSelected" : ""}">
                                <spring:message code="clients.show.rates"/></div>
                            <div class="toggleTableButtonRight m-0 p-0 ${!empty param.showRates ? "" : "buttonSelected"}">
                                <spring:message
                                        code="clients.show.timesheets"/></div>
                        </div>
                    </sec:authorize>
                </form:form>
                <!-- CLIENT DETAILS END-->

                <%--                    MESSAGES START--%>
                <c:if test="${!empty param.editSuccess}">
                    <div class="text-success"><spring:message code="clients.show.client.updated.msg"/></div>
                </c:if>
                <c:if test="${!empty param.rateAddSuccess}">
                    <div class="text-success"><spring:message code="clients.show.rate.added.msg"/></div>
                </c:if>
                <c:if test="${!empty param.rateDeleteSuccess}">
                    <div class="text-success"><spring:message code="clients.show.rate.deleted.msg"/></div>
                </c:if>
                <c:if test="${!empty param.timesheetAddSuccess}">
                    <div class="text-success"><spring:message code="clients.show.timesheet.added.msg"/></div>
                </c:if>
                <c:if test="${!empty param.timesheetDeleteSuccess}">
                    <div class="text-success"><spring:message code="clients.show.timesheet.deleted.msg"/></div>
                </c:if>
                <%--                    MESSAGES END--%>


                <%--                LAST RATES START--%>
                <sec:authorize access="hasAuthority('RATES')">
                    <table class="table ${!empty param.showRates ? "" : "d-none"}" id="ratesTable">
                        <thead>
                        <tr>
                            <td class="fs-4 p-3" colspan="6"><spring:message code="clients.show.rates"/></td>
                            <td class="text-center" valign="middle">
                                <c:if test="${!client.closed}">
                                    <button type="button" class="button m-auto"
                                            onclick="location.href='/clients/${client.id}/rate/add'"><spring:message
                                            code="clients.show.rate.add.new"/>
                                    </button>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <spring:message code="rate.date.from"/>
                            </th>
                            <th>
                                <spring:message code="rate.date.to"/>
                            </th>
                            <th><spring:message code="rate.student.rate"/></th>
                            <th><spring:message code="rate.applicant.rate"/></th>
                            <th><spring:message code="rate.attorney.rate"/></th>
                            <th><spring:message code="rate.partner.rate"/></th>
                            <th><spring:message code="app.actions"/></th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:if test="${rates.size() == 0}">
                            <tr>
                                <td colspan="7">
                                    <spring:message code="clients.show.no.rates.msg"/>
                                </td>
                            </tr>
                            <span class="d-none">${totalRatePages = 1}</span>
                        </c:if>
                        <c:forEach var="rate" items="${rates}">
                            <tr>
                                <td data-${rate.id}-date-from>${rate.dateFromString}</td>
                                <td data-${rate.id}-date-to>${rate.dateToString}</td>
                                <td>${rate.studentRate} <spring:message code="app.currency"/></td>
                                <td>${rate.applicantRate} <spring:message code="app.currency"/></td>
                                <td>${rate.attorneyRate} <spring:message code="app.currency"/></td>
                                <td>${rate.partnerRate} <spring:message code="app.currency"/></td>
                                <td>
                                    <c:if test="${!client.closed}">
                                        <button type="button" class="button" data-rate-delete-button
                                                data-id="${rate.id}" data-client-id="${client.id}"><spring:message
                                                code="app.delete"/>
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <div class="pagination ${!empty param.showRates ? "" : "d-none"}" id="ratesPagination">
                        <div ${ratePage > 1 ? "class=\"pageLeft\"" : ""}><i
                                class="fa fa-angle-double-left" ${ratePage > 1 ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=1&showRates=true'\"") : ""}></i>
                        </div>
                        <div ${ratePage > 1 ? "class=\"pageLeft\"" : ""}><i
                                class="fa fa-angle-left" ${ratePage > 1 ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=").concat(ratePage - 1).concat("&showRates=true'\"") : ""}></i>
                        </div>
                        <div class="currentPage">${ratePage} <spring:message
                                code="app.pagination"/> ${totalRatePages}</div>
                        <div ${ratePage < totalRatePages ? "class=\"pageRight\"" : ""}><i
                                class="fa fa-angle-right" ${ratePage < totalRatePages ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=").concat(ratePage + 1).concat("&showRates=true'\"") : ""}></i>
                        </div>
                        <div ${ratePage < totalRatePages ? "class=\"pageRight\"" : ""}><i
                                class="fa fa-angle-double-right" ${ratePage < totalRatePages ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=").concat(totalRatePages).concat("&showRates=true'\"") : ""}></i>
                        </div>
                    </div>
                </sec:authorize>
                <%--                LAST RATES END--%>


                <%--                TIMESHEETS START--%>
                <table class="table ${!empty param.showRates ? "d-none" : ""}" id="timesheetsTable">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="4"><spring:message code="clients.show.timesheets"/></td>
                        <td class="text-center" valign="middle">
                            <c:if test="${!client.closed}">
                                <button type="button" class="button m-auto"
                                        onclick="${"location.href='/timesheet/add?client=".concat(client.id).concat("'")}">
                                    <spring:message code="clients.show.timesheet.add.new"/>
                                </button>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <spring:message code="timesheet.date.executed"/>
                        </th>
                        <th>
                            <spring:message code="timesheet.user"/>
                        </th>
                        <th><spring:message code="timesheet.hours"/></th>
                        <th><spring:message code="timesheet.description"/></th>
                        <th><spring:message code="app.actions"/></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${timesheets.size() == 0}">
                        <tr>
                            <td colspan="7">
                                <spring:message code="clients.show.no.timesheets.msg"/>
                            </td>
                        </tr>
                        <span class="d-none">${totalTimesheetPages = 1}</span>
                    </c:if>
                    <c:forEach var="timesheet" items="${timesheets}">
                        <tr>
                            <td data-${timesheet.id}-date-executed>
                                    ${timesheet.dateExecutedString}
                            </td>
                            <td>
                                    ${timesheet.userNameDTO.fullName}
                            </td>
                            <td data-${timesheet.id}-hours>
                                    ${timesheet.hours}
                            </td>
                            <td>
                                    ${timesheet.shortDescription}
                            </td>

                            <td>
                                <c:if test="${!client.closed}">

                                    <button type="button" class="button"
                                            onclick="${"location.href='/timesheet/client/".concat(client.id).concat("/show/").concat(timesheet.id).concat("'")}">
                                        <spring:message code="app.go.to.details"/>
                                    </button>

<%--                                    <c:if test="${timesheet.userNameDTO.id == loggedUserId}">--%>
<%--                                        <button type="button" class="button" data-timesheet-delete-button--%>
<%--                                                data-id="${timesheet.id}" data-client-id="${client.id}">--%>
<%--                                            <spring:message code="app.delete"/>--%>
<%--                                        </button>--%>
<%--                                    </c:if>--%>

                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="pagination ${!empty param.showRates ? "d-none" : ""}" id="timesheetsPagination">
                    <div ${timesheetPage > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-double-left" ${timesheetPage > 1 ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?timesheetPage=1'\"") : ""}></i>
                    </div>
                    <div ${timesheetPage > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-left" ${timesheetPage > 1 ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?timesheetPage=").concat(timesheetPage - 1).concat("'\"") : ""}></i>
                    </div>
                    <div class="currentPage">${timesheetPage} <spring:message
                            code="app.pagination"/> ${totalTimesheetPages}</div>
                    <div ${timesheetPage < totalTimesheetPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-right" ${timesheetPage < totalTimesheetPages ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?timesheetPage=").concat(timesheetPage + 1).concat("'\"") : ""}></i>
                    </div>
                    <div ${timesheetPage < totalTimesheetPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-double-right" ${timesheetPage < totalTimesheetPages ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?timesheetPage=").concat(totalTimesheetPages).concat("'\"") : ""}></i>
                    </div>
                </div>
                <%--                TIMESHEETS END--%>

            </div>


        </div>

    </div>

    <%--    CLIENT DELETE MODAL START--%>
    <sec:authorize access="hasAuthority('ADMIN')">
        <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body">
                        <p><spring:message code="clients.show.client.delete.confirmation.msg"/> ${client.name}?</p>
                    </div>
                    <div class="modal-footer d-flex justify-content-around">
                        <button type="button" class="button" style="min-width: 25%"
                                onclick="location.href='/clients/delete/${client.id}'"><spring:message code="app.yes"/>
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal">
                            <spring:message code="app.no"/></button>
                    </div>
                </div>
            </div>
        </div>
        <span id="rateDeleteMsgPart1" class="d-none"><spring:message
                code="clients.show.rate.delete.confirmation.msg1"/></span>
        <span id="rateDeleteMsgPart2" class="d-none"><spring:message
                code="clients.show.rate.delete.confirmation.msg2"/></span>
    </sec:authorize>
    <%--    CLIENT DELETE MODAL END--%>

    <%--    RATE DELETE MODAL START--%>
    <sec:authorize access="hasAuthority('RATES')">
        <div class="modal fade" id="deleteRateModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body">
                        <p id="rateDeleteMsg"></p>
                    </div>
                    <div class="modal-footer d-flex justify-content-around">
                        <button type="button" class="button" id="confirmRateDelete" style="min-width: 25%">
                            <spring:message code="app.yes"/>
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal">
                            <spring:message code="app.no"/></button>
                    </div>
                </div>
            </div>
        </div>
    </sec:authorize>
    <%--    RATE DELETE MODAL END--%>


    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>

<script src="/js/clientShowScript.js" defer>
</script>
</body>
</html>
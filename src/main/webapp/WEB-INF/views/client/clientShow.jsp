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
                <form:form modelAttribute="client" action="/clients/edit" method="post" cssClass="editForm">
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
                    <div>
                        <button type="button" class="button mx-2" onclick="location.href='/clients/list'"><spring:message code="app.back"/>
                        </button>
                        <button type="button" class="button mx-2 ${edit ? "d-none" : ""}" id="toggleEdit"><spring:message code="app.edit"/>
                        </button>
                        <button type="submit" class="button mx-2 ${edit ? "" : "d-none"}" id="saveEdit"><spring:message code="app.save"/></button>
                        <sec:authorize access="hasAuthority('ADMIN')">
                            <button type="button" class="button mx-2" data-bs-toggle="modal"
                                    data-bs-target="#deleteModal"><spring:message code="app.delete"/>
                            </button>

                        </sec:authorize>

                    </div>
                    <c:if test="${!empty param.editSuccess}">
                        <div class="text-success"><spring:message code="clients.show.client.updated.msg"/></div>
                    </c:if>
                    <c:if test="${!empty param.rateAddSuccess}">
                        <div class="text-success"><spring:message code="clients.show.rate.added.msg"/></div>
                    </c:if>
                    <c:if test="${!empty param.rateDeleteSuccess}">
                        <div class="text-success"><spring:message code="clients.show.rate.deleted.msg"/></div>
                    </c:if>
                </form:form>
                <!--            CLIENT DETAILS END-->

                <%--                LAST RATES START--%>
<%--                TODO dodać tu toggle między pokazywaniem rates i timesheets--%>
                <sec:authorize access="hasAuthority('RATES')">
                    <table class="table" id="ratesTable">
                        <thead>
                        <tr>
                            <td class="fs-4 p-3" colspan="6"><spring:message code="clients.show.rates"/></td>
                            <td class="text-center" valign="middle">
                                <c:if test="${!client.closed}">
                                    <button type="button" class="button m-auto"
                                            onclick="location.href='/clients/${client.id}/rate/add'"><spring:message code="clients.show.rate.add.new"/>
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
                            <span class="d-none">${totalPages = 1}</span>
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
                                                data-id="${rate.id}" data-client-id="${client.id}"><spring:message code="app.delete"/>
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <div class="pagination">
                        <div ${ratePage > 1 ? "class=\"pageLeft\"" : ""}><i
                                class="fa fa-angle-double-left" ${ratePage > 1 ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=1'\"") : ""}></i>
                        </div>
                        <div ${ratePage > 1 ? "class=\"pageLeft\"" : ""}><i
                                class="fa fa-angle-left" ${ratePage > 1 ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=").concat(ratePage - 1).concat("'\"") : ""}></i>
                        </div>
                        <div class="currentPage">${ratePage} <spring:message code="app.pagination"/> ${totalRatePages}</div>
                        <div ${ratePage < totalRatePages ? "class=\"pageRight\"" : ""}><i
                                class="fa fa-angle-right" ${ratePage < totalRatePages ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=").concat(ratePage + 1).concat("'\"") : ""}></i>
                        </div>
                        <div ${ratePage < totalRatePages ? "class=\"pageRight\"" : ""}><i
                                class="fa fa-angle-double-right" ${ratePage < totalRatePages ? "onclick=\"location.href='/clients/show/".concat(client.id).concat("?ratePage=").concat(totalRatePages).concat("'\"") : ""}></i>
                        </div>
                    </div>
                </sec:authorize>
                <%--                LAST RATES END--%>

                <div style="min-height: 80vh"></div>

                <%--                LAST RATES START--%>
                <%--                TODO zmodyfikować na wyświetlanie timesheet--%>
                <table class="table">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="6">Stawki</td>
                    </tr>
                    <tr>
                        <th>
                            Data od
                        </th>
                        <th>
                            Data do
                        </th>
                        <th>Stawka studenta</th>
                        <th>Stawka aplikanta</th>
                        <th>Stawka adwokata</th>
                        <th>Stawka partnera</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${rates.size() == 0}">
                        <tr>
                            <td colspan="7">
                                Brak stawek
                            </td>
                        </tr>
                        <span class="d-none">${totalPages = 1}</span>
                    </c:if>
                    <c:forEach var="rate" items="${rates}">
                        <tr>
                            <td>${rate.dateFromString}</td>
                            <td>${rate.dateToString}</td>
                            <td>${rate.studentRate} zł</td>
                            <td>${rate.applicantRate} zł</td>
                            <td>${rate.attorneyRate} zł</td>
                            <td>${rate.partnerRate} zł</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <%--                LAST RATES END--%>


                <c:url var="urlPaging" value="/clients/list">
                    <c:param name="search" value="${search}"/>
                    <c:param name="all" value="${all}"/>
                    <c:param name="sortName" value="${sortName}"/>
                    <c:param name="sortType" value="${sortType}"/>
                </c:url>
                <%--                TODO dostosować paginację do timesheetów--%>
                <div class="pagination">
                    <div ${page > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-double-left" ${page > 1 ? "onclick=\"location.href='".concat(urlPaging).concat("&page=1'\"") : ""}></i>
                    </div>
                    <div ${page > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-left" ${page > 1 ? "onclick=\"location.href='".concat(urlPaging).concat("&page=").concat(page - 1).concat("'\"") : ""}></i>
                    </div>
                    <div class="currentPage">${page} z ${totalPages}</div>
                    <div ${page < totalPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-right" ${page < totalPages ? "onclick=\"location.href='".concat(urlPaging).concat("&page=").concat(page + 1).concat("'\"") : ""}></i>
                    </div>
                    <div ${page < totalPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-double-right" ${page < totalPages ? "onclick=\"location.href='".concat(urlPaging).concat("&page=").concat(totalPages).concat("'\"") : ""}></i>
                    </div>
                </div>
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
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal"><spring:message code="app.no"/></button>
                    </div>
                </div>
            </div>
        </div>
        <span id="rateDeleteMsgPart1" class="d-none"><spring:message code="clients.show.rate.delete.confirmation.msg1"/></span>
        <span id="rateDeleteMsgPart2" class="d-none"><spring:message code="clients.show.rate.delete.confirmation.msg2"/></span>
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
                        <button type="button" class="button" id="confirmRateDelete" style="min-width: 25%"><spring:message code="app.yes"/>
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal"><spring:message code="app.no"/></button>
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
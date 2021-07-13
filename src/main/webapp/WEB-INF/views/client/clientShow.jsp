<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
                        <div>Data utworzenia:</div>
                        <div><form:input path="createdString" disabled="true" cssClass="form-control"
                                         cssStyle="width: 50%"/></div>
                    </div>
                    <div>
                        <div>Nazwa klienta:</div>
                        <div><form:input path="name" disabled="true" cssClass="form-control"
                                         cssErrorClass="form-control text-danger border-danger" cssStyle="width: 50%"/>
                            <form:errors path="name" cssClass="text-danger mx-2"/></div>
                    </div>
                    <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">
                        <div>Deaktywacja klienta:</div>
                        <div><form:checkbox path="closed" cssClass="form-check-input p-0 m-0" disabled="true"/></div>
                    </div>
                    <%--                    <sec:authorize access="hasAuthority('RATES')">--%>
                    <%--                        <div class="form-check form-switch p-0 m-0" style="min-height: 2rem">--%>
                    <%--                            <div>Klient ma ustawione stawki:</div>--%>
                    <%--                            <div><form:checkbox path="ratesSet" cssClass="form-check-input p-0 m-0" disabled="true"/></div>--%>
                    <%--                        </div>--%>
                    <%--                    </sec:authorize>--%>
                    <div>
                        <button type="button" class="button mx-2" onclick="location.href='/clients/list'">Powrót
                        </button>
                        <button type="button" class="button mx-2 ${edit ? "d-none" : ""}" id="toggleEdit">Edycja
                        </button>
                        <button type="submit" class="button mx-2 ${edit ? "" : "d-none"}" id="saveEdit">Zapisz</button>
                        <sec:authorize access="hasAuthority('ADMIN')">
                            <button type="button" class="button mx-2" data-bs-toggle="modal"
                                    data-bs-target="#deleteModal">Usuń
                            </button>

                        </sec:authorize>

                    </div>
                    <c:if test="${!empty param.editSuccess}">
                        <div class="text-success">Klient pomyślnie zmodyfikowany</div>
                    </c:if>
                    <c:if test="${!empty param.rateAddSuccess}">
                        <div class="text-success">Stawka pomyślnie dodana</div>
                    </c:if>
                    <c:if test="${!empty param.rateDeleteSuccess}">
                        <div class="text-success">Stawka pomyślnie usunięta</div>
                    </c:if>
                </form:form>
                <!--            CLIENT DETAILS END-->

                <%--                LAST RATES START--%>
<%--                TODO dodać tu toggle między pokazywaniem rates i timesheets--%>
                <sec:authorize access="hasAuthority('RATES')">
                    <table class="table" id="ratesTable">
                        <thead>
                        <tr>
                            <td class="fs-4 p-3" colspan="6">Stawki</td>
                            <td class="text-center" valign="middle">
                                <c:if test="${!client.closed}">
                                    <button type="button" class="button m-auto"
                                            onclick="location.href='/clients/${client.id}/rate/add'">Dodaj nową
                                    </button>
                                </c:if>
                            </td>
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
                            <th>Akcje</th>
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
                                <td data-${rate.id}-date-from>${rate.dateFromString}</td>
                                <td data-${rate.id}-date-to>${rate.dateToString}</td>
                                <td>${rate.studentRate} zł</td>
                                <td>${rate.applicantRate} zł</td>
                                <td>${rate.attorneyRate} zł</td>
                                <td>${rate.partnerRate} zł</td>
                                <td>
                                    <c:if test="${!client.closed}">
                                        <button type="button" class="button" data-rate-delete-button
                                                data-id="${rate.id}" data-client-id="${client.id}">Usuń
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
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
                <%--                TODO dostosować paginację--%>
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
                        <p>Czy na pewno chcesz usunąć klienta ${client.name}?</p>
                    </div>
                    <div class="modal-footer d-flex justify-content-around">
                        <button type="button" class="button" style="min-width: 25%"
                                onclick="location.href='/clients/delete/${client.id}'">Tak
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal">Nie</button>
                    </div>
                </div>
            </div>
        </div>
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
                        <button type="button" class="button" id="confirmRateDelete" style="min-width: 25%"
                                o>Tak
                        </button>
                        <button type="button" class="button" style="min-width: 25%" data-bs-dismiss="modal">Nie</button>
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
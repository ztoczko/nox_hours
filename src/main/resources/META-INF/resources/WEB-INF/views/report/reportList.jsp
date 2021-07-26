<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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

            <!--            REPORT LIST START-->

            <div class="mainContentDiv">

                <%--                MESSAGES START--%>
                <c:if test="${!empty param.deleteSuccess}">
                    <div class="text-success mb-2"><spring:message code="report.list.report.deleted.msg"/></div>
                </c:if>
                <%--                MESSAGES END--%>
                <spring:message code="report.emptyField" var="emptyField"/>
                <spring:message code="app.yes" var="yesMsg"/>
                <spring:message code="app.no" var="noMsg"/>

                <table class="table">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="8"><spring:message code="report.list.reports"/></td>
                        <td class="text-center" valign="middle">
                            <button type="button" class="button m-auto" onclick="location.href='/reports/add'">
                                <spring:message code="report.list.add.new"/></button>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <spring:message code="report.created"/>
                        </th>
                        <th>
                            <spring:message code="report.dateFrom"/>
                        </th>
                        <th>
                            <spring:message code="report.dateTo"/>
                        </th>
                        <th>
                            <spring:message code="report.baseUser"/>
                        </th>
                        <th>
                            <spring:message code="report.baseClient"/>
                        </th>
                        <th>
                            <spring:message code="report.showDetails"/>
                        </th>
                        <th>
                            <spring:message code="report.showNames"/>
                        </th>
                        <th>
                            <spring:message code="report.showRates"/>
                        </th>

                        <th><spring:message code="app.actions"/></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${reports.size() == 0}">
                        <tr>
                            <td colspan="9">
                                <spring:message code="report.list.no.reports.message"/>
                            </td>
                        </tr>
                        <span class="d-none">${totalPages = 1}</span>
                    </c:if>
                    <c:forEach var="report" items="${reports}">
                        <tr>
                            <td>${report.createdString}</td>
                            <td>${report.dateFromString}</td>
                            <td>${report.dateToString}</td>
                            <td>${empty report.baseUserDTO ? emptyField : report.baseUserDTO.fullName}</td>
                            <td>${empty report.baseClient ? emptyField : report.baseClient.name}</td>
                            <td>${report.showDetails ? yesMsg : noMsg}</td>
                            <td>${report.showNames ? yesMsg : noMsg}</td>
                            <td>${report.showRates ? yesMsg : noMsg}</td>
                            <td>
                                <button type="button" class="button m-1"
                                        onclick="location.href = '/reports/show/${report.id}'"><spring:message
                                        code="app.go.to.details"/>
                                </button>
                                <button type="button" class="button m-1"
                                        onclick="location.href = '/reports/delete/${report.id}'"><spring:message
                                        code="app.delete"/>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>

                <div class="pagination">
                    <div ${page > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-double-left" ${page > 1 ? "onclick=\"location.href='/reports/list?page=1'" : ""}></i>
                    </div>
                    <div ${page > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-left" ${page > 1 ? "onclick=\"location.href='/reports/list?page=".concat(page - 1).concat("'\"") : ""}></i>
                    </div>
                    <div class="currentPage">${page} <spring:message code="app.pagination"/> ${totalPages}</div>
                    <div ${page < totalPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-right" ${page < totalPages ? "onclick=\"location.href='/reports/list?page=".concat(page + 1).concat("'\"") : ""}></i>
                    </div>
                    <div ${page < totalPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-double-right" ${page < totalPages ? "onclick=\"location.href='/reports/list?page=".concat(totalPages).concat("'\"") : ""}></i>
                    </div>
                </div>
            </div>
            <!--            REPORT LIST END-->
        </div>
    </div>

    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>
</body>
</html>
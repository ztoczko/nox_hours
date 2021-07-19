<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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

                <%--                &lt;%&ndash;                MESSAGES START&ndash;%&gt;--%>
                <%--                <c:if test="${!empty param.deleteSuccess}">--%>
                <%--                    <div class="text-success mb-2"><spring:message code="report.list.report.deleted.msg"/></div>--%>
                <%--                </c:if>--%>
                <%--                &lt;%&ndash;                MESSAGES END&ndash;%&gt;--%>
                <%--                <spring:message code="report.emptyField" var="emptyField"/>--%>
                <%--                <spring:message code="app.yes" var="yesMsg"/>--%>
                <%--                <spring:message code="app.no" var="noMsg"/>--%>

                <!--            REPORT AGGREGATE START-->
                <table class="table">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="4">
                            <spring:message code="report.show.aggregate"/><br>
                            <spring:message code="report.show.aggregate.msg"
                                            arguments="${report.dateFromString}, ${report.dateToString}"/>
                        </td>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                        <th colspan="3" style="width: 75%"><spring:message code="report.show.hours.for.rank"/>
                            <spring:message
                                    code="user.rank.student"/></th>
                        <td style="width: 25%">${report.hoursByRank[0]}h</td>
                    </tr>
                    <tr>
                        <th colspan="3"><spring:message code="report.show.hours.for.rank"/> <spring:message
                                code="user.rank.applicant"/></th>
                        <td>${report.hoursByRank[1]}h</td>
                    </tr>
                    <tr>
                        <th colspan="3"><spring:message code="report.show.hours.for.rank"/> <spring:message
                                code="user.rank.attorney"/></th>
                        <td>${report.hoursByRank[2]}h</td>
                    </tr>
                    <tr>
                        <th colspan="3"><spring:message code="report.show.hours.for.rank"/> <spring:message
                                code="user.rank.partner"/></th>
                        <td>${report.hoursByRank[3]}h</td>
                    </tr>
                    <tr>
                        <th colspan="3"><spring:message code="report.show.total.hours"/></th>
                        <td>${report.totalHours}h</td>
                    </tr>
                    <c:if test="${report.showRates && empty unauthorizedRateAccess}">

                        <c:choose>
                            <c:when test="${rateError}">
                                <tr>
                                    <td colspan="4" class="text-danger border-danger"><spring:message
                                            code="report.show.rates.error"/></td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <th colspan="3" style="width: 75%"><spring:message
                                            code="report.show.values.for.rank"/>
                                        <spring:message
                                                code="user.rank.student"/></th>
                                    <td style="width: 25%">${report.valueByRank[0]} <spring:message
                                            code="app.currency"/></td>
                                </tr>
                                <tr>
                                    <th colspan="3"><spring:message code="report.show.values.for.rank"/> <spring:message
                                            code="user.rank.applicant"/></th>
                                    <td>${report.valueByRank[1]} <spring:message code="app.currency"/></td>
                                </tr>
                                <tr>
                                    <th colspan="3"><spring:message code="report.show.values.for.rank"/> <spring:message
                                            code="user.rank.attorney"/></th>
                                    <td>${report.valueByRank[2]} <spring:message code="app.currency"/></td>
                                </tr>
                                <tr>
                                    <th colspan="3"><spring:message code="report.show.values.for.rank"/> <spring:message
                                            code="user.rank.partner"/></th>
                                    <td>${report.valueByRank[3]} <spring:message code="app.currency"/></td>
                                </tr>
                                <tr>
                                    <th colspan="3"><spring:message code="report.show.total.value"/></th>
                                    <td>${report.totalValue} <spring:message code="app.currency"/></td>
                                </tr>
                            </c:otherwise>
                        </c:choose>

                    </c:if>

                    </tbody>
                </table>
                <!--            REPORT AGGREGATE END-->


                <!--            REPORT DETAILS START-->
                <table class="table">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="${report.showNames ? "6" : "5"}"><spring:message
                                code="report.show.details"/></td>
                    </tr>
                    <tr>
                        <th>
                            <spring:message code="timesheet.date.executed"/>
                        </th>
                        <c:if test="${report.showNames}">
                            <th>
                                <spring:message code="timesheet.user"/>
                            </th>
                        </c:if>
                        <th>
                            <spring:message code="timesheet.rank.when.created"/>
                        </th>
                        <th>
                            <spring:message code="timesheet.client"/>
                        </th>
                        <th>
                            <spring:message code="timesheet.hours"/>
                        </th>
                        <th>
                            <spring:message code="timesheet.description"/>
                        </th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${report.timesheets.size() == 0}">
                        <tr>
                            <td colspan="${report.showNames ? "6" : "5"}">
                                <spring:message code="report.show.no.timesheets.message"/>
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach var="timesheet" items="${report.timesheets}">
                        <tr>
                            <td>${timesheet.dateExecutedString}</td>
                            <c:if test="${report.showNames}">
                                <td>${timesheet.userNameDTO.fullName}</td>
                            </c:if>
                            <td>
                                <span class="${timesheet.rankWhenCreated == 1 ? "" : "d-none"}"><spring:message
                                        code="user.rank.student"/></span>
                                <span class="${timesheet.rankWhenCreated == 2 ? "" : "d-none"}"><spring:message
                                        code="user.rank.applicant"/></span>
                                <span class="${timesheet.rankWhenCreated == 3 ? "" : "d-none"}"><spring:message
                                        code="user.rank.attorney"/></span>
                                <span class="${timesheet.rankWhenCreated == 4 ? "" : "d-none"}"><spring:message
                                        code="user.rank.partner"/></span>
                            </td>
                            <td>${timesheet.client.name}</td>
                            <td>${timesheet.hours}</td>
                            <td>${timesheet.description}</td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
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
</body>
</html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

            <!--            DASHBOARD START-->
            <div class="summaryDashboard">

                <div>
                    <div><spring:message code="dashboard.last.timesheets.msg"/></div>
                    <div>
                        <div>${recentCount}</div>
                    </div>
                </div>
                <div>
                    <div><spring:message code="dashboard.last.hours.msg"/></div>
                    <div>
                        <div>${recentSum}</div>
                    </div>
                </div>

            </div>

            <div class="mainContentDiv">
                <!-- bg-white table-striped table-hover -->
                <table class="table ">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="4"><spring:message code="dashboard.last.activities"/></td>
                    </tr>
                    <tr>
                        <th><spring:message code="activity.created"/></th>
                        <th><spring:message code="activity.fullname"/></th>
                        <th><spring:message code="activity.description"/></th>
                        <th><spring:message code="app.actions"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${activities.size() == 0}">
                        <tr>
                            <td colspan="4"><spring:message code="dashboard.no.activities.message"/></td>
                        </tr>
                    </c:if>
                    <c:forEach var="activity" items="${activities}">
                        <tr>
                            <td>${activity.createdString}</td>
                            <td>${activity.fullName}</td>
                            <td>${activity.description}</td>
                            <td>
                                <button type="button" class="button"
                                        onclick="location.href='/clients/show/${activity.client.id}'">
                                    <spring:message code="app.go.to.details"/>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
                <%--                <div class="pagination">--%>
                <%--                    <div class="pageLeft"><i class="fa fa-angle-double-left"></i></div>--%>
                <%--                    <div class="pageLeft"><i class="fa fa-angle-left"></i></div>--%>
                <%--                    <div class="currentPage">X z Y</div>--%>
                <%--                    <div class="pageRight"><i class="fa fa-angle-right"></i></div>--%>
                <%--                    <div class="pageRight"><i class="fa fa-angle-double-right"></i></div>--%>
                <%--                </div>--%>
            </div>
            <!--            DASHBOARD END-->

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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

            <!--            CLIENT LIST START-->

            <div class="mainContentDiv">

                <c:url var="urlSorting" value="/clients/list">
                    <c:param name="search" value="${search}"/>
                    <c:param name="all" value="${all}"/>
                    <c:param name="page" value="${page}"/>
                </c:url>
                <div class="searchBox">
                    <form method="get">
                        <input type="hidden" name="all" value="${all}"/>
                        <input type="hidden" name="sortName" value="${sortName}"/>
                        <input type="hidden" name="sortType" value="${sortType}"/>
                        <input type="text" name="search" placeholder="<spring:message code="search.placeholder"/>"/>
                        <button type="submit" class="button"><i class="fa fa-search"></i> <spring:message
                                code="search.msg"/></button>
                    </form>
                    <form method="get">
                        <input type="hidden" name="search" value="${search}"/>
                        <input type="hidden" name="sortName" value="${sortName}"/>
                        <input type="hidden" name="sortType" value="${sortType}"/>
                        <input type="hidden" name="page" value="${page}"/>
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="showAllToggle" name="all"
                                   value="true" ${all ? "checked" : ""}>
                            <label class="form-check-label"><spring:message
                                    code="clients.list.show.inactive.msg"/></label>
                        </div>
                    </form>
                </div>
                <c:if test="${!empty param.deleteSuccess}">
                    <div class="text-success mb-2"><spring:message code="clients.list.client.deleted.msg"/></div>
                </c:if>
                <c:if test="${!empty param.addSuccess}">
                    <div class="text-success mb-2"><spring:message code="clients.list.client.added.msg"/></div>
                </c:if>
                <table class="table">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="2"><spring:message code="clients.list.clients"/></td>
                        <td class="text-center" valign="middle">
                            <button type="button" class="button m-auto" onclick="location.href='/clients/add'">
                                <spring:message code="clients.list.add.new"/></button>
                        </td>
                    </tr>
                    <tr>
                        <th class="pointer">
                            <spring:message code="client.created"/>
                            <c:choose>
                                <c:when test="${sortName.equals('created')}">
                                    <c:if test="${sortType.equals('asc')}">
                                        <i class="fa fa-sort-asc"
                                           onclick="location.href='${urlSorting.concat("&sortName=created&sortType=desc")}'"></i>
                                    </c:if>
                                    <c:if test="${sortType.equals('desc')}">
                                        <i class="fa fa-sort-desc"
                                           onclick="location.href='${urlSorting.concat("&sortName=created&sortType=asc")}'"></i>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-sort"
                                       onclick="location.href='${urlSorting.concat("&sortName=created&sortType=desc")}'"></i>
                                </c:otherwise>
                            </c:choose>
                        </th>
                        <th class="pointer">
                            <spring:message code="client.name"/>
                            <c:choose>
                                <c:when test="${sortName.equals('name')}">
                                    <c:if test="${sortType.equals('asc')}">
                                        <i class="fa fa-sort-asc"
                                           onclick="location.href='${urlSorting.concat("&sortName=name&sortType=desc")}'"></i>
                                    </c:if>
                                    <c:if test="${sortType.equals('desc')}">
                                        <i class="fa fa-sort-desc"
                                           onclick="location.href='${urlSorting.concat("&sortName=name&sortType=asc")}'"></i>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-sort"
                                       onclick="location.href='${urlSorting.concat("&sortName=name&sortType=asc")}'"></i>
                                </c:otherwise>
                            </c:choose>
                        </th>
                        <th><spring:message code="app.actions"/></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${clients.size() == 0}">
                        <tr>
                            <td colspan="3">
                                <spring:message code="clients.list.no.clients.message"/>
                            </td>
                        </tr>
                        <span class="d-none">${totalPages = 1}</span>
                    </c:if>
                    <c:forEach var="client" items="${clients}">
                        <tr ${client.closed ? "style='opacity: 0.5'" : ""}>
                            <td>${client.createdString}</td>
                            <td>${client.name}</td>
                            <td>
                                <button type="button" class="button"
                                        onclick="location.href = '/clients/show/${client.id}'"><spring:message
                                        code="app.go.to.details"/>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
                <c:url var="urlPaging" value="/clients/list">
                    <c:param name="search" value="${search}"/>
                    <c:param name="all" value="${all}"/>
                    <c:param name="sortName" value="${sortName}"/>
                    <c:param name="sortType" value="${sortType}"/>
                </c:url>
                <div class="pagination">
                    <div ${page > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-double-left" ${page > 1 ? "onclick=\"location.href='".concat(urlPaging).concat("&page=1'\"") : ""}></i>
                    </div>
                    <div ${page > 1 ? "class=\"pageLeft\"" : ""}><i
                            class="fa fa-angle-left" ${page > 1 ? "onclick=\"location.href='".concat(urlPaging).concat("&page=").concat(page - 1).concat("'\"") : ""}></i>
                    </div>
                    <div class="currentPage">${page} <spring:message code="app.pagination"/> ${totalPages}</div>
                    <div ${page < totalPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-right" ${page < totalPages ? "onclick=\"location.href='".concat(urlPaging).concat("&page=").concat(page + 1).concat("'\"") : ""}></i>
                    </div>
                    <div ${page < totalPages ? "class=\"pageRight\"" : ""}><i
                            class="fa fa-angle-double-right" ${page < totalPages ? "onclick=\"location.href='".concat(urlPaging).concat("&page=").concat(totalPages).concat("'\"") : ""}></i>
                    </div>
                </div>
            </div>
            <!--            CLIENT LIST END-->

        </div>

    </div>

    <!--    FOOTER START-->
    <jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
    <!--    FOOTER END-->
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>
<script>
    document.getElementById("showAllToggle").addEventListener("click", function () {
        this.parentElement.parentElement.submit();
    });
</script>
</body>
</html>
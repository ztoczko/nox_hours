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

            <!--            USER LIST START-->

            <div class="mainContentDiv">

                <c:url var="urlSorting" value="/admin/list">
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
                        <button type="submit" class="button"><i class="fa fa-search"></i> <spring:message code="search.msg"/></button>
                    </form>
                    <form method="get">
                        <input type="hidden" name="search" value="${search}"/>
                        <input type="hidden" name="sortName" value="${sortName}"/>
                        <input type="hidden" name="sortType" value="${sortType}"/>
                        <input type="hidden" name="page" value="${page}"/>
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="showAllToggle" name="all"
                                   value="true" ${all ? "checked" : ""}>
                            <label class="form-check-label"><spring:message code="admin.list.show.locked.msg"/></label>
                        </div>
                    </form>
                </div>

<%--                MESSAGES START--%>
                <c:if test="${!empty param.deleteSuccess}">
                    <div class="text-success mb-2"><spring:message code="admin.list.user.deleted.msg"/></div>
                </c:if>
                <c:if test="${!empty param.addSuccess}">
                    <div class="text-success mb-2"><spring:message code="admin.list.user.added.msg"/></div>
                </c:if>
<%--                MESSAGES END--%>

                <table class="table">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="4"><spring:message code="admin.list.users"/></td>
                        <td class="text-center" valign="middle"><button type="button" class="button m-auto" onclick="location.href='/admin/add'"><spring:message code="admin.list.add.new"/></button> </td>
                    </tr>
                    <tr>
                        <th class="pointer">
                            <spring:message code="user.firstName"/>
                            <c:choose>
                                <c:when test="${sortName.equals('firstname')}">
                                    <c:if test="${sortType.equals('asc')}">
                                        <i class="fa fa-sort-asc"
                                           onclick="location.href='${urlSorting.concat("&sortName=firstname&sortType=desc")}'"></i>
                                    </c:if>
                                    <c:if test="${sortType.equals('desc')}">
                                        <i class="fa fa-sort-desc"
                                           onclick="location.href='${urlSorting.concat("&sortName=firstname&sortType=asc")}'"></i>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-sort"
                                       onclick="location.href='${urlSorting.concat("&sortName=firstname&sortType=desc")}'"></i>
                                </c:otherwise>
                            </c:choose>
                        </th>
                        <th class="pointer">
                            <spring:message code="user.lastName"/>
                            <c:choose>
                                <c:when test="${sortName.equals('lastname')}">
                                    <c:if test="${sortType.equals('asc')}">
                                        <i class="fa fa-sort-asc"
                                           onclick="location.href='${urlSorting.concat("&sortName=lastname&sortType=desc")}'"></i>
                                    </c:if>
                                    <c:if test="${sortType.equals('desc')}">
                                        <i class="fa fa-sort-desc"
                                           onclick="location.href='${urlSorting.concat("&sortName=lastname&sortType=asc")}'"></i>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-sort"
                                       onclick="location.href='${urlSorting.concat("&sortName=lastname&sortType=asc")}'"></i>
                                </c:otherwise>
                            </c:choose>
                        </th>
                        <th class="pointer">
                            <spring:message code="user.email"/>
                            <c:choose>
                                <c:when test="${sortName.equals('email')}">
                                    <c:if test="${sortType.equals('asc')}">
                                        <i class="fa fa-sort-asc"
                                           onclick="location.href='${urlSorting.concat("&sortName=email&sortType=desc")}'"></i>
                                    </c:if>
                                    <c:if test="${sortType.equals('desc')}">
                                        <i class="fa fa-sort-desc"
                                           onclick="location.href='${urlSorting.concat("&sortName=email&sortType=asc")}'"></i>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-sort"
                                       onclick="location.href='${urlSorting.concat("&sortName=email&sortType=asc")}'"></i>
                                </c:otherwise>
                            </c:choose>
                        </th>
                        <th class="pointer">
                            <spring:message code="user.rank"/>
                            <c:choose>
                                <c:when test="${sortName.equals('rank')}">
                                    <c:if test="${sortType.equals('asc')}">
                                        <i class="fa fa-sort-asc"
                                           onclick="location.href='${urlSorting.concat("&sortName=rank&sortType=desc")}'"></i>
                                    </c:if>
                                    <c:if test="${sortType.equals('desc')}">
                                        <i class="fa fa-sort-desc"
                                           onclick="location.href='${urlSorting.concat("&sortName=rank&sortType=asc")}'"></i>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-sort"
                                       onclick="location.href='${urlSorting.concat("&sortName=rank&sortType=asc")}'"></i>
                                </c:otherwise>
                            </c:choose>
                        </th>
                        <th><spring:message code="app.actions"/></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${users.size() == 0}">
                        <tr>
                            <td colspan="3">
                                <spring:message code="admin.list.no.users.message"/>
                            </td>
                        </tr>
                        <span class="d-none">${totalPages = 1}</span>
                    </c:if>
                    <c:forEach var="user" items="${users}">
                        <tr ${user.isLocked ? "style='opacity: 0.5'" : ""}>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.email}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.rank == 1}">
                                        <spring:message code="user.rank.student"/>
                                    </c:when>
                                    <c:when test="${user.rank == 2}">
                                        <spring:message code="user.rank.applicant"/>
                                    </c:when>
                                    <c:when test="${user.rank == 3}">
                                        <spring:message code="user.rank.attorney"/>
                                    </c:when>
                                    <c:when test="${user.rank == 4}">
                                        <spring:message code="user.rank.partner"/>
                                    </c:when>
                                </c:choose>

                            </td>
                            <td>
                                <button type="button" class="button"
                                        onclick="location.href = '/admin/show/${user.id}'"><spring:message code="app.go.to.details"/>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
                <c:url var="urlPaging" value="/admin/list">
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
            <!--            USER LIST END-->

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
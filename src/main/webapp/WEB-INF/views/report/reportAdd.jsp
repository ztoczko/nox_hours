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
                            <form:select path="baseUser" cssClass="form-control"
                                         cssErrorClass="form-control text-danger border-danger"
                                         cssStyle="max-width: 50%">
                                <form:option value="-1">...</form:option>
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
                            <form:select path="baseClient" cssClass="form-control"
                                         cssErrorClass="form-control text-danger border-danger"
                                         cssStyle="max-width: 50%">
                                <form:option value="-1">...</form:option>
                                <form:options itemValue="id" itemLabel="name" items="${clients}"/>
                            </form:select>
                            <form:errors path="baseClient" cssClass="text-danger mx-2"/>
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


<%--<script src="/popper.min.js+bootstrap.min.js.pagespeed.jc.7FgZl6cJhb.js"></script>--%>
<%--<script>eval(mod_pagespeed_Lp4SucdGsq);</script>--%>
<%--<script>eval(mod_pagespeed_f_sjjbg9NQ);</script>--%>
<%--<script src="/picker.js+picker.date.js+main.js.pagespeed.jc.ynDLArhs-A.js"></script>--%>
<%--<script>eval(mod_pagespeed_8w_PIyg5ry);</script>--%>
<%--<script>eval(mod_pagespeed_VesJUVlTKR);</script>--%>
<%--<script>eval(mod_pagespeed_vkHW6T5iEa);</script>--%>
<%--<script defer src="https://static.cloudflareinsights.com/beacon.min.js"--%>
<%--        data-cf-beacon='{"rayId":"66dc6b6b5e880028","token":"cd0b4b3a733644fc843ef0b185f98241","version":"2021.6.0","si":10}'></script>--%>

<%--<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>--%>
<script src="/js/bootstrap-datepicker.js"></script>

<script src="/js/reportAddScript.js"></script>


</body>
</html>
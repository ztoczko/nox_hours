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

                <!--            TIMESHEET DETAILS START-->
                <form:form modelAttribute="timesheet" action="/timesheet/add" method="post"
                           cssClass="editForm">
                    <form:hidden path="rankWhenCreated" value="${userRank}"/>

                    <c:choose>
                        <c:when test="${loggedUserAdminStatus}">
                            <div>
                                <div>
                                    <spring:message code="timesheet.user"/>
                                </div>
                                <div>
                                    <form:select path="user" items="${users}" itemValue="id" itemLabel="fullName"
                                                 cssClass="form-control"
                                                 cssErrorClass="form-control text-danger border-danger"
                                                 cssStyle="max-width: 50%" data-user-id="${timesheet.user.id}"/>
                                    <form:errors path="user" cssClass="text-danger mx-2"/>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form:hidden path="user" value="${userId}"/>
                        </c:otherwise>

                    </c:choose>
                    <div>
                        <div>
                            <spring:message code="timesheet.client"/>:
                        </div>

                        <div id="clientDiv" data-client-id="${!empty client ? client.id : "0"}">
                            <form:select path="client" cssClass="form-control"
                                         cssErrorClass="form-control text-danger border-danger"
                                         cssStyle="min-width:50%; max-width: 50%">
                                <c:if test="${empty client}">
                                    <form:option value="-1">...</form:option>
                                </c:if>
                                <form:options items="${clients}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <form:errors path="client" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div>
                            <spring:message code="timesheet.date.executed"/>
                        </div>
                        <div class="input-group date datepicker" data-date-format="${dateFormat}">
                            <form:input path="dateExecuted" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="min-width:20%; max-width: 50%"
                                        readonly="true"/>
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            <form:errors path="dateExecuted" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div><spring:message code="timesheet.description"/>:</div>
                        <div>
                            <form:textarea path="description" cssClass="form-control"
                                           cssErrorClass="form-control text-danger border-danger"
                                           cssStyle="max-width: 50%" rows="3"/>
                            <form:errors path="description" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <div><spring:message code="timesheet.hours"/>:</div>
                        <div>
                            <form:input path="hours" cssClass="form-control"
                                        cssErrorClass="form-control text-danger border-danger"
                                        cssStyle="max-width: 50%"/>
                            <form:errors path="hours" cssClass="text-danger mx-2"/>
                        </div>
                    </div>

                    <div>
                        <button type="button" class="button mx-2" onclick="location.href='/dashboard'"><spring:message
                                code="app.back"/>
                        </button>
                        <button type="submit" class="button mx-2"><spring:message code="app.save"/></button>
                    </div>

                </form:form>

            </div>

            <!--            TIMESHEET DETAILS END-->
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

<script src="/js/timesheetAddScript.js"></script>


</body>
</html>
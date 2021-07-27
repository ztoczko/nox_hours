<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <div class="row">
        <div class="col d-flex justify-content-between topBar">
            <div class="ms-3 pageTitle"><img class="img-fluid" src="/hourglass.png"/><spring:message
                    code="header.title"/>
                <span><spring:message code="header.version"/></span>
            </div>
            <div class="me-5 fs-3 d-flex justify-content-center align-items-center menuText">
                <div><i class="fs-1 fa fa-user-circle"></i></div>
                <div class="ms-3">${loggedUserName}</div>
            </div>
        </div>
    </div>
</header>
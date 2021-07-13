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

            <!--&lt;!&ndash;            FORM START&ndash;&gt;-->
            <!--            <div class="mainContentDiv">-->


            <!--                <input type="text" list="cars" />-->
            <!--                <datalist id="cars">-->
            <!--                    <option>Volvo</option>-->
            <!--                    <option>Saab</option>-->
            <!--                    <option>Mercedes</option>-->
            <!--                    <option>Audi</option>-->
            <!--                </datalist>-->
            <!--            </div>-->
            <!--&lt;!&ndash;            FORM END&ndash;&gt;-->


            <!--            &lt;!&ndash;            CASE LIST  START&ndash;&gt;-->
            <!--            <div class="searchDiv">-->


            <!--            </div>-->

            <!--            <div class="mainContentDiv">-->

            <!--                <table class="bg-white table table-striped table-hover">-->
            <!--                    <thead>-->
            <!--                    <tr>-->
            <!--                        <td class="fs-4 p-3" colspan="4">Lista spraw</td>-->
            <!--                    </tr>-->
            <!--                    <tr>-->
            <!--                        <th>Data</th>-->
            <!--                        <th>Użytkownik</th>-->
            <!--                        <th>Aktywność</th>-->
            <!--                        <th>Akcje</th>-->
            <!--                    </tr>-->
            <!--                    </thead>-->
            <!--                    <tbody>-->
            <!--                    <tr>-->
            <!--                        <td>2021-05-31 13:20:00</td>-->
            <!--                        <td>Jan Kowalski</td>-->
            <!--                        <td>użytkownik dodał nowe rozliczenie czasu pracy dla sprawy {case name}</td>-->
            <!--                        <td><button type="button" class="button">Przejdź</button> </td>-->
            <!--                    </tr>-->
            <!--                    <tr>-->
            <!--                        <td>2021-05-30 13:20:00</td>-->
            <!--                        <td>Antoni Nowak</td>-->
            <!--                        <td>użytkownik dodał nową sprawę {case name}</td>-->
            <!--                        <td><button type="button" class="button button-clicked">Przejdź</button></td>-->
            <!--                    </tr>-->
            <!--                    <tr>-->
            <!--                        <td>2021-05-29 13:20:00</td>-->
            <!--                        <td>Antoni Nowak</td>-->
            <!--                        <td>użytkownik edytował rozliczenie czasu pracy dla sprawy {case name}</td>-->
            <!--                        <td><button type="button" class="button">Przejdź</button></td>-->
            <!--                    </tr>-->
            <!--                    <tr>-->
            <!--                        <td>2021-05-28 13:20:00</td>-->
            <!--                        <td>Karolina Kiełbasa</td>-->
            <!--                        <td>użytkownik zamknął sprawę {case name}</td>-->
            <!--                        <td><button type="button" class="button">Przejdź</button></td>-->
            <!--                    </tr>-->
            <!--                    </tbody>-->
            <!--                </table>-->
            <!--            </div>-->
            <!--            &lt;!&ndash;            CASE LIST END&ndash;&gt;-->


            <!--            DASHBOARD START-->
            <div class="summaryDashboard">

                <div>
                    <div><spring:message code="dashboard.last.timesheets.msg"/> </div>
                    <div>
                        <div>X</div>
                    </div>
                </div>
                <div>
                    <div><spring:message code="dashboard.last.hours.msg"/></div>
                    <div>
                        <div>X</div>
                    </div>
                </div>


            </div>

            <div class="mainContentDiv">
                <!-- bg-white table-striped table-hover -->
                <table class="table ">
                    <thead>
                    <tr>
                        <td class="fs-4 p-3" colspan="4">Ostatnia aktywność</td>
                    </tr>
                    <tr>
                        <th>Data</th>
                        <th>Użytkownik</th>
                        <th>Aktywność</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>2021-05-31 13:20:00</td>
                        <td>Jan Kowalski</td>
                        <td>użytkownik dodał nowe rozliczenie czasu pracy dla sprawy {case name}</td>
                        <td>
                            <button type="button" class="button">Przejdź</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2021-05-30 13:20:00</td>
                        <td>Antoni Nowak</td>
                        <td>użytkownik dodał nową sprawę {case name}</td>
                        <td>
                            <button type="button" class="button">Przejdź</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2021-05-29 13:20:00</td>
                        <td>Antoni Nowak</td>
                        <td>użytkownik edytował rozliczenie czasu pracy dla sprawy {case name}</td>
                        <td>
                            <button type="button" class="button">Przejdź</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2021-05-28 13:20:00</td>
                        <td>Karolina Kiełbasa</td>
                        <td>użytkownik zamknął sprawę {case name}</td>
                        <td>
                            <button type="button" class="button">Przejdź</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="pagination">
                    <div class="pageLeft"><i class="fa fa-angle-double-left"></i></div>
                    <div class="pageLeft"><i class="fa fa-angle-left"></i></div>
                    <div class="currentPage">X z Y</div>
                    <div class="pageRight"><i class="fa fa-angle-right"></i></div>
                    <div class="pageRight"><i class="fa fa-angle-double-right"></i></div>
                </div>
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
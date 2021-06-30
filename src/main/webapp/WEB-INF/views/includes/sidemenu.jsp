<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="nav flex-column my-5 p-0 py-5">
    <li id="menu-dashboard" class="nav-item fs-5 px-4 py-2 my-1 text-center" onclick="location.href='/dashboard'"><i class="fa fa-home"></i><span class="menuText"> Strona główna</span>
    </li>
    <li id="menu-timesheet" class="nav-item fs-5 px-4 py-2 my-1 text-center" onclick="location.href='/timesheet/add'"><i class="fa fa-plus-circle"></i><span
            class="menuText"> Dodaj rozliczenie</span></li>
    <li id="menu-clients" class="nav-item fs-5 px-4 py-2 my-1 text-center" onclick="location.href='/clients/list'"><i class="fa fa-balance-scale"></i><span
            class="menuText"> Sprawy</span></li>
    <li id="menu-reports" class="nav-item fs-5 px-4 py-2 my-1 text-center" onclick="location.href='/reports/list'"><i class="fa fa-folder-open"></i><span
            class="menuText"> Raporty</span></li>
    <li id="menu-admin" class="nav-item fs-5 px-4 py-2 my-1 text-center" onclick="location.href='/admin/list'"><i class="fa fa-users"></i><span class="menuText"> Lista użytkowników</span>
    </li>
    <li id="menu-settings" class="nav-item fs-5 px-4 py-2 my-1 text-center" onclick="location.href='/settings'"><i class="fa fa-gear"></i><span class="menuText"> Ustawienia</span>
    </li>
    <li id="menu-logout" class="nav-item fs-5 px-4 py-2 my-1 text-center" onclick="location.href='/logout'"><i class="fa fa-sign-out"></i><span
            class="menuText"> Wyloguj</span></li>
</ul>
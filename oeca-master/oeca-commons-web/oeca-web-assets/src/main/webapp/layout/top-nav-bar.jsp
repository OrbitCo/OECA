<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<stripes:layout-definition>
    <script type="text/javascript">
        $(function () {
            $("#tab" + '${tab}').addClass("active");
        });
    </script>
    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <div class="navbar-brand" style="margin-top: -5px">
                    <img src="${pageContext.request.contextPath}/static/img/epa.png" style="width: 104px; height: 32px"/>
                </div>
                <p class="navbar-text" style="margin: 8px 15px 0px 0px; font-size: 12px; line-height: 1">
                    United States<br/>Environmental Protection<br/>Agency
                </p>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li id="tabHome"><a href="">Home</a></li>
                    <li id="tabResources" class="dropdown">
                        <a href="#" class="" data-toggle="dropdown">Resources <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                            </li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right nav-help">
                    <li id="nav-bar-context"></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-info-sign"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                    <span class="navbar-dropdown-text">
                                        CDX Help Desk Email:  <a href="mailto:helpdesk@epacdx.net?subject=CDX Help Desk Request">helpdesk@epacdx.net</a>
                                    </span>
                                    <span class="navbar-dropdown-text">
                                        CDX Help Desk Phone: 888-890-1995 | (970) 494-5500 for International callers
                                    </span>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</stripes:layout-definition>
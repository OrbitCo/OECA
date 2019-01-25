<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%--<!-- EPA Template version 4.1.1, 05 August 2010 -->--%>
<stripes:layout-definition>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="${pageContext.request.contextPath}/static/img/favicon.ico">

        <title>${not empty title ? title : "OECA Application"}</title>
		<script type="application/javascript">
			var config = {
				ctx: "${pageContext.request.contextPath}",
				auth: {
					ctx: "${actionBean.authBaseUrl}"
				},
                registration: {
                    ctx: "${actionBean.registrationBaseUrl}"
                },
                ref: {
                    ctx: "${actionBean.refBaseUrl}"
                },
				cdx: {
					forgotUserUrl: "${actionBean.cdxForgotUserUrl}",
					forgotPassUrl: "${actionBean.cdxForgotPassUrl}",
                    activateAccountUrl: "${actionBean.cdxActivateAccountUrl}"
				}
			}
        </script>
        <!-- Bootstrap core CSS -->
        <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/bootstrap-switch.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/bootstrap-dialog.min.css" rel="stylesheet">

        <%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/datatables/dataTables.bootstrap.min.css"/>--%>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/datatables/responsive.bootstrap.min.css"/>

        <link href="${pageContext.request.contextPath}/static/css/select2/select2.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/select2/select2-bootstrap.css" rel="stylesheet">

        <link href="${pageContext.request.contextPath}/static/css/multiselect/bootstrap-multiselect.css"
              rel="stylesheet">

        <link href="${pageContext.request.contextPath}/static/css/tokenfield/bootstrap-tokenfield.css" rel="stylesheet">

        <link href="${pageContext.request.contextPath}/static/css/datepicker/datepicker3.css" rel="stylesheet">

        <link href="${pageContext.request.contextPath}/static/css/toastr/toastr.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/font-awesome.min.css" rel="stylesheet">


        <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
        <!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/assets/ie8-responsive-file-warning.js"></script>
        <![endif]-->
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/assets/ie-emulation-modes-warning.js"></script>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/assets/ie10-viewport-bug-workaround.js"></script>

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/assets/html5shiv.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/assets/respond.min.js"></script>
        <![endif]-->

        <script src="${pageContext.request.contextPath}/static/js/jquery/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/bootstrap.min.js"></script>
        <%--<script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>--%>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/bootstrap-switch.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/bootstrap-dialog.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/bootstrap-session-timeout.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/hide-show-password/hideShowPassword.min.js"></script>

        <!-- Select2 -->
        <script src="${pageContext.request.contextPath}/static/js/select2/select2.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/select2/select2-searchInputPlaceholder.js"></script>

        <!-- Data Tables -->
        <%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/data-tables/jquery.dataTables.min.js"></script>--%>
        <%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/data-tables/dataTables.bootstrap.min.js"></script>--%>
        <%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/data-tables/responsive/dataTables.responsive.min.js"></script>--%>
        <%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/data-tables/responsive/responsive.bootstrap.min.js"></script>--%>

        <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/data-tables-1.10/datatables.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/data-tables-1.10/jquery.dataTables.min.js"></script>

        <!--TokenField-->
        <script src="${pageContext.request.contextPath}/static/js/tokenfield/bootstrap-tokenfield.min.js"></script>

        <!-- Datepicker -->
        <script src="${pageContext.request.contextPath}/static/js/datepicker/bootstrap-datepicker.js"></script>

        <!-- Datepicker -->
        <script src="${pageContext.request.contextPath}/static/js/moment/moment.min.js"></script>

        <%--Combodate (dropdown dates--%>
        <script src="${pageContext.request.contextPath}/static/js/combodate/combodate.js"></script>

        <!-- Knockout -->
        <script src="${pageContext.request.contextPath}/static/js/knockout/knockout-3.4.0.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/knockout/knockout.mapping-latest.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/knockout/knockout.validation.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/knockout/knockout-rule-engine.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/knockout/knockout-validation-rules.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/knockstrap/knockstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/knockout/knockout-additional-bindings.js"></script>

        <!-- Multi-Select (must be loaded after kockout, as there are bindings being registered)-->
        <script src="${pageContext.request.contextPath}/static/js/multiselect/bootstrap-multiselect.js"></script>

        <!-- Pager -->
        <script src="${pageContext.request.contextPath}/static/js/pager/pager.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/jquery/jquery.ba-hashchange.min.js"></script>

        <script src="${pageContext.request.contextPath}/static/js/jquery/jquery.blockUI.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/toastr/toastr.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/jquery/jquery.inputmask.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/print-this/printThis.js"></script>

        <script src="${pageContext.request.contextPath}/static/js/iodash/lodash.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/js-cookie/js.cookie.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/postaljs/postal.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/bootstrap/bootstrap-tour.min.js"></script>


        <%-- OECA --%>
        <script src="${pageContext.request.contextPath}/static/js/oeca.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/oeca-components/oeca-common-models.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/oeca-register-models.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/oeca-modal.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/oeca-common.js"></script>

        <link href="${pageContext.request.contextPath}/static/css/navbar-fixed-top.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/oeca.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/oeca-print.css" rel="stylesheet" media="print">
        <link href="${pageContext.request.contextPath}/static/css/pager.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/bootstrap-tour.min.css" rel="stylesheet">


        <stripes:layout-component name="additionalHead">

        </stripes:layout-component>
		
        <security:authorize access="isAuthenticated()">
            <script type="application/javascript">

            </script>
        </security:authorize>
    </head>

    <body>
    <stripes:layout-component name="topNavBar">
        <stripes:layout-render name="/layout/top-nav-bar.jsp"/>
    </stripes:layout-component>
    <div id="container" class="container">
        <stripes:layout-component name="container">
            [This is the body/div[class=container]
        </stripes:layout-component>
    </div>

    <stripes:layout-component name="footer">

    </stripes:layout-component>

    </body>
    </html>
</stripes:layout-definition>
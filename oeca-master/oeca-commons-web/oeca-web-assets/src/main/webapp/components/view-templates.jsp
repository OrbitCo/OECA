<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="stripes"
           uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>
<stripes:layout-definition>
    <script type="text/html" id="underlined-field">
        <span class="cor-underline" data-bind="css: $data.css != undefined ? $data.css : ''">
            <span class="form-control-static">
                <span data-bind="text: field"></span>
            </span>
        </span>
    </script>
    <script type="text/html" id="underlined-field-group">
        <!-- ko foreach: fields -->
            <span class="cor-underline" data-bind="css: $data.css != undefined ? $data.css : ''">
                <span class="form-control-static">
                    <span data-bind="text: field"></span>
                </span>
            </span>
        <!-- /ko -->
    </script>
    <script type="text/html" id="view-checkbox">
        <label>
            <input type="radio" class="sr-only" disabled/>
            <span class="fa" data-bind="css: {'fa-check-square-o': isChecked, 'fa-square-o': !isChecked}"></span>
            <span data-bind="text: text"></span>
        </label>
    </script>
    <script type="text/html" id="yes-no-boxes">
        <label class="yes-no-box">
            <input type="radio" class="sr-only" disabled/>
            <span class="fa" data-bind="css: {
                        'fa-check-square-o': ko.utils.unwrapObservable(field) == true,
                        'fa-square-o': ko.utils.unwrapObservable(field) != true
                     }"></span>
            YES
        </label>
        <label class="yes-no-box">
            <input type="radio" class="sr-only" disabled/>
            <span class="fa" data-bind="css: {
                        'fa-check-square-o': ko.utils.unwrapObservable(field) == false,
                        'fa-square-o': ko.utils.unwrapObservable(field) != false
                     }"></span>
            NO
        </label>
    </script>
    <script type="text/html" id="text-block-field">
        <blockquote data-bind="visible: $data.field, css: $data.css != undefined ? $data.css : ''">
            <span style="word-wrap: break-word" data-bind="text: field"></span>
        </blockquote>
    </script>
    <script type="text/html" id="cor-address">
        <div class="row">
            <div class="col-xs-12 form-group cor-underline-group">
                <label class="control-label">Street/Location:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: address1
                                 }
                            }"></span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6 form-group cor-underline-group">
                <label class="control-label">City:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: city
                                }
                            }"></span>
            </div>
            <div class="col-xs-2 form-group cor-underline-group">
                <label class="control-label">State:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: stateCode
                                }
                            }"></span>
            </div>
            <div class="col-xs-4 form-group cor-underline-group">
                <label class="control-label">Zip Code:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: zipCode
                                }
                            }"></span>
            </div>
        </div>
		<!-- ko ifnot: $parent.hideCounty -->
        <div class="row">
            <div class="col-xs-12 form-group cor-underline-group">
                <label class="control-label">County or Similar Government Subdivision:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: county
                                }
                            }"></span>
            </div>
        </div>
		<!-- /ko -->
    </script>
    <script type="text/html" id="cor-contact">
        <!-- ko if: $data.firstName != undefined -->
        <div class="form-group cor-underline-group">
            <label class="control-label">First Name&nbsp;&nbsp;&nbsp;Middle Initial&nbsp;&nbsp;&nbsp;Last Name:</label>
            <span class="cor-underline half">
                <span class="form-control-static">
                    <span data-bind="text: $data.firstName"></span>
                </span>
            </span>
            <!-- ko if: $data.middleInitial -->
            <span class="cor-underline xs">
                <span class="form-control-static">
                    <span data-bind="text: $data.middleInitial"></span>
                </span>
            </span>
            <!-- /ko -->
            <!-- ko ifnot: $data.middleInitial -->
            <span class="cor-underline xs">
                <span class="form-control-static">
                    <span>&nbsp;</span>
                </span>
            </span>
            <!-- /ko -->
            <span class="cor-underline half">
                <span class="form-control-static">
                    <span data-bind="text: $data.lastName"></span>
                </span>
            </span>
        </div>
        <!-- /ko -->
        <!-- ko if: $data.title != undefined -->
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Title:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: title
                                }
                            }"></span>
                    </div>
                </div>
            </div>
        <!-- /ko -->
        <!-- ko if: $data.phone != undefined -->
            <div class="row">
                <div class="col-xs-4">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Phone:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: phone
                                }
                            }"></span>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Ext.</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: phoneExtension
                                }
                            }"></span>
                    </div>
                </div>
            </div>
        <!-- /ko -->
        <!-- ko if: $data.email != undefined -->
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Email:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: email
                                }
                            }"></span>
                    </div>
                </div>
            </div>
        <!-- /ko -->
    </script>
    <script type="text/html" id="cor-location">
        <div class="row">
            <div class="col-xs-6">
                <div class="form-group cor-underline-group">
                    <label class="control-label" for="lat-long">Latitude/Longitude:</label>
                    <span data-bind="template: {
                                        name: 'underlined-field',
                                        data: {
                                            field: display
                                        }
                                    }"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6">
                <div class="form-group cor-underline-group">
                    <label class="control-label" for="lat-long-data-source">Latitude/Longitude Data Source:</label>
                    <span id="lat-long-data-source" data-bind="template: {
                                                        name: 'underlined-field',
                                                        data: {
                                                            field: latLongDataSource
                                                        }
                                                    }"></span>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="form-group cor-underline-group">
                    <label class="control-label" for="horizontal-ref">Horizontal Reference Datum:</label>
                    <span id="horizontal-ref" data-bind="template: {
                                                            name: 'underlined-field',
                                                            data: {
                                                                field: horizontalReferenceDatum
                                                            }
                                                        }"></span>
                </div>
            </div>
        </div>
    </script>
    <script type="text/html" id="cor-certification">
        <div class="panel panel-default">
            <div class="panel-heading">Certification Information</div>
            <div class="panel-body">
                <!-- Needed for CoR since certification information is not saved until after CoR generation -->
                <div data-bind="visible: $root.viewCor === 'true'">
                    <div class="row">
                        <div class="col-sm-12">
                            <p>
                                I certify under penalty of law that this document and all attachments were prepared under my
                                direction or supervision in accordance with a system designed to assure that qualified
                                personnel properly gathered and evaluated the information submitted.
                                Based on my inquiry of the person or persons who manage the system, or those persons
                                directly responsible for gathering the information, the information submitted is, to the
                                best of my knowledge and belief, true, accurate, and complete.
                                I have no personal knowledge that the information submitted is other than true, accurate,
                                and complete. I am aware that there are significant penalties for submitting false
                                information, including the possibility of fine and imprisonment for knowing violations.
                                Signing an electronic document on behalf of another person is subject to criminal, civil,
                                administrative, or other lawful action.
                            </p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="control-label" for="certifier-name">Certified By:</label>
                                <span class="form-group-static" id="certifier-name">
                                ${actionBean.user.name}
                                (${actionBean.user.username})
                            </span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="control-label" for="certified-date">Certified On:</label>
                                <span class="form-group-static" id="certified-date" data-bind="text: oeca.utils.formatDateTime(new Date())"></span>
                            </div>
                        </div>
                    </div>
                </div>

                <div data-bind="visible: $root.viewCor !== 'true'">
                    <!-- ko if: operatorInformation.certifier() -->
                    <div class="row">
                        <div class="col-sm-12">
                            <p>
                                I certify under penalty of law that this document and all attachments were prepared under my
                                direction or supervision in accordance with a system designed to assure that qualified
                                personnel properly gathered and evaluated the information submitted.
                                Based on my inquiry of the person or persons who manage the system, or those persons
                                directly responsible for gathering the information, the information submitted is, to the
                                best of my knowledge and belief, true, accurate, and complete.
                                I have no personal knowledge that the information submitted is other than true, accurate,
                                and complete. I am aware that there are significant penalties for submitting false
                                information, including the possibility of fine and imprisonment for knowing violations.
                                Signing an electronic document on behalf of another person is subject to criminal, civil,
                                administrative, or other lawful action.
                            </p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="control-label" for="certifier-name">Certified By:</label>
                                <span class="form-group-static" id="certifier-name">
                                <span data-bind="text: operatorInformation.certifier().name"></span>
                                    <!-- ko if: operatorInformation.certifier().userId -->
                                (<span data-bind="text: operatorInformation.certifier().userId"></span>)
                                    <!-- /ko -->
                            </span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <label class="control-label" for="certified-date">Certified On:</label>
                                <span class="form-group-static" id="certified-date" data-bind="text: oeca.utils.formatDateTime($parent.form().certifiedDate())"></span>
                            </div>
                        </div>
                    </div>
                    <!-- /ko -->
                    <!-- ko ifnot: operatorInformation.certifier -->
                    Form has not been certified yet.
                    <!-- /ko -->
                </div>
            </div>
        </div>
    </script>
</stripes:layout-definition>
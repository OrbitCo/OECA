rcra.createNamespace("reports");
rcra.reports = {
    $criteriaModal: null,
    searchCriteria: null,
    contextPath: null,
    reportConfig: null,
    registerModal: function (modal) {
        rcra.reports.$criteriaModal = $("#reportCriteria_modalDialog");

        if (modal) {
            rcra.reports.$criteriaModal = modal;
        }

        if (!rcra.reports.$criteriaModal.length > 0) {
            throw new EventException("Criteria modal is not defined.");
            return;
        }

        rcra.reports.$criteriaModal.on('hidden.bs.modal', function () {
            var context = rcra.reports.$criteriaModal.find(".modal-content");
            ko.cleanNode(context[0]);
            context =  rcra.reports.$criteriaModal.find(".modal-footer");
            ko.cleanNode(context[0]);

            $("#custom-footer-buttons-placeholder").html("");
            $(".validationMessage").html("");

        });
    },
    showReportCriteria: function (reportData, contextPath, criteriaData) {
        var criteriaModalBody = rcra.reports.$criteriaModal.find(".modal-body");
        var criteriaModalFooter = rcra.reports.$criteriaModal.find(".modal-footer");

        var title = reportData.reportName();
        var path = reportData.parameterPage();

        if (criteriaData) {
            if (typeof criteriaData == "string") {
                criteriaData = JSON.parse(criteriaData);
            }

            criteriaData = ko.mapping.fromJS(criteriaData);
        }

        rcra.reports.searchCriteria = criteriaData;
        rcra.reports.contextPath = contextPath;
        rcra.reports.reportConfig = reportData;

        criteriaModalFooter.find("#report-description").text(reportData.description());

        criteriaModalBody.load(contextPath + "/static/html-templates" + path + "?_=" + new Date().getTime(), function() {
            rcra.reports.$criteriaModal.find(".modal-title").text(title);
            rcra.reports.$criteriaModal.modal("show");
        });
    },
    applyTemplateBindings: function (viewModel, options) {
        if (!options) {
            options = {};
        }

        var settings = {
            submitButtonText: "Submit",
            cancelButtonText: "Cancel",
            hideSubmitButton: false,
            hideCancelButton: false,
            beforeSubmit: null,
            afterSubmit: null,
            enableValidation: true,
            validationSettings: {deep: true, observable: false}
        };

        settings = $.extend(settings, options);

        if (rcra.reports.searchCriteria) {
            //Prepopulate bound values
            ko.mapping.fromJS(rcra.reports.searchCriteria, {}, viewModel.criteria);
        }

        //Submit report processing request to server
        viewModel.submit = function() {
            if (settings.beforeSubmit) {
                settings.beforeSubmit();
            }

            if (settings.enableValidation) {
                if (!viewModel.isValid()) {
                    viewModel.errors.showAllMessages();
                    return;
                }
            }

            viewModel.criteria = rcra.cleanKoProperties(viewModel.criteria);

            if (viewModel.criteria.__ko_mapping__) {
                delete (viewModel.criteria.__ko_mapping__);
            }

            var data = ko.mapping.toJSON({
                parameters: viewModel.criteria,
                reportCode: rcra.reports.reportConfig.code(),
                description: viewModel.reportDescription()
            });

            rcra.logger.debug(data);

            $.ajax({
                 url: rcra.reports.contextPath + "/rest/report/new",
                 data: data,
                 dataType: "json",
                 type: "POST",
                 contentType: rcra.xhrSettings.mimeTypes.JSON,
                 beforeSend: rcra.xhrSettings.setJsonAcceptHeader,
                 success: function (result) {
                     if (settings.afterSubmit) {
                        settings.afterSubmit(result);
                     } else {
                        window.location.href = rcra.reports.contextPath + "/action/secured/reports/report-history";
                     }
                 }
             });

            //ToDo: remove code below when server side is complete
            /*var result = {
                id: 1,
                criteria: ko.mapping.toJS(viewModel.criteria),
                dateCreated: new Date()
            };

            if (settings.afterSubmit) {
                settings.afterSubmit(result);
            } else {
                window.location.href = rcra.reports.contextPath + "/action/secured/reports/report-history";
            }*/
        };

        var $footerPlaceholder = $("#footer-buttons-placeholder");
        $footerPlaceholder.html("");

        var $submitButton = $("<button/>",
            {
                "type": "button",
                "class": "btn btn-primary pull-left",
                "data-bind": "click: submit",
                "text": settings.submitButtonText
            });

        var $closeButton = $("<button/>",
            {
                "type": "button",
                "class": "btn btn-default pull-left",
                "data-dismiss": "modal",
                "text": settings.cancelButtonText
            });

        //Set additional buttons on modal
        var customButtons = $("#custom-footer-buttons").html();

        if (!settings.hideSubmitButton) {
            $footerPlaceholder.append($submitButton);
        }

        if (customButtons.length > 0) {
            $footerPlaceholder.append(customButtons);
        }

        if (!settings.hideCancelButton) {
            $footerPlaceholder.append($closeButton);
        }

        //Set context of Knockout bindings
        var $context = $(".criteria-modal").find(".modal-content");

        viewModel.reportDescription = ko.observable();

        if (settings.enableValidation) {
            if (!viewModel.errors) {
                viewModel.errors = ko.validation.group(viewModel, settings.validationSettings);
            }
            if (!viewModel.isValid) {
                viewModel.isValid = function () {
                    return viewModel.errors().length <= 0;
                };
            }

            ko.applyBindings(ko.validatedObservable(viewModel, settings.validationSettings), $context[0]);
        } else {
            ko.applyBindings(viewModel, $context[0]);
        }
    }
};
var userTypeModel = function() {
    var self = this;
    self.code = ko.observable();
    self.userRoleId = ko.observable();
    self.dataflow = ko.observable();
    self.description = ko.observable();
    self.status = ko.observable();
    self.esaRequirement = ko.observable();
    self.signatureQuestionsRequired = ko.observable();
    self.subject = ko.observable();
};
//Instance-Role model used for registration where instance(dataflow) is selected by the user from a set of dataflows
var instanceRoleViewModel = function () {
    var self = this;
    self.instanceName = ko.observable().extend({
        required: true
    });
    self.userType = ko.observable(new userTypeModel()).extend({
        required: true
    });
    self.userTypes = ko.observableArray();
    self.instanceName.subscribe(function () {
        if (self.instanceName() == undefined) {
            self.userTypes(undefined);
            self.userType(undefined);
        } else {
            $.getJSON(config.ctx + '/api/registration/v1/userType?dataflowAcronym=' + self.instanceName(),
                function (data) {
                    self.userTypes(data);
                }
            );
        }
    });
    self.errors = ko.validation.group(self, {deep: true});
};

//Dataflow-role model is used in registration with a single dataflow
var dataflowRoleViewModel = function(dataflowAcronym) {
    var self = this;
    self.dataflowAcronym = ko.observable(dataflowAcronym).extend({
        required: true
    });
    self.roles = ko.observableArray();
    $.getJSON(config.registration.ctx + '/api/registration/v1/role?dataflowAcronym='+self.dataflowAcronym(),
        function (data) {
            self.roles(data);
        }
    );
    self.role = ko.observable(new userTypeModel()).extend({
        required: true
    });
    self.errors = ko.validation.group(self, {deep: true});
};

var persInfoViewModel = function () {
    var self = this;
    self.userId = ko.observable();
    self.password = ko.observable();
    self.title = ko.observable().extend({required: true});
    self.firstName = ko.observable().extend({required: true, maxLength: 20, nonNumeric: true});
    self.middleInitial = ko.observable().extend({alphaOnly: true});
    self.lastName = ko.observable().extend({required: true, maxLength: 30, nonNumeric: true});
    self.suffix = ko.observable();
    self.jobTitle = ko.observable().extend({maxLength: 38, nonNumeric: true});

    self.errors = ko.validation.group(self, {deep: true});

};

var questionAnswerModel = function() {
    var self = this;
    self.question = ko.observable().extend({ required: true });
    self.answer = ko.observable('').extend({
        required: true, maxLength: 30 });
};

var userPassViewModel = function () {
    var self = this;

//        self.userIdPass = ko.observable(new userIdPassPart());
    self.userIdErrorMessage = ko.observable();
    self.passwordErrorMessage = ko.observable();
    self.enablePasswordInput = ko.observable(false);//password fields are disabled as long as userId is empty

    self.userId = ko.observable().extend({required: true,
        validation: {
            async: true,
            validator: function (val, params, callback) {
                var userString = ko.toJSON({userId: val, password: ""});
                self.userIdErrorMessage(null);
                $.ajax({
                    url: config.registration.ctx + '/api/registration/v1/user/validation/id',
                    data: userString,
                    dataType: "json",
                    contentType: oeca.xhrSettings.mimeTypes.JSON,
                    type: 'POST',
                    success: function (data) {
                        self.userIdErrorMessage(null);
                        callback(data);
                    },
                    statusCode: {
                        500: function(data) {
                            if (data.responseJSON.code === "E_InvalidArgument") {
                                self.userIdErrorMessage(data.responseJSON.message);
                                toastr.remove();
                                callback(false);
                            }
                        }
                    }
                })
            },
            message: function () {
                if (self.userIdErrorMessage() !== null && self.userIdErrorMessage !== undefined) {
                    return ko.toJS(self.userIdErrorMessage);
                } else { return "User ID already exists."}
            }
        }});

    self.userId.subscribe(function (val) {
        if (val !== null && val !== '') {
            self.enablePasswordInput(true);
        } else {
            self.enablePasswordInput(false);
        }
    });

    self.password = ko.observable().extend({required: true,
        validation: {
            async: true,
            validator: function (val, params, callback) {
                self.passwordErrorMessage(null);
                if (self.userId() === undefined || self.userId() === null) {
                    self.passwordErrorMessage("Please, enter User ID.");
                    callback(false);
                } else {
                    var userString = ko.toJSON({userId: self.userId, password: val});
                    $.ajax({
                        url: config.registration.ctx + '/api/registration/v1/user/validation/password',
                        data: userString,
                        dataType: "json",
                        contentType: oeca.xhrSettings.mimeTypes.JSON,
                        type: 'POST',
                        statusCode: {
                            500: function(data) {
                                if (data.responseJSON.code === "E_WeakPassword" || data.responseJSON.code === "E_ValidationError") {
                                    self.passwordErrorMessage(data.responseJSON.message);
                                    callback(false);
                                } else {
                                    self.passwordErrorMessage(data.responseJSON.code);
                                    callback(false); }
                            },
                            204: function () {
                                toastr.remove();
                                callback(true);
                            }
                        }
                    })
                }
            },
            message: function () {
                if (self.passwordErrorMessage() !== null || self.passwordErrorMessage !== undefined) {
                    return ko.toJS(self.passwordErrorMessage);
                }
            }

        }
    });
    self.verifyPassword = ko.observable().extend({
        mustEqual: {
            params: self.password,
            message: "Passwords do not match."
        },
        required:true
    });
    self.secretAnswer1 = ko.observable(new questionAnswerModel());
    self.secretAnswer2 = ko.observable(new questionAnswerModel());
    self.secretAnswer3 = ko.observable(new questionAnswerModel());
    self.termsCheck = ko.observable(false).extend({
        checked: {message: 'Agreement required'}
    });
    self.errors = ko.validation.group(self, {deep: true});
};

var esignViewModel = function () {
    var self = this;

    self.electronicSignatureAnswer1 = ko.observable(new questionAnswerModel());
    self.electronicSignatureAnswer2 = ko.observable(new questionAnswerModel());
    self.electronicSignatureAnswer3 = ko.observable(new questionAnswerModel());
    self.electronicSignatureAnswer4 = ko.observable(new questionAnswerModel());
    self.electronicSignatureAnswer5 = ko.observable(new questionAnswerModel());

    self.electronicSignatureAnswer1().answer.extend({
        unique: {
            collection: function() {
                return [self.electronicSignatureAnswer2().answer(), self.electronicSignatureAnswer3().answer(),
                    self.electronicSignatureAnswer4().answer(),self.electronicSignatureAnswer5().answer()]
            },
            externalValue: true
        }
    });
    self.electronicSignatureAnswer2().answer.extend({
        unique: {
            collection: function() {
                return [self.electronicSignatureAnswer1().answer(), self.electronicSignatureAnswer3().answer(),
                    self.electronicSignatureAnswer4().answer(),self.electronicSignatureAnswer5().answer()]
            },
            externalValue: true
        }
    });
    self.electronicSignatureAnswer3().answer.extend({
        unique: {
            collection: function() {
                return [self.electronicSignatureAnswer1().answer(), self.electronicSignatureAnswer2().answer(),
                    self.electronicSignatureAnswer4().answer(),self.electronicSignatureAnswer5().answer()]
            },
            externalValue: true
        }
    });
    self.electronicSignatureAnswer4().answer.extend({
        unique: {
            collection: function() {
                return [self.electronicSignatureAnswer1().answer(), self.electronicSignatureAnswer2().answer(),
                    self.electronicSignatureAnswer3().answer(),self.electronicSignatureAnswer5().answer()]
            },
            externalValue: true
        }
    });
    self.electronicSignatureAnswer5().answer.extend({
        unique: {
            collection: function() {
                return [self.electronicSignatureAnswer1().answer(), self.electronicSignatureAnswer2().answer(),
                    self.electronicSignatureAnswer3().answer(),self.electronicSignatureAnswer4().answer()]
            },
            externalValue: true
        }
    });
    self.errors = ko.validation.group(self, {deep: true, observable: true});
};

var orgSearchViewModel = function () {
    var self = this;
    self.organizationName = ko.observable('').extend({required: true, minLengthTrimmed: 3});
    self.mailingAddress1 = ko.observable('');
    self.mailingAddress2 = ko.observable('');
    self.city = ko.observable('');
    self.stateCode = ko.observable('').extend({required:true});
    self.zip = ko.observable('');

    self.errors = ko.validation.group(self);
};

var cdxOrgViewModel = function () {
    var self = this;

    self.organizationName = ko.observable();
    self.userOrganizationId = ko.observable();
    self.organizationId = ko.observable();
    self.mailingAddress1 = ko.observable();
    self.mailingAddress2 = ko.observable();
    self.mailingAddress3 = ko.observable();
    self.mailingAddress4 = ko.observable();
    self.city = ko.observable();
    self.stateCode = ko.observable();
    self.zip = ko.observable();
    self.countryCode = ko.observable();
    self.email = ko.observable(null).extend({required: true});
    self.verifyEmail = ko.observable(null).extend({
        mustEqualNoCase: {
            params: self.email,
            message: "Emails do not match."
        },
        required:true
    });
    self.phone = ko.observable(null).extend({required: true, phoneUS: true});
    self.phoneExtension = ko.observable(null).extend({digit:true, maxLength: 5});
    self.fax = ko.observable();
    self.primaryOrg = ko.observable();
    self.cdxEsaStatus = ko.observable();

    self.confirmationCode = ko.observable(null).extend({required:true});
    self.confCodeErrorMessage = ko.observable(null);
    self.codeValidationAdded = ko.observable(false);
    self.codeIsValid = ko.observable(false);

    self.errors = ko.validation.group(self, {deep: true, observable: true, live: true});
};

var newOrgViewModel = function () {
    var self = this;
    //cdxOrgViewModel.call(self);
    self.organizationName = ko.observable(null).extend({required: true});
    self.userOrganizationId = ko.observable();
    self.mailingAddress1 = ko.observable(null).extend({required: true});
    self.mailingAddress2 = ko.observable();
    self.mailingAddress3 = ko.observable();
    self.mailingAddress4 = ko.observable();
    self.city = ko.observable(null).extend({required: true});
    self.stateCode = ko.observable(null).extend({required: true});
    self.zip = ko.observable(null).extend({required: true, zipCodeUS: true, maxLength: 5});
    self.countryCode = ko.observable(null).extend({required: true});
    self.email = ko.observable(null).extend({required: true});
    self.verifyEmail = ko.observable(null).extend({
        mustEqualNoCase: {
            params: self.email,
            message: "Emails do not match."
        },
        required:true
    });
    self.phone = ko.observable(null).extend({required: true, phoneUS: true});
    self.phoneExtension = ko.observable(null).extend({digit:true, maxLength: 5});
    self.fax = ko.observable();
    self.primaryOrg = ko.observable();
    self.cdxEsaStatus = ko.observable();

    self.confirmationCode = ko.observable(null).extend({required:true});
    self.confCodeErrorMessage = ko.observable(null);
    self.codeValidationAdded = ko.observable(false);
    self.codeIsValid = ko.observable(false);

    self.errors = ko.validation.group(self, {deep: true, observable: true, live: true});
};

var idProofingModel = function () {
    var self = this;
    self.userId = ko.observable();
    self.firstName = ko.observable();
    self.lastName = ko.observable();
    self.middleInitial = ko.observable();
    self.mailingAddress1 = ko.observable().extend({required: true, streetAddress: true});
    self.mailingAddress2 = ko.observable().extend({streetAddress: true});
    self.city = ko.observable().extend({required: true});
    self.state = ko.observable().extend({required: true});
    self.zip = ko.observable().extend({required: true, zipCodeUS: true});
    self.phone = ko.observable().extend({phoneUS: true});
    self.dateOfBirth = ko.observable().extend({required: true});
    //suppress DOB validation messages on entry
    self.dateOfBirth.subscribe(function() {
        setTimeout(function () {
            self.errors.showAllMessages(false);
        }, 200);
    });
    self.userRoleId = ko.observable();
    self.ssnlast4 = ko.observable().extend({required: true, maxLength: 4, digit: true});
    self.agreeToEsa = ko.observable(false).extend({
        checked: {message: 'Agreement required'}
    });
    //validate all fields on Agree to ESA
    self.agreeToEsa.subscribe(function() {
        self.errors.showAllMessages(true);
    });
    self.errors = ko.validation.group(self, {deep: true});

};

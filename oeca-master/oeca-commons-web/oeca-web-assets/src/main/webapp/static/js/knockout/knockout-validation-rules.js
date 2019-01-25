ko.validation.rules['firstCharAlphaOnly'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (val) {
        var pattern = new RegExp("^[A-Za-z]");
        return pattern.test(val);
    },
    message: 'This field must start with an alpha character.'
};

ko.validation.rules['noSpaces'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (val) {
        var pattern = new RegExp("^\\S*$");
        return pattern.test(val);
    },
    message: 'This field cannot contain spaces.'
};

ko.validation.rules['zipCodeUS'] = {
    validator: function (val) {
        if (val) {
            return /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(val);
        }
        return true;
    },
    message: 'Please specify a valid Zip Code.'
};

ko.validation.rules['zipCodeUSConditional'] = {
    validator: function (val, condition) {
        var required = false;
        if (typeof condition == 'function') {
            required = condition();
        }
        else {
            required = condition;
        }

        if (required) {
            return /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(val);
        } else {
            return true;
        }
    },
    message: 'Please specify a valid Zip Code.'
};

ko.validation.rules['mustEqual'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (val, otherField) {
        return val === this.getValue(otherField);
    },
    message: 'The fields must have the same value.'
};

ko.validation.rules['mustEqualNoCase'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (val, otherField) {
        if (val) {
            return val.toLowerCase() === this.getValue(otherField).toLowerCase();
        }
        return val === this.getValue(otherField);
    },
    message: 'The fields must have the same value.'
};

ko.validation.rules['lessThan'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (val, otherVal) {
        return val < otherVal;
    },
    message: function (val) {
        return 'Please enter a value less than ' + val +'.'
    }
};
ko.validation.rules['greaterThan'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (val, otherVal) {
        return val > otherVal;
    },
    message: function (val) {
        return 'Please enter a value greater than ' + val +'.'
    }
};

ko.validation.rules['alphaOnly'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (value) {
        if (!value) {
            return true;
        }

        return /^[A-Z]+$/i.test(value);
    },
    message: 'This field must not contain non-alpha characters'
};

ko.validation.rules['alphaNumericOnly'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (value) {
        if (!value) {
            return true;
        }

        return /^[a-z0-9]+$/i.test(value);
    },
    message: 'This field must not contain special characters.'
};

ko.validation.rules['nonNumeric'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (value) {
        if (!value) {
            return true;
        }

        return /^([^0-9]*)$/.test(value);
    },
    message: 'This field must not contain numbers.'
};

ko.validation.rules['password'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (value, params) {
        var userId = this.getValue(params.userId);
        if (userId) {
            if (value.toLowerCase() == userId.toLowerCase()) {
                params.message = "The password may not contain the User Name.";
                return false;
            }
        }

        if (value.toLowerCase().indexOf("password") >= 0) {
            params.message = "The password may not contain the word: password.";
            return false;
        }

        if (!/^[a-zA-Z0-9]+$/.test(value)) {
            params.message = "The password may only contain letters and numbers.";
            return false;
        }

        if (!/(?=.*[a-z]).*$/.test(value)) {
            params.message = "The password must contain at least one lowercase letter.";
            return false;
        }

        if (!/(?=.*[A-Z]).*$/.test(value)) {
            params.message = "The password must contain at least one uppercase letter.";
            return false;
        }

        if (!/(?=.*[0-9]).*$/.test(value)) {
            params.message = "The password must contain at least one number.";
            return false;
        }

        if (!/^[A-Za-z][A-Za-z0-9]*$/.test(value)) {
            params.message = "The password must begin with a letter.";
            return false;
        }

        if (value.length > 15) {
            params.message = "Passwords cannot be longer than 15 characters.";
            return false;
        }

        if (value.length < 8) {
            params.message = "Password must be at least 8 characters.";
            return false;
        }

        return true;
    },
    message: function(params, obsv) {
        if (params.message) {
            return params.message;
        }

        return "The password format is invalid.";
    }
};

ko.validation.rules['checked'] = {
    validator: function (value) {
        if (!value) {
            return false;
        }
        return true;
    },
    message: "At least one option is required"
};

ko.validation.rules['fromDate'] = {
    validator: function (val, otherVal) {
        return moment(otherVal).isBefore(val) || moment(val).isSame(otherVal);
    },
    message: function (val) {
        return 'The date must be greater than or equal to ' + val + '.'
    }
};

ko.validation.rules['futureDate'] = {
    validator: function (val) {
        var diff = moment().startOf('day').diff(moment(val), 'days');
        return diff >= 0;
    },
    message: 'The date must be no later than today.'
};

ko.validation.rules['step'] = {
    validator: function (val, step) {
        if (ko.validation.utils.isEmptyVal(val) || step === 'any')
            return true;

        var nStep = parseFloat(step);
        if (isNaN(nStep) || nStep <= 0)
            return true; // invalid step

        var d = this._decimalPlaces(nStep);
        var prec = Math.pow(10, d);

        var v = val * prec;
        var v1 = parseFloat(v.toFixed(0));
        var v2 = v;
        if (v1 !== v2)
            return false; // 'val' has more decimal places than 'step'

        nStep = parseFloat((nStep * prec).toFixed(0));

        return (v1 % nStep) === 0;
    },
    message: 'The value must increment by {0}',
    _decimalPlaces: function (num) {
        var match = ('' + num).match(/(?:\.(\d+))?(?:[eE]([+-]?\d+))?$/);
        if (!match) { return 0; }
        return Math.max(
            0,
            // Number of digits right of decimal point.
            (match[1] ? match[1].length : 0)
                // Adjust for scientific notation.
            - (match[2] ? +match[2] : 0));
    }
};

ko.validation.rules['streetAddress'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (value, params) {
        if (value) {
            return value.toLowerCase().indexOf("p.o. box") < 0 && value.toLowerCase().indexOf("po box") < 0;
        }
        return true;
    },
    message: "Please enter a street address (not P.O. box)."
}

/*
 * https://github.com/Knockout-Contrib/Knockout-Validation/wiki/User-Contributed-Rules
 * This rules checks the given array of objects/observables and returns
 * true if at least one of the elements validates agains the the default
 * 'required' rules
 *
 * Example:
 * self.mobilePhone.extend({ requiresOneOf: [self.homePhone, self.mobilePhone] });
 * self.homePhone.extend({ requiresOneOf: [self.homePhone, self.mobilePhone] });
 *
 */
ko.validation.rules['requiresOneOf'] = {
    getValue: function (o) {
        return (typeof o === 'function' ? o() : o);
    },
    validator: function (val, fields) {
        var self = this;

        var anyOne = ko.utils.arrayFirst(fields, function (field) {
            var stringTrimRegEx = /^\s+|\s+$/g,
                testVal;

            var val = self.getValue(field);

            if (val === undefined || val === null)
                return false;

            testVal = val;
            if (typeof (val) == "string") {
                testVal = val.replace(stringTrimRegEx, '');
            }

            return ((testVal + '').length > 0);

        });

        return (anyOne != null);
    },
    message: 'One of these fields is required'

};
ko.validation.rules['minLengthTrimmed'] = {
    validator: function (val, minLength) {
        if(ko.validation.utils.isEmptyVal(val)) { return true; }
        var normalizedVal = ko.validation.utils.isNumber(val) ? ('' + val) : val;
        return normalizedVal.trim().length >= minLength;
    },
    message: 'Please enter at least {0} characters, not including spaces.'
}
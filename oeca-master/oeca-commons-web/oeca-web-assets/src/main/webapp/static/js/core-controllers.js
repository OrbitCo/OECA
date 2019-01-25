/**
 * Created by smckay on 6/2/2017.
 */
function initNavigation() {
    ko.validation.registerExtenders();
    pager.Href.hash = '#!/';
    var vm = new NavigationalController();
    pager.extendWithPage(vm);
    ko.applyBindings(vm);
    pager.start();
}
var NavigationalController = function() {
    var self = this;
    self.component = function(name, params) {
        return function(page, callback) {
            var componentDiv = $('<div></div>');
            if(params) {
                componentDiv.attr('data-bind', "component: {name: '" + name + "', params: " + params + "}");
            }
            else {
                componentDiv.attr('data-bind', "component: '" + name + "'");
            }
            $(page.element).html(componentDiv);
            callback();
        }
    }
    self.disposeComponent = function(page, callback) {
        if(callback) {
            $(page.element).hide();
            callback();
            $(page.element).children().each(function(index, child) {
                ko.utils.domNodeDisposal.removeNode(child);
            });
        }
    }
    self.showCor = function() {
        self.viewCor = 'true';
    }
}
var BaseFormController = function(data, params) {
    var self = this;
    self.form = ko.observable(null);
    self.error = ko.observable(null);
    self.loadForm = function() {
        var id = ko.utils.unwrapObservable(params.id);
        var url = params.baseUrl + (id || '');
        $.getJSON(url, function(data) {
            self.form(new params.formObject(data));
        }).error(function() {

        });
    }
    self.loadForm();
    if(params.id.subscribe) {
        self.idSubscription = params.id.subscribe(function() {
            self.form(null);
            self.loadForm();
        });
    }
}
var BaseFormSectionController = function(data, params) {
    var self = this;
    self.expand = params.toggle;
    self.showSaveButton = params.nextSectionShown;
    self.errors = ko.validation.group(self, {deep: true});
    params.errors(self.errors);
    self.componentLoaded = function() {
        params.onLoad(true);
    };
    self.saveAndContinue = function() {
        if(self.errors().length > 0) {
            self.errors.showAllMessages();
        }
        else {
            params.completeCallback();
        }
    }
}
var ScreeningQuestion = function(question, dependency) {
    var self = this;
    self.question = question;
    self.dependency = ko.pureComputed(dependency);
    self.dependancySub = self.dependency.subscribe(function(val) {
        if(!val) {
            self.question(null);
        }
    });
    self.dispose = function() {
        self.dependencySub.dispose();
        self.dependency.dispose();
    }
}
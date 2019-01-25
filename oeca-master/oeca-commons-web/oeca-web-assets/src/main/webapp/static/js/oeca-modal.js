var ModalControl = function(settings) {
    var self = this;
    self.name = ko.observable(settings.name);
    self.open = ko.observable(false);
    self.popupModel = ko.observable(settings.model ? new settings.model(null, {modalControl: self}) : {});
    self.openModal = function(params) {
        params = params || {};
        if(params.reset) {
            self.reset();
        }
        else if(params.data) {
            var data = params.copy ? ko.mapping.toJS(params.data) : params.data;
            self.refreshData(data);
        }
        self.open(true);
    }
    self.reset = function() {
        if(!settings.reset) {
            throw "No reset function was set for modal " + self.name();
        }
        settings.reset(self.popupModel);
        self.open(false);
    }
    self.refreshData = function(data) {
        if(self.popupModel().refresh) {
            self.popupModel().refresh(data);
        }
        else {
            var mapSettings = settings.mapSettings || {};
            ko.mapping.fromJS(data, mapSettings, self.popupModel)
        }
        self.open(false);
    }
    self.closeModal = function(event, data) {
        self.open(false);
        postal.publish({
            channel: "modal",
            topic: self.name() + ".close." + event,
            data: {
                result: event,
                name: self.name(),
                data: data || self.popupModel()
            }
        });
    }
}
var popupRegistry = {};

function openModal(name, id, settings) {
    var popupId = id || name;
    if(!popupRegistry[popupId]) {
        var params = id ? {
            id: id
        } : {};
        var subscription = postal.subscribe({
            channel: 'modal',
            topic: popupId + '.init',
            callback: function(data, envelope) {
                popupRegistry[popupId].openModal(settings);
                postal.unsubscribe(subscription);
            }
        });
        var modalComponent = $('<div data-bind="component: {name: \'' + name + '\', params: ' + ko.toJSON(params) + '}"></div>');
        $('#popups').append(modalComponent);
        ko.applyBindings(null, modalComponent.get(0));
        popupRegistry[popupId] = "pending";
    }
    else if (popupRegistry[popupId] == "pending") {
        //modal is loading
    }
    else {
        popupRegistry[popupId].openModal(settings);
    }
}

//custom component loaders
ko.components.loaders.unshift({
	loadViewModel: function(name, viewModelConfig, callback) {
		if(viewModelConfig.url && viewModelConfig.viewModelClass) {
			$.getJSON(viewModelConfig.url, function(data) {
				callback(function(params, componentInfo) {
					var model = new viewModelConfig.viewModelClass(data);
					return model;
				})
			});
		}
		else {
			callback(null);
		}
	},
	loadTemplate: function(name, templateConfig, callback) {
		if(templateConfig.url) {
			$.get(templateConfig.url, null, function(data) {
				callback($.parseHTML(data));
			})
		}
		else {
			callback(null);
		}
	}
});
ko.components.loaders.unshift({
	loadViewModel: function(name, viewModelConfig, callback) {
		if(viewModelConfig.modal) {
			callback(function(params, componentInfo) {
				popupId = params.id || name;
				popupRegistry[popupId] = new ModalControl($.extend({
					name: popupId,
					model: viewModelConfig.viewModelClass ? viewModelConfig.viewModelClass : null,
				}, viewModelConfig.settings));
                postal.publish({
                    channel: 'modal',
                    topic: popupId + '.init'
                });
				return popupRegistry[popupId];
			});
		}
		else {
			callback(null);
		}
	},
	loadTemplate: function(name, templateConfig, callback) {
		if(templateConfig.modal) {
			$.get(config.ctx + '/action/modal/' + templateConfig.modal, function(data) {
				callback($.parseHTML(data));
			});
		}
		else {
			callback(null);
		}
	}
});
ko.components.loaders.unshift({
	loadTemplate: function(name, templateConfig, callback) {
		if(templateConfig.component) {
			$.get(config.ctx + '/action/components/' + templateConfig.component, function(data) {
				callback($.parseHTML(data));
			});
		}
		else callback(null);
	}
});
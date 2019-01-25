var Location = function(data) {
	var self = this;
	data = $.extend({
		latitude: null,
		longitude: null,
		latLongDataSource: null,
		horizontalReferenceDatum: null
	}, data);
	ko.mapping.fromJS(data, {
        latitude: {
			create: function(options) {
				return ko.observable(options.data).extend({round: 4});
			}
		},
		longitude: {
			create: function(options) {
				return ko.observable(options.data).extend({round: 4});
			}
		}
	}, self);
	//if the checked value is anything other than the available options check Other.
    self.otherCheckedValue = ko.observable(null);
    oeca.common.utils.setOtherCheckedValue(self.latLongDataSource, self.otherCheckedValue, oeca.common.constants.latLongDataSource, '');
	self.otherLatLongDataSource = ko.observable(null);
	self.latitudeDisplay = ko.pureComputed({
		read: function() {
			if (self.latitude() !== null && self.latitude() !== undefined) {
				return Math.abs(self.latitude());
			}
            return null;
        },
		write: function(val) {
		    var dir = self.latitudeDir();
		    if ((val >= 0 && dir == 'N') || (val < 0 && dir == 'S')) {
		        self.latitude(Math.abs(val));
		    }
		    else if ((val < 0 && dir == 'N') || (val >= 0 && dir == 'S')) {
		        self.latitude(Math.abs(val)*-1);
		    }
		}
    });
	self.longitudeDisplay = ko.pureComputed({
		read: function () {
			if (self.longitude() !== null && self.longitude() !== undefined) {
				return Math.abs(self.longitude());
			}
			return null;
		},
		write: function(val) {
		    var dir = self.longitudeDir();
		    if ((val >= 0 && dir == 'E') || (val < 0 && dir == 'W')) {
		        self.longitude(Math.abs(val));
		    }
		    else if ((val < 0 && dir == 'E') || (val >= 0 && dir == 'W')) {
		        self.longitude(Math.abs(val)*-1);
		    }
		}
    });
	self.latitudeDir = ko.pureComputed({
		read: function() {
            return self.latitude() < 0 ? 'S' : 'N'
        },
        write: function(dir) {
            if (dir == 'N') {
		        self.latitude(Math.abs(self.latitude()));
            }
            else if (dir == 'S') {
		        self.latitude(Math.abs(self.latitude())*-1);
            }
        }
    });
	self.longitudeDir = ko.pureComputed({
		read: function () {
            return self.longitude() > 0 ? 'E' : 'W';
        },
        write: function(dir) {
            if (dir == 'E') {
		        self.longitude(Math.abs(self.longitude()));
            }
            else if (dir == 'W') {
		        self.longitude(Math.abs(self.longitude())*-1);
            }
        }
    });
	self.getLatLong = ko.pureComputed(function() {
		if(self.latitude() && self.longitude()) {
			return {
				lat: self.latitude(),
				lng: self.longitude()
			};
		}
		else {
			return null;
		}
	});
	self.setLatLong = function(latLong) {
		self.latitude(latLong.lat);
		self.longitude(latLong.lng);
		self.latLongDataSource('Map');
	    self.horizontalReferenceDatum('WGS 84');
	};
	self.display = ko.pureComputed(function() {
		if(self.latitude() !== null && self.latitude() !== undefined && self.longitude() !== null && self.longitude() !== undefined) {
			return Math.abs((Math.round(self.latitude() * 10000) / 10000)) + '°' + self.latitudeDir() + ', ' +
				Math.abs((Math.round(self.longitude() * 10000) / 10000)) + '°' + self.longitudeDir();
		}
		else {
			return '';
		}
	});
};
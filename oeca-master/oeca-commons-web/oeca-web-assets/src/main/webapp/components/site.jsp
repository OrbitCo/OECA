<div class="row">
	<div class="col-sm-12 form-group">
		<label class="control-label" for="site-name" data-bind="text: labels.siteName"></label>
		<input id="site-name" class="form-control" type="text" maxlength="80" data-bind="value: siteName" />
	</div>
</div>
<hr>
<div class="h4" data-bind="text: labels.addressHeading"></div>
<address-info params="address: {
                       address1: siteAddress.address1,
                       address2: siteAddress.address2,
                       city: siteAddress.city,
                       state: siteAddress.stateCode,
                       zip: siteAddress.zipCode,
                       county: siteAddress.county
				}, 
				titles: labels.address,
				lockState: lockState,
				lockStateUrl: lockStateUrl"></address-info>
<hr>
<div class="row">
	<div class="col-sm-6">
		<div class="h4" data-bind="text: labels.locationHeading"></div><span class="help-block">Click on the map to automatically find Latitude and Longitude.</span>
		<location params="location: siteLocation,
							titles: labels.location"></location>
	</div>
	<div class="col-sm-6" data-bind="if: expand">
		<div style="height: 250px;" data-bind="leaflet: {
													latLongMarker: {
														latLong: siteLocation
													},
													center: {
														city: siteAddress.city(),
														state: siteAddress.stateCode(),
														lat: siteLocation.latitude(),
														long: siteLocation.longitude()
													}
												}"></div>
	</div>
</div>
<!-- ko if: showIndian -->
<hr>
<div class="row">
	<div class="col-md-12 form-group">
		<label class="control-label">Is your project located on a property of religious or cultural significance to an Indian tribe?</label>
		<div class="radio">
			<label>
				<input type="radio" name="religious-cultural-site" data-bind="checkedValue: true, checked: siteIndianCountry">
				Yes
			</label>
		</div>
		<div class="radio">
			<label>
				<input type="radio" name="religious-cultural-site" data-bind="checkedValue: false, checked: siteIndianCountry">
				No
			</label>
		</div>
	</div>
</div>
<div class="row subquestion" data-bind="slideVisible: siteIndianCountry">
	<span class="glyphicon glyphicon-share-alt"></span>
	<div class="col-md-12 form-group">
		<label class="control-label">Indicate which tribe this land is of religious or cultural significance to.</label>
		<select id="biaCode" class="form-control" data-bind="lookup: {
																	options: 'tribes',
																	filter: {
																		value: 'stateCode',
																		by: siteAddress.stateCode
																	}
																},
																value: siteIndianCountryTribe,
																optionsValue: 'tribeName',
																optionsText: 'tribeName',
																optionsCaption: '',
																valueAllowUnset: true,
																select2: {
																	placeholder: 'Select Tribe'
																}" style="width: 500px">
		</select>
	</div>
</div>
<!-- /ko -->
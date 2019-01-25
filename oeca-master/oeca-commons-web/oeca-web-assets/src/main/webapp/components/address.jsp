<%--
	Sample full JSON:
	{
		address1: null,
		address2: null,
		city: null,
		state: null,
		zip: null,
		zip4: null,
		county: null
	}
--%>
<!-- ko with: address -->
<div class="row">
	<div class="col-sm-12 form-group">
		<label class="control-label" for="address-1" data-bind="text: $parent.labels.address1"></label>
		<input id="address-1" class="form-control" type="text" maxlength="50" data-bind="value: address1" />
	</div>
</div>
<!-- ko if: $data.address2 != undefined -->
<div class="row">
	<div class="col-sm-12 form-group">
		<label class="control-label" for="address-2" data-bind="text: $parent.labels.address2"></label>
		<input id="address-2" class="form-control" type="text" maxlength="50" data-bind="value: address2" />
	</div>
</div>
<!-- /ko -->
<div class="row">
	<div class="col-sm-6 col-xs-12 form-group">
		<label class="control-label" for="city" data-bind="text: $parent.labels.city"></label>
		<input id="city" class="form-control" type="text" maxlength="30" data-bind="value: city" />
	</div>
	<div class="col-sm-6 col-xs-12 form-group">
		<label class="control-label" for="state" data-bind="text: $parent.labels.state"></label>
		<!-- ko ifnot: $parent.lockState -->
		<select style="width: 100%" class="form-control" data-bind="lookup: 'states',
						value: state,
						optionsText: 'stateName',
						optionsValue: 'stateCode',
						optionsCaption: '',
						valueAllowUnset: true,
						select2: {placeholder: 'Select a State'}">
		</select>
		<!-- /ko -->
		<!-- ko if: $parent.lockState -->
		<div class="input-group">
			<select style="width: 100%" class="form-control" data-bind="lookup: 'states',
				value: state,
				optionsText: 'stateName',
				optionsValue: 'stateCode',
				optionsCaption: '',
				valueAllowUnset: true,
				disable: true,
				select2: {placeholder: 'Select a State'}">
			</select>
			<div class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></div>
		</div>
		<a data-bind="attr: { href: $parent.lockStateUrl }">Why can't I edit this field?</a>
		<!-- /ko -->
	</div>
</div>
<div class="row">
	<div class="col-lg-4 col-md-4 col-sm-4 col-xs-6 form-group">
		<label class="control-label" for="zip" data-bind="text: $parent.labels.zip"></label>
		<input type="text" class="form-control" maxlength="5" data-bind="maskedZip: zip" />
	</div>
	<!-- ko if: $data.zip4 != undefined -->
	<div class="col-lg-2 col-md-2 col-sm-2 col-xs-6 form-group">
		<label class="control-label" for="zip4" data-bind="text: $parent.labels.zip4"></label>
		<input type="text" class="form-control" maxlength="4" data-bind="value: zip4"/>
	</div>
	<!-- /ko -->
	<!-- ko ifnot: $parent.hideCounty -->
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 form-group">
		<label class="control-label" for="county" data-bind="text: $parent.labels.county"></label>
		<select id="county" class="form-control" style="width: 100%" data-bind="lookup: {
																options: 'counties', 
																filter: {
																	value: 'stateCode',
																	by: state
																}
															},
															value: county,
															optionsText: 'countyName',
															optionsValue: 'countyName',
															valueAllowUnset: true,
															optionsCaption: '',
															select2: {
																placeholder: 'Select County'
															}">
		</select>
	</div>
	<!-- /ko -->
</div>
<!-- /ko -->
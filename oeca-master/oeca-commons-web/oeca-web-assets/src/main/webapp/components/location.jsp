<!-- ko with: location -->
<div class="row">
	<div class="col-sm-6 form-group">
		<label class="control-label" for="latitude" data-bind="text: $parent.labels.latitude"></label>
		<dfn data-bind="popover: oeca.common.definitions.latLong"><span class="glyphicon glyphicon-info-sign"></span></dfn>
		<div class="input-group">
			<input id="latitude" class="form-control" type="text" data-bind="maskedLatLong: latitudeDisplay" />
			<div class="input-group-btn">
				<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">&deg; <span data-bind="text: latitudeDir"></span> <span class="caret"></span></button>
				<ul class="dropdown-menu" style="min-width: 50px;">
					<li><a href="JavaScript:;" data-bind="click: function(){latitudeDir('N');}">&deg; N</a></li>
					<li><a href="JavaScript:;" data-bind="click: function(){latitudeDir('S');}">&deg; S</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="col-sm-6 form-group">
		<label class="control-label" for="longitude" data-bind="text: $parent.labels.longitude"></label>
		<dfn data-bind="popover: oeca.common.definitions.latLong"><span class="glyphicon glyphicon-info-sign"></span></dfn>
		<div class="input-group">
			<input id="longitude" class="form-control" type="text" data-bind="maskedLatLong: longitudeDisplay" />
			<div class="input-group-btn">
				<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">&deg; <span data-bind="text: longitudeDir"></span> <span class="caret"></span></button>
				<ul class="dropdown-menu" style="min-width: 50px;">
					<li><a href="JavaScript:;" data-bind="click: function(){longitudeDir('W');}">&deg; W</a></li>
					<li><a href="JavaScript:;" data-bind="click: function(){longitudeDir('E');}">&deg; E</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<!-- ko ifnot: $parent.hideDataSource -->
<div class="row">
	<div class="col-sm-6 form-group">
		<label class="control-label" data-bind="text: $parent.labels.dataSource"></label>
		<div class="radio">
			<label class="radio-inline">
				<input type="radio" name="lat-long-data-source" data-bind="checkedValue: oeca.common.constants.latLongDataSource.Map, checked: latLongDataSource" />
				Map
			</label>
			<label class="radio-inline">
				<input type="radio" name="lat-long-data-source" data-bind="checkedValue: oeca.common.constants.latLongDataSource.GPS, checked: latLongDataSource" />
				GPS
			</label>
			<label class="radio-inline">
				<input type="radio" name="lat-long-data-source" data-bind="checkedValue: otherCheckedValue, checked: latLongDataSource" />
				Other
			</label>
		</div>
	</div>
	<div class="col-sm-6 form-group required" data-bind="fadeVisible: latLongDataSource() == otherCheckedValue()">
		<label class="control-label" for="other-data-source" data-bind="text: $parent.labels.otherDataSource"></label>
		<input id="other-data-source" class="form-control" type="text" maxlength="100" data-bind="value: otherCheckedValue"/>
	</div>
</div>
<!-- /ko -->
<!-- ko ifnot: $parent.hideDatum -->
<div class="row">
	<div class="col-sm-12 form-group">
		<label class="control-label" data-bind="text: $parent.labels.datum"></label>
		<!-- ko ifnot: $parent.hideDatumTooltip -->
		    <dfn data-bind="popover: oeca.common.definitions.mapWidget"><span class="glyphicon glyphicon-info-sign"></span></dfn>
		<!-- /ko -->
		<div class="radio">
			<label class="radio-inline">
				<input type="radio" name="ref-datum" data-bind="checkedValue: 'NAD 27', checked: horizontalReferenceDatum" />
				NAD 27
			</label>
			<label class="radio-inline">
				<input type="radio" name="ref-datum" data-bind="checkedValue: 'NAD 83', checked: horizontalReferenceDatum" />
				NAD 83
			</label>
			<label class="radio-inline">
				<input type="radio" name="ref-datum" data-bind="checkedValue: 'WGS 84', checked: horizontalReferenceDatum" />
				WGS 84
			</label>
		</div>
	</div>
</div>
<!-- /ko -->
<!-- /ko -->
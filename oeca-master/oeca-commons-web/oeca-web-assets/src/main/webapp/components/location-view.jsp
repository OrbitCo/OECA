<!-- ko with: location -->
<div class="row">
	<div class="col-sm-12">
		<div class="form-group">
			<label class="control-label" for="latitude-longitude" data-bind="text: $parent.labels.latLong"></label>
			<span class="form-group-static" id="latitude-longitude" data-bind="html: display"></span>
		</div>
	</div>
</div>
<div class="row">
    <div class="col-sm-6">
        <div class="form-group">
            <label class="control-label" for="data-source" data-bind="text: $parent.labels.dataSource"></label>
            <span class="form-group-static" id="data-source" data-bind="text: latLongDataSource" style="word-wrap: break-word"></span>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="form-group">
            <label class="control-label" for="horizontal-datum" data-bind="text: $parent.labels.datum"></label>
            <span class="form-group-static" id="horizontal-datum" data-bind="text: horizontalReferenceDatum"></span>
        </div>
    </div>
</div>
<!-- /ko -->
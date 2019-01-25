<!-- ko with: address -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="operator-address" data-bind="text: $parent.labels.address1"></label>
            <span class="form-group-static" id="operator-address" data-bind="text: address1"></span>
        </div>
    </div>
</div>
<!-- ko if: $data.address2 != undefined -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="operator-address" data-bind="text: $parent.labels.address2"></label>
            <span class="form-group-static" id="operator-address" data-bind="text: address2"></span>
        </div>
    </div>
</div>
<!-- /ko -->
<div class="row">
    <div class="col-sm-5">
        <div class="form-group">
            <label class="control-label" for="operator-city" data-bind="text: $parent.labels.city"></label>
            <span class="form-group-static" id="operator-city" data-bind="text: city"></span>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="form-group">
            <label class="control-label" for="operator-state" data-bind="text: $parent.labels.state"></label>
            <span class="form-group-static" id="operator-state" data-bind="text: state"></span>
        </div>
    </div>
    <div class="col-sm-3">
        <div class="form-group">
            <label class="control-label" for="operator-zip" data-bind="text: $parent.labels.zip"></label>
            <span class="form-group-static" id="operator-zip" data-bind="text: zip"></span>
        </div>
    </div>
</div>
<!-- ko ifnot: $parent.hideCounty -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="operator-county" data-bind="text: $parent.labels.county"></label>
            <span class="form-group-static" id="operator-county" data-bind="text: county"></span>
        </div>
    </div>
</div>
<!-- /ko -->
<!-- /ko -->
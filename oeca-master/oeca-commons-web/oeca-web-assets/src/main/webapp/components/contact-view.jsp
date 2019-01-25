<!-- ko with: contact -->
<div class="row">
    <div class="col-sm-12">
    	<div class="form-group">
    		<label class="control-label" for="contact-name" data-bind="text: $parent.labels.name"></label>
    		<span class="form-group-static" id="contact-name" data-bind="text: name"></span>
    	</div>

    </div>
</div>
<div class="row">
    <!-- ko if: $data.organization != undefined -->
    <div class="col-sm-7 form-group">
        <label class="control-label" for="contact-organization" data-bind="text: $parent.labels.organization"></label>
        <span class="form-control-static" id="contact-organization" data-bind="text: organization"
              style="word-wrap: break-word"></span>
    </div>
    <!-- /ko -->
    <!-- ko if: $data.title != undefined -->
    <div class="col-sm-7 form-group">
        <label class="control-label" for="poc-title" data-bind="text: $parent.labels.title"></label>
        <span id="poc-title" class="form-control-static" data-bind="text: title" ></span>
    </div>
    <!-- /ko -->
</div>
<!-- ko if: $data.address != undefined -->
<div class="row">
    <div class="col-sm-6 form-group">
        <label class="control-label" for="contact-address" data-bind="text: $parent.labels.address"></label>
        <span id="contact-address" class="form-control" data-bind="text: address"></span>
    </div>
</div>
<!-- /ko -->
<!-- ko if: $data.phone != undefined -->
<div class="row">
    <div class="col-sm-6 col-xs-12 form-group">
        <label class="control-label" for="phone" data-bind="text: $parent.labels.phone"></label>
        <span id="phone" class="form-control-static" data-bind="text: $parent.fullPhone"></span>
    </div>
</div>
<!-- /ko -->
<!-- ko if: $data.email != undefined -->
<div class="row">
    <div class="col-sm-12 form-group">
        <label class="control-label" for="email" data-bind="text: $parent.labels.email"></label>
        <span id="email" class="form-control-static" data-bind="text: email"></span>
    </div>
</div>
<!-- /ko -->
<!-- ko if: $data.ein != undefined -->
<div class="row">
    <div class="col-sm-12 form-group">
        <label class="control-label" for="ein" data-bind="text: $parent.labels.ein"></label>
        <span id="ein" class="form-control-static" data-bind="text: ein"></span>
    </div>
</div>
<!-- /ko -->
<!-- /ko -->
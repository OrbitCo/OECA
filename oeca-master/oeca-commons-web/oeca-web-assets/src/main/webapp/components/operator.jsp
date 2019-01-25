<div class="h4" data-bind="text: labels.operatorHeading"></div>
<div class="row">
    <div class="col-sm-6 form-group">
        <label class="control-label" for="operator-name" data-bind="text: labels.operatorName"></label>
        <!-- ko ifnot: lockName -->
        <input id="operator-name" class="form-control" type="text" maxlength="80" data-bind="value: operatorName"/>
        <!-- /ko -->
        <!-- ko if: lockName -->
        <div class="input-group">
            <input id="operator-name" class="form-control" type="text" data-bind="value: operatorName" disabled/>
            <div class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></div>
        </div>
        <a href="JavaScript:oeca.common.notifications.operatorNameLocked();">Why can't I edit this field?</a>
        <!-- /ko -->
    </div>
</div>
<hr>
<div class="h4" data-bind="text: labels.addressHeading"></div>
<address-info params="address: {
		                        address1: operatorAddress.address1,
		                        address2: operatorAddress.address2,
		                        city: operatorAddress.city,
		                        state: operatorAddress.stateCode,
		                        zip: operatorAddress.zipCode,
		                        county: operatorAddress.county
							}, titles: labels.address, hideCounty: hideCounty"></address-info>
<hr>
<div class="h4" data-bind="text: labels.contactHeading"></div>
<contact-info params="contact: {
								firstName: operatorPointOfContact.firstName,
								middleInitial: operatorPointOfContact.middleInitial,
								lastName: operatorPointOfContact.lastName,
								title: operatorPointOfContact.title,
								phone: operatorPointOfContact.phone,
								phoneExtension: operatorPointOfContact.phoneExtension,
								email: operatorPointOfContact.email
							}, titles: labels.contact"></contact-info>
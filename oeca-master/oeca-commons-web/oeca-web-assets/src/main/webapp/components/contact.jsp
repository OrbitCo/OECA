<!-- ko with: contact -->
	<div class="row">
		<div class="col-sm-5 form-group">
			<label class="control-label" for="first-name" data-bind="text: $parent.labels.firstName"></label>
			<input id="first-name" class="form-control" type="text" maxlength="30" data-bind="value: firstName" />
		</div>
		<div class="col-sm-2 form-group">
			<label class="control-label" for="middle-initial" data-bind="text: $parent.labels.middleInitial"></label>
			<input id="middle-initial" class="form-control" type="text" maxlength="1" data-bind="value: middleInitial" />
		</div>
		<div class="col-sm-5 form-group">
			<label class="control-label" for="last-name" data-bind="text: $parent.labels.lastName"></label>
			<input id="last-name" class="form-control" type="text" maxlength="30" data-bind="value: lastName" />
		</div>
	</div>
	<div class="row">
		<!-- ko if: $data.organization != undefined -->
			<div class="col-sm-7 form-group">
				<label class="control-label" for="contact-organization" data-bind="text: $parent.labels.organization"></label>
				<input id="contact-organization" class="form-control" type="text" maxlength="100" data-bind="value: organization" />
			</div>
		<!-- /ko -->
		<!-- ko if: $data.title != undefined -->
			<div class="col-sm-7 form-group">
				<label class="control-label" for="poc-title" data-bind="text: $parent.labels.title"></label>
				<input id="poc-title" class="form-control" type="text" maxlength="40" data-bind="value: title" />
			</div>
		<!-- /ko -->
	</div>
	<!-- ko if: $data.address != undefined -->
	<div class="row">
	    <div class="col-sm-7 form-group">
	        <label class="control-label" for="contact-address" data-bind="text: $parent.labels.address"></label>
	        <input type="text" id="contact-address" class="form-control" maxlength="50" data-bind="value: address"/>
	    </div>
	</div>
	<!-- /ko -->
	<!-- ko if: $data.phone != undefined -->
	<div class="row">
		<div class="col-sm-3 col-xs-7 form-group">
			<label class="control-label" for="phone" data-bind="text: $parent.labels.phone"></label>
			<input id="phone" class="form-control" type="text" data-bind="maskedPhone: phone" />
		</div>
		<div class="col-sm-2 col-xs-5 form-group">
			<label class="control-label" for="phone-ext" data-bind="text: $parent.labels.phoneExt"></label>
			<input id="phone-ext" class="form-control" type="text" maxlength="4" data-bind="maskedPhoneExtension: phoneExtension"/>
		</div>
	</div>
	<!-- /ko -->
	<!-- ko if: $data.email != undefined -->
	<div class="row">
		<div class="col-sm-12 form-group">
			<label class="control-label" for="email" data-bind="text: $parent.labels.email"></label>
			<input id="email" class="form-control" type="text" maxlength="100" data-bind="value: email" />
		</div>
	</div>
	<!-- /ko -->
	<!-- ko if: $data.verifyEmail != undefined -->
	    <div class="row">
            <div class="col-sm-12 form-group">
                <label class="control-label" for="verify-email" data-bind="text: $parent.labels.verifyEmail"></label>
                <input id="verify-email" class="form-control" type="text" data-bind="value: verifyEmail" />
            </div>
        </div>
	<!-- /ko -->
	<!-- ko if: $data.ein != undefined -->
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label" for="ein" data-bind="text: $parent.labels.ein"></label>
				<input id="ein" class="form-control" type="text" data-bind="value: ein" maxlength="10"/>
			</div>
		</div>
	<!-- /ko -->
<!-- /ko -->
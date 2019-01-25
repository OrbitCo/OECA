<!-- ko with: siteInformation -->
<div class="row">
    <div class="col-xs-12">
        <div class="form-group cor-underline-group">
            <label class="control-label" for="project-site-name" data-bind="text: $parent.labels.siteName"></label>
            <span id="project-site-name" data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field:  siteName
                                }
                            }"></span>
        </div>
    </div>
</div>
<h5 data-bind="text: $parent.labels.addressHeading"></h5>
<div class="row">
    <div class="col-xs-12">
        <span data-bind="template: {
                    name: 'cor-address',
                    data: siteAddress
                }"></span>
    </div>
</div>
<div class="row">
    <div class="col-xs-12">
        <span data-bind="template: {
                    name: 'cor-location',
                    data: siteLocation
                }"></span>
    </div>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="form-group">
            <label class="control-label" for="indian-significance">Is your project located on a property of
                religious or cultural significance to an Indian tribe?</label>
            <span id="indian-significance" data-bind="template: {
                            name: 'yes-no-boxes',
                            data: {
                                field: siteIndianCountry
                            }
                         }"></span>
        </div>
    </div>
</div>
<div class="row subquestion" data-bind="slideVisible: siteIndianCountry">
    <div class="col-xs-12">
        <div class="form-group cor-underline-group">
            <label class="control-label" for="indian-significance-tribe">Indicate which tribe this land is
                of religious or cultural significance to.</label>
            <span id="indian-significance-tribe" data-bind="template: {
                            name: 'underlined-field',
                            data: {
                                field: siteIndianCountryTribe
                            }
                        }"></span>
        </div>
    </div>
</div>
<!-- /ko -->
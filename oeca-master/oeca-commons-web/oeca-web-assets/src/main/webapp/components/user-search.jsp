<h3>Find User</h3>
<div data-bind="if: showSearch()">
    <p>Search for a user by entering information in one of the fields below and clicking Search.</p>
    <span class="validationMessage" data-bind="validationMessage: searchCriteria"></span>
    <!-- ko with: searchCriteria -->
    <div class="row">
        <div class="col-sm-3 form-group">
            <label class="control-label" for="user-id">User ID</label>
            <input id="user-id" class="form-control" type="text" data-bind="value: userId" />
        </div>
        <div class="col-sm-3 form-group">
            <label class="control-label" for="first-name">First Name</label>
            <input id="first-name" class="form-control" type="text" data-bind="value: firstName" />
        </div>
        <div class="col-sm-2 form-group">
            <label class="control-label" for="middle-initial">Middle Initial</label>
            <input id="middle-initial" class="form-control" type="text" maxlength="1" data-bind="value: middleInitial" />
        </div>
        <div class="col-sm-4 form-group">
            <label class="control-label" for="last-name">Last Name</label>
            <input id="last-name" class="form-control" type="text" data-bind="value: lastName" />
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6 form-group">
            <label class="control-label" for="contact-organization">Organization</label>
            <input id="contact-organization" class="form-control" type="text" data-bind="value: organization" />
        </div>
        <div class="col-sm-6 form-group">
            <label class="control-label" for="email">Email</label>
            <input id="email" class="form-control" type="text" data-bind="value: email" />
        </div>
    </div>
    <div class="row">
        <div class="col-sm-7 form-group">
            <label class="control-label" for="contact-address">Address</label>
            <input type="text" id="contact-address" class="form-control" data-bind="value: address"/>
        </div>
    </div>
    <!-- /ko -->
    <div>
        <button class="btn btn-primary" data-bind="click: function(){search()}">Search</button>
    </div>
</div>
<div data-bind="if: loadResults, visible: showResults()">
    <p>Select a user from the list below:</p>
    <table class="table table-bordered table-condensed dataTable responsive no-wrap word-break" style="width: 100%"
           data-bind="attr: {id: id + '-search-results'},
           datatable: dtConfig()">
        <thead>
        <tr>
            <th>User ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Organization</th>
            <th>Role</th>
            <th>Address</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
    <br style="clear: both"/>
    <p>Can't find the user you're looking for?
        <a href="JavaScript:" data-bind="click: showSearch">Return to User Search</a>
    </p>
    <!-- ko if: inviteCallback != undefined -->
    <p>or <a href="JavaScript:" data-bind="click: inviteCallback">Invite a User to Register</a>.
    </p>
    <!-- /ko -->
    <!-- ko if: helpText -->
    <p data-bind="template: helpText.results"></p>
    <!-- /ko -->
</div>

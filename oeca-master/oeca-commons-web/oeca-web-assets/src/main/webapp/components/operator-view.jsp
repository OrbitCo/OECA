<h5 data-bind="text: labels.operatorHeading"></h5>
 <div class="row">
     <div class="col-xs-7">
         <div class="form-group cor-underline-group">
             <label class="control-label" for="operator-name" data-bind="text: labels.operatorName"></label>
             <span id="operator-name" data-bind="template: {
                         name: 'underlined-field',
                         data: {
                             field: operatorName
                         }
                     }"></span>
         </div>
     </div>
 </div>
 <h5 data-bind="text: labels.addressHeading"></h5>
 <span data-bind="template: {
             name: 'cor-address',
             data: operatorAddress
         }"></span>
 <h5 data-bind="text: labels.contactHeading"></h5>
 <span data-bind="template: {
                                 name: 'cor-contact',
                                 data: {
                                     firstName: operatorPointOfContact.firstName,
                                     middleInitial: operatorPointOfContact.middleInitial,
                                     lastName: operatorPointOfContact.lastName,
                                     title: operatorPointOfContact.title,
                                     phone: operatorPointOfContact.phone,
                                     phoneExtension: operatorPointOfContact.phoneExtension,
                                     email: operatorPointOfContact.email
                                 }
                             }"></span>

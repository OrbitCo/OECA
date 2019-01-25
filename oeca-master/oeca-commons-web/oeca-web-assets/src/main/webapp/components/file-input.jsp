<%--
	params:
		id: unique id for the input id and label for attributes. Should be a unique id.
		label: optional text to name the file button.  defaults to 'Upload a File'
--%>
<div>
    <fieldset data-bind="disable: uploader().locked">
        <div data-bind="fileDrag: uploader">
            <input type="file" multiple data-bind="fileInput: uploader, customFileInput: {}, attr: {'id': id}">
        </div>
    </fieldset>
    <!-- ko if: uploader().filesUploading().length > 0 -->
    <div class="h4">Uploading Files</div>
    <ul class="list-group" data-bind="foreach: uploader().filesUploading">
        <li class="list-group-item" data-bind="css: {
														'list-group-item-danger': status() == 'error',
														'list-group-item-info': status() == 'pending',
														'list-group-item-success': status() == 'uploaded'
													}">
				<span class="fa" data-bind="css: {
												'fa-spin fa-spinner': status() == 'pending',
												'fa-exclamation-triangle': status() == 'error',
												'fa-check': status() == 'uploaded'
											}"></span>
            <span data-bind="text: name"></span>
            <span data-bind="if: status() == 'error'">(<span data-bind="text: errorMessage"></span>)</span>
        </li>
    </ul>
    <!-- /ko-->
</div>
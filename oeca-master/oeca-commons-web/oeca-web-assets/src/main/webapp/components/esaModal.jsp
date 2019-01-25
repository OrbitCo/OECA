<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<stripes:layout-definition>

  <div class="modal-dialog modal-lg" >
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close"
                data-dismiss="modal">
          <span aria-hidden="true">&times;</span>
          <span class="sr-only">Close</span>
        </button>
        <h4>Electronic Signature Agreement</h4>
        <input type="button" id="printEsa" class="btn btn-primary" data-bind="click: $root.printEsa" value="Print">
      </div>
      <div class="modal-body">
        <div data-bind="html: $root.esaHtml"></div>
      </div>
    </div>
  </div>
</stripes:layout-definition>

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
        <h4>CDX Terms and Conditions</h4>
      </div>
      <div class="modal-body">
        <div style="width:100%; max-height:500px; text-align:left; overflow: auto">
          <p>The access and use of CDX Registration for the electronic submittal of environmental information require the creation of a user ID and password that
            I must maintain and keep confidential. I will review the following steps concerning the creation and maintenance of a user ID and password.</p>
          <h4><b>Warning Notice</b></h4>
          <p>In proceeding and accessing U.S. Government information and information systems, you acknowledge that you fully understand and consent to all of the following:</p>
          <ol type="1">
            <li>you are accessing U.S. Government information and information systems that are provided for official U.S. Government purposes only;</li>
            <li>unauthorized access to or unauthorized use of U.S. Government information or information systems is subject to criminal, civil, administrative, or other lawful action;</li>
            <li>the term U.S. Government information system includes systems operated on behalf of the U.S. Government;</li>
            <li>you have no reasonable expectation of privacy regarding any communications or information used, transmitted, or stored on U.S. Government information systems;</li>
            <li>at any time, the U.S. Government may for any lawful government purpose, without notice, monitor, intercept, search,
            and seize any authorized or unauthorized communication to or from U.S. Government information systems or information used or stored on U.S. Government information systems;</li>
            <li>at any time, the U.S. Government may for any lawful government purpose, search and seize any authorized or unauthorized device,
            to include non-U.S. Government owned devices, that stores U.S. Government information;</li>
            <li>any communications or information used, transmitted, or stored on U.S. Government information systems may be used
            or disclosed for any lawful government purpose, including but not limited to, administrative purposes, penetration testing,
            communication security monitoring, personnel misconduct measures, law enforcement, and counterintelligence inquiries; and</li>
            <li>you may not process or store classified national security information on this computer system.</li>
            </ol>
          <h4><b>Privacy Statement</b></h4>
          <p>EPA will use the personal identifying information which you provide for the expressed purpose of registration
            to the Central Data Exchange site and for updating and correcting information in internal EPA databases as necessary.
            The Agency will not make this information available for other purposes unless required by law.
            EPA does not sell or otherwise transfer personal information to an outside third party.
            <a href="https://www.gpo.gov/fdsys/pkg/FR-2002-03-18/html/02-6486.htm" target="_blank">Federal Register: March 18, 2002 (Volume 67, Number 52), Page 12010-12013</a>.
          </p>

          <h4><b>Choosing a CDX Password</b></h4>
          <p>For CDX registration purposes, I agree to select a password which will not be easily guessed (e.g., my name, my children's names, birthdays, etc.).
            Passwords must be a minimum of 8 alpha-numeric characters (no spaces or special characters) and contain at least 1 of each of the following:</p>
          <ul>
            <li>uppercase character</li>
            <li>lowercase character</li>
            <li>number</li>
          </ul>

          <p>Passwords may not begin with a number nor contain the word "password" nor contain your User Name.</p>
          <h4><b>Protecting my CDX Password</b></h4>
          <p>I agree to protect my CDX password.</p>
          <i style="color: red">I will not divulge my password to any other individual</i>; I will not store it in an unprotected location;
          and I will not allow it to be written into computer scripts to achieve automated login.
          <h4><b>Limited CDX Software Distribution</b></h4>
          <p>Any distribution of software provided by the Environmental Protection Agency's Central Data Exchange shall be handled according to any defined license practices.</p>
          <p>CDX provides tools which contains FIPS-validated RSA BSAFE Crypto-J which is classified under
            Export Commodity Classification Number (ECCN) 5D002 "Encryption Sofware" referenced under CCATS G059799.
            This product is eligible for license exception ENC under Sections 740.17 (A) and (B) (2) of the Export Administration Regulations (EAR).
            The exportation of this item classified by the Bureau of Industry and Security (BIS) as 5D002 "Unrestricted"
            to foreign subsidiaries of US companies is permitted under this license exception ("ENC "Encryption").
            This license exception does not apply to the embargoed nations of Cuba, Iran, North Korea, Sudan and Syria or any parties
            found on the various government denial lists including the Department of Commerce Denied Parties List.
            For additional information and guidance regarding your use of this product, please refer to the United States' standard regulations
            for encryption at <a href="http://www.access.gpo.gov/bis/ear/pdf/740.pdf" target="_blank">http://www.access.gpo.gov/bis/ear/pdf/740.pdf</a></p>
          <h4><b>Actions to take if my CDX Account has been Compromised</b></h4>
          <p>If I have determined that my CDX account has become compromised, I agree to contact the
            <a href="${actionBean.baseCdxUrl}/Contact" target="_blank">CDX Technical Support staff</a> at 888-890-1995 or (970) 494-5500 for International callers as soon as possible.</p>
          <h4><b>Terminating my CDX Account</b></h4>
          <p>I agree to notify CDX within ten working days if my duties change and I no longer need to interact with the CDX on behalf of my organization.
            I agree to make this notification via either the CDX web interface or by notifying the
            <a href="${actionBean.baseCdxUrl}/Contact" target="_blank">CDX Technical Support staff</a> at 888-890-1995 or
            (970) 494-5500 for International callers. This notification will allow CDX to deactivate
            my account and protect it from potential abuse by others.</p>
        </div>
      </div>
    </div>
  </div>
</stripes:layout-definition>

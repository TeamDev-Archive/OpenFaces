<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form>
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->

<%--
    <o:ajaxSettings onajaxend="printInfo('onajaxend works; event type: ' + event.type, 'output', true)"
                    onajaxstart="printInfo('onajaxstart works; event type: ' + event.type, 'output', true)"
                    onerror="printInfo('onerror works; event type: ' + event.type, 'output', true)"
                    onsessionexpired="printInfo('onsessionexpired works; event type: ' + event.type, 'output', true)">
      <f:facet name="progressMessage">
        <o:defaultProgressMessage imageUrl="/panther_eye.png" text="custom loading"
                                  style="background: red; border: 2px solid green;"/>
      </f:facet>
    </o:ajaxSettings>
    <div id="output"></div>
--%>

    <h:commandButton type="submit" value="Submit"/>
    <br/>
    <h:commandLink value="back" action="index"/>
    <br/>
    <o:suggestionField>
      <o:dropDownItem value="Red"/>
      <o:dropDownItem value="Yellow"/>
      <o:dropDownItem value="Blue"/>
    </o:suggestionField>
    <br/>
    <o:suggestionField listAlignment="right" style="width:150px;" listStyle="width:200px;">
      <o:dropDownItem value="Red"/>
      <o:dropDownItem value="Yellow"/>
      <o:dropDownItem value="Blue"/>
    </o:suggestionField>
    <br/>
    <o:suggestionField listItemStyle="color: Teal; font-size: 18px;"
                       listStyle="border: 1px dotted red;"
                       rolloverListItemStyle="color: orange; font: Comic Sans MS; background: LightGreen;"
                       rolloverListStyle="border: 3px dotted pink;"
                       rolloverStyle="border: 1px dotted darkblue;"
                       style="width: 230px;">
      <o:dropDownItem value="Red"/>
      <o:dropDownItem value="Yellow"/>
      <o:dropDownItem value="Blue"/>
    </o:suggestionField>

    <o:suggestionField listAlignment="right" style="width:150px;"
                       listStyle="width:200px;" suggestionMode="all" autoComplete="true">
      <o:dropDownItem value="Red"/>
      <o:dropDownItem value="Yellow"/>
      <o:dropDownItem value="Blue"/>
      <o:dropDownItem value="Green"/>
      <o:dropDownItem value="White"/>
      <o:dropDownItem value="Black"/>
      <o:dropDownItem value="Gray"/>
      <o:dropDownItem value="Tan"/>
      <o:dropDownItem value="Fuchsia"/>
      <o:dropDownItem value="Magenta"/>
      <o:dropDownItem value="Orange"/>
      <o:dropDownItem value="Teal"/>
    </o:suggestionField>

    <o:suggestionField listAlignment="right" style="width:150px;" listStyle="width:200px;"
                       suggestionMode="stringStart" autoComplete="true" suggestionDelay="0">
      <o:dropDownItem value="Red"/>
      <o:dropDownItem value="Yellow"/>
      <o:dropDownItem value="Blue"/>
      <o:dropDownItem value="Green"/>
      <o:dropDownItem value="White"/>
      <o:dropDownItem value="Black"/>
      <o:dropDownItem value="Gray"/>
      <o:dropDownItem value="Tan"/>
      <o:dropDownItem value="Fuchsia"/>
      <o:dropDownItem value="Magenta"/>
      <o:dropDownItem value="Orange"/>
      <o:dropDownItem value="Teal"/>
    </o:suggestionField>

    <o:suggestionField suggestionMode="custom" converter="#{DropDownBean.colorConverter}">
      <o:dropDownItems value="#{DropDownBean.colors}"/>
    </o:suggestionField>

    <h3>with &lt;o:loadBundle&gt;</h3>
    <o:loadBundle basename="org.openfaces.portlets.TestLoadBundle" var="bundle"/>
    <o:suggestionField id="plants"
                       suggestionMode="custom"
                       autoComplete="true"
                       customValueAllowed="false"
                       listItemStyle="white-space:nowrap;">
      <o:dropDownItems value="#{DropDownBean.suggestedPlants}"/>
      <h:outputText value="#{bundle['week']}"/>
    </o:suggestionField>


  </h:form>
</f:view>

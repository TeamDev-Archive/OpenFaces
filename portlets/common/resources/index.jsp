<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form>

    <!--<link rel='stylesheet' type='text/css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->

    <style type="text/css"><!--
    .component_names {
      vertical-align: top;
      font-weight: bold;
    }

    --></style>
    <h:commandLink value="Calendar" action="calendar"/>
    <br/>
    <h:commandLink value="Chart" action="chart"/>
    <br/>
    <h:commandLink value="Confirmation" action="confirmation"/>
    <br/>
    <h:commandLink value="DataTable" action="datatable"/>
    <br/>
    <h:commandLink value="DateChooser" action="datechooser"/>
    <br/>
    <h:commandLink value="DropDownField" action="dropdownfield"/>
    <br/>
    <h:commandLink value="DynamicImage" action="dynamicimage"/>
    <br/>
    <h:commandLink value="FoldingPanel" action="foldingpanel"/>
    <br/>
    <h:commandLink value="HintLabel" action="hintlabel"/>
    <br/>
    <h:commandLink value="SuggestionField" action="suggestionfield"/>
    <br/>
    <h:commandLink value="Miscellaneous" action="miscellaneous"/>
    <br/>
    <h:commandLink value="PopupLayer" action="popuplayer"/>
    <br/>
    <h:commandLink value="TabbedPane" action="tabbedpane"/>
    <br/>
    <h:commandLink value="TabSet" action="tabset"/>
    <br/>
    <h:commandLink value="TreeTable" action="treetable"/>
    <br/>
    <h:commandLink value="TwoListSelection" action="twolistselection"/>
    <br/>
    <h:commandLink value="Validation" action="validation"/>
    <br/>
    <h:commandButton value="submit" type="submit"/>

  </h:form>
</f:view>
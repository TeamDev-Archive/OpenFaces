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

    <h:commandButton action="sbmt" value="Submit"/>
    <h:commandLink value="back" action="index"/>
    <o:tabbedPane loadingMode="ajax"
                  tabAlignment="bottomOrRight"
                  tabPlacement="left"
                  style="height:250px;">
      <o:tabbedPaneItem>
        <f:facet name="tab">
          <h:outputText value="Tab1"/>
        </f:facet>
        <h:inputText value="Tab1 inner text" required="true"/>
        <h:commandButton value="submit"/>
      </o:tabbedPaneItem>
      <o:tabbedPaneItem>
        <f:facet name="tab">
          <h:outputText value="Tab2"/>
        </f:facet>
        <h:outputText value="Tab2 inner text"/>
        <o:calendar/>
      </o:tabbedPaneItem>
    </o:tabbedPane>
    <o:tabbedPane loadingMode="server"
                  containerStyle="background: beige;"
                  rolloverContainerStyle="border: 2px dotted blue;"
                  rolloverSelectedTabStyle="background: LightGreen; font-weight: bold;"
                  rolloverStyle="border: 2px dashed lightGreen;"
                  rolloverTabStyle="font-weight: bold;"
                  selectedTabStyle="background: DarkGreen; font-weight: bold; color: white;"
                  tabEmptySpaceStyle="background: LightBlue;"
                  tabStyle="color: red;"
                  style="height: 300px; width: 200px;">
      <o:tabbedPaneItem>
        <f:facet name="tab">
          <h:outputText value="Tab1"/>
        </f:facet>
        <h:outputText value="Tab1 inner text"/>
      </o:tabbedPaneItem>
      <o:tabbedPaneItem>
        <f:facet name="tab">
          <h:outputText value="Tab2"/>
        </f:facet>
        <h:outputText value="Tab2 inner text"/>
      </o:tabbedPaneItem>
    </o:tabbedPane>
    <br>

    <h3>with &lt;o:loadBundle&gt;</h3>
    <o:loadBundle basename="org.openfaces.portlets.TestLoadBundle" var="bundle"/>
    <o:tabbedPane id="tabbedPaneID"
                  containerStyle="padding: 10px 10px 10px 10px;">
      <o:tabbedPaneItem>
        <f:facet name="tab">
          <h:outputText value="#{bundle.today}" id="firstTabID"/>
        </f:facet>
        <h:outputText value="#{bundle['week']}"/>
      </o:tabbedPaneItem>
      <o:tabbedPaneItem>
        <f:facet name="tab">
          <h:outputText value="#{bundle['week']}" id="secondTabID"/>
        </f:facet>
        <h:outputText value="#{bundle.today}"/>
      </o:tabbedPaneItem>
    </o:tabbedPane>


  </h:form>
</f:view>

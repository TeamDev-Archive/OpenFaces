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
    <br/>
    <table>
      <tr>
        <td>
          Styles
          LoadingMode="server"
        </td>
        <td>
          <o:foldingPanel loadingMode="server" expanded="false"
                          captionStyle="background: MediumSlateBlue; font-weight: bold; font-size: 18px;"
                          contentStyle="background: LightSkyBlue; color: DarkBlue; font-size: 20px; padding: 20px 20px 20px 20px;"
                          style="border: 2px dashed LawnGreen;">
            <f:facet name="caption">
              <h:outputText value="FoldingPanel caption"/>
            </f:facet>
            <h:outputText value="Just some text inside folding panel"/>
          </o:foldingPanel>
        </td>
      </tr>
      <tr>
        <td>
          loadingMode="ajaxLazy"
          expanded="false"
          foldingDirection="up"
        </td>
        <td>
          <o:foldingPanel loadingMode="ajaxLazy"
                          expanded="false"
                          foldingDirection="up">
            <f:facet name="caption">
              <h:outputLabel value="Folding panel caption"/>
            </f:facet>
            <o:captionArea>
              <o:expansionToggleButton hint="hint"/>
            </o:captionArea>
            <h:panelGroup>
              <h:outputText value="Folding panel's content"/>
              <o:calendar/>
              <h:commandButton value="submit"/>
              <h:inputText required="true"/>
            </h:panelGroup>
          </o:foldingPanel>
        </td>
      </tr>
      <tr>
        <td>
          loadingMode="client"
          expanded="false"
          onstatechange="alert('!');"
        </td>
        <td>
          <o:foldingPanel loadingMode="client"
                          expanded="false"
                          onstatechange="alert('!');">
            <f:facet name="caption">
              <h:outputText value="FoldingPanel caption"/>
            </f:facet>
            <h:outputText value="Just some text inside folding panel"/>
          </o:foldingPanel>
        </td>
      </tr>
    </table>
    <h3>with &lt;o:loadBundle&gt;</h3>
    <o:loadBundle basename="org.openfaces.portlets.TestLoadBundle" var="bundle"/>
    <o:foldingPanel id="calendarFoldingPanel"
                    expanded="false"
                    contentStyle="padding: 10px 10px 10px 10px;">
      <f:facet name="caption">
        <h:outputText value="#{bundle.today}" id="zero"/>
      </f:facet>
      <h:outputText value="#{bundle.today}" id="first"/>
      <h:outputText value="#{bundle['week']}" id="second"/>
    </o:foldingPanel>
  </h:form>
</f:view>

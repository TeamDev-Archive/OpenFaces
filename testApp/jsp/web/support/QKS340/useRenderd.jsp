<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j" %>

<html>
<head>
  <title></title>
</head>

<body>
<f:view>
  <table>
    <tr>
      <td valign="top" width="200px">
        <h:form>
          <a4j:commandLink action="#{QKS340.renderMain}" value="Main page" reRender="CONTENT"/>
          <br/>
          <a4j:commandLink action="#{QKS340.renderDataTable}" value="DataTable page" reRender="CONTENT"/>
          <br/>
          <a4j:commandLink action="#{QKS340.renderTreeTable}" value="TreeTable page" reRender="CONTENT"/>
          <br/>
        </h:form>
      </td>
      <td valign="top">
        <a4j:outputPanel id="CONTENT">
          <a4j:outputPanel rendered="#{QKS340.renderedMain}">
            <jsp:include page="main_page.jsp"/>
          </a4j:outputPanel>
          <a4j:outputPanel rendered="#{QKS340.renderedDataTable}">
            <h:form>
              <o:tabbedPane loadingMode="ajax"
                            tabPlacement="top"
                            tabAlignment="topOrLeft"
                            style="margin-top: 10px;"
                            containerStyle="padding: 15px 15px 15px 15px;">
                <o:tabbedPaneItem>
                  <f:facet name="tab">
                    <h:outputText value="Poppy" styleClass="tab_name"/>
                  </f:facet>
                  <o:calendar/>
                </o:tabbedPaneItem>
                <o:tabbedPaneItem>
                  <f:facet name="tab">
                    <h:outputText value="Tree stump" styleClass="tab_name"/>
                  </f:facet>
                  <o:dateChooser/>
                </o:tabbedPaneItem>
                <o:tabbedPaneItem>
                  <f:facet name="tab">
                    <h:outputText value="Lane" styleClass="tab_name"/>
                  </f:facet>
                  <o:dropDownField>
                    <o:dropDownItem value="red"/>
                    <o:dropDownItem value="blue"/>
                    <o:dropDownItem value="green"/>
                  </o:dropDownField>
                </o:tabbedPaneItem>
              </o:tabbedPane>
            </h:form>
          </a4j:outputPanel>
          <a4j:outputPanel rendered="#{QKS340.renderedTreeTable}">
            <jsp:include page="treeTable.jsp"/>
          </a4j:outputPanel>
        </a4j:outputPanel>
      </td>
    </tr>
  </table>
</f:view>

</body>
</html>
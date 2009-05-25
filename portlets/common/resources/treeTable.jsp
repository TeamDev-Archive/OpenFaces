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
<h1>Sorting and filtering - Ajax</h1>
<o:treeTable id="forumTreeTable_ajax" var="message"
             expansionState="#{ForumTreeTableBean.forumTreeTableExpansionState}" style="width: 100%;"
             columnIdVar="columnId"
             textStyle="font-family: verdana,Helvetica,sans-serif; font-size: 10pt;"
             sortColumnId="date">
  <o:dynamicTreeStructure nodeChildren="#{ForumTreeTableBean.nodeChildren}"/>
  <o:treeColumn id="subject" sortingExpression="#{message.subject}" filterExpression="#{message.subject}"
                filterKind="searchField" style="text-align: left;">
    <f:facet name="header">
      <h:outputText value="Subject"/>
    </f:facet>
    <h:outputText value="#{message.subject}"/>
  </o:treeColumn>
  <o:column id="author" sortingExpression="#{message.author}" filterExpression="#{message.author}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="From"/>
    </f:facet>
    <h:outputText value="#{message.author}"/>
  </o:column>
  <o:column id="date" sortingExpression="#{message.date}" filterExpression="#{ForumTreeTableBean.dateCategory}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="Sent"/>
    </f:facet>
    <h:outputText value="#{message.date}">
      <f:convertDateTime type="date" dateStyle="medium"/>
    </h:outputText>
  </o:column>
</o:treeTable>
<h1>Sorting and filtering - Server</h1>
<o:treeTable id="forumTreeTable_server" var="message"
             expansionState="allCollapsed" style="width: 100%;"
             columnIdVar="columnId"
             textStyle="font-family: verdana,Helvetica,sans-serif; font-size: 10pt;"
             sortColumnId="date"
             useAjax="false">
  <o:dynamicTreeStructure nodeChildren="#{ForumTreeTableBean.nodeChildren}"/>
  <o:treeColumn id="subject" sortingExpression="#{message.subject}" filterExpression="#{message.subject}"
                filterKind="searchField" style="text-align: left;">
    <f:facet name="header">
      <h:outputText value="Subject"/>
    </f:facet>
    <h:outputText value="#{message.subject}"/>
  </o:treeColumn>
  <o:column id="author" sortingExpression="#{message.author}" filterExpression="#{message.author}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="From"/>
    </f:facet>
    <h:outputText value="#{message.author}"/>
  </o:column>
  <o:column id="date" sortingExpression="#{message.date}" filterExpression="#{ForumTreeTableBean.dateCategory}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="Sent"/>
    </f:facet>
    <h:outputText value="#{message.date}">
      <f:convertDateTime type="date" dateStyle="medium"/>
    </h:outputText>
  </o:column>
</o:treeTable>
<h1>Single node selection</h1>
<o:treeTable id="single_selection" var="message"
             expansionState="allExpanded" style="width: 100%;"
             columnIdVar="columnId"
             textStyle="font-family: verdana,Helvetica,sans-serif; font-size: 10pt;"
             sortColumnId="date"
             useAjax="true">
  <o:dynamicTreeStructure nodeChildren="#{ForumTreeTableBean.nodeChildren}"/>
  <o:singleNodeSelection nodeData="#{ForumTreeTableBean.selectedNode}"/>
  <o:treeColumn id="subject" sortingExpression="#{message.subject}" filterExpression="#{message.subject}"
                filterKind="searchField" style="text-align: left;">
    <f:facet name="header">
      <h:outputText value="Subject"/>
    </f:facet>
    <h:outputText value="#{message.subject}"/>
  </o:treeColumn>
  <o:column id="author" sortingExpression="#{message.author}" filterExpression="#{message.author}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="From"/>
    </f:facet>
    <h:outputText value="#{message.author}"/>
  </o:column>
  <o:column id="date" sortingExpression="#{message.date}" filterExpression="#{ForumTreeTableBean.dateCategory}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="Sent"/>
    </f:facet>
    <h:outputText value="#{message.date}">
      <f:convertDateTime type="date" dateStyle="medium"/>
    </h:outputText>
  </o:column>
</o:treeTable>
<h1>Multiple selection</h1>
<o:treeTable id="multiple_selection" var="message"
             expansionState="levelsExpanded:0" style="width: 100%;"
             columnIdVar="columnId"
             textStyle="font-family: verdana,Helvetica,sans-serif; font-size: 10pt;"
             sortColumnId="date"
             useAjax="true">
  <o:multipleNodeSelection nodeDatas="#{ForumTreeTableBean.selectedNodes}"/>
  <o:dynamicTreeStructure nodeChildren="#{ForumTreeTableBean.nodeChildren}"/>
  <o:treeColumn id="subject" sortingExpression="#{message.subject}" filterExpression="#{message.subject}"
                filterKind="searchField" style="text-align: left;">
    <f:facet name="header">
      <h:outputText value="Subject"/>
    </f:facet>
    <h:outputText value="#{message.subject}"/>
  </o:treeColumn>
  <o:column id="author" sortingExpression="#{message.author}" filterExpression="#{message.author}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="From"/>
    </f:facet>
    <h:outputText value="#{message.author}"/>
  </o:column>
  <o:column id="date" sortingExpression="#{message.date}" filterExpression="#{ForumTreeTableBean.dateCategory}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="Sent"/>
    </f:facet>
    <h:outputText value="#{message.date}">
      <f:convertDateTime type="date" dateStyle="medium"/>
    </h:outputText>
  </o:column>
</o:treeTable>
<h1>Styles</h1>
<o:treeTable id="styles"
             var="colorStyle"
             filterAcceptedRowStyle="font: Comic Sans MS; font-size:24px;"
             filterSubsidiaryRowStyle="font: Comic Sans MS; font-size:10px;"
             bodyOddRowStyle="background: #F8FFA8;"
             bodyRowStyle="background: #D0FFFE;"
             bodySectionStyle="color: #13731E;"
             commonFooterRowStyle="background: #3333AF; border: 1px dashed yellow;"
             commonHeaderRowStyle="background: #C90EBE; color: white;"
             filterRowStyle="color: SlateBlue; font-weight: bold; background: NavajoWhite;"
             focusedStyle="border: 2px solid yellow;"
             footerRowStyle="background: Teal; border: 2px solid Salmon;"
             footerSectionStyle="font-weight: bold;"
             headerRowStyle="background: LightSlateGray; border: 3px solid red;"
             headerSectionStyle="font-weight: normal;"
             rolloverRowStyle="background: gray; color: white; font-weight: bold;"
             rolloverStyle="border: 3px solid black;"
             sortedColumnBodyStyle="font-weight: bold;"
             noDataRowStyle="color: darkgreen; font-weight: bold;"
             sortedColumnFooterStyle="background: Salmon; border: 2px solid Teal;"
             sortedColumnHeaderStyle="background:yellow;"
             sortedColumnStyle="background: IndianRed;"
             expansionState="allCollapsed">
  <o:singleNodeSelection style="color: blue;"/>
  <o:staticTreeStructure>
    <o:treeNode value="Colors">
      <o:treeNode value="Warm colors">
        <o:treeNode value="Red"/>
        <o:treeNode value="Yellow"/>
      </o:treeNode>
      <o:treeNode value="Cold colors">
        <o:treeNode value="Blue"/>
        <o:treeNode value="Purple"/>
      </o:treeNode>
    </o:treeNode>
  </o:staticTreeStructure>
  <o:treeColumn sortingExpression="#{colorStyle}">
    <f:facet name="header">
      <h:outputText value="Header 1"/>
    </f:facet>
    <h:outputText value="#{colorStyle}"/>
    <f:facet name="footer">
      <h:outputText value="Footer 1"/>
    </f:facet>
  </o:treeColumn>
  <o:treeColumn filterExpression="#{colorStyle}" filterKind="searchField">
    <f:facet name="header">
      <h:outputText value="Header 2"/>
    </f:facet>
    <h:outputText value="#{colorStyle}"/>
    <f:facet name="footer">
      <h:outputText value="Footer 1"/>
    </f:facet>
  </o:treeColumn>
  <f:facet name="footer">
    <h:outputText value="Common footer"/>
  </f:facet>
  <f:facet name="header">
    <h:outputText value="Common header"/>
  </f:facet>
</o:treeTable>
<h1>Separators</h1>
<o:treeTable id="separators" var="separators" expansionState="allCollapsed"
             horizontalGridLines="2px solid red"
             verticalGridLines="2px solid blue"
             commonHeaderSeparator="2px dotted green"
             commonFooterSeparator="2px dashed greenyellow"
             headerHorizSeparator="2px dotted gray"
             footerHorizSeparator="2px solid skyblue"
             headerVertSeparator="2px solid yellow"
             footerVertSeparator="2px solid black">
  <f:facet name="above">
    <h:outputText value="Above section style test"/>
  </f:facet>
  <f:facet name="header">
    <h:outputText value="Header section style test"/>
  </f:facet>
  <f:facet name="footer">
    <h:outputText value="Footer section style test"/>
  </f:facet>
  <f:facet name="below">
    <h:outputText value="Below section style test"/>
  </f:facet>
  <o:staticTreeStructure>
    <o:treeNode value="TopNode1">
      <o:treeNode value="subnode 1.1"/>
      <o:treeNode value="subnode 1.2"/>
      <o:treeNode value="subnode 1.3"/>
      <o:treeNode value="subnode 1.4"/>
      <o:treeNode value="subnode 1.5"/>
      <o:treeNode value="subnode 1.6"/>
    </o:treeNode>
    <o:treeNode value="TopNode2">
      <o:treeNode value="subnode 2.1"/>
      <o:treeNode value="subnode 2.2"/>
      <o:treeNode value="subnode 2.3"/>
    </o:treeNode>
    <o:treeNode value="TopNode3">
      <o:treeNode value="subnode 3.1"/>
      <o:treeNode value="subnode 3.2"/>
    </o:treeNode>
  </o:staticTreeStructure>
  <o:treeColumn>
    <h:outputText value="#{separators}"/>
  </o:treeColumn>
  <o:treeColumn>
    <h:outputText value="#{separators}"/>
  </o:treeColumn>
</o:treeTable>
<h3>with &lt;o:loadBundle&gt;</h3>
<o:loadBundle basename="org.openfaces.portlets.TestLoadBundle" var="bundle"/>
<o:treeTable id="forumTreeTable_ajax2" var="message"
             expansionState="#{ForumTreeTableBean.forumTreeTableExpansionState}" style="width: 100%;"
             columnIdVar="columnId"
             textStyle="font-family: verdana,Helvetica,sans-serif; font-size: 10pt;"
             sortColumnId="date">
  <o:dynamicTreeStructure nodeChildren="#{ForumTreeTableBean.nodeChildren}"/>
  <o:treeColumn id="subject" sortingExpression="#{message.subject}" filterExpression="#{message.subject}"
                filterKind="searchField" style="text-align: left;">
    <f:facet name="header">
      <h:outputText value="Subject"/>
    </f:facet>
    <h:outputText value="#{bundle['week']}"/>
  </o:treeColumn>
  <o:column id="author" sortingExpression="#{message.author}" filterExpression="#{message.author}"
            filterKind="dropDownField" style="width: 120px; text-align: left;">
    <f:facet name="header">
      <h:outputText value="From"/>
    </f:facet>
    <h:outputText value="#{bundle.today}"/>
  </o:column>
</o:treeTable>

</h:form>
</f:view>

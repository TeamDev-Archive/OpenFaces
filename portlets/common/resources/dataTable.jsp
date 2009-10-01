<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
<h:form id="form">
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

<h1>Pagination, fitlering and sorting (разбивка на страницы, фильтрация и сортировка). Use Ajax="true"</h1>
<o:dataTable rowKey="#{person.name}" id="filter_ajax" pageSize="5" var="person" value="#{peopleList.person}"
             useAjax="true" sortAscending="false" sortColumnId="name">
  <o:column id="name"
            sortingExpression="#{person.name}"
            sortingComparator="caseInsensitiveText">
    <f:facet name="header">
      <h:outputText value="Name/Имя/Ім'я"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:inputTextFilter expression="#{person.name}"/>
    </f:facet>
    <h:outputText value="#{person.name}"/>
  </o:column>
  <o:column id="profession"
            sortingExpression="#{person.profession}">
    <f:facet name="header">
      <h:outputText value="Profession/Профессия/Професія"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:dropDownFieldFilter expression="#{person.profession}"/>
    </f:facet>
    <h:outputText value="#{person.profession}"/>
  </o:column>
  <o:column>
    <f:facet name="header">
      <h:outputText value="Hobby/Хобби/Хобі"/>
    </f:facet>
    <h:outputText value="#{person.hobby}"/>
  </o:column>
  <f:facet name="below">
    <o:dataTablePaginator
            pageNumberPrefix="Страница"
            pageCountPreposition="из"
            previousText="Предыдущая"
            nextText="Следующая"
            firstText="Первая"
            lastText="Последняя"/>
  </f:facet>
</o:dataTable>
<br/>

<h1>Paging, fitlering and sorting. Use Ajax="false"</h1>
<o:dataTable rowKey="#{person.name}" id="filter_server" pageSize="5" var="person" value="#{peopleList.person}"
             useAjax="false">
  <o:column id="name"
            sortingExpression="#{person.name}"
            sortingComparator="caseInsensitiveText">
    <f:facet name="header">
      <h:outputText value="Name"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:inputTextFilter expression="#{person.name}" value="#{peopleList.filterValue}"/>
    </f:facet>
    <h:outputText value="#{person.name}"/>
  </o:column>
  <o:column id="profession"
            sortingExpression="#{person.profession}">
    <f:facet name="header">
      <h:outputText value="Profession"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:dropDownFieldFilter expression="#{person.profession}"/>
    </f:facet>
    <h:outputText value="#{person.profession}"/>
  </o:column>
  <o:column>
    <f:facet name="header">
      <h:outputText value="Hobby"/>
    </f:facet>
    <h:outputText value="#{person.hobby}"/>
  </o:column>
  <f:facet name="below">
    <o:dataTablePaginator/>
  </f:facet>
</o:dataTable>
<br/>

<h1>Multiple selection</h1>
<o:dataTable rowKey="#{person.name}" id="mult_selection" pageSize="5" var="person" value="#{peopleList.person}"
             useAjax="true">
  <o:multipleRowSelection rowDatas="#{peopleList.selectedRows}" style="background: #a4aec5; color: white"/>
  <o:column id="name"
            sortingExpression="#{person.name}"
            sortingComparator="caseInsensitiveText">
    <f:facet name="header">
      <h:outputText value="Name"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:inputTextFilter expression="#{person.name}"/>
    </f:facet>
    <h:outputText value="#{person.name}"/>
  </o:column>
  <o:column id="profession"
            sortingExpression="#{person.profession}">
    <f:facet name="header">
      <h:outputText value="Profession"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:comboBoxFilter expression="#{person.profession}"/>
    </f:facet>
    <h:outputText value="#{person.profession}"/>
  </o:column>
  <o:column>
    <f:facet name="header">
      <h:outputText value="Hobby"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:dropDownFieldFilter expression="#{person.hobby}"/>
    </f:facet>
    <h:outputText value="#{person.hobby}"/>
  </o:column>
  <f:facet name="below">
    <o:dataTablePaginator/>
  </f:facet>
</o:dataTable>
<br/>

<h1>Single row selection</h1>
<o:dataTable rowKey="#{person.name}" id="single_selection" pageSize="5" var="person" value="#{peopleList.person}"
             useAjax="true">
  <o:singleRowSelection rowData="#{peopleList.selectedRow1}" style="background: #a4aec5; color: white"/>
  <o:column id="name"
            sortingExpression="#{person.name}"
            sortingComparator="caseInsensitiveText">
    <f:facet name="header">
      <h:outputText value="Name"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:inputTextFilter expression="#{person.name}"/>
    </f:facet>
    <h:outputText value="#{person.name}"/>
  </o:column>
  <o:column id="profession"
            sortingExpression="#{person.profession}">
    <f:facet name="header">
      <h:outputText value="Profession"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:dropDownFieldFilter expression="#{person.profession}"/>
    </f:facet>
    <h:outputText value="#{person.profession}"/>
  </o:column>
  <o:column>
    <f:facet name="header">
      <h:outputText value="Hobby"/>
    </f:facet>
    <h:outputText value="#{person.hobby}"/>
  </o:column>
  <f:facet name="below">
    <o:dataTablePaginator/>
  </f:facet>
</o:dataTable>
<br/>

<h1>Styles</h1>
<o:dataTable rowKey="#{person.name}"
             id="syles"
             pageSize="5"
             var="person"
             value="#{peopleList.person}"
             useAjax="true"
             bodyOddRowStyle="background: #F8FFA8;"
             bodyRowStyle="background: #D0FFFE;"
             bodySectionStyle="color: #13731E;"
             commonFooterRowStyle="background: #3333AF; border: 1px dashed yellow;"
             commonHeaderRowStyle="background: #C90EBE; color: white;"
             filterRowStyle="color: SlateBlue; font-weight: bold; background: NavajoWhite;"
             focusedStyle="border: 2px solid yellow ! important;"
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
             sortedColumnStyle="background: IndianRed;">
  <f:facet name="header">
    <h:outputText value="It is a common header"/>
  </f:facet>
  <o:singleRowSelection rowData="#{peopleList.selectedRow2}" style="background: #a4aec5; color: white"/>
  <o:column id="name"
            sortingExpression="#{person.name}"
            sortingComparator="caseInsensitiveText">
    <f:facet name="header">
      <h:outputText value="Name"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:inputTextFilter expression="#{person.name}"/>
    </f:facet>
    <h:outputText value="#{person.name}"/>
    <f:facet name="footer">
      <h:outputText value="Name"/>
    </f:facet>
  </o:column>
  <o:column id="profession"
            sortingExpression="#{person.profession}">
    <f:facet name="header">
      <h:outputText value="Profession"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:dropDownFieldFilter expression="#{person.profession}"/>
    </f:facet>
    <h:outputText value="#{person.profession}"/>
    <f:facet name="footer">
      <h:outputText value="Profession"/>
    </f:facet>
  </o:column>
  <o:column>
    <f:facet name="header">
      <h:outputText value="Hobby"/>
    </f:facet>
    <h:outputText value="#{person.hobby}"/>
    <f:facet name="footer">
      <h:outputText value="Hobby"/>
    </f:facet>
  </o:column>
  <f:facet name="footer">
    <h:outputText value="It is a common footer"/>
  </f:facet>
  <f:facet name="below">
    <o:dataTablePaginator/>
  </f:facet>
</o:dataTable>

<h1>with LoadBundle</h1>
<o:loadBundle basename="org.openfaces.portlets.TestLoadBundle" var="bundle"/>
<o:dataTable rowKey="#{person.name}"
             pageSize="5"
             var="person"
             value="#{peopleList.person}">
  <f:facet name="header">
    <h:outputText value="It is a common header"/>
  </f:facet>
  <o:singleRowSelection rowData="#{peopleList.selectedRow2}" style="background: #a4aec5; color: white"/>
  <o:column id="name"
            sortingExpression="#{person.name}"
            sortingComparator="caseInsensitiveText">
    <f:facet name="header">
      <h:outputText value="Name"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:inputTextFilter expression="#{person.name}"/>
    </f:facet>
    <h:outputText value="#{bundle.today}"/>
    <f:facet name="footer">
      <h:outputText value="Name"/>
    </f:facet>
  </o:column>
  <o:column id="profession"
            sortingExpression="#{person.profession}">
    <f:facet name="header">
      <h:outputText value="Profession"/>
    </f:facet>
    <f:facet name="subHeader">
      <o:dropDownFieldFilter expression="#{person.profession}"/>
    </f:facet>
    <h:outputText value="#{bundle['week']}"/>
    <f:facet name="footer">
      <h:outputText value="Profession"/>
    </f:facet>
  </o:column>
  <o:column>
    <f:facet name="header">
      <h:outputText value="Hobby"/>
    </f:facet>
    <h:outputText value="#{person.hobby}"/>
    <f:facet name="footer">
      <h:outputText value="Hobby"/>
    </f:facet>
  </o:column>
  <f:facet name="footer">
    <h:outputText value="It is a common footer"/>
  </f:facet>
  <f:facet name="below">
    <o:dataTablePaginator/>
  </f:facet>
</o:dataTable>
<h:outputText style="color:red; font-weight:bold; font-size:18px;" value="#{bundle.today}"/>
</h:form>
</f:view>

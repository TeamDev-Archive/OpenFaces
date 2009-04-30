<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<o:dataTable id="paginableDataTable"
             pageSize="3"
             var="paginable"
             value="#{TestEventsBean.tableTestCollection}"
             verticalGridLines="1px solid gray">
  <f:facet name="below">
    <o:dataTablePaginator id="paginableDataTablePaginator"/>
  </f:facet>
  <o:column id="col_first" style="padding: 10px 10px 10px 10px;">
    <f:facet name="header">
      <h:outputText value="first column header" id="paginableDataTable_firstColumnHeader"/>
    </f:facet>
    <h:outputText value="#{paginable.thirdColumn}" id="paginableDataTable_firstColumnBody"/>
    <f:facet name="footer">
      <h:outputText value="first column footer" id="paginableDataTable_firstColumnFooter"/>
    </f:facet>
  </o:column>
  <o:column id="col_second" style="padding: 10px 10px 10px 10px;">
    <f:facet name="header">
      <h:outputText value="second column header" id="paginableDataTable_secondColumnHeader"/>
    </f:facet>
    <h:outputText value="#{paginable.fourthColumn}" id="paginableDataTable_secondColumnBody"/>
    <f:facet name="footer">
      <h:outputText value="second column footer" id="paginableDataTable_secondColumnFooter"/>
    </f:facet>
  </o:column>
</o:dataTable>

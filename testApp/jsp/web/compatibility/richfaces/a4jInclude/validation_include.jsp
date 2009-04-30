<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:subview id="validation">
    <h:form id="clientValidation">
      <h1>Client validation</h1>
      <o:clientValidationSupport clientValidation="onSubmit">
        <o:floatingIconMessage style="background-color:yellow; border:solid 2px red;" showDetail="true"
                               showSummary="false"/>
      </o:clientValidationSupport>
      <h:inputText required="true" id="required_def"/>
      <br/>
      <h:inputText required="true" id="required"/>
      <br/>
      <h:message for="required" style="color:orange" showDetail="true"/>
      <br/>
      <h:message for="required" style="color:red;" showSummary="true"/>
      <br/>
      <o:dateChooser id="dch" required="true"/>
      <br/>
      <h:commandButton value="submit"/>
    </h:form>
    <h:form id="serverValidation">
      <h1>Server Validation</h1>
      <o:clientValidationSupport clientValidation="off">
        <o:floatingIconMessage style="background-color:yellow; border:solid 2px red;" showDetail="true"
                               showSummary="false"/>
      </o:clientValidationSupport>
      <h:inputText required="true" id="required_def"/>
      <br/>
      <h:inputText required="true" id="required"/>
      <br/>
      <h:message for="required" style="color:orange" showDetail="true"/>
      <br/>
      <h:message for="required" style="color:red;" showSummary="true"/>
      <br/>
      <o:dateChooser id="dch" required="true"/>
      <br/>
      <h:commandButton value="submit"/>
    </h:form>
</f:subview>

<f:subview id="validation"
           xmlns="http://www.w3.org/1999/xhtml"
           xmlns:h="http://java.sun.com/jsf/html"
           xmlns:ui="http://java.sun.com/jsf/facelets"
           xmlns:f="http://java.sun.com/jsf/core"
           xmlns:o="http://openfaces.org/">
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


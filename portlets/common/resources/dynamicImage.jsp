<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form>
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->
    <h:commandButton type="submit" value="Submit"/>
    <br/>
    <h:commandLink value="back" action="index"/>
    <br/>
    <o:dynamicImage data="#{testPortletBean.testImage}" width="150" height="150" alt="dynamic image"
                    style="border:red solid 3px;"/>

  </h:form>
</f:view>

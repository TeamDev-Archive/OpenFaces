<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form>
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->
    <h:commandLink value="back" action="index"/>
    <o:chart model="#{util.chart}" view="pie"/>
    <o:chart model="#{util.chart}"
             legendVisible="true"
             textStyle="color:orange;"
             titleText="People population in the different cuntries"
             width="500" height="500">
      <o:barChartView colors="#3E8EB3, #5ACAFF, #B3773E, #FFC559"
                      labelsVisible="true"
                      foregroundAlpha="0.5"/>
    </o:chart>

  </h:form>
</f:view>

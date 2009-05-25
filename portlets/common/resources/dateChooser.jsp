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
    <o:dateChooser id="dcLong"
                   dateFormat="long"
                   locale="ru"
                   todayText="Segodnya"
                   noneText="Nichego"
                   headerStyle="background: #DEF3FF; color: black;"
                   daysHeaderStyle="background: #87B2C9; color: white;"
                   selectedDayStyle="border: 1px solid black; background: white; color: black;"
                   dayStyle="border: 1px solid white;"
                   rolloverDayStyle="background: #FCE55D;"
                   weekendDayStyle="color: #B09F41;"
                   footerStyle="background: #DEF3FF; color: black; border-top: 1px solid #87B2C9;"/>
    <br/>
    <o:dateChooser dateFormat="short"/>
    <br/>
  </h:form>
</f:view>

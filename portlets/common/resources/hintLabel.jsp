<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form>
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->
    <h:commandButton action="sbmt" value="Submit"/>
    <h:commandLink value="back" action="index"/>
    <table width="500px" border="1">
      <tr>
        <td width="25%">
          Mercury
        </td>
        <td width="75%">
          <o:hintLabel value="Is the closest planet to the Sun and the eighth largest" style="width:200px;"
                       hintStyle="background-color:#fffff1"/>
        </td>
      </tr>
      <tr>
        <td width="25%">
          Venus
        </td>
        <td width="75%">
          <o:hintLabel value="Is the second planet from the Sun and the sixth largest" style="width:200px;"
                       hintStyle="background-color:#fffff1"/>
        </td>
      </tr>
      <tr>
        <td width="25%">
          Earth
        </td>
        <td width="75%">
          <o:hintLabel value="Is the third planet from the Sun and the fifth largest" style="width:200px;"
                       hintStyle="background-color:#fffff1"/>
        </td>
      </tr>
      <tr>
        <td width="25%">
          Mars
        </td>
        <td width="75%">
          <o:hintLabel value="Is the fourth planet from the Sun and the seventh largest" style="width:200px;"
                       hintStyle="background-color:#fffff1"/>
        </td>
      </tr>
    </table>
    <o:hintLabel
            style="background: beige; border: 1px solid brown; padding-top: 10px; padding-bottom: 10px; width: 100px;"
            hintStyle="background: PaleGreen; border: 1px solid DarkGreen; padding-top: 20px; padding-bottom: 20px;"
            value="Jupiter is more than twice as massive as all the other planets combined (the mass of Jupiter is 318 times that of Earth)"
            hint="Jupiter is more than twice as massive as all the other planets combined (the mass of Jupiter is 318 times that of Earth)"/>
    <o:hintLabel value="<b>Mars</b> is the <i>fourth</i> planet from the Sun and the seventh largest"
                 style="width:200px;" escape="false"/>
  </h:form>
</f:view>

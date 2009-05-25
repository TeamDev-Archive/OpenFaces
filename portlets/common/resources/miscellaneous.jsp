<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
<h:form id="form1">
<!--<link rel='stylesheet' type='text/css' id='main_css'-->
<!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->
<h:commandButton action="sbmt" value="Submit"/>
<h:commandLink value="back" action="index"/>
<o:focus autoSaveFocus="true" focusedComponentId="input1"/>
<o:scrollPosition autoSaveScrollPos="true"/>
<table>
<tr>
  <td>
    Input1
  </td>
  <td>
    <h:inputText id="input1"/>
  </td>
</tr>
<tr>
  <td>
    Input2
  </td>
  <td>
    <h:inputText id="input2"/>
  </td>
</tr>
<tr>
  <td>
    Input3
  </td>
  <td>
    <h:inputText id="input3"/>
  </td>
</tr>
<tr>
  <td>
    Input4
  </td>
  <td>
    <h:inputText id="input4"/>
  </td>
</tr>
<tr>
  <td>
    Input5
  </td>
  <td>
    <h:inputText id="input5"/>
  </td>
</tr>
<tr>
  <td>
    Input6
  </td>
  <td>
    <h:inputText id="input6"/>
  </td>
</tr>
<tr>
  <td>
    Input7
  </td>
  <td>
    <h:inputText id="input7"/>
  </td>
</tr>
<tr>
  <td>
    Input8
  </td>
  <td>
    <h:inputText id="input8"/>
  </td>
</tr>
<tr>
  <td>
    Input9
  </td>
  <td>
    <h:inputText id="input9"/>
  </td>
</tr>
<tr>
  <td>
    Input10
  </td>
  <td>
    <h:inputText id="input10"/>
  </td>
</tr>
<tr>
  <td>
    Input11
  </td>
  <td>
    <h:inputText id="input11"/>
  </td>
</tr>
<tr>
  <td>
    Input12
  </td>
  <td>
    <h:inputText id="input12"/>
  </td>
</tr>
<tr>
  <td>
    Input13
  </td>
  <td>
    <h:inputText id="input13"/>
  </td>
</tr>
<tr>
  <td>
    Input14
  </td>
  <td>
    <h:inputText id="input14"/>
  </td>
</tr>
<tr>
  <td>
    Input15
  </td>
  <td>
    <h:inputText id="input15"/>
  </td>
</tr>
<tr>
  <td>
    Input16
  </td>
  <td>
    <h:inputText id="input16"/>
  </td>

</tr>

</table>
</h:form>
</f:view>

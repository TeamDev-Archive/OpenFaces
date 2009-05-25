<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form id="form">
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->
    <h:commandButton type="submit" value="Submit"/>
    <br/>
    <h:commandLink value="back" action="index"/>
    <br/>
    <table>
      <tr>
        <td>
                    draggable="true"
          modalLayerStyle="background-color:gray;"
        </td>
        <td>
          <h:commandButton style="margin-top: 10px;"
                           value="Delete" id="buttonInvoker"
                           onclick="O$('form:buttonConfirmation').runConfirmedFunction(function(){O$('form:buttonPopup').show()});
                              return false;"/>
        </td>
      </tr>
      <tr>
        <td>
          for="confInvoker"           captionText="Confirmation caption" draggable="true"
          alignToInvoker="true"
        </td>
        <td>
          <h:commandLink id="confInvoker" onclick="alert('done!');return false;" value="Execute"/>
        </td>
      </tr>
      <tr>
        <td>
          Styles
        </td>
        <td>
          <h:commandButton id="button1" value="Show styled confirmation" onclick="alert('done!');return false;"/>
        </td>
      </tr>
    </table>
    <o:popupLayer id="buttonPopup"
                  hideOnOuterClick="true"
                  hidingTimeout="3000">
      <h:outputText style="color:#d46400;" value="Action is executed!"/>
    </o:popupLayer>
    <o:confirmation id="buttonConfirmation"
                    standalone="true"
                    draggable="true"
                    message="Confirm your action!"
                    modalLayerStyle="background-color:gray;"/>
    <o:confirmation id="conf"
                    for="confInvoker"
                    alignToInvoker="true"
                    captionText="Confirmation caption"
                    draggable="true"/>
    <o:confirmation id="styleConf"
                    for="button1"
                    buttonAreaStyle="background: blue;"
                    cancelButtonStyle="border: 1px dashed black;"
                    captionStyle="border: 1px dashed white;"
                    captionText="Caption Text"
                    detailsStyle="color: red;"
                    iconAreaStyle="border: 1px solid orange;"
                    messageStyle="color: blue;"
                    contentStyle="background: beige;"
                    modalLayerStyle="background: url(ground_Camus.gif);"
                    okButtonStyle="border: 1px dashed pink;"
                    rolloverButtonAreaStyle="background: azure;"
                    rolloverCancelButtonStyle="border: 3px dashed black;"
                    rolloverDetailsStyle="font-weight: bold;"
                    rolloverIconAreaStyle="border: 3px solid red;"
                    rolloverMessageStyle="color: green;"
                    rolloverContentStyle="background: orange;"
                    rolloverOkButtonStyle="border: 3px dashed pink;"
                    style="border: 3px solid black;"/>
  </h:form>
</f:view>

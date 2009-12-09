<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>PopupLayer Test</title>
  <script type="text/javascript">
    function applyAnswer(popupId, answer) {
      var popup = document.getElementById(popupId);
      popup.hide();
      var label = popup.getAnchorElement();
      label.innerHTML = answer;
    }

    function changePopupVisibility(invokerButton, popupId) {
      var popup = document.getElementById(popupId);
      if (popup.isVisible()) {
        popup.hide();
        invokerButton.value = 'show';
      } else {
        popup.show();
        invokerButton.value = 'hide';
      }
    }

    function showAttachedPopup(elt, popupId) {
      var popup = document.getElementById(popupId);
      popup.attachToElement(elt, 'center', 'middle');
      popup.show();
    }

    function setoff(popup, clientX, clientY) {
      if (popup.style.position != 'absolute') {
        popup.style.position = 'absolute';

        popup.style.width = "200px";
        popup.style.height = "100px";

        var x = popup.offsetLeft;
        var y = popup.offsetTop;

        if (clientX > (x + 200) || clientY > (y + 100)) {
          popup.style.left = clientX - 100;
          popup.style.top = clientY - 50;
        }
      }
    }

    function settleDown(popup, evtX, evtY) {
      var leftFrame = document.getElementById("leftDiv");
      checkAndInplaceDiv(leftFrame, popup, evtX, evtY);

      var bottomFrame = document.getElementById("bottomDiv");
      checkAndInplaceDiv(bottomFrame, popup, evtX, evtY);
    }

    function checkAndInplaceDiv(parentDiv, popup, evtX, evtY) {
      var x1 = O$.getElementPos(parentDiv).x;
      var x2 = x1 + parentDiv.offsetWidth;
      var y1 = O$.getElementPos(parentDiv).y;
      var y2 = y1 + parentDiv.offsetHeight;

      if (evtX > x1 && evtX < x2 && evtY > y1 && evtY < y2) {
        popup.parentNode.removeChild(popup);
        popup.style.position = "relative";
        popup.style.width = "100%";
        popup.style.height = "100%";
        popup.style.top = 0;
        popup.style.left = 0;
        parentDiv.appendChild(popup);
        popup.onmouseout();
      }
    }

    function checkSettleDown() {
      var popup = document.getElementById("form1:popupLayer1");
      settleDown(popup, popup._leftField.value, popup._topField.value);

      var btn = document.getElementById('popupInvoker1');
      btn.value = popup.isVisible() ? 'hide' : 'show';
    }

  </script>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
</head>

<body style="background-color: #E0FFFF;" onload="checkSettleDown();"
      onclick="document.getElementById('form1:popupLayer3').showAtXY(event.clientX, event.clientY);">
<h2 style="background: #e0e0e0; padding-bottom: 5px; border-bottom: 1px solid black;">Popup layer component tests</h2>
<f:view>
  <h:form id="form1">
   <%@ include file="popupLayerTest_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
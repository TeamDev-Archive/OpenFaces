<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>TabSet Test</title>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
  <script type="text/javascript">
    var imgSrc = new Array(5);
    imgSrc[0] = "../../../../work.jpg";
    imgSrc[1] = "../../../../disc.jpg";
    imgSrc[2] = "../../../../leas.jpg";
    imgSrc[3] = "../../../../artw.jpg";
    imgSrc[4] = "../../../../casa.jpg";

    function setImageByTabSet() {
      var img1 = O$('img1');
      img1.src = imgSrc[O$('form1:imageSelector')._index];
    }

    function setTabSetLabel(index) {
      var label = O$('tabSetLabel');
      label.innerHTML = "Selected index " + index;
    }
  </script>
</head>

<body>
<f:view>
  <h:form id="form1">
   <%@ include file="tabSetTest_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
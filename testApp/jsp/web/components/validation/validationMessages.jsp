<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Validation messages</title>
  <script type="text/javascript">
    function fillInvalidData(formName) {
      var field = document.getElementById(formName + & quot;
    :
      required & quot;
    )
      ;
      field.value =
    &
      quot;
    &
      quot;
      ;
      field = document.getElementById(formName + & quot;
    :
      validDR & quot;
    )
      ;
      field.value = 5;
      field = document.getElementById(formName + & quot;
    :
      url & quot;
    )
      ;
      field.value =
    &
      quot;
      not
      url & quot;
      ;
      field = document.getElementById(formName + & quot;
    :
      ddf & quot;
    )
      ;
      field.value =
    &
      quot;
    &
      quot;
      ;

    }

    function fillValidData(formName) {
      var field = document.getElementById(formName + & quot;
    :
      required & quot;
    )
      ;
      field.value =
    &
      quot;
      text & quot;
      ;
      field = document.getElementById(formName + & quot;
    :
      validDR & quot;
    )
      ;
      field.value = 0.1;
      field = document.getElementById(formName + & quot;
    :
      url & quot;
    )
      ;
      field.value =
    &
      quot;
      http://www.teamdev.com&quot;;
              field = document.getElementById(formName + & quot;
    :
      ddf & quot;
    )
      ;
      field.value =
    &
      quot;
      text & quot;
      ;

    }
  </script>
</head>

<body>
<f:view>
  <a href="../../../../main.jsp" style="float: right;">Return to main page</a>
  <br/>
  <a href="../../../../components_test.jsp" style="float: right;">Return to validation testing main page</a>
  <table width="100%">
    <tr>
      <td width="50%">
        <h:form id="serverValidation">
          <%@ include file="validationMessagesServer_core.xhtml" %>
        </h:form>
      </td>
      <td width="50%">
        <h:form id="clientValidation">
          <%@ include file="validationMessagesClient_core.xhtml" %>
        </h:form>
      </td>
    </tr>
  </table>
</f:view>

</body>
</html>
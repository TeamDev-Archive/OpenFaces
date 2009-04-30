<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title></title>
  <script>
    function startMeasuring() {
      window.tableProfiler = new O$.Profiler();
      window.tableProfiler.startMeasuring("Table Reloading Total");
      window.tableProfiler.logTimeStamp("start");
    }
    function endMeasuring() {
      window.tableProfiler.logTimeStamp("end");
      window.tableProfiler.endMeasuring("Table Reloading Total");
      window.tableProfiler.showAllTimings();
    }
  </script>
</head>

<body>
<f:view>
  <h:form id="frm">
   <%@ include file="QKS1140_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
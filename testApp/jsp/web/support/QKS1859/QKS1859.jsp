<%@ page import="org.openfaces.testapp.support.QKS145.DataTableItem" %>
<html>
<head>
  <title>Data Table &mdash; OpenFaces Demo</title>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
</head>

<body>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib uri="http://openfaces.org/" prefix="o"%>
<% java.util.ArrayList arr = null;%>
<%
  HttpSession sess = request.getSession(true);
  arr = new java.util.ArrayList();
  arr.add(new DataTableItem(1, "AAAAA"));
  arr.add(new DataTableItem(2, "BBBB"));
  arr.add(new DataTableItem(3, "CCCC"));
  arr.add(new DataTableItem(4, "DDDD"));
  arr.add(new DataTableItem(5, "EEEE"));
  arr.add(new DataTableItem(6, "FFFF"));
  arr.add(new DataTableItem(7, "GGGG"));
  arr.add(new DataTableItem(8, "HHHH"));
  arr.add(new DataTableItem(9, "IIII"));
  sess.setAttribute("Mylist", arr);
  request.setAttribute("Mylist", arr);
%>
<div id="nonFooter">
<f:view>
  <h:form>
   <%@ include file="QKS1859_core.xhtml" %>
  </h:form>
</f:view>
</div>

</body>
</html>
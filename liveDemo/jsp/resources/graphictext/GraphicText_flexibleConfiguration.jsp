 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
 <%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<html>
<head>
  <!--<link rel="STYLESHEET" type="text/css" href="datatable.css"/>-->
  <link rel="SHORTCUT ICON" href="../images/icons/favicon.ico"/>

  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="AUTHOR" content="TeamDev Ltd."/>
  <meta name="CONTENT-LANGUAGE" content="en-US,ru"/>
  <meta name="COPYRIGHT" content=" 2012 TeamDev Ltd."/>
  <meta name="CACHE-CONTROL" content="Public"/>

  <%@ include file="../template/keywords.xhtml"%>

  <title>Graphic Text &#8212; OpenFaces Demo</title>
  <link rel="stylesheet" href="../design/css/style.css" type="text/css" media="screen,projection"/>
  <link rel="stylesheet" href="../design/css/menu.css" type="text/css" media="screen,projection"/>
  <link rel="stylesheet" href="../design/css/print.css" type="text/css" media="print"/>
  <link rel="stylesheet" href="../design/css/handheld.css" type="text/css" media="handheld"/>
  <!--[if lt IE 7]>
      <link rel="stylesheet" type="text/css" media="all" href="../design/css/style_ie6.css">
      <script src="/js/stylefix.js" type="text/javascript"></script>
  <![endif]-->
  <link rel="icon" href="../favicon.ico" type="image/x-icon"/>
  <link rel="shortcut icon" href="../favicon.ico" type="image/x-icon"/>
  <script src="../design/js/style.js" type="text/javascript"></script>

  <link rel="stylesheet" href="graphictext.css" type="text/css" media="screen,projection"/>
</head>

<body>

<%@ include file="../template/pageBackground.xhtml"%>

<div id="Page" class="clearfix">
  <f:view>
    <h:form id="form">

      <%@ include file="../template/logo.xhtml"%>

      <!--start site menu-->
      <%@ include file="../template/siteMenu.xhtml"%>
      <!--end start menu-->

      <!--- start tabs -->
      <%@ include file="../template/tabs.xhtml"%>
      <!--- end tabs -->

      <div id="LeftPannel">
        <!--- start key features -->
        <%@ include file="../template/sidePart.xhtml"%>
        <!--- end key features -->

        <!--- start menu -->
        <%@ include file="../template/menu.xhtml"%>
        <!--- end menu -->
      </div>

      <div id="Content">
        <!-- start content -->
        <%@ include file="GraphicText_flexibleConfiguration_core.xhtml"%>
        <!-- end content -->
      </div>
    </h:form>
  </f:view>
</div>

<%@ include file="../template/footer.xhtml"%>

</body>
</html>
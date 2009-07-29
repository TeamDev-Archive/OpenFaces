<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Calendar</title>
    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen,projection"/>
    <link rel="stylesheet" href="css/navigation.css" type="text/css" media="screen,projection"/>
    <link rel="stylesheet" href="css/sidebar.css" type="text/css" media="screen,projection"/>
    <link rel="stylesheet" href="css/content.css" type="text/css" media="screen,projection"/>

    <link rel="stylesheet" href="css/print.css" type="text/css" media="print"/>
    <link rel="stylesheet" href="css/handheld.css" type="text/css" media="handheld"/>

    <link rel="shortcut icon" href="favicon.ico" type="image/vnd.microsoft.icon"/>
    <link rel="icon" href="favicon.ico" type="image/vnd.microsoft.icon"/>
    <link rel="stylesheet" href="treetable.css" type="text/css" media="screen,projection"/>
    <link rel="stylesheet" href="css/mail.css" type="text/css" media="screen,projection"/>
    <script type="text/javascript">
        function updateLayout() {
            var dayTable = O$("form:dayTable");
            if (dayTable != null) {
                if (dayTable.updateLayout) {
                    dayTable.updateLayout();
                } else {
                    setTimeout(updateLayout, 1500);
                }
            }
        }
    </script>
</head>
<body>
<f:view>
    <h:form id="form">

        <div id="Navigation" class="NavigationSelected-Calendar">
            <div class="NavigationPanel" id="Navigation-Email">
                <h1>
                    <img src="images/logo.png" alt="OpenFaces"/>
                    Email
                </h1>
                <a href="mail.jsf" class="label">
                    <img src="images/titles/navigation-email.png" alt="Email"/>
                </a>
            </div>
            <div class="NavigationPanel" id="Navigation-Calendar">
                <h1>Calendar</h1>
            </div>
            <div class="NavigationPanel" id="Navigation-Tasks">
                <h1>Tasks</h1>
                <a href="tasks.jsf" class="label">
                    <img src="images/titles/navigation-tasks.png" alt="Tasks"/>
                </a>
            </div>
        </div>

        <div id="Sidebar" class="Sidebar">
            <div class="Sidebar-content">
                <rich:calendar id="calendar" popup="false" showApplyButton="false" cellWidth="24px" cellHeight="22px"
                               style="width:200px"
                               value="#{DayTableBean.calendarDate}" dataModel="#{DayTableBean}">
                    <a4j:support event="onchanged" reRender="dayTable">
                        <f:param name="dateChangeEventSource" value="calendar"/>
                    </a4j:support>
                </rich:calendar>
            </div>

            <div class="SidebarFooter">
                <a class="ButtonPageSource" onclick="O$('form:pageSource').showCentered(); return false">
                    <span>View page source</span>
                </a>

                <div class="Copyright">
                    <p>&copy;&nbsp;TeamDev Ltd. | OpenFaces.org</p>
                </div>
            </div>
        </div>

        <div id="Content" class="Content">
            <o:dayTable id="dayTable"
                        events="#{DayTableBean.events}"
                        day="#{DayTableBean.dayTableDate}"
                        startTime="6:00"
                        timetableChangeListener="#{DayTableBean.processTimetableChanges}"
                        majorTimeStyle="font-family: MS Shell Dlg,Arial,sans-serif; font-size: 14pt;"
                        timeSuffixStyle="font-size: 7pt; line-height: 1em;">
                <o:timetableEvent style="text-align:center;" descriptionStyle="color: #5f5f5f;"
                                  escapeDescription="false" escapeName="false"/>
                <o:eventArea horizontalAlignment="rightEdge" verticalAlignment="above">
                    <h:commandLink title="Postpone training for 1 hour" style="margin: 10px">
                        <h:outputText value="1&#160;hour&#160;later" escape="false"/>
                        <o:reloadComponents componentIds=":form:dayTable" action="#{DayTableBean.doLater}"
                                            disableDefault="true"/>
                    </h:commandLink>
                </o:eventArea>

                <o:eventActionBar>
                    <o:eventAction id="oneDayPostponeAction"
                                   imageUrl="images/daytable/customAction1.gif"
                                   hint="Postpone for 1 day"/>
                </o:eventActionBar>
            </o:dayTable>
        </div>

        <o:popupLayer id="pageSource"
                      styleClass="SourceView"
                      modal="true"
                      modalLayerClass="SourceViewModalLayer">
            <h:commandButton onclick="O$('form:pageSource').hide(); return false" value="hide"/>
            <rich:insert src="/calendar.jsp" highlight="xhtml"/>
        </o:popupLayer>

    </h:form>
</f:view>
</body>
</html>
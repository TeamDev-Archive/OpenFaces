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
    <link rel="stylesheet" href="css/calendar.css" type="text/css" media="screen,projection"/>
    <script type="text/javascript">
        function initCalendar() {
            var calendar = O$("form:calendar");
            var rows = calendar.tBodies[0].rows;
            for (var r = 0; r != rows.length; r++) {
                var row = rows[r];
                var cells = row.cells;
                for (var c = 0; c != cells.length; c++)
                    if (cells[c].className.indexOf("rich-calendar-select") != -1) {
                    	updateCurrentWeek(row);
                    	return;
                    }
            }
        }
        function updateCurrentWeek(tr) {
            var tbody = tr.parentNode;
            if (tbody._of_calendar_currentweek)
            	tbody._of_calendar_currentweek.className = null;
        	tr.className = "calendar-currentweek";
        	tbody._of_calendar_currentweek = tr;
        }
        function updateCalendarStyles(td) {
            updateCurrentWeek(td.parentNode);
        }
    </script>
</head>
<body onload="initCalendar()">
<f:view>
    <h:form id="form">

        <div id="Navigation" class="NavigationSelected-Calendar" style="z-index:0">
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
                <rich:calendar id="calendar" popup="false" showApplyButton="false" cellWidth="30px" cellHeight="19px"
                               styleClass="calendar-current" showWeeksBar="false" showFooter="false"
                               value="#{DayTableBean.calendarDate}" dataModel="#{DayTableBean}"
                               mode="ajax" ondateselect="updateCalendarStyles(this);">
                    <a4j:ajax event="changed" render="dayTableToolBar,dayTable">
                        <f:param name="dateChangeEventSource" value="calendar"/>
                    </a4j:ajax>
                    <h:outputText value="{day}" styleClass="inner"/>
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
        	<rich:toolBar itemSeparator="none" styleClass="day-table-toolbar">
        		<a4j:commandLink reRender="dayTable,calendar,dayTableToolBar" oncomplete="initCalendar()"
        				actionListener="#{DayTableBean.prevDayEventActionListener}">
        			<h:outputText value="&lt;" escape="false"/>
        		</a4j:commandLink>
       			<h:graphicImage url="images/calendar/daytable-tool-separator.png"/>
       			<h:panelGroup id="dayTableToolBar" styleClass="date">
        			<h:outputText value="#{DayTableBean.dayOfWeek}" styleClass="day-of-week"/>
        			<h:panelGroup styleClass="day-of-month">
        				<h:outputText value="#{DayTableBean.dayOfMonth}"/>
        				<h:outputText value=" "/>
	        			<h:outputText value="#{DayTableBean.month}"/>
        			</h:panelGroup>
        		</h:panelGroup>
       			<h:graphicImage url="images/calendar/daytable-tool-separator.png"/>
        		<a4j:commandLink reRender="dayTable,calendar,dayTableToolBar" oncomplete="initCalendar()"
        				actionListener="#{DayTableBean.nextDayEventActionListener}">
        			<h:outputText value="&gt;" escape="false"/>
        		</a4j:commandLink>
        	</rich:toolBar>

            <o:dayTable id="dayTable"
                        events="#{DayTableBean.events}"
                        day="#{DayTableBean.dayTableDate}"
                        startTime="6:00"
                        timetableChangeListener="#{DayTableBean.processTimetableChanges}"
                        majorTimeStyle="font-family: MS Shell Dlg,Arial,sans-serif; font-size: 14pt;"
                        timeSuffixStyle="font-size: 7pt; line-height: 1em;">
                <o:timetableEvent style="text-align:center;" descriptionStyle="color: #5f5f5f;"
                                  escapeDescription="false" escapeName="false"/>
                <o:eventArea id="eventArea" horizontalAlignment="right" verticalAlignment="above">
                    <h:commandLink id="postponeOneHour" title="Postpone training for 1 hour" style="margin: 10px">
                        <h:outputText value="1&#160;hour&#160;later" escape="false"/>
                        <o:ajax render=":form:dayTable" listener="#{DayTableBean.doLater}"/>
                    </h:commandLink>
                </o:eventArea>

                <o:eventActionBar>
                    <o:eventAction id="oneDayPostponeAction"
                                   imageUrl="images/daytable/customAction1.gif"
                                   hint="Postpone for 1 day"/>
                </o:eventActionBar>
                <o:ajax render=":form:calendar" event="change"/>
            </o:dayTable>
        </div>

        <o:window id="pageSource" width="70%" height="70%"
                      styleClass="SourceView"
                      modal="true"
                      modalLayerClass="SourceViewModalLayer">
    		<f:facet name="caption"><h:outputText value="calendar.jsp"/></f:facet>
            <rich:insert src="/calendar.jsp" highlight="xhtml"/>
        </o:window>
    </h:form>
</f:view>
</body>
</html>
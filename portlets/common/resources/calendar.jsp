<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form>
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->

    <h:commandButton action="sbmt" value="Submit"/>
    <h:commandLink value="back" action="index"/>
    <table>
      <tr>
        <td width="200px;">
          Calendar without any attributes
        </td>
        <td>
          <o:calendar id="c"/>
        </td>
      </tr>
      <tr>
        <td width="200px;">
          Required validator (default presentation)
          showFooter="false"
        </td>
        <td>
          <o:calendar id="required" required="true" showFooter="false"/>
        </td>
      </tr>
      <tr>
        <td width="200px;">
          Styles
        </td>
        <td>
          <o:calendar id="styles"
                      bodyStyle="background: PowderBlue;"
                      daysHeaderStyle="background: #A8FFFE;"
                      disabledDayStyle="color: blue;"
                      dayStyle="color: red;"
                      footerStyle="background: #12FF28;"
                      headerStyle="background: #FF36F8;"
                      inactiveMonthDayStyle="color: yellow;"
                      rolloverDayStyle="font-weight: bold;"
                      rolloverDisabledDayStyle="color: darkgreen;"
                      rolloverInactiveMonthDayStyle="color: aquamarine;"
                      rolloverSelectedDayStyle="background: orange; font-weight: bold;"
                      rolloverTodayStyle="border: 1px solid black;"
                      rolloverWeekendDayStyle="background: red;"
                      selectedDayStyle="border: 1px solid SpringGreen;"
                      todayStyle="color: Teal;"
                      weekendDayStyle="border: 1px dotted RoyalBlue;">
            <o:dateRanges disableExcludes="true">
              <o:simpleDateRange fromDate="#{testPortletBean.fromDate}" toDate="#{testPortletBean.toDate}"/>
            </o:dateRanges>
          </o:calendar>
        </td>
      </tr>
      <tr>
        <td width="200px;">
          The other attributes
          firstDayOfWeek="3"
          locale="ru"
          noneText="N"
          todayText="C"
        </td>
        <td>
          <o:calendar id="attr"
                      firstDayOfWeek="3"
                      locale="ru"
                      noneText="N"
                      todayText="C">
            <o:dateRanges disableExcludes="true">
              <o:simpleDateRange fromDate="#{testPortletBean.fromDate}" toDate="#{testPortletBean.toDate}"
                                 dayStyle="color:orange"/>
            </o:dateRanges>
          </o:calendar>

        </td>
      </tr>
    </table>
  </h:form>
</f:view>

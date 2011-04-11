<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="uiComponents" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="jfc" uri="http://www.jahia.org/tags/charts" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<%--@elvariable id="acl" type="java.lang.String"--%>

    <template:addResources type="javascript" resources="FusionCharts.js" />

    <c:if test="${renderContext.editMode}"><fmt:message key="chart.edit" /></c:if>
    <c:set var="bindedComponent"  value="${uiComponents:getBindedComponent(currentNode, renderContext, 'j:bindedComponent')}"/>


    <div id="${currentNode.name}" align="center"><fmt:message key="chart.replace" /></div>
    <script type="text/javascript">
        var myChart = new FusionCharts("${url.currentModule}/swf/FCF_${currentNode.properties.type.string}.swf", "myChart${currentNode.name}", "${currentNode.properties.width.string}", "${currentNode.properties.height.string}");
        <%--myChart.setDataURL("${url.base}${bindedComponent.path}.xml");--%>
        myChart.setDataXML("${jfc:convert(bindedComponent.properties.text.string)}");
        <%--myChart.setDataURL("/modules/jahia-fusion-charts/Data.xml");--%>
        <%-- myChart.setDataXML("<graph caption='Monthly Unit Sales' xAxisName='Month' yAxisName='Units' showNames='1' decimalPrecision='0' formatNumberScale='0'><set name='Jan' value='462' color='AFD8F8' /><set name='Feb' value='857' color='F6BD0F' /><set name='Mar' value='671' color='8BBA00' /><set name='Apr' value='494' color='FF8E46'/><set name='May' value='761' color='008E8E'/><set name='Jun' value='960' color='D64646'/><set name='Jul' value='629' color='8E468E'/><set name='Aug' value='622' color='588526'/><set name='Sep' value='376' color='B3AA00'/><set name='Oct' value='494' color='008ED6'/><set name='Nov' value='761' color='9D080D'/><set name='Dec' value='960' color='A186BE'/></graph>");--%>
        myChart.render("${currentNode.name}");
    </script>
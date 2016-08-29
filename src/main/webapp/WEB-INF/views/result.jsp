<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false" %>
<%@ page contentType="text/html;charset=Windows-1251" language="java" %>
<html>
<head>
	<title>Person</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:5px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
		.error {color: #D8000C;font-size:12px;font-weight:bold}
	</style>
</head>
<body>
<h3>
	Результаты обработки данных
</h3>

<c:if test="${!empty resultList}">
	<table class="tg">
	<c:forEach items="${resultList}" var="res">
		<tr>
			<td>${res}</td>
		</tr>
	</c:forEach>
	</table>
</c:if>

<c:url var="getListAction" value="/persons/list" ></c:url>
<form:form action="${getListAction}" method="GET">
<input type="submit" value="<spring:message text="На главную страницу"/>" />
</form:form>

</body>
</html>

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
	Персонал
</h3>

<c:url var="addAction" value="/person/add" ></c:url>
<form:form action="${addAction}" modelAttribute="person">
<table>
	<c:if test="${!empty person.lastName}">
	<tr>
		<td>
            <form:label path="id">
                <spring:message text="ID"/>
            </form:label>
		</td>
		<td>
			<form:input path="id" readonly="true" size="8"  disabled="true" />
			<form:hidden path="id" />
		</td> 
	</tr>
	</c:if>

	<tr>
		<td>
            <form:label path="lastName">
                <spring:message text="Фамилия"/>
            </form:label>
		</td>
		<td>
			<form:input path="lastName" />
		</td>
		<td><form:errors path="lastName" class="error" /></td>
	</tr>
	<tr>
		<td>
			<form:label path="firstName">
				<spring:message text="Имя"/>
			</form:label>
		</td>
		<td>
		    <form:input path="firstName" />
		</td>
		<td><form:errors path="firstName" class="error" /></td>
	</tr>
	<tr>
		<td>
			<form:label path="middleName">
				<spring:message text="Отчество"/>
			</form:label>
		</td>
		<td>
			<form:input path="middleName" />
		</td>
		<td><form:errors path="middleName" class="error" /></td>
	</tr>
	<tr>
		<td>
			<form:label path="birthDate">
				<spring:message text="Дата рождения"/>
			</form:label>
		</td>
		<td>
		    <fmt:formatDate value="${person.birthDate}" var="dateString" pattern="dd.MM.yyyy" />
			<form:input path="birthDate" value="${dateString}"/>
        </td>
		<td><form:errors path="birthDate"  class="error" /></td>

	</tr>

	<tr>
		<td colspan="2">
			<c:if test="${!empty person.lastName}">
				<input type="submit"
					value="<spring:message text="Обновить"/>" />
			</c:if>
			<c:if test="${empty person.lastName}">
				<input type="submit"
					value="<spring:message text="Добавить"/>" />
			</c:if>
		</td>
	</tr>
</table>
</form:form>
<br>



<c:url var="getListAction" value="/persons/list" ></c:url>
<form:form action="${getListAction}" method="GET" modelAttribute="person">
<input type="submit" value="<spring:message text="Получить"/>" />
</form:form>



<c:url var="dataProcessAction" value="/persons/process" ></c:url>
<c:if test="${!empty listPersons}">
<h3>Список персон</h3>
<form:form action="${dataProcessAction}" modelAttribute="selected">
	<table class="tg">
	<tr>
		<th width="80">Выбрать</th>
		<th width="80">ID</th>
		<th width="120">Фамилия</th>
		<th width="120">Имя</th>
		<th width="120">Отчество</th>
		<th width="120">Дата рождения</th>
		<th width="120">Комментарий</th>
		<th width="60">Обновить</th>
		<th width="60">Удалить</th>
	</tr>
	<c:forEach items="${listPersons}" var="p" varStatus="status">
		<tr>
			<td><form:checkbox path="selectedIds[${status.index}]" value="${p.id}" />
			<td>${p.id}</td>
			<td>${p.lastName}</td>
			<td>${p.firstName}</td>
			<td>${p.middleName}</td>
			<fmt:formatDate value="${p.birthDate}" var="dateString" pattern="dd.MM.yyyy" />
			<td>${dateString}</td>
			<td>${p.comment}</td>
			<td><a href="<c:url value='/edit/${p.id}' />" >Обновить</a></td>
			<td><a href="<c:url value='/remove/${p.id}' />" >Удалить</a></td>
		</tr>
	</c:forEach>

	<tr>
		<td colspan="2" bordercolor="#fff">
			<input type="submit" value="<spring:message text="Обработать данные"/>" />
		</td>
	</tr>

	</table>
</form:form>
</c:if>

</body>
</html>

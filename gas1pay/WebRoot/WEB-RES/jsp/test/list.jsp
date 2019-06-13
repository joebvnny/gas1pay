<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--                MVC               IOC/AOP               ORM               -->
<!--  [WebClient]  <===>  [WebServer]  <===>  [BizServer]  <===>  [DBServer]  -->
<!--  {HTM/CSS/JS}        {JSP/Servlet}       {Action/Biz/DAO}    {SQL}       -->
<html>
<head>
<title>Order List</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/WEB-RES/lib/_test_jquery_easyui/jquery-1.11.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/WEB-RES/lib/_test_jquery_easyui/jquery.easyui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/WEB-RES/lib/_test_jquery_easyui/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/WEB-RES/css/easyui/icon.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/WEB-RES/css/easyui/default/easyui.css" />
<style type="text/css">
    .bold {font-weight: bold}
</style>
</head>
<body>
<div id="dlg" class="easyui-dialog" title="燃气订单管理" style="width:680px;height:400px">
    <div align="center">
    <form id="findForm" action="select.do" method="POST">
    <fieldset>
    <legend>燃气订单查找</legend>
    <input type="text" name="orderId"/>&nbsp;&nbsp;<input type="submit" value="查找""/>
    </fieldset>
    </form>
    </div>
    
	<fieldset>
	<legend><a href="list.do">燃气订单列表</a></legend>
	<table align="center" width="600" border="1" cellpadding="1" cellspacing="1">
	<thead>
	<tr bgcolor="#999999" class="bold">
	    <td>ID</td><td>CompanyCode</td><td>ChargeFee</td><td>OrderTime</td><td>操作</td>
	</tr>
	</thead>
	<tbody>
	<c:if test="${orders.size()>0 }">
	<c:forEach var="m" items="${orders }">
	<tr bgcolor="#CCCCCC">
	    <td>${m.id }</td><td>${m.companyCode }</td><td>${m.chargeFee }</td><td>${m.orderTime }</td>
	    <td><a href="${m.id }/edit.do">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${m.id }/delete.do" onclick="javascript:return confirm('确认删除 ID[${m.id}]？')">删除</a></td>
	</tr>
	</c:forEach>
	</c:if>
	</tbody>
	</table>
	</fieldset>

	<div align="center">
	<fieldset>
	<legend>燃气订单注册</legend>
	<sf:form id="regForm" action="insert.do" method="POST" modelAttribute="order">
	<table border="0" cellpadding="1" cellspacing="1">
	<tr><td><b>ID :</b></td><td><sf:input path="id"/></td></tr>
	<tr><td><b>CompanyCode :</b></td><td><sf:input path="companyCode"/></td></tr>
	<tr><td><b>ChargeFee :</b></td><td><sf:input path="chargeFee"/></td></tr>
	<tr><td><b>OrderTime :</b></td><td><sf:input path="orderTime"/></td></tr>
	<tr><td colspan="2"><input type="submit" value="注册"/>&nbsp;&nbsp;<input type="reset" value="重置"/></td></tr>
	</table>
	</sf:form>
	</fieldset>
	</div>
</div>
</body>
</html>
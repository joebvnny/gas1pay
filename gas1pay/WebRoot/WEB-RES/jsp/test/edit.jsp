<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Order Edit</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/WEB-RES/lib/_test_jquery_easyui/jquery-1.11.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/WEB-RES/lib/_test_jquery_easyui/jquery.easyui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/WEB-RES/lib/_test_jquery_easyui/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/WEB-RES/css/easyui/icon.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/WEB-RES/css/easyui/default/easyui.css" />
</head>
<body>
<div id="dlg" class="easyui-dialog" title="燃气订单修改" style="width:480px;height:240px">
    <div align="center">
    <sf:form id="editForm" action="update.do" method="POST" modelAttribute="order"><br/><br/>
    <table border="0" cellpadding="1" cellspacing="1">
        <tr><td><b>CompanyCode :</b></td><td><sf:input path="companyCode"/></td></tr>
        <tr><td><b>ChargeFee :</b></td><td><sf:input path="chargeFee"/></td></tr>
        <tr><td><b>OrderTime :</b></td><td><sf:input path="orderTime"/></td></tr>
        <tr><td colspan="2"><input type="submit" value="修改"/>&nbsp;&nbsp;<input type="reset" value="重置"/>&nbsp;&nbsp;<input type="button" value="返回" onclick="javascript:history.go(-1)"/></td></tr>
    </table>
    </sf:form>
    </div>
</div>
</body>
</html>
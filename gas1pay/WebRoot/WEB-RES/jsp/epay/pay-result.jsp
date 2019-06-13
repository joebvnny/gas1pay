<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>气付通支付结果</title>
<link rel="shortcut icon" href="/WEB-RES/img/HL2.ico" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/sui.css">
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/web.css">
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/pay.css">
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/public.css" />
<script type="text/javascript" src="/WEB-RES/lib/common.js"></script>

<style>
td, a {
    font-size: 14px;
}
.footer {
    width: 100%;
    height: 40px;
    bottom: 0px;
    z-index: 3;
    position: fixed;
    overflow: visible;
    background: #e8e8e8;
    border-top: solid 1px #ccc;
}
</style>
</head>

<body>
<div id="header">
    <div class="logo"><img src="/WEB-RES/img/HENL_CENTER.png" /></div>
    <div style="font-size:14px"><a href="javascript:void(0)" onclick="window.location.href='/'" target="_self" class="aProblem">退出</a></div>
</div>

<div align="center">
    <table cellspacing="10">
        <tr><td><b>${payResult}</b></td></tr>
        <tr><td>&emsp;</td></tr>
        <tr><td>${tradeNoName}：${tradeNo}</td></tr>
        <tr><td>支付金额：${payedFee} 元</td></tr>
        <tr><td>&emsp;</td></tr>
        <tr><td align="center">[<a href="javascript:void(0)" onclick="window.location.href='/'" target="_self">回执确认</a>]</td></tr>
    </table>
</div>

<div class="footer">
    <p class="copyright_banquan">©2005～2017&emsp;海力智能科技股份有限公司&emsp;&emsp;ICP备案号：蜀ICP备50263615号</p>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>海力气付通支付网关</title>
<link rel="shortcut icon" href="/WEB-RES/img/HL2.ico" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/sui.css">
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/web.css">
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/pay.css">
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/public.css" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/lib/jquery-easyui-1.4.4/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/WEB-RES/lib/jquery-easyui-1.4.4/themes/default/easyui.css">
<script type="text/javascript" src="/WEB-RES/lib/jquery-easyui-1.4.4/jquery.min.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/common.js"></script>
<script type="text/javascript" src="/WEB-RES/js/payways.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/jquery-easyui-1.4.4/jquery.qrcode.min.js"></script>

<style>
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

<div class="width1003">
<div class="pay_infor">
    <div class="infor_box">
        <lable><span>订单总额：</span><span id="amount"></span><span>元</span></label>
        <span class="helpInfo"><a href="javascript:void(0)" onMouseOver="showInfo()" onMouseOut="hideInfo()">查看详单</a>
            <div id="order_detail_div" style="display:none;position:absolute;left:64px;z-index:999">
                <table id="datagrid_orderdetail" title="详单明细" style="width:370px;height:120px" data-options="method:'get',url:'datagrid_data1.json',singleSelect:true,collapsible:false"></table>
            </div>
        </span>
        <label><span>应付金额：</span><span id="amount2"></span></label>
        <div class="clear"></div>
    </div>
</div>

<div class="pay_mode_nav"><span>请选择您的支付方式：</span></div>
<div class="bank_list">
    <input id="pay_gateway" name="pay_gateway" style="display: none" />
    <ul class="sel_list">
    <table>
    <tr>
    <td width=360>
        <li>
        <h6><label>支付宝支付</label></h6>
        <label>
            <input type="radio" name="paymentInfo.bankSelect" id="gateway_AL" value="AL" onclick="setSelValue(this.value)">
            <a class="AliPay01" href="javascript:void(0)" onclick="imageSel('AL')"></a>
        </label>
        </li>
    </td>
    <td width=360>
        <li>
        <h6><label>微信扫码支付</label></h6>
        <label>
            <input type="radio" name="paymentInfo.bankSelect" id="gateway_WX" value="WX" onclick="setSelValue(this.value)">
            <a class="WX_TENPAY" href="javascript:void(0)" onclick="imageSel('WX')"></a>
        </label>
        </li>
    </td>
    <td width=360>
        <li>
        <h6><label>银联/网银支付</label></h6>
        <label>
            <input type="radio" name="paymentInfo.bankSelect" id="gateway_UN" value="UN" onclick="setSelValue(this.value)">
            <a class="DEBITCARD_UNIONPAY" href="javascript:void(0)" onclick="imageSel('UN')"></a>
        </label>
        </li>
    </td>
    </tr>
    </table>
    </ul>
    <div style="height:120px"></div>
    <div class="confirm_pay"><a href="javascript:void(0)" onclick="gatewayPay()" class="confirm_pay_btn"></a></div>
</div>
</div>

<div class="up-box w600" style="display:none;z-index:20000; position:fixed; left:420px; top:100px;" id="weixin_pay_qr" dhxbox="1">
    <div class="up-box-hd">微信扫码支付<img src="/WEB-RES/img/icon_close.png" id="weixinpayClose" style="cursor:pointer;float:right" /></div>
    <div class="up-box-bd">
        <div style="text-align:center"><span id="wexin_total"></span></div>
        <div  class="up-con clearfix" style="text-align:center">
        <div id="qr_code"></div>
        <div style="color:#666; margin-top:20px">
            <span style="font-size:14px; width:100%; height:35px; line-height:20px; text-align:center; float:right">
                <img src="/WEB-RES/img/wx_code_scan.png">&emsp;打开手机微信“扫一扫”继续付款。<br clear="none">支付完成前，请勿关闭此二维码支付窗口！
            </span>
        </div>
        </div>
    </div>
</div>

<div class="up-box w600" style="display:none; z-index:20000; position:fixed; left:420px; top:230px" id="waitpay_online" dhxbox="1">
    <div class="up-box-hd">银联网上支付<img src="/WEB-RES/img/icon_close.png" id="unionpayClose" style="cursor:pointer;float:right" /></div>
    <div class="up-box-bd">
        <div class="up-con clearfix">
            <span style="vertical-align:middle"></span>
            <div class="r-txt">
                <p class="ft14"><img src="/WEB-RES/img/working.gif">&emsp;支付完成前，请勿关闭此状态窗口！<br clear="none">支付完成后，请稍等页面处理结束自动刷新返回。</p>
            </div>
        </div>
    </div>
</div>

<div class="mask" id="ztky_mask" style="display:none"></div>
<div class="footer">
    <p class="copyright_banquan">©2005～2017&emsp;海力智能科技股份有限公司&emsp;&emsp;ICP备案号：蜀ICP备50263615号</p>
</div>
</body>
</html>
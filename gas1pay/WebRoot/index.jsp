<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>海力气付通燃气缴费平台</title>
<link rel="shortcut icon" href="/WEB-RES/img/HL2.ico" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/public.css" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/layer/css/layer.css" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/clicki.web.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/clicki.loginandreg.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/jquery-confirm.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/lib/bootstrap-3.2.0/bootstrap-core/css/bootstrap.min.css" media="screen" />
<script type="text/javascript" src="/WEB-RES/lib/jquery-easyui-1.4.4/jquery.min.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/jquery-easyui-1.4.4/jquery-confirm.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/jquery.cookie.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/common.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/global.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/jquery.smart-form.js"></script>
<script type="text/javascript" src="/WEB-RES/css/layer/js/loading-overlay.min.js"></script>

<style type="text/css">
.header {
    background: url(/WEB-RES/img/bg_banner.jpg) no-repeat 0px 0px;
    height: 240px;
    width: 100%;
    color: #fff;
    text-align: center;
    background-size: cover;
}
.header h1 {
    font-size: 40px;
    font-weight: 400;
    padding: 55px 0 0 0;
}
.header h3 {
    font-size: 20px;
    font-weight: 400;
    padding: 2px 10px;
}
.help {
    background: url(/WEB-RES/img/contact3.png) no-repeat;
    padding: 2px 0 0 24px;
    color: orange;
}
.error{
    font-size: 12px;
    color: #0097E9;
    left: 5px;
    top: 28px;
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

<script type="text/javascript">
var iSubmit = false;
var companyCode = null;
var provinceBM = null;
var cityBM = null;
var regionBM = null;

$(document).ready(function() {
    $("#customerIdInput").focus();
    //从cookie加载燃气公司信息
    companyCode = $.cookie('company');
    if(companyCode==null) return;
    provinceBM = $.cookie('province');
    cityBM = $.cookie('city');
    regionBM = $.cookie('region');
});

$(document).keydown(function(event) {
    if(event.keyCode == 13) { //回车等于提交
        $('#loginHLEPay').click();
    }
});

function toPost(postData) {
    var target = $('#loginBox');
    var postData = eval(postData);
    var companyCode = postData.company;
    var customerId = customerIdInput.value;
    if(iSubmit) {
        $('#errorTxt').html('请勿重复提交！');
        $('#errorMsg').show();
        return;
    }
    if(companyCode == null | companyCode == '') {
        $('#errorTxt').html('请选择您的燃气公司！');
        $('#errorMsg').show();
        return;
    }
    if(customerId == null | $.trim(customerId) == '') {
        $('#errorTxt').html('请填写燃气用户编号！');
        $('#errorMsg').show();
        return;
    }
//     if(!/^[0-9]+$/.test($.trim(customerId))) {
//         $('#errorTxt').html('请核对并输入数字编号！');
//         $('#errorMsg').show();
//         return;
//     }
    iSubmit = true;
    target.loadingOverlay();
    $.ajax({
        url : '/gas1pay/webLogon.do',
        type : 'post',
        data : {
            companyCode: companyCode,
            customerId : customerId
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            if(target.hasClass('loading')) {
                target.loadingOverlay('remove');
            }
            $('#errorTxt').html('连接燃气公司系统失败，请稍后再试！错误响应码：' + XMLHttpRequest.status);
            $('#errorMsg').show();
            iSubmit = false;
        },
        success : function(result, textStatus, jqXHR) {
            $.cookie('company', postData.company, {
                expires : 30
            });
            $.cookie('province', postData.province, {
                expires : 30
            });
            $.cookie('city', postData.city, {
                expires : 30
            });
            $.cookie('region', postData.region, {
                expires : 30
            });

            if(target.hasClass('loading')) {
                target.loadingOverlay('remove');
            }
            
            if(result.code == 0) {
                var customerInfo = $.parseJSON(result.data);
                
                $.confirm({
                    title : '信息确认',
                    content : '<font color=red>' + customerInfo.customer[0].adr + '</font> 的 <font color=red>' + customerInfo.customer[0].yhname + '</font> 用户，请确认您的个人信息，以免费用充入他户！',
                    //autoClose : 'cancel|30000',
                    confirmButtonClass : 'btn-danger',
                    confirmButton : '确认',
                    cancelButton : '取消',
                    confirm : function() {
                        document.write("<form action='/gas1pay/webPayBill.do' name='formx1' style='display:none' enctype='multipart/form-data'></form>");

                        var input_info = document.createElement("input");
                        input_info.type = "text";
                        input_info.name = "customerInfo";
                        input_info.value = result.data;

                        document.formx1.appendChild(input_info);
                        document.formx1.method = "POST";
                        document.formx1.charset = "UTF-8";
                        document.formx1.submit();
                    },
                    cancel : function() {
                        return;
                    }
                });
            } else if(result.code == -1) {
                $('#errorTxt').html(result.message);
                $('#errorMsg').show();
            }

            iSubmit = false;
        }
    });
}
</script>
</head>

<body>
<div class="header">
    <h1>海力气付通：网上燃气缴费，即时充值到表</h1>
    <h3>通过海力气付通平台，足不出户、无须排队，即可进行便捷的燃气缴费、欠费查询和历史账单查询</h3>
</div>
<div class="theCenterBox" style="margin-top:40px">
<div class="theLoginBootstrap">
<div class="theLoginArea" id="loginBox">
    <p style="position:relative">
        <img src="/WEB-RES/img/gas_product.jpg"/><label>请选择燃气公司：</label>
    </p>
    <div class="theProvince" style="padding:15px">
        <form action="#" id="formContainer" class="form form-horizontal"></form>
    </div>

<script type="text/javascript">
$(function() {
    var eles = [[{
        ele: {
            type: 'select', name: 'province', title: '省:', withNull: true,
            items: [
                { text: '四川', value: 'SiChuan', extendAttr: { id: 1000 } }
            ]
        }
    },{
        ele: {
            type: 'select', name: 'city', title: '市:', withNull: true,
            items: [
                { text: '成都', value: 'ChengDu', extendAttr: { id: 1000001, parentId: 1000 } },
                { text: '绵阳', value: 'MianYang', extendAttr: { id: 1000002, parentId: 1000 } },
                { text: '自贡', value: 'ZiGong', extendAttr: { id: 1000003, parentId: 1000 } }
            ]
        }
    },{
        ele: {
            type: 'select', name: 'region', title: '区/县:', withNull: true,
            items: [
                { text: '成华区', value: 'CH', extendAttr: { id: 1000001001, parentId: 1000001 } },
                { text: '锦江区', value: 'JJ', extendAttr: { id: 1000001002, parentId: 1000001 } },
                { text: '青羊区', value: 'QY', extendAttr: { id: 1000001003, parentId: 1000001 } },
                { text: '金牛区', value: 'JN', extendAttr: { id: 1000001004, parentId: 1000001 } },
                { text: '武侯区', value: 'WH', extendAttr: { id: 1000001005, parentId: 1000001 } }
            ]
        }
    },{
        ele: {
            type: 'select', name: 'company', title: '公司:', withNull: true,
            items: [
                { text: '海力燃气公司', value: '0000000000', extendAttr: { id: 1000001001001, parentId: 1000001001 } },
                { text: '慧众云平台', value: '0000000001', extendAttr: { id: 1000001001002, parentId: 1000001001 } }
            ]
        }
    }]];
    
    var bsForm = new BSForm({ eles: eles, autoLayout: true }).Render('formContainer', function(sf) {
        //编辑页面的绑定
        sf.InitFormData({
            province: provinceBM,
            city: cityBM,
            region: regionBM,
            company: companyCode
        });
        //必须先赋值再生成插件
        global.Fn.CascadeSelect({
            targets: ['province', 'city', 'region', 'company'],
            primaryKey: 'data-id',
            relativeKey: 'data-parentId'
        });
    });

    $("#loginHLEPay").bind('click', function() {
        var postData = bsForm.GetFormData();
        toPost(postData); 
    });
});
</script>

    <p style="position: relative;margin-top:20px">
        <img src="/WEB-RES/img/user_multiple.png"/><label>填写燃气用户号：</label>
        <span id="errorMsg" style="display:none; width:600px; float:right"><img src="/WEB-RES/img/icon_error.png" align="middle"><span id="errorTxt" style="font-size:14px;color:red"></span></span>
    </p>
    <div class="input-group input-group-lg">
        <input type="text" id="customerIdInput" class="form-control" autocomplete="on" placeholder="在此输入您的燃气用户编号" style="width:500px" />
        <span style="float:left; width:80px">&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</span>
        <input type="button" class="btn btn-primary btn-lg" id="loginHLEPay" value="登录气付通平台" />
    </div>
    <br/><br/><br/>
</div>
</div>
</div>

<div class="footer">
    <p class="copyright_banquan">©2005～2017&emsp;海力智能科技股份有限公司&emsp;&emsp;ICP备案号：蜀ICP备50263615号</p>
</div>
</body>
</html>
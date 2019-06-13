<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户燃气缴费信息验证</title>
<link rel="shortcut icon" href="/WEB-RES/img/HL2.ico" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/sui.css" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/public.css" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/layer/css/layer.css" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/lib/bootstrap-3.2.0/bootstrap-core/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/WEB-RES/css/templatemo-style.css" />
<script type="text/javascript" src="/WEB-RES/lib/jquery-easyui-1.4.4/jquery.min.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/jquery.cookie.js"></script>
<script type="text/javascript" src="/WEB-RES/js/hlauth.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/common.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/sui.min.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/bootstrap-3.2.0/bootstrap-core/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/WEB-RES/css/layer/js/loading-overlay-order.min.js"></script>
<script type="text/javascript" src="/WEB-RES/lib/bootstrap-3.2.0/bootstrap-table/js/bootstrap-table.js"></script>
<script type="text/javascript" src="/WEB-RES/js/paybill.js"></script>

<style type="text/css">
.table th, .table td {
    text-align: center; 
    height: 32px;
}
.alert-warning h3 {
    display: inline;
    color: green;
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
.checkbar {
    float: right;
    text-align: left;
    padding: 20px;
}
</style>
</head>

<body>
<div class="navbar navbar-default navbar-fixed-top">
    <div class="navbar-brand" style="font-size:14px;color:#fff;float:left">【${companyName}】${customerName} 用户您好！请确认个人信息，填写缴费金额并验证提交</div>
    <span class="navbar-brand" style="font-size:14px;float:right">[<a href="javascript:void(0)" onclick="window.location.href='/'" target="_self">退出</a>]</span>
</div>

<div style="margin:40px">
    <input id="companyCode" value=${companyCode} type="hidden" /><input id="customerCode" value=${customerCode} type="hidden"/><input id="payInfo" value=${payInfo} type="hidden" />
</div>

<div id="customer" style="margin: 20px">
    <table class="table table-bordered">
        <tr>
        <td><h3>客户姓名：${customerName}</h3></td>
        <td><h3>客户编号：${customerCode}</h3></td>
        <td><h3>账户余额：${bcye}&nbsp;元</h3></td>
        </tr>
    </table>
</div>

<script type="text/javascript">
$(function() {
    $("#mememe").click(function() {
        $("#arrearage").hide();
        $("#abhistory").hide();
    });
});
</script>
<div id="arrearage" style="margin:20px; display:none">
<div class="alert alert-warning">
    <strong>您的燃气表已欠费</strong>，欠费总金额：<h3>${abstractMoney}</h3>&nbsp元<a href="#" id="mememe" class="close" data-dismiss="alert">&times;</a>
</div>
<h3>历史欠费明细：</h3>
<input value=${historyAbInfo} id="input_historyAbInfo" style="display:none" />
<table class="table table-bordered" data-toggle="table" id="abstractTable">
    <thead>
        <tr>
            <th data-field="month">欠费月份</th>
            <th data-field="abstractFee">欠费金额</th>
            <th data-field="beginVolume">当月起读数(方)</th>
            <th data-field="endVolume">当月止读数(方)</th>
            <th data-field="addressDetail">详细地址</th>
        <tr>
    </thead>
</table>
<script type="text/javascript">
    var $abstractTable = $('#abstractTable');
    var ss = $('#input_historyAbInfo').val();
    var data_tmp = eval(ss);
    var rows1 = [];
    for(var i=0; i<data_tmp.length; i++) {
        rows1.push({
            month : data_tmp[i].month,
            abstractFee : data_tmp[i].abstractFee,
            beginVolume : data_tmp[i].beginVolume,
            endVolume : data_tmp[i].endVolume,
            addressDetail : data_tmp[i].addressDetail
        });
    }
    $(function() {
        $abstractTable.bootstrapTable('load', rows1);
        $abstractTable.bootstrapTable('hideLoading');
    });
</script>
</div>
<script type="text/javascript">
    var t = "${abstractMoney}";
    if(parseFloat(t) < 0.00) {
        $('#arrearage').show();
        $('#abhistory').show();
    }
</script>

<script type="text/javascript">
$(function() {
    $("#kekeke").click(function() {
        $("#historydiv").hide();
        $("#payhistory").hide();
    });
    $("#showHistory").click(function() {
        $("#historyBlock").toggle(1000);
    });
});
</script>
<div id="historydiv" style="margin:20px">
<div id="historyOneRecords" style="display:none"><strong>${historyOne}</strong></div>
<div id="historyThreeRecords" style="display:none"><strong>${historyThree}</strong></div>
<div class="alert alert-warning"><a style="cursor:pointer" id="showHistory"><h3>历史缴费记录：</h3><a><a id="kekeke" class="close" data-dismiss="alert">&times;</a></div>
<div id="historyBlock" style="display:none">
<ul class="nav nav-tabs">
    <li  class="active"><a href="#one" data-toggle="tab">近一个月</a></li>
    <li><a href="#three" data-toggle="tab">近三个月</a></li>
</ul>
<div id="myTabContent" class="tab-content">
<div id="one" class="tab-pane fade in active">
    <table id="onetable" class="table table-bordered" data-toggle="table" data-pagination="true">
        <thead>
            <tr>
                <th data-field="occurTime">发生时间</th>
                <th data-field="fee">缴费金额</th>
            </tr>
        </thead>
    </table>
</div>
<div id="three" class="tab-pane fade">
    <table id="threetable" class="table table-bordered" data-toggle="table" data-pagination="true">
        <thead>
            <tr>
                <th data-field="occurTime">发生时间</th>
                <th data-field="fee">缴费金额</th>
            </tr>
        </thead>
    </table>
</div>
</div>
</div>
</div>
<script type="text/javascript">
$(function() {
    var $onetable = $('#onetable');
    var ss = document.getElementById("historyOneRecords").innerText;
    var data_tmp = eval(ss);

    var rows2 = [];
    for(var i=0; i<data_tmp.length; i++) {        
        rows2.push({
            occurTime : data_tmp[i].createTime,
            fee : data_tmp[i].payFee
        });
    }
    
    var $threetable = $('#threetable');
    ss = document.getElementById("historyThreeRecords").innerText;
    var data_tmp = eval(ss);
    
    if(data_tmp.length==0){
        $("#historydiv").hide();
        $("#payhistory").hide();
    }

    var rows3 = [];
    for(var i=0; i<data_tmp.length; i++) {
        rows3.push({
            occurTime : data_tmp[i].createTime,
            fee : data_tmp[i].payFee
        });
    }
    
    $onetable.bootstrapTable('load', rows2);
    $onetable.bootstrapTable('hideLoading');
    
    $threetable.bootstrapTable('load', rows3);
    $threetable.bootstrapTable('hideLoading');
});
</script>

<div class="sui-msg msg-block msg-tips" style="margin-bottom: 10px;">
    <div class="msg-icon"></div>
    <div class="msg-con">点击气表的【充值】栏并填写金额；对于卡表类燃气表，充值额不能低于其欠费额；充值单位为“元”，最小额度为“分”</div>
</div>

<table id="tt"></table>

<script type="text/javascript">
var $table = $('#tt'), $remove = $('#remove'), selections = [];
var ss = $('#payInfo').val();
var data_tmp = eval(ss);

var rows4 = [];
for(var i=0; i<data_tmp.length; i++) {
    rows4.push({
        addressDetail : data_tmp[i].addressDetail,
        addressID : data_tmp[i].addressID,
        currentMeterFee : data_tmp[i].currentMeterFee,
        meterName : data_tmp[i].meterName,
        meterSerialNo : data_tmp[i].meterSerialNo,
        meterType : data_tmp[i].meterType
    });
}
function initTable() {
    $table.bootstrapTable({
        //url: '/payment/userPayinfo.do',
        columns: [[{
            field : 'addressID',
            title : '地址序号',
            footerFormatter : totalNameFormatter,
            align : 'center',
            visible:false
        },{
            field : 'meterType',
            title : '表类型编码',
            footerFormatter : totalNameFormatter,
            align : 'center',
            visible:false
        },{
            field : 'addressDetail',
            title : '详细地址',
            footerFormatter : totalNameFormatter,
            align : 'center',
        },{
            field : 'meterSerialNo',
            title : '气表编号',
            footerFormatter : totalNameFormatter,
            align : 'center',
        },{
            field : 'meterName',
            title : '气表类型',
            footerFormatter : totalNameFormatter,
            align : 'center',

        },{
            field : 'currentMeterFee',
            title : '气表余额',
            footerFormatter : totalNameFormatter,
            align : 'center',
        },{
            field : 'userTotalChargeFee',
            title : '【充值】',
            formatter : function(value, row, index) {
                var e = '0.00';
                return e;
            },
            align : 'center',
            cache : false,
            editable : {
                type : 'text',
                title : '充值金额',
                validate : function(value) {
                    var regular = /\s+/g;
                    if(regular.test(value)) {
                        return '输入不能带空格';
                    }
                    value = $.trim(value);
                    var reg = /^[0-9]{1}\d*(\.{0,1}\d{0,2})$/;
                    if(!reg.test(value)) {
                        return '请输入不小于0的数字格式';
                    }
                    if(value<0) return '金额不能小于0';

                    var data = $table.bootstrapTable('getData'),
                    index = $(this).parents('tr').data('index');
                    $.each(data, function(i, row) {
                        if(i == index) {
                            meterFee = row.currentMeterFee;
                        }
                    });

                    if(parseFloat(meterFee) < 0) {
                        isSmallValue = -parseFloat(meterFee) > parseFloat(value) ? true : false;
                        if(isSmallValue) {
                            return '普表充值金额必须大于或等于欠费金额！';
                        }
                    }

                    totalMoney(data, index, value);
                    return '';
                }
            }
        }]]
    });

    $table.bootstrapTable('load', rows4);
    $table.bootstrapTable('hideLoading');
}

function getIdSelections() {
    return $.map($table.bootstrapTable('getSelections'), function(row) {
        return row.id;
    });
}

function responseHandler(res) {
    $.each(res.rows, function(i, row) {
        row.state = $.inArray(row.id, selections) !== -1;
    });
    return res;
}

function detailFormatter(index, row) {
    var html = [];
    $.each(row, function(key, value) {
        html.push('<p><b>' + key + ':</b> ' + value + '</p>');
    });
    return html.join('');
}

function operateFormatter(value, row, index) {
    return [
            '<a class="like" href="javascript:void(0)" title="Like">',
            '<i class="glyphicon glyphicon-heart"></i>',
            '</a>  ',
            '<a class="remove" href="javascript:void(0)" title="Remove">',
            '<i class="glyphicon glyphicon-remove"></i>', '</a>'
            ].join('');
}

window.operateEvents = {
    'click .like' : function(e, value, row, index) {
        alert('You click like action, row: ' + JSON.stringify(row));
    },
    'click .remove' : function(e, value, row, index) {
        $table.bootstrapTable('remove', {
            field : 'id',
            values : [ row.id ]
        });
    }
};

function totalTextFormatter(data) {
    return 'Total';
}

function totalNameFormatter(data) {
    return data.length;
}

function totalPriceFormatter(data) {
    var total = 0;
    $.each(data, function(i, row) {
        total += +(row.price.substring(0));
    });

    return '$' + total;
}

$(function() {
    var scripts = [
        location.search.substring(1) || '/WEB-RES/lib/bootstrap-3.2.0/bootstrap-table/js/bootstrap-table.js',
            '/WEB-RES/lib/bootstrap-3.2.0/bootstrap-table/js/extensions/editable/bootstrap-table-editable.js',
            '/WEB-RES/lib/bootstrap-3.2.0/bootstrap-editable/js/bootstrap-editable.js'
    ], eachSeries = function(arr, iterator, callback) {
        callback = callback || function() {};
        if(!arr.length) {
            return callback();
        }
        var completed = 0;
        var iterate = function() {
            iterator(arr[completed], function(err) {
                if(err) {
                    callback(err);
                    callback = function() {};
                } else {
                    completed += 1;
                    if(completed >= arr.length) {
                        callback(null);
                    } else {
                        iterate();
                    }
                }
            });
        };
        iterate();
    };

    eachSeries(scripts, getScript, initTable);
});

function getScript(url, callback) {
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.src = url;
    var done = false;
    // Attach handlers for all browsers
    script.onload = script.onreadystatechange = function() {
        if(!done && (!this.readyState || this.readyState == 'loaded' || this.readyState == 'complete')) {
            done = true;
            if(callback) {
                callback();
            }
            // Handle memory leak in IE
            script.onload = script.onreadystatechange = null;
        }
    };
    head.appendChild(script);
    return undefined;
}
</script>

<div class="checkbar">
    <h3 class="money" style="float:right">待付款：<strong id="readyPayment" style="color:red">0.00</strong>&nbsp;元</h3>
    <form class="form-inline" id="payCheckForm">
        <div class="form-group">
            <img id="captchaImg" style="cursor:pointer; width:70px; height:22px" onclick="refresh()"/>：验证码：
            <input id="captchaInput" name="captchaInput" class="form-control" style="width:85px; height:24px; float:right" maxlength="4" onkeypress="if(event.keyCode==13) {return false;}" />
        </div>
    </form>
    <p id="errorMsg" style="color:#ff0000; text-align:center; display:block"></p>
    <div id="submitChkBtn"><input type="button" class="btn btn-primary btn-lg" id="submitCheck" value="提交付款验证" /></div><br/><br/>
</div>

<div class="footer">
    <p class="copyright_banquan">©2005～2017&emsp;海力智能科技股份有限公司&emsp;&emsp;ICP备案号：蜀ICP备50263615号</p>
</div>
</body>
</html>
$(document).ready(function() {
    $("#captchaInput").val("");
    $("#captchaInput").focus();
    reloadCaptcha();
    $('#submitCheck').unbind('click').click(function() {
        doPaymentPost();
    });
});


function refresh() {
    reloadCaptcha();
    $("#errorMsg").hide();
}
//刷新验证码
function reloadCaptcha() {
    $("#captchaImg").attr("src", "/gas1pay/captcha.do?date=" + new Date());
}


function historyQuery(txt) {
    return '<ul class="nav nav-tabs">'+
            '<li  class="active"><a href="#one" data-toggle="tab">近一个月</a></li>'+
            '<li><a href="#three" data-toggle="tab">近三个月</a></li>'+
            '</ul>'+
            '<div id="myTabContent" class="tab-content">'+
            '<div class="tab-pane fade in active" id="one">'+
            '<table class="table table-bordered"><tr><td>发生日期</td><td>发生日期</td><td>充值金额</td><td>购买放量</td><td>充值状态</td></tr></table>'+
            '</div>'+
            '<div class="tab-pane fade" id="three">'+
            '<table class="table table-bordered"><tr><td>发生日期</td><td>发生日期</td><td>充值金额</td><td>购买放量</td><td>充值状态</td></tr></table>'+
            '</div>'+
            '</div>';
}


/**
 * 生成待支付订单
 */
var iSubmit = false;

function doPaymentPost(customerCode) {
    if(iSubmit == true) {
        $("#errorMsg").text("请勿重复提交订单！");
        $("#errorMsg").show();
        return;
    }
    var target = $('#submitChkBtn');
    var datagrid_info = $('#tt').bootstrapTable('getData');
    var companyCode = $('#companyCode').val();
    var customerCode = $('#customerCode').val();
    
    var datagrid_field_info = [];
    
    for(var payInfo in datagrid_info) {
        datagrid_field_info.push({
                '"meterType"': ("\""+datagrid_info[payInfo].meterType+"\""),
                '"addressID"' : ("\""+datagrid_info[payInfo].addressID+"\""),
                '"addressDetail"' : ("\""+datagrid_info[payInfo].addressDetail+"\""),
                '"meterSerialNo"': ("\""+datagrid_info[payInfo].meterSerialNo+"\""),
                '"currentMeterFee"' : ("\""+datagrid_info[payInfo].currentMeterFee+"\""),
                '"userTotalChargeFee"': ("\""+datagrid_info[payInfo].userTotalChargeFee+"\"")
        });
    }
    
    var orderForm = obj2Str(datagrid_field_info);
    var totalMoney = $('#readyPayment').html();
    var payCheckCode = $("#payCheckForm").serialize();
    if(parseFloat(totalMoney)<=0.00) {
        $("#errorMsg").text("请给您的气表充值！");
        $("#errorMsg").show();
        return;
    }
    target.loadingOverlay();
    
    iSubmit = true;
    $.ajax({
        url : "/gas1pay/webPayCheck.do",
        data : {
            orderForm : orderForm,
            totalMoney : totalMoney,
            payCheckCode: payCheckCode,
            companyCode:  companyCode,
            customerCode: customerCode
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            target.loadingOverlay('remove');
            $("#errorMsg").text("生成订单异常，请联系管理员！错误响应码：" + XMLHttpRequest.status);
            $("#errorMsg").show();
            iSubmit = false;
        },
        success : function(result) {
            $("#errorMsg").hide(); 
            target.loadingOverlay('remove');
            if(result.code == 0) {
                var orderId = result.data;
                var payWaysUrl = "/gas1pay/webPayWays.do?orderId="+orderId;
                location = encodeURI(payWaysUrl);
            } else if(result.code==-2) {
                $("#errorMsg").text(result.message);
                $("#errorMsg").show();
            } else if(result.code==601) {
                $("#errorMsg").text(result.message);
                $("#errorMsg").show();
                location = "/index.jsp";
            } else {
                $("#errorMsg").text(result.message);
                $("#errorMsg").show();
                $("#captchaInput").focus();
            }
            iSubmit = false;
        }
    });
}


function getvalue(name) {
    var str = window.location.search;

    if(str.indexOf(name)!=-1) {
        var pos_start = str.indexOf(name)+name.length+1;
        var pos_end = str.indexOf("&",pos_start);
        if(pos_end==-1) {
            return str.substring(pos_start);
        } else {
            alert("该值不存在！");
        }
    }
}


function totalMoney(data, index, fee) {
    var reg=/^[0-9]{1}\d*(\.{0,1}\d{0,2})$/;
    var totalValue = 0;
    $.each(data, function(i, row) {
        if(i==index) return;
        var charge_value = $.trim(row.userTotalChargeFee);
        if(!reg.test(charge_value)) return;
        totalValue += +charge_value;
    });
    totalValue += +fee;
    $('#readyPayment').html(totalValue.toFixed(2));
}


function rowformater(value,row,index) {
    var str = '<input type="button" style="width:60px" value="确认" name="kao" onclick="rowAccept()">';
    return str;
}


function rowAccept() {
    if(endEditing()) {
        $('#tt').datagrid('acceptChanges');
        totalMoney();
    }
}


function endEditing() {
    if(editIndex == undefined) {
        editIndex = undefined;
        return true;
    }
    
    if($('#tt').datagrid('validateRow', editIndex)) {
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}


function obj2Str(o) {
    var r = [];
    if(typeof o=="string" || o==null) {
        return o;
    }
    if(typeof o=="object") {
        if(!o.sort) {
            r[0] = "{";
            for(var i in o) {
                r[r.length] = i;
                r[r.length] = ":";
                r[r.length] = obj2Str(o[i]);
                r[r.length] = ",";
            }
            r[r.length-1]="}";
        } else {
            r[0] = "[";
            for(var i=0; i<o.length; i++) {
                r[r.length] = obj2Str(o[i]);
                r[r.length] = ",";
            }
            r[r.length-1] = "]";
        }
        return r.join("");
    }
    return o.toString();
}

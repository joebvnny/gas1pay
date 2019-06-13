var orderId = undefined;
var customerId = undefined;
var meterId = undefined;
var orderFee = undefined;
var companyCode = undefined;

$(document).ready(function() {
    
    $("#unionpayClose").click(function() {
        window.location.reload();
    });

    $("#weixinpayClose").click(function() {
        window.location.reload();
    });
    
    orderId = decodeURI(getValue('orderId'));
    $.ajax({
        url: "/gas1pay/getOrderInfo.do?orderId="+orderId,
        type: "post",
        dataType: "json",
        cache: false,
        async: false,
        success: function(result) {
            if(result.code==0) {
                var order = result.data;
                customerId = order.customerId;
                meterId = order.meterId;
                orderFee = order.chargeFee;
                companyCode = order.companyCode;
            }
        }
    });
    $('#amount').html(orderFee);
    $('#amount').css({"font-size":"15px","font-weight":"bold","color":"#f60"});
    $('#amount2').html('<font class="cOrange" style="font-size:18px" id="amount2">'+orderFee+'</font>'+'&nbsp;&nbsp;元');

});


function imageSel(theVal) {
    var checkId = "#gateway_" + theVal;
    $(checkId)[0].checked = true;
    $('#pay_gateway').val(theVal);
}

function setSelValue(theVal) {
    $('#pay_gateway').val(theVal);
}


//非空验证
function checks() {
    if($("#pay_gateway").val()=="") {
        alert("请选择支付方式！");
        return false;
    } else {
        return true;
    }
}


function getValue(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if(r != null) {
        return unescape(r[2]);
    }
    return null;
}


function gatewayPay() {
    if(checks()) {
        var payGateway = $('#pay_gateway').val();
        
        if(payGateway=='AL') {
            $.ajax({
                url : "/gas1pay/webALPay.do",
                type : 'post',
                data : "orderId="+orderId+"&orderFee="+orderFee+"&companyCode="+companyCode,
                dataType : 'html',
                error : function(xhr, status, error) {
                    if(xhr.status == 404) {
                        alert("本商户不支持支付宝，请选择其它方式支付！");
                    }
                },
                success : function(result) {
                    document.write(result);
                    document.close();
                }
            });
        }
        
        else if(payGateway=='WX') {
            $("#ztky_mask").css({
                display : "block",
                height : $(document).height()
            });
            $("#weixin_pay_qr").css({
                display : "block"
            });
            
            $('#wexin_total').html('<font style="font-size:26px;font-weight:700;color:#f60">'+orderFee+'</font>'+'&nbsp;&nbsp;元');
            
            $.ajax({
                url: "/gas1pay/webWXPay.do?orderId="+orderId+"&orderFee="+orderFee+"&companyCode="+companyCode,
                beforeSend: function() {
                    $('#qrcode-waiting').html('<div class="qrcode-img-area"><div class="ui-loading qrcode-loading">加载中…</div></div>');
                },
                error : function(xhr, status, error) {
                    if(xhr.status == 404) {
                        alert("本商户不支持微信支付，请选择其它方式支付！");
                        window.location.reload();
                    }
                },
                success: function(result) {
                    $('#qrcode-waiting').remove();
                    $('#qr_code').qrcode(result.data);
                }  
            });  
            
            setInterval(function() {
                $.ajax({
                    url: "/gas1pay/refreshOrderStatus.do?orderId="+orderId,
                    type: "post",
                    dataType: "json",
                    cache: false,
                    success: function(result) {
                        if(result.code==1) {
                            location.replace("/gas1pay/wuReturnPage.do?orderId="+orderId);
                        }
                    }
                });
            }, 3000);
        }
        
        else if(payGateway=='UN') {
            $("#ztky_mask").css({
                display: "block",
                height: $(document).height()
            });
            $("#waitpay_online").css({
                display: "block"
            });
            
            window.open("/gas1pay/webUNPay.do?orderId="+orderId+"&orderFee="+orderFee+"&companyCode="+companyCode);
            
            setInterval(function() {
                $.ajax({
                    url: "/gas1pay/refreshOrderStatus.do?orderId="+orderId,
                    type: "post",
                    dataType: "json",
                    cache: false,
                    success: function(result) {
                        if(result.code==1) {
                            location.replace("/gas1pay/wuReturnPage.do?orderId="+orderId);
                        }
                    }
                });
            }, 3000);
        }
        
    }
}


function hideInfo(){
    $("#order_detail_div").hide();
}
function showInfo() {
    $("#order_detail_div").show();
    $("#datagrid_orderdetail").datagrid({
//        url : '/gas1pay/getOrderInfo.do?orderID='+orderId,
        data : [
            {customerId: customerId, meterId: meterId, orderFee: orderFee}
        ],
        columns: [[{
            title : '客户编号',
            field : 'customerId',
            width : 120,
            align : 'center'
        },{
            title : '气表编号',
            field : 'meterId',
            width : 120,
            align : 'center'
        },{
            title : '充值金额',
            field : 'orderFee',
            width : 120,
            align : 'center'
        }]]
    });
}

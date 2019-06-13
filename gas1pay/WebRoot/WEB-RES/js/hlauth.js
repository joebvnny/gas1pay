/**
 * 生成授权认证头信息
 */
function genAuthHead(userid, passwd) {
    var authToken = $.cookie('authToken');
    if(authToken==null) {
        $.ajax({
            type: "POST",
            url : "http://www.hl-epay.com/auth/authentication.do",
            async: false,
            dataType: "json",
            data : {
                userid: userid,
                passwd: passwd
            },
            success : function(result) {
                authToken = result.data.token;
                $.cookie('authToken', authToken, {
                    expires : 7
                });
            }
        });
    }
    return "HLEPAY." + authToken;
}

//$.ajax({
//    url : "/path/resourceNeed2Auth",
//    headers: {
//        "Authorization": genAuthHead("useridXXX", "passwdYYY")
//    },
//    ...
//});
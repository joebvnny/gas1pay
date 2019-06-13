<%@page import="java.io.PrintWriter"%>
<%@page import="com.hl.web.util.WebUtil"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
Log log = LogFactory.getLog("error_jsp");
Object errorCode = request.getAttribute("javax.servlet.error.status_code");
Object errorUri = (request.getAttribute("javax.servlet.error.request_uri"));
String url = errorUri.toString().toLowerCase();
Throwable error = null;
if(url.endsWith(".jsp") || url.endsWith(".do")) {
    error = (Throwable)(request.getAttribute("javax.servlet.error.exception"));
    if(error == null) {
        error = (Throwable)(request.getAttribute("exception"));
        log.error(WebUtil.getClientIp(request) + " 访问 " + errorUri + " 异常！errorCode=" + errorCode);
    } else {
        log.error(WebUtil.getClientIp(request) + " 访问 " + errorUri + " 异常！errorCode=" + errorCode, error);
    }
}
%>
<html>
<head>
<title>出错了</title>
<link rel="stylesheet" href="/WEB-RES/lib/bootstrap-3.2.0/bootstrap-core/css/bootstrap.min.css">
<link rel="stylesheet" href="/WEB-RES/css/ace.min.css">
<link rel="stylesheet" href="/WEB-RES/css/font-awesome.min.css">
</head>
<body>
    <div class="col-xs-12">
        <div class="error-container">
            <div class="well">
                <h1 class="grey lighter smaller">您好，出了一些问题！</h1>
                <hr>
                <h3 class="lighter smaller">后台处理异常，请联系运维管理人员，或稍后再试！</h3>
                <div class="space"></div>
                <div>
                    <h4 class="lighter smaller">具体错误内容如下：</h4>
                    <ul class="list-unstyled spaced inline bigger-110 margin-15">
                        <li>
                            <div>
                            <%
                                if(error != null) {
                                    error.printStackTrace(new PrintWriter(out));
                                }
                            %>
                            </div>
                        </li>
                    </ul>
                </div>
                <hr>
                <div class="space"></div>
                <div class="center">
                    <a href="/" class="btn btn-primary">回到首页</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
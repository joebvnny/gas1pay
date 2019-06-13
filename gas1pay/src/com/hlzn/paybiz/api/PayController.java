package com.hlzn.paybiz.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hlzn.common.CommonUtil;
import com.hlzn.common.IdGenerator;
import com.hlzn.common.JsonModelAndView;
import com.hlzn.common.RespBean;
import com.hlzn.entity.CompanyInfo;
import com.hlzn.entity.GasPayOrder;
import com.hlzn.paybiz.biz.PayConfig;
import com.hlzn.paybiz.biz.PayException;
import com.hlzn.paybiz.biz.PayService;
import com.hlzn.paybiz.biz.PayThread;
import com.hlzn.payway.alpay.util.AlipayNotify;
import com.hlzn.payway.alpay.util.AlipaySubmit;
import com.hlzn.payway.unpay.SDKUtil;
import com.hlzn.payway.unpay.UnionBase;
import com.hlzn.payway.wxpay.util.Signature;
import com.hlzn.payway.wxpay.util.WXPayMD5;
import com.hlzn.webpay.epay.CustomerInfo;
import com.hlzn.webpay.epay.CustomerPayment;
import com.hlzn.webpay.epay.HistoryAbInfo;
import com.hlzn.webpay.epay.HistoryPayInfo;
import com.hlzn.webpay.epay.PayInfo;
import com.hlzn.entity._GasPayConst;

/**
 * 气付通通讯网关（支持前端WEB/APP/WX方式进行REST访问）
 */
@Controller
@RequestMapping("/gas1pay")
public class PayController {
    
    private static final Logger logger = Logger.getLogger(PayController.class);
    
    @Autowired
    private PayService payService;

    
    /**
     * 获取公司商户信息列表，可传companyCode参数返回特定公司商户信息
     */
    @RequestMapping("/getCompanyInfo.do")
    public ModelAndView getCompanies(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        RespBean resp = new RespBean(0, "SUCCESS", null);
        String code = request.getParameter("companyCode");
        if(StringUtils.isNotEmpty(code)) {
            CompanyInfo companyInfo = payService.getCompanyByCode(code);
            resp.setData(companyInfo);
        } else {
            List<CompanyInfo> companies = payService.listCompanies();
            resp.setData(companies);
        }
        return new JsonModelAndView(resp);
    }
    
    
    /**
     * WEB校验燃气公司和客户号并返回相关信息
     * HTTP传入请求参数companyCode,customerId
     */
    @RequestMapping(value="/webLogon.do", method=RequestMethod.POST)
    public JsonModelAndView webLogon(HttpServletRequest request, HttpServletResponse response, Model model) {
        RespBean resp = new RespBean(0, "SUCCESS", null);
        
        String companyCode = request.getParameter("companyCode");
        String customerId = request.getParameter("customerId");
        
        CompanyInfo companyInfo = payService.getCompanyByCode(companyCode);
        
        if(companyInfo == null) {
            resp.setCode(-1);
            resp.setMessage("燃气公司信息有误！");
            return new JsonModelAndView(resp);
        }
        
        request.getSession().setAttribute("companyInfo", companyInfo);
        
        GasGateway gateway = new GasGateway(companyInfo.getGatewayUrl(), "hlUsername", "hlPasword");
        String customerInfo = gateway.queryCustomerInfoById(customerId);
        
        if(StringUtils.isEmpty(customerInfo)) {
            resp.setCode(-1);
            resp.setMessage("连接燃气公司失败！");
            return new JsonModelAndView(resp);
        }
        
//        String clientIp = CommonUtil.getClientIp(request);
        JSONObject jsonCompanyInfo = JSONObject.fromObject(companyInfo);
//        logger.info("[" + clientIp + "]companyInfo=" + jsonCompanyInfo.toString());
        JSONObject jsonCustomerInfo = JSONObject.fromObject(customerInfo);
//        logger.info("[" + clientIp + "]customerInfo=" + customerInfo);
        JSONArray customerArray = (JSONArray)jsonCustomerInfo.get("customer");
        
        if(customerArray.size() == 0) {
            resp.setCode(-1);
            resp.setMessage("查无此燃气用户，请重新输入！");
            return new JsonModelAndView(resp);
        }
        
        jsonCustomerInfo.putAll(jsonCompanyInfo);
        resp.setData(jsonCustomerInfo.toString());
        
        return new JsonModelAndView(resp);
    }
    
    
    /**
     * 燃气缴费订单页
     */
    @RequestMapping(value="/webPayBill.do", method=RequestMethod.POST)
    public String webPayBill(HttpServletRequest request, HttpServletResponse response, Model model) {
        String customerInfo = request.getParameter("customerInfo");
        JSONObject jsonCustomerInfo = JSONObject.fromObject(customerInfo);
        
        Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
        classMap.put("customer", CustomerInfo.class);
        classMap.put("historyAbstractDetail", HistoryAbInfo.class);
        classMap.put("historyOne", HistoryPayInfo.class);
        classMap.put("historyThree", HistoryPayInfo.class);
        classMap.put("payInfo", PayInfo.class);
        
        CustomerPayment customerPayment = (CustomerPayment)JSONObject.toBean(jsonCustomerInfo, CustomerPayment.class, classMap);
        
        model.addAttribute("customerName", customerPayment.getCustomer().get(0).getYhname());
        model.addAttribute("customerCode", customerPayment.getCustomer().get(0).getYhcode());
        model.addAttribute("companyName", customerPayment.getCompanyName());
        model.addAttribute("companyCode", customerPayment.getCompanyCode());
        model.addAttribute("abstractMoney", customerPayment.getCustomer().get(0).getAbstractTotalMoney());
        model.addAttribute("bcye", customerPayment.getCustomer().get(0).getBcye());
        
        JSONArray jsonArray = JSONArray.fromObject(customerPayment.getHistoryAbstractDetail());  
        model.addAttribute("historyAbInfo", jsonArray.toString());
        
        jsonArray = JSONArray.fromObject(customerPayment.getHistoryOne()); 
        model.addAttribute("historyOne", jsonArray.toString().trim());
        
        jsonArray = JSONArray.fromObject(customerPayment.getHistoryThree());
        model.addAttribute("historyThree", jsonArray.toString().trim());
        
        jsonArray = JSONArray.fromObject(customerPayment.getPayInfo());
        model.addAttribute("payInfo", jsonArray.toString());
        
        return "/epay/paybill.jsp";
    }
    
    
    /**
     * 生成随机验证码并存入当前SESSION
     */
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response, @RequestParam String date) {
        BufferedImage img = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);

        Graphics graphic = img.getGraphics();
        Random r = new Random();
        Color c = new Color(255, 255, 255);
        graphic.setColor(c);
        graphic.fillRect(0, 0, 68, 22);
        Color borderColor = new Color(0xB5, 0xB5, 0xB5);
        graphic.setColor(borderColor);
        graphic.drawRect(0, 0, 67, 21);
        StringBuffer sb = new StringBuffer();
        char[] dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        int len = dic.length;
        for(int i=0; i<4; i++) {
            int index = r.nextInt(len);
            graphic.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
            graphic.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
            graphic.drawString("" + dic[index], (i*15)+3, 18);
            sb.append(dic[index]);
        }
        
        HttpSession session = request.getSession();
        if(session != null) {
            session.setAttribute("capchacode", sb.toString());
        }
        
        try {
            ImageIO.write(img, "JPG", response.getOutputStream());
        } catch(IOException e) {
            logger.error(e.getMessage());
        }
    }
    
    
    /**
     * 验证支付并生成订单
     */
    @RequestMapping(value="/webPayCheck.do", method=RequestMethod.POST)
    public JsonModelAndView webPayCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RespBean resp = new RespBean(0, "SUCCESS", null);
        String capchaCode = request.getSession(false).getAttribute("capchacode").toString();
        
        Properties property = new Properties();
        property.load(new ByteArrayInputStream((request.getParameter("payCheckCode")).getBytes("UTF-8")));
        
        String captchaInput = String.valueOf(property.get("captchaInput")).trim();
        if("".equals(captchaInput)) {
            resp.setCode(-1);
            resp.setMessage("请填写验证码！");
            return new JsonModelAndView(resp);
        }
        if(!capchaCode.equals(captchaInput.toUpperCase())) {
            resp.setCode(-1);
            resp.setMessage("验证码错误！");
            return new JsonModelAndView(resp);
        }
        
        String orderForm = request.getParameter("orderForm");
        orderForm = orderForm.replaceAll("\\\\","");
        JSONObject jsonOrder = JSONArray.fromObject(orderForm).getJSONObject(0);
        
        String appType = _GasPayConst.APP_TYPE_WEB;
        String companyCode = request.getParameter("companyCode");
        String customerId = request.getParameter("customerCode");
        String meterId = jsonOrder.getString("meterSerialNo");
        String cardId = null;
        Double chargeFee = Double.valueOf(request.getParameter("totalMoney"));
        String payType = null;
        
        String orderId = payService.createOrder(appType, companyCode, customerId, meterId, cardId, chargeFee, payType);
        orderId = CommonUtil.Base64Encode(orderId);
        
        resp.setData(orderId);
        return new JsonModelAndView(resp);
    }
    
    
    /**
     * 转支付网关选取页
     */
    @RequestMapping("/webPayWays.do")
    public String webPayWays(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        return "/epay/payways.jsp";
    }
    
    
    /**
     * 根据订单ID返回订单JSON
     */
    @RequestMapping("/getOrderInfo.do")
    public JsonModelAndView getOrderInfo(HttpServletRequest request, HttpServletResponse response) {
        RespBean resp = new RespBean(0, "SUCCESS", null);
        String orderId = CommonUtil.Base64Decode(request.getParameter("orderId"));
        GasPayOrder order = payService.getOrderById(orderId);
        resp.setData(order);
        return new JsonModelAndView(resp);
    }
    
    
    /**
     * 轮询订单支付状态（0:未支付；1:已支付）
     */
    @RequestMapping("/refreshOrderStatus.do")
    public JsonModelAndView refreshOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        RespBean resp = new RespBean(0, "SUCCESS", null);
        String orderId = CommonUtil.Base64Decode(request.getParameter("orderId"));
        GasPayOrder order = payService.getOrderById(orderId);
        boolean paid = !order.getStatus().equals(_GasPayConst.ORDER_STATUS_NEW);
        resp.setCode(paid ? 1 : 0);
        return new JsonModelAndView(resp);
    }
    
    
    /**
     * 支付宝网页支付
     */
    @RequestMapping(value="/webALPay.do", method=RequestMethod.POST)
    public void webALPay(HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
        String orderId = CommonUtil.Base64Decode(request.getParameter("orderId"));
        String orderFee = request.getParameter("orderFee");
        String companyCode = request.getParameter("companyCode");
        Properties alPayConfig = new PayConfig(companyCode).getPayConfigAL();
        
        if(alPayConfig == null) {
            throw new PayException("本商户不支持支付宝，请选择其它方式支付！");
        }
        
        payService.updateOrderPayType(orderId, _GasPayConst.PAY_TYPE_AL);
        
        CompanyInfo companyInfo = payService.getCompanyByCode(companyCode);
        
        alPayConfig.setProperty("partner", companyInfo.getAlPid());
        alPayConfig.setProperty("out_trade_no", orderId);
        alPayConfig.setProperty("total_fee", orderFee);
        
        String[] paramNames = new String[] {"_input_charset","partner","seller_email","service","payment_type","defaultbank","paymethod","subject","body","show_url","out_trade_no","total_fee","notify_url","return_url"};
        HashMap<String, String> alPayParams = new HashMap<String, String>();
        for(String paramName : paramNames) {
            alPayParams.put(paramName, alPayConfig.getProperty(paramName));
        }
        
        String payGatewayUrl = alPayConfig.getProperty("payGatewayUrl");
        String aliKey = companyInfo.getAlKey();
        String alPayRequest = AlipaySubmit.buildRequest(payGatewayUrl, alPayParams, "POST", "确认", aliKey);
        
        logger.info("支付宝[" + orderId + "]请求报文：" + alPayRequest);
        
        response.getWriter().println(alPayRequest);
    }
    
    
    /**
     * 支付宝支付回调
     */
    @RequestMapping("/alPayReturn.do")
    public String alPayReturn(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        //获取支付宝的回调反馈信息
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();
        for(Iterator<String> iter=requestParams.keySet().iterator(); iter.hasNext(); ) {
            String paramName = (String)iter.next();
            String[] values = (String[])requestParams.get(paramName);
            String paramValue = "";
            for(int i=0; i<values.length; i++) {
                paramValue = (i==values.length-1) ? paramValue+values[i] : paramValue+values[i]+",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
            params.put(paramName, paramValue);
        }
        
        final String outTradeNo = request.getParameter("out_trade_no");
        final String tradeNo = request.getParameter("trade_no");
        String totalFee  = request.getParameter("total_fee");
        String tradeStatus = request.getParameter("trade_status");
        
        logger.info("支付宝[" + outTradeNo + "]响应报文：" + params.toString());
        
        final CompanyInfo companyInfo = payService.getCompanyByOrderId(outTradeNo);
        
        boolean tradeSuccess = tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED");
        boolean verifySuccess = AlipayNotify.verify(params, companyInfo.getAlPid(), companyInfo.getAlKey());
        
        logger.info("支付宝[" + outTradeNo + "]支付" + (tradeSuccess ? "成功！":"失败！") + "验签" + (verifySuccess ? "成功！":"失败！"));
        
        String payResultMsg = null;
        if(verifySuccess) {
            if(tradeSuccess) {
                new PayThread(payService, outTradeNo, tradeNo, companyInfo.getGatewayUrl(), "hlUsername", "hlPasword").start();
                payResultMsg = "气付通订单[" + outTradeNo + "]支付成功！";
            } else {
                payResultMsg = "气付通订单[" + outTradeNo + "]支付失败！";
            }
        } else {
            if(tradeSuccess) {
                payResultMsg = "气付通订单[" + outTradeNo + "]付款成功，但验签失败！";
            } else {
                payResultMsg = "气付通订单[" + outTradeNo + "]付款失败，且验签失败！";
            }
        }
        logger.info(payResultMsg);
        
        model.addAttribute("orderId", outTradeNo);
        model.addAttribute("tradeNo", tradeNo);
        model.addAttribute("payedFee", totalFee);
        model.addAttribute("tradeNoName", "阿里单号");
        model.addAttribute("payResult", payResultMsg);
        return "/epay/pay-result.jsp";
    }
    
    
    /**
     * 微信扫码网页支付
     */
    @RequestMapping(value="/webWXPay.do", method=RequestMethod.POST)
    public JsonModelAndView webWXPay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String orderId = CommonUtil.Base64Decode(request.getParameter("orderId"));
        String orderFee = request.getParameter("orderFee");
        String companyCode = request.getParameter("companyCode");
        Properties wxPayConfig = new PayConfig(companyCode).getPayConfigWX();
        
        if(wxPayConfig == null) {
            throw new PayException("本商户不支持微信支付，请选择其它方式支付！");
        }
        
        payService.updateOrderPayType(orderId, _GasPayConst.PAY_TYPE_WX);
        
        CompanyInfo companyInfo = payService.getCompanyByCode(companyCode);
        
        wxPayConfig.setProperty("mch_id", companyInfo.getWxPid());
        wxPayConfig.setProperty("appid", companyInfo.getWxAppId());
        wxPayConfig.setProperty("out_trade_no", orderId);
        wxPayConfig.setProperty("total_fee", String.valueOf((int)(Double.parseDouble(orderFee)*100)));
        wxPayConfig.setProperty("nonce_str", IdGenerator.genRandomStr(16));
        
        String[] paramNames = new String[] {"trade_type","product_id","body","mch_id","appid","out_trade_no","total_fee","nonce_str","notify_url"};
        HashMap<String, String> wxPayParams = new HashMap<String, String>();
        for(String paramName : paramNames) {
            wxPayParams.put(paramName, wxPayConfig.getProperty(paramName));
        }
        wxPayParams.put("sign", Signature.getSign(wxPayParams, companyInfo.getWxKey()));
        
        String wxPayRequest = CommonUtil.map2Xml(wxPayParams);
        logger.info("微信支付[" + orderId + "]请求报文：" + wxPayRequest);
        
        String wxPayResponse = CommonUtil.postUrlData(wxPayConfig.getProperty("payGatewayUrl"), wxPayRequest);
        logger.info("微信支付[" + orderId + "]预付二维码报文：" + wxPayResponse);
        
        RespBean resp = new RespBean(0, "SUCCESS", null);
        Document wxPayDoc = null;
        String code_url = null;
        try {
            wxPayDoc = DocumentHelper.parseText(wxPayResponse);
        } catch(DocumentException e) {
            resp.setCode(-1);
            resp.setMessage("二维码URL解析失败");
            return new JsonModelAndView(resp);
        }
        if(wxPayDoc != null) {
            Element root = wxPayDoc.getRootElement();
            if(root == null) {
                 resp.setCode(-1);
                 resp.setMessage("返回数据结构异常");
                 return new JsonModelAndView(resp);
            }
            Element codeUrl = root.element("code_url");
            if(codeUrl == null) {
                resp.setCode(-1);
                resp.setMessage("无返回二维码code_url参数");
                return new JsonModelAndView(resp);
            }  
            code_url = codeUrl.getText();
        }
        resp.setData(code_url);
        
        return new JsonModelAndView(resp);
    }
    
    
    /**
     * 微信支付回调
     */
    @RequestMapping("/wxPayReturn.do")
    public void wxPayReturn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line;
        StringBuffer sb = new StringBuffer();
        while((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        inputStream.close();
        
        //解析XML成MAP
        Map<String, String> xmlMap = new HashMap<String, String>();
        xmlMap = CommonUtil.parseL1XML(sb.toString());
        
        //设置TreeMap过滤空值
        SortedMap<String, String> wxRespParams = new TreeMap<String, String>();
        Iterator<String> iter = xmlMap.keySet().iterator();
        String value = "";
        while(iter.hasNext()) {
            String key = iter.next();
            String paramValue = xmlMap.get(key);
            if(null != paramValue) {
                value = paramValue.trim();
            }
            wxRespParams.put(key, value);
        }
        
        final CompanyInfo companyInfo = payService.getCompanyByOrderId(wxRespParams.get("out_trade_no"));
        
        if(companyInfo == null) {
            logger.info(wxRespParams.get("out_trade_no") + "订单已经得到处理!");
            return;
        }
        
        final String outTradeNo = wxRespParams.get("out_trade_no");
        final String tradeNo = (String)wxRespParams.get("transaction_id");
        String totalFee = Double.toString(((Double.parseDouble(wxRespParams.get("total_fee"))/100)));
        
        logger.info("微信支付[" + outTradeNo + "]响应报文：" + wxRespParams);
        
        String wxKey = companyInfo.getWxKey();
        boolean tradeSuccess = "SUCCESS".equals((String)wxRespParams.get("result_code"));
        //微信支付回调验签
        boolean verifySuccess = WXPayMD5.isWxPaySign("UTF-8", wxRespParams, wxKey);
        logger.info("微信扫码[" + outTradeNo + "]支付" + (tradeSuccess ? "成功！":"失败！") + "验签" + (verifySuccess ? "成功！":"失败！"));
        
        String payResultMsg = null;
        if(verifySuccess) {
            String notifyXml = "";
            if(tradeSuccess) {
                new PayThread(payService, outTradeNo, tradeNo, companyInfo.getGatewayUrl(), "hlUsername", "hlPasword").start();
                payResultMsg = "气付通订单[" + outTradeNo + "]支付成功！";
                //通知微信，异步确认成功，必写！不然会一直通知后台，八次之后就认为交易失败
                notifyXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                payResultMsg = "气付通订单[" + outTradeNo + "]支付失败！";
                notifyXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(notifyXml.getBytes());
            out.flush();
            out.close();
        } else {
            if(tradeSuccess) {
                payResultMsg = "气付通订单[" + outTradeNo + "]付款成功，但验签失败！";
            } else {
                payResultMsg = "气付通订单[" + outTradeNo + "]付款失败，且验签失败！";
            }
        }
        logger.info(payResultMsg);
        
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("payResult", payResultMsg);
        resultMap.put("tradeNoName", "微信单号");
        resultMap.put("tradeNo", tradeNo);
        resultMap.put("payedFee", totalFee);
        request.getServletContext().setAttribute(outTradeNo, resultMap);
    }
    
    
    /**
     * 微信/银联支付结果页
     */
    @RequestMapping("/wuReturnPage.do")
    public String wuReturnPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        String orderId = CommonUtil.Base64Decode(request.getParameter("orderId"));
        
        @SuppressWarnings("unchecked")
        Map<String, String> resultMap = (Map<String, String>)request.getServletContext().getAttribute(orderId);
        String payResult = resultMap.get("payResult");
        String tradeNoName = resultMap.get("tradeNoName");
        String tradeNo = resultMap.get("tradeNo");
        String payedFee = resultMap.get("payedFee");
        
        model.addAttribute("payResult", payResult);
        model.addAttribute("tradeNoName", tradeNoName);
        model.addAttribute("tradeNo", tradeNo);
        model.addAttribute("payedFee", Double.valueOf(payedFee));
        
        request.getServletContext().removeAttribute(orderId);
        
        return "/epay/pay-result.jsp";
    }
    
    
    /**
     * 银联网页支付
     */
    @RequestMapping("webUNPay.do")
    public void webUNPay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String orderId = CommonUtil.Base64Decode(request.getParameter("orderId"));
        String orderFee = request.getParameter("orderFee");
        String companyCode = request.getParameter("companyCode");
        PayConfig payConfig = new PayConfig(companyCode);
        Properties unPayConfig = payConfig.getPayConfigUN();
        
        if(unPayConfig == null) {
            throw new PayException("本商户不支持银联支付，请选择其它方式支付！");
        }
        
        payService.updateOrderPayType(orderId, _GasPayConst.PAY_TYPE_UN);
        
        CompanyInfo companyInfo = payService.getCompanyByCode(companyCode);
        
        unPayConfig.setProperty("merId", companyInfo.getUnPid());
        unPayConfig.setProperty("orderId", orderId);
        unPayConfig.setProperty("txnAmt", String.valueOf((int)(Double.parseDouble(orderFee)*100)));
        unPayConfig.setProperty("txnTime", SDKUtil.generateTxnTime());
        
        String[] paramNames = new String[] {"frontUrl","backUrl","version","encoding","signMethod","txnType","txnSubType","bizType","channelType","accessType","currencyCode","merId","orderId","txnAmt","txnTime","reqReserved"};
        HashMap<String, String> unPayParams = new HashMap<String, String>();
        for(String paramName : paramNames) {
            unPayParams.put(paramName, unPayConfig.getProperty(paramName));
        }
        
        String certPath = PayConfig.getPayConfigPath(companyCode) + companyInfo.getUnCertFile();
        String certPwd = companyInfo.getUnCertKey();
        
        Map<String, String> submitForm = UnionBase.signData(unPayParams, certPath, certPwd, payConfig);
        String payGatewayUrl = unPayConfig.getProperty("frontUrl");
        
        RespBean resp = new RespBean(0, "SUCCESS", null);
        resp.setData(org.json.JSONObject.valueToString(submitForm));
        
        String unRequestHTML = UnionBase.createHtml(payGatewayUrl, submitForm);
        logger.info("银联[" + orderId + "]请求报文：" + unRequestHTML);
        
        response.getWriter().write(unRequestHTML);
    }
    
    
    /**
     * 银联支付回调
     */
    @RequestMapping("/unPayReturn.do")
    public void unPayReturn(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> unRespParams = new HashMap<String, String>();
        Enumeration<String> temp = request.getParameterNames();
        if(null != temp) {
            while(temp.hasMoreElements()) {
                String key = temp.nextElement();
                String value = request.getParameter(key);
                unRespParams.put(key, value);
                //删除键值为空的无效字段
                if(StringUtils.isEmpty(unRespParams.get(key))) {
                    unRespParams.remove(key);
                }
            }
        }
        
        final String outTradeNo = unRespParams.get("orderId");
        final String tradeNo = unRespParams.get("queryId");
        String totalFee = Double.toString(((Double.parseDouble(unRespParams.get("txnAmt"))/100)));
        
        logger.info("银联[" + outTradeNo + "]响应报文：" + unRespParams);
        
        final CompanyInfo companyInfo = payService.getCompanyByOrderId(outTradeNo);
        
        String tradeStatus = unRespParams.get("respCode");
        boolean tradeSuccess = tradeStatus.equals("00") || tradeStatus.equals("A6");
        logger.info("银联[" + outTradeNo + "]支付" + (tradeSuccess ? "成功！":"失败！"));
        
        String payResultMsg = null;
        if(tradeSuccess) {
            new PayThread(payService, outTradeNo, tradeNo, companyInfo.getGatewayUrl(), "hlUsername", "hlPasword").start();
            payResultMsg = "气付通订单[" + outTradeNo + "]支付成功！";
        } else {
            payResultMsg = "气付通订单[" + outTradeNo + "]支付失败！";
        }
        
        logger.info(payResultMsg);
        
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("payResult", payResultMsg);
        resultMap.put("tradeNoName", "银联单号");
        resultMap.put("tradeNo", tradeNo);
        resultMap.put("payedFee", totalFee);
        request.getServletContext().setAttribute(outTradeNo, resultMap);
    }
    
    
    /**
     * 慧付宝支付订单接口
     */
    @RequestMapping("hfbPay.do")
    public void hfbPay(HttpServletRequest request, HttpServletResponse response, Model model) {
        //TODO 慧付宝与支付相关的服务端代码
    }
    
    
    /**
     * 慧付宝支付回调接口
     */
    @RequestMapping("hfbPayReturn.do")
    public void hfbPayReturn(HttpServletRequest request, HttpServletResponse response, Model model) {
        //TODO 慧付宝支付第三方回调代码
    }
    
    
    /**
     * 微信公众号支付订单接口
     */
    @RequestMapping("wxhPay.do")
    public void wxhPay(HttpServletRequest request, HttpServletResponse response, Model model) {
        //TODO 微信公众号与支付相关的服务端代码
    }
    
    
    /**
     * 微信公众号支付回调接口
     */
    @RequestMapping("wxhPayReturn.do")
    public void wxhPayReturn(HttpServletRequest request, HttpServletResponse response, Model model) {
        //TODO 微信公众号支付第三方回调代码
    }
    
}
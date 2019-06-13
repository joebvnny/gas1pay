package com.hlzn.common;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * WebService认证头
 */
public class WebServiceAuth extends AbstractPhaseInterceptor<SoapMessage> {

	private String username;
	private String password;
	
	public WebServiceAuth(String username, String password) {
		super(Phase.PREPARE_SEND);
		this.username = username;
		this.password = password;
	}

	@Override
	public void handleMessage(SoapMessage soap) throws Fault {
		Document doc = DOMUtils.createDocument();
		
		Element auth = doc.createElement("jAuth");
		Element username = doc.createElement("username");
		Element password = doc.createElement("password");
		
		username.setTextContent(this.username);
		password.setTextContent(this.password);
		
		auth.appendChild(username);
		auth.appendChild(password);
		
        List<Header> headers = soap.getHeaders();
		headers.add(0, new Header(new QName("hlauth"), auth));
	}

}
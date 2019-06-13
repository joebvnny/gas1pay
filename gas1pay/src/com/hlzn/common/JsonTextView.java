package com.hlzn.common;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 自定义JSON文本视图类型
 */
public class JsonTextView extends AbstractView {

	public static final String RESPONSE_BEAN_KEY = "RespBean.DATA";
    public static final String RESPONSE_TYPE_JSON = "JSON";
    public static final String RESPONSE_TYPE_XML = "XML";

	private static ObjectMapper jsonObj = new ObjectMapper();
	static{
		jsonObj.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

    protected String characterEncoding = "UTF-8";

	public JsonTextView() {
		this.setContentType("text/plain; charset=UTF-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.reset();
		response.setContentType(this.getContentType());
		response.setCharacterEncoding(characterEncoding);
		
		Object bean = model.get(RESPONSE_BEAN_KEY);
		
		String type = this.setRespType();
		
		if(RESPONSE_TYPE_JSON.equalsIgnoreCase(type)) {
			jsonObj.writeValue(response.getWriter(), bean);
		} else {
			throw new RuntimeException("Response Type " + type.toUpperCase() + " not supported!");
		}
	}

	private String setRespType() {
		return RESPONSE_TYPE_JSON;
	}
	
}
package com.hlzn.common;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

/**
 * JSON文本格式的MVC返回类型
 */
public class JsonModelAndView extends ModelAndView {

    public JsonModelAndView() {
        super(new JsonTextView());
    }

	public JsonModelAndView(Map<String, Object> model) {
		super(new JsonTextView(), model);
	}

	public JsonModelAndView(String modelName, Object modelObject) {
		super(new JsonTextView(), modelName, modelObject);
	}

	public JsonModelAndView(Object jsonBean) {
		super(new JsonTextView());
		this.getModelMap().put(JsonTextView.RESPONSE_BEAN_KEY, jsonBean);
	}
	
}
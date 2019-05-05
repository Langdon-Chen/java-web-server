package com.langdon.tinywebserver.http.impl;

import com.langdon.tinywebserver.http.HttpMethod;
import com.langdon.tinywebserver.http.HttpRequest;
import sun.security.x509.IPAddressName;

import java.util.Map;

public class BasicHttpRequest extends BasicHttpMessage implements HttpRequest {

	private HttpMethod method;

	private String requestUri;

	private Map<String,Object> attributes ;
	
	public HttpMethod getHttpMethod() {
		// TODO Auto-generated method stub
		return method;
	}

	public String getRequestUri() {
		// TODO Auto-generated method stub
		return requestUri;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public void setMethod(HttpMethod method)
	{
		this.method = method;
	}

	public void setRequestUri(String requestUri)
	{
		this.requestUri = requestUri;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public IPAddressName getRemoteIP(){
		// todo
		return null;
	}
}

package com.langdon.tinywebserver.http.impl;

import java.nio.charset.Charset;
import java.util.Map;
import com.langdon.conf.WebConfiguration;
import com.langdon.tinywebserver.http.HttpVersion;
import com.langdon.tinywebserver.http.HttpMessage;

public class BasicHttpMessage implements HttpMessage {

	private HttpVersion version;

	private byte[] entity = null;

	private Map<String, String> headers;
	
	public HttpVersion getHttpVersion() {
		// TODO Auto-generated method stub
		return version;
	}

	public Map<String, String> getHeaders() {
		// TODO Auto-generated method stub
		return headers;
	}

	public byte[] getEntity() {
		// TODO Auto-generated method stub
		return entity;
	}

	public void setVersion(HttpVersion version) {
		this.version = version;
	}

	public void setEntity(byte[] entity) {
		this.entity = entity;
	}

	public void setEntity(String entity){
		this.entity = entity.getBytes(Charset.forName(WebConfiguration.getServerCharset()));
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	

}

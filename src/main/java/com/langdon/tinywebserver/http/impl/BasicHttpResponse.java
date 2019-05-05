package com.langdon.tinywebserver.http.impl;

import com.langdon.tinywebserver.http.HttpResponse;
import com.langdon.tinywebserver.http.HttpStatusCode;

import java.util.Map;

public class BasicHttpResponse extends BasicHttpMessage implements HttpResponse {

	private HttpStatusCode statusCode;

	public HttpStatusCode getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(HttpStatusCode statusCode)
	{
		this.statusCode = statusCode;
	}

	public BasicHttpResponse(){
		this.statusCode = HttpStatusCode.OK;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getHttpVersion())
				.append((char) Character.SPACE_SEPARATOR)
				.append(statusCode.getCode())
				.append((char)Character.SPACE_SEPARATOR)
				.append(statusCode.getReasonPhrase())
				.append((char)Character.LINE_SEPARATOR);
		for (Map.Entry<String ,String > entry : getHeaders().entrySet()){
			sb.append(entry.getKey())
					.append((char)Character.SPACE_SEPARATOR)
					.append(entry.getValue())
					.append((char)Character.LINE_SEPARATOR)
					.append((char)Character.LINE_SEPARATOR);
		}
		return getEntity()==null ? sb.toString() : sb.append(new String(getEntity())).toString();
	}
}

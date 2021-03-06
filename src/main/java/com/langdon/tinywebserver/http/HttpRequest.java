package com.langdon.tinywebserver.http;

import sun.security.x509.IPAddressName;

import java.util.Map;

public interface HttpRequest extends HttpMessage {
	/**
	 * Returns the HTTP method of this request.
	 * 
	 * @see also http://tools.ietf.org/html/rfc2616.html#section-5.1.1
	 * 
	 * @return method
	 */
	HttpMethod getHttpMethod();

	/**
	 * Returns the request URI of this request.
	 * 
	 * @see also http://tools.ietf.org/html/rfc2616.html#section-5.1.2
	 * 
	 * @return the request URI as string
	 */
	String getRequestUri();

	Map<String,Object> getAttributes();

	Object getAttribute(String key);

	IPAddressName getRemoteIP();
}

package com.langdon.tinywebserver.server;

import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.langdon.tinywebserver.http.HttpRequest;
import com.langdon.tinywebserver.http.HttpResponse;
import com.langdon.tinywebserver.http.HttpStatusCode;
import com.langdon.tinywebserver.http.impl.BasicHttpRequest;
import com.langdon.tinywebserver.http.impl.BasicHttpResponse;
import com.langdon.tinywebserver.www.Http;

/**
 * 一个处理request请求的线程；
 * @author langdon
 *
 */
public class HttpWorker extends AbstractHttpWorker implements Callable<Void> {
	
	protected final Socket socket;

	protected final HttpServer server;

	/**
	 * 创建新的worker线程处理request
	 * @param socket
	 * @param server 
	 */
	public HttpWorker(Socket socket, HttpServer server)
	{
		this.socket = socket;
		this.server = server;

	}

	// void 与 Void
	public Void call() throws Exception
	{
		// Parse request from InputStream
		BasicHttpRequest request = parseRequest(socket.getInputStream());
		// Create appropriate response
		HttpResponse response = handleRequest(request);

		// Send response and close connection, if necessary
		//如果是keepAlive ， 则继续dispatch这个socket链接；
		//所以，如果不是长连接，那就直接close了，这就解释了为什么socket面向连接，http不是
		if (keepAlive(request, response)) {
			sendResponse(response, socket.getOutputStream());
			server.dispatchRequest(socket);
		} else {
			response.getHeaders().put("Connection", "close");
			sendResponse(response, socket.getOutputStream());
			socket.close();
		}

		// We do not return anything here.
		return null;
	}

    /**
     * 处理请求的URL，如果请求的资源存在，就把所访问的资源放在entity里面；
	 * Creates an appropriate {@link HttpResponse} to the given
	 * {@link HttpRequest}. Note however, that this method is not yet sending
	 * the response.
	 *
	 * @param request
	 *            The {@link HttpRequest} that must be handled.
	 * @return
	 */
    protected HttpResponse handleRequest(BasicHttpRequest request)
	{
		BasicHttpResponse response = new BasicHttpResponse();
		response.setHeaders(new HashMap<String, String>());
		response.getHeaders().put(Http.SERVER, server.getServerSignature());
		response.getHeaders().put(Http.DATE,new Date().toString());
		response.setVersion(request.getHttpVersion());
		// MVC 模式
		HttpResponse httpResponse = server.getPathDispatcher().handleRequest(request,response);
		if (httpResponse!=null){
			response = (BasicHttpResponse)httpResponse;
			return response;
		}else {
			response.setStatusCode(HttpStatusCode.NOT_FOUND);
			// todo 改用更优雅的返回方式
			byte[] fileContent = ("<!DOCTYPE html>\n" +
					"<html>\n" +
					"<head>\n" +
					"<meta charset=\"UTF-8\">\n" +
					"<title>"
					+ "ERROR"
					+ "</title>\n" +
					"</head>\n" +
					"<body>\n" +
					"<h1 align='center' > "
					+ HttpStatusCode.NOT_FOUND
					+ " </h1>\n" +
					"</body>\n" +
					"</html>").getBytes();
			response.setEntity(fileContent);
			return response;
		}

		//		String requestUri = request.getRequestUri();
//		if (requestUri.equals("/"))
//		{
//			requestUri = "/index.html";
//		}
		// todo dispatch

//		File requestFile = new File("webroot/" + requestUri);
//
//		File rootDir = new File("webroot/");
//		try
//		{
//			//这里打印的是计算机上该文件的访问路径
//			System.out.println("requestMethod: "+request.getHttpMethod());
//			System.out.println("requestUri: "+request.getRequestUri());
//			System.out.println("requestUri.file.canoicalPath: "+requestFile.getCanonicalPath());
//			if (!requestFile.getCanonicalPath().startsWith(rootDir.getCanonicalPath()))
//			{
//				response.setStatusCode(HttpStatusCode.FORBIDDEN);
//				return response;
//			}
//		}
//		catch (IOException e1)
//		{
//			response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
//			return response;
//		}
//		//访问的资源存在，get方法->读取文件，返回
//		if (requestFile.exists())
//		{
//			response.setStatusCode(HttpStatusCode.OK);
//			InputStream inputStream;
//			try
//			{
//				inputStream = new FileInputStream(requestFile);
//				byte [] fileContent;
//				switch(method)
//				{
//				case GET:
//					fileContent = new byte[(int) requestFile.length()];
//					inputStream.read(fileContent);//将文件内容写入fileContent
//					inputStream.close();
//					break;
//				default:
//					fileContent = ("<!DOCTYPE html>\n" +
//							"<html>\n" +
//							"<head>\n" +
//							"<meta charset=\"UTF-8\">\n" +
//							"<title>"
//							+ method
//							+ "</title>\n" +
//							"</head>\n" +
//							"<body>\n" +
//							"<h1 align='center' >you have "
//							+ method
//							+ " sth.!</h1>\n" +
//							"</body>\n" +
//							"</html>").getBytes();
//					break;
//				}
//				//byte [] fileContent = new byte[(int) requestFile.length()];
//				//多样化content
//				response.setEntity(fileContent);
//				// guess and set the content type
//				response.getHeaders().put(Http.CONTENT_TYPE, URLConnection.guessContentTypeFromName(requestFile.getAbsolutePath()));
//			}
//			catch (FileNotFoundException e)
//			{
//				response.setStatusCode(HttpStatusCode.NOT_FOUND);
//			}
//			catch (IOException e)
//			{
//				response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
//			}
//		}
//		else
//		{
//			response.setStatusCode(HttpStatusCode.NOT_FOUND);
//			byte[] fileContent = ("<!DOCTYPE html>\n" +
//					"<html>\n" +
//					"<head>\n" +
//					"<meta charset=\"UTF-8\">\n" +
//					"<title>"
//					+ "ERROR"
//					+ "</title>\n" +
//					"</head>\n" +
//					"<body>\n" +
//					"<h1 align='center' > "
//					+ HttpStatusCode.NOT_FOUND
//					+ " </h1>\n" +
//					"</body>\n" +
//					"</html>").getBytes();
//			response.setEntity(fileContent);
//		}

		//return response;
	}





}

package com.langdon.tinywebserver.server;

import com.langdon.tinywebserver.http.impl.BasicHttpRequest;
import com.langdon.tinywebserver.http.HttpMethod;
import com.langdon.tinywebserver.http.HttpRequest;
import com.langdon.tinywebserver.http.HttpResponse;
import com.langdon.tinywebserver.http.HttpVersion;
import com.langdon.tinywebserver.www.Http;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc
 * @create by langdon on 2018/5/25-上午10:59
 * @author langdon
 **/

public abstract class AbstractHttpWorker {

    protected final Log logger = LogFactory.getLog(getClass());
    /**
     *
     * Parses an incoming request. Reads the {@link InputStream} and creates an
     * corresponding {@link HttpRequest} object, which will be returned.
     *
     * @param inputStream
     *            The stream to read from.
     * @return
     * @throws IOException
     */
    protected BasicHttpRequest parseRequest(InputStream inputStream) throws IOException
    {
        String firstLine = readLine(inputStream);
        logger.info(firstLine);
        BasicHttpRequest request = new BasicHttpRequest();
        request.setVersion(HttpVersion.extractVersion(firstLine));
        request.setMethod(HttpMethod.extractMethod(firstLine));
        request.setRequestUri(extractRequestUri(firstLine));
        request.setAttributes(extractAttributes(firstLine));
        Map<String, String> headers = new HashMap<String, String>();
        String nextLine = "";
        System.out.println(firstLine);
        while (!(nextLine = readLine(inputStream)).equals("")) {
            System.out.println(nextLine);
            String values[] = nextLine.split(":", 2);
            headers.put(values[0], values[1].trim());
        }
        request.setHeaders(headers);
        if (headers.containsKey(Http.CONTENT_LENGTH)) {
            int size = Integer.parseInt(headers.get(Http.CONTENT_LENGTH));
            byte[] data = new byte[size];
            int n;
            for (int i = 0; i < size && (n = inputStream.read()) != -1; i++) {
                data[i] = (byte) n;
            }
            request.setEntity(data);
        }
        else {
            request.setEntity(new byte[0]);
        }

        return request;
    }

    /**
     * 读取inputstream；直到遇到了\r\n
     * A helper method that reads an InputStream until it reads a CRLF (\r\n\).
     * Everything in front of the linefeed occured is returned as String.
     *
     * @param inputStream
     *            The stream to read from.
     * @return The character sequence in front of the linefeed.
     * @throws IOException
     */
    protected String readLine(InputStream inputStream) throws IOException {
        StringBuffer result = new StringBuffer();
        boolean crRead = false;
        int n;
        while ((n = inputStream.read()) != -1)
        {
            if (n == '\r')
            {
                crRead = true;
                continue;
            }
            else if (n == '\n' && crRead)
            {
                return result.toString();
            }
            else
            {
                result.append((char) n);
            }
        }
        return result.toString();
    }

    /**
     * Determines whether a connection should be kept alive or not on
     * server-side. This decision is made based upon the given (
     * {@link HttpRequest}, {@link HttpResponse}) couple, respectively their
     * header values.
     *
     * @param request
     * @param response
     * @return true, if the server should keep open the connection, otherwise
     *         false.
     */
    protected boolean keepAlive(HttpRequest request, HttpResponse response)
    {
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec8.html
        if (response.getHeaders().containsKey(Http.CONNECTION) && response.getHeaders().get(Http.CONNECTION).equalsIgnoreCase("close")) {
            return false;
        }
        if (request.getHttpVersion().equals(HttpVersion.VERSION_1_1)) {
            if (request.getHeaders().containsKey(Http.CONNECTION) && request.getHeaders().get(Http.CONNECTION).equalsIgnoreCase("close")) {
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * Sends a given {@link HttpResponse} over the given {@link OutputStream}.
     *
     * @param response
     * @param outputStream
     * @throws IOException
     */
    protected void sendResponse(HttpResponse response, OutputStream outputStream) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        writer.write(response.getHttpVersion().toString());
        writer.write(' ');
        writer.write("" + response.getStatusCode().getCode());
        writer.write(' ');
        writer.write(response.getStatusCode().getReasonPhrase());
        writer.write(Http.CRLF);

        //设置header.contentLength
        if (response.getEntity() != null && response.getEntity().length > 0)
        {
            response.getHeaders().put(Http.CONTENT_LENGTH, "" + response.getEntity().length);
        }
        else
        {
            response.getHeaders().put(Http.CONTENT_LENGTH, "" + 0);
        }
        //返回header信息
        if (response.getHeaders() != null)
        {
            for (String key : response.getHeaders().keySet())
            {
                writer.write(key + ": " + response.getHeaders().get(key) + Http.CRLF);
            }
        }
        writer.write(Http.CRLF);
        writer.flush();
        //？ 为什么这里写header使用writer，写entity用outputStream;因为entity是byte[]
        //写入entity信息
        if (response.getEntity() != null && response.getEntity().length > 0)
        {
            outputStream.write(response.getEntity());
        }
        outputStream.flush();

    }

    /**
     *  extract url params from firstLine {@link AbstractHttpWorker#parseRequest(InputStream)}
     *  for example, /pet/get?name=tom&age=18  should be name->tom and age->18
     * @param firstLine
     * @return
     */
    private Map<String,Object> extractAttributes(String firstLine){
        Map<String,Object> attributes = new HashMap<>();
        // todo
        int start = firstLine.indexOf(" ") + 1;
        int end = firstLine.lastIndexOf(" ");
        String requestUriWithAttributes = firstLine.substring(start,end);
        int questionMarkIndex = requestUriWithAttributes.indexOf("?");
        // there may are not attributes in request
        if (questionMarkIndex > 0){
            String [] params =  requestUriWithAttributes.substring(questionMarkIndex+1).split("&");
            for (String p : params){
                String [] keyValue = p.split("=");
                attributes.put(keyValue[0],keyValue[1]);
            }
        }
        return attributes;
    }

    /**
     * extract request uri without attributes
     * for example, /pet/get?name=tom&age=18  should be /pet/get
     * @param firstLine
     * @return
     */
    private String extractRequestUri(String firstLine){
        int start = firstLine.indexOf(" ") + 1;
        int end = firstLine.lastIndexOf(" ");
        String requestUriWithAttributes = firstLine.substring(start,end);
        int questionMarkIndex = requestUriWithAttributes.indexOf("?");
        if (questionMarkIndex > 0){
            return requestUriWithAttributes.substring(0,questionMarkIndex);
        }
        return requestUriWithAttributes;
    }


}

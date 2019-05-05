package com.langdon.tinywebserver.server;

import com.langdon.conf.WebConfiguration;
import com.langdon.tinywebserver.dispatcher.PathPathPathDispatcher;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
	
	
	public static final String SERVER_NAME = WebConfiguration.getServerName();
	public static final String SERVER_VERSION = WebConfiguration.getServerVersion();
	public static final int DEFAULT_PORT = WebConfiguration.getServerPort();
	public static final String SERVER_SIGNATURE = SERVER_NAME + File.separator + SERVER_VERSION;
	public static final String APPLICATION_CONTEST = "bean.xml";

	private volatile boolean running = false;

	private PathPathPathDispatcher pathDispatcher;
	private final ExecutorService workerPool;
	private final ExecutorService dispatcherService;
	private final ServerSocket serverSocket;

	private  final Logger logger = Logger.getLogger(getClass());
	/**
	 *  Creates a new HTTP server.
	 */
	public HttpServer(PathPathPathDispatcher pathDispatcher) {
		this(HttpServer.DEFAULT_PORT, pathDispatcher);
	}
	
	/**
	 *
	 * Creates a new HTTP server bound to the given port.
	 * 
	 * @param port listening port
	 *
	 * @throws IOException
	 */
	public HttpServer(int port, PathPathPathDispatcher pathDispatcher) {

		try {
			this.pathDispatcher = pathDispatcher;
			serverSocket = new ServerSocket(port);
			workerPool = Executors.newFixedThreadPool(16);
			dispatcherService = Executors.newSingleThreadExecutor();
		}
		catch (IOException e) {
			throw new RuntimeException("Error while starting server", e);
		}

	}
	
	/**
	 * 非线程的start方法;Starts the configured webserver.
	 */
	public void start() {
		running = true;
		// Initiate the main server loop accepting incoming connections.
		// 这里submit就意味着让executor帮我们管理线程，它会自动调用线程的start()方法
		dispatcherService.submit((Runnable) () -> {
            while (running) {
                try {
                    //每一个HTTP访问，就相当于accept
                    Socket socket = serverSocket.accept();
                    dispatchRequest(socket);
                } catch (SocketException e) {
                    // ignore due to close signaling
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
		logger.info("WebServer started on port " + serverSocket.getLocalPort() + "...");

	}

	/**
	 * Shuts down the webserver.
	 */
	public void stop() {
		try {
			running = false;
			dispatcherService.shutdown();
			workerPool.shutdown();
			serverSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.err.println("Webserver stopped.");
		}
	}

	/**
	 * Dispatches an incoming socket connection to an appropriate handler.
	 * 
	 * @param socket
	 */
	public void dispatchRequest(Socket socket) {
		workerPool.submit(new HttpWorker(socket,this));
	}

	/**
	 * Returns the signature of the webserver.
	 * 
	 * @return
	 */
	public String getServerSignature() {
		return HttpServer.SERVER_SIGNATURE;
	}

	public PathPathPathDispatcher getPathDispatcher() {
		return pathDispatcher;
	}
}

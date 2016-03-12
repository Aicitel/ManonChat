import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * filename:SocketServer.java
 * author:martin
 * comment:socketserver
 */

/**
 * @author martin
 * 
 */
public class SocketServer {

	/*
	 * List to maintain Clients socket 
	 */
	private static List<Socket> clients=null;
	
	public static int getClientsNum(){
		return clients.size();
	}
	public static boolean isClientClosed(int index){
		return clients.get(index).isClosed(); 
	}
	public static Socket getClientByID(int index){ 
		try {
			if(!clients.get(index).isClosed())
				return clients.get(index);
			else 
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
			
	
	public static void main(String[] args) {
		ServerSocket server = null;
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			// new a socket server
			server = new ServerSocket(39998);
			clients = new ArrayList<Socket>();
			
			while(true) {
				// start to listen, this step will be blocked
				Socket request = server.accept();
				request.setKeepAlive(true);
				
				// when getting a request, server will start a thread to handle the request.
				// and then keep going to listen.
				executor.execute(new HandleDataThread(request));
				clients.add(request);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

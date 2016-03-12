
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class HandleDataThread implements Runnable {
	// request from client
	private Socket request;

	// request id
	private int requestID;
	//private String requestUserName;

	public HandleDataThread(Socket request) {
		this.request = request;
	}

	@Override
	public void run() {
		try {
			//request.setSoTimeout(20000);
			String binaryString = "";
			while (true) {
				// get info from request when getting a socket request
				String reqStr = "";
				try {
					// if read() get a timeout exception
					reqStr = SocketUtil.readStrFromStream(request.getInputStream());
				} catch (SocketTimeoutException e) {
					// then break while loop, stop the service
					System.out.println(SocketUtil.getNowTime() + " : Time is out, request[" + requestID + "] has been closed.");
					break;
				}

				binaryString = reqStr;
				//Method remain to implement
				String context = FileUtil.ParseReq(binaryString)[0];
				
				for(int i = 0; i<SocketServer.getClientsNum();i++){
					if(!SocketServer.isClientClosed(i))
						SocketUtil.writeStr2Stream(context , SocketServer.getClientByID(i).getOutputStream());
				}
				System.out.println(binaryString);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				try {
					request.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

package mTe;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TeServer {
	private ServerSocket server;
	private Thread threadSrv;
	private List<Thread> receiveThds;
	
	public TeServer(int port) {
		try {
			System.out.println("Server start");
			server = new ServerSocket(port);
	        receiveThds = new ArrayList<Thread>();
	        threadSrv = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (!Thread.interrupted()) {
							Socket socket = server.accept();
							System.out.println("Server receive connect:"+socket.getPort());
							Thread receive = new Thread(new Task(socket));
							receiveThds.add(receive);
							receive.start();
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			});
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}

	public void Start() {
		threadSrv.start();
	}
	
	public void Stop() {
		for (Thread thread : receiveThds) {
        	thread.interrupt();
        }
		
		threadSrv.interrupt();
		System.out.println("TeServer Stop");
	}
	
    static class Task implements Runnable {
        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                handleSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handleSocket() throws Exception {
	        // 接受请求后使用Socket进行通信，创建BufferedReader用于读取数据
        	Reader read = new InputStreamReader(socket.getInputStream());
            Writer writer = new OutputStreamWriter(socket.getOutputStream());
        	char ch[] = new char[1024];
	        while (!Thread.interrupted()) {
	        	try {
		        	int len = read.read(ch);
			        System.out.println(socket.getPort()+" received frome client:len="+ len + ", "+ String.valueOf(ch, 0, len));
	        	} catch (Exception e) {
	        		System.out.println("socket close.");
	        		break;
	        	}
	            writer.write("Hello, Client");
	            writer.flush();
	        }

        	try {
                writer.close();
                read.close();
        		socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	ch = null;
        }
    }
}

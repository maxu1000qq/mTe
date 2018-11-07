package mTe;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class TeClient {
	private Socket client;
	private Writer write;
	private Reader read;
	private Thread threadCli;

	public TeClient(String dstIp, int port) {
		try {
			System.out.println("Client start");
			client = new Socket(dstIp, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Start() {
        threadCli = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.interrupted()) {
						if (client.isConnected()) {
							read = new InputStreamReader(client.getInputStream());
							char ch[] = new char[1024];
							while (!Thread.interrupted()) {
						        int len = read.read(ch);
						        System.out.println("client:received frome server:len="+ len + ", "+ String.valueOf(ch, 0, len));
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				
				System.out.println("Client thread terminate!");
				
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        threadCli.start();
	}

	public void Stop() {
		threadCli.interrupt();
		System.out.println("Client stop");
	}

	public boolean Send() {
		if (client.isConnected()) {
			Writer writer;
			try {
				writer = new OutputStreamWriter(client.getOutputStream());
		        writer.write("ep=JRWBSNCNTLQSCJLQ&pw=123456");
		        writer.flush();
		        return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Server socket close.");
				return false;
			}
		}
		
		return false;
	}
}

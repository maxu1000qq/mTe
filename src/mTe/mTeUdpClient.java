package mTe;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class mTeUdpClient {
	private DatagramSocket udpHandler;
	//private Object semaphore;
	InetAddress dstAddress;
	//private String dstIp;
	private int port;
	private Thread threadCli;
	private int sendHbCnt = 0;
	private int recvHbCnt = 0;
	byte[] recvBuffer;
	private int bufferLen = 1024 * 2;
	private boolean isRegOk = false;

	public mTeUdpClient(String dstIp, int port) {
		try {
			System.out.println("Udp Client start");
			//this.dstIp = dstIp;
			this.port = port;
			try {
				recvBuffer = new byte[bufferLen];
				dstAddress = InetAddress.getByName(dstIp);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			udpHandler = new DatagramSocket(6000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public mTeUdpClient() {
		try {
			System.out.println("Udp Client start");
			recvBuffer = new byte[bufferLen];
			udpHandler = new DatagramSocket(7000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void Start(Object semaphore) {
        //this.semaphore = semaphore;
		threadCli = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.interrupted()) {
						DatagramPacket packet = new DatagramPacket(recvBuffer, bufferLen);
						udpHandler.receive(packet);
						String recvMsg = new String(recvBuffer, 0, packet.getLength());
						SocketAddress addr = packet.getSocketAddress();
						System.out.println("received frome "+ addr.toString() + ", len=" + packet.getLength());
						ProcMsg(addr, recvMsg);
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				
				udpHandler.close();
			}
		});
        threadCli.start();
	}

	public void Stop() {
		threadCli.interrupt();
		System.out.println("Client stop");
	}

	public boolean SendHello() {
		try {
			System.out.println("Send Register");
			byte[] data = "ep=JRWBSNCNTLQSCJLQ&pw=123456".getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, dstAddress, port);
			udpHandler.send(packet);
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Server socket close.");
			return false;
		}
	}

	public boolean SendHeartbeat() {
		try {
			sendHbCnt++;
			String msg = "Heartbeat from pc:" + sendHbCnt;
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, dstAddress, port);
			System.out.println("Send:"+msg);
			udpHandler.send(packet);
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Server socket close.");
			return false;
		}
	}
	
	public boolean Send(SocketAddress addr, byte[] data) {
		try {
			System.out.println("send msg.");
			DatagramPacket packet = new DatagramPacket(data, data.length, addr);
			udpHandler.send(packet);
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Server socket close.");
			return false;
		}
	}
	
	public void printCount()
	{
		System.out.println("information: send HB packet=" + sendHbCnt
				+ ", recv HB packet=" + recvHbCnt);
	}
	
	private void ProcMsg(SocketAddress addr, String msg) {
		/*if (msg.contains("Heartbeat")) {
			recvHbCnt++;
			int index = msg.indexOf(":");
			String deviceCnt = msg.substring(index+1);
			int cnt = Integer.valueOf(deviceCnt);
			System.out.println(msg + ": device Hb:" + cnt);
		} else if (msg.contains("iotxx:ok") || msg.contains("iotxx:update")) {
			synchronized(semaphore) {
				semaphore.notify();
				isRegOk = true;
				System.out.println(msg + ": Register to platform OK.");
			}
		} else*/ {
			try {
				byte[] binaryData = msg.getBytes("ISO-8859-1");
				for (int i = 0; i < binaryData.length; i++)
				{
					System.out.print(String.format("%02x", binaryData[i]));
				}
				System.out.println("");
//				if (binaryData.length > 4) {
//					binaryData[binaryData.length - 1] = 0x44;
//					binaryData[binaryData.length - 2] = 0x33;
//					binaryData[binaryData.length - 3] = 0x22;
//					binaryData[binaryData.length - 4] = 0x11;
//				}
				Send(addr, binaryData);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean getRegState() {
		return isRegOk;
	}
}

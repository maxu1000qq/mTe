package mTe;

public class mTe {
    public static void main(String[] args) {
    	System.out.println("Hello mTe!");
    	
    	//Object semaphore = new Object();
    	
   		/*TeServer server = new TeServer(61111);
   		server.Start();
   		// server wait
   		while(!Thread.interrupted())
   		{

   		}
   		server.Stop();*/

   		//TeClient client = new TeClient("10.244.1.34", 31443); // connection timeout
    	/*mTeUdpClient client = new mTeUdpClient("115.29.240.46", 6000); // nodeip OK
   		client.Start(semaphore);
   		// client send message
   		while (true) {
   	   		//client.SendHello();
   	   		try {
   				synchronized(semaphore) {
   					semaphore.wait(5000); // 5s
   					if (client.getRegState()) {
   						break;
   					}
   				}
   			} catch (InterruptedException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
   		}

   		while(!Thread.interrupted())
   		{
   			//client.SendHeartbeat();
   			try {
				Thread.sleep(20000); // ms
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   		}
   		client.Stop();*/
    	
    	mTeUdpClient client = new mTeUdpClient();
   		client.Start(null);
    	while(!Thread.interrupted()) {
   			/*client.SendHeartbeat();
   			try {
				Thread.sleep(20000); // ms
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    	}

   		System.out.println("Bye mTe!");
    }
}

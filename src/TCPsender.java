import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class TCPsender extends Thread{

    private long k;
    private int n;
    private double rate;
    private int sendingTime;

    public TCPsender(long k, int n, int sendingTime ) {
        this.k = k;
        this.n = n;
        this.sendingTime = sendingTime;
    }

    @Override
    public void run() {
        long packetCounter = 0;

        try {
            int port = 3000;
            byte[] outbuf = new byte[1400];

            long startTime = System.currentTimeMillis();
            long stopTime = startTime + sendingTime * 1000;

            Socket so = new Socket("localhost",port);

            //client
           OutputStream output = so.getOutputStream();

            while(startTime < stopTime){
                if((packetCounter % n) == 0)
                    sleep(k);
                output.write(outbuf);
                packetCounter++;

                startTime = System.currentTimeMillis();


            }
            rate = (packetCounter*1400*8/1000.) / sendingTime;
            so.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sender: rate " + rate + " kbit/s");
    }
}

import java.io.IOException;
import java.net.*;

public class UDPsender extends Thread{

    private long k;
    private int n;
    private double rate;

    public UDPsender(long k, int n) {
        this.k = k;
        this.n = n;
    }

    @Override
    public void run() {

        try {
            InetAddress dst = InetAddress.getLocalHost();
            int port = 3000;
            byte[] outbuf = new byte[1400];
            int len = 1400;
            DatagramPacket request = new DatagramPacket(outbuf, len, dst, port);
            DatagramSocket socket = new DatagramSocket();

            long startTime = System.currentTimeMillis();
            long stopTime = startTime + 1000;

            long packetCounter = 0;

            while(startTime < stopTime){
                if((packetCounter % n) == 0)
                    sleep(k);
                socket.send(request);
                packetCounter++;

                startTime = System.currentTimeMillis();

                rate = (packetCounter*1400*8/1000.);
            }


        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("Sender: rate " + rate + " kbit/s");
    }

    public static void main(String[] args) throws Exception{

    }
}

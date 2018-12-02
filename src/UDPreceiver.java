import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UDPreceiver extends Thread{
    private DatagramSocket ds;
    private byte[] buf = new byte[1400];
    private DatagramPacket dp = new DatagramPacket(buf, 1400); ;
    private int intervall = 30000;
    private int packetCounter = 0;
    private long startTime;
    private long stopTime;
    private boolean weiterGehts = true;
    private double humanTime;
    private double rate;

    public static void main(String[] args) throws Exception {
        UDPreceiver receiver = new UDPreceiver();
        receiver.start();
        UDPsender sender = new UDPsender(20, 100);
        sender.start();
    }

    @Override
    public void run(){
        try{
            ds = new DatagramSocket(3000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        while(weiterGehts){
            try {
                ds.setSoTimeout(intervall);
                ds.receive(dp);
                packetCounter++;
            } catch (IOException e) {
                if(e instanceof SocketTimeoutException){
                    stopTime = System.currentTimeMillis();
                    weiterGehts = false;
                }else{
                    e.printStackTrace();
                }
            }
            humanTime = ((stopTime - startTime) - intervall) / 1000.0;
        }
        ds.close();
        // (packetCounter * length * 8 Bit / kilo) / time
        rate = packetCounter * 1400 * 8/1000.0 / humanTime;

        System.out.println("Receiver: rate "+ rate + " kbit/s");
    }
}

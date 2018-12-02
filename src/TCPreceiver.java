import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class TCPreceiver extends Thread{
    private ServerSocket ss;
    private Socket acceptSocket;
    private byte[] buf = new byte[1400];
    private long packetCounter = 0;
    private long startTime;
    private long stopTime;
    private boolean weiterGehts = true;
    private double humanTime;
    private double rate;

    public static void main(String[] args) throws Exception {
        int k = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);

        TCPreceiver receiver = new TCPreceiver();
        receiver.start();
        TCPsender sender = new TCPsender(k, n, 20);
        sender.start();
    }

    @Override
    public void run(){
        try{
            ss = new ServerSocket(3000);
            acceptSocket = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startTime = System.currentTimeMillis();
        int input = 0;

        try(InputStream is = acceptSocket.getInputStream()){
            while(input != -1) {
                input = is.read(buf);
                packetCounter++;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        stopTime = System.currentTimeMillis();
        humanTime = (stopTime - startTime) / 1000.0;
        // (packetCounter * length * 8 Bit / kilo) / time
        long step = packetCounter * 1400 * 8 ;
        rate = (step/1000.0) / humanTime;

        System.out.println("Receiver: rate "+ rate + " kbit/s");
    }
}

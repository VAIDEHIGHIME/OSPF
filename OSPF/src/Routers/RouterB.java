package Routers;
import java.net.InetAddress;
import java.util.ArrayList;

public class RouterB{
    
     /*
                        ========================
                            NETWORK TOPOLOGY
                        ========================
                                  3
                        A_____________________B
                        |                     |
                        |                     |
                        |                     |
                        |                     |
                    2   |                     |
                        |                     |   1
                        |                     |
                        |                     |
                        |                     |
                        D_____________________C
    
                                   4
        
        */
     public static void main(String[] args) throws Exception{ 
        
        //info stored at Router B
        int portB=4444;
        int portA=9999;
        int portC=4521;
        InetAddress ipB=InetAddress.getLocalHost();
        InetAddress ipA=InetAddress.getLocalHost();
        InetAddress ipC=InetAddress.getLocalHost(); 
        String Self="B";
        String SequenceNo="2";
        String TTL="2";
        String neighbourhood;
        neighbourhood = "A/3-C/1";
        String Seperator="======================================================\n";
        
        
        Router B=new Router(portB,ipB);
        System.out.println("Welcome This is Router B\n");        
        System.out.println(Seperator);
        B.setNeighbourhood(neighbourhood);
        System.out.println("These are the neighbours\n");
        B.printNeighbour();
        System.out.println(Seperator);
        
        B.createLSDB(Self,SequenceNo,TTL);
        //B.Sent("B-7287-20"+B.setLSPStringFromNeighbours(),ipA,9999); 
        //B.Recieve();//recieve from A
        //B.Recieve();//recieve from C
        //B.Sent("B-7287-20"+B.setLSPStringFromNeighbours(),ipA,4522); 
        String c;
        c = B.ControlSignal();//recieve from A
        int count=Integer.parseInt(c.trim());
        for(int i=0;i<count;i++){
            B.Recieve(Self);
        }
        ArrayList<String> LSP;//sent to A and C
        LSP = B.setLSDBString();
        B.Sent(String.valueOf(LSP.size()), ipA, portA);
        B.Sent(String.valueOf(LSP.size()), ipC, portC);
        for(int i=0;i<LSP.size();i++){
            //System.out.println("Sending:"+LSP.get(i));
            B.Sent(LSP.get(i), ipA, portA);
            B.Sent(LSP.get(i), ipC, portC);

        }  
        c = B.ControlSignal();//recieve from C
        count=Integer.parseInt(c.trim());
        for(int i=0;i<count;i++){
            B.Recieve(Self);
        }
        c = B.ControlSignal();//recieve from A
        count=Integer.parseInt(c.trim());
        for(int i=0;i<count;i++){
            B.Recieve(Self);
        }
        System.out.println("This is the LSDB\n");
        B.printLSDB(); 
        System.out.println(Seperator);
        System.out.println("Starting Routing.....\n");
        B.CreateRoutingTable(Self); 
        System.out.println("End");
       
     }
    
}
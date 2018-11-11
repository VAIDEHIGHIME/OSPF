package Routers;

import java.net.InetAddress;
import java.util.ArrayList;

public class RouterA{
    
        /*
                        ========================
                            NETWORK TOPOLOGY
                        ========================
                                 3
                        A_____________________B
                        |                     |
                        |                     |
                        |                     |
                      2 |                     |
                        |                     | 1
                        |                     |
                        |                     |
                        |                     |
                        D_____________________C
                                  4
        
        */
        
    public static void main(String[] args) throws Exception{
        
        //Information stored at A
        int portA=9999;
        int portB=4444;
        int portD=4522;
        InetAddress ipA=InetAddress.getLocalHost();
        InetAddress ipB=InetAddress.getLocalHost();
        InetAddress ipD=InetAddress.getLocalHost();
        String Self="A";
        String SequenceNo="1";
        String TTL="2";
        String neighbourhood;
        neighbourhood = "B/3-D/2";
        String Seperator="======================================================\n";
        
        
        Router A=new Router(portA,ipA);
        System.out.println("Welcome This is Router A\n");
        System.out.println(Seperator);        
        A.setNeighbourhood(neighbourhood);
        System.out.println("These are the neighbours\n");
        A.printNeighbour();
        System.out.println(Seperator);
        
        
        A.createLSDB(Self,SequenceNo,TTL);
        //A.Recieve();//recieve from B
        //A.Recieve();//recieve from c            
        //A.Sent(Self+"-"+SequenceNo+"-"+TTL+A.setLSPStringFromNeighbours(),ipB,portB);
        //A.Sent(Self+"-"+SequenceNo+"-"+TTL+A.setLSPStringFromNeighbours(),ipC,portC);
        
        A.ControlSignal();
        
        ArrayList<String> LSP=new ArrayList<>();//sent to B and D
        LSP = A.setLSDBString();
        A.Sent(String.valueOf(LSP.size()), ipB, portB);
        A.Sent(String.valueOf(LSP.size()), ipD, portD);
        for(int i=0;i<LSP.size();i++){
           // System.out.println("Sending:"+LSP.get(i));
            A.Sent(LSP.get(i), ipB, portB);
            A.Sent(LSP.get(i), ipD, portD);

        }
        String c=A.ControlSignal();//recieve from B
        int count=Integer.parseInt(c);
        for(int i=0;i<count;i++){
            A.Recieve(Self);
        }
        c=A.ControlSignal();//recieve from D
        count=Integer.parseInt(c);
        for(int i=0;i<count;i++){
            A.Recieve(Self);
        }
        LSP=new ArrayList<>();//sent to B and D
        LSP = A.setLSDBString();
        A.Sent(String.valueOf(LSP.size()), ipB, portB);
        A.Sent(String.valueOf(LSP.size()), ipD, portD);
        for(int i=0;i<LSP.size();i++){
           // System.out.println("Sending:"+LSP.get(i));
            A.Sent(LSP.get(i), ipB, portB);
            A.Sent(LSP.get(i), ipD, portD);

        }
        System.out.println("This is the LSDB\n");
        A.printLSDB(); 
        System.out.println(Seperator);
        System.out.println("Starting Routing.....\n");
        A.CreateRoutingTable(Self); 
        System.out.println("End");
        
        
        
        
    }
}  

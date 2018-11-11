package Routers;

import java.net.InetAddress;
import java.util.ArrayList;

public class RouterC{
    
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
         
        //information stored at Router C
        int portC=4521;
        int portB=4444;
        int portD=4522;
        InetAddress ipC=InetAddress.getLocalHost();
        InetAddress ipB=InetAddress.getLocalHost();
        InetAddress ipD=InetAddress.getLocalHost(); 
        String Self="C";
        String SequenceNo="3";
        String TTL="2";
        String neighbourhood;
        neighbourhood = "B/1-D/4";
        String Seperator="======================================================\n";
        
        
        Router C=new Router(portC,ipC);
        System.out.println("Welcome This is Router C\n");  
        System.out.println(Seperator);
        C.setNeighbourhood(neighbourhood);
        System.out.println("These are the neighbours\n");
        C.printNeighbour(); 
        System.out.println(Seperator);
        
        C.createLSDB(Self,SequenceNo,TTL);
        //C.Sent("C-4838-394"+C.setLSPStringFromNeighbours(),ipA,9999);
        //C.Recieve();
        //C.Recieve();
        //C.Sent("C-4838-3"+C.setLSPStringFromNeighbours(),ipA,4522);
        String c=C.ControlSignal();//recieve from B
        int count=Integer.parseInt(c.trim());
        for(int i=0;i<count;i++){
            C.Recieve(Self);
        }
        ArrayList<String> LSP;//sent to B and D
        LSP = C.setLSDBString();
        C.Sent(String.valueOf(LSP.size()), ipB, portB);
        C.Sent(String.valueOf(LSP.size()), ipD, portD);
        for(int i=0;i<LSP.size();i++){
           // System.out.println("Sending:"+LSP.get(i));
            C.Sent(LSP.get(i), ipB, portB);
            C.Sent(LSP.get(i), ipD, portD);

        }
        c=C.ControlSignal();//recieve from D
        count=Integer.parseInt(c);
        for(int i=0;i<count;i++){
            C.Recieve(Self);
        }
        System.out.println("This is the LSDB\n");
        C.printLSDB();
        System.out.println(Seperator); 
        System.out.println("Starting Routing.....\n");
        C.CreateRoutingTable(Self); 
        System.out.println("End");
        }
    
}
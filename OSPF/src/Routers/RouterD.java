package Routers;

import java.net.InetAddress;
import java.util.ArrayList;

public class RouterD{
    
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
        int portD=4522;
        int portA=9999;
        int portC=4521;
        InetAddress ipD=InetAddress.getLocalHost(); 
        InetAddress ipA=InetAddress.getLocalHost();
        InetAddress ipC=InetAddress.getLocalHost(); 
        String Self="D";
        String SequenceNo="4";
        String TTL="2";
        String neighbourhood;
        neighbourhood = "A/2-C/4";
        String Seperator="======================================================\n";
        
        Router D=new Router(portD,ipD);
        System.out.println("Welcome This is Router D\n");  
        System.out.println(Seperator);
        D.setNeighbourhood(neighbourhood);
        System.out.println("These are the neighbours\n");
        D.printNeighbour(); 
        System.out.println(Seperator);
        
        D.createLSDB(Self,SequenceNo,TTL);
        //D.Sent("D-4838-34"+D.setLSPStringFromNeighbours(),ipB,4444);
       // D.Sent("D-4838-34"+D.setLSPStringFromNeighbours(),ipB,4521);
       // D.Recieve();
       // D.Recieve();
       D.Sent("1",ipA,portA);
        
        String c=D.ControlSignal();//recieve from A
        int count=Integer.parseInt(c);
        for(int i=0;i<count;i++){
            D.Recieve(Self);
        }
        
        c=D.ControlSignal();//recieve from C
        count=Integer.parseInt(c);
        for(int i=0;i<count;i++){
            D.Recieve(Self);
        }
        ArrayList<String> LSP;
        LSP = D.setLSDBString();
        D.Sent(String.valueOf(LSP.size()), ipA, portA);
        D.Sent(String.valueOf(LSP.size()), ipC, portC);
        for(int i=0;i<LSP.size();i++){
            //System.out.println("Sending:"+LSP.get(i));
            D.Sent(LSP.get(i), ipA,portA);
            D.Sent(LSP.get(i), ipC,portC);

        }
        c=D.ControlSignal();//recieve from A
        count=Integer.parseInt(c);
        for(int i=0;i<count;i++){
            D.Recieve(Self);
        }
        System.out.println("This is the LSDB\n");
        D.printLSDB();
        System.out.println(Seperator);
        System.out.println("Starting Routing.....\n");
        D.CreateRoutingTable(Self); 
        System.out.println("End");
    }
    
}
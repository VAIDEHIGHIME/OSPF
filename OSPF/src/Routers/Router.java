package Routers;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

public class Router{   
    //Class Variable
    private DatagramSocket socket;
    private byte[] buffer;
    private InetAddress ia;
    private int port;  
    private ArrayList<NeighbourInfo> neighbourhood;
    private ArrayList<LSP> LSDB;
    private ArrayList<LSPmainContent> TentativeList=new ArrayList<>();
    private ArrayList<LSPmainContent> ConfirmedList=new ArrayList<>();
    
    
  
    public int getLSBDSize(){
        return(this.LSDB.size());
    }
   
    
    public Router(int port,InetAddress ia) throws SocketException, UnknownHostException{
        this.neighbourhood = new ArrayList<>();
        this.LSDB=new ArrayList<>();
        this.ia = ia;
        this.port=port;
        this.socket=new DatagramSocket(this.port);
        
    }    
    
    public void Sent(String s,InetAddress ia,int destPort) throws UnknownHostException, IOException{
        this.buffer=new byte[1024];
        this.buffer=s.getBytes(); 
        DatagramPacket dp=new DatagramPacket(this.buffer, this.buffer.length,ia,destPort);
        this.socket.send(dp);
        //System.out.println("Packet Sent");       
    }
    public String ControlSignal() throws IOException{
        this.buffer=new byte[1024];
        DatagramPacket dp= new DatagramPacket(this.buffer,this.buffer.length);
        this.socket.receive(dp);
        String str=new String(dp.getData(),0,dp.getLength());
        //System.out.println("Router Recieved:" + str);
        return(str);    
    }
    
    public void Recieve(String Self) throws IOException{
        this.buffer=new byte[1024];
        DatagramPacket dp= new DatagramPacket(this.buffer,this.buffer.length);
        this.socket.receive(dp);
        String str=new String(dp.getData(),0,dp.getLength());
        //System.out.println("Router Recieved:" + str);
        String[] Recievedouter;
        Recievedouter = str.split("-");
        /*for (String Recievedouter1 : Recievedouter) {
            System.out.println(Recievedouter1);
        }*/
        LSP RecievedLSP=new LSP();
        RecievedLSP.sender=Recievedouter[0];
        RecievedLSP.sequenceNo=Recievedouter[1];
        RecievedLSP.TTL=Recievedouter[2];
        int j=3;
        while(j<Recievedouter.length){
            String[] Recievedinner=Recievedouter[j].split("/");          
            LSPmainContent infoRecieved=new LSPmainContent();
            for(int i=0;i<Recievedinner.length;i++){
                infoRecieved.router=Recievedinner[i];
                i=i+1;
                infoRecieved.cost=Recievedinner[i];
                i=i+1;
                infoRecieved.hop=Recievedinner[i];
                RecievedLSP.routersAndTheirCostToReach.add(infoRecieved);  
                
            }
                             
            j=j+1;
        }      
        bufferringRecievedLSPList(RecievedLSP);
        filterLSDB(Self);
        
    }
    
    private void KnowingNeighbours(NeighbourInfo info){
             this.neighbourhood.add(info);           
    }
    public void printNeighbour(){
        for(int i=0;i<this.neighbourhood.size();i++){
            System.out.println("Neighbour:"+this.neighbourhood.get(i).router+"\t"+"Cost:"+this.neighbourhood.get(i).cost);
        }
        System.out.println("");
    }
    private void printLSP(ArrayList<LSPmainContent> info){
        int i=0;
        while(i<info.size()){
            System.out.println("Router:"+info.get(i).router+"\t"+"Cost:"+info.get(i).cost+"\t"+"Hop:"+info.get(i).hop);
                i++;
        }
        System.out.println("");
    }
    
    public void bufferringRecievedLSPList(LSP info){
        this.LSDB.add(info);
    }
    public void printLSDB(){
        
        for(int i=0;i<this.LSDB.size();i++){
            System.out.println("Sender:"+this.LSDB.get(i).sender+"\t"+"Sequence No:"+this.LSDB.get(i).sequenceNo+"\t"+"TTL:"+this.LSDB.get(i).TTL); 
            printLSP(this.LSDB.get(i).routersAndTheirCostToReach);
        }
    } 
    public void setNeighbourhood(String neighbourhood){
        String[] outerInfo;
        String[] innerInfo;
        outerInfo=neighbourhood.split("-");
        for (String _outerInfo : outerInfo) {
            innerInfo = _outerInfo.split("/");
            int j=0;
            NeighbourInfo info=new NeighbourInfo();
            while(j<innerInfo.length-1){               
                info.router=innerInfo[j];
                j=j+1;
                info.cost=innerInfo[j];
                j=j+1;
            }
            KnowingNeighbours(info);
        }
    }
    
    /*public String setLSPStringFromNeighbours(){
        String LSPString ="-";
        for(int i=0;i<this.neighbourhood.size();i++){
            if(i==this.neighbourhood.size()-1){
                LSPString=LSPString+this.neighbourhood.get(i).router+"/"+this.neighbourhood.get(i).cost+"/"+this.neighbourhood.get(i).router;
                
            }
            else{
                LSPString=LSPString+this.neighbourhood.get(i).router+"/"+this.neighbourhood.get(i).cost+"/"+this.neighbourhood.get(i).router+"-";
                
            }
        }
       return(LSPString);    
    }*/
     
    public void createLSDB(String Self,String sequenceNo,String TTL){
        addSelf(Self,sequenceNo,TTL);       
    }

    private void addSelf(String Self,String sequenceNo,String TTL) {
        LSP e=new LSP();
        e.sender=Self;
        e.sequenceNo=sequenceNo;
        e.TTL=TTL;
        LSPmainContent f= new LSPmainContent();
        f.router=Self;
        f.cost="0";
        f.hop="_";
        e.routersAndTheirCostToReach.add(f);
        for(int i=0;i<this.neighbourhood.size();i++){
            LSPmainContent g= new LSPmainContent();
            g.router=this.neighbourhood.get(i).router;
            g.cost=this.neighbourhood.get(i).cost;
            g.hop=this.neighbourhood.get(i).router;
            e.routersAndTheirCostToReach.add(g);      
        }      
        this.LSDB.add(e);
    }
    public ArrayList<String> setLSDBString(){
        ArrayList<String> ListOfInfoToSend=new ArrayList<>();
        String infoTOSend ="";        
        LSP info=new LSP();
        for(int i=0;i<this.LSDB.size();i++){
            info=this.LSDB.get(i);
            infoTOSend = info.sender+"-"+info.sequenceNo+"-"+info.TTL+"-";
            for(int j=0;j<this.LSDB.get(i).routersAndTheirCostToReach.size();j++){ 
                if(j==this.LSDB.get(i).routersAndTheirCostToReach.size()-1){
                infoTOSend=infoTOSend+this.LSDB.get(i).routersAndTheirCostToReach.get(j).router+"/"+this.LSDB.get(i).routersAndTheirCostToReach.get(j).cost+"/"+this.LSDB.get(i).routersAndTheirCostToReach.get(j).hop;   
                }
                else{
                    infoTOSend=infoTOSend+this.LSDB.get(i).routersAndTheirCostToReach.get(j).router+"/"+this.LSDB.get(i).routersAndTheirCostToReach.get(j).cost+"/"+this.LSDB.get(i).routersAndTheirCostToReach.get(j).hop+"-";   
             
                    
                }
            }
                   
            ListOfInfoToSend.add(infoTOSend);
        }
        return(ListOfInfoToSend);  
    }  
    
    private void filterLSDB(String Self){
        for(int i=0;i<this.LSDB.size();i++){
            for(int j=i+1;j<this.LSDB.size();j++){
                if(this.LSDB.get(i).sequenceNo == null ? this.LSDB.get(j).sequenceNo == null : this.LSDB.get(i).sequenceNo.equals(this.LSDB.get(j).sequenceNo)){
                    this.LSDB.remove(j);
                }
            }
            Collections.sort(this.LSDB.get(i).routersAndTheirCostToReach,new sortLSPmainContentByCost());
            removeDummyHops(Self, i);
        }
        
    }
    
    private void removeDummyHops(String Self,int i){
        if(this.LSDB.get(i).sender == null ? Self != null : !this.LSDB.get(i).sender.equals(Self)){
            for(int j=0;j<this.LSDB.get(i).routersAndTheirCostToReach.size();j++){
                if("_".equals(this.LSDB.get(i).routersAndTheirCostToReach.get(j).hop)){
                    this.LSDB.get(i).routersAndTheirCostToReach.remove(j);
                }
                
            }
            
        }
        
    }
    
    public void CreateRoutingTable(String Self){
        
        initializeLists(Self);
        shortestPathAlgo();  
        //printRoutingTable();
        printRoutingTable();
                    
    }
    
    private void initializeLists(String Self){
        for(int i=0;i<this.LSDB.size();i++){
            if(this.LSDB.get(i).sender == null ? Self == null : this.LSDB.get(i).sender.equals(Self)){
                for(int j=0;j<this.LSDB.get(i).routersAndTheirCostToReach.size();j++){
                    if("0".equals(this.LSDB.get(i).routersAndTheirCostToReach.get(j).cost)&&"_".equals(this.LSDB.get(i).routersAndTheirCostToReach.get(j).hop) ){
                        this.ConfirmedList.add(this.LSDB.get(i).routersAndTheirCostToReach.get(j));
                    }
                    else{
                        this.TentativeList.add(this.LSDB.get(i).routersAndTheirCostToReach.get(j));
                    }
                    
                }
                
            }
        }
        printLists();
        System.out.println("[+]");
        
    }
    
    private void shortestPathAlgo(){
        ArrayList<LSP> LSDB_Copy;       
        LSDB_Copy = (ArrayList<LSP>) this.LSDB.clone();
        while(!this.TentativeList.isEmpty()){
            LSPmainContent removed=this.TentativeList.remove(0);
            this.ConfirmedList.add(removed);
            // search for the same sender
            //here we get the lsa of a same sender that 
            //was sent in the confirmed list then
            //check all the lsp if the router  is equal
            //to that of the routers of the tentative list
            //the update else add to tentative list only if 
            //they are not present in the confirmed list 
            for(int i=0;i<LSDB_Copy.size();i++){
                if(removed.router == null ? LSDB_Copy.get(i).sender == null : removed.router.equals(LSDB_Copy.get(i).sender)){
                    for(int j=0;j<LSDB_Copy.get(i).routersAndTheirCostToReach.size();j++){
                        for(int k=0;k<this.TentativeList.size();k++){
                            if(LSDB_Copy.get(i).routersAndTheirCostToReach.get(j).router == null ? this.TentativeList.get(k).router != null : !LSDB_Copy.get(i).routersAndTheirCostToReach.get(j).router.equals(this.TentativeList.get(k).router)){
                                //check if it is present in the confirmed list
                                int visited=0;
                                for(int l=0;l<this.ConfirmedList.size();l++){
                                    if(this.TentativeList.get(k).router == null ? this.ConfirmedList.get(l).router == null : this.TentativeList.get(k).router.equals(this.ConfirmedList.get(l).router)){
                                        this.TentativeList.remove(k);
                                        visited = 1;
                                        break;                                        
                                    }                                    
                                }
                                if(visited!=1){
                                        this.TentativeList.add(LSDB_Copy.get(i).routersAndTheirCostToReach.get(j));
                                        this.TentativeList.get(this.TentativeList.size()-1).cost=String.valueOf(Integer.parseInt(this.TentativeList.get(this.TentativeList.size()-1).cost.trim())+Integer.parseInt(removed.cost.trim()));
                                        this.TentativeList.get(this.TentativeList.size()-1).hop=removed.hop;
                                        //Collections.sort(this.TentativeList,new sortLSPmainContentByCost());
                                        
                                }
                            }
                            else{
                                //update
                                if(Integer.parseInt(this.TentativeList.get(k).cost.trim())>Integer.parseInt(removed.cost.trim())+Integer.parseInt(LSDB_Copy.get(i).routersAndTheirCostToReach.get(j).cost.trim())){
                                    this.TentativeList.get(k).cost=String.valueOf(Integer.parseInt(removed.cost.trim())+Integer.parseInt(LSDB_Copy.get(i).routersAndTheirCostToReach.get(j).cost.trim()));
                                    this.TentativeList.get(k).hop=removed.hop;
                                    //Collections.sort(this.TentativeList,new sortLSPmainContentByCost());
                                        
                                    
                                    
                                }
                                
                            }
                        }
                        
                        
                    }
                    
                }
            } 
            
            printLists();
            System.out.println("[+]");
        }
        
        
        
    }
    
    private void printLists(){
        System.out.println("This is the Tentative List\n");
        printLSP(this.TentativeList);
        
        System.out.println("This is the Confirmed List\n");
        printLSP(this.ConfirmedList);        
    }
    
   /* private void printRoutingTable(){
        final Object[][] table = new String[this.ConfirmedList.size()][];        
        table[0] = new String[] { "Target", "Cost", "Hop" };             
        int j=1;
        for(int i=0;i<this.ConfirmedList.size();i++){
            table[j]=new String[] {this.ConfirmedList.get(i).router,this.ConfirmedList.get(i).cost,this.ConfirmedList.get(i).hop};
        }
        for (final Object[] row : table) {
            System.out.format("%-15s%-15s%-15s\n", row);
        }
       
        
    }*/
    private void printHeader() {
        System.out.println(String.format("%30s %25s %10s %25s %10s", "Destinition", "|", "Cost", "|", "Hop"));
        System.out.println(String.format("%s", "----------------------------------------------------------------------------------------------------------------"));
    }
    private void printItems() {
        for(int i=0;i<this.ConfirmedList.size();i++){
        System.out.println(String.format("%30s %25s %10.2s %25s %10s", this.ConfirmedList.get(i).router, "|", this.ConfirmedList.get(i).cost, "|", this.ConfirmedList.get(i).hop));
        }
    }
    
    private void printRoutingTable(){
        System.out.println("Routing Table\n");
        printHeader();
        printItems();
    }  
    
}
package Routers;

import java.util.ArrayList;

public class LSP
{
    //variables of this class
    public String sender;
    public String sequenceNo;
    public String TTL;   
    public ArrayList<LSPmainContent> routersAndTheirCostToReach;

    public LSP() {
        this.routersAndTheirCostToReach = new ArrayList<>();
    }
}
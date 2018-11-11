package Routers;

import java.util.Comparator;

class sortLSPmainContentByCost implements Comparator<LSPmainContent> 
{ 
    // Used for sorting in ascending order of cost
    @Override
    public int compare(LSPmainContent a, LSPmainContent b) 
    { 
        return(Integer.parseInt(a.cost.trim()) - Integer.parseInt(b.cost.trim())); 
    } 
} 
public class node{
    private int[] routeTable;

    public node(int[] init){
        routeTable = new int[4];
        for(int i = 0; i < 4; i++)
            routeTable[i] = init[i];
    }

    public boolean rtUpdate(int[] rcvdTable){
        boolean flag = false;

        for(int i = 0; i < 4; i++)
            if(routeTable[i] > rcvdTable[i] + 1){
                routeTable[i] = rcvdTable[i] + 1;
                flag = true;
            }
        return flag;
    }

    public int[] getTable(){
        return routeTable;
    }

}

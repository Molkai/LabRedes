public class node{
    private int[] routeTable;
    static private int id;

    public node(int[] init, int id){
        routeTable = new int[4];
        this.id = id;

        for(int i = 0; i < 4; i++)
            routeTable[i] = init[i];
    }

    public boolean rtUpdate(int[] rcvdTable, int id){
        boolean flag = false;

        for(int i = 0; i < 4; i++)
            if(routeTable[i] > rcvdTable[i] + routeTable[id]){
                routeTable[i] = rcvdTable[i] + routeTable[id];
                flag = true;
            }
        return flag;
    }

    public int[] getTable(){
        return routeTable;
    }
}

public class node{
    private int[][] routeTable;
    static private int id;

    public node(int[][] init, int id){
        routeTable = new int[4][2];
        this.id = id;

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 2; j++)
                routeTable[i][j] = init[i][j];
    }

    public boolean rtUpdate(int[] rcvdTable, int id){
        boolean flag = false;

        for(int i = 0; i < 4; i++)
            if(routeTable[i] > rcvdTable[i] + routeTable[id]){
                routeTable[i][0] = rcvdTable[i] + routeTable[id];
                routeTable[i][1] = id;
                flag = true;
            }
        return flag;
    }

    public int[] getTable(){
        return routeTable;
    }
}

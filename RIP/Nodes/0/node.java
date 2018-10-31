public class node{
    private int[][] routeTable;
    private int[] direct;
    static private int id;

    public node(int[][] init, int id){
        routeTable = new int[4][2];
        direct = new int[4];
        this.id = id;

        for(int i = 0; i < 4; i++){
            direct[i] = init[i][0];
            for(int j = 0; j < 2; j++)
                routeTable[i][j] = init[i][j];
        }
    }

    public boolean rtUpdate(int[] rcvdTable, int id){
        boolean flag = false;

        for(int i = 0; i < 4; i++)
            if(routeTable[i][0] > rcvdTable[i] + direct[id]){
                routeTable[i][0] = rcvdTable[i] + direct[id];
                routeTable[i][1] = id;
                flag = true;
            }
        return flag;
    }

    public void printTable(){
        for(int i = 0; i < 4; i++)
            System.out.printf("Node[%d]\t%d\t%d\n", i, routeTable[i][0], routeTable[i][1]);
    }

    public int[] getTable(){
        int[] sendVector = new int[4];

        for(int i = 0; i < 4; i++)
            sendVector[i] = routeTable[i][0];
        return sendVector;
    }

    public int getId(){
        return id;
    }
}

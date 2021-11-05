public class MapperTest implements Runnable{
    private int rowsByThread;
    private int currentThreadId;
    private Grid grid;
    private Grid newGrid;
    private int N;


    public MapperTest(int rowsByThread, int currentThreadId, Grid grid, Grid newGrid, int N) {
        this.currentThreadId = currentThreadId;
        this.rowsByThread = rowsByThread;
        this.grid = grid;
        this.newGrid = newGrid;
        this.N = N;
    }
    @Override
    public void run() {
        int alivedNeighbors = 0;
        for(int i = currentThreadId*rowsByThread; i<(currentThreadId+1)*rowsByThread; i++) {
            for(int j=0; j<N; j++){
                int cellValue = grid.getCell(i,j);
                alivedNeighbors =  grid.getNeighbors(i, j);
                newGrid.setCell(i, j, cellValueInNextGeneration(cellValue, alivedNeighbors));
            }
        }

    }

    public int cellValueInNextGeneration(int currentCellValue, int alivedNeighbors) {
        if(currentCellValue == 1 && (alivedNeighbors == 2 || alivedNeighbors == 3)) return 1;
        else if(currentCellValue == 0 && (alivedNeighbors == 3 || alivedNeighbors == 6)) return 1;
        return 0;
    }
}

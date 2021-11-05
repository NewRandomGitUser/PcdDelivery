public class HighLife {
    private Grid grid;
    private Grid newGrid;
    private int N;
    private int nThreads;
    private long paralellCodeTimeVariation;

    public HighLife(int n, int nThreads){
        this.N = n;
        this.grid = new Grid(N);
        this.newGrid = new Grid(N);
        grid.fillInitial();
        this.nThreads = nThreads;
        paralellCodeTimeVariation = 0;
    }

    public long getParalellCodeTimeVariation() {
        return paralellCodeTimeVariation;
    }

    public void fillNewGridHighLife(int nThreads) {
        int rowsByThread = N / nThreads;
        Thread[] th = new Thread[nThreads];
        MapperTest[] rh = new MapperTest[nThreads];
        long start = System.currentTimeMillis();
        for (int k = 0; k < nThreads; k++) {
            rh[k] = new MapperTest(rowsByThread, k, grid, newGrid, N);
            th[k] = new Thread(rh[k]);
            th[k].start();
        }
        try {
            for(int i=0; i<nThreads; i++) {
                th[i].join();
            }
            long finish = System.currentTimeMillis();
            paralellCodeTimeVariation += (finish - start);
        } catch (InterruptedException e) { System.out.println("Excecao"); }
    }

    public void copyNewGridToGrid(){
        for(int i = 0; i<N; i++) {
            for(int j=0; j<N; j++){
                int newGridCellValue = newGrid.getCell(i,j);
                grid.setCell(i,j,newGridCellValue);
            }
        }
    }


    public void printGridAndNewGrid(){
        System.out.println("Grid:");
        grid.print();
        System.out.println("New Grid:");
        newGrid.print();
    }


    public void loopGenerations(int numberOfGenerations) {

        int alivedCells = grid.alivedCells();
        System.out.println("Execution for [" + nThreads + "] threads:");
        System.out.println("Initial Condition, Alived Cells: ["+ alivedCells+"]");
        for(int i = 0; i<numberOfGenerations; i++) {
            fillNewGridHighLife(nThreads);
            copyNewGridToGrid();
            if(i==0 || i == 1 || i == 2 || i== 3 || i == 4 || i==1999) {
                alivedCells = grid.alivedCells();
                System.out.println("Generation: ["+ (i+1) + "], Alived Cells: ["+ alivedCells+"]");
            }
            newGrid.fillGridWithZeros();
        }
        System.out.println();
    }

    public void printDemonstration() {
        System.out.println("Initial Condition" + " - Submatrix:50X50:");
        grid.printSubMatrix();
        for(int i = 0; i<5; i++) {
            System.out.println("Geration: [" + (i+1) + "] -  Submatrix:50X50:");
            fillNewGridHighLife(nThreads);
            copyNewGridToGrid();
            grid.printSubMatrix();
            newGrid.fillGridWithZeros();
        }
    }
}

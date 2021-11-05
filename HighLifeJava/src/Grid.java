public class Grid {
    private int N;
    private int matrix[][];

    public Grid(int N) {
        this.N = N;
        this.matrix = new int [N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.matrix[i][j] = 0;
    }

    public void print(){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(this.matrix[i][j] + "  ");
            System.out.println();
        }
    }

    public void printSubMatrix(){
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++)
                System.out.print(this.matrix[i][j] + "  ");
            System.out.println();
        }
    }

    public void fillGlinder(){
        int lin = 1, col = 1;
        matrix[lin][col+1] = 1;
        matrix[lin+1][col+2] = 1;
        matrix[lin+2][col] = 1;
        matrix[lin+2][col+1] = 1;
        matrix[lin+2][col+2] = 1;
    }

    public void fillRPentomino(){
        int lin =10, col = 30;
        matrix[lin][col+1] = 1;
        matrix[lin][col+2] = 1;
        matrix[lin+1][col] = 1;
        matrix[lin+1][col+1] = 1;
        matrix[lin+2][col+1] = 1;
    }

    public void fillInitial(){
        fillGlinder();
        fillRPentomino();
    }

    void fillGridWithZeros() {
        for(int i = 0; i < N; i++){
            for(int j=0; j < N; j++){
                matrix[i][j] = 0;
            }
        }
    }

    public int getNeighbors(int i, int j) {
        int alivedNeighbors = 0;
        //(cima esquerda)
        if(i!=0 && j!=0 && matrix[i-1][j-1]==1) alivedNeighbors++;
        //(esquerda)
        if(j!=0 && matrix[i][j-1] == 1) alivedNeighbors++;
        //(baixo esquerda)
        if(i!=N-1 && j!=0 && matrix[i+1][j-1] == 1) alivedNeighbors++;
        //(cima)
        if(i!=0 && matrix[i-1][j]==1) alivedNeighbors++;
        //(baixo)
        if(i!=N-1 && matrix[i+1][j]==1) alivedNeighbors++;
        //(cima direita)
        if(i!=0 && j!=N-1 && matrix[i-1][j+1]==1) alivedNeighbors++;
        //(direita)
        if(j!=N-1 && matrix[i][j+1]==1) alivedNeighbors++;
        //(baixo direita)
        if(i!=N-1 && j!=N-1 && matrix[i+1][j+1]==1) alivedNeighbors++;
        //(borda superior)
        if(i==0 && j!=0 && j!=N-1) {
            if(matrix[N-1][j]==1) alivedNeighbors++;
            if(matrix[N-1][j-1]==1) alivedNeighbors++;
            if(matrix[N-1][j+1]==1) alivedNeighbors++;
        }
        //(borda inferior)
        if(i==N-1 && j!=0 && j!=N-1) {
            if(matrix[0][j]==1) alivedNeighbors++;
            if(matrix[0][j-1]==1) alivedNeighbors++;
            if(matrix[0][j+1]==1) alivedNeighbors++;
        }
        //(borda esquerda)
        if(j==0 && i!=0 && i!=N-1) {
            if(matrix[i][N-1]==1) alivedNeighbors++;
            if(matrix[i-1][N-1]==1) alivedNeighbors++;
            if(matrix[i+1][N-1]==1) alivedNeighbors++;
        }
        //(borda direita)
        if(j==N-1 && i!=0 && i!=N-1) {
            if(matrix[i][0]==1) alivedNeighbors++;
            if(matrix[i-1][0]==1) alivedNeighbors++;
            if(matrix[i+1][0]==1) alivedNeighbors++;
        }
        //(canto superior esquerdo)
        if(i==0 && j==0) {
            if(matrix[N-1][N-1]==1) alivedNeighbors++;
        }
        //(canto superior direito)
        if(i==0 && j==N-1) {
            if(matrix[N-1][0]==1) alivedNeighbors++;
        }
        //(canto inferior esquerdo)
        if(i==N-1 && j==0) {
            if(matrix[0][N-1]==1) alivedNeighbors++;
        }
        //(canto inferior direito)
        if(i==N-1 && j==N-1) {
            if(matrix[0][0]==1) alivedNeighbors++;
        }
        return alivedNeighbors;
    }

    public int getCell(int i, int j) {
        return this.matrix[i][j];
    }

    public void setCell(int i, int j, int value) {
       this.matrix[i][j] = value;
    }

    public int alivedCells() {
        int total = 0;
        for(int i = 0; i<N; i++){
            for(int j=0; j<N; j++)
                total += matrix[i][j];
        }
        return total;
    }
}

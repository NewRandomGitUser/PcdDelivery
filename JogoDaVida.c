#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <omp.h>
#include <sys/time.h>
#include <time.h>
#define N 2048

long long paralellCodeTime = 0;

void readGridFromStdin(int **grid){
    for(int i = 0; i < N; i++){
        for(int j=0; j < N; j++){
            scanf("%d",&grid[i][j]);
        }
    }
}

void allocateGrid(int **grid){
    for(int i = 0; i < N; i++) grid[i] = (int *)malloc(N * sizeof(int));
}

void fillGlinder(int **grid) {
    int lin = 1, col = 1;
    grid[lin][col+1] = 1;
    grid[lin+1][col+2] = 1;
    grid[lin+2][col] = 1;
    grid[lin+2][col+1] = 1;
    grid[lin+2][col+2] = 1;
}

void fillRPentomino(int **grid) {
    int lin =10, col = 30;
    grid[lin][col+1] = 1;
    grid[lin][col+2] = 1;
    grid[lin+1][col] = 1;
    grid[lin+1][col+1] = 1;
    grid[lin+2][col+1] = 1;
}

void fillInitialGrid(int **grid){
    fillGlinder(grid);
    fillRPentomino(grid);
}

void fillGridWithZeros(int **grid) {
    for(int i = 0; i < N; i++){
        for(int j=0; j < N; j++){
            grid[i][j] = 0;
        }
    }
}

void printGrid(int **grid){
    for(int i = 0; i < N; i++){
        for(int j=0; j < N; j++){
            printf("%d ",grid[i][j]);
        }
        printf("\n");
    }
}

void printSubMatrix(int **grid) {
    for(int i = 0; i < 50; i++){
        for(int j=0; j < 50; j++){
            printf("%d ",grid[i][j]);
        }
        printf("\n");
    }

}

void readGridAndNewGrid(int **grid, int **newGrid){
    allocateGrid(grid);
    fillInitialGrid(grid);
    allocateGrid(newGrid);
}

int getNeighbors(int** grid, int i, int j) {
    int alivedNeighbors = 0;
    //(cima esquerda)
    if(i!=0 && j!=0 && grid[i-1][j-1]==1) alivedNeighbors++;
    //(esquerda)
    if(j!=0 && grid[i][j-1] == 1) alivedNeighbors++;
    //(baixo esquerda)
    if(i!=N-1 && j!=0 && grid[i+1][j-1] == 1) alivedNeighbors++;
    //(cima)
    if(i!=0 && grid[i-1][j]==1) alivedNeighbors++;
    //(baixo)
    if(i!=N-1 && grid[i+1][j]==1) alivedNeighbors++;
    //(cima direita)
    if(i!=0 && j!=N-1 && grid[i-1][j+1]==1) alivedNeighbors++;
    //(direita)
    if(j!=N-1 && grid[i][j+1]==1) alivedNeighbors++;
    //(baixo direita)
    if(i!=N-1 && j!=N-1 && grid[i+1][j+1]==1) alivedNeighbors++;
    //(borda superior)
    if(i==0 && j!=0 && j!=N-1) {
        if(grid[N-1][j]==1) alivedNeighbors++;
        if(grid[N-1][j-1]==1) alivedNeighbors++;
        if(grid[N-1][j+1]==1) alivedNeighbors++;
    }
    //(borda inferior)
    if(i==N-1 && j!=0 && j!=N-1) {
        if(grid[0][j]==1) alivedNeighbors++;
        if(grid[0][j-1]==1) alivedNeighbors++;
        if(grid[0][j+1]==1) alivedNeighbors++;
    }
    //(borda esquerda)
    if(j==0 && i!=0 && i!=N-1) {
        if(grid[i][N-1]==1) alivedNeighbors++;
        if(grid[i-1][N-1]==1) alivedNeighbors++;
        if(grid[i+1][N-1]==1) alivedNeighbors++;
    }
    //(borda direita)
    if(j==N-1 && i!=0 && i!=N-1) {
        if(grid[i][0]==1) alivedNeighbors++;
        if(grid[i-1][0]==1) alivedNeighbors++;
        if(grid[i+1][0]==1) alivedNeighbors++;
    }
    //(canto superior esquerdo)
    if(i==0 && j==0) {
        if(grid[N-1][N-1]==1) alivedNeighbors++;
    }
    //(canto superior direito)
    if(i==0 && j==N-1) {
        if(grid[N-1][0]==1) alivedNeighbors++;
    }
    //(canto inferior esquerdo)
    if(i==N-1 && j==0) {
        if(grid[0][N-1]==1) alivedNeighbors++;
    }
    //(canto inferior direito)
    if(i==N-1 && j==N-1) {
        if(grid[0][0]==1) alivedNeighbors++;
    }
    return alivedNeighbors;
}

int cellValueInNextGenerationGameOfLife(int currentCellValue, int alivedNeighbors) {
    if(currentCellValue == 1 && (alivedNeighbors == 2 || alivedNeighbors == 3)) return 1;
    else if(currentCellValue == 0 && alivedNeighbors == 3) return 1;
    return 0;
}

void fillNewGridGameOfLife(int **grid, int **newGrid, int nThreads) {
    struct timeval inicio, final;
    long long tmili;
    gettimeofday(&inicio, NULL);
    
    int alivedNeighbors = 0;
    omp_set_num_threads(nThreads);
    #pragma omp parallel firstprivate(alivedNeighbors)
    {
        #pragma omp  for
            for(int i = 0; i<N; i++) {
                for(int j=0; j<N; j++){
                    alivedNeighbors =  getNeighbors(grid, i, j);
                    newGrid[i][j] = cellValueInNextGenerationGameOfLife(grid[i][j],alivedNeighbors);
                }
            }
    }
    gettimeofday(&final, NULL);
    tmili = (int) (1000*(final.tv_sec - inicio.tv_sec) + 
    (final.tv_usec - inicio.tv_usec)/1000);
    paralellCodeTime += tmili;
}

void copyVector(int **origin, int **destiny) {
    for(int i = 0; i<N; i++) {
        for(int j=0; j<N; j++){
            destiny[i][j] = origin[i][j];
        }
    }
}

int alivedCells(int** grid){
    int total = 0;
        for(int i = 0; i<N; i++){
            for(int j=0; j<N; j++)
                total+=grid[i][j];
        } 
    return total;
}

void loopGenerations(int **grid, int **newGrid, int numberOfGenerations, int nThreads) {
    paralellCodeTime = 0;
    printf("Execution for [%d] threads:\n", nThreads);
    printf("Initial Condition, Alived Cells: [%d]\n",alivedCells(grid));
    for(int i = 0; i<numberOfGenerations; i++) {
        fillNewGridGameOfLife(grid, newGrid, nThreads);
        copyVector(newGrid,grid);
        if(i==0 || i == 1 || i == 2 || i== 3 || i == 4 ||i==1999) {
            printf("Generation:[%d], Alived Cells:[%d]\n", i+1,alivedCells(grid));
        } 
        fillGridWithZeros(newGrid);
    }
    printf("\n");
}

long long ExecuteGameOfLifeWithNThreads(int nThreads) {
    struct timeval inicio, final;
    long long tmili;
    gettimeofday(&inicio, NULL);

    int **grid = (int **)malloc(N * sizeof(int*));
    int **newGrid = (int **)malloc(N * sizeof(int*));
    allocateGrid(grid);
    allocateGrid(newGrid);
    fillGridWithZeros(grid);
    fillInitialGrid(grid);
    fillGridWithZeros(newGrid);
    loopGenerations(grid, newGrid, 2000, nThreads);

    gettimeofday(&final, NULL);
    tmili = (int) (1000*(final.tv_sec - inicio.tv_sec) + 
    (final.tv_usec - inicio.tv_usec) / 1000);

    return tmili;
}

void printGridDemonstration(int **grid, int **newGrid) {
    printf("Initial Condition:, submatrix:50X50\n");
    printSubMatrix(grid);
    for(int i = 0; i<5; i++) {
        fillNewGridGameOfLife(grid, newGrid, 1);
        copyVector(newGrid,grid);
        printf("\nGeration:%d, submatrix:50X50\n", i+1);
        printSubMatrix(grid);
        fillGridWithZeros(newGrid);
    }
}

void ExecuteSequential() {
    int **grid = (int **)malloc(N * sizeof(int*));
    int **newGrid = (int **)malloc(N * sizeof(int*));
    allocateGrid(grid);
    allocateGrid(newGrid);
    fillInitialGrid(grid);
    fillGridWithZeros(newGrid);
    printGridDemonstration(grid, newGrid);
}

int main() {
    ExecuteSequential();
    int numberOfThreadsPerExecution[4] = {1,2,4,8};
    long long totalTimeOfExecution[4];
    long long timeVariationParalellCodePerExecution[4] = {0,0,0,0};
    
    for(int i=0; i<4; i++) {
        totalTimeOfExecution[i] = ExecuteGameOfLifeWithNThreads(numberOfThreadsPerExecution[i]);
        timeVariationParalellCodePerExecution[i] = paralellCodeTime;
    }
    for(int i=0; i<4; i++) {
        printf("Execution for [%d] threads, Total Time: [%lldms], Paralell Code Variation Time: [%lldms]\n", 
        numberOfThreadsPerExecution[i], totalTimeOfExecution[i],timeVariationParalellCodePerExecution[i]);
    }
    return 0;
}

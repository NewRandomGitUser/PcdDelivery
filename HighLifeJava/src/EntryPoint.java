import java.util.ArrayList;
import java.util.Arrays;

public class EntryPoint {

    public static void ExecuteDemonstration(){
        HighLife game = new HighLife(2048, 1);
        game.printDemonstration();
    }

    public static TimeVariation ExecuteHighLifeNThreads(int nThreads) {
        TimeVariation timeVariation = new TimeVariation();
        long start = System.currentTimeMillis();
        HighLife game = new HighLife(2048, nThreads);
        game.loopGenerations(2000);
        long finish = System.currentTimeMillis();
        long totalTimeElapsed = finish - start;
        timeVariation.setTotalTime(totalTimeElapsed);
        timeVariation.setParalellCodeTime(game.getParalellCodeTimeVariation());
        return timeVariation;
    }

    public static void main(String [] args) {
        ExecuteDemonstration();
        final var numberOfThreadsPerExecution = Arrays.asList(1,2,4,8);
        final var timeVariationPerExecution = new ArrayList<TimeVariation>();
        for(var number: numberOfThreadsPerExecution) {
            timeVariationPerExecution.add(ExecuteHighLifeNThreads(number));
        }
        for(int i = 0; i<4; i++) {
            System.out.println("Execution for [" + numberOfThreadsPerExecution.get(i) +
                    "] threads, Total Time: [" + timeVariationPerExecution.get(i).getTotalTime()
                    + "ms], Paralell Code Variation Time: [" + timeVariationPerExecution.get(i).getParalellCodeTime() + "ms]");
        }
    }
}

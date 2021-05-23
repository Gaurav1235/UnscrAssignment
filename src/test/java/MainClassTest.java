import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainClassTest {

    @Test
    void checkMeanAndVariance() throws InterruptedException {
        Pair arr[] = new Pair[4];
        arr[0] = new Pair(31.5, 41.27);
        arr[1] = new Pair(3, 4.1);
        arr[2] = new Pair(8.5, 8.7);
        arr[3] = new Pair(0,1.1);

        Histogram histogram=new Histogram(arr);
        double[] values={1,31.51,41.27,30,8.1,8.2,40.1};
        ArrayList<Thread> threads = new ArrayList<>();
        //List<Thread> threadList;
        for(int i=0;i<values.length;i++) {
            threads.add(new Thread(new RunnableImpl(values[i], histogram)));
        }
        // chnage this vlaues.lenght to threads.elgnth when other query also run
        for(int i=0;i< values.length;i++){
            threads.get(i).start();
        }
        for(int i=0;i< values.length;i++){
            threads.get(i).join();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double mean = histogram.getMean();
                double variance = histogram.getVariance();
                System.out.println("here is the mean in test class "+mean);
                System.out.println("here is the variance in test class "+variance);
                assertEquals(24.2,mean,0.1);
                assertEquals(422.2,variance,0.1);

            }
        };

        Thread t = new Thread(runnable);
        t.start();
        t.join();

    }
}
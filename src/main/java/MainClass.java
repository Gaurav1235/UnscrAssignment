import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Enter the interval size");
        Scanner sc = new Scanner(System.in);
        int intervalSize = sc.nextInt();
        System.out.println("Enter the intervals: ");
        int k=0;

        Pair intervals[] = new Pair[intervalSize];
        while(intervalSize>0){
            double x=sc.nextDouble();
            BigDecimal bdDown1=new BigDecimal(x).setScale(2, RoundingMode.HALF_DOWN);
            x=bdDown1.doubleValue();
            double y=sc.nextDouble();
            BigDecimal bdDown2=new BigDecimal(y).setScale(2,RoundingMode.HALF_DOWN);
            y=bdDown2.doubleValue();
            boolean isValid=true;
            if(x>y){
                System.out.println("Please enter valid interval");
            }
            else {
                for (int i = 0; i < k; i++) {
                    if (y <= (double) intervals[i].getKey() || x >= (double) intervals[i].getValue()) {
                        isValid = true;
                    } else {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    intervals[k] = new Pair(x, y);
                    intervalSize--;
                    k++;
                } else {
                    System.out.println("The interval entered is getting overlapped, please enter a valid interval");
                }
            }
        }

        Histogram histogram=new Histogram(intervals);
        ArrayList<Thread> threads = new ArrayList<>();
        Runnable runnableGetHistogram = new Runnable() {
            @Override
            public void run() {
                for(int i=0;i< intervals.length;i++){
                    System.out.println("["+intervals[i].getKey()+","+intervals[i].getValue()+") "+histogram.getCount(i));
                }
                double mean = histogram.getMean();
                System.out.println("Mean : "+mean);
                double variance = histogram.getVariance();
                System.out.println("Variance : "+variance);
                ArrayList l = histogram.getOutliers();
                System.out.print("Outliers are : ");
                if(l.size()==0){
                    System.out.print("None");
                }
                for(int i=0;i< l.size();i++){
                    System.out.print( l.get(i)+" ");
                }
                System.out.println();
            }
        };

        Runnable runnableGetMean = new Runnable() {
            @Override
            public void run() {
                double mean = histogram.getMean();
                System.out.println("Mean so far "+mean);
            }
        };

        Runnable runnableGetVariance = new Runnable() {
            @Override
            public void run() {
                double variance = histogram.getVariance();
                System.out.println("Variance so far "+variance);
            }
        };

        System.out.println("Enter the number of queries you want to give: ");
        int queries = Integer.parseInt(sc.next());
        sc.nextLine();
        System.out.println("Enter the queries: ");
        while (queries > 0) {
            String command = sc.nextLine();
            String [] commands=command.split(" ");
            String commandType = commands[0];
            switch (commandType){
                case "insert":
                    double insertItem=Double.parseDouble(commands[1]);
                    threads.add(new Thread(new RunnableImpl(insertItem, histogram)));
                    //System.out.println(insertItem);
                    break;
                case "findMean":
                    threads.add(new Thread(runnableGetMean));
                    //System.out.println("The Mean is");
                    break;
                case "findVariance":
                    threads.add(new Thread(runnableGetVariance));
                    //System.out.println("The Variance is");
                    break;
                case "getHistogram":
                    threads.add(new Thread((runnableGetHistogram)));
                    //System.out.println("The getHistogram is");
                    break;
                default:
                    System.out.println("Please enter a valid query");
                    queries++;
            }
            queries--;
        }

        for(int i=0;i< threads.size();i++){
            threads.get(i).start();
        }
        for(int i=0;i< threads.size();i++){
            threads.get(i).join();
        }

    }
}

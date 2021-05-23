import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Histogram {

    private Object key = new Object();
    private double mean;
    private double variance;
    private int count;
    private double[][] intervals;
    private ArrayList<Double> insertedValues ;
    private ArrayList<Double> outliers ;

    Histogram(Pair[] arr){
        //converting each pair to hold double by checking if any int value coming
        this.count=0;
        this.mean=0;
        this.variance=0;
        this.insertedValues=new ArrayList<>();
        this.outliers=new ArrayList<>();
        for(int i=0;i<arr.length;i++) {
            if(arr[i].getKey().getClass().getName()=="java.lang.Integer"){
                int x= (int) arr[i].getKey();
                arr[i]=new Pair ((double)x,arr[i].getValue());
            }
            if(arr[i].getValue().getClass().getName()=="java.lang.Integer") {
                int y= (int) arr[i].getValue();
                arr[i]=new Pair (arr[i].getKey(),(double)y);
            }
        }
        //sorting the arr of intervals given according to the first value in interval
        Arrays.sort(arr, new Comparator<Pair>() {
            @Override
            public int compare(Pair p1, Pair p2) {
                double d1= (double) p1.getKey();
                double d2= (double) p2.getKey();
                if(d1-d2>0.0){
                    return 1;
                }
                else{
                    return -1;
                }
            }
        });
        // copying the sorted intervals to this class intervals array
        double[][] intervalsCopy= new double[arr.length][3];
        for(int i=0;i<arr.length;i++) {
            intervalsCopy[i][0]= (double) arr[i].getKey();
            intervalsCopy[i][1]= (double) arr[i].getValue();
            intervalsCopy[i][2]=0;
        }
        this.intervals=intervalsCopy;

    }
    //returning the count of elements in the interval
    public int getCount(int index){
        return (int) intervals[index][2];
    }
    //returning the mean calculated so far
    public double getMean(){
        synchronized (key) {
            return mean;
        }
    }
    //returning the variance calculated so far
    public double getVariance(){
        synchronized (key) {
            return variance;
        }
    }
    //returning the list of outliers so far
    public ArrayList getOutliers(){
        return this.outliers;
    }
    //inserting the number in the correct interval and calculating the mean and variance including this number
    public boolean insert(double x){
        synchronized (key) {

            int l=0;
            int h=intervals.length-1;
            while(l<=h){
                int mid=l+(h-l)/2;
                if(intervals[mid][0]>x){
                    h=mid-1;
                }
                else if(intervals[mid][1]<x){
                    l=mid+1;
                }
                else{
                    h=mid;
                    break;
                }
            }
            //precision
            if(h==-1 || intervals[h][1]<=x){
                outliers.add(x);
                return false;
            }
            intervals[h][2]+=1;
            count++;
            mean=(mean*(count-1)+x)/(double) count;
            BigDecimal bdDown1=new BigDecimal(mean).setScale(2,RoundingMode.HALF_DOWN);
            mean= bdDown1.doubleValue();
            insertedValues.add(x);
            if(count==1){
                variance=0;
            }
            else {
                variance=0;
                for(int i=0;i<insertedValues.size();i++){
                    variance+= (insertedValues.get(i)-mean)*(insertedValues.get(i)-mean);
                }
                variance = variance/(double)(count-1);
                BigDecimal bdDown2=new BigDecimal(variance).setScale(2, RoundingMode.HALF_DOWN);
                variance = bdDown2.doubleValue();
            }
            return true;
        }
    }
}

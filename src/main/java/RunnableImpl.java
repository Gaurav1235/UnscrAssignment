public class RunnableImpl implements Runnable{
    private double insertItem;
    Histogram histogram;
    RunnableImpl(double item,Histogram histogram){
        this.insertItem=item;
        this.histogram=histogram;
    }
    public void run()
    {
        boolean insertStatus = histogram.insert(insertItem);
        if(insertStatus){
            System.out.println("Inserted "+insertItem);
        }
        else{
            System.out.println(insertItem+" out of bounds");
        }
    }
}

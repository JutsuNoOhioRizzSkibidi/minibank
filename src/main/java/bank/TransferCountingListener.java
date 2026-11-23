package bank;

public class TransferCountingListener implements TransferListener{
    private int count;
    @Override
    public void onTransferCompleted(Transaction transaction){
        count++;
    }
    public int getCount(){
        return count;
    }
}
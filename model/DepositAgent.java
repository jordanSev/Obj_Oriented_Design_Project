package acctMgr.model;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DepositAgent extends AbstractModel implements Runnable, Agent {
	private Object pauseLock;
	private volatile boolean paused;
	public volatile boolean active;
	private Account account;
	private BigDecimal amount;
	private BigDecimal transferred;
	private int iters = 0;
	private String name = new String("Default");
	private AgentStatus status;
	
	public DepositAgent(Account account, BigDecimal amount){
		this.account = account;
		this.amount = amount;
		this.amount.setScale(2, RoundingMode.HALF_UP);
		this.transferred = BigDecimal.ZERO;
		this.transferred.setScale(2, RoundingMode.HALF_UP);
		
		this.status = AgentStatus.Running;
		this.active = true;
		this.paused = false;
		this.pauseLock = new Object();
	}
	
	public DepositAgent(Account account, BigDecimal amount, int iters){
		this(account, amount);
		this.iters = iters;
		this.active = false;
	}
	
	public void run() {
			while(active || iters > 0) {
				synchronized (pauseLock) {
					while (paused) {
						try {
							pauseLock.wait();
						} catch (InterruptedException e) {
							System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
						}
					}
				}
				account.deposit(amount);
				transferred = transferred.add(amount);
				iters--;
				final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AmountTransferredUpdate, transferred, AgentStatus.NA);
				SwingUtilities.invokeLater(
						new Runnable() {
							public void run() {
								notifyChanged(me);
							}
						});
				try {
					Thread.sleep(300);
				}
				catch(InterruptedException ex){
					System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
				}
			}
		
	}
	public BigDecimal getTransferred(){return this.transferred;}
	public void onPause() {
        synchronized (pauseLock) {
            paused = true;
            setStatus(AgentStatus.Paused);
        }
    }

    public void onResume() {
        synchronized (pauseLock) {
            paused = false;
            setStatus(AgentStatus.Running);
            pauseLock.notify();
        }
    }
    
    public void setStatus(AgentStatus agSt) {
    	status = agSt;
    	final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AgentStatusUpdate, BigDecimal.ZERO, agSt);
    	SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
    }
    
    public AgentStatus getStatus(){return status;}
    public void setName(String name) {this.name = name;}
    public String getName(){return name;}
    public Account getAccount(){return account;}
    public void finish(){
    	active = false;
    }
}

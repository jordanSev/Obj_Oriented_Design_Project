package acctMgr.model;
import javax.swing.SwingUtilities;

import acctMgr.model.ModelEvent.EventKind;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * The Class Account.
 */
public class Account extends AbstractModel {
	
	/** The Constant EuroExchange. */
	private final static BigDecimal EuroExchange = BigDecimal.valueOf(0.79);
	
	/** The Constant YenExchange. */
	private final static BigDecimal YenExchange = BigDecimal.valueOf(94.1);
	
	/** The balance. */
	private BigDecimal balance;
	
	/** The name. */
	private String name;
	
	/** The id. */
	private String ID;
	
	/** The id. */
	private int id;
	
	/** The currency type. */
	private String currencyType;
	
	/** The current state. */
	private boolean currentState;
	
	/**
	 * Instantiates a new account.
	 *
	 * @param name the name
	 * @param ID the id
	 * @param balance the balance
	 * @param currency the currency
	 * @param changeState the change state
	 */
	public Account(String name, String ID, BigDecimal balance, String currency, boolean changeState){
		this.name = name;
		this.ID = ID;
		this.balance = balance;
		this.balance.setScale(2, RoundingMode.HALF_UP);
		this.currencyType = currency;
		this.currentState = changeState;
	}
	
	/**
	 * Instantiates a new account.
	 *
	 * @param name the name
	 * @param id the id
	 * @param balance the balance
	 */
	public Account(String name, int id, BigDecimal balance)
	{
		this.name = name;
		this.id = id;
		this.balance = balance;
		this.balance.setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * Store.
	 *
	 * @param value the value
	 */
	public void store(int value){
		balance = new BigDecimal(value);
		ModelEvent me = new ModelEvent(EventKind.BalanceUpdate, balance,AgentStatus.Running);
		notifyChanged(me);
	}
	
	/**
	 * Gets the currency type.
	 *
	 * @return the currency type
	 */
	public String getCurrencyType( ){
		return currencyType;
	}
	
	/**
	 * Sets the currency type.
	 *
	 * @param newCurrency the new currency type
	 */
	public void setCurrencyType( String newCurrency){
		currencyType = newCurrency;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID(){
		return ID;
	}
	
	/**
	 * Gets the balance.
	 *
	 * @return the balance
	 */
	public BigDecimal getBalance(){return balance;}
	
	/**
	 * Deposit.
	 *
	 * @param amount the amount
	 */
	public synchronized void deposit(BigDecimal amount) {
		balance = balance.add(amount);
		
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);

		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
		notifyAll();
	}

	
	/**
	 * Withdraw.
	 *
	 * @param amount the amount
	 * @throws OverdrawException the overdraw exception
	 */
	public synchronized void withdraw(BigDecimal amount) throws OverdrawException {
		if (amount.doubleValue() > balance.doubleValue()) throw new OverdrawException(amount.doubleValue(),balance);
	     else
	     {
		     balance = balance.subtract(amount);
		     balance = balance.setScale(2,BigDecimal.ROUND_HALF_UP);
	     }
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
	}
	
	public synchronized void autoWithdraw(BigDecimal amount, Agent ag) throws InterruptedException {
		while(balance.subtract(amount).signum() < 0) {
			ag.setStatus(AgentStatus.Blocked);
			//System.out.println("autoWithdraw blocking");
			wait();
		}
		if(ag.getStatus() == AgentStatus.Paused) return;
		ag.setStatus(AgentStatus.Running);
				
		balance = balance.subtract(amount);
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, this.balance, AgentStatus.Running);
		SwingUtilities.invokeLater(
			new Runnable() {
			    public void run() {
			    	notifyChanged(me);
			    }
			});
	}
	/**
	 * Gets the euro balance.
	 *
	 * @return the euro balance
	 */
	public BigDecimal getEuroBalance() {
		BigDecimal bal = balance.add(BigDecimal.ZERO);
		bal.setScale(2, RoundingMode.HALF_UP);
		bal = bal.multiply(EuroExchange);

		return bal;
	}
	
	/**
	 * Withdraw euro.
	 *
	 * @param amount the amount
	 * @throws OverdrawException the overdraw exception
	 */
	public void withdrawEuro(BigDecimal amount) throws OverdrawException{

		amount = amount.divide(EuroExchange, 2, RoundingMode.CEILING);

		if (amount.doubleValue() > balance.doubleValue()) throw new OverdrawException(amount.doubleValue(),balance);
	     else
	     {
		     balance = balance.subtract(amount);
		     balance = balance.setScale(2,BigDecimal.ROUND_HALF_UP);
	     }
		

		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);

		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	/**
	 * Deposit euro.
	 *
	 * @param amount the amount
	 */
	public void depositEuro(BigDecimal amount) {
		amount = amount.divide(EuroExchange, 2, RoundingMode.CEILING);
		balance = balance.add(amount);

		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	
	
	/**
	 * Gets the yen balance.
	 *
	 * @return the yen balance
	 */
	public BigDecimal getYenBalance() {
		BigDecimal bal = balance.add(BigDecimal.ZERO);
		bal.setScale(2, RoundingMode.HALF_UP);
		bal = bal.multiply(YenExchange);
		return bal;

	}
	
	/**
	 * Withdraw yen.
	 *
	 * @param amount the amount
	 * @throws OverdrawException the overdraw exception
	 */
	public void withdrawYen(BigDecimal amount) throws OverdrawException {
		if (amount.doubleValue() > balance.doubleValue()) throw new OverdrawException(amount.doubleValue(),balance);
	     else
	     {
		     balance = balance.subtract(amount);
		     balance = balance.setScale(2,BigDecimal.ROUND_HALF_UP);
	     }
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);

		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	/**
	 * Deposit yen.
	 *
	 * @param amount the amount
	 */
	public void depositYen(BigDecimal amount) {
		amount = amount.divide(YenExchange, 2, RoundingMode.CEILING);
		balance = balance.add(amount);

		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + " " + ID + " " + balance + "\n";
	}
}


package acctMgr.model;
import java.math.BigDecimal;


/**
 * The Class ModelEvent.
 */
public class ModelEvent {
	
	/**
	 * The Enum EventKind.
	 */
	public enum EventKind {
		
		BalanceUpdate, AgentStatusUpdate, AmountTransferredUpdate
	}
	
	/** The kind. */
	private EventKind kind;
	
	/** The balance. */
	private BigDecimal balance;
	
	/** The ag st. */
	private AgentStatus agSt;
	
	/**
	 * Instantiates a new model event.
	 *
	 * @param kind the kind
	 * @param balance the balance
	 * @param agSt the ag st
	 */
	public ModelEvent(EventKind kind, BigDecimal balance, AgentStatus agSt){
		this.balance = balance;
		this.kind = kind;
		this.agSt = agSt;
	}
	
	/**
	 * Gets the kind.
	 *
	 * @return the kind
	 */
	public EventKind getKind(){return kind;}
	
	/**
	 * Gets the balance.
	 *
	 * @return the balance
	 */
	public BigDecimal getBalance(){
		return balance;
	}
	
	/**
	 * Gets the ag status.
	 *
	 * @return the ag status
	 */
	public AgentStatus getAgStatus(){return agSt;}
}

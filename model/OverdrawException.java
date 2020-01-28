package acctMgr.model;

import java.math.BigDecimal;

/**
 * The Class OverdrawException.
 */
public class OverdrawException extends Exception {

	/**
	 * Instantiates a new overdraw exception.
	 *
	 * @param value the value
	 * @param balance the balance
	 */
	OverdrawException(double value, BigDecimal balance){
		super("Insufficient funds: amount to withdraw is "+ value + " is greater than available funds: " +balance);
	}
}


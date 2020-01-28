package acctMgr.controller;

import acctMgr.model.AccountList;
import acctMgr.model.Agent;
import acctMgr.model.Account;
import acctMgr.model.OverdrawException;
import acctMgr.view.AccountView;
import acctMgr.view.AgentView;
import acctMgr.utils.AgentManager;

import java.math.BigDecimal;


import javax.swing.SwingUtilities;

		public class AccountController extends AbstractController {

			public void operation(String opt) {
				if(opt == AccountView.Deposit) {
					BigDecimal amount = ((AccountView)getView()).getAmount();
					((Account)getModel()).deposit(amount);
				} else if(opt == AccountView.Withdraw) {
					BigDecimal amount = ((AccountView)getView()).getAmount();
					try {
						((Account)getModel()).withdraw(amount);
					}
					catch(OverdrawException ex) {
						final String msg = ex.getMessage();
						SwingUtilities.invokeLater(new Runnable() {
						      public void run() {
						    	  ((AccountView)getView()).showError(msg);
						      }
						    });
					}
				}else if(opt == AccountView.StartDepAgent) {
						final AccountView acView = (AccountView)getView();
						
						BigDecimal amount = acView.getAmount();
						if(amount.signum() > 0) {
							final Agent ag = AgentManager.createDepAgent(((Account)getModel()), amount);
							final AgentController agContr = new AgentController();
							agContr.setModel(ag);
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									acView.createAgentView(ag, agContr);
								}
						    	});
						}
					} else if(opt == AccountView.StartWithdrawAgent) {
						final AccountView acView = (AccountView)getView();
						BigDecimal amount = acView.getAmount();
						if(amount.signum() > 0) {
							final Account accnt = (Account)getModel();
							final Agent ag = AgentManager.createWithdrawAgent(((Account)getModel()), amount);
							final AgentController agContr = new AgentController();
							agContr.setModel(ag);
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
						    	  AgentView app = acView.createAgentView(ag, agContr);
						    	  accnt.addModelListener(app);
						      }
						    });
						}}
				  else if(opt == AccountView.Save) {
					AccountList.save();
				} else if(opt == AccountView.SaveAndExit){
		            AccountList.save( );
		            AccountView acView = (AccountView)getView();
		            acView.dispose();
				}
			}
		}


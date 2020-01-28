package acctMgr.view;

import acctMgr.controller.Controller;
import acctMgr.controller.AccountController;
import acctMgr.controller.AgentController;
import acctMgr.model.Model;
import acctMgr.model.Account;
import acctMgr.model.AccountList;
import acctMgr.model.Agent;
import acctMgr.model.ModelEvent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class AccountView extends JFrameView {

	public final static String Withdraw = "         Withdraw         ";
	public final static String StartWithdrawAgent = "StartWithdrawAgent ";
	public final static String Save = "Save";
	public final static String SaveAndExit = "Save and Exit";
	public final static String Deposit = "       Deposit       ";
	public final static String StartDepAgent = "StartDepAgent";
	public static Object accountView;
	private List<AgentController> agentContrs = new ArrayList<AgentController>(10);
	private String initAmountS;
	
	public BigDecimal getAmount() {
		BigDecimal amount = BigDecimal.ZERO;
		try {
			amount = new BigDecimal(amountField.getText());
			
		}
		catch(NumberFormatException ex) {
			amountField.setText(initAmountS);
			showError("Amount field only accepts decimals");
		}
		return amount;
	}
	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private AccountView(Model model, Controller controller){
		super(model, controller);
		initAmountS = "50.00";
		this.getContentPane().add(getContent());


		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				super.windowClosing(evt);
				AccountList.save();
				System.exit(0);
			}
		});

		setLocation(400, 300);
		pack();
	}
	public static void accountView(Account account) {
		final AccountController contr = new AccountController();
		contr.setModel(account);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AccountView app = new AccountView(account, contr);
				contr.setView(app);
				app.setVisible(true);
			}
		});
	}
	public AgentView createAgentView(Agent ag, AgentController agContr){
		AgentView app = new AgentView(ag, agContr);
		agContr.setView(app);
		agentContrs.add(agContr);
		app.setVisible(true);
		return app;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private JTextPane balanceField;
	private JTextPane getBalanceField(){
		if(balanceField == null){
			balanceField = new JTextPane();
			balanceField.setText((((Account)getModel()).getBalance()).toString());
			balanceField.setSize(200, 25);
			balanceField.setEditable(false);
		}
		return balanceField;
	}
	private JTextPane amountField;
	private JTextPane getAmountField(){
		if(amountField == null){
			amountField = new JTextPane();

			amountField.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					if ( ((c < '0') || (c > '9')) || c == KeyEvent.VK_PERIOD || c == KeyEvent.VK_DECIMAL) {
						e.consume();  // ignore event
					}
				}
			});

			amountField.setText(initAmountS);
			amountField.setSize(200, 25);
			amountField.setEditable(true);
		}
		return amountField;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private JPanel topPanel;
	private JPanel textPanel;

	private JPanel getContent() {
		if (topPanel == null) {
			topPanel = new JPanel();
			GridLayout layout = new GridLayout(2, 1);
			topPanel.setLayout(layout);
			topPanel.setPreferredSize(new Dimension(400, 200));
			GridBagConstraints ps = new GridBagConstraints();
			ps.gridx = 0;
			ps.gridy = 0;
			ps.fill = GridBagConstraints.HORIZONTAL;

			GridBagConstraints bs = new GridBagConstraints();
			bs.gridx = 0;
			bs.gridy = 1;
			topPanel.add(getTextFieldPanel(), null);
			topPanel.add(getButtonPanel(), null);
		}
		return topPanel;
	}
	private JPanel getTextFieldPanel()
	{
		if(textPanel == null){
			GridBagConstraints bl = new GridBagConstraints();
			bl.gridx = 0;
			bl.gridy = 0;

			GridBagConstraints bf = new GridBagConstraints();
			bf.gridx = 1;
			bf.gridy = 0;

			GridBagConstraints aml = new GridBagConstraints();
			aml.gridx = 0;
			aml.gridy = 1;

			GridBagConstraints amf = new GridBagConstraints();
			amf.gridx = 1;
			amf.gridy = 1;

			textPanel = new JPanel();
			textPanel.setLayout(new GridBagLayout());
			textPanel.setPreferredSize(new Dimension(400, 75));
			textPanel.add(getBalanceLabel(), bl);
			textPanel.add(getBalanceField(), bf);
			textPanel.add(getAmountLabel(), aml);
			textPanel.add(getAmountField(), amf);

		}
		return textPanel;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private JLabel balanceLabel;
	private JLabel getBalanceLabel(){
		if(balanceLabel == null){
			balanceLabel = new JLabel();
			balanceLabel.setText("Balance:");
			balanceLabel.setPreferredSize(new Dimension(200, 200));
		}
		return balanceLabel;
	}
	private JLabel amountLabel;
	private JLabel getAmountLabel(){
		if(amountLabel == null){
			amountLabel = new JLabel();
			amountLabel.setText("Amount:");
			amountLabel.setPreferredSize(new Dimension(200, 100));
		}
		return amountLabel;
	}
	private JPanel buttonPanel;
	private JPanel getButtonPanel()
	{
		if(buttonPanel == null){
			GridBagConstraints depButtonCtr = new GridBagConstraints();
			depButtonCtr.gridx = 1;
			depButtonCtr.gridy = 0;
			
			GridBagConstraints depAgButtonCtr = new GridBagConstraints();
            depAgButtonCtr.gridx = 1;
            depAgButtonCtr.gridy = 2;

			GridBagConstraints wButtonCtr = new GridBagConstraints();
			wButtonCtr.gridx = 2;
			wButtonCtr.gridy = 0;

            GridBagConstraints wAgButtonCtr = new GridBagConstraints();
            wAgButtonCtr.gridx = 2;
            wAgButtonCtr.gridy = 2;

			GridBagConstraints saveButtonCtr = new GridBagConstraints();
			saveButtonCtr.gridx = 3;
			saveButtonCtr.gridy = 0;

			GridBagConstraints saveAndExitButtonCtr = new GridBagConstraints();
			saveAndExitButtonCtr.gridx = 4;
			saveAndExitButtonCtr.gridy = 0;

			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getDepButton(), depButtonCtr);
			buttonPanel.add(getDepAgentButton(), depAgButtonCtr);
			buttonPanel.add(getWithdrawButton(), wButtonCtr);
			buttonPanel.add(getWithdrawAgentButton(), wAgButtonCtr);
			buttonPanel.add(getSaveButton(), saveButtonCtr);
			buttonPanel.add(getSaveExitButton(), saveAndExitButtonCtr);
		}

		return buttonPanel;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private JButton saveExitButton;
	private JButton getSaveExitButton(){
		if(saveExitButton == null){
			saveExitButton = new JButton(SaveAndExit);
			saveExitButton.addActionListener(handler);
		}
		return saveExitButton;
	}
	
	private JButton saveButton;
	private JButton getSaveButton(){
		if(saveButton == null){
			saveButton = new JButton(Save);
			saveButton.addActionListener(handler);
		}
		return saveButton;
	}

	private JButton withdrawButton;
	private JButton getWithdrawButton(){
		if(withdrawButton == null){
			withdrawButton = new JButton(Withdraw);
			withdrawButton.addActionListener(handler);
		}
		return withdrawButton;
	}
	private JButton startWithdrawAgentButton;
	private JButton getWithdrawAgentButton(){
		if(startWithdrawAgentButton == null){
			startWithdrawAgentButton = new JButton(StartWithdrawAgent);
			startWithdrawAgentButton.addActionListener(handler);
		}
		return startWithdrawAgentButton;
	}
	
	private JButton depButton;
	private JButton getDepButton(){
		if(depButton == null){
			depButton = new JButton(Deposit);
			depButton.addActionListener(handler);
		}
		return depButton;
	}
	private JButton startDepAgentButton;
	private JButton getDepAgentButton(){
		if(startDepAgentButton == null){
			startDepAgentButton = new JButton(StartDepAgent);
			startDepAgentButton.addActionListener(handler);
		}
		return startDepAgentButton;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private Handler handler = new Handler();
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			((AccountController)getController()).operation(evt.getActionCommand());
		}
	}
	public void modelChanged(ModelEvent me){
		if(me.getKind() == ModelEvent.EventKind.BalanceUpdate) {
			System.out.println("Balance field to " + me.getBalance());
			balanceField.setText((me.getBalance()).toString());
		}
	}
}

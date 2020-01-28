package acctMgr.view;

import acctMgr.controller.AgentController;
import acctMgr.controller.Controller;
import acctMgr.model.Agent;
import acctMgr.model.AgentStatus;
import acctMgr.model.Model;
import acctMgr.model.ModelEvent;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;


public class AgentView extends JFrameView {
	public final static String Pause = "Pause";
	public final static String Resume = "Resume";
	public final static String Dismiss = "Dismiss";
	
	private JPanel topPanel;
	private JPanel textPanel;
	private JPanel buttonPanel;
	
	private JLabel transferredLabel;
	private JLabel statusLabel;
	private JTextPane transferredField;
	private JTextPane statusField;
	
	private JButton dismissButton;
	private Handler handler = new Handler();
	public AgentView(Model model, Controller controller){
		super(model, controller);
		this.getContentPane().add(getContent());
		final Controller contr = controller;
		addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent evt) {
		         ((AgentController)contr).operation(Dismiss);
		    }
		});
		this.setTitle(((Agent)model).getName());
		setLocation(400, 450);
		pack();
	}
	private JPanel getContent() {
		if (topPanel == null) {
			topPanel = new JPanel();
			GridLayout layout = new GridLayout(2, 1);
			topPanel.setLayout(layout);
			topPanel.add(getTextFieldPanel(), null);
			topPanel.add(getButtonPanel(), null);
		}
		return topPanel;
	}
	private JPanel getTextFieldPanel()
	{
		if(textPanel == null){
			GridBagConstraints tl = new GridBagConstraints();
			tl.gridx = 0;
			tl.gridy = 0;
			
			GridBagConstraints tf = new GridBagConstraints();
			tf.gridx = 1;
			tf.gridy = 0;
			
			GridBagConstraints sl = new GridBagConstraints();
			sl.gridx = 0;
			sl.gridy = 1;
			
			GridBagConstraints sf = new GridBagConstraints();
			sf.gridx = 1;
			sf.gridy = 1;
			
			textPanel = new JPanel();
			textPanel.setLayout(new GridBagLayout());
			textPanel.add(getTransferredLabel(), tl);
			textPanel.add(getTransferredField(), tf);
			textPanel.add(getStatusLabel(), sl);
			textPanel.add(getStatusField(), sf);
		}
		return textPanel;
	}
	private JPanel getButtonPanel()
	{
		if(buttonPanel == null){
			GridBagConstraints pauseButtonCtr = new GridBagConstraints();
			pauseButtonCtr.gridx = 0;
			pauseButtonCtr.gridy = 0;
			
			GridBagConstraints resButtonCtr = new GridBagConstraints();
			resButtonCtr.gridx = 1;
			resButtonCtr.gridy = 0;
			
			GridBagConstraints dissButtonCtr = new GridBagConstraints();
			dissButtonCtr.gridx = 2;
			dissButtonCtr.gridy = 0;
			
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getDismissButton(), dissButtonCtr);
		}
		
		return buttonPanel;
	}

	private JButton getDismissButton(){
		if(dismissButton == null){
			dismissButton = new JButton(Dismiss);
			dismissButton.addActionListener(handler);
		}
		return dismissButton;
	}
	private JLabel getTransferredLabel(){
		if(transferredLabel == null){
			transferredLabel = new JLabel();
			transferredLabel.setText("Transferred:");
			transferredLabel.setPreferredSize(new Dimension(200, 20));
		}
		return transferredLabel;
	}
	private JTextPane getTransferredField(){
		if(transferredField == null){
			transferredField = new JTextPane();
			transferredField.setText((((Agent)getModel()).getTransferred()).toString());
			transferredField.setSize(400, 25);
			transferredField.setEditable(false);
		}
		return transferredField;
	}
	private JLabel getStatusLabel(){
		if(statusLabel == null){
			statusLabel = new JLabel();
			statusLabel.setText("Status:");
			statusLabel.setPreferredSize(new Dimension(200, 20));
		}
		return statusLabel;
	}
	private JTextPane getStatusField(){
		if(statusField == null){
			statusField = new JTextPane();
			statusField.setText(AgentStatus.Running.toString());
			statusField.setSize(200, 25);
			statusField.setEditable(false);
		}
		return statusField;
	}
	public void modelChanged(ModelEvent me){
        ModelEvent.EventKind kind = me.getKind();
        if(kind == ModelEvent.EventKind.AmountTransferredUpdate) {
            transferredField.setText((me.getBalance()).toString());
        }
        else if(kind == ModelEvent.EventKind.AgentStatusUpdate) {
            AgentStatus agSt = me.getAgStatus();
            System.out.println("Status to " + agSt.toString());
            statusField.setText(agSt.toString());
        }
    }
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			((AgentController)getController()).operation(evt.getActionCommand());
		}
	}
}

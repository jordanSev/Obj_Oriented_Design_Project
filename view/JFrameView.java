package acctMgr.view;
import javax.swing.JFrame;
import acctMgr.model.Model;
import acctMgr.model.AbstractModel;
import acctMgr.model.ModelListener;
import acctMgr.controller.Controller;
public abstract class JFrameView extends JFrame implements View, ModelListener {
	private Model model;
	private Controller controller;

	public JFrameView (Model model, Controller controller){
		setModel(model);
		setController(controller);
	}

	public void registerWithModel(){
		((AbstractModel)model).addModelListener(this);
	}
	public void unregisterWithModel(){
		((AbstractModel)model).removeModelListener(this);
	}

	public Controller getController(){return controller;}
	
	public void setController(Controller controller){
		this.controller = controller;
	}

	public Model getModel(){return model;}
	
	public void setModel(Model model) {
		this.model = model;
		registerWithModel();
	}

}

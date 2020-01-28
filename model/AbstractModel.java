package acctMgr.model;
import java.util.List;
import java.util.ArrayList;

/**
 * The Class AbstractModel.
 */
public abstract class AbstractModel implements Model {
	
	/** The listeners. */
	private List<ModelListener> listeners = new ArrayList<ModelListener>(5);
	
	
	/* (non-Javadoc)
	 * @see acctMgr.model.Model#notifyChanged(acctMgr.model.ModelEvent)
	 */
	public void notifyChanged(ModelEvent event) {
		for(ModelListener ml : listeners){
			ml.modelChanged(event);
		}
	}
	
	
	/**
	 * Adds the model listener.
	 *
	 */
	public void addModelListener(ModelListener l){
		listeners.add(l);
	}
	
	/**
	 * Removes the model listener.
	 *
	 */
	public void removeModelListener(ModelListener l){
		listeners.remove(l);
	}

}

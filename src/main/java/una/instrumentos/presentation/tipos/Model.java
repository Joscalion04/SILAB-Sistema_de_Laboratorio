package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {
	private List<TipoInstrumento> list;
	private TipoInstrumento current;
	private int changedProps = NONE;

	public static final int NONE = 0;
	public static final int LIST = 1;
	public static final int CURRENT = 2;

	public void addObserver(Observer o) {
		super.addObserver(o);
		commit();
	}

	public void commit() {
		setChanged();
		notifyObservers(changedProps);
		changedProps = NONE;
	}

	public Model() {
	}

	public void init(List<TipoInstrumento> list) {
		setList(list);
		setCurrent(new TipoInstrumento());
	}

	public List<TipoInstrumento> getList() {
		return list;
	}

	public void setList(List<TipoInstrumento> list) {
		this.list = list;
		changedProps |= LIST;
	}

	public TipoInstrumento getCurrent() {
		return current;
	}

	public void setCurrent(TipoInstrumento current) {
		changedProps |= CURRENT;
		this.current = current;
	}
}
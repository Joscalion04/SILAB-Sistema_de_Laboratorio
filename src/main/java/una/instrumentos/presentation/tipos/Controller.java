package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.Service;
import una.instrumentos.logic.TipoInstrumento;
import una.utiles.ReportGenerator;

import java.util.List;

/**
 * Controlador que maneja la lógica de la interfaz de usuario para la gestión de tipos de instrumentos.
 */
public class Controller {
	private final View view;
	private final Model model;

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		initializeComponents();
	}

	public Model getModel() {
		return model;
	}

	private void initializeComponents() {
		// Inicializa los componentes
		model.init(Service.instance().search(new TipoInstrumento()));
		view.setController(this);
		view.setModel(model);
	}

	public void setListCurrentAndCommit(List<TipoInstrumento> list, TipoInstrumento current) {
		if (list != null) {	// esta condicion permite llamar al metodo sin actualizar la lista (ej: edit)
			model.setList(list);
		}
		model.setCurrent(current);
		model.commit();
	}

	/**
	 * Realiza una búsqueda de tipos de instrumentos basada en un filtro y actualiza el modelo.
	 *
	 * @param filter El filtro de búsqueda.
	 */
	public void search(TipoInstrumento filter) {
		try {
			List<TipoInstrumento> rows = Service.instance().search(filter);
			if (rows.isEmpty()) {
				throw new RuntimeException("Ningún tipo de instrumento coincide con los criterios de búsqueda");
			}
			setListCurrentAndCommit(rows, new TipoInstrumento());
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void edit(int row) {
		// Obtener el elemento seleccionado de la lista
		TipoInstrumento e = model.getList().get(row);
		try {
			// Lee el elemento desde la base de datos (Data)
			TipoInstrumento current = Service.instance().read(e);
			setListCurrentAndCommit(null, current);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(TipoInstrumento e) throws Exception {
		try {
			// Se lee el elemento desde la base de datos (Data)
			TipoInstrumento current = Service.instance().read(e);

			// Se setean los valores desde el view
			current.setNombre(view.getNombre());
			current.setUnidad(view.getUnidad());

			// Se actualiza el objeto en la base de datos
			Service.instance().update(current);
			// Se establece la lista actual y se confirma la transacción
			setListCurrentAndCommit(null, current);

			// Se actualiza la vista para reflejar los cambios
			search(new TipoInstrumento());
		} catch (Exception ex) {
			if(ex.getMessage().equals("Posee Instrumentos Asociados"))
				throw ex;
			ex.printStackTrace();
		}
	}

	public int save(TipoInstrumento tipoInstrumento) {
		if (!validateNonEmptyField(tipoInstrumento.getCodigo(), "codigo") ||
				!validateNonEmptyField(tipoInstrumento.getNombre(), "nombre") ||
				!validateNonEmptyField(tipoInstrumento.getUnidad(), "unidad")) {
			return 0;
		}

		try {
			Service service = Service.instance();
			try {
				service.create(tipoInstrumento);
			} catch (Exception e) {
				view.showError("Ya existe un tipo de instrumento con ese código");
				view.highlightEmptyField("codigo");
				return 0;
			}
			updateModelAfterSave(service);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		return 1;
	}

	private boolean validateNonEmptyField(String value, String fieldName) {
		if (value.isEmpty()) {
			view.showError("El " + fieldName + " no puede estar vacío");
			view.highlightEmptyField(fieldName);
			return false;
		}
		return true;
	}

	private void updateModelAfterSave(Service service) {
		try {
			TipoInstrumento emptySearch = new TipoInstrumento();
			setListCurrentAndCommit(service.search(emptySearch), new TipoInstrumento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(TipoInstrumento tipoInstrumento) {
		try {
			Service.instance().delete(tipoInstrumento);
			setListCurrentAndCommit(Service.instance().search(new TipoInstrumento()), new TipoInstrumento());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public View getView() {
		return view;
	}

	public void generateReport() {
		String filePath = "src/main/java/una/reportes/tipos_instrumentos.pdf";
		ReportGenerator.generateTypesOfInstrumentsReport(model, filePath);
		view.showMessage("Reporte generado exitosamente en: " + filePath);
	}

	public void loadList(List<TipoInstrumento> tipoInstrumentoList) {
		try {
			Service.instance().loadTipoList(tipoInstrumentoList);
			setListCurrentAndCommit(tipoInstrumentoList, new TipoInstrumento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Maneja la acción de eliminación de un tipo de instrumento seleccionado.
	 *
	 * @param selectedRow El índice de la fila seleccionada en la lista.
	 */
	public void handleDeleteAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar un elemento de la lista");
			return;
		}
		TipoInstrumento tipoInstrumento = model.getList().get(selectedRow);
		try {
			delete(tipoInstrumento);
			view.showMessage("El instrumento codigo " + tipoInstrumento.getCodigo() + " ha sido eliminado exitosamente");
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	/**
	 * Maneja la acción de guardar un nuevo tipo de instrumento.
	 *
	 * @param codigo El código del tipo de instrumento.
	 * @param nombre El nombre del tipo de instrumento.
	 * @param unidad La unidad del tipo de instrumento.
	 */
	public void handleSaveAction(String codigo, String nombre, String unidad) {
		TipoInstrumento tipoInstrumento = new TipoInstrumento();
		tipoInstrumento.setCodigo(codigo);
		tipoInstrumento.setNombre(nombre);
		tipoInstrumento.setUnidad(unidad);

		int result = save(tipoInstrumento);
		if (result == 1) {
			view.showMessage("Instrumento codigo " + codigo + " guardado exitosamente");
		}
	}

	/**
	 * Maneja la acción de editar un tipo de instrumento seleccionado.
	 *
	 * @param selectedRow El índice de la fila seleccionada en la lista.
	 */
	public void handleEditAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar un elemento de la lista");
			return;
		}
		try {
			TipoInstrumento tipoInstrumento = model.getList().get(selectedRow);
			edit(tipoInstrumento);
			view.showMessage("El instrumento codigo " + tipoInstrumento.getCodigo() + " ha sido editado exitosamente");
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	/**
	 * Maneja la acción de búsqueda de tipos de instrumento por nombre.
	 *
	 * @param searchNombre El nombre a buscar.
	 */
	public void handleSearchAction(String searchNombre) {
		TipoInstrumento tipoInstrumento = new TipoInstrumento();
		tipoInstrumento.setNombre(searchNombre);
		try {
			search(tipoInstrumento);
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}
}

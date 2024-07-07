package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Medicion;
import una.instrumentos.logic.Service;
import una.utiles.ReportGenerator;
import una.utiles.Utiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	private final View view;
	private final Model model;

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		initializeComponents();  // Mover esta llamada después de inicializar this.model
		view.setController(this);
		view.setModel(model);
	}

	public Model getModel() {
		return model;
	}

	private void initializeComponents() {
		model.init(Service.instance().search(new Calibracion()));
	}

	public void search(Calibracion filter) {
		try {
			List<Calibracion> rows = Service.instance().search(filter);
			// se tira la excepcion despues, porque
			if (rows.isEmpty()) {
				// Tirar una excepcion que no se encontro
				throw new Exception("Ninguna calibración coincide con el criterio de busqueda");
			}
			model.setList(rows);
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void edit(int row) {
		Calibracion e = model.getList().get(row);
		try {
			Calibracion current = Service.instance().read(e);
			model.setCurrent(current);
			model.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void edit(Calibracion e, Medicion medicion) {
		try {
			Calibracion current = Service.instance().read(e);
			// buscar la medicion y remplazarla con los nuevos datos
			for (int i = 0; i < current.getMediciones().size(); i++) {
				if (current.getMediciones().get(i).getNumero() == medicion.getNumero()) {    // si el numero de la medicion es igual al numero de la medicion que se quiere editar
					current.getMediciones().set(i, medicion);
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int save(Calibracion calibracion, Instrumento instrumentoSeleccionado) {
		if (!validateAndHandleEmptyField(calibracion.getNumero(), "numero") ||
				!validateAndHandleEmptyField(calibracion.getFecha().toString(), "fecha") ||
				!validateAndHandleEmptyField(calibracion.getNumeroDeMediciones().toString(), "mediciones")) {
			return 0;
		}

		if (instrumentoSeleccionado == null) {
			view.showError("Debe seleccionar un instrumento");
			return 0;
		}

		if (calibracion.getNumeroDeMediciones() < 2) {
			view.showError("El número de mediciones debe ser mayor a 2");
			return 0;
		}
		if (calibracion.getNumeroDeMediciones() > (instrumentoSeleccionado.getMaximo() + 1)) {
			view.showError("El número de mediciones debe ser menor a " + (instrumentoSeleccionado.getMaximo() + 1));
			return 0;
		}
		try {
			Utiles.parseDate(calibracion.getFecha().toString());
		} catch (Exception e) {
			view.showError("La fecha no es válida");
			return 0;
		}

		calibracion.agregarMediciones(calibracion.getNumeroDeMediciones(), instrumentoSeleccionado.getMinimo(), instrumentoSeleccionado.getMaximo());

		instrumentoSeleccionado.agregarCalibracion(calibracion);
		calibracion.setInstrumento(instrumentoSeleccionado);

		updateModelAfterSave(Service.instance());
		return 1;
	}

	private boolean validateAndHandleEmptyField(String value, String fieldName) {
		if (value.isEmpty()) {
			view.showError("El " + fieldName + " no puede estar vacío");
			view.highlightEmptyField(fieldName);
			return false;
		}
		return true;
	}

	private void updateModelAfterSave(Service service) {
		Calibracion emptySearch = new Calibracion();
		try {
			model.setList(service.search(emptySearch));
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Calibracion calibracion) {
		model.getInstrumentoSeleccionado().getCalibraciones().remove(calibracion);
		try {
			Service.instance().delete(calibracion);
			model.setList(Service.instance().search(new Calibracion()));
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			if(e.getMessage().equals("Mediciones Asociadas")){
				view.showError(e.getMessage());
			}
			throw new RuntimeException(e.getMessage());
		}
	}

	public View getView() {
		return this.view;
	}

	public void generateReport() {
		String filePath = "src/main/java/una/reportes/calibraciones_report.pdf";
		ReportGenerator.generateCalibrationsReport(model, filePath);
		view.showMessage("Reporte generado exitosamente en: " + filePath);
	}

	public void loadList(List<Calibracion> calibracionList) {
		try {
			Service.instance().loadCalibracionList(calibracionList);
			model.setList(calibracionList);
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void instrumentoSeleccionadoCambiado(Instrumento instrumento) {
		// TODO: Eliminar este metodo y mover su contenido a la clase Mediator
		view.showCalibracionesTable(instrumento);
		view.mostrarInformacionInstrumento(instrumento);
		model.setInstrumentoSeleccionado(instrumento);
		if (instrumento == null) {
			return;
		}
		// recargar la lista de calibraciones
		loadList(instrumento.getCalibraciones());
	}

	public void noInstrumentSelected() {
		model.setList(new ArrayList<>());
		model.commit();
	}

	public void setList(ArrayList<Calibracion> list) {
		model.setList(list);
	}

	public void handleDeleteAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar una calibración");
			return;
		}

		try {
			Calibracion calibracion = model.getList().get(selectedRow);
			delete(calibracion);
			view.showMessage("La calibración número " + calibracion.getNumero() + " ha sido eliminada exitosamente");
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	public void handleSaveAction(String numero, LocalDate fecha, Integer numeroDeMediciones) {
		Calibracion calibracion = new Calibracion();
		calibracion.setNumero(model.getInstrumentoSeleccionado().getCalibraciones().size()+1+"");
		calibracion.setFecha(fecha);
		calibracion.setNumeroDeMediciones(numeroDeMediciones);
		if (save(calibracion, model.getInstrumentoSeleccionado()) == 1) {
			view.showMessage("Calibración número " + numero + " guardada exitosamente");
		} else {
			view.showError("No se pudo guardar la calibración");
		}
	}

	public void handleEditAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar una calibración");
			return;
		}

		Calibracion calibracion = model.getList().get(selectedRow);
		edit(selectedRow);
	}

	public void handleSearchAction(String searchNumero) {
		try {
			Calibracion filter = new Calibracion();
			filter.setNumero(searchNumero);
			search(filter);
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	public void handleListClick(int selectedRow) {
		if (selectedRow < 0) {
			return;
		}
		try {
			edit(selectedRow);
		} catch (Exception e) {
			view.showError("Parece que hubo un error al seleccionar la calibración");
		}
	}

	public Instrumento getInstrumentoSeleccionado() {
		return model.getInstrumentoSeleccionado();
	}

	public void setInstrumentoSeleccionado(Instrumento instrumentoSeleccionado) {
		model.setInstrumentoSeleccionado(instrumentoSeleccionado);
	}

}
package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.utiles.Utiles;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class View implements Observer {
	private JPanel panel;
	private JTextField searchNumero;
	private JButton search;
	private JButton save;
	private JTable list;
	private JButton delete;
	private JLabel searchNumeroLbl;
	private JTextField numero;
	private JTextField mediciones;
	private JTextField fecha;
	private JLabel numeroLbl;
	private JLabel medicionesLbl;
	private JLabel fechaLlb;
	private JButton clear;
	private JButton edit;
	private JButton report;
	private JLabel instrumentoLbl;
	private JTable medicionesList;
	private JScrollPane medicionesListContainer;
	private JLabel añoMesDiaLabel;

	private Controller controller;

	private MedicionesTableModel tableModel;
	private Model model;

	public View() {
		initializeUI();
		setupEventHandlers();
		initializeButtonStates();
	}

	private void initializeUI() {
		medicionesListContainer.setVisible(false);
		list.getTableHeader().setReorderingAllowed(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		medicionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private void setupEventHandlers() {
		search.addActionListener(e -> controller.handleSearchAction(searchNumero.getText()) );
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.handleListClick(list.getSelectedRow());

			}
		});
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clearAction();
			}
		});
		save.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (save.isEnabled()) {
					numero.setText(Utiles.generateRandomStringNumber());
					try{
						LocalDate localDate = Utiles.parseDate(fecha.getText());
						controller.handleSaveAction(numero.getText(), localDate, Integer.parseInt(mediciones.getText()));
					}catch (Exception ex){
						showError(ex.getMessage());
					}

				}
			}
		});
		delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.handleDeleteAction(list.getSelectedRow());
			}
		});
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.handleEditAction(list.getSelectedRow());
			}
		});
		report.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.generateReport();
			}
		});
		list.getSelectionModel().addListSelectionListener(e -> {
			updateDeleteButtonState();
			updateEditButtonState();
			updateSaveState();
		});
	}

	private void initializeButtonStates() {
		updateDeleteButtonState();
		updateEditButtonState();
		updateSaveState();
	}

	private void clearAction() {
		numero.setText("");
		mediciones.setText("0");
		list.clearSelection();
		save.setEnabled(true);
		numero.setEnabled(false);
		mediciones.setEnabled(true);
		fecha.setEnabled(true);
		medicionesListContainer.setVisible(false);
	}

	public void showError(String message) {
		JOptionPane.showMessageDialog(panel, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void setModel(Model model) {
		this.model = model;
		model.addObserver(this);
	}

	public void update(Observable updatedModel, Object properties) {
		int changedProps = (int) properties;

		if ((changedProps & Model.LIST) == Model.LIST) {
			updateCalibracionList();
		}

		if ((changedProps & Model.CURRENT) == Model.CURRENT) {
			updateCurrentCalibracion();
		}

		panel.revalidate();
	}

	private void updateCalibracionList() {
		int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
		list.setModel(new TableModel(cols, model.getList()));
		list.setRowHeight(30);
		TableColumnModel columnModel = list.getColumnModel();
		columnModel.getColumn(2).setPreferredWidth(200);
	}

	private void updateCurrentCalibracion() {
		Calibracion currentCalibracion = model.getCurrent();
		numero.setText(String.valueOf(currentCalibracion.getNumero()));
		fecha.setText(currentCalibracion.getFecha().toString());
		mediciones.setText(String.valueOf(currentCalibracion.getNumeroDeMediciones()));

		boolean enableEdit = currentCalibracion.getNumero().isEmpty() || model.getList().isEmpty();
		numero.setEnabled(false);

		int[] cols = {MedicionesTableModel.NUMERO, MedicionesTableModel.REFERENCIA, MedicionesTableModel.MEDICION};
		List<Boolean> editables = Arrays.asList(false, true, true);

		tableModel = new MedicionesTableModel(cols, currentCalibracion.getMediciones(), editables);
        medicionesList.setModel(tableModel);
		medicionesList.setRowHeight(30);
		TableColumnModel columnModel = medicionesList.getColumnModel();
		columnModel.getColumn(2).setPreferredWidth(200);

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (tableModel.necesitaResaltado(row, model.instrumentoSeleccionado)) {
					c.setBackground(Color.RED);
				} else {
					c.setBackground(table.getBackground());
				}
				return c;
			}
		};

		medicionesList.setDefaultRenderer(Object.class, renderer);

		medicionesListContainer.setVisible(!enableEdit);

        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                if (tableModel.necesitaResaltado(row,model.instrumentoSeleccionado)) {
                    JOptionPane.showMessageDialog(null, "El valor de la medición esta fuera del Rango de la Tolerancia del Instrumentos es decir, "+ model.instrumentoSeleccionado.getTolerancia());
                }
            }
        });


	}


	// Metodo que se llama cuando se selecciona un instrumento para mostrar la tabla de calibraciones
	public void showCalibracionesTable(Instrumento instrumentoSeleccionado) {
		try {
			if (instrumentoSeleccionado == null) {
				// Si el instrumento seleccionado es NULL, muestra la tabla vacía
				controller.noInstrumentSelected();
			} else {
				Calibracion filter = new Calibracion();
				String searchTerm = searchNumero.getText();

				filter.setInstrumento(instrumentoSeleccionado);
				filter.setNumero(searchTerm);	// da igual si esta vacio, porque el metodo search() lo ignora

				try {
					controller.search(filter);
				} catch (Exception ex) {
					// No se hace nada porque esto se ejecuta cuando se entra a esta pantalla
				}}
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	public void mostrarInformacionInstrumento(Instrumento instrumento) {
		if (instrumento == null) {
			instrumentoLbl.setText("No hay ningún instrumento seleccionado");
		} else {
			String labelText = String.format("%s - %s (%s - %s)",
					instrumento.getSerie(), instrumento.getDescripcion(), instrumento.getMinimo(), instrumento.getMaximo());
			instrumentoLbl.setText(labelText);
		}
	}

	public void highlightEmptyField(String fieldName) {
		switch (fieldName) {
			case "numero":
				numero.requestFocus();
				break;
			case "mediciones":
				mediciones.requestFocus();
				break;
			case "fecha":
				fecha.requestFocus();
				break;
		}
	}


	private void updateDeleteButtonState() {
		int selectedRowCount = list.getSelectedRowCount();
		delete.setEnabled(selectedRowCount > 0);
	}

	private void updateEditButtonState() {
		int selectedRowCount = list.getSelectedRowCount();
		edit.setEnabled(selectedRowCount > 0);	// Si hay una fila seleccionada, se activa el boton para modificar
		mediciones.setEnabled(selectedRowCount <= 0);	// Si hay una fila seleccionada, se desactiva el campo de mediciones
		fecha.setEnabled(selectedRowCount <= 0);	// Si hay una fila seleccionada, se desactiva el campo de fecha
	}

	private void updateSaveState() {
		int selectedRowCount = list.getSelectedRowCount();
		save.setEnabled(selectedRowCount == 0);
	}

	public String getNumero() {
		return numero.getText();
	}

	public String getMediciones() {
		return mediciones.getText();
	}

	public String getFecha() {
		return fecha.getText();
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(panel, message, "Informacion", JOptionPane.INFORMATION_MESSAGE);
	}

}

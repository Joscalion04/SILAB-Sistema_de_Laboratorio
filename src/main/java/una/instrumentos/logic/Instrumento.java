package una.instrumentos.logic;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
public class Instrumento {
	@XmlID
	String serie;
	String descripcion;
	@XmlIDREF
	@XmlElement(name = "tipoDelInstrumento")
	TipoInstrumento tipo;	// Combo box
	Integer minimo;
	Integer maximo;
	Integer tolerancia;
	//Lista de calibraciones
	@XmlElementWrapper(name = "calibraciones")
	@XmlElement(name = "calibracion")
	List<Calibracion> calibraciones;

	public Instrumento() {
		this("","",0,0,0,new TipoInstrumento());
		this.calibraciones = new ArrayList<Calibracion>();
	}
	public Instrumento(String serie, String descripcion, Integer minimo, Integer maximo, Integer tolerancia, TipoInstrumento tipo) {
		this.serie = serie;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.minimo = minimo;
		this.maximo = maximo;
		this.tolerancia = tolerancia;
		this.calibraciones = new ArrayList<Calibracion>();
	}

	public void updateInfo(Instrumento instrumento) {
		this.descripcion = instrumento.getDescripcion();
		this.tipo = instrumento.getTipo();
		this.minimo = instrumento.getMinimo();
		this.maximo = instrumento.getMaximo();
		this.tolerancia = instrumento.getTolerancia();
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoInstrumento getTipo() {
		return tipo;
	}

	public void setTipo(TipoInstrumento tipo)throws Exception {
		if(!calibraciones.isEmpty()){
			throw new Exception("Calibraciones Asociadas");
		}else{
			this.tipo = tipo;
		}
	}

	public Integer getMinimo() {
		return minimo;
	}

	public void setMinimo(Integer minimo) {
		this.minimo = minimo;
	}

	public Integer getMaximo() {
		return maximo;
	}

	public void setMaximo(Integer maximo) {
		this.maximo = maximo;
	}

	public Integer getTolerancia() {
		return tolerancia;
	}

	public void setTolerancia(Integer tolerancia) {
		this.tolerancia = tolerancia;
	}

	public List<Calibracion> getCalibraciones() {
		return calibraciones;
	}

	public void setCalibraciones(List<Calibracion> calibraciones) {
		this.calibraciones = calibraciones;
	}

	public void agregarCalibracion(Calibracion calibracion) {
		calibraciones.add(calibracion);
	}
	public boolean hasCalibraciones() {
		return !calibraciones.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Instrumento that = (Instrumento) o;
		return Objects.equals(serie, that.serie) && Objects.equals(descripcion, that.descripcion) && Objects.equals(tipo, that.tipo) && Objects.equals(minimo, that.minimo) && Objects.equals(maximo, that.maximo) && Objects.equals(tolerancia, that.tolerancia);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serie, descripcion, tipo, minimo, maximo, tolerancia);
	}
}

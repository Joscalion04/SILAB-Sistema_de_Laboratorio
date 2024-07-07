package una.instrumentos.presentation.acercaDe;

import javax.swing.*;
import java.awt.*;

public class View {
	public View() {
		agregarElTextoAcercaDe();
	}
	private JPanel panel;
	private JLabel acercaDeLabel;

	private void agregarElTextoAcercaDe() {
		StringBuilder s = new StringBuilder();
		s.append("<html>");
		s.append("<style>");
		s.append("body {");
		s.append("    font-family: 'Arial', sans-serif;");
		s.append("    background-color: #f0f0f0;");
		s.append("    color: #444444;");
		s.append("    text-align: center;");
		s.append("    padding: 20px;");
		s.append("}");
		s.append("h1 {");
		s.append("    font-size: 48px;");
		s.append("    text-transform: uppercase;");
		s.append("    color: #ff6f61;");
		s.append("    margin-bottom: 10px;");
		s.append("}");
		s.append("p {");
		s.append("    font-size: 18px;");
		s.append("    line-height: 1.6;");
		s.append("    color: #666666;");
		s.append("}");
		s.append(".developers, .contact-info {");
		s.append("    margin-top: 30px;");
		s.append("}");
		s.append(".developers h1, .contact-info h1 {");
		s.append("    font-size: 28px;");
		s.append("    color: #007bff;");
		s.append("    margin-bottom: 10px;");
		s.append("}");
		s.append(".developers p, .contact-info p {");
		s.append("    margin: 5px 0;");
		s.append("}");
		s.append("</style>");
		s.append("<body>");
		s.append("<h1>Silab: Sistema de Laboratorios</h1>");
		s.append("<p>Version 2.0</p>");
		s.append("<p>Copyright (C) 2024</p>");
		s.append("<div class='developers'>");
		s.append("    <h1>Desarrolladores</h1>");
		s.append("    <p>Alessandro Cambronero Delgado</p>");
		s.append("    <p>Domingo Camacho González</p>");
		s.append("    <p>Juan Ignacio Arrieta Mena</p>");
		s.append("    <p>Joseph León Cabezas</p>");
		s.append("</div>");
		s.append("<div class='contact-info'>");
		s.append("    <h1>Contactos</h1>");
		s.append("    <p>alessandro.cambronero.delgado@est.una.ac.cr</p>");
		s.append("    <p>domingo.camacho.gonzalez@est.una.ac.cr</p>");
		s.append("    <p>ignacio.arrieta.mena@est.una.ac.cr</p>");
		s.append("    <p>joseph.leon.cabezas@est.una.ac.cr</p>");
		s.append("</div>");

		// Cargar la imagen deseada desde los recursos.
		s.append("<img src=\"" + getClass().getResource("/icon.png") + "\">");

		s.append("</body>");
		s.append("</html>");
		acercaDeLabel.setText(s.toString());
	}

	public Component getPanel() {
		return panel;
	}
}

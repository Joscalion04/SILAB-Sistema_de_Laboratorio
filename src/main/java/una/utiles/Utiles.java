package una.utiles;

import com.itextpdf.text.pdf.PdfPTable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utiles {
    public static LocalDate parseDate(String dateString) throws Exception {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            if(localDate.isAfter(LocalDate.now())){
                throw new Exception("1");
            }else{
                return localDate;
            }
        } catch (Exception e) {
            if(e.getMessage().equals("1")){
                throw new Exception("No se pueden agregar calibraciones en el futuro");
            }else {
                throw new Exception("Formato de fecha inválido: Año-Mes-Dia");
            }
        }
    }

    public static PdfPTable formatDate(LocalDate fecha) {
        PdfPTable table = new PdfPTable(1);
        table.addCell(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return table;
    }

    public static String generateRandomStringNumber(){return String.valueOf((int) (Math.random() * 1000000));}
}


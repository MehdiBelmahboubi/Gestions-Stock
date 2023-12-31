package Table_data;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

public class Impression_Exportation extends Component {
    public static void imprimerTable(JTable table, String titre)
    {
        MessageFormat entete = new MessageFormat(titre);
        MessageFormat pied = new MessageFormat("Page{0,number,integer}");

        try {
            table.print(JTable.PrintMode.FIT_WIDTH,entete,pied);
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Erreur :\n"+e,"Impression",JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            throw new RuntimeException("Error opening file: " + e.getMessage(), e);
        }
    }

    public static void exporterExcel(JTable table) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(table);
            File saveFile = jFileChooser.getSelectedFile();

            if (saveFile != null) {
                if (!saveFile.getName().toLowerCase().endsWith(".xlsx")) {
                    saveFile = new File(saveFile.toString() + ".xlsx");
                }

                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("film");
                Row headerRow = sheet.createRow(0);

                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                }

                for (int j = 0; j < table.getRowCount(); j++) {
                    Row row = sheet.createRow(j + 1);
                    for (int k = 0; k < table.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (table.getValueAt(j, k) != null) {
                            cell.setCellValue(table.getValueAt(j, k).toString());
                        }
                    }
                }

                try (FileOutputStream out = new FileOutputStream(saveFile)) {
                    wb.write(out);
                }

                wb.close();
                openFile(saveFile.toString());
                JOptionPane.showMessageDialog(null, "Data exported successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Export canceled or no file selected.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting data: " + e.getMessage());
            throw new RuntimeException("Error exporting data: " + e.getMessage(), e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unexpected error: " + e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }
}

package Table_data;

import javax.swing.*;

public class navigation_button {
    public static void FirstRow(JTable table)
    {
        if(table.getRowCount()>0)
        {
            table.setRowSelectionInterval(0,0);
        }
    }

    public static void LastRow(JTable table)
    {
        int lastRow = table.getRowCount() - 1;
        if(lastRow >= 0){
            table.setRowSelectionInterval(lastRow,lastRow);
        }
    }

    public static void NextRow(JTable table)
    {
        int currentRowIndex = table.getSelectedRow();
        if(table.getRowCount() - 1 > currentRowIndex)
        {
            int nextRowIndex = currentRowIndex + 1;
            table.setRowSelectionInterval(nextRowIndex,nextRowIndex);
        }
    }

    public static void PreviousRow(JTable table)
    {
        int currentRowIndex = table.getSelectedRow();
        if(currentRowIndex > 0)
        {
            int previousRowIndex = currentRowIndex - 1;
            table.setRowSelectionInterval(previousRowIndex,previousRowIndex);
        }
    }
}

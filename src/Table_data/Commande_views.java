package Table_data;


import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Commande_views extends Component {
    public static void populateTable(JTable table) throws SQLException {
        try(Connection con = connection.getConnection())
        {
            String query = "Select * From Commande";
            try(PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i=1;i<= columnCount;i++){
                    columnNames[i - 1]=metaData.getColumnName(i);
                }

                DefaultTableModel model = new DefaultTableModel(columnNames,0);

                while(resultSet.next())
                {
                    Object[] rowData = new Object[columnCount];
                    for (int i=1;i<=columnCount;i++)
                    {
                        rowData[i-1]=resultSet.getObject(i);
                    }
                    model.addRow(rowData);
                }
                table.setModel(model);
            }
        }
    }


    public static void populateCombo(JComboBox<String> j1) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT code FROM clients";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Clear existing items in the combo box
                j1.removeAllItems();

                // Add items from the result set to the combo box
                while (resultSet.next()) {
                    String nom = resultSet.getString("Code");
                    j1.addItem(nom);
                }
            }
        }
    }



    public static void SelectRow(JTextField j1, JComboBox<String> j2, JDateChooser j3, JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            String num = model.getValueAt(selectedRow, 0).toString();
            String codeClient = model.getValueAt(selectedRow, 2).toString();
            Object dateObj = model.getValueAt(selectedRow, 1);

            // Convert the date object to a string and set it to the JDateChooser
            if (dateObj instanceof Date) {
                j3.setDate((Date) dateObj);
            } else {
                // Handle the case where the date object is not a Date
                j3.setDate(null); // Set the date to null if it's not a valid date
            }

            j1.setText(num);
            j2.setSelectedItem(codeClient);
        }
    }


    public static void addCommande(JTextField j1, JComboBox<String> j2, JDateChooser j3, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String insertQuery = "INSERT INTO `Commande` (`Num`, `Date_cmd`, `code_client`) VALUES (?, ?, ?);";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, j1.getText());

                java.util.Date dateChooserDate = j3.getDate();
                if (dateChooserDate != null) {
                    Date sqlDate = new Date(dateChooserDate.getTime());
                    preparedStatement.setDate(2, sqlDate);
                } else {
                    preparedStatement.setNull(2, java.sql.Types.DATE);
                }

                preparedStatement.setString(3, j2.getSelectedItem().toString());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteCommande(JTextField j1,JTable table)
    {
        try(Connection con = connection.getConnection()){
            String query = "Delete from Commande where Num = ? ";
            try(PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setString(1,j1.getText());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateCommande(JTextField j1, JComboBox<String> j2, JDateChooser j3, JTable table) {
        try (Connection con = connection.getConnection()) {
            String query = "UPDATE `commande` SET `Num` = ?, `Date_cmd` = ?, `code_client` = ? WHERE `commande`.`Num` = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, j1.getText());

                // Convert the JDateChooser date to java.sql.Date
                java.util.Date dateChooserDate = j3.getDate();
                if (dateChooserDate != null) {
                    java.sql.Date sqlDate = new java.sql.Date(dateChooserDate.getTime());
                    preparedStatement.setDate(2, sqlDate);
                } else {
                    preparedStatement.setNull(2, java.sql.Types.DATE);
                }

                preparedStatement.setString(3, j2.getSelectedItem().toString());
                preparedStatement.setString(4, j1.getText());

                preparedStatement.executeUpdate();
                populateTable(table);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void SearchCommande(JTextField j1, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT * FROM commande WHERE Num = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, j1.getText());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    String[] columnNames = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames[i - 1] = metaData.getColumnName(i);
                    }
                    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                    while (resultSet.next()) {
                        Object[] rowData = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            rowData[i - 1] = resultSet.getObject(i);
                        }
                        model.addRow(rowData);
                    }
                    table.setModel(model);
                }
            }
        }
    }


    public static void clear(JTextField j1, JComboBox<String> j2, JDateChooser j3, JTable table) {
        j1.setText("");
        j2.setSelectedItem(null);
        j3.setDate(null);

        try {
            populateTable(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

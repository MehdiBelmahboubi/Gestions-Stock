package Table_data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class lignelivraison_view extends Component {

    public static void populateTable(JTable table) throws SQLException {
        try(Connection con = connection.getConnection())
        {
            String query = "Select * From ligne_livraison";
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


    public static void populateCombo2(JComboBox<String> j1) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT code FROM fournisseur";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Clear existing items in the combo box
                j1.removeAllItems();

                // Add items from the result set to the combo box
                while (resultSet.next()) {
                    String nom = resultSet.getString("code");
                    j1.addItem(nom);
                }
            }
        }
    }


    public static void populateCombo1(JComboBox<String> j1) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT code FROM article";
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


    public static void SelectRow(JTextField j1,JTextField j2, JComboBox<String> j3, JComboBox<String> j4, JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            String id = model.getValueAt(selectedRow, 0).toString();
            String Quantite_livr = model.getValueAt(selectedRow, 1).toString();
            String codeArticle = model.getValueAt(selectedRow, 2).toString();
            String codeFrn = model.getValueAt(selectedRow, 3).toString();


            j1.setText(id);
            j2.setText(Quantite_livr);
            j3.setSelectedItem(codeArticle);
            j4.setSelectedItem(codeFrn);
        }
    }


    public static void addLigneliv(JTextField j1,JTextField j2, JComboBox<String> j3, JComboBox<String> j4, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String insertQuery = "INSERT INTO `ligne_livraison` (`id`,`quantite_livre`, `code_article`, `code_fourn`) VALUES (?, ?, ?,?);";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, j1.getText());
                preparedStatement.setString(2,j2.getText());
                preparedStatement.setString(3, j3.getSelectedItem().toString());
                preparedStatement.setString(4, j4.getSelectedItem().toString());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public static void DeleteLigneliv(JTextField j1,JTable table)
    {
        try(Connection con = connection.getConnection()){
            String query = "Delete from ligne_livraison where id = ? ";
            try(PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setString(1,j1.getText());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public static void UpdateLigneliv(JTextField j1,JTextField j2, JComboBox<String> j3, JComboBox<String> j4, JTable table) {
        try (Connection con = connection.getConnection()) {
            String query = "UPDATE `ligne_livraison` SET `id` = ?,`quantite_livre` = ?, `code_article` = ?, `code_fourn` = ? WHERE `ligne_livraison`.`id` = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, j1.getText());
                preparedStatement.setString(2, j2.getText());
                preparedStatement.setString(3, j3.getSelectedItem().toString());
                preparedStatement.setString(4, j4.getSelectedItem().toString());
                preparedStatement.setString(5, j1.getText());

                preparedStatement.executeUpdate();
                populateTable(table);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void SearchLigneliv(JTextField j1, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT * FROM ligne_livraison WHERE id = ?";
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


    public static void clear(JTextField j1,JTextField j2, JComboBox<String> j3, JComboBox<String>  j4, JTable table) {
        j1.setText("");
        j2.setText("");
        j3.setSelectedItem(null);
        j4.setSelectedItem(null);

        try {
            populateTable(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

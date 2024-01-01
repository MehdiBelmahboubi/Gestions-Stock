package Table_data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ligneCommande_view extends Component {

    public static void populateTable(JTable table) throws SQLException {
        try(Connection con = connection.getConnection())
        {
            String query = "Select * From ligne_commande";
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


    public static void populateCombo1(JComboBox<String> j1) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT Num FROM Commande";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Clear existing items in the combo box
                j1.removeAllItems();

                // Add items from the result set to the combo box
                while (resultSet.next()) {
                    String nom = resultSet.getString("Num");
                    j1.addItem(nom);
                }
            }
        }
    }

    public static void populateCombo2(JComboBox<String> j1) throws SQLException {
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
            String Quantite_cmd = model.getValueAt(selectedRow, 0).toString();
            String codeDepot = model.getValueAt(selectedRow, 2).toString();
            String codeArticle = model.getValueAt(selectedRow, 3).toString();


            j1.setText(id);
            j2.setText(Quantite_cmd);
            j3.setSelectedItem(codeDepot);
            j4.setSelectedItem(codeArticle);
        }
    }


    public static void addLigneCmd(JTextField j1,JTextField j2, JComboBox<String> j3, JComboBox<String> j4, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String insertQuery = "INSERT INTO `ligne_commande` (`Num`,`quantite_commande`, `code_cmd`, `code_article`) VALUES (?, ?, ?);";
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


    public static void DeleteStock(JTextField j1,JTable table)
    {
        try(Connection con = connection.getConnection()){
            String query = "Delete from ligne_commande where id = ? ";
            try(PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setString(1,j1.getText());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}

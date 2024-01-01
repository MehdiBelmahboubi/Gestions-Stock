package Table_data;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Societedistribution_view extends Component {

    public static void populateTable(JTable table) throws SQLException {
        try(Connection con = connection.getConnection())
        {
            String query = "Select * From societe_distribution";
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
            String query = "SELECT code FROM fournisseur";
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

    public static void SelectRow(JTextField j1,JTextField j2,JTextField j3,JTextField j4 ,JTextField j5,JTextField j6,JComboBox j7, JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            String id = model.getValueAt(selectedRow, 0).toString();
            String nom = model.getValueAt(selectedRow, 1).toString();
            String telephone = model.getValueAt(selectedRow, 2).toString();
            String Rue = model.getValueAt(selectedRow, 3).toString();
            String ville = model.getValueAt(selectedRow, 4).toString();
            String adresse = model.getValueAt(selectedRow, 5).toString();
            String code_fourn = model.getValueAt(selectedRow, 6).toString();

            j1.setText(id);
            j2.setText(nom);
            j3.setText(telephone);
            j4.setText(Rue);
            j5.setText(ville);
            j6.setText(adresse);
            j7.setSelectedItem(code_fourn);
        }
    }


    public static void addSociete(JTextField j1,JTextField j2,JTextField j3,JTextField j4 ,JTextField j5,JTextField j6,JComboBox j7, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String insertQuery = "INSERT INTO `societe_distribution` (`id`, `nom`, `telephone`, `rue`, `ville`, `adresse`, `code_fourni`) VALUES (?, ?, ?, ?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setString(1,j1.getText());
                preparedStatement.setString(2,j2.getText());
                preparedStatement.setString(3,j3.getText());
                preparedStatement.setString(4,j4.getText());
                preparedStatement.setString(5,j5.getText());
                preparedStatement.setString(6,j6.getText());
                preparedStatement.setString(7, j7.getSelectedItem().toString());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public static void DeleteSociete(JTextField j1,JTable table)
    {
        try(Connection con = connection.getConnection()){
            String query = "Delete from societe_distribution where id = ? ";
            try(PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setString(1,j1.getText());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public static void UpdateSociete(JTextField j1,JTextField j2,JTextField j3,JTextField j4 ,JTextField j5,JTextField j6,JComboBox j7, JTable table) {
        try (Connection con = connection.getConnection()) {
            String query = "UPDATE `societe_distribution` SET `id` = ?,`nom` = ?, `telephone` = ?, `rue` = ?, `ville` = ?, `adresse` = ?,`code_fourni` = ? WHERE `societe_distribution`.`id` = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, j1.getText());
                preparedStatement.setString(2,j2.getText());
                preparedStatement.setString(3,j3.getText());
                preparedStatement.setString(4,j4.getText());
                preparedStatement.setString(5,j5.getText());
                preparedStatement.setString(6,j6.getText());
                preparedStatement.setString(7, j7.getSelectedItem().toString());
                preparedStatement.setString(8, j1.getText());

                preparedStatement.executeUpdate();
                populateTable(table);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void SearchSociete(JTextField j1, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT * FROM societe_distribution WHERE id = ?";
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


    public static void clear(JTextField j1,JTextField j2,JTextField j3,JTextField j4 ,JTextField j5,JTextField j6,JComboBox j7, JTable table) {
        j1.setText("");
        j2.setText("");
        j3.setText("");
        j4.setText("");
        j5.setText("");
        j6.setText("");
        j7.setSelectedItem(null);

        try {
            populateTable(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

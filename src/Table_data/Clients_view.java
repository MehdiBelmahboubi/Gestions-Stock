package Table_data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.MessageFormat;

public class Clients_view extends Component {

    public static void populateTable(JTable table) throws SQLException {
        try(Connection con = connection.getConnection())
        {
            String query = "Select * From Clients";
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

    public static void addClient(JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTextField j5, JTextField j6, JTextField j7, JTable table) throws SQLException {
        try(Connection con = connection.getConnection()) {
            String insertquery = "INSERT INTO `clients` (`Code`, `Nom`, `Prenom`, `Adresse`, `Ville`, `Pays`, `numero_tele`) VALUES (?, ?, ?, ?, ?, ?, ?);";
            try(PreparedStatement preparedStatement = con.prepareStatement(insertquery)) {
                preparedStatement.setString(1,j1.getText());
                preparedStatement.setString(2,j2.getText());
                preparedStatement.setString(3,j3.getText());
                preparedStatement.setString(4,j4.getText());
                preparedStatement.setString(5,j5.getText());
                preparedStatement.setString(6,j6.getText());
                preparedStatement.setString(7,j7.getText());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e){
            e.printStackTrace();
        }
    }

    public static void DeleteClient(JTextField j1,JTable table)
    {
        try(Connection con = connection.getConnection()){
            String query = "Delete from Clients where Code = ? ";
            try(PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setString(1,j1.getText());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateClient(JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTextField j5, JTextField j6, JTextField j7, JTable table)
    {
            try(Connection con = connection.getConnection()) {
                String query = "UPDATE `clients` SET `Code` = ?, `Nom` = ?, `Prenom` = ?, `Adresse` = ?, `Ville` = ?, `Pays` = ?, `numero_tele` = ? WHERE `clients`.`Code` = ?";
                try (PreparedStatement preparedStatement = con.prepareStatement(query)){
                    preparedStatement.setString(1,j1.getText());
                    preparedStatement.setString(2,j2.getText());
                    preparedStatement.setString(3,j3.getText());
                    preparedStatement.setString(4,j4.getText());
                    preparedStatement.setString(5,j5.getText());
                    preparedStatement.setString(6,j6.getText());
                    preparedStatement.setString(7,j7.getText());
                    preparedStatement.setString(8,j1.getText());

                    preparedStatement.executeUpdate();
                    populateTable(table);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public static void SearchClient(JTextField j1, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT * FROM Clients WHERE code = ?";
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


    public static void clear(JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTextField j5, JTextField j6, JTextField j7,JTable table)
    {
        j1.setText("");
        j2.setText("");
        j3.setText("");
        j4.setText("");
        j5.setText("");
        j6.setText("");
        j7.setText("");
        try {
            populateTable(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void SelectRow(JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTextField j5, JTextField j6, JTextField j7, JTable table)
    {
        int selectedRow = table.getSelectedRow();

        if (selectedRow>=0){
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            String code = model.getValueAt(selectedRow,0).toString();
            String nom = model.getValueAt(selectedRow,1).toString();
            String prenom = model.getValueAt(selectedRow,2).toString();
            String adresse = model.getValueAt(selectedRow,3).toString();
            String ville = model.getValueAt(selectedRow,4).toString();
            String pays = model.getValueAt(selectedRow,5).toString();
            String num_tele = model.getValueAt(selectedRow,6).toString();

            j1.setText(code);
            j2.setText(nom);
            j3.setText(prenom);
            j4.setText(adresse);
            j5.setText(ville);
            j6.setText(pays);
            j7.setText(num_tele);
        }
    }
}

package Table_data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.MessageFormat;

public class article_view extends Component {

    public static void populateTable(JTable table) throws SQLException {
        try(Connection con = connection.getConnection())
        {
            String query = "Select * From article";
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

    public static void addarticle(JTextField j1, JTextField j2, JTextField j3, JTextField j4,  JTable table) throws SQLException {
        try(Connection con = connection.getConnection()) {
            String insertquery = "INSERT INTO `article` (`code`, `Nom`, `Marque`, `prix`) VALUES (?, ?, ?, ?);";
            try(PreparedStatement preparedStatement = con.prepareStatement(insertquery)) {
                preparedStatement.setString(1,j1.getText());
                preparedStatement.setString(2,j2.getText());
                preparedStatement.setString(3,j3.getText());
                preparedStatement.setString(4,j4.getText());


                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e){
            e.printStackTrace();
        }
    }

    public static void Deletearticle(JTextField j1,JTable table)
    {
        try(Connection con = connection.getConnection()){
            String query = "Delete from article where code = ? ";
            try(PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setString(1,j1.getText());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void Updatearticle(JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTable table)
    {
        try(Connection con = connection.getConnection()) {
            String query = "UPDATE `article` SET `code` = ?, `Nom` = ?, `Marque` = ?, `prix` = ? WHERE `article`.`code` = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setString(1,j1.getText());
                preparedStatement.setString(2,j2.getText());
                preparedStatement.setString(3,j3.getText());
                preparedStatement.setString(4,j4.getText());
                preparedStatement.setString(5,j1.getText());

                preparedStatement.executeUpdate();
                populateTable(table);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Searcharticle(JTextField j1, JTable table) throws SQLException {
        try (Connection con = connection.getConnection()) {
            String query = "SELECT * FROM article WHERE code = ?";
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


    public static void clear(JTextField j1, JTextField j2, JTextField j3, JTextField j4,JTable table)
    {
        j1.setText("");
        j2.setText("");
        j3.setText("");
        j4.setText("");

        try {
            populateTable(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void SelectRow(JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTable table)
    {
        int selectedRow = table.getSelectedRow();

        if (selectedRow>=0){
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            String code = model.getValueAt(selectedRow,0).toString();
            String Nom = model.getValueAt(selectedRow,1).toString();
            String quantite_stock = model.getValueAt(selectedRow,2).toString();
            String prix = model.getValueAt(selectedRow,3).toString();


            j1.setText(code);
            j2.setText(Nom);
            j3.setText(quantite_stock);
            j4.setText(prix);

        }
    }
}

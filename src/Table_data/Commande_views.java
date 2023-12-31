package Table_data;


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
            String query = "SELECT Nom FROM clients";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Clear existing items in the combo box
                j1.removeAllItems();

                // Add items from the result set to the combo box
                while (resultSet.next()) {
                    String nom = resultSet.getString("Nom");
                    j1.addItem(nom);
                }
            }
        }
    }



    public static void SelectRow(JTextField j1, JComboBox j2, JTextField j3, JTable table)
    {
        int selectedRow = table.getSelectedRow();

        if (selectedRow>=0){
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            String Num = model.getValueAt(selectedRow,0).toString();
            String date = model.getValueAt(selectedRow,1).toString();
            String code_client = model.getValueAt(selectedRow,2).toString();

            j1.setText(Num);
            j2.setSelectedItem(code_client);
            j3.setText(date);

        }
    }

    public static void addCommande(JTextField j1, JComboBox j2, JTextField j3, JTable table) throws SQLException {
        try(Connection con = connection.getConnection()) {
            String insertquery = "INSERT INTO `Commande` (`Num`, `Date_cmd`, `code_client`) VALUES (?, ?, ?);";
            try(PreparedStatement preparedStatement = con.prepareStatement(insertquery)) {
                preparedStatement.setString(1,j1.getText());
                preparedStatement.setString(2,j3.getText());
                preparedStatement.setString(3,j2.toString());

                preparedStatement.executeUpdate();

                populateTable(table);
            }
        } catch (SQLException | NumberFormatException e){
            e.printStackTrace();
        }
    }

    public static void clear(JTextField j1, JComboBox j2, JTextField j3,JTable table)
    {
        j1.setText("");
        j2.setSelectedItem(null);
        j3.setText("");
        try {
            populateTable(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

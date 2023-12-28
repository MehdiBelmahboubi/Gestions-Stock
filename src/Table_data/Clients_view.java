package Table_data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

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

    public  void addPerson(JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTextField j5, JTextField j6, JTextField j7, JTable table) throws SQLException {
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

                int rows = preparedStatement.executeUpdate();

                if(rows > 0)
                {
                    populateTable(table);
                    JOptionPane.showMessageDialog(this,"Client bien ajouter.");
                }else {
                    JOptionPane.showMessageDialog(this,"Ajout Refuser !!!");
                }
            }
        } catch (SQLException | NumberFormatException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error connecting to database");
        }
    }
}

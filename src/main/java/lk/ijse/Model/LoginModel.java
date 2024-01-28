package lk.ijse.Model;

import lk.ijse.DBConnection.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginModel {
    public static List<String> getClientName() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT User_name FROM user";

        List<String> name = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            name.add(resultSet.getString(1));
        }
        return name;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.sql.*;
import java.util.List;

/**
 *
 * @author Asus
 */
public class DatabaseSQL {

    private String url = "jdbc:mysql://localhost:3306/users";
    private String username = "root";
    private String password = "Facebook123!";
    private Object User;

    public void connectAndFetchData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(String check) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usersdata WHERE UserName = ? OR EmailAddress = ? OR ContactNumber = ?");
            ps.setString(1, check);
            ps.setString(2, check);
            ps.setString(3, check);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rs.close();
                ps.close();
                con.close();
                return true;
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void registerUser(User user) {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("INSERT INTO usersdata (Account_ID, UserName, EmailAddress, ContactNumber, Password) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, user.getAccountID());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmailAddress());
            ps.setString(4, user.getContactNumber());
            ps.setString(5, user.getPassword());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserLogin(String loginId) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usersdata WHERE UserName = ? OR EmailAddress = ?");
            ps.setString(1, loginId);
            ps.setString(2, loginId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User.Builder builder = new User.Builder()
                        .setAccountID(rs.getString("Account_ID"))
                        .setUsername(rs.getString("UserName"))
                        .setEmailAddress(rs.getString("EmailAddress"))
                        .setContactNumber(rs.getString("ContactNumber"))
                        .setPassword(rs.getString("Password"));

                rs.close();
                ps.close();
                con.close();

                return builder.build();
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateUserDetail(User user) {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("UPDATE usersdata SET Account_ID=?, UserName=?, EmailAddress=?, ContactNumber=?, Password=?, Name=?, Birthday=?, Age=?, Address=?, Gender=?, Relationship_Status=?, Hobbies=?, Jobs=?, NumberOfFriends=? WHERE Account_ID=?");
            ps.setString(1, user.getAccountID());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmailAddress());
            ps.setString(4, user.getContactNumber());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getName());
            ps.setString(7, user.getBirthday());
            ps.setInt(8, user.getAge());
            ps.setString(9, user.getAddress());
            ps.setString(10, user.getGender());
            ps.setString(11, user.getRelationshipStatus());
            ps.setString(12, user.getHobbies() != null ? String.join(",", user.getHobbies()) : null);
            ps.setString(13, user.getJobs() != null ? String.join(",", user.getJobs()) : null);
            ps.setInt(14, user.getNumberOfFriends());
            ps.setString(15, user.getAccountID());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditUserDetail(User user, String columnName) {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("UPDATE usersdata SET " + columnName + " = ? WHERE Account_ID = ?");

            switch (columnName) {
                case "UserName":
                    ps.setString(1, user.getUsername());
                    break;
                case "EmailAddress":
                    ps.setString(1, user.getEmailAddress());
                    break;
                case "ContactNumber":
                    ps.setString(1, user.getContactNumber());
                    break;
                case "Password":
                    ps.setString(1, user.getPassword());
                    break;
                case "Name":
                    ps.setString(1, user.getName());
                    break;
                case "Birthday":
                    ps.setString(1, user.getBirthday());
                    break;
                case "Address":
                    ps.setString(1, user.getAddress());
                    break;
                case "Gender":
                    ps.setString(1, user.getGender());
                    break;
                case "Relationship_Status":
                    ps.setString(1, user.getRelationshipStatus());
                    break;
                default:
                    System.out.println("Invalid column name: " + columnName);
                    return;
            }

            ps.setString(2, user.getAccountID());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

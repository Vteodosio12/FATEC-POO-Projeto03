package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author iego_
 */
public class Result {
    private long rowId;
    private long result;
    private User user;
    private CategoryEnum category;

    public Result(long rowId, long result, User user, CategoryEnum category) {
        this.rowId = rowId;
        this.result = result;
        this.user = user;
        this.category = category;
    }
    
    public Result(long rowId, long result, String userLogin, long categoryEnumId) throws Exception {
        this.rowId = rowId;
        this.result = result;
        this.user.setLogin(userLogin);
        this.category.setId(categoryEnumId);
//        this.category.setName(CategoryEnum.getCategoryEnum(categoryEnumId).getName());
    }
    
    public static ArrayList<Result> getList() throws Exception{
        ArrayList<Result> list = new ArrayList<>();
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(web.DbListener.URL);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from results");
        while(rs.next()){
            list.add(new Result(
                rs.getLong("rowid"),
                rs.getLong("result"),
                rs.getString("fk_user_login"),
                rs.getLong("fk_category_enum")
            ));
        }
        rs.close();
        stmt.close();
        con.close();
        return list;
    }
    
    public static Result getResult(long id)throws Exception{
        Result transaction = null;
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(web.DbListener.URL);
        String SQL = "SELECT rowid, * from results WHERE id=?";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            transaction = new Result(
                rs.getLong("rowid"),
                rs.getLong("result"),
                rs.getString("fk_user_login"),
                rs.getLong("fk_category_enum")
            );
        }
        rs.close();
        stmt.close();
        con.close();
        return transaction;
    }
    
    public static void addResult(long result, User user, CategoryEnum category) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(web.DbListener.URL);
        String SQL;
        if (category != null) {
            SQL = "INSERT INTO results(result, fk_user_login, fk_category_enum) VALUES(?,?,?)";
        } else {
            SQL = "INSERT INTO results(result, fk_user_login, fk_category_enum) VALUES(?,?)";
        }
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, result);
        stmt.setString(2, user.getLogin());
        if (category != null) {
            stmt.setLong(3, category.getId());
        }
        stmt.execute();
        stmt.close();
        con.close();
    }
    
    public static void putResult(long result, User user, CategoryEnum category, long rowId) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(web.DbListener.URL);
        String SQL;
        if (category != null) {
            SQL = "UPDATE results SET result=?, fk_user_login=?, fk_category_enum=?  WHERE id=?";
        } else {
            SQL = "UPDATE results SET result=?, fk_user_login=? WHERE id=?";
        }
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, result);
        stmt.setString(2, user.getLogin());
        if (category != null) {
            stmt.setLong(3, category.getId());
            stmt.setLong(4, rowId);
        } else {
            stmt.setLong(3, rowId);
        }
        stmt.execute();
        stmt.close();
        con.close();
    }
    
    public static void removeResult(long id) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(web.DbListener.URL);
        String SQL = "DELETE FROM results WHERE id=?";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, id);
        stmt.execute();
        stmt.close();
        con.close();
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }
}

package sql;

import java.sql.*;

public class DBTools {
    private static final String URL = "jdbc:mysql://localhost:3306/student" +
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";//jdbc总协议 mysql子协议 子名称//localhost:3307/student
    private static final String USERNAME = "root";
    private static final String PASSWORD = "5978836.wo";
    private static DBTools db;//
    private Connection track;//接口connection创建的track
    private PreparedStatement order;//创建prepare statement对象传输sql语句到数据库执行数据操作
    // 该接口属于预处理操作 同一对象可以多次使用 sql语句可以带参数 具体内容可以暂时不设置
    // 类似的接口还有statement不过statement传输的是执行的是静态的sql语句 没有参数的sql语句通常都是statement执行 resultset产生的对象 一个只产生一个

    //private DBTools() {}

    public static DBTools getDBTools() {
        if (db == null) {
            db = new DBTools();
        }
        return db;
    }

    // 测试SQL服务器能否链接
    public static boolean isAvailable() {
        try (Connection test = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            if (test == null) {
                throw new SQLException("Can't create a track");
            }
            return true;
        } catch (SQLException throwables) {
            System.err.println("SQL Serve is not available");
            throwables.printStackTrace();
            return false;
        }
    }

    public void close() {
        if (db != null) {
            try {
                if (db.track != null) {
                    db.track.close();//关闭数据库
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Connection getConnection() {//连接数据库
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//Class.forName()加载驱动
            if (track == null || track.isClosed()) {
                this.track = DriverManager.getConnection(URL, USERNAME, PASSWORD);//connection接口对象track链接
                // DriverManager.getConnection()用于链接数据库，通过地址以及用户账号
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.err.println("Not Find the Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return track;
    }

    // 用于查询
    public ResultSet query(String sql){
        try {
            order = getConnection().prepareStatement(sql);
            return order.executeQuery();//executeQuery用于执行sql语句
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
       return null;
    }
    // 用于信息添加和修改
    public int update(String sql) {
        int status = 0;
        try {
            order = getConnection().prepareStatement(sql);
            status = order.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }
    public int update(String sql, Object[] param) {
        int stauts = 0;
        try {
            order = getConnection().prepareStatement(sql);
            for (int i = 1; i < param.length; i++) {
                order.setObject(i, param[i]);
            }
            stauts = order.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stauts;
    }
}
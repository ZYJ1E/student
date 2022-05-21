package sql;

import java.sql.*;

public class DBTools {
    private static final String URL = "jdbc:mysql://localhost:3306/student" +
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";//jdbc��Э�� mysql��Э�� ������//localhost:3307/student
    private static final String USERNAME = "root";
    private static final String PASSWORD = "5978836.wo";
    private static DBTools db;//
    private Connection track;//�ӿ�connection������track
    private PreparedStatement order;//����prepare statement������sql��䵽���ݿ�ִ�����ݲ���
    // �ýӿ�����Ԥ������� ͬһ������Զ��ʹ�� sql�����Դ����� �������ݿ�����ʱ������
    // ���ƵĽӿڻ���statement����statement�������ִ�е��Ǿ�̬��sql��� û�в�����sql���ͨ������statementִ�� resultset�����Ķ��� һ��ֻ����һ��

    //private DBTools() {}

    public static DBTools getDBTools() {
        if (db == null) {
            db = new DBTools();
        }
        return db;
    }

    // ����SQL�������ܷ�����
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
                    db.track.close();//�ر����ݿ�
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Connection getConnection() {//�������ݿ�
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//Class.forName()��������
            if (track == null || track.isClosed()) {
                this.track = DriverManager.getConnection(URL, USERNAME, PASSWORD);//connection�ӿڶ���track����
                // DriverManager.getConnection()�����������ݿ⣬ͨ����ַ�Լ��û��˺�
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.err.println("Not Find the Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return track;
    }

    // ���ڲ�ѯ
    public ResultSet query(String sql){
        try {
            order = getConnection().prepareStatement(sql);
            return order.executeQuery();//executeQuery����ִ��sql���
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
       return null;
    }
    // ������Ϣ��Ӻ��޸�
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
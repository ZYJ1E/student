package test;
import sql.DBTools;
import sql.GroupScoreDB;
import windows.LoginWindow;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/*
AWT是Abstract Windowing Toolkit 的缩写 意思是：Java抽象窗口工具
java中EventQueue的作用_Swing之EventQueue简介:https://blog.csdn.net/weixin_30677191/article/details/114961331
 */
public class StudentFrame {
    public static int EXIT_WITHOUT_IO_ACCESS = 900;
    public static int EXIT_WITHOUT_LOGIN = 1000;
    public static int EXIT_CANNOT_CONNECT_WITH_SQL = 1100;


    public static void main(String[] args) {
        new Thread(() -> {
            GroupScoreDB groupScoreTools = GroupScoreDB.getGroupScoreDBdb();
            System.out.println("Finish Loading groupScoreTools");
        }).start();

        try {
            checkDirs();
        } catch (IOException exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "程序无读写权限，无法使用",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(EXIT_WITHOUT_IO_ACCESS);
        }
        EventQueue.invokeLater(() -> {
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            if (loginWindow.getDefaultCloseOperation()
                    ==WindowConstants.DISPOSE_ON_CLOSE) {
        System.out.println("Login Successfully");
        EventQueue.invokeLater(() -> {
            var mainWindow = new MainWindow(loginWindow.getUserName());
            mainWindow.setLocationRelativeTo(null);
        });
            }
            }
        });
        });
    }
    private static void checkDirs() throws IOException {
        final var file = new File(".config");
        if (!file.exists()) {
            final var userData = new File(".config/userdata.info");
            if (!file.mkdir() || !userData.createNewFile()) {
                throw new IOException(String.format("创建失败 %s %s",
                        file.getPath(), file.getName()));
            }
        }

        if (!DBTools.isAvailable()) {
            JOptionPane.showMessageDialog(null,
                    "无法连接到SQL服务器，程序无法进行", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("Can't Connect to SQL serve");
            System.exit(EXIT_CANNOT_CONNECT_WITH_SQL);
        } else {
            System.out.println("SQL serve is available");
        }
    }
}
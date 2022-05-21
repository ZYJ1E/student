package windows;

import security.AES;
import security.MD5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import component.bg_picture;

/*
swing介绍:https://blog.csdn.net/xietansheng/article/details/72814492
java final关键字:https://www.runoob.com/java/java-modifier-types.html
box.add(projectNamePanel);
box.add(enterUserName);
box.add(enterPassWord);
box.add(loginRegister);
 */
public final class LoginWindow extends JFrame {
    public final static int DEFAULT_WIDTH=400;//构建类常量
    public final static int DEFAULT_HEIGHT=400;
    public static final Dimension BOX_SIZE=new Dimension(150,25);
    public final LoginWindow myself=this;
    private String userName=null;
    public LoginWindow() {
        Image image=new ImageIcon("src/component/ca2ea724-787a-4d5f-9a82-558daf08c985.jpg").getImage();
        JPanel bg_panel = new bg_picture(image);
        setLayout(new BorderLayout());
        Box box= Box.createVerticalBox();
        LoginBar loginMenuBar= new LoginBar();
        JLabel projectNameLabel=new JLabel("学生成绩管理系统");
        JPanel projectNamePanel=new JPanel();
        projectNamePanel.add(projectNameLabel);
        JLabel userName1=new JLabel("账号:");
        JTextField userNameText = new JTextField();
        JPanel enterUserName=new JPanel();
        userNameText.setPreferredSize(BOX_SIZE);
        enterUserName.add(userName1);
        enterUserName.add(userNameText);
        JLabel passWord1=new JLabel("密码:");
        JPasswordField passWordText = new JPasswordField();
        JPanel enterPassWord=new JPanel();
        passWordText.setPreferredSize(BOX_SIZE);
        enterPassWord.add(passWord1);
        enterPassWord.add(passWordText);
        JButton loginButton=new JButton("登录");
        JButton registerButton=new JButton("注册");
        JPanel loginRegister=new JPanel();
        loginRegister.add(registerButton);
        loginRegister.add(loginButton);
        box.add(projectNamePanel);
        box.add(enterUserName);
        box.add(enterPassWord);
        box.add(loginRegister);
        bg_panel.setLayout(null);
        bg_panel.add(box);
        box.setBounds(100,70,200,170);
        add(loginMenuBar,BorderLayout.NORTH);
        add(bg_panel,BorderLayout.CENTER);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("欢迎使用学生成绩管理系统");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowListener whenClosing = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                var ensure_exit
                        = JOptionPane.showConfirmDialog(null,
                        "是否退出？可能会丢失未保存的数据!",
                        "NOTICE!"
                        , JOptionPane.YES_NO_OPTION);
                if (ensure_exit == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        };
        addWindowListener(whenClosing);
        registerButton.addActionListener(event->EventQueue.invokeLater(() ->{
            setEnabled(false);
            EventQueue.invokeLater(() -> {
                JDialog registerDialog = new JDialog(new RegisterDialog(myself));
                registerDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                registerDialog.setLocationRelativeTo(passWordText);
            });
        }));
        loginButton.addActionListener(event -> {
            final File userData = new File("admin/admin.info");
            final boolean isEmpty = userNameText.getText().length() == 0 ||
                    passWordText.getPassword().length == 0;

            if (isEmpty) {
                JOptionPane.showMessageDialog(null,
                        "用户名或密码为空", "ERROR!",
                        JOptionPane.ERROR_MESSAGE
                );
                System.err.println("Empty Username or Password when Logging");
            } else if (!userData.exists()) {
                JOptionPane.showMessageDialog(null,
                        "找不到账户信息，请先注册账号", "找不到用户信息",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                try (BufferedReader dataReader =
                             new BufferedReader(new InputStreamReader(new FileInputStream(userData)))) {
                    String userName = MD5.encode(userNameText.getText());
                    final String passWord = MD5.encode(passWordText.getPassword());

                    String fileInfo;
                    long lines = 0;
                    while ((fileInfo = dataReader.readLine()) != null) {
                        ++lines;
                        String[] getDataText = AES.decode(fileInfo).split("&");

                        // 数据格式不正确
                        if (getDataText.length != 2) {
                            System.err.printf("Data Error: %s %d\n", fileInfo, lines);
                            dispose();
                        }

                        assert userName != null;
                        final boolean isMatch = userName.equals(getDataText[0]) &&
                                passWord.equals(getDataText[1]);
                        if (isMatch) {
                            this.userName = userNameText.getText();
                            JOptionPane.showMessageDialog(null,
                                    "登陆成功", "SUCCESS!",
                                    JOptionPane.INFORMATION_MESSAGE);
                            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            dispose();
                            throw new Exception();
                        }
                    }
                    if (getDefaultCloseOperation() != DISPOSE_ON_CLOSE) {
                        JOptionPane.showMessageDialog(null,
                                "账户或密码错误", "Error",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null,
                            "没有用户文件的读写权限，本程序已无法正常工作", "ERROR!",
                            JOptionPane.ERROR_MESSAGE);
                    exception.printStackTrace();
                } catch (Exception e) {
                    // 完成登录
                    System.out.println("登陆成功");
                }
            }
        });
    }
    public String getUserName() {
        return this.userName;
    }
    private class LoginBar extends JMenuBar {
        public LoginBar() {
            JMenu aboutMenu = new JMenu("关于");
            JMenu optionMenu = new JMenu("选项");
            JMenuItem authorInfo = new JMenuItem("作者");
            JMenuItem softwareInfo = new JMenuItem("软件信息");
            JMenuItem exitItem = new JMenuItem("直接退出");
            authorInfo.addActionListener(e -> JOptionPane.showMessageDialog(null,
                    "信管11班：李广军\n信管10班：张英杰\n信管10班：吴朝辉\n"
                    , "AUTHORS!", JOptionPane.INFORMATION_MESSAGE));
            softwareInfo.addActionListener(e -> JOptionPane.showMessageDialog(null,
                    "Java程序设计与实践大作业\n学生信息管理系统"
                    , "SYSTEM!", JOptionPane.INFORMATION_MESSAGE));
            exitItem.addActionListener(this::actionPerformed);
            aboutMenu.add(authorInfo);
            aboutMenu.add(softwareInfo);
            optionMenu.add(exitItem);
            add(optionMenu);
            add(aboutMenu);
            setResizable(false);
            setVisible(true);
        }
        /*
dispatchEvent:https://www.cnblogs.com/baobaoandxiangxiang/archive/2006/10/06/522238.html
         */
        private void actionPerformed(ActionEvent e) {
            System.out.println("testing");
            myself.dispatchEvent(new WindowEvent(myself, WindowEvent.WINDOW_CLOSING));
        }
    }
}

package windows;


import component.TimeClock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import sql.GroupScoreDB;


public class MainWindow extends JFrame {
    private final MainWindow myself =this;
    public MainWindow(String username){
        JPanel topPanel=new JPanel();
        JPanel projectPanel=new JPanel();
        JLabel projectName=new JLabel("信管学生成绩管理系统");
        projectPanel.add(projectName);
        JLabel welcomeMsg=new JLabel(String.format("您好，%s",username));
        topPanel.add(welcomeMsg);
        topPanel.add(new TimeClock());
        JPanel centerPanel=new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,50));
        JButton managerButton=new JButton("成绩信息管理");
        JButton gradeAnalyzeButton=new JButton("成绩综合分析");
        //JButton dataGenerateButton=new JButton("测试数据生成");
        JButton exitButton=new JButton("退出管理系统");
        centerPanel.add(managerButton);
        centerPanel.add(gradeAnalyzeButton);
        centerPanel.add(exitButton);
        //centerPanel.add(dataGenerateButton);

        add(projectPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);
        add(topPanel,BorderLayout.PAGE_END);
        addWindowListener(new WindowAdapter() {
            //@Override
            public void WindowClosing(WindowEvent e){
                final int ensureExit=JOptionPane.showConfirmDialog(
                        null,"是否退出","是",
                        JOptionPane.YES_NO_OPTION
                );
                if (ensureExit==0){
                    System.out.println("MainWindow exit");
                    System.exit(0);
                }
            }
        });
        exitButton.addActionListener(event->
               // myself.dispatchEvent(new WindowEvent(myself,WindowEvent.WINDOW_CLOSING))
                System.exit(0));
        managerButton.addActionListener(event->{
            System.out.println("select Manager");
            var manager = new GradeManagerWindow();
            manager.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Loading all students info");
                    new Thread(() -> {
                        try {
                            GroupScoreDB.getGroupScoreDBdb().listStu();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    }).start();
                    System.out.println("Finish loading all students info");
                }
            });
        });
        gradeAnalyzeButton.addActionListener(event->{
            System.out.println("select Analyzer");
            var analyzer = new GradeAnalyzeWindow();
        });
        //dataGenerateButton.addActionListener(event->{

        //});
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("信管学生管理系统");
        setResizable(false);
        setVisible(true);
    }

}

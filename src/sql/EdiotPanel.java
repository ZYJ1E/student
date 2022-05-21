package sql;

import javax.swing.*;
import java.awt.*;

public class EdiotPanel extends JFrame {
    //数据输入
    JPanel idPanel = new JPanel(new GridLayout(1, 2));
    JPanel namePanel = new JPanel(new GridLayout(1, 2));
    JPanel sexualPanel = new JPanel(new GridLayout(1, 2));
    JPanel birthdayPanel = new JPanel(new GridLayout(1, 2));
    JPanel classPanel = new JPanel(new GridLayout(1, 2));
    JPanel chinPanel = new JPanel(new GridLayout(1, 2));
    JPanel mathPanel = new JPanel(new GridLayout(1, 2));
    JPanel engPanel = new JPanel(new GridLayout(1, 2));
    JPanel phyPanel = new JPanel(new GridLayout(1, 2));
    JPanel chemPanel = new JPanel(new GridLayout(1, 2));
    JPanel bioPanel = new JPanel(new GridLayout(1, 2));
    JPanel polPanel = new JPanel(new GridLayout(1, 2));
    JPanel hisPanel = new JPanel(new GridLayout(1, 2));
    JPanel geoPanel = new JPanel(new GridLayout(1, 2));

    JLabel idLabel = new JLabel("学号");
    JLabel nameLabel = new JLabel("姓名");
    JLabel sexualLabel = new JLabel("性别");
    JLabel birthdayLabel = new JLabel("生日");
    JLabel classLabel = new JLabel("班级");
    JLabel chinLabel = new JLabel("语文");
    JLabel mathLabel = new JLabel("数学");
    JLabel engLabel = new JLabel("英语");
    JLabel phyLabel = new JLabel("物理");
    JLabel chemLabel = new JLabel("化学");
    JLabel bioLabel = new JLabel("生物");
    JLabel polLabel = new JLabel("政治");
    JLabel hisLabel = new JLabel("历史");
    JLabel geoLabel = new JLabel("地理");

    public EdiotPanel(String[] data,JTextField[] textFields){
        setLayout(new GridLayout(8,2));

        idPanel.add(idLabel);
        idPanel.add(textFields[0]);
        namePanel.add(nameLabel);
        namePanel.add(textFields[1]);
        sexualPanel.add(sexualLabel);
        sexualPanel.add(textFields[2]);
        birthdayPanel.add(birthdayLabel);
        birthdayPanel.add(textFields[3]);
        classPanel.add(classLabel);
        classPanel.add(textFields[4]);
        chinPanel.add(chinLabel);
        chinPanel.add(textFields[5]);
        mathPanel.add(mathLabel);
        mathPanel.add(textFields[6]);
        engPanel.add(engLabel);
        engPanel.add(textFields[7]);
        phyPanel.add(phyLabel);
        phyPanel.add(textFields[8]);
        chemPanel.add(chemLabel);
        chemPanel.add(textFields[9]);
        bioPanel.add(bioLabel);
        bioPanel.add(textFields[10]);
        polPanel.add(polLabel);
        polPanel.add(textFields[11]);
        hisPanel.add(hisLabel);
        hisPanel.add(textFields[12]);
        geoPanel.add(geoLabel);
        geoPanel.add(textFields[13]);

        add(idPanel);
        add(namePanel);
        add(sexualPanel);
        add(birthdayPanel);
        add(classPanel);
        add(chinPanel);
        add(mathPanel);
        add(engPanel);
        add(phyPanel);
        add(chemPanel);
        add(bioPanel);
        add(polPanel);
        add(hisPanel);
        add(geoPanel);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}

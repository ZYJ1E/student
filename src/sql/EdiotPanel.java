package sql;

import javax.swing.*;
import java.awt.*;

public class EdiotPanel extends JFrame {
    //��������
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

    JLabel idLabel = new JLabel("ѧ��");
    JLabel nameLabel = new JLabel("����");
    JLabel sexualLabel = new JLabel("�Ա�");
    JLabel birthdayLabel = new JLabel("����");
    JLabel classLabel = new JLabel("�༶");
    JLabel chinLabel = new JLabel("����");
    JLabel mathLabel = new JLabel("��ѧ");
    JLabel engLabel = new JLabel("Ӣ��");
    JLabel phyLabel = new JLabel("����");
    JLabel chemLabel = new JLabel("��ѧ");
    JLabel bioLabel = new JLabel("����");
    JLabel polLabel = new JLabel("����");
    JLabel hisLabel = new JLabel("��ʷ");
    JLabel geoLabel = new JLabel("����");

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

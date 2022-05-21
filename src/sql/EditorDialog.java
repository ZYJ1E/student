package sql;

import test.EventLogger;
import test.Student;
import component.datechoose;
import javax.swing.*;
import java.awt.*;


public class EditorDialog extends JDialog {
    private static final EventLogger EVENT_LOGGER = EventLogger.getEventLogger();
    private static final StuDB STU_TOOL = new StuDB();
    private final String[] res = null;
    private JTable table = null;
    private JButton revokeButton = null;
    private String classID = null;

    final String[] className = STU_TOOL.listClass();

    JPanel buttonPanel1 = new JPanel(new GridLayout(1, 1));
    JPanel buttonPanel2 = new JPanel(new GridLayout(1, 1));
    JButton saveButton = new JButton("����");
    JButton cancelButton = new JButton("ȡ��");
    String sexual[]={"��","Ů"};
    JComboBox sex=new JComboBox(sexual);//Ĭ���Ա�Ϊ��
    JTextField[] textFields = new JTextField[14];

    public EditorDialog(int row, String[] data) {//�༭��ť�󶨵ĺ���
        buttonPanel1.add(saveButton);
        buttonPanel2.add(cancelButton);

        //�ı�����
        for (int i = 0; i <14; i++) {
            textFields[i] = new editTextField(data[i]);
            //System.out.println(editorTable.getSelectedRow());
            textFields[i].setText(STU_TOOL.findStu("", StuDB.FIND_NAME, className[0])[row][i]);
            // textFields[i].setText("");
        }
        textFields[0].setEnabled(false);

        //�����±༭���
        EdiotPanel Ediotpanel=new EdiotPanel(data,textFields);
        Ediotpanel.setTitle("��Ϣ�༭");
        Ediotpanel.add(buttonPanel1);
        Ediotpanel.add(buttonPanel2);

        cancelButton.addActionListener(event ->{
        for (int i = 0; i <14; i++) {
            textFields[i].setText("");
        }
                Ediotpanel.dispose();}
        );
        saveButton.addActionListener(event -> {
            Ediotpanel.setVisible(false);
            String[] upData = new String[data.length];
            System.out.println(data.length);
            for (int i = 0; i < data.length - 1; i++) {
                upData[i] = textFields[i].getText();
                table.setValueAt(textFields[i].getText(), row, i);
            }
            final var newStu = Student.getStudent(upData);
            table.setValueAt(newStu.getSumScore().toString(), row, 14);

            STU_TOOL.updateStu(newStu);
            EVENT_LOGGER.update(Student.getStudent(data));
            classID = upData[4];

            JOptionPane.showMessageDialog(null,
                    "�޸ĳɹ�", "SUCCESS",
                    JOptionPane.INFORMATION_MESSAGE);
            revokeButton.setEnabled(true);
            STU_TOOL.closeAll();
            dispose();
        });
    }

    public EditorDialog(String[] data) {//���Ӱ�ť�󶨵ĺ���
        buttonPanel1.add(saveButton);
        buttonPanel2.add(cancelButton);

        for (int i = 0; i <14 ; i++) {
            textFields[i] = new editTextField(data[14]);
            textFields[i].setText("");
        }
        //�ı����ʽ�༭
        datechoose birthday = datechoose.getInstance();
        birthday.register(textFields[3]);
        textFields[2].setText(sex.getSelectedItem().toString());
        textFields[0].setText("ѧ���Զ�����");
        textFields[0].setEditable(false);

        //�������Ӱ�ť���
        EdiotPanel Addpanel=new EdiotPanel(data,textFields);
        Addpanel.setTitle("��Ϣ����");
        Addpanel.add(buttonPanel1);
        Addpanel.add(buttonPanel2);

        //�¼���Ӧ��ť
        cancelButton.addActionListener(event -> Addpanel.dispose());
        saveButton.addActionListener(event -> {
            Addpanel.setVisible(false);
            var upData = new String[data.length];
            upData[0] = "111";
            for (int i = 1; i < data.length - 1; i++) {
                upData[i] = textFields[i].getText();
            }
            final Student newStu = Student.getStudent(upData);
            newStu.setSumScore();
            Long id = STU_TOOL.addStu(newStu);
            EVENT_LOGGER.add(id.toString());
            JOptionPane.showMessageDialog(null, "��ӳɹ�", "SUCCESS��", JOptionPane.INFORMATION_MESSAGE);
            STU_TOOL.closeAll();
            dispose();
        });
    }

    public void setTable(JTable table) {
        this.table = table;
    }
    public void setRevokeButton(JButton revokeButton) {
        this.revokeButton = revokeButton;
    }
}

class editTextField extends JTextField {
    public editTextField(String s) {
        setText(s);
        setColumns(12);
        setVisible(true);
    }
}
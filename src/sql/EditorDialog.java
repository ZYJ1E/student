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
    JButton saveButton = new JButton("保存");
    JButton cancelButton = new JButton("取消");
    String sexual[]={"男","女"};
    JComboBox sex=new JComboBox(sexual);//默认性别为男
    JTextField[] textFields = new JTextField[14];

    public EditorDialog(int row, String[] data) {//编辑按钮绑定的函数
        buttonPanel1.add(saveButton);
        buttonPanel2.add(cancelButton);

        //文本内容
        for (int i = 0; i <14; i++) {
            textFields[i] = new editTextField(data[i]);
            //System.out.println(editorTable.getSelectedRow());
            textFields[i].setText(STU_TOOL.findStu("", StuDB.FIND_NAME, className[0])[row][i]);
            // textFields[i].setText("");
        }
        textFields[0].setEnabled(false);

        //导入新编辑面板
        EdiotPanel Ediotpanel=new EdiotPanel(data,textFields);
        Ediotpanel.setTitle("信息编辑");
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
                    "修改成功", "SUCCESS",
                    JOptionPane.INFORMATION_MESSAGE);
            revokeButton.setEnabled(true);
            STU_TOOL.closeAll();
            dispose();
        });
    }

    public EditorDialog(String[] data) {//增加按钮绑定的函数
        buttonPanel1.add(saveButton);
        buttonPanel2.add(cancelButton);

        for (int i = 0; i <14 ; i++) {
            textFields[i] = new editTextField(data[14]);
            textFields[i].setText("");
        }
        //文本框格式编辑
        datechoose birthday = datechoose.getInstance();
        birthday.register(textFields[3]);
        textFields[2].setText(sex.getSelectedItem().toString());
        textFields[0].setText("学号自动分配");
        textFields[0].setEditable(false);

        //增加增加按钮面板
        EdiotPanel Addpanel=new EdiotPanel(data,textFields);
        Addpanel.setTitle("信息增加");
        Addpanel.add(buttonPanel1);
        Addpanel.add(buttonPanel2);

        //事件反应按钮
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
            JOptionPane.showMessageDialog(null, "添加成功", "SUCCESS！", JOptionPane.INFORMATION_MESSAGE);
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
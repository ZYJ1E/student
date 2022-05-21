package windows;

import component.ClassComBox;
import execl.ExcelExport;
import sql.EditorDialog;
import test.EventLogger;
import sql.StuDB;
import test.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class GradeManagerWindow  extends JFrame {
    private static final StuDB STU_TOOLS=new StuDB();
    private static final EventLogger EVENT_LOGGER=EventLogger.getEventLogger();
    private final GradeManagerWindow myself=this;
    private final String[] TITLE={
            "学号", "姓名","性别","出生年月","班级","语文","数学",
            "英语", "物理","化学","生物","政治","历史","地理"
            ,"总分"
    };
    public GradeManagerWindow(){
        JPanel sfPanel=new JPanel();
        JPanel searchPanel=new JPanel();
        JPanel functionPanel=new JPanel();
        JPanel dataPanel=new JPanel();
        JPanel selectClassPanel=new JPanel();
        ClassComBox selectClassRange=new ClassComBox();//选择班级下拉框
        JPanel changePagePanel=new JPanel(new FlowLayout());

        searchPanel.setLayout(new GridLayout(1,2));
        selectClassPanel.setLayout(new GridLayout(1,3));
        functionPanel.setLayout(new GridLayout(1,6));
        JTextField findTextFiled=new JTextField(10);
        JButton findButton=new JButton("查找");
        JButton addButton=new JButton("增加");
        JButton removeButton=new JButton("删除");
        JButton editButton=new JButton("编辑");
        JButton revokeButton=new JButton("撤销");
        JButton exportButton=new JButton("导出");
        JPanel  selectPanel=new JPanel();
        selectPanel.setBorder(BorderFactory.createTitledBorder("请选择查找内容"));

        JRadioButton idButton=new JRadioButton("学号");
        JRadioButton nameButton=new JRadioButton("姓名");
        idButton.setSelected(true);
        nameButton.setSelected(true);

        ButtonGroup group=new ButtonGroup();
        group.add(idButton);
        group.add(nameButton);
        selectPanel.add(nameButton);
        selectPanel.add(idButton);

        selectClassPanel.add(selectPanel);
        selectClassPanel.add(selectClassRange);
        selectClassPanel.add(findButton);
        searchPanel.add(findTextFiled);
        searchPanel.add(selectClassPanel);
        functionPanel.add(addButton);
        functionPanel.add(removeButton);
        functionPanel.add(editButton);
        functionPanel.add(revokeButton);
        functionPanel.add(exportButton);

        sfPanel.setLayout(new GridLayout(2, 1));
        sfPanel.add(searchPanel);
        sfPanel.add(functionPanel);

        dataPanel.setLayout(new GridLayout(1, 1));
        String[][] data = STU_TOOLS.findStu("", StuDB.FIND_NAME, (String) Objects.requireNonNull(selectClassRange.getSelectedItem()));

        DefaultTableModel editorTableMode = new DefaultTableModel(data, TITLE) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        var editorTable = new JTable(editorTableMode);
        var editorLayout = new DefaultTableCellRenderer();
        editorLayout.setHorizontalAlignment(JLabel.CENTER);
        editorTable.setRowHeight(27);
        editorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        editorTable.setDefaultRenderer(Object.class, editorLayout);

        editorTable.getSelectionModel().addListSelectionListener(e -> {
            exportButton.setEnabled(true);
            editButton.setEnabled(true);
            removeButton.setEnabled(true);
        });
        updateEditor(editorTable, STU_TOOLS.findStu("", StuDB.FIND_NAME, (String) selectClassRange.getSelectedItem()));
        var editorScroll = new JScrollPane(editorTable);
        dataPanel.add(editorScroll);


        add(sfPanel, BorderLayout.NORTH);
        add(dataPanel, BorderLayout.CENTER);
        add(changePagePanel, BorderLayout.SOUTH);




        editorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    editButton.doClick();
                }
            }
        });

        removeButton.setEnabled(false);
        revokeButton.setEnabled(false);
        editButton.setEnabled(false);


        removeButton.addActionListener(event -> {
            final var selectedRow = editorTable.getSelectedRow();
            if (selectedRow != -1) {
                var stuData = new String[15];
                for (int i = 0; i < 15; i++) {
                    stuData[i] = (String) editorTable.getValueAt(selectedRow, i);
                }
                final var stu = Student.getStudent(stuData);
                STU_TOOLS.delStu(stuData[0]);
                EVENT_LOGGER.remove(stu);


                editorTableMode.removeRow(selectedRow);
            }
            System.out.println("Delete Row:" + editorTableMode.getRowCount());
            JOptionPane.showMessageDialog(null,
                    "删除成功", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
            selectClassRange.update();
            editButton.setEnabled(false);
            removeButton.setEnabled(false);
            revokeButton.setEnabled(true);
        });


        addButton.addActionListener(event -> {

            Student newStudent = new Student();

            ((DefaultTableModel) editorTable.getModel()).addRow(newStudent.toStringArray());
            EventQueue.invokeLater(() -> {
                EditorDialog editWindow = new EditorDialog(newStudent.toStringArray());
                editWindow.setLocationRelativeTo(null);
                editWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        ((DefaultTableModel) editorTable.getModel()).removeRow(editorTable.getRowCount() - 1);
                    }
                });
            });
            //System.err.println("add fail!");
        });



        exportButton.addActionListener(event -> new Thread(() -> {
            final var export = new ExcelExport();
            if (!export.isExport()) {
                System.out.println("Cancel export");
            } else if (export.exportAll()) {
                JOptionPane.showMessageDialog(null,
                        "导出成功", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "导出失败", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start());

        findButton.addActionListener(event -> {
            System.out.println(editorTable.getSelectedRow());
            String object = findTextFiled.getText();
            String Class = (String) selectClassRange.getSelectedItem();
            String[][] findResult = null;
            System.out.printf("Find: %s\n", object);

            if (nameButton.isSelected()) {
                findResult = STU_TOOLS.findStu(object, StuDB.FIND_NAME, Class);
            } else if (idButton.isSelected()) {
                if ("".equals(object)) {
                    object = "0";
                }
                findResult = STU_TOOLS.findStu(object, StuDB.FIND_ID, Class);
            }

            assert findResult != null;
            final int findNumber = findResult.length;

            updateEditor(editorTable, findResult);
            //exportAllButton.setEnabled(false);
            removeButton.setEnabled(false);
            editButton.setEnabled(false);

            if (findNumber != 0) {
                editButton.setEnabled(true);

                final var msg = String.format("找到了%d条结果", findNumber);
                JOptionPane.showMessageDialog(null,
                        msg, "FINISH!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "未找到任何结果", "FINISH!",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        });

        revokeButton.addActionListener(event -> {
            var e = EVENT_LOGGER.takeOutLastEvent();

            switch ((int) e[0]) {
                case EventLogger.ADD: {
                    STU_TOOLS.delStu((String) e[1]);
                }
                break;
                case EventLogger.DEL: {
                    STU_TOOLS.addOldStu((Student) e[1]);
                }
                break;
                case EventLogger.UPDATE: {
                    STU_TOOLS.updateStu((Student) e[1]);
                }
                break;
                default: {
                }
            }
            if (EVENT_LOGGER.isEmpty()) {
                revokeButton.setEnabled(false);
            }
            editButton.setEnabled(false);
            findButton.doClick();
        });
        // 编辑按钮事件
        editButton.addActionListener(event -> {
            //System.out.println("editButton");
            int row = editorTable.getSelectedRow();
            System.out.println(row);
            var stuData = getEditData(row, editorTable);
            EventQueue.invokeLater(() -> {var editor = new EditorDialog(row, stuData);
                editor.setTable(editorTable);
                editor.setRevokeButton(revokeButton);
                editor.setLocationRelativeTo(null);
                editor.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        selectClassRange.update();
                    }
                });
            });
        });


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                final var ensureExit = JOptionPane.showConfirmDialog(null,
                        "你确认退出吗，未保存的内容将不会保存",
                        "WARING!",
                        JOptionPane.YES_NO_OPTION);
                if (ensureExit == JOptionPane.YES_OPTION) {
                    myself.setEnabled(true);
                    dispose();
                }
            }
        });

        // 设置窗口属性
        setSize(800, 800);
        setResizable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("信息成绩管理");
        setVisible(true);
    }

    public void updateEditor(JTable table, String[][] result) {
        new Thread(() -> ((DefaultTableModel) table.getModel()).setDataVector(result, TITLE)).start();
    }

    public String[] getEditData(int row, JTable table) {
        String[] res = new String[15];
        for (int i = 0; i < 15; i++) {
            res[i] = (String) table.getValueAt(row, i);
        }

        return res;
    }
}

class AnalyzeButton extends JButton implements ActionListener {
    public AnalyzeButton(String s) {
        super(s);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
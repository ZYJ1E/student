package windows;


import execl.ExcelExport;
import sql.StuDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class selectExportDialog extends JDialog {
    public static final int ID_ASC = 0;
    public static final int ID_DESC = 1;
    public static final int SUM_ASC = 2;
    public static final int SUM_DESC = 3;
    private static final StuDB STU_TOOLS = new StuDB();
    private static final String[] TITLE = {"学号", "姓名", "性别", "班级", "语文", "数学", "英语", "物理","化学","生物","政治","历史","地理","总成绩"};
    private JCheckBox[] checkBoxes;
    private JButton enterButton;
    private JButton cancelButton;
    private JPanel checkboxesPanel;
    private JPanel buttonPanel;
    private JPanel sortPanel;
    private JRadioButton[] sortType;
    private JLabel chosenClassName;

    private final String className;
    private JFrame fatherWindow;

    public selectExportDialog(String className) {
        this.className = className;
        setLayout(new GridLayout(4, 1));

        getClassName();
        setCheckBoxes();
        setButtons();
        setSortType();
        setChosenClassName();

        add(chosenClassName);
        add(checkboxesPanel);
        add(sortPanel);
        add(buttonPanel);

        setTitle("请选择要输出的内容");

        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fatherWindow.setVisible(true);
                fatherWindow.setEnabled(true);
                dispose();
            }
        });
        setVisible(true);
    }

    private void setCheckBoxes() {
        final var checkBoxesSize = TITLE.length;
        checkboxesPanel = new JPanel(new GridLayout(4, 4));
        checkBoxes = new JCheckBox[checkBoxesSize];

        for (int i = 0; i < checkBoxesSize; i++) {
            checkBoxes[i] = new JCheckBox(TITLE[i]);
            //checkBoxes[i].setFont(new Font("楷体", Font.PLAIN, 20));
            checkboxesPanel.add(checkBoxes[i]);
            if (i >= 4 && i < 14) {
                checkBoxes[i].addItemListener(this::actionPerformed);
            }
        }
        checkBoxes[0].setSelected(true);
        checkBoxes[0].setEnabled(false);
        checkBoxes[1].setSelected(true);
        checkBoxes[1].setEnabled(false);
        checkBoxes[3].setSelected(true);
        checkBoxes[3].setEnabled(false);
        checkBoxes[13].addActionListener(this::sumScoreIsSelected);
    }

    private void getClassName() {
        var className = new JComboBox<String>();
        className.setModel(new DefaultComboBoxModel<>(STU_TOOLS.listClass()));
    }

    private void setButtons() {
        buttonPanel = new JPanel(new FlowLayout());
        enterButton = new JButton("确定");
        cancelButton = new JButton("取消");
        buttonPanel.add(enterButton);
        buttonPanel.add(cancelButton);

        setButtonListeners();
    }

    private void setSortType() {
        final String[] title = {"学号升序排序", "学号降序排序", "总成绩升序排序", "总成绩降序排序"};
        sortPanel = new JPanel(new FlowLayout());
        sortType = new JRadioButton[4];
        var group = new ButtonGroup();
        for (int i = 0; i < sortType.length; i++) {
            sortType[i] = new JRadioButton(title[i]);
            group.add(sortType[i]);
            sortPanel.add(sortType[i]);
        }

        sortType[0].setSelected(true);
        sortType[2].setEnabled(false);
        sortType[3].setEnabled(false);
    }

    private void setButtonListeners() {
        cancelButton.addActionListener(event -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        enterButton.addActionListener(this::actionPerformed);
    }

    public void setFatherWindow(JFrame fatherWindow) {
        this.fatherWindow = fatherWindow;
    }

    private void setChosenClassName() {
        chosenClassName = new JLabel("当前选择的班级是:" + className, JLabel.CENTER);
        //chosenClassName.setFont(new Font("宋体", Font.ITALIC, 20));
    }

    private void actionPerformed(ItemEvent e) {
        if (checkBoxes[4].isSelected() &&
                checkBoxes[5].isSelected() &&
                checkBoxes[6].isSelected()&&
                checkBoxes[7].isSelected()&&
                checkBoxes[8].isSelected()&&
                checkBoxes[9].isSelected()&&
                checkBoxes[10].isSelected()&&
                checkBoxes[11].isSelected()&&
                checkBoxes[12].isSelected()) {
            checkBoxes[13].setSelected(true);
        }
        if (checkBoxes[13].isSelected()) {
            sortType[2].setEnabled(true);
            sortType[3].setEnabled(true);
        }
    }

    private void sumScoreIsSelected(ActionEvent e) {
        if (checkBoxes[13].isSelected()) {
            sortType[2].setEnabled(true);
            sortType[3].setEnabled(true);
        } else {
            sortType[2].setEnabled(false);
            sortType[3].setEnabled(false);
            if (!sortType[0].isSelected() && !sortType[1].isSelected()) {
                sortType[0].setSelected(true);
            }
        }
    }

    private void actionPerformed(ActionEvent event) {
         var selectItems = new ArrayList<String>();
        for (var box : checkBoxes) {
            if (box.isSelected()) {
                String subject = box.getText();
                switch (subject) {
                    case "语文":
                    case "数学":
                    case "地理":
                    case "英语":
                    case "物理":
                    case "化学":
                    case "生物":
                    case "政治":
                    case "历史":
                    case "总成绩": {
                        selectItems.add(subject);
                        selectItems.add(subject + "班级平均分");
                        selectItems.add(subject + "总平均分");
                    }
                    default:
                        selectItems.add(subject);
                }
            }
        }
        // 传入ExcelExport
        int type = -1;
        for (int i = 0; i < sortType.length; i++) {
            if (sortType[i].isSelected()) {
                type = i;
                break;
            }
        }

        var export = new ExcelExport();
        if (!export.isExport()) {
            System.out.println("Cancel export");
        } else if (export.exportSelected(selectItems, className, type)) {
            JOptionPane.showMessageDialog(null,
                    "导出成功", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.printf("Export %s %d Successful\n", Arrays.toString(selectItems.toArray()), type);
        } else {
            JOptionPane.showMessageDialog(null,
                    "导出失败", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.printf("Failed to Export %s %d Successful\n",
                    Arrays.toString(selectItems.toArray()), type);
        }
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}


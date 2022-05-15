package windows;


import component.ClassComBox;
import test.Student;
import sql.GroupScoreDB;
import sql.StuDB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;

import static java.awt.EventQueue.invokeLater;

/**
 *
 */
public class GradeAnalyzeWindow extends JFrame {
    private static final StuDB STU_TOOLS = new StuDB();
    private final GradeAnalyzeWindow myself = this;
    private final String[] TITLE = {
            "ѧ��", "����", "�Ա�", "��������", "�༶",
            "����", "���İ༶ƽ����", "����ȫУƽ����",
            "��ѧ", "��ѧ�༶ƽ����", "��ѧȫУƽ����",
            "Ӣ��", "Ӣ��༶ƽ����", "Ӣ��ȫУƽ����",
            "����", "����༶ƽ����", "����ȫУƽ����",
            "��ѧ", "��ѧ�༶ƽ����", "��ѧȫУƽ����",
            "����", "����༶ƽ����", "����ȫУƽ����",
            "����", "���ΰ༶ƽ����", "����ȫУƽ����",
            "��ʷ", "��ʷ�༶ƽ����", "��ʷȫУƽ����",
            "����", "����༶ƽ����", "����ȫУƽ����",
            "�ܳɼ�", "�༶�ܳɼ�ƽ����", "ȫУ�ܳɼ�ƽ����"
    };
    private HashMap classScore;
    private final GroupScoreDB groupScoreTools = GroupScoreDB.getGroupScoreDBdb();

    private final DefaultTableModel editorTableMode;

    public GradeAnalyzeWindow() {
        // �������
        var sfPanel = new JPanel();
        var searchPanel = new JPanel();
        var functionPanel = new JPanel();
        var dataPanel = new JPanel();
        var selectClassPanel = new JPanel();
        var selectClassRange = new ClassComBox();

        // ���Һ͹������
        searchPanel.setLayout(new GridLayout(1, 2));
        selectClassPanel.setLayout(new GridLayout(1, 3));
        functionPanel.setLayout(new GridLayout(1, 6));
        var findTextFiled = new JTextField();
        var findButton = new JButton("����");
        var analyzeButton = new AnalyzeButton("���˳ɼ�����");
        var classAnalyzeButton = new AnalyzeButton("���˳ɼ�����");
        var exportGradeButton = new JButton("�ɼ���Ϣ����");

        var selectPanel = new JPanel();
        // selectPanel ���������
        var idButton = new JRadioButton("ѧ��");
        idButton.setFont(new Font("����", Font.PLAIN, 20));

        var nameButton = new JRadioButton("����");
        nameButton.setFont(new Font("����", Font.PLAIN, 20));
        nameButton.setSelected(true);

        var group = new ButtonGroup();
        group.add(idButton);
        group.add(nameButton);
        selectPanel.add(nameButton);
        selectPanel.add(idButton);

        // �ؼ����
        selectClassPanel.add(selectPanel);
        selectClassPanel.add(selectClassRange);
        selectClassPanel.add(findButton);
        searchPanel.add(findTextFiled);
        searchPanel.add(selectClassPanel);
        functionPanel.add(analyzeButton);
        functionPanel.add(classAnalyzeButton);
        functionPanel.add(exportGradeButton);

        // ���Һ͹����������
        sfPanel.setLayout(new GridLayout(2, 1));
        sfPanel.add(searchPanel);
        sfPanel.add(functionPanel);

        // ��Ϣ���
        dataPanel.setLayout(new GridLayout(1, 1));
        String[][] data = {{"���ڼ���������"}, {"�����ĵȺ�"}, {"��Ҫ������������"}};

        // �༭��
        editorTableMode = new DefaultTableModel(data, new String[]{"��ʾ"}) {
            @Override // �޷�ֱ�ӱ༭������ͨ��˫����༭��ť��ʵ��
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        var editorTable = new JTable(editorTableMode);
        var editorLayout = new DefaultTableCellRenderer();
        editorLayout.setHorizontalAlignment(JLabel.CENTER);
        editorTable.setRowHeight(27); // �����и�
        editorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // ����ֻѡ��һ��
        editorTable.setDefaultRenderer(Object.class, editorLayout);
        // ����ѡ��һ�к�����ʹ��ĳЩ����
        editorTable.getSelectionModel().addListSelectionListener(e -> {
            analyzeButton.setEnabled(true);
            classAnalyzeButton.setEnabled(true);
        });
        new Thread(() -> {
            try {
                updateEditor(groupScoreTools.listStu());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }).start();


        var editorScroll = new JScrollPane(editorTable);
        dataPanel.add(editorScroll);

        // ����������
        add(sfPanel, BorderLayout.NORTH);
        add(dataPanel, BorderLayout.CENTER);

        // ����¼�����������

        // �༭�����¼�
        editorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    classAnalyzeButton.doClick();
                }
            }
        });
        // ��Ӱ༶ѡ���������¼�
        // ��ť��������
        analyzeButton.setEnabled(false);
        classAnalyzeButton.setEnabled(false);
        exportGradeButton.setEnabled(false);

        // ��ȡ����
        invokeLater(new Thread(() -> {
            classScore = groupScoreTools.getScores();
            exportGradeButton.setEnabled(true);
        })::start);

        // ������ť�¼�
        analyzeButton.addActionListener(event -> {

            final int selectLine = editorTable.getSelectedRow();
            var stuData = new String[9];

            for (int i = 0; i < 6; i++) {
                stuData[i] = (String) editorTable.getValueAt(selectLine, i);
            }
            stuData[6] = (String) editorTable.getValueAt(selectLine, 8);
            stuData[7] = (String) editorTable.getValueAt(selectLine, 11);
            stuData[8] = (String) editorTable.getValueAt(selectLine, 14);

            invokeLater(() -> {
                var chart = new AnalyzeFrame(Student.getStudent(stuData), AnalyzeFrame.PIE_CHART);
                chart.setLocationRelativeTo(myself);
            });
        });
        classAnalyzeButton.addActionListener(event -> {
            final int selectLine = editorTable.getSelectedRow();
            var stuData = new String[9];

            for (int i = 0; i < 6; i++) {
                stuData[i] = (String) editorTable.getValueAt(selectLine, i);
            }
            stuData[6] = (String) editorTable.getValueAt(selectLine, 8);
            stuData[7] = (String) editorTable.getValueAt(selectLine, 11);
            stuData[8] = (String) editorTable.getValueAt(selectLine, 14);

            invokeLater(() -> {
                var chart = new AnalyzeFrame(Student.getStudent(stuData), AnalyzeFrame.BAR_CHART);
                chart.setLocationRelativeTo(myself);
            });
        });
        // ���Ұ�ť�¼�
        findButton.addActionListener(event -> {
            System.out.println(editorTable.getSelectedRow());
            String object = findTextFiled.getText();
            String Class = (String) selectClassRange.getSelectedItem();
            String[][] findResult = null;
            System.out.printf("Find: %s\n", object);

            if (nameButton.isSelected()) {
                assert Class != null;
                try {
                    findResult = groupScoreTools.findStu(object, StuDB.FIND_NAME, Class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (idButton.isSelected()) {
                assert Class != null;
                try {
                    findResult = groupScoreTools.findStu(object, StuDB.FIND_ID, Class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            assert findResult != null;
            final int findNumber = findResult.length;
            // ˢ��table
            updateEditor(findResult);
            analyzeButton.setEnabled(false);
            classAnalyzeButton.setEnabled(false);

            if (findNumber != 0) {

                final var msg = String.format("�ҵ���%d�����", findNumber);
                JOptionPane.showMessageDialog(null,
                        msg, "FINISH",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "δ�ҵ��κν��", "FINISH",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        });

        // ������ť�¼�
        exportGradeButton.addActionListener(event -> invokeLater(() -> {
            this.setEnabled(false);
            var frame = new selectExportDialog((String) selectClassRange.getSelectedItem());
            frame.setLocationRelativeTo(null);
            frame.setFatherWindow(this);
        }));

        // �˳���ť�¼�
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                final var ensureExit = JOptionPane.showConfirmDialog(null,
                        "��ȷ���˳�ϵͳ��",
                        "WARING",
                        JOptionPane.YES_NO_OPTION);
                if (ensureExit == JOptionPane.YES_OPTION) {
                    myself.setEnabled(true);
                    STU_TOOLS.closeAll();
                    dispose();
                }
            }
        });

        // ���ô�������
        setSize(1400, 800);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("�ɼ��ۺϷ���");
        setVisible(true);
    }

    private void updateEditor(String[][] result) {
        editorTableMode.setDataVector(result, myself.TITLE);
    }
}
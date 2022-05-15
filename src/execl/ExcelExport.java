package execl;


import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import test.classscore;
import sql.ClassScoreDB;
import sql.GroupScoreDB;
import sql.StuDB;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;


public class ExcelExport {
    public static final String[] TITLE = {"ѧ��", "����", "����", "�Ա�", "�༶",
            "����", "���İ༶ƽ���ɼ�", "����ȫУƽ���ɼ�", "��ѧ", "��ѧ�༶ƽ���ɼ�", "��ѧȫУƽ���ɼ�", "Ӣ��", "Ӣ��༶ƽ���ɼ�", "Ӣ��ȫУƽ���ɼ�",
            "����", "����༶ƽ���ɼ�", "����ȫУƽ���ɼ�","��ѧ", "��ѧ�༶ƽ���ɼ�", "��ѧȫУƽ���ɼ�","����", "����༶ƽ���ɼ�", "����ȫУƽ���ɼ�",
            "����", "���ΰ༶ƽ���ɼ�", "����ȫУƽ���ɼ�","��ʷ", "��ʷ�༶ƽ���ɼ�", "��ʷȫУƽ���ɼ�","����", "����༶ƽ���ɼ�", "����ȫУƽ���ɼ�",
            "�ܳɼ�", "�༶�ܳɼ�", "ȫУ�ܳɼ�"};
    private static final StuDB STU_TOOL = new StuDB();
    private static final GroupScoreDB GROUP_SCORE_TOOLS = GroupScoreDB.getGroupScoreDBdb();
    private static final String all = "stu";
    private String filePath;
    private final int result;

    public ExcelExport() {
        JFileChooser chooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        chooser.setCurrentDirectory(fsv.getHomeDirectory());
        chooser.setDialogTitle("��ѡ�񵼳�λ��");
        chooser.setApproveButtonText("ȷ��");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        result = chooser.showOpenDialog(null);//�����Ĵ���
        if (result == JFileChooser.APPROVE_OPTION) {//�����ȷ�ϻ򱣴� ��Ӧ���¼�
            filePath = chooser.getSelectedFile().getPath();
            System.out.println("Choose Dictionary: " + filePath);
        }
    }

    public boolean isExport() {
        return result == JFileChooser.APPROVE_OPTION;
    }

    public boolean exportAll() {
        final var classNames = STU_TOOL.listClass();

        SXSSFWorkbook wb;
        wb = new SXSSFWorkbook(5000);//�ڴ��л���5000������
        wb.setCompressTempFiles(true);//��ʱ�ļ��Ƿ����ѹ�� ��ѹ���Ļ����̺ܿ챻д��
        for (var className : classNames) {
            final String sheetName;
            if (all.equals(className)) {
                sheetName = "����ѧ��";
            } else {
                sheetName = className;
            }
            var sheet = wb.createSheet(sheetName);//sheetname�ǰ༶��
            var firstRow = sheet.createRow(0);//������һ��
            for (int i = 0; i < TITLE.length; i++) {
                var cell = firstRow.createCell(i);//������Ԫ��
                cell.setCellValue(TITLE[i]);//��Ԫ���ֵ
            }
            var res = STU_TOOL.findStu("", StuDB.FIND_NAME, className);
            for (int i = 1; i <= res.length; i++) {//���ҽ������res
                var row = sheet.createRow(i);
                for (int j = 0; j < res[i - 1].length; j++) {
                    var cell = row.createCell(j);
                    cell.setCellValue(res[i - 1][j]);
                }
                if (i % 1000 == 0) {
                    System.out.println(i);
                    for (int j = i - 999; j <= i; j++) {
                        sheet.getRow(j);
                    }
                }
            }
        }

        try {
            final var fileName = String.format("%s/�ܳɼ���Ϣ��%s%d.xlsx",
                    filePath, LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()), wb.hashCode());
            OutputStream fileOut = new FileOutputStream(fileName);
            final var buffer = new BufferedOutputStream(fileOut, 20480);
            new Thread(() -> {
                try {
                    wb.write(buffer);
                    buffer.flush();
                    buffer.close();
                    wb.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }).start();
            System.out.printf("Save %s Successfully!\n", fileName);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("�޷�д����ļ�");
            e.printStackTrace();
        }
        return false;
    }

    public boolean exportSelected(ArrayList<String> options, String className, int type) {
        final var classScores = GROUP_SCORE_TOOLS.getScores().get(className);

        String sheetName = all.equals(className) ? "ȫУ�ɼ�" : className + "��ɼ�";

        final SXSSFWorkbook wb;
        wb = new SXSSFWorkbook(5000);
        wb.setCompressTempFiles(true);
        final var sheet = wb.createSheet(sheetName);

        var firstRow = sheet.createRow(0);
        for (int i = 0; i < options.size(); i++) {
            var cell = firstRow.createCell(i);
            cell.setCellValue(options.get(i));
        }

        // ��ȡĳ��ѧ����Ϣ������type��˳��
        var res = new ClassScoreDB().listStuScores(className, type);
        for (int i = 1; i <= res.length; i++) {
            var row = sheet.createRow(i);
            int column = 0;
            for (var opt : options) {
                switch (opt) {
                    case "ѧ��" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][0]);
                    }
                    case "����" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][1]);
                    }
                    case "�Ա�" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][2]);
                    }
                    case "����" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][3]);
                    }
                    case "�༶" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][4]);
                    }
                    case "����" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][5]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.CHIN));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.CHIN));
                    }
                    case "��ѧ" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][6]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.MATH));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.MATH));
                    }
                    case "Ӣ��" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][7]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.ENG));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.ENG));
                    }
                    case "����" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][8]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.PHYS));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.PHYS));
                    }
                    case "��ѧ" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][9]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.CHEM));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.CHEM));
                    }
                    case "����" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][10]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.BIO));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.BIO));
                    }
                    case "����" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][11]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.POL));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.POL));
                    }
                    case "��ʷ" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][12]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.HIS));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.HIS));
                    }
                    case "����" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][13]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.GEO));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.GEO));
                    }
                    case "�ܳɼ�" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][14]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.ALL));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.ALL));
                    }
                    default -> {
                    }
                }
            }
            if (i % 1000 == 0) {
                System.out.println(i);
                for (int j = i - 999; j <= i; j++) {
                    sheet.getRow(j);
                }
            }
        }

        try {
            final var fileName = String.format("%s/%s��%s%d.xlsx",
                    filePath, sheetName, LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()), wb.hashCode());
            OutputStream fileOut = new FileOutputStream(fileName);
            final var buffer = new BufferedOutputStream(fileOut, 20480);
            new Thread(() -> {
                try {
                    wb.write(buffer);
                    buffer.flush();
                    buffer.close();
                    wb.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }).start();
            System.out.printf("Save %s Successfully!\n", fileName);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("�޷�д����ļ�");
            e.printStackTrace();
        }



        return false;
    }
}
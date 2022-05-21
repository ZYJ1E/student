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
    public static final String[] TITLE = {"学号", "姓名", "生日", "性别", "班级",
            "语文", "语文班级平均成绩", "语文全校平均成绩", "数学", "数学班级平均成绩", "数学全校平均成绩", "英语", "英语班级平均成绩", "英语全校平均成绩",
            "物理", "物理班级平均成绩", "物理全校平均成绩","化学", "化学班级平均成绩", "化学全校平均成绩","生物", "生物班级平均成绩", "生物全校平均成绩",
            "政治", "政治班级平均成绩", "政治全校平均成绩","历史", "历史班级平均成绩", "历史全校平均成绩","地理", "地理班级平均成绩", "地理全校平均成绩",
            "总成绩", "班级总成绩", "全校总成绩"};
    private static final StuDB STU_TOOL = new StuDB();
    private static final GroupScoreDB GROUP_SCORE_TOOLS = GroupScoreDB.getGroupScoreDBdb();
    private static final String all = "stu";
    private String filePath;
    private final int result;

    public ExcelExport() {
        JFileChooser chooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        chooser.setCurrentDirectory(fsv.getHomeDirectory());
        chooser.setDialogTitle("请选择导出位置");
        chooser.setApproveButtonText("确定");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        result = chooser.showOpenDialog(null);//弹出的窗口
        if (result == JFileChooser.APPROVE_OPTION) {//点击了确认或保存 响应的事件
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
        wb = new SXSSFWorkbook(5000);//内存中缓存5000条数据
        wb.setCompressTempFiles(true);//临时文件是否进行压缩 不压缩的话磁盘很快被写满
        for (var className : classNames) {
            final String sheetName;
            if (all.equals(className)) {
                sheetName = "所有学生";
            } else {
                sheetName = className;
            }
            var sheet = wb.createSheet(sheetName);//sheetname是班级名
            var firstRow = sheet.createRow(0);//创建第一行
            for (int i = 0; i < TITLE.length; i++) {
                var cell = firstRow.createCell(i);//创建单元格
                cell.setCellValue(TITLE[i]);//单元格的值
            }
            var res = STU_TOOL.findStu("", StuDB.FIND_NAME, className);
            for (int i = 1; i <= res.length; i++) {//查找结果集合res
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
            final var fileName = String.format("%s/总成绩信息表%s%d.xlsx",
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
            System.err.println("无法写入该文件");
            e.printStackTrace();
        }
        return false;
    }

    public boolean exportSelected(ArrayList<String> options, String className, int type) {
        final var classScores = GROUP_SCORE_TOOLS.getScores().get(className);

        String sheetName = all.equals(className) ? "全校成绩" : className + "班成绩";

        final SXSSFWorkbook wb;
        wb = new SXSSFWorkbook(5000);
        wb.setCompressTempFiles(true);
        final var sheet = wb.createSheet(sheetName);

        var firstRow = sheet.createRow(0);
        for (int i = 0; i < options.size(); i++) {
            var cell = firstRow.createCell(i);
            cell.setCellValue(options.get(i));
        }

        // 获取某班学生信息，按照type的顺序
        var res = new ClassScoreDB().listStuScores(className, type);
        for (int i = 1; i <= res.length; i++) {
            var row = sheet.createRow(i);
            int column = 0;
            for (var opt : options) {
                switch (opt) {
                    case "学号" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][0]);
                    }
                    case "姓名" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][1]);
                    }
                    case "性别" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][2]);
                    }
                    case "生日" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][3]);
                    }
                    case "班级" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][4]);
                    }
                    case "语文" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][5]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.CHIN));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.CHIN));
                    }
                    case "数学" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][6]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.MATH));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.MATH));
                    }
                    case "英语" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][7]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.ENG));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.ENG));
                    }
                    case "物理" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][8]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.PHYS));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.PHYS));
                    }
                    case "化学" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][9]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.CHEM));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.CHEM));
                    }
                    case "生物" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][10]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.BIO));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.BIO));
                    }
                    case "政治" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][11]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.POL));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.POL));
                    }
                    case "历史" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][12]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.HIS));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.HIS));
                    }
                    case "地理" -> {
                        var cell = row.createCell(column++);
                        cell.setCellValue(res[i - 1][13]);
                        var cellClassAve = row.createCell(column++);
                        cellClassAve.setCellValue(classScores.getSubjectAverage(classscore.GEO));
                        var cellAllAve = row.createCell(column++);
                        cellAllAve.setCellValue(new ClassScoreDB().getAllAvg(className, ClassScoreDB.GEO));
                    }
                    case "总成绩" -> {
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
            final var fileName = String.format("%s/%s表%s%d.xlsx",
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
            System.err.println("无法写入该文件");
            e.printStackTrace();
        }



        return false;
    }
}
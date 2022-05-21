package windows;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import test.classscore;
import test.Student;
import sql.GroupScoreDB;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;


public class AnalyzeFrame extends JFrame {
    public static final int PIE_CHART = 21;
    public static final int BAR_CHART = 22;
    private final HashMap classScore = GroupScoreDB.getGroupScoreDBdb().getScores();

    public AnalyzeFrame(Student student, int type) {
        switch (type) {
            case PIE_CHART: {
                add(pieChart(student));
            }
            break;
            case BAR_CHART: {
                add(barChart(student));
            }
            break;
            default: {
            }
        }
        setSize(1200, 800);

        setVisible(true);
    }

    public ChartPanel pieChart(Student student) {
        final String info = String.format("姓名:%s 学号:%d 性别:%s 班级:%s",
                student.getName(), student.getID(), student.getSexual(), student.getClassID());

        initCharts();

        DefaultPieDataset dataset = new DefaultPieDataset(); // 创建数据集
        dataset.setValue("语文", (double) student.getChin());
        dataset.setValue("数学", (double) student.getMath());
        dataset.setValue("英语", (double) student.getEng());
        dataset.setValue("物理", (double) student.getPhys());
        dataset.setValue("化学", (double) student.getChem());
        dataset.setValue("生物", (double) student.getBio());
        dataset.setValue("政治", (double) student.getPol());
        dataset.setValue("历史", (double) student.getHis());
        dataset.setValue("地理", (double) student.getGeo());
        var chart = ChartFactory.createPieChart3D(info, dataset, true, false, false);

        final var plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(270);
        plot.setForegroundAlpha(0.60f);
        plot.setInteriorGap(0.02);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(("{0}:{1}分({2})"),
                NumberFormat.getNumberInstance(), new DecimalFormat("0.000%")));

        setVisible(true);
        return new ChartPanel(chart);
    }

    public ChartPanel barChart(Student student) {
        final String info = String.format("姓名:%s 学号:%d 性别:%s 班级:%s",
                student.getName(), student.getID(), student.getSexual(), student.getClassID());

        initCharts();

        // 设置数据集
        var stuDataset = new DefaultCategoryDataset();
        stuDataset.setValue(student.getSumScore() / 3F, "个人成绩", "平均总成绩(整除9)");
        stuDataset.setValue(student.getMath(), "个人成绩", "语文");
        stuDataset.setValue(student.getMath(), "个人成绩", "数学");
        stuDataset.setValue(student.getEng(), "个人成绩", "英语");
        stuDataset.setValue(student.getPhys(), "个人成绩", "物理");
        stuDataset.setValue(student.getChem(), "个人成绩", "化学");
        stuDataset.setValue(student.getBio(), "个人成绩", "生物");
        stuDataset.setValue(student.getPol(), "个人成绩", "政治");
        stuDataset.setValue(student.getHis(), "个人成绩", "历史");
        stuDataset.setValue(student.getGeo(), "个人成绩", "地理");
      

        var score = (classscore) classScore.get(student.getClassID());
        stuDataset.setValue(score.getSubjectAverage(classscore.ALL) / 9F,
                "班级平均成绩", "平均总成绩(整除9)");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHIN), "班级平均成绩", "语文");
        stuDataset.setValue(score.getSubjectAverage(classscore.MATH), "班级平均成绩", "数学");
        stuDataset.setValue(score.getSubjectAverage(classscore.ENG), "班级平均成绩", "英语");
        stuDataset.setValue(score.getSubjectAverage(classscore.PHYS), "班级平均成绩", "物理");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHEM), "班级平均成绩", "化学");
        stuDataset.setValue(score.getSubjectAverage(classscore.BIO), "班级平均成绩", "生物");
        stuDataset.setValue(score.getSubjectAverage(classscore.POL), "班级平均成绩", "政治");
        stuDataset.setValue(score.getSubjectAverage(classscore.HIS), "班级平均成绩", "历史");
        stuDataset.setValue(score.getSubjectAverage(classscore.GEO), "班级平均成绩", "地理");
       
        

        score = (classscore) classScore.get("stu");
        stuDataset.setValue(score.getSubjectAverage(classscore.ALL) / 9F,
                "全校平均成绩", "平均总成绩(整除9)");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHIN), "全校平均成绩", "语文");
        stuDataset.setValue(score.getSubjectAverage(classscore.MATH), "全校平均成绩", "数学");
        stuDataset.setValue(score.getSubjectAverage(classscore.ENG), "全校平均成绩", "英语");
        stuDataset.setValue(score.getSubjectAverage(classscore.PHYS), "全校平均成绩", "物理");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHEM), "全校平均成绩", "化学");
        stuDataset.setValue(score.getSubjectAverage(classscore.BIO), "全校平均成绩", "生物");
        stuDataset.setValue(score.getSubjectAverage(classscore.POL), "全校平均成绩", "政治");
        stuDataset.setValue(score.getSubjectAverage(classscore.HIS), "全校平均成绩", "历史");
        stuDataset.setValue(score.getSubjectAverage(classscore.GEO), "全校平均成绩", "地理");

        var chart = ChartFactory.createBarChart("个人成绩分析", "", "成绩",
                stuDataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        //显示条目标签
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
        renderer.setDefaultItemLabelsVisible(true);

        chart.setTitle(info);

        setVisible(true);
        return new ChartPanel(chart);
    }

    private void initCharts() {
        final Font titleFront = new Font("楷体", Font.BOLD, 25);
        final Font textFront = new Font("宋体", Font.PLAIN, 20);


        //创建主题样式，避免中文乱码
        var chartTheme = new StandardChartTheme("CN"); // 设置标题字体
        chartTheme.setExtraLargeFont(titleFront); // 设置图例的字体
        chartTheme.setRegularFont(textFront); // 设置轴向的字体
        chartTheme.setLargeFont(textFront); // 应用主题样式
        ChartFactory.setChartTheme(chartTheme);
    }
}


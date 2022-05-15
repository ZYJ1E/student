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
        final String info = String.format("����:%s ѧ��:%d �Ա�:%s �༶:%s",
                student.getName(), student.getID(), student.getSexual(), student.getClassID());

        initCharts();

        DefaultPieDataset dataset = new DefaultPieDataset(); // �������ݼ�
        dataset.setValue("����", (double) student.getChin());
        dataset.setValue("��ѧ", (double) student.getMath());
        dataset.setValue("Ӣ��", (double) student.getEng());
        dataset.setValue("����", (double) student.getPhys());
        dataset.setValue("��ѧ", (double) student.getChem());
        dataset.setValue("����", (double) student.getBio());
        dataset.setValue("����", (double) student.getPol());
        dataset.setValue("��ʷ", (double) student.getHis());
        dataset.setValue("����", (double) student.getGeo());
        var chart = ChartFactory.createPieChart3D(info, dataset, true, false, false);

        final var plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(270);
        plot.setForegroundAlpha(0.60f);
        plot.setInteriorGap(0.02);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(("{0}:{1}��({2})"),
                NumberFormat.getNumberInstance(), new DecimalFormat("0.000%")));

        setVisible(true);
        return new ChartPanel(chart);
    }

    public ChartPanel barChart(Student student) {
        final String info = String.format("����:%s ѧ��:%d �Ա�:%s �༶:%s",
                student.getName(), student.getID(), student.getSexual(), student.getClassID());

        initCharts();

        // �������ݼ�
        var stuDataset = new DefaultCategoryDataset();
        stuDataset.setValue(student.getSumScore() / 3F, "���˳ɼ�", "ƽ���ܳɼ�(����9)");
        stuDataset.setValue(student.getMath(), "���˳ɼ�", "����");
        stuDataset.setValue(student.getMath(), "���˳ɼ�", "��ѧ");
        stuDataset.setValue(student.getEng(), "���˳ɼ�", "Ӣ��");
        stuDataset.setValue(student.getPhys(), "���˳ɼ�", "����");
        stuDataset.setValue(student.getChem(), "���˳ɼ�", "��ѧ");
        stuDataset.setValue(student.getBio(), "���˳ɼ�", "����");
        stuDataset.setValue(student.getPol(), "���˳ɼ�", "����");
        stuDataset.setValue(student.getHis(), "���˳ɼ�", "��ʷ");
        stuDataset.setValue(student.getGeo(), "���˳ɼ�", "����");
      

        var score = (classscore) classScore.get(student.getClassID());
        stuDataset.setValue(score.getSubjectAverage(classscore.ALL) / 9F,
                "�༶ƽ���ɼ�", "ƽ���ܳɼ�(����9)");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHIN), "�༶ƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.MATH), "�༶ƽ���ɼ�", "��ѧ");
        stuDataset.setValue(score.getSubjectAverage(classscore.ENG), "�༶ƽ���ɼ�", "Ӣ��");
        stuDataset.setValue(score.getSubjectAverage(classscore.PHYS), "�༶ƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHEM), "�༶ƽ���ɼ�", "��ѧ");
        stuDataset.setValue(score.getSubjectAverage(classscore.BIO), "�༶ƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.POL), "�༶ƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.HIS), "�༶ƽ���ɼ�", "��ʷ");
        stuDataset.setValue(score.getSubjectAverage(classscore.GEO), "�༶ƽ���ɼ�", "����");
       
        

        score = (classscore) classScore.get("stu");
        stuDataset.setValue(score.getSubjectAverage(classscore.ALL) / 9F,
                "ȫУƽ���ɼ�", "ƽ���ܳɼ�(����9)");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHIN), "ȫУƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.MATH), "ȫУƽ���ɼ�", "��ѧ");
        stuDataset.setValue(score.getSubjectAverage(classscore.ENG), "ȫУƽ���ɼ�", "Ӣ��");
        stuDataset.setValue(score.getSubjectAverage(classscore.PHYS), "ȫУƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.CHEM), "ȫУƽ���ɼ�", "��ѧ");
        stuDataset.setValue(score.getSubjectAverage(classscore.BIO), "ȫУƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.POL), "ȫУƽ���ɼ�", "����");
        stuDataset.setValue(score.getSubjectAverage(classscore.HIS), "ȫУƽ���ɼ�", "��ʷ");
        stuDataset.setValue(score.getSubjectAverage(classscore.GEO), "ȫУƽ���ɼ�", "����");

        var chart = ChartFactory.createBarChart("���˳ɼ�����", "", "�ɼ�",
                stuDataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        //��ʾ��Ŀ��ǩ
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
        renderer.setDefaultItemLabelsVisible(true);

        chart.setTitle(info);

        setVisible(true);
        return new ChartPanel(chart);
    }

    private void initCharts() {
        final Font titleFront = new Font("����", Font.BOLD, 25);
        final Font textFront = new Font("����", Font.PLAIN, 20);


        //����������ʽ��������������
        var chartTheme = new StandardChartTheme("CN"); // ���ñ�������
        chartTheme.setExtraLargeFont(titleFront); // ����ͼ��������
        chartTheme.setRegularFont(textFront); // �������������
        chartTheme.setLargeFont(textFront); // Ӧ��������ʽ
        ChartFactory.setChartTheme(chartTheme);
    }
}


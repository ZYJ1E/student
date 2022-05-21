package sql;


import sql.BaseDB;
import sql.StuDB;
import test.classscore;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public class GroupScoreDB implements BaseDB {
    private static ResultSet res;
    private static GroupScoreDB groupScoreDBdb = null;
    private static HashMap<String, classscore> classScores;
    private static String[][] allStudents;
    private ResultSet resultSet;

    private GroupScoreDB() throws SQLException {
        resultSet = DB.query("select classid, avg(chin), avg(math), avg(eng), avg(phys), avg(chem), avg(bio),avg(pol),avg(his), avg(geo),avg(sumscore) from `stu` group by classid");
        classScores = new HashMap<>();
        while (resultSet.next()) {
            var Class = new classscore();
            final var classname = resultSet.getString(1);
            Class.setAvgChin(resultSet.getDouble(2));
            Class.setAvgMath(resultSet.getDouble(3));
            Class.setAvgEng(resultSet.getDouble(4));
            Class.setAvgPhys(resultSet.getDouble(5));
            Class.setAvgChem(resultSet.getDouble(6));
            Class.setAvgBio(resultSet.getDouble(7));
            Class.setAvgPol(resultSet.getDouble(8));
            Class.setAvgHis(resultSet.getDouble(9));
            Class.setAvgGeo(resultSet.getDouble(10));
            Class.setAvgAll(resultSet.getDouble(11));
            classScores.put(classname, Class);
        }
        resultSet = DB.query("select avg(chin), avg(math), avg(eng),avg(phys),avg(chem), avg(bio),avg(pol),avg(his),avg(geo),avg(sumscore) from `stu`");
        var Class = new classscore();
        while (resultSet.next()) {
            Class.setAvgChin(resultSet.getDouble(1));
            Class.setAvgMath(resultSet.getDouble(2));
            Class.setAvgEng(resultSet.getDouble(3));
            Class.setAvgPhys(resultSet.getDouble(4));
            Class.setAvgChem(resultSet.getDouble(5));
            Class.setAvgBio(resultSet.getDouble(6));
            Class.setAvgPol(resultSet.getDouble(7));
            Class.setAvgHis(resultSet.getDouble(8));
            Class.setAvgGeo(resultSet.getDouble(9));
            Class.setAvgAll(resultSet.getDouble(10));
        }

        classScores.put("stu", Class);

        closeAll();
    }

    public static GroupScoreDB getGroupScoreDBdb() {
        if (groupScoreDBdb == null) {
            try {
                System.out.println("GroupScoreDbdb is null");
                groupScoreDBdb = new GroupScoreDB();
                return groupScoreDBdb;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return groupScoreDBdb;
    }

    public HashMap<String, classscore> getScores() {
        return classScores;
    }

    public String[][] listStu() throws SQLException {
        if (allStudents == null) {
            ArrayList<String> classname = new ArrayList<>();
            res = DB.query("select classid from `stu` group by classid order by avg(sumscore) desc;");
            while (res.next()) {
                classname.add(res.getString(1));
            }
            ArrayList<String[]> stus = new ArrayList<>();
            final var allScore = classScores.get("stu");
            for (var Name : classname) {
                String sql = String.format("SELECT * FROM `stu` WHERE `CLASSID` = '%s'", Name);
                res = DB.query(sql);
                while (res.next()) {
                    var score = classScores.get(Name);
                    getInfo(stus, score, allScore, res, Name);
                }
            }
            allStudents = stus.toArray(new String[stus.size()][17]);
            closeAll();
        }

        return allStudents;
    }

    public String[][] findStu(String object, int type, String Class) throws SQLException {
        String findOrder = null;
        var res = new ArrayList<String[]>();
        if ("stu".equals(Class)) {
            switch (type) {
                case StuDB.FIND_NAME: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE NAME LIKE '%%%s%%'", object);
                }
                break;
                case StuDB.FIND_ID: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE `ID` = '%s'", object);
                }
                break;
                case StuDB.FIND_SUMSCORE_DESC: {
                    findOrder = "SELECT * FROM `stu` ";
                }
                default: {
                }
            }
            try {
                return listStu();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } else {
            switch (type) {
                case StuDB.FIND_NAME: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE NAME LIKE '%%%s%%' AND `CLASSID` = '%s'", object, Class);
                }
                break;
                case StuDB.FIND_ID: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE `ID` = '%s' AND `CLASSID` = '%s'", object, Class);
                }
                break;
                default: {
                }
            }
        }


        resultSet = DB.query(findOrder);
        final var score = classScores.get(Class);
        final var allScore = classScores.get("stu");
        try {
            while (resultSet.next()) {
                getInfo(res, score, allScore, resultSet, Class);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeAll();

        return res.toArray(new String[res.size()][9]);
    }

    private void getInfo(ArrayList<String[]> res, classscore score,
                         classscore allScore, ResultSet resultSet, String Class)
            throws SQLException {
        final long id = resultSet.getLong("id");
        final var name = resultSet.getString("name");
        final var sexual = resultSet.getString("sexual");
        final var birthday = resultSet.getDate("birthday").toString();
        final var classid = resultSet.getString("classid");

        final int chin = resultSet.getInt("chin");
        final double allChin= allScore.getSubjectAverage(classscore.CHIN);
        final int math = resultSet.getInt("math");
        final double allMath = allScore.getSubjectAverage(classscore.MATH);
        final int eng = resultSet.getInt("eng");
        final double allEng = allScore.getSubjectAverage(classscore.ENG);
        final int phys = resultSet.getInt("phys");
        final double allPhys = allScore.getSubjectAverage(classscore.PHYS);
        final int chem= resultSet.getInt("chem");
        final double allChem = allScore.getSubjectAverage(classscore.CHEM);
        final int bio = resultSet.getInt("bio");
        final double allBio = allScore.getSubjectAverage(classscore.BIO);
        final int pol = resultSet.getInt("pol");
        final double allPol = allScore.getSubjectAverage(classscore.POL);
        final int his = resultSet.getInt("his");
        final double allHis = allScore.getSubjectAverage(classscore.HIS);
        final int geo = resultSet.getInt("geo");
        final double allGeo = allScore.getSubjectAverage(classscore.GEO);
        final int sumscore = resultSet.getInt("sumscore");
        final double allSum = allScore.getSubjectAverage(classscore.ALL);

        double classChin = score.getSubjectAverage(classscore.CHIN);
        double classMath = score.getSubjectAverage(classscore.MATH);
        double classEng = score.getSubjectAverage(classscore.ENG);
        double classPhys = score.getSubjectAverage(classscore.PHYS);
        double classChem = score.getSubjectAverage(classscore.CHEM);
        double classBio = score.getSubjectAverage(classscore.BIO);
        double classPol = score.getSubjectAverage(classscore.POL);
        double classHis = score.getSubjectAverage(classscore.HIS);
        double classGeo = score.getSubjectAverage(classscore.GEO);
        double classSum = score.getSubjectAverage(classscore.ALL);


        final var e = new String[]{
                Long.toString(id), name, sexual, birthday, classid,
                Integer.toString(chin), Double.toString(classChin), Double.toString(allChin),
                Integer.toString(math), Double.toString(classMath), Double.toString(allMath),
                Integer.toString(eng), Double.toString(classEng), Double.toString(allEng),
                Integer.toString(phys), Double.toString(classPhys), Double.toString(allPhys),
                Integer.toString(chem), Double.toString(classChem), Double.toString(allChem),
                Integer.toString(bio), Double.toString(classBio), Double.toString(allBio),
                Integer.toString(pol), Double.toString(classPol), Double.toString(allPol),
                Integer.toString(his), Double.toString(classHis), Double.toString(allHis),
                Integer.toString(geo), Double.toString(classGeo), Double.toString(allGeo),
                Integer.toString(sumscore), Double.toString(classSum), Double.toString(allSum)
        };
        res.add(e);
    }

    @Override
    public void closeAll() {
        try {
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        DB.close();
    }
}

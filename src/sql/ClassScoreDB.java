package sql;


import windows.selectExportDialog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassScoreDB implements BaseDB {
    public static final int CHIN=0;
    public static final int MATH=1;
    public static final int ENG=2;
    public static final int PHYS=3;
    public static final int CHEM=4;
    public static final int BIO=5;
    public static final int POL=6;
    public static final int HIS=7;
    public static final int GEO=8;
    public static final int ALL = 9;
    private static final String all = "stu";
    private static ResultSet res;

    public String[][] listStuScores(String className, int type) {
        String order;
        if (all.equals(className)) {
            order = "SELECT * FROM `stu` ORDER BY ";
        } else {
            order = String.format("SELECT * FROM `stu` WHERE `CLASSID` = '%s' ORDER BY ", className);
        }

        switch (type) {
            case selectExportDialog
                    .ID_ASC:
                order += "`ID` ASC";
                break;
            case selectExportDialog.
                    ID_DESC:
                order += "`ID` DESC";
                break;
            case selectExportDialog.
                    SUM_ASC:
                order += "`SUMSCORE` ASC";
                break;
            case selectExportDialog.
                    SUM_DESC:
                order += "`SUMSCORE` DESC";
                break;
            default: {
                System.err.println("No Option exist " + this.getClass().toString());
            }
        }
        res = DB.query(order);
        var info = new ArrayList<String[]>();
        try {
            while (res.next()) {
                final long id = res.getLong("id");
                final var name = res.getString("name");
                final var sexual = res.getString("sexual");
                final var birthday = res.getDate("birthday").toString();
                final var classid = res.getString("classid");
                final int chin = res.getInt("chin");
                final int math = res.getInt("math");
                final int eng = res.getInt("eng");
                final int phys = res.getInt("phys");
                final int chem = res.getInt("chem");
                final int bio = res.getInt("bio");
                final int pol = res.getInt("pol");
                final int his = res.getInt("his");
                final int geo = res.getInt("geo");
                final int sumscore = res.getInt(14);
                final var e = new String[]{
                        Long.toString(id), name, sexual, birthday, classid,
                         Integer.toString(chin),Integer.toString(math),Integer.toString(eng),
                        Integer.toString(phys),Integer.toString(chem),Integer.toString(bio),
                        Integer.toString(pol),Integer.toString(his),Integer.toString(geo),
                        Integer.toString(sumscore)
                };
                info.add(e);
            }
            closeAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return info.toArray(new String[info.size()][15]);
    }

    public double getAllAvg(String className, int subject) {
        double avg = 0;
        String order;

        if (all.equals(className)) {
            switch (subject) {
                case CHIN: {
                    order = "SELECT AVG(`CHIN`) from `stu`";
                }
                break;
                case MATH: {
                    order = "SELECT AVG(`MATH`) from `stu`";
                }
                break;
                case ENG: {
                    order = "SELECT AVG(`ENG`) from `stu`";
                }
                break;
                case PHYS: {
                    order = "SELECT AVG(`PHYS`) from `stu`";
                }
                break;
                case CHEM: {
                    order = "SELECT AVG(`CHEM`) from `stu`";
                }
                break;
                case BIO: {
                    order = "SELECT AVG(`BIO`) from `stu`";
                }
                break;
                case POL: {
                    order = "SELECT AVG(`POL`) from `stu`";
                }
                break;
                case HIS: {
                    order = "SELECT AVG(`HIS`) from `stu`";
                }
                break;
                case GEO: {
                    order = "SELECT AVG(`GEO`) from `stu`";
                }
                break;
                default: {
                    order = "SELECT AVG(`SUMSCORE`) from `stu`";
                }
            }
        } else {
            switch (subject) {
                case CHIN: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "CHIN", className);
                }break;
                case MATH: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "MATH", className);
                }break;
                case ENG: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "ENG", className);
                }break;
                case PHYS: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "PHYS", className);
                }
                break;
                case CHEM: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "CHEM", className);
                }
                break;
                case BIO: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "BIO", className);
                }
                break;
                case POL: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "POL", className);
                }
                break;
                case HIS: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "HIS", className);
                }
                break;
                case GEO: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "GEO", className);
                }
                break;
                default: {
                    order = String.format("SELECT AVG(`%s`) from `stu` WHERE `CLASSID` = '%s'", "SUMSCORE", className);
                }
            }
        }

        res = DB.query(order);
        try {
            while (res.next()) {
                avg = res.getDouble(1);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return avg;
    }

    @Override
    public void closeAll() throws SQLException {
        res.close();
        DB.close();
    }
}


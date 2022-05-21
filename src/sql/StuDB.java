package sql;

import test.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class StuDB implements BaseDB {
    public final static int FIND_NAME = 1;
    public final static int FIND_ID = 2;
    public final static int FIND_SUMSCORE_DESC = 3;
    private ResultSet res;//返回查询结果集

    public Long addStu(Student student)  {
        // 向总表中添加数据
        final String addToStudent = "INSERT INTO `stu` (name, sexual, birthday, " +
                "classid, chin, math, eng,phys,chem, bio,pol,his,geo,sumscore) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?)";
        DB.update(addToStudent, student.getObj());

        // 获得新ID
        final String getNewID = "SELECT * FROM `stu` WHERE ID = LAST_INSERT_ID()";
        res = DB.query(getNewID);
        Long newID = null;
        try {
            while (res.next()) {
                newID = res.getLong(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return newID;
    }

    public void addOldStu(Student student) {
        // 向总表中添加数据
        final String addToStudent = String.format("INSERT INTO `stu` (id, name, sexual, birthday, " +
                "classid, chin, math, eng,phys,chem, bio,pol,his,geo,sumscore) " +
                "values(%d, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?)", student.getID());
        DB.update(addToStudent, student.getObj());

        closeAll();
    }
    // 删除学生信息
    public void delStu(String id) {
        final String removeOrder = "DELETE FROM `stu` WHERE ID = " + id;
        DB.update(removeOrder);
        closeAll();
    }
    // 更新学生信息
    public void updateStu(Student student) {
        // 更新总表中的数据
        final String updateOrigin = String.format("UPDATE `stu` SET name = ?, sexual = ?, birthday = ?, classid = ?, " + "chin= ?, math= ?, eng= ?,phys= ?,chem= ?, "+
                "bio= ?,pol= ?,his= ?,geo= ?,sumscore = ? WHERE ID = %d", student.getID());
        DB.update(updateOrigin, student.getObj());
        closeAll();
    }

    // 列出班级
    public String[] listClass(){
        ArrayList<String> lists = new ArrayList<String>();
        lists.add("stu");
        String order = "SELECT DISTINCT `CLASSID` FROM `stu`";
        res = DB.query(order);
        try {
            while (res.next()) {
                lists.add(res.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeAll();
        return lists.toArray(new String[lists.size()]);
    }

// 查找功能
    public String[][] findStu(String object, int type, String Class) {
        String findOrder = null;
        ArrayList<String[]> res = new ArrayList<String[]>();
        if ("stu".equals(Class)) {
            switch (type) {
                case FIND_NAME: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE NAME LIKE '%%%s%%'", object);
                    break;
                }
                case FIND_ID: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE `ID` = '%s'", object);
                    break;
                }

                case FIND_SUMSCORE_DESC: {
                    findOrder = "SELECT * FROM `stu` ";
                    break;
                }
                default: {
                    break;
                }
            }
        } else {
            switch (type) {
                case FIND_NAME: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE NAME LIKE '%%%s%%' AND `CLASSID` = '%s'", object, Class);
                }
                break;
                case FIND_ID: {
                    findOrder = String.format(
                            "SELECT * FROM `stu` WHERE `ID` = '%s' AND `CLASSID` = '%s'", object, Class);
                }
                break;
                default: {
                }
            }
        }
        this.res = DB.query(findOrder);
        try {
            while (this.res.next()) {
                final var id = this.res.getLong("id");
                final var name = this.res.getString("name");
                final var sexual = this.res.getString("sexual");
                final var birthday = this.res.getDate("birthday").toString();
                final var classid = this.res.getString("classid");
                final int chin = this.res.getInt("chin");
                final int math = this.res.getInt("math");
                final int eng = this.res.getInt("eng");
                final int phys = this.res.getInt("phys");
                final int chem = this.res.getInt("chem");
                final int bio = this.res.getInt("bio");
                final int pol = this.res.getInt("pol");
                final int his = this.res.getInt("his");
                final int geo = this.res.getInt("geo");
                final int sumscore = this.res.getInt("sumscore");
                final var e = new String[]{
                        Long.toString(id), name, sexual, birthday, classid,Integer.toString(chin),
                        Integer.toString(math), Integer.toString(eng), Integer.toString(phys),Integer.toString(chem),Integer.toString(bio),
                        Integer.toString(pol),Integer.toString(his),Integer.toString(geo),Integer.toString(sumscore)
                };
                res.add(e);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeAll();
        return res.toArray(new String[res.size()][15]);
    }
    @Override
    public void closeAll() {
        DB.close();
        if (res != null) {
            try {
                res.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
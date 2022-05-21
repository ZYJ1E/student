//班级下拉框组件
package component;


import sql.StuDB;

import javax.swing.*;

public class ClassComBox extends JComboBox<String> {//班级下拉表组件
    private static final StuDB STU_TOOL = new StuDB();//STU_TOOL 为学生数据库工具

    public ClassComBox() {
        setBorder(BorderFactory.createTitledBorder("请选择班级"));
        setModel(new DefaultComboBoxModel<>(STU_TOOL.listClass()));
        setSelectedItem("stu");//默认班级为stu
    }

    public void update() {
        setModel(new DefaultComboBoxModel<>(STU_TOOL.listClass()));
    }
}

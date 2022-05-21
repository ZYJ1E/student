package component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class TimeClock extends JPanel {
    public TimeClock(){
        setLayout(new FlowLayout(FlowLayout.LEADING));
        JLabel showTime=new JLabel();
        JLabel showDate=new JLabel();
        LocalDateTime systemTime=LocalDateTime.now();
        showDate.setText(String.format("%dƒÍ%02d‘¬%02d»’%s",systemTime.getYear(),
                systemTime.getMonthValue(),
                systemTime.getDayOfMonth(),
                systemTime.getDayOfWeek().toString()));
    showTime.setText(String.format("%02d:%02d:%02d",systemTime.getHour(),
            systemTime.getMinute(),
            systemTime.getSecond()));
    add(showDate);
    add(showTime);
    setVisible(true);
    }
}

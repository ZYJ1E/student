package component;

import java.awt.*;
import java.io.Serial;
import javax.swing.*;

public class bg_picture extends JPanel {
    @Serial
    //private static final long serialVersionUID = -6352788025440244338L;
    private Image image = null;
    public bg_picture(Image image) {
        this.image = image;

    }
    // �̶�����ͼƬ���������JPanel������ͼƬ������������
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}


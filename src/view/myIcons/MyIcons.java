package view.myIcons;

import javax.swing.*;
import java.awt.*;

public class MyIcons {
    public static final ImageIcon CHECK_OK;
    public static final ImageIcon ERROR;
    static {

//        ImageIcon im = new ImageIcon("pics/checkok.png");
        CHECK_OK = new ImageIcon("pics/checkok.png");
//        ImageIcon im = new ImageIcon("pics/ok-16.png");
//        Image image = im.getImage();
//        Image newImage = image.getScaledInstance(30,30,Image.SCALE_DEFAULT);
//        CHECK_OK = new ImageIcon(newImage);
        ERROR = new ImageIcon("pics/error3.jpg");
    }
}

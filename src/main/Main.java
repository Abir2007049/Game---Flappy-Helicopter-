package main;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception
    {
        //System.out.println("Hello world!");
        int boardWidth=360;
        int boardHeight=600;

        JFrame frame=new JFrame("Flappy Helicopter");
       // frame.setVisible(true);
        frame.setSize(boardWidth,boardWidth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Application application=new Application();
        frame.add(application);
        frame.pack();
        application.requestFocus();
        frame.setVisible(true);

    }
}
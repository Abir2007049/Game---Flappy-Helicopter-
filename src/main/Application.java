package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Application extends JPanel implements ActionListener , KeyListener {
    int boardwidth = 360;
    int boardHeight = 640;


    //img
    Image bg;
    Image birdimg;
    Image topp;
    Image bottomp;


    // bird

    int birdx=boardwidth/8;
    int birdy=boardHeight/2;
    int birdWidth=64;
    int birdHeight=54;




    class Bird {
        int x=birdx;
        int y=birdy;
        int width=birdWidth;
        int height=birdHeight;
        Image img;
        Bird(Image img) {
            this.img=img;

        }
    }
    //pipe
    int pipex=boardwidth;
    int pipey=0;
    int pipeWidth=64;
    int pipeHeight=412;
    class Pipe
    {
        int x=pipex;
        int y=pipey;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;
        Pipe(Image img)
        {
            this.img=img;
        }
    }




    //logic
    Bird bird;

    int vely=0;
    int velx=-4;
    int gravity=1;
    ArrayList<Pipe> pipes;
    Random random=new Random();
    //timer
    Timer gameLoop;
    Timer PlacePipesTimer;

    //gameover
    boolean GameOver=false;
    //score
    double score=0;



    public Application() {
        setPreferredSize(new Dimension(boardwidth, boardHeight));
        // Load images from the resources folder
        setFocusable(true);
        addKeyListener(this);
        bg = loadImage("images/bg.jpg");
        birdimg = loadImage("images/bird.png");
        topp = loadImage("images/toppipe.png");
        bottomp = loadImage("images/bottompipe.png");
        bird=new Bird(birdimg);
        pipes=new ArrayList<Pipe>();
        //pipes timer
        PlacePipesTimer=new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlacePipes();

            }
        });
        PlacePipesTimer.start();


        //timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public boolean collision(Bird a,Pipe b)
    {
        return  a.x < b.x+b.width &&
                a.x + a.width>b.x &&
                a.y<b.y + b.height &&
                a.y + a.height > b.y;


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(GameOver)
        {
            PlacePipesTimer.stop();
            gameLoop.stop();
        }

    }
    private Image loadImage(String path) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    public void PlacePipes()
    {   int randomPipeY= (int) (pipey - pipeHeight/4 -Math.random()*(pipeHeight/2));
        int OpeningSpace=boardHeight/4;


        Pipe topPipe=new Pipe(topp);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe=new Pipe(bottomp);
        bottomPipe.y=topPipe.y + pipeHeight+ OpeningSpace;
        pipes.add(bottomPipe);


    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }


    private void draw(Graphics g) {
        //System.out.println("draw");
        // Draw background
        g.drawImage(bg, 0, 0, boardwidth, boardHeight, null);
        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);
        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);

        }
        //score show
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN,32));
        if(GameOver) {
            g.drawString("Game Over:" + String.valueOf((int) score), 10, 35);
        }
        else
        {
            g.drawString(String.valueOf((int)score), 10,35);

        }

    }


    public void move() {
        vely += gravity;
        bird.y += vely;
        bird.y = Math.max(bird.y, 0);
        for (int i=0;i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            pipe.x+=velx;

            if(!pipe.passed && bird.x > pipe.x + pipe.width)
            {
                pipe.passed=true;
                score+=0.5;
            }
            if(collision(bird,pipe)) {
                GameOver = true;
            }
        }
        if(bird.y>boardHeight)
        {
            GameOver=true;
        }

    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            vely=-9;
            if(GameOver)
            {
                //restart
                bird.y=birdy;
                vely=0;
                pipes.clear();
                score=0;
                GameOver=false;
                gameLoop.start();
                PlacePipesTimer.start();


            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}

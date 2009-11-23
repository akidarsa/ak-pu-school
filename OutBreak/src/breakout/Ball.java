/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.util.Random;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 *
 * @author ee462a25
 */
public class Ball extends JPanel {

    public Ball(int width, int height, JLabel location, JLabel direction, JLabel life) {
        panelWidth = width;
        panelHeight = height;
        // ball's initial location is at the center
        ballPos = new TwoDVector(width / 2, height / 4, false);
        PanelCenter = new TwoDVector(width / 2, height / 2, false);
        paddlePos = new TwoDVector(panelWidth / 2, deathTop + paddleWidth / 2, false);
        double tempVX;
        double tempVY;
        do {
            tempVX = Math.random();
        } while (tempVX < 0.1);
        tempVY = Math.sqrt(1 - tempVX * tempVX);

        brickPos = new Vector();
        brickRadius = new Vector();
        bricksExist = new Vector();

        wFrame = new Win();
        lFrame = new Lose();
        wFrame.setDefaultCloseOperation(wFrame.HIDE_ON_CLOSE);
        lFrame.setDefaultCloseOperation(lFrame.HIDE_ON_CLOSE);

        ballVel = new TwoDVector(tempVX, tempVY, false);

        setSize(width, height);

        locationLabel = location;
        directionLabel = direction;
        lifeLabel = life;

    }

    public void makeBricks() {
        Random rand = new Random();
        double yMax;
        boolean overLap;
        int min = 3;
        int max = 20;
        TwoDVector temp;
        TwoDVector prevBrick;
        int prevRadius;

        for (int lcv = 0; lcv < staticCounter; lcv++) {

            do {
                radius = (int) (min + rand.nextDouble() * (max - min));

                x = radius + rand.nextDouble() * (panelWidth - 2 * radius);
                yMax = Math.sqrt(((panelWidth / 2 - radius) * (panelWidth / 2 - radius)) - (panelWidth / 2 - x) * (panelWidth / 2 - x));
                y = panelWidth / 2 - yMax + rand.nextDouble() * 2 * yMax;

                temp = new TwoDVector(x, y, false);

                overLap = false;

                for (int j = 0; j < lcv; j++) {
                    prevBrick = (TwoDVector) brickPos.get(j);
                    prevRadius = (Integer) brickRadius.get(j);
                    if (temp.getDiff(prevBrick) <= (radius + (int) prevRadius) * (radius + (int) prevRadius) + 5) {
                        overLap = true;
                    }
                }

                if (temp.getDiff(ballPos) <= (radius + ballSize / 2) * (radius + ballSize / 2)) {
                    overLap = true;
                }

            } while (y > deathTop - radius || overLap);

            isBricked = true;
            brickPos.add(lcv, temp);
            brickRadius.add(lcv, radius);
            bricksExist.add(lcv, isBricked);
        }

    }

    public void start() {
        lifeCounter = 3;
        isRunning = true;
        brickCounter = 15;
        staticCounter = 15;
        makeBricks();
        isGameOver = false;
        isDemo = false;
        ballPos = new TwoDVector(panelWidth / 2, panelHeight / 4, false);
    }

    public void panelPause() {
        isRunning = false;
    }

    public void panelUnPause() {
        isRunning = true;
    }

    public void setPaddle(int pos) {
        if (isRunning && !isGameOver && !isDemo) {

            if (pos < paddlePos.getX()) {
                paddlePos.setX(paddlePos.getX() - 1);
            }
            if (pos > paddlePos.getX()) {
                paddlePos.setX(paddlePos.getX() + 1);
            }
            if (paddlePos.getX() < 49) {
                paddlePos.setX(49);
            }
            if (paddlePos.getX() >= (panelWidth - paddleWidth - 19)) {
                paddlePos.setX(panelWidth - paddleWidth - 19);
            }
        }

    }

    public void setPaddle(int pos, char a) {

        if (pos < paddlePos.getX()) {
            paddlePos.setX(paddlePos.getX() - 1);
        }
        if (pos > paddlePos.getX()) {
            paddlePos.setX(paddlePos.getX() + 1);
        }
        if (paddlePos.getX() < 49) {
            paddlePos.setX(49);
        }
        if (paddlePos.getX() >= (panelWidth - paddleWidth - 19)) {
            paddlePos.setX(panelWidth - paddleWidth - 19);
        }


    }

    public void toggleDemo() {
        isDemo = !isDemo;
    }

    @Override
    public void paintComponent(Graphics gfx) {
        //draw labels
        locationLabel.setText("Position " + (int) ballPos.getX() + " , " + (int) ballPos.getY());
        int vx = (int) (ballVel.getX() * 1000);
        int vy = (int) (ballVel.getY() * 1000);
        directionLabel.setText("Velocity " + vx + " , " + vy);
        lifeLabel.setText("Life: " + lifeCounter);

        // erase everything
        gfx.setColor(backgroundColor);
        gfx.fillRect(0, 0, panelWidth - 1, panelHeight - 1);

        // draw ground
        gfx.setColor(foreGroundColor);
        gfx.fillOval(0, 0, panelWidth, panelHeight);

        // draw paddle
        gfx.setColor(paddleColor);
        gfx.fillArc((int) paddlePos.getX() - paddleWidth / 2, deathTop, paddleWidth, paddleWidth, 0, 180);

        // draw death space
        gfx.setColor(hellColor);
        Graphics2D g2d = (Graphics2D) gfx;
        Arc2D ch7 = new Arc2D.Double();
        ch7.setArc(0, 0, panelWidth + .2, panelHeight + .2, 220, 100, Arc2D.CHORD);
        g2d.fill(ch7);

        // update ball's location
        if (isRunning && !isGameOver) {
            ballPos.add(ballVel);
        }

        // draw ball
        gfx.setColor(ballColor);
        gfx.fillOval((int) ballPos.getX() - ballSize / 2, (int) ballPos.getY() - ballSize / 2, ballSize, ballSize);

        // draw bricks
        for (int i = 0; i < staticCounter; i++) {
            if (i % 2 == 1) {
                gfx.setColor(Color.pink);
            } else {
                gfx.setColor(Color.magenta);
            }
            isBricked = (Boolean) bricksExist.get(i);
            if (isBricked) {
                pos = (TwoDVector) brickPos.get(i);
                x = pos.getX();
                y = pos.getY();
                radius = (Integer) brickRadius.get(i);
                gfx.fillOval((int) (x - radius), (int) (y - radius), 2 * (int) radius, 2 * (int) radius);
            }
        }

        // Collision Detection with walls
        if (ballPos.getDiff(PanelCenter) >= ((panelHeight - ballSize) / 2 * (panelWidth - ballSize) / 2)) {
            if (isRunning && !isGameOver) {
                ballPos.sub(ballVel);
            }
            normVec = new TwoDVector(PanelCenter.getX() - ballPos.getX(), PanelCenter.getY() - ballPos.getY(), true);
            TwoDVector temp = new TwoDVector(ballVel.getX(), ballVel.getY(), false);
            TwoDVector noiseX = new TwoDVector(0.0001, 0, false);
            TwoDVector noiseY = new TwoDVector(0, 0.0001, false);
            temp.reflect(normVec);
            if (((temp.getX() + ballVel.getX()) < .00001) &&
                    ((temp.getY() + ballVel.getY()) < .00001)) {
                if (temp.getX() != temp.getY()) {
                    temp.add(noiseX);
                    temp.add(noiseY);
                } else {
                    temp.add(noiseX);
                    temp.sub(noiseY);
                }
            }

            ballVel = new TwoDVector(temp.getX(), temp.getY(), false);
            if (isRunning && !isGameOver) {
                ballPos.add(ballVel);
            }
            isPaddled = false;
            return;
        }

        // Collision Detection with Paddle
        if (ballPos.getDiff(paddlePos) <= ((paddleWidth + ballSize) / 2 * (paddleWidth + ballSize) / 2 + 1) && !isPaddled) {
            if (isRunning && !isGameOver) {
                ballPos.sub(ballVel);
            }
            normVec = new TwoDVector(paddlePos.getX() - ballPos.getX(), paddlePos.getY() - ballPos.getY(), true);
            ballVel.reflect(normVec);

            if (isRunning && !isGameOver) {
                ballPos.add(ballVel);
            }
            isPaddled = true;
            return;
        }

        // draw message and update paddle
        if (isDemo && !isGameOver) {
            gfx.setColor(demoMsgColor);
            gfx.drawString("~~ Demo ~~", panelWidth / 2, panelHeight / 2);
            this.setPaddle((int) ballPos.getX(), (char) staticCounter);

        }

        // Collision Detection with DeathPanel
        if (ballPos.getY() >= deathTop + paddleWidth / 2 - ballSize / 2 && !isGameOver) {
            lifeCounter--;
            if (lifeCounter == 0) {
                isGameOver = true;
                lFrame.setVisible(true);
            } else {
                ballPos = new TwoDVector(panelWidth / 2, panelHeight / 4, false);
            }

        }

        // Collision Detection with Bricks
        for (int i = 0; i < staticCounter; i++) {
            isBricked = (Boolean) bricksExist.get(i);
            if (isBricked) {
                pos = (TwoDVector) brickPos.get(i);
                x = pos.getX();
                y = pos.getY();
                radius = (Integer) brickRadius.get(i);
                if (ballPos.getDiff(pos) <= ((radius + ballSize / 2) * (radius + ballSize / 2) + 1)) {
                    if (isRunning && !isGameOver) {
                        ballPos.sub(ballVel);
                    }
                    normVec = new TwoDVector(x - ballPos.getX(), y - ballPos.getY(), true);
                    ballVel.reflect(normVec);

                    if (isRunning && !isGameOver) {
                        ballPos.add(ballVel);
                    }
                    isBricked = false;
                    bricksExist.setElementAt(isBricked, i);
                    brickCounter--;
                    if (brickCounter <= 0) {
                        isGameOver = true;
                        wFrame.setVisible(true);
                    }
                    isPaddled = false;
                    return;
                }
            }
        }
    }


    private int panelWidth;
    private int panelHeight;
    private int ballSize = 30;
    private int paddleWidth = 30;
    private int brickCounter;
    private int staticCounter;
    private int deathTop = 232;
    private int lifeCounter;
    private int radius;

    private double x;
    private double y;

    private Color backgroundColor = Color.black;
    private Color foreGroundColor = Color.white;
    private Color ballColor = Color.gray;
    private Color paddleColor = Color.blue;
    private Color hellColor = Color.red;
    private Color demoMsgColor = Color.green;
    
    private TwoDVector ballPos;
    private TwoDVector ballVel;
    private TwoDVector PanelCenter;
    private TwoDVector normVec;
    private TwoDVector paddlePos;
    private TwoDVector pos;
    
    private JLabel locationLabel;
    private JLabel directionLabel;
    private JLabel lifeLabel;

    private Vector brickPos;
    private Vector brickRadius;
    private Vector bricksExist;
    
    private Win wFrame;
    private Lose lFrame;

    private boolean isPaddled;
    private boolean isRunning;
    private boolean isGameOver;
    private boolean isDemo;
    private boolean isBricked;
    
}

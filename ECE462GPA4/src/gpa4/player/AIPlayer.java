/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4.player;

import gpa4.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Stack;

/**
 *
 * @author Jake
 */
public class AIPlayer extends Player {

    public AIPlayer(GameController gc, PlayField pf, int playerNumber, int ti) {

        this.playerNumber = playerNumber;
        this.pf = pf;
        this.gc = gc;
        threadIndex = ti;

        runThread1 = true;
        ai = new DemoManipulator();
        mop = new mopUpThread();
        mop.start();
        //aiThread = new AIThread();
        //mop = new mopUpThread();
        //mop.start();
    }

    public boolean isNetworkPlayer() {
        return false;
    }

    public boolean isKeyboardPlayer() {
        return false;
    }

    public boolean isAIPlayer() {
        return true;
    }

    public boolean acceptKeyPress(KeyEvent evt) {
        return false;
    }

    public boolean acceptNetworkCommand() {
        return false;
    }

    public boolean acceptMoveSignal() {


//            try {
//            //aiThread1.join();

        aiThread = new AIThread();
        aiThread.start();
//            runThread1 = false;
//        } catch (InterruptedException e) {
//            // Thread was interrupted
//        }



//        else{
//            aiThread2 = new AIThread();
//            aiThread2.start();
//            runThread1 = true;
//        }

        return true;
    }

    private void getPieces() {
        piece1 = pf.getPresentPiece();
        piece2 = pf.getNextPiece();
        /*
        if (piece1 == null)
        piece1 = new Piece(piece);
        else if(piece2 == null)
        piece2 = new Piece(piece);
        else{
        piece1 = new Piece(piece2);
        }*/

    }

    private class AIThread extends Thread {

        @Override
        public void run() {
            getPieces();

            Stack<Command> input = new Stack<Command>();

            /*
            if (piece2 == null)
            input = ai.calculateMove(piece1, pf);
            else
            input = ai.calculateMove(piece1, piece2, pf);
             */
            if (gc.getGameover() != 2) {
                input = ai.calculateMove(piece1, pf);
                //System.out.println("Sent commands: (top of stack: " + input.peek() + ")");
                //for (int i = 0; i < input.size(); i++)
                //    System.out.println(input.get(i));

                //executeStack(input);

                //mop = new mopUpThread();
                mop.clear();
                mop.setInput(input);
                //if (mop.isAlive() == false) {
                //    mop.start();
                //}
            }

            //gc.getNextPiece(playerNumber);
            //gc.run();
        }
    }

    private class mopUpThread extends Thread {

        @Override
        public void run() {

            //System.out.println("Sent commands: (top of stack: " + input.peek() + ")");
            //for (int i = 0; i < input.size(); i++)
            //System.out.println("mop");
            comStack = new Stack<Command>();
            while (true) {
                int breakCnt = 0;
                while (!comStack.isEmpty()) {
                    //System.out.println("Executing: " + commands.peek());
                    while (gc.getPaused() == true) {
                    }
                    breakCnt = 0;
                    try {
                        //if (input.peek() == Command.DOWN) sleep(300);
                        while (gc.sendCommand(comStack.peek(), playerNumber, threadIndex) == false && comStack.peek() != Command.DOWN) {
                            sleep(10);
                            breakCnt++;
                            if (breakCnt > 200) {
                                //System.out.println("   breakCnt: " + breakCnt);
                                gc.setPaused(true);
                                break;
                            }
                        }
                        sleep(150);
                        comStack.pop();

                    } catch (IOException ex) {
                        //System.out.println("IOException in AIPlayer");
                    } catch (InterruptedException ex) {
                        //System.out.println("InterruptedException in AIPlayer");
                    } catch (java.util.EmptyStackException ex) {
                    }
                }
                if (input != null) {
                    comStack.addAll(input);
                    input=null;
                }
            }
        }

        public void setInput(Stack<Command> s) {
            input = s;
            System.out.println("setInput");
        }
        public void clear() {
            input = null;
            comStack.clear();
            System.out.println("clearStack");
        }
        Stack<Command> input;
        Stack<Command> comStack;
    }
    private Boolean runThread1;
    private Piece piece1;  //Present piece
    private Piece piece2;  //Next piece
    private int threadIndex;
    private AIThread aiThread;
    private mopUpThread mop;
    private int playerNumber;
    private PlayField pf;
    private GameController gc;
    private DemoManipulator ai;
}

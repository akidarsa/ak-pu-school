/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gpa4;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ee462a5
 */
public class DropTimer extends Thread {
    private int dropCntr;
    private GameController gc;
    private int threadIndex;
    private int playerNum;
    private boolean isRun;
    public DropTimer(GameController gc,int pi,int ti){
        this.gc = gc;
        dropCntr=0;
        threadIndex=ti;
        playerNum=pi;
        isRun=true;
    }
    public void resetTimer(){
        dropCntr=0;
    }

    public void end(){
        isRun=false;
    }
    @Override
    public void run() {
        dropCntr=0;
        while(isRun){
            if (dropCntr>=50){
                try {
                    gc.sendCommand(Command.DOWN, playerNum, threadIndex);
                    //System.out.println("dropTimer: "+ playerNum+" "+threadIndex);
                } catch (IOException ex) {
                    //Logger.getLogger(DropTimer.class.getName()).log(Level.SEVERE, null, ex);
                }
                dropCntr=0;
            }
            try {

                sleep(10);
                dropCntr++;
            } catch (InterruptedException ie) {
            }
        }
    }
}

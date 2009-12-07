/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gpa4.player;

import gpa4.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 *
 * @author Jake
 */
public class KeyboardPlayer extends Player {



    // Also allows for easy construction of first and second player
    public KeyboardPlayer(GameController gc, int pn, int ti){
        this.gc = gc;
        this.playerNumber = pn;

        if (pn == 0){
            this.left = KeyEvent.VK_A;
            this.right = KeyEvent.VK_D;
            this.rotate = KeyEvent.VK_W;
            this.drop = KeyEvent.VK_S;
        }else{    // Overwrites if second player
            this.left = KeyEvent.VK_LEFT;
            this.right = KeyEvent.VK_RIGHT;
            this.rotate = KeyEvent.VK_UP;
            this.drop = KeyEvent.VK_DOWN;
        }
    }

    // set players keystrokes by creating a new one
    public KeyboardPlayer(GameController gc, int pn, int ti,
            int left, int right, int rotate, int drop){
        this.gc = gc;
        threadIndex=ti;
        playerNumber=pn;
        this.left = left;
        this.right = right;
        this.rotate = rotate;
        this.drop = drop;
    }
    public boolean isNetworkPlayer(){return false;}
    public boolean isKeyboardPlayer(){return true;}
    public boolean isAIPlayer(){return false;}

    public boolean acceptNetworkCommand(){return false;}
    public boolean acceptMoveSignal(){


            
        //} catch (IOException ioException) {
         //   ioException.printStackTrace();
        //}
    return false;}
    
    public boolean acceptKeyPress(KeyEvent evt){

        int keycode = evt.getKeyCode();
        //System.out.println("keyPress Player "+ playerNumber);
        if (gc!=null){
            // Must be in play mode so all user input
            if (keycode == left) {
                try {
                    gc.sendCommand(Command.LEFT, playerNumber, threadIndex);
                } catch (IOException ex) {
                    //Logger.getLogger(KeyboardPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (keycode == right) {
                try {
                    gc.sendCommand(Command.RIGHT, playerNumber, threadIndex);
                } catch (IOException ex) {
                    //Logger.getLogger(KeyboardPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (keycode == rotate) {
                try {
                    gc.sendCommand(Command.ROTATE, playerNumber, threadIndex);
                } catch (IOException ex) {
                    //Logger.getLogger(KeyboardPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (keycode == drop) {
                try {
                    gc.sendCommand(Command.DOWN, playerNumber, threadIndex);
                } catch (IOException ex) {
                    //Logger.getLogger(KeyboardPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

    GameController gc;
    private int playerNumber;
    private int threadIndex;
    private int left;
    private int right;
    private int rotate;
    private int drop;
    public String message;
}

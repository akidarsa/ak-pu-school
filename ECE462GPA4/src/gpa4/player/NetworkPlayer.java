/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gpa4.player;

import java.awt.event.KeyEvent;

/**
 *
 * @author Jake
 */
public class NetworkPlayer extends Player {

    public boolean isNetworkPlayer(){return true;}
    public boolean isKeyboardPlayer(){return false;}
    public boolean isAIPlayer(){return false;}
    public boolean acceptKeyPress(KeyEvent evt){return false;}
    public boolean acceptNetworkCommand(){return true;}
    public boolean acceptMoveSignal(){return false;}
}

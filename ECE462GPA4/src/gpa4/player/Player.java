/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gpa4.player;

import java.awt.event.KeyEvent;

/**
 *
 * @author Jake
 * This class is setup to be used by the GameController so it may contain
 * any two forms of input and be able to use those no matter what they may be
 */
public abstract class Player {

    public abstract boolean acceptKeyPress(KeyEvent evt);
    public abstract boolean acceptNetworkCommand();
    public abstract boolean acceptMoveSignal();
    public abstract boolean isNetworkPlayer();
    public abstract boolean isKeyboardPlayer();
    public abstract boolean isAIPlayer();
}

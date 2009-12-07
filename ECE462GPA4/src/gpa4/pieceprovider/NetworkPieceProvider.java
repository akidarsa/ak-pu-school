/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gpa4.pieceprovider;

import gpa4.*;
import java.util.ArrayList;

/**
 *
 * @author Jake
 */
public class NetworkPieceProvider extends PieceProvider {
    public NetworkPieceProvider(){
        queue1 = new ArrayList<Piece>();
        queue2 = new ArrayList<Piece>();
    }

    public Piece nextPiece(int i){
        if (i == 0){
            if (queue1.isEmpty()){
                return null;
            }
            return queue1.remove(0);
        }
        else if (i == 1){
            if (queue2.isEmpty()){
                return null;
            }
            return queue2.remove(0);
        }
        return null;
    }

    public void queuePiece(String string, int playerNumber){
        if (playerNumber == 2){
            queue1.add(new Piece(string));
        }else if(playerNumber ==3){
            queue2.add(new Piece(string));
        }

        
    }

    public int queueSize(int i){
        if (i == 0){
            return queue1.size();
        } else if(i ==1){
            return queue2.size();
        }
        return -1;
    }

    private ArrayList<Piece> queue1;
    private ArrayList<Piece> queue2;
}

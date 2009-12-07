/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author LoH_Zhang_M
 */
/*
 * NextPiecePanel is the panel that draws the next piece.
 *
 */
public class NextPiecePanel extends JPanel {

    public NextPiecePanel(int width, int height) {
        ww = width;
        wh = height;
        setSize(ww, wh);
        setBackground(Color.white);
        idle=false;
    }

    public void SetPiece(Piece inPiece) {
        piece = new Piece(inPiece);
    }

    public void setIdle(boolean s) {
        idle=s;
    }

    @Override
    public void paintComponent(Graphics gfx) {
        super.paintComponent(gfx);
        gfx.setColor(Color.WHITE);
        gfx.fillRect(0, 0, ww, wh);
        if (piece != null && idle==false) {
            for (int i = 0; i < piece.size(); i++) {
                int[] tmparray = new int[2];
                tmparray = (int[]) piece.get(i);
                gfx.setColor(Color.green);
                gfx.fillRect(15 + tmparray[0] * 10, 30 + tmparray[1] * 10, 10, 10);
                gfx.setColor(Color.black);
                gfx.drawRect(15 + tmparray[0] * 10, 30 + tmparray[1] * 10, 10, 10);
            }
        }
    }
    private int ww, wh;
    private Piece piece;
    private boolean idle;
}

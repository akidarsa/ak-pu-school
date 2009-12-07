/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.SynchronousQueue;
import javax.swing.JPanel;

/**
 *
 * @author LoH_Zhang_M
 */
/*
 * PlayField is the Panel which draws the tetrix game and handles every aspect
 * of the game
 */
public class PlayField extends JPanel {

    @SuppressWarnings("unchecked")
    public PlayField(int width, int height, int pn, String Filename) {
        ww = width;
        wh = height;
        playerNum=pn;
        pcounter = 0;
        square4 = 0;
        square5 = 0;
        square6 = 0;
        square7 = 0;
        squareTotal = 0;
        gameover=false;
        setSize(ww, wh);
        pieceWidth = ww / 20;
        pieceHeight = wh / 30;
        gf = new GameField();
        setBackground(Color.white);
        idle=false;
        //nxtPiecePanel = nextPsPanel;
        //nxtPiecePanel.SetPiece(fr.getPiece(1));
        iniPaintDrop();
    }

    public PlayField(PlayField oldField) {
        ww = oldField.ww;
        wh = oldField.wh;
        playerNum=oldField.playerNum;
        pcounter = oldField.pcounter;
        square4 = oldField.square4;
        square5 = oldField.square5;
        square6 = oldField.square6;
        square7 = oldField.square7;
        squareTotal = oldField.squareTotal;
        gameover=oldField.gameover;
        idle=oldField.idle;
        setSize(ww, wh);
        pieceWidth = ww / 20;
        pieceHeight = wh / 30;
        gf = new GameField(oldField.gf);
        setBackground(Color.white);
        //boardList.add(fr.getPiece(0));
        //nxtPiecePanel = new NextPiecePanel(20, 20);
        //nxtPiecePanel.SetPiece(fr.getPiece(1));

        tPiece = new Piece(oldField.tPiece);
        nPiece = new Piece(oldField.nPiece);

        gc = oldField.gc;
        paintPosX = oldField.paintPosX;
        paintPosY = oldField.paintPosY;
        linesRemoved = oldField.linesRemoved;
        score = oldField.score;
    }

    public void setGameController(GameController gc) {
        this.gc = gc;
    }

    public boolean makeMove(Command command) {
        if (gameover == false) {
            if (command == Command.DOWN) {
                return moveDown();
            } else if (command == Command.LEFT) {
                return moveLeft();
            } else if (command == Command.RIGHT) {
                return moveRight();
            } else if (command == Command.ROTATE) {
                return rotateLeft();
            }
        }
        return false;
    }

    public void setPiece(Piece inPiece) {
        tPiece = new Piece(inPiece);
        ++pcounter;
        getSquares();
    }

    public void setNextPiece(Piece inPiece) {
        nPiece = new Piece(inPiece);
    }

    private void getSquares() {
        int numSquare = tPiece.size();
        if (numSquare == 4) {
            ++square4;
            squareTotal += 4;
        }
        if (numSquare == 5) {
            ++square5;
            squareTotal += 5;
        }
        if (numSquare == 6) {
            ++square6;
            squareTotal += 6;
        }
        if (numSquare == 7) {
            ++square7;
            squareTotal += 7;
        }
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int getPieceCount() {
        return pcounter;
    }

    public int getSquare4() {
        return square4;
    }

    public int getSquareTotal() {
        return squareTotal;
    }

    public int getSquare5() {
        return square5;
    }

    public int getSquare6() {
        return square6;
    }

    public int getSquare7() {
        return square7;
    }

    public int getScore() {
        return score;
    }

    public void setIdle(boolean i) {
        idle = i;
    }

    @Override
    public void paintComponent(Graphics gfx) {
        if (playerNum!=-1){
        gfx.setColor(Color.BLACK);
        gfx.fillRect(0, 0, ww, wh);
        //if(pieceIdx < fr.getSize())
        //{
        if (tPiece != null && idle==false) {
            for (int j = 0; j < tPiece.size(); j++) {
                int[] tmparray = new int[2];
                tmparray = (int[]) tPiece.get(j);
                gfx.setColor(tPiece.pieceColor);
                gfx.fillRect(paintPosX + tmparray[0] * pieceWidth, paintPosY + tmparray[1] * pieceHeight, pieceWidth, pieceHeight);
                gfx.setColor(tPiece.pieceBorderColor);
                gfx.drawRect(paintPosX + tmparray[0] * pieceWidth, paintPosY + tmparray[1] * pieceHeight, pieceWidth, pieceHeight);
            }
            for (int i = 0; i < gf.size(); i++) {
                int[] tmparray = new int[2];
                tmparray[0] = gf.getX(i);
                tmparray[1] = gf.getY(i);

                gfx.setColor(tPiece.pieceColor);
                gfx.fillRect(tmparray[0] * pieceWidth, tmparray[1] * pieceHeight, pieceWidth, pieceHeight);
                gfx.setColor(tPiece.pieceBorderColor);
                gfx.drawRect(tmparray[0] * pieceWidth, tmparray[1] * pieceHeight, pieceWidth, pieceHeight);
            }
        }
        }
        //if(pieceIdx != fr.getSize() - 1)
        //{
        //nxtPiecePanel.SetPiece(fr.getPiece((pieceIdx + 1)));
        //}

        //}
    }

    private void iniPaintDrop() {
        if (tPiece!=null){
            paintPosX=((20-tPiece.width())/2/*-(tPiece.width()%2)*/)*pieceWidth;
        }else
            paintPosX = ww / 2;
        paintPosY = 0;
        square4 = 0;
        square5 = 0;
        square6 = 0;
        square7 = 0;
        gameover=false;
        squareTotal = 0;
        pcounter = 0;
        linesRemoved = 0;
        score = 0;
        //pieceIdx = rand.nextInt(fr.getSize());
        //tPiece=new Piece(fr.pieceList.get(pieceIdx));
        //nxtPieceIdx = rand.nextInt(fr.getSize());
        //nxtPiecePanel.SetPiece(fr.pieceList.get(nxtPieceIdx));
    }

    public boolean rotateRight() {
        int prevX = paintPosX;
        int prevY = paintPosY;
        tPiece.rotateRight();
        while (paintPosX < 0) {
            paintPosX += pieceWidth;
        }
        while (paintPosX + tPiece.MaxX() * pieceWidth + 10 > ww) {
            paintPosX -= pieceWidth;
        }
        boolean c = checkMove();
        if (c == true) {
            tPiece.rotateLeft();
            paintPosX = prevX;
            paintPosY = prevY;
        }
        return !c;
    }

    public boolean rotateLeft() {
        int prevX = paintPosX;
        int prevY = paintPosY;
        tPiece.rotateLeft();
        while (paintPosX < 0) {
            paintPosX += pieceWidth;
        }
        while (paintPosX + tPiece.MaxX() * pieceWidth + 10 > ww) {
            paintPosX -= pieceWidth;
        }
        boolean c = checkMove();
        if (c == true) {
            tPiece.rotateRight();
            paintPosX = prevX;
            paintPosY = prevY;
        }
        return !c;
    }

    public boolean moveLeft() {
        boolean checkBounds = false;
        // System.out.println(tPiece.MinX());
        //System.out.println("moveLeft");
        if (paintPosX + tPiece.MinX() * pieceWidth > 0) {
            checkBounds = true;
            //System.out.println(paintPosX);
            paintPosX -= pieceWidth;
            //System.out.println(paintPosX);
        }
        boolean c = checkMove();
        if (c == true) {
            paintPosX += pieceWidth;
        }
        return !c && checkBounds;
    }

    public boolean moveRight() {
        boolean checkBounds = false;
        //System.out.println("moveRight");
        if (paintPosX + tPiece.MaxX() * pieceWidth + 10 < ww - 10) {
            checkBounds = true;
            paintPosX += pieceWidth;
        }
        boolean c = checkMove();
        if (c == true) {
            paintPosX -= pieceWidth;
        }
        return !c && checkBounds;
    }

    public boolean moveDown() {
        //System.out.println("moveDown");
        paintPosY += pieceHeight;
        boolean c = checkMove();
        //if (c == false || tPiece.MaxY() >= 30) {
            paintPosY -= pieceHeight;
            updatePaintPos();
            if (playerNum!=-1)
                gc.resetDropTimer(playerNum);
        //}
        return !c;
    }
    public void setPlayerNum(int i){
        playerNum=i;
    }
    public void updatePaintPos() {
        
        //run the line queue
        
        paintPosY += pieceHeight;
        boolean c = checkMove();
        int lns = 0;
        if (c == true && gameover==false) {
            gf.addPiece(tPiece, (paintPosX / pieceWidth), (paintPosY / pieceHeight));
            paintPosY = 0;
            if (playerNum!=-1){
                gc.takeLines(playerNum);
            }
            lns = gf.checkLines();
            recentLinesRemoved = lns;
            if (lns > 0 && playerNum!=-1) {
                sendRemovedLines();
            }
            linesRemoved += lns;
            score += lns * lns * 100;
            setPiece(nPiece);
            if (tPiece!=null){
                paintPosX=((20-tPiece.width())/2/*-(tPiece.width()%2)*/)*pieceWidth;
            }else
                paintPosX = ww / 2;
            gf.savePrev();
            c = checkMove();
            if (c == true) {
                gc.setGameover(playerNum); //returns -1 if gameover
                gameover=true;
            }
            if (playerNum!=-1) {
                gc.getNextPiece(playerNum);
            }
            //System.out.println("Playfield New Piece");
            
        }
        repaint();

    }

    public int getRecentLinesRemoved() {
        return recentLinesRemoved;
    }

    public void sendRemovedLines() {
       LinkedList<String> temp = gf.getLinesRemoved();
       //System.out.println("sendRemovedLines player: "+playerNum);
        if (temp != null) {
            gc.queueLines(playerNum, temp);
        }
    }
    public void addLine(String line) {
        gf.addLine(line);
        updatePaintPos();
        paintPosY -= pieceHeight;
        repaint();
    }
    public boolean checkMove() {
        boolean chk = false;
        //System.out.println("checkmove:");
        //gf.printCoor();
        for (int i = 0; i < tPiece.size(); i++) {
            int[] tmpcoor = new int[2];
            tmpcoor = (int[]) tPiece.get(i);
            //System.out.print(tmpcoor[0]+" "+tmpcoor[1]);
            //System.out.print((tmpcoor[0]+(paintPosX/tPiece.pieceWidth)) +"," +(tmpcoor[1]+(paintPosY/tPiece.pieceHeight))+" ");
            //gf.printCoor();
            chk = (gf.unavailCoor((tmpcoor[0] + (paintPosX / pieceWidth)), (tmpcoor[1] + (paintPosY / pieceHeight))));
            //System.out.println(check);
            if (chk == true) {
                break;
            }
        }
        if (paintPosY + tPiece.MaxY() * pieceHeight + pieceHeight > wh) {
            chk = true;
        }
        return chk;

    }
    /*
    public int scoreHeight() {

    // Needs to search through all points in GameField gf and return
    //  height of highest block.

    return gf.highestBlock();
    }
     */

    public int scoreHoles() {

        return gf.numberHoles();
    }

    public int scoreDepth() {
        return gf.holeDepth();
    }

    public int scoreRoughness() {

        return gf.getRoughness();
    }

    public int checkLines() {
        return gf.checkLines();
    }

    public void startGame() {
        gameover=false;
        gf.clear();
        //System.out.println(tPiece.width());
        iniPaintDrop();
    }

    public Piece getPresentPiece() {
        return tPiece;
    }
    public Piece getNextPiece() {
        return nPiece;
    }
    private GameController gc;
    private int ww;
    private int wh;
    private int pieceHeight;
    private int pieceWidth;
    //private int pieceIdx, nxtPieceIdx;
    private int paintPosX;
    private int paintPosY;
    private Piece tPiece;
    private Piece nPiece;
    //private NextPiecePanel nxtPiecePanel;
    private GameField gf;
    private boolean gameover;
    private int pcounter;
    private int square4;
    private int square5;
    private int square6;
    private int square7;
    private int squareTotal;
    private int linesRemoved;
    private int recentLinesRemoved;
    private int score;
    private int playerNum;
    private boolean idle;
}

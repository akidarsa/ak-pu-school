/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4;
import gpa4.pieceprovider.*;
import gpa4.player.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author neuen
 */
public class GameController extends Thread {

    public GameController(PlayField p1, PlayField p2, NextPiecePanel np1,
            NextPiecePanel np2, DialogPanel dp1, String Filename) {

        filename = Filename;
        pf = new PlayField[2];
        npp = new NextPiecePanel[2];
        dt = new DropTimer[2];
        dp=dp1;
        pf[0] = p1;
        pf[1] = p2;
        npp[0] = np1;
        npp[1] = np2;
        currPiece = new Piece[2];
        nextPiece = new Piece[2];
        pf[0].setGameController(this);
        pf[1].setGameController(this);
        lineQueue = new LinkedList[2];
        lineQueue[0]= new LinkedList<String>();
        lineQueue[1]= new LinkedList<String>();
        commandQueue = new LinkedList[2];
        commandQueue[0]= new LinkedList<Command>();
        commandQueue[1]= new LinkedList<Command>();

        //tetrisMusic = new SoundPlayer("/fonky.wav");//"/tetrisb.mid");
        //tetrisMusic.playStartupSound();
    }
    /*
     * Gametypes are:
     *  1 = Single player
     *  2 = Two player both using keyboard
     *  3 = Two player 1 player 1 ai
     *  4 = Two player networking
     *  5 = qualifier
     */
    // See GameState enum for details
    private SoundPlayer tetrisMusic;
    private final Boolean repeat = false;
    private DialogPanel dp;
    private Player player1;
    private Player player2;
    private DropTimer[] dt;
    private PieceProvider pieceProvider;
    private String filename;
    private GameState gameType;
    private PlayField[] pf;
    private NextPiecePanel[] npp;
    private Piece[] currPiece;
    private Piece[] nextPiece;
    private boolean isPaused;
    private boolean isDemo;
    private int gameover = -1;
    private int currThreadIndex=-1;
    private final int stdDelay = 10;
    private NetworkPieceProvider networkPieceProvider;
    private LinkedList<String>[] lineQueue;
    private LinkedList<Command>[] commandQueue;
    private Client client;
    private String hostName;
    private UserInterface uif;
    private boolean winner;

    public void setupNewGame(GameState newState) {

        gameType=newState;
        lineQueue[0].clear();
        lineQueue[1].clear();
        commandQueue[0].clear();
        commandQueue[1].clear();
        if (newState == GameState.VersusNetwork || newState == GameState.Qualifier ||
                newState == GameState.DemoVs) {
            networkPieceProvider = new NetworkPieceProvider();
            pieceProvider = networkPieceProvider;
            if (client!=null) client.end();
            client = new Client(this, networkPieceProvider);
            uif = new UserInterface(this, newState);
            //uif.setVisible(true);
            uif.show();
        } else if (filename == null || filename.compareTo("pieces.bin") == 0) {
            pieceProvider = new RandomPieceProvider();
            initiateGame(newState);
        } else {
            pieceProvider = new FilePieceProvider(filename);
            initiateGame(newState);
        }

    }

    public void awaitResponse(GameState gameState){
        hostName = uif.getHostName();
        if (!client.startConnection(hostName, gameState)){
            System.out.println("Network connection failed.");
        }
    }

    public void initiateGame(GameState newState){

        if (newState == GameState.VersusNetwork || newState == GameState.Qualifier) {
           
        }
        currThreadIndex++;
        isPaused=false;
        winner=false;
        gameover=-1;
        switch (newState) {
            case Welcome:
                break;
            case GameOver:
                break;
            case Single:
                player1 = new KeyboardPlayer(this, 0,currThreadIndex,dp.getKeyLeft(),
                        dp.getKeyRight(),dp.getKeyRotate(),dp.getKeyDown());
                player2 = null;
                break;
            case DemoMode:
                player1 = new AIPlayer(this, pf[0], 0,currThreadIndex);
                player2 = null;
                break;
            case DemoVs:
                player1 = new AIPlayer(this, pf[0], 0,currThreadIndex);
                player2 = new NetworkPlayer();
                break;
            case VersusPlayer:
                player1 = new KeyboardPlayer(this, 0,currThreadIndex,dp.getKeyLeft(),
                        dp.getKeyRight(),dp.getKeyRotate(),dp.getKeyDown());
                player2 = new KeyboardPlayer(this, 1,currThreadIndex,dp.getKeyLeft1(),
                        dp.getKeyRight1(),dp.getKeyRotate1(),dp.getKeyDown1());
                break;
            case VersusComputer:

                player1 = new KeyboardPlayer(this, 0,currThreadIndex,dp.getKeyLeft(),
                        dp.getKeyRight(),dp.getKeyRotate(),dp.getKeyDown());
                player2 = new AIPlayer(this, pf[1], 1,currThreadIndex);
                break;
            case VersusNetwork:

                player1 = new KeyboardPlayer(this, 0,currThreadIndex,dp.getKeyLeft(),
                        dp.getKeyRight(),dp.getKeyRotate(),dp.getKeyDown());
                player2 = new NetworkPlayer();
                break;
            case Qualifier:

                player1 = new AIPlayer(this, pf[0], 0,currThreadIndex);
                player2 = null; //and when null, no lines should be transferred
                break;
        }

        if (player1!=null){
            if (dt[0]!=null && dt[0].isAlive()) dt[0].end();
            if (player1.isNetworkPlayer()==false && newState!=GameState.Qualifier &&
                    newState!=GameState.VersusNetwork && newState!=GameState.DemoVs){
                dt[0]= new DropTimer(this,0,currThreadIndex);
                dt[0].start();
            }
            nextPiece[0]=pieceProvider.nextPiece(0);
            pf[0].setPiece(nextPiece[0]);
            getNextPiece(0);
            pf[0].setIdle(false);
            npp[0].setIdle(false);
            pf[0].repaint();
            npp[0].repaint();
            pf[0].startGame();
            //player1.acceptMoveSignal();
        }else{
            pf[0].setIdle(true);
            npp[0].setIdle(true);
            pf[0].repaint();
            npp[0].repaint();
        }
        if (player2!=null){
            if (dt[1]!=null && dt[1].isAlive()) dt[1].end();
            if (player2.isNetworkPlayer()==false){
                dt[1]= new DropTimer(this,1,currThreadIndex);
                dt[1].start();
            }
            nextPiece[1]=pieceProvider.nextPiece(1);
            pf[1].setPiece(nextPiece[1]);
            getNextPiece(1);
            pf[1].setIdle(false);
            npp[1].setIdle(false);
            npp[1].repaint();
            pf[1].repaint();
            pf[1].startGame();
            //player2.acceptMoveSignal();
        }else{
            pf[1].setIdle(true);
            npp[1].setIdle(true);
            pf[1].repaint();
            npp[1].repaint();
        }
    }
    
    public boolean sendCommand(Command c, int i, int threadIndex) throws IOException {

        if (gameType == GameState.VersusNetwork || gameType == GameState.Qualifier ||
                gameType==GameState.DemoVs){
            if (i == 0 || i == 1){
                // call to client
                client.sendCommand(c);
            }
            if (i== 2 || i == 3){
                //System.out.println("  -client executed:" + c);
                //pf[i-2].makeMove(c);
                commandQueue[i-2].add(c);
            }
            return true;
        }
        else if (threadIndex!=currThreadIndex) {
            return true;
        }
        else{
            if (isPaused==false){
                 //pf[i].makeMove(c);
                 commandQueue[i].add(c);
                 return true;
            }
        }
        return false;
    }
    private void executeCommands(int i) {
        try {
        while (!commandQueue[i].isEmpty()){
            //System.out.println("executeCommands: "+commandQueue[i].isEmpty());
            pf[i].makeMove(commandQueue[i].remove());
        }
        }catch (java.util.NoSuchElementException e){

        }
    }
    
    public void resetDropTimer(int i){
        if (isPaused==false && dt[i]!=null){
            dt[i].resetTimer();
        }
    }

    public void keyPress(KeyEvent evt){
        if (player1!=null && isPaused==false) {
            //System.out.println("keyPress Player 1");
            player1.acceptKeyPress(evt);
            pf[0].repaint();
        }
        if (player2!=null && isPaused==false) {
            //System.out.println("keyPress Player 2 ");
            player2.acceptKeyPress(evt);
            pf[1].repaint();
        }
    }

    public boolean getPaused() {
        return isPaused;
    }

    public boolean getDemo() {
        return isDemo;
    }
    
    public void getNextPiece(int i) {
        currPiece[i] = nextPiece[i];
        do {nextPiece[i] = pieceProvider.nextPiece(i);} while(nextPiece[i] == null);
        pf[i].setNextPiece(nextPiece[i]);
        npp[i].SetPiece(nextPiece[i]);
        if (player1!=null && i==0) player1.acceptMoveSignal();
        if (player2!=null && i==1) player2.acceptMoveSignal();
        npp[i].repaint();
    }
    public void queueLines(int playerNumber,LinkedList<String> lines){
        int i;
        if (playerNumber==1) i=0;
        else i=1;
        if (gameType==GameState.VersusNetwork || gameType==GameState.Qualifier ||
                gameType==GameState.DemoVs){
            if (playerNumber>1){
                lineQueue[playerNumber-2].addAll(lines);
            }
        }else{
            lineQueue[i].addAll(lines);
        }
    }
    public void queueLine(int playerNumber,String line){
        int i;
        if (playerNumber==1) i=0;
        else i=1;
        if (gameType==GameState.VersusNetwork || gameType==GameState.Qualifier
                || gameType == GameState.DemoVs){
            if (playerNumber>1){
                lineQueue[playerNumber-2].add(line);
            }
        }else{
            lineQueue[i].add(line);
        }
    }
    public void takeLines(int playerNumber){
        //System.out.println("takeLines: "+playerNumber+" "+lineQueue[playerNumber].size());
        //TODO insert network attack request
        while (lineQueue[playerNumber].isEmpty()==false){

            pf[playerNumber].addLine(lineQueue[playerNumber].remove());
        }
    }
    public void setGameover(int pn){
        
        if (pn==0){
            if (gameover==1) gameover=2;
            else gameover=0;
            commandQueue[0].clear();
            if (dt[1]!=null)
                dt[0].end();
        }else if (pn==1){
            if (gameover==0) gameover=2;
            else gameover=1;
            commandQueue[1].clear();
            gameover=1;
            if (dt[1]!=null)
                dt[1].end();
        }
        if (gameover==2){
            if (pf[0].getScore()>pf[1].getScore()){
                winner=true;
            }else winner=false;
        }
        System.out.println("setGameover: "+pn +" "+gameover);
    }
    public int getGameover() {
        //returns -1 if the game is not over and
        //the corresponding int of the loser and 2 if its a tie
        return gameover;
    }
    public void setWinner(boolean win){
        winner=win;
    }
    public boolean getWinner(){
        return winner;
    }
    public GameState getGameType(){
        return gameType;
    }
    public void setPaused(boolean pause) {
        //pause/unpause me
        isPaused = pause;
    }
    @Override
    public void run() {
        while (true) {
                //if (gameType==GameState.Qualifier||gameType==GameState.VersusNetwork ||
                  //      gameType==GameState.DemoVs){
                    executeCommands(0);
                    executeCommands(1);
                //}
                if (player1!=null){
                    
                    pf[0].repaint();
                    //rtn1 = pf[0].updatePaintPos();
                    //player1.acceptMoveSignal();
                }                
                if (player2!=null) {
                    
                    pf[1].repaint();
                    //rtn2 = pf[1].updatePaintPos();
                    //player2.acceptMoveSignal();
                }
                if (player1!=null){
                    npp[0].repaint();
                }
                if (player2!=null){
                    npp[1].repaint();
                }


                try {
                    if (stdDelay > 0) {
                        sleep(stdDelay);
                    }
                } catch (InterruptedException ie) {
                }            

            
        }
    }



}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import java.util.Date;

/**
 *
 * @author ee462b29-Jacob Wyant
 */

/* The idea
 * -Move piece into place on board in all possible combinations
 * -Calculate a 'score' of each move based on the state of the board
 * -Return a collection of commands to put the piece into that position
 */

/*
 * DemoManipulator is the demo player class that calculates and moves pieces
 * based on above idea when the demo menu item is activated and until the play
 * button is activated.
 */
public class DemoManipulator {

    static private int presentModifier = 0;
    static private int presentAverage = 0;
    static private int bestAverage = 0;
    static private int games = 0;
    static private int dif = 1;
    static private int total = 0;
    static private int linesRemovedLastRun = 0;
    static private int linesRemovedThisPiece = 0;
    static private Boolean changed = false;

    static private int lineRemovalModifier = 114;  //11  //112 Best:22, 924
    static private int holeNumberModifier = 120;     //15  //120
    static private int holeDepthModifier = 31;                //30
    static private int fallDistanceModifier = 42;      //4     //41
    static private int boardRoughnessModifier = 52;      //5    //52
    final private Boolean debug = false;
    final private Boolean testing = false;

    private void alterMod(int mod, int dif){
        switch (mod){
            case 0:
                lineRemovalModifier += dif;
                break;
            case 1:
                holeNumberModifier += dif;
                break;
            case 2:
                holeDepthModifier += dif;
                break;
            case 3:
                fallDistanceModifier += dif;
                break;
            case 4:
                boardRoughnessModifier += dif;
                break;
        }
    }

    public DemoManipulator() {

        if (!testing){
            return;
        }
        System.out.println("linesRemovedLastRun " + linesRemovedLastRun);
        System.out.println("Modifiers: " + lineRemovalModifier + " "
                + holeNumberModifier + " " + holeDepthModifier + " "
                + fallDistanceModifier + " " + boardRoughnessModifier);
        total += linesRemovedLastRun;
        linesRemovedLastRun = 0;
        int gamesInAverage = 3;
        if (games < gamesInAverage){
            games++;
            return;
        }

        int average = total/games;
        total = 0;
        games = 1;
        if (!changed){
            bestAverage = average;
            alterMod(presentModifier, dif);
            changed = true;
            return;
        }
        else{
            presentAverage = average;
        }

        // if ready to move to a new mod
        Boolean presentWon = true;
        if (presentAverage < bestAverage){
            presentWon = false;
            alterMod(presentModifier, -dif);  //present lost, change back
        }else{
            bestAverage = presentAverage;
        }

        try {
            // Create file
            FileWriter fstream = new FileWriter("LOG.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);

            int delta = presentAverage-bestAverage;
            if (presentWon){
                out.write("Cur won, average: " + presentAverage
                    + " dif: " + delta);
            }else{
                delta = -delta;
                out.write("Best won, average: " + bestAverage
                    + " dif: " + delta);
            }
            out.write(" Winning Modifiers: " +lineRemovalModifier + " "
                                     + holeNumberModifier + " "
                                     + holeDepthModifier + " "
                                     + fallDistanceModifier + " "
                                     + boardRoughnessModifier + "\n");

            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        presentModifier++;
        if (presentModifier == 5){
            presentModifier = 0;
            dif = dif * -1;
        }

        alterMod(presentModifier, dif);  // change next mod
    }

    public Stack<Command> calculateMove(Piece piece, PlayField playField) {
        return calculateMove(piece, piece, playField, false);
    }

    public Stack<Command> calculateMove(Piece piece1, Piece piece2,
            PlayField playField) {
        return calculateMove(piece1, piece2, playField, true);
    }

    private Stack<Command> calculateMove(Piece piece1, Piece piece2,
            PlayField playFieldArg, boolean lookAhead) {

        Date date = new Date();
        //long t1 = date.getTime();
        date.setTime(0);

        PlayField playField = new PlayField(playFieldArg);
        playField.setPlayerNum(-1);
        PlayField stableField = new PlayField(playField);
        Stack<Command> moves = new Stack<Command>();
        Stack<Command> best = new Stack<Command>();
        Stack<Command> temp = new Stack<Command>();
        int min = 0;

        //add isSymmetric() to Piece to reduce number of loops
        int variance = 4; //piece.isSymmetric()? 2: 4;
        int score = 0;
        int downPresses = 0;

        if (debug) {
            for (int i = 0; i < 10; i++) {
                System.out.println();
            }
            System.out.println("New Piece"); // <---------------------------------
        }

        for (int i = 0; i < variance; i++) {

            if (debug) {
                System.out.println("Rotation #" + i); // <-----------------------------
            }
            int counter = 0;
            //Setup by pushing piece to the left
            while (stableField.makeMove(Command.LEFT)) {
                moves.push(Command.LEFT);
            }

            //Run through loop of all moves
            do {
                downPresses = 1;
                counter++;
                playField = new PlayField(stableField);
                score = 0;
                //Drop
                while (playField.makeMove(Command.DOWN)) {
                    downPresses++;
                }
                //playField.updatePaintPos();

                int lines = playField.getRecentLinesRemoved();

                // Score the present field
                int lr = lines * -lineRemovalModifier;
                int hn = playField.scoreHoles() * holeNumberModifier;
                int hd = playField.scoreDepth() * holeDepthModifier;
                int br = playField.scoreRoughness() * boardRoughnessModifier;
                int ph = downPresses * -fallDistanceModifier;
                //System.out.println("        Score: " + score); //<-------------------

                score = lr + hn + br + ph;

                // Look ahead if the second piece is known
                if (lookAhead) {
                    calculateMove(piece2, piece2, playField, false);
                    score += lookAheadScore;  //Careful, utilizing side effect
                }

                //Store the commands if present placement is best
                if (score < min || best.isEmpty()) {
                    if (debug) {
                        System.out.println("              ----New Min----"); // <--------
                    }
                    linesRemovedThisPiece = lines;  // <----------------------------
                    min = score;
                    best = (Stack<Command>) moves.clone();
                    for (int j = 0; j < downPresses; j++) {
                        best.push(Command.DOWN);
                    }
                }

                //Wiggle? (shift piece when at bottom)

                //Setup for next run
                if (moves.isEmpty()) {
                    moves.push(Command.RIGHT);
                    if (debug) {
                        System.out.println("  Counter: " + counter + " Score: " + "lr: " + lr + " hn: " + hn + " br: " + br + " total: " + score + " Pushed Right"); //<-------------------
                    }
                } else if (moves.peek() == Command.ROTATE || moves.peek() == Command.RIGHT) {
                    moves.push(Command.RIGHT);
                    if (debug) {
                        System.out.println("  Counter: " + counter + " Score: " + "lr: " + lr + " hn: " + hn + " br: " + br + " total: " + score + " Pushed Right"); //<-------------------
                    }
                } else {
                    moves.pop();
                    if (debug) {
                        System.out.println("  Counter: " + counter + " Score: " + "lr: " + lr + " hn: " + hn + " br: " + br + " total: " + score + " Popped Left"); //<-------------------
                    }
                }
            } while (stableField.makeMove(Command.RIGHT));

            if (debug) {
                System.out.println("  looked through " + counter + " columns");   //<----------
            }
            if(!moves.isEmpty()){
                moves.pop(); // Pop off last (failed) move right
            }

            //Reset the field
            while (!moves.isEmpty()) {
                if (moves.peek() == Command.ROTATE) {
                    break;
                }
                moves.pop();
                stableField.makeMove(Command.LEFT);
            }

            //Rotate and repeat
            stableField.makeMove(Command.ROTATE);
            moves.push(Command.ROTATE);
        }
        linesRemovedLastRun += linesRemovedThisPiece; //<-------------------------------

        if (lookAhead) {
            lookAheadScore = min; // side effect for use in overload
        }
        Stack<Command> output = new Stack<Command>();
        while (!best.isEmpty()) {
            output.push(best.pop());//invert for ease of use
        }

        long t2 = date.getTime();
        //long dif = t2 - t1;
        //System.out.println(t2);

        return output;
    }

    /*
    public Move getNextMove() {
        Move nextMove;
        if (isManipulating) {
            nextMove = Move.LEFT;
        } else {
            nextMove = Move.NONE;
        }
        return nextMove;
    }
    private void getEdgeBlockSizeAndOrient() {
    int[] tmpL = {getEdgeBlockSize(p.MinCoorX(), 0), 1};
    int[] tmpR = {getEdgeBlockSize(p.MaxCoorX(), 0), 2};
    int[] tmpT = {getEdgeBlockSize(p.MinCoorY(), 1), 0};
    int[] tmpB = {getEdgeBlockSize(p.MaxCoorY(), 1), 3};
    sizeOrientList.add(tmpL);
    sizeOrientList.add(tmpR);
    sizeOrientList.add(tmpT);
    sizeOrientList.add(tmpB);
    }
     

    private int getEdgeBlockSize(int comparator, int xy) {
        int xyOpp = Math.abs(xy - 1);
        int min = 9999;
        int max = 0;
        for (int i = 0; i < p.size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) p.get(i);
            if (squareCoor[xy] == comparator) {
                if (squareCoor[xyOpp] > max) {
                    max = squareCoor[xyOpp];
                }
                if (squareCoor[xyOpp] < min) {
                    min = squareCoor[1];
                }
            }
        }
        return (max - min + 1);
    }
*/
    public enum Move {

        LEFT, RIGHT, DOWN, ROTATE, NONE
    }

    /*
    Orient: TOP = 0, LEFT = 1, RIGHT = 2, BOTTOM = 3
     
    private boolean isManipulating;
    private ArrayList<Move> moveSequence;
    private ArrayList<int[]> sizeOrientList;
    private int moveListIndex;
    private int score1;
    private int score2; */
    private Piece p;
    private int lookAheadScore;
    private GameField gf;
}

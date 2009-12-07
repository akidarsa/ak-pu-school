/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This class is a class which describes a tetrix Piece.
 * @author ee462a28
 */
public class Piece {

    private class Coordinate {

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
        private int x;
        private int y;
    }
    private ArrayList<Coordinate> coorList;
    private int linelength;
    private ArrayList<Coordinate> genList;
    private ArrayList<Coordinate> rrList;
    private ArrayList<Coordinate> rlList;
    public Color pieceColor;
    public Color pieceBorderColor;

    public Piece(String s) {
        String str = s;
        coorList = new ArrayList<Coordinate>();
        makePiece(str);
        pieceColor = Color.green;
        pieceBorderColor = Color.black;
    }

    public Piece(Piece p) {
        coorList = new ArrayList<Coordinate>();
        makePiece(p);
        pieceColor = Color.green;
        pieceBorderColor = Color.black;
    }

    private void makePiece(String str) {
        if (str.length() == 16) {
            linelength = 4;
        }
        if (str.length() == 25) {
            linelength = 5;
        }
        if (str.length() == 36) {
            linelength = 6;
        }
        if (str.length() == 49) {
            linelength = 7;
        }
        for (int i = 0; i < str.length(); i += linelength) {

            for (int j = 0; j < linelength; j++) {
                char c = str.toCharArray()[i + j];
                int xCoor = j;
                int yCoor = i / linelength;
                // System.out.print(c);
                if (c == '1') {
                    Coordinate coord = new Coordinate(xCoor, yCoor);
                    //System.out.print(Coor[0]+" "+Coor[1]+", ");
                    coorList.add(coord);
                    //System.out.print("{"+xCoor+","+yCoor+"}");
                    }
            }
        }
    }

    public void makePiece(Piece p) {
        if (p==null) return;
        coorList.clear();
        //System.out.print(p);
        linelength = p.size();
        for (int i = 0; i < p.size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) p.get(i);
            //System.out.println(squareCoor[0]+","+squareCoor[1]);
            Coordinate coord = new Coordinate(squareCoor[0], squareCoor[1]);
            coorList.add(coord);
        }
        //System.out.print("out\n"+toString());
    }

    public String toString() {

        String s = "";
        for (int i = 0; i < size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) get(i);
            s = s + squareCoor[0] + "," + squareCoor[1] + "\n";
        }
        return s;
    }

    public int size() {
        return linelength;
    }
    public int width(){
        int x=0;
        for (int i=0; i<size(); i++){
            if (get(i)[0]>x){
                x=get(i)[0];
            }
        }
        return x+1;
    }
    public int[] get(int i) {
        int[] array = new int[2];
        if (i < coorList.size()) {
            Coordinate c = coorList.get(i);
            array[0] = c.getX();
            array[1] = c.getY();
        } else {
            array[0] = -Short.MAX_VALUE;
            array[1] = -Short.MAX_VALUE;
        }
        return array;
    }

    public void rotateRight() {
        int tmpx = 0;
        int tmpy = 0;
        rrList = new ArrayList<Coordinate>();
        for (int i = 0; i < size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = get(i);
            tmpx = squareCoor[0];
            tmpy = squareCoor[1];
            squareCoor[0] = tmpy;
            squareCoor[1] = size() - tmpx;
            Coordinate coord = new Coordinate(squareCoor[0], squareCoor[1]);
            rrList.add(coord);
        }
        coorList = rrList;
        generalize();
    }

    public void rotateLeft() {
        int tmpx = 0;
        int tmpy = 0;
        rlList = new ArrayList<Coordinate>();
        for (int i = 0; i < size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) get(i);
            tmpx = squareCoor[0];
            tmpy = squareCoor[1];
            squareCoor[0] = size() - tmpy;
            squareCoor[1] = tmpx;
            Coordinate coord = new Coordinate(squareCoor[0], squareCoor[1]);
            rlList.add(coord);
        }
        coorList=rlList;
        generalize();
    }

    public void generalize() {
        //moves to the top left hand corner of the coordinate system
        int xDiff = MinX();
        int yDiff = MinY();
        genList = new ArrayList<Coordinate>();
        for (int i = 0; i < size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) get(i);
            Coordinate coord = new Coordinate(squareCoor[0] - xDiff, squareCoor[1] - yDiff);
            genList.add(coord);
        }
        coorList = genList;
    }

    public int MaxX() {
        int maxCoorX = 0;
        //System.out.println(size());
        for (int i = 0; i < this.size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) this.get(i);
            if (maxCoorX < squareCoor[0]) {
                maxCoorX = squareCoor[0];
            }
        }
        return maxCoorX;
    }

    public int MinX() {
        int minCoorX = 9999;
        //System.out.println("TPiece size: " + size());
        for (int i = 0; i < this.size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) this.get(i);
            //System.out.println("Square coord: " + squareCoor[0]);
            if (minCoorX > squareCoor[0]) {
                minCoorX = squareCoor[0];
            }
        }
        return minCoorX;
    }

    public int MaxY() {
        int maxCoorY = 0;
        //System.out.println(size());
        for (int i = 0; i < this.size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) this.get(i);
            if (maxCoorY < squareCoor[1]) {
                maxCoorY = squareCoor[1];
            }
        }
        return maxCoorY;
    }

    public int MinY() {
        int minCoorY = 9999;
        //System.out.println("TPiece size: " + size());
        for (int i = 0; i < this.size(); i++) {
            int[] squareCoor = new int[2];
            squareCoor = (int[]) this.get(i);
            //System.out.println("Square coord: " + squareCoor[0]);
            if (minCoorY > squareCoor[1]) {
                minCoorY = squareCoor[1];
            }
        }
        return minCoorY;
    }
}

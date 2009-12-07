/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.SynchronousQueue;

/**
 * This Class describes the Gameboard. It lists the coordinates that are
 * currently being used by old pieces.
 * @author ee462a28
 */
public class GameField {

    public GameField() {
        coorList = new ArrayList<int[]>();
    }

    public GameField(GameField oldField) {
        coorList = new ArrayList<int[]>(oldField.coorList);
    }

    public void addCoor(int x, int y) {
        int[] coor = {x, y};
        coorList.add(coor);
    }

    public int size() {
        return coorList.size();
    }

    public void clear() {
        coorList.clear();
    }
    public void addPiece(Piece p, int w, int h){
        savePrev();
        lastPiece = new ArrayList<int[]>();
        for (int i = 0; i < p.size(); i++) {
            int[] tmparray = new int[2];
            tmparray = (int[]) p.get(i);
            tmparray[0] = tmparray[0] + w;
            tmparray[1] = tmparray[1] + h - 1;
            lastPiece.add(tmparray);
            addCoor(tmparray[0], tmparray[1]);
        }  
    }
    public ArrayList<int[]> getLastPiece(){
        return lastPiece;
    }
    public int checkLines() {
        //System.out.print("checkLines");
        if (coorList == null) {
            coorList = new ArrayList<int[]>();
            return 0;
        }
        prevRemovedLines = new LinkedList<String>();
        prevRemovedLines.clear();
        boolean c;
        int cnt, cntY;
        cntY = 0;
        for (int y = 0; y < 30; y++) {
            cnt = 0;
            for (int x = 0; x < 20; x++) {
                c = unavailCoor(x, y);
                if (c == false) {
                    break;
                } else {
                    cnt++;
                }
            }
            //System.out.print(" " + cnt);
            if (cnt == 20) {
                // System.out.print(" Remove Line" + y);

                removeLine(y);
                moveDown(y);
                prevRemovedLines.add(toString(prevCoorList,y));
                cntY++;
            }
        }
        //System.out.println(": "+cntY);
        return cntY;
    }
    public String toString(ArrayList<int[]> list, int y){
        String line = new String("");
        boolean block=false;
        for (int x=0; x<20; x++){
            for (int i = 0; i < list.size(); i++) {
                int[] coor = list.get(i);
                if (coor[1]==y && coor[0]==x){
                    block=true;
                    break;
                }
            }
            if (block) line = line+"1";
            else line=line+"0";
            block=false;
        }
        //System.out.println(line);
        return line;
    }
    public void addLine(String s){
        moveUp();
        for (int i=0; i<s.length(); i++){
            char c= s.charAt(i);
            if (c=='1'){
                addCoor(i,29);
            }

        }
        //System.out.println("addLine size:" +size());
    }
    public void removeLine(int y) {
        //printCoor();
        for (int i = 0; i < size(); i++) {
            //System.out.println(i);
            if (getY(i) == y) {
                coorList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < lastPiece.size(); i++) {
            if (getY(i) == y && getY(i)!=-1) {

                lastPiece.remove(i);
                i--;
            }
        }
    }

    public void moveDown(int y) {
        //System.out.println("moveDown");
        for (int j = y - 1; j >= 0; j--) {
            //System.out.println("y=" + j);
            for (int i = 0; i < size(); i++) {
                int[] coor = {getX(i), getY(i)};
                //System.out.println("i="+i+ "getY: "+ coor[1]);
                if (coor[1] == j) {
                    //System.out.println("true");
                    //System.out.println(coor[0] + "," + coor[1]);
                    coor[1]++;
                    coorList.remove(i);
                    coorList.add(coor);
                    i = -1;
                }
            }
        }
        for (int i = 0; i < lastPiece.size(); i++){
            if(lastPiece.get(i)[1] < y){
                int[] temp = {lastPiece.get(i)[0], lastPiece.get(i)[1]+1};
                lastPiece.set(i, temp);
            }
        }
    }

    public boolean unavailCoor(int x, int y) {
        int[] tmp = {x, y};
        int[] lstcoor = {0, 0};
        //System.out.println("availCoor");
        //System.out.println(tmp[0]+" "+ tmp[1]);
        //printCoor();
        if (coorList == null){
            coorList = new ArrayList<int[]>();
            return false;
        }
        boolean check = false;
        for (int i = 0; i < size(); i++) {
            lstcoor[0] = getX(i);
            lstcoor[1] = getY(i);
            if (lstcoor[0] == -1 || lstcoor[1] == -1) {return true;}
            if (tmp[0] == lstcoor[0] && tmp[1] == lstcoor[1]) {
                check = true;
                break;
            }

        }
        //System.out.println(check);

        return check;
    }
    public void moveUp() {
        //System.out.println("moveUp");
        for (int y = 1; y < 30; y++) {
            //System.out.println("y=" + j);
            for (int i = 0; i < size(); i++) {
                int[] coor = {getX(i), getY(i)};
                //System.out.println("i="+i+ "getY: "+ coor[1]);
                if (coor[1] == y) {
                    //System.out.println("true");
                    
                    coor[1]--;
                    coorList.remove(i);
                    coorList.add(coor);
                    //System.out.println(coor[0] + "," + coor[1]);
                    i = -1;
                }
            }
        }
    }
    public void addLine(int y, ArrayList<int[]> source, ArrayList<int[]> target){
        for (int i = 0; i < source.size(); i++) {
            int[] temp;
            temp=source.get(i);
            if (temp[1]==y){
                target.add(temp);
            }
        }
    }
    public LinkedList<String> getLinesRemoved(){
        //System.out.println("getLinesRemoved: "+ prevRemovedLines1.size());
        return prevRemovedLines;
    }
    public void savePrev(){
        prevCoorList = new ArrayList<int[]>(coorList);
        
    }

    public int numberHoles() {
        int holes = 0;
        if (lastPiece != null) {
            for (int i = 0; i < lastPiece.size(); i++) {
                for (int y = lastPiece.get(i)[1] + 1; y < boardHeight; y++) {
                    int x = lastPiece.get(i)[0];
                    if (!unavailCoor(x, y)) {
                        holes++;
                    } else {
                        break;
                    }
                }
            }
        }

        return holes;
    }

    public int holeDepth() {
        int depth = 0;
        if (lastPiece==null){
            return 0;
        }
        for (int i = 0; i < lastPiece.size(); i++) {
            int y = lastPiece.get(i)[1]+1;
            for (int end = y+5; y < end && y < boardHeight; y++) {
                int x = lastPiece.get(i)[0];
                if (!unavailCoor(x, y)) {
                    depth++;
                }
            }
        }

        return depth;
    }

    public int getRoughness() {
        int neighbors = 0;
        int[] present = new int[2];
        if (lastPiece==null) return 0;
        int size = lastPiece.size();

        for (int i = 0; i < size; i++) {
            present[0] = lastPiece.get(i)[0];
            present[1] = lastPiece.get(i)[1];

            present[0]++;
            if (unavailCoor(present[0], present[1]) || present[0] == boardWidth) {
                neighbors++;
            }
            present[0] -= 2;
            if (unavailCoor(present[0], present[1]) || present[0] == -1) {
                neighbors++;
            }

            present[0]++;

            present[1]++;
            if (unavailCoor(present[0], present[1]) || present[0] == boardHeight) {
                neighbors++;
            }
            present[1] -= 2;
            if (unavailCoor(present[0], present[1])) {
                neighbors++;
            }
        }

        return -neighbors; //negative so denser boards have lower scores
                            // (lower score = better)
    }

    public void printCoor() {
        int[] tmp;
        System.out.println("printCoor:");
        for (int i = 0; i < size(); i++) {
            System.out.println(getX(i) + "," + getY(i) + " ");
        }
    }

    public int getX(int i) {
        try{
            if (coorList == null) return -1;
            if (coorList.isEmpty()) return -1;
            if (coorList.get(i) == null) return -1;
            if (coorList.size() > i)
                return coorList.get(i)[0];
            else
                return -1;
        }catch (IndexOutOfBoundsException err){
            return -1;
        }
    }

    public int getY(int i) {
        try{
            if (coorList == null) return -1;
            if (coorList.isEmpty()) return -1;
            if (coorList.get(i) == null) return -1;
            if (coorList.size() > i)
                return coorList.get(i)[1];
            else
                return -1;
        }catch (IndexOutOfBoundsException err){
            return -1;
        }
    }
    private ArrayList<int[]> coorList;
    private ArrayList<int[]> prevCoorList;
    private ArrayList<int[]> lastPiece;
    private LinkedList<String> prevRemovedLines;
    final private int boardHeight = 30;
    final private int boardWidth = 20;
}

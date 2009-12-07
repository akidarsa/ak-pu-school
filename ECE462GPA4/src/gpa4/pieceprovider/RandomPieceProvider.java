/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gpa4.pieceprovider;

import gpa4.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jake
 */
public class RandomPieceProvider extends PieceProvider {

    public RandomPieceProvider() {
        filename = "superseq";//"seq5.txt";
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
            String s = null;
            pieceList = new LinkedList<Piece>();
            while ((s = in.readLine()) != null) {
                pieceList.add(new Piece(s));
            }
            in.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void copyPieceList(List dst, List src) {
        List piece;
        List copyPiece;
        int[] copyCoor;
        for (int i = 0; i < src.size(); i++) {
            piece = new LinkedList<int[]>();
            copyPiece = new LinkedList<int[]>();
            piece = (List) src.get(i);
            DeepCopy(copyPiece, piece);
            dst.add(copyPiece);
        }
    }

    private void DeepCopy(List dst, List src) {
        //dst = new LinkedList();
        Iterator srcIter = src.iterator();
        while (srcIter.hasNext()) {
            coorItem = new int[2];
            coorItem = (int[]) srcIter.next();
            dst.add(coorItem);
        }
    }
    public Piece nextPiece(int i){
        if (i==0){
            Random r = new Random();
            int rand = r.nextInt(pieceList.size());
            //System.out.println(rand);
            piece1++;// = rand;
            return getPiece(piece1);
        }else if(i==1){
            Random r = new Random();
            int rand = r.nextInt(pieceList.size());
            piece2++;//=rand;
            return getPiece(piece2);
        }else return null;
    }

    public Piece getPiece(int i) {
        if (i < pieceList.size()) {
            return pieceList.get(i);
        } else {
            return null;
        }
    }

    public int getSize() {
        return pieceList.size();
    }

    String filename;
    public LinkedList<Piece> pieceList;
    private int[] coorItem;
    public File file;
    private int piece1=-1,piece2=-1;

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpa4;

/**
 *
 * @author iZad
 */
//to run the server "java -jar sirtet.jar"
import gpa4.pieceprovider.NetworkPieceProvider;
import java.io.*;
import java.net.*;
import java.lang.String.*;


public class Client extends Thread {
    private final String clientID="Grp1";
    //poop
    private Socket requestSocket;
    private ObjectOutputStream out;
    private PrintStream printS = null;
    private BufferedReader buf = null;
    private ObjectInputStream in;
    private enum NetworkState{
        START,
        ID,
        GAMETYPE,
        ACCEPTED,
        INITIALIZE,
        SETUP,
        RUNGAME,
    }
    private NetworkState networkState;
    public String message;
    public String ouputMessage;
    private String command;
    private String tetrisPiece;
    private String finalTetrisPiece;
    private String playerid;
    private String currentOpponent;
    private int id;
    private String[] temp;
    private String finalString;
    private String secondString;
    private String hostName;
    private boolean ready;
    private boolean start;
    private boolean waiting;
    private boolean end;
    private GameController gc;
    private GameState gameState;
    private NetworkPieceProvider networkPieceProvider;

    Client(GameController gc, NetworkPieceProvider networkPieceProvider) {
        this.networkPieceProvider = networkPieceProvider;
        this.gc = gc;
    }
    //String message;
    /*String join = "JOIN";
    String quit = "QUIT";
    String ready = "READY?\n";
    String left = "g7:LEFT\n";
    String right = "g7:RIGHT\n";
    String fall = "g7:FALL\n";
    String rotate = "g7:ROTATE\n";
    String attack_line = "g7:ATTACK";
    String piece = "g7:PIECE";
    String gameover = "g7:GAMEOVER\n";*/

    /*Requester() {
    }*/
    public boolean startConnection(String hostName, GameState gameState) {
        //System.out.println("Host Name: " + hostName);
        this.hostName = hostName;
        this.gameState = gameState;
        this.networkState = NetworkState.ID;

        ouputMessage = null;
        currentOpponent = null;
        end=false;
        ready = false;
        start = false;
        waiting = false;

        try{
            //1. creating a socket to connect to the server
            requestSocket = new Socket(hostName, 9462);
            //System.out.println("Connected to localhost in port 9462");
            //2. get Input and Output streams
            requestSocket.setSoTimeout(10000); //10 Second timeout
            printS = new PrintStream(requestSocket.getOutputStream(), true);
            //out.flush();
            buf = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host");
            return false;
        } catch (IOException ioException) {
            System.err.println("ioException found.  Server running?");
            return false;
        }

        this.start();
        return true;
    }

    @Override
    public void run() {
        while (true) {
            try {
                temp = new String[2];
                message = null;
                command = null;
                message = (String) buf.readLine();
                if (message == null) continue;
                if (end==true) break;
                //System.out.println("server>" + message);
                
                switch (networkState) {
                    case ID:
                        //read what the server sends after connecting
                        if (message.startsWith("ID?")){
                            //reply back with our ID
                            sendMessage("ID="+clientID+"\n");
                            networkState = NetworkState.GAMETYPE;
                        }
                        break;
                    case GAMETYPE:
                        //3: Communicating with the server
                        //read what the server sends next (GAMETYPE)
                        if (message.startsWith("GAMETYPE?")){
                            if (gameState == GameState.VersusNetwork || gameState== GameState.DemoVs){
                                message = "GAMETYPE=tetris\n";
                            } else {
                                message = "GAMETYPE=tetris-qualifier\n";
                            }
                            sendMessage(message);
                            networkState = NetworkState.ACCEPTED;
                        }
                        break;
                    case ACCEPTED:
                        if (message.startsWith("ACCEPTED.")){
                            networkState = NetworkState.INITIALIZE;
                        }
                        break;
                    case INITIALIZE:

                        command = null;
                        playerid = null;

                        if (message.startsWith("JOIN")){
                            String delimeter = " ";
                            temp = message.split(delimeter);
                            playerid = temp[1];
                            command = temp[0];
                            
                            currentOpponent = playerid;
                        }
                        else if (message.startsWith("QUIT")){
                            String delimeter = " ";
                            temp = message.split(delimeter);
                            playerid = temp[1];
                            command = temp[0];

                            gc.setGameover(1);
                        }
                        else if (message.startsWith(Constants.ready)) {
                            playerid = null;
                            command = Constants.ready;

                            ready = true;
                            message = "READY\n";
                            sendMessage(message);
                        }

                        if (currentOpponent != null && ready){
                            networkState = NetworkState.SETUP;
                        } else if(gameState == GameState.Qualifier && ready){
                            networkState = NetworkState.SETUP;
                        }
                        break;
                    case SETUP:

                        String delimeter;
                        command = " ";

                        if (message.startsWith("START")){
                            start = true;
                        } else {
                            delimeter = ":";
                            temp = message.split(delimeter);
                            playerid = temp[0];
                            command = temp[1];
                        }

                        
                        if (command.startsWith(Constants.piece)){
                            if(playerid.equals(clientID)){
                                id = 2;
                                delimeter = " ";
                                temp = command.split(delimeter);
                                command = temp[0];
                                tetrisPiece = temp[1];
                                finalTetrisPiece = tetrisPiece.substring(0, pieceLength(tetrisPiece));
                                networkPieceProvider.queuePiece(finalTetrisPiece, id);
                                //do something for g7
                            }
                            else if (playerid.equals(currentOpponent)){
                                id = 3;
                                delimeter = " ";
                                temp = message.split(delimeter);
                                command = temp[0];
                                tetrisPiece = temp[1];
                                finalTetrisPiece = tetrisPiece.substring(0, pieceLength(tetrisPiece));
                                networkPieceProvider.queuePiece(finalTetrisPiece, id);
                                //do something for opponent
                            }
                        } else if (message.startsWith("QUIT")){
                            delimeter = " ";
                            temp = message.split(delimeter);
                            playerid = temp[1];
                            command = temp[0];
                            if (playerid.equals(currentOpponent))
                            gc.setGameover(1);
                        }

                        if (networkPieceProvider.queueSize(0) >=2
                                && networkPieceProvider.queueSize(1) >= 2
                                && start){
                            gc.initiateGame(gameState);
                            networkState = NetworkState.RUNGAME;
                        } else if (gameState == GameState.Qualifier
                                && networkPieceProvider.queueSize(0) >=2
                                && start){
                            gc.initiateGame(gameState);
                            networkState = NetworkState.RUNGAME;
                        }
                        break;
                    case RUNGAME:
                        if (message.startsWith("QUIT")){
                            delimeter = " ";
                            temp = message.split(delimeter);
                            playerid = temp[1];
                            if (playerid.startsWith(clientID)){
                                id = 2;
                            } else if(playerid.startsWith(currentOpponent)){
                                id = 3;
                            }
                            command = temp[0];
                            gc.setGameover(id-2);
                            continue;
                        }

                        delimeter = ":";
                        temp = message.split(delimeter);
                        playerid = temp[0];
                        command = temp[1];
                        //currentOpponent = playerid;

                        delimeter = " ";
                        if (playerid.startsWith(clientID)){
                            id = 2;
                        } else if(playerid.startsWith(currentOpponent)){
                            id = 3;
                        }
                        if (command.startsWith("WIN")){
                            if (id==2) gc.setWinner(true);
                        }
                        else if (command.startsWith("LOSE")){
                            if (id==2) gc.setWinner(false);
                        }
                        else if (command.startsWith(Constants.left)){
                            gc.sendCommand(Command.LEFT, id, 42);
                        }
                        else if (command.startsWith(Constants.right)){
                            gc.sendCommand(Command.RIGHT, id, 42);
                        }
                        else if (command.startsWith(Constants.fall)){
                            gc.sendCommand(Command.DOWN, id, 42);
                        }
                        else if (command.startsWith(Constants.rotate)){
                            gc.sendCommand(Command.ROTATE, id, 42);
                        }
                        else if (command.startsWith(Constants.attack_line)){
                            temp = command.split(delimeter);
                            command = temp[0];
                            tetrisPiece = temp[1];
                            finalTetrisPiece = tetrisPiece.substring(0, 20);
                            gc.queueLine(id, finalTetrisPiece);
                            //do something for g7
                        }else if (command.startsWith(Constants.piece)){
                            temp = command.split(delimeter);
                            command = temp[0];
                            tetrisPiece = temp[1];
                            finalTetrisPiece = tetrisPiece.substring(0, pieceLength(tetrisPiece));
                            networkPieceProvider.queuePiece(finalTetrisPiece, id);
                            //do something for g7
                        }else if (command.startsWith("GAMEOVER")){
                            temp = command.split(delimeter);
                            //playerid = temp[0];
                            //command = temp[1];
                            System.out.println("network gameover: "+id);
                            gc.setGameover(id-2);
                        }

                        break;
                }

            } catch (SocketTimeoutException timeout) {

                //System.out.println("Connection Lost");
            }catch (IOException ioException){
                //System.out.println("IOException");
            }

        }

    }
    public void end(){
        //System.out.println("endClient");
        end=true;
    }
    private int pieceLength(String piece){
        if (piece == null) return 0;
        int size = 0;
        for (int i = 0; i < piece.length(); i++){
            if (piece.charAt(i) == '0' || piece.charAt(i) == '1'){
                size++;
            } else{
                return size;
            }
        }
        return size;
    }


    public void sendCommand(Command c) throws IOException{

        if(c == c.LEFT){
            //ouputMessage = "g7:LEFT\n";
            sendMessage(clientID+":LEFT\n");
        }
        else if(c == c.RIGHT){
            //ouputMessage = "g7:RIGHT\n";
            sendMessage(clientID+":RIGHT\n");
        }
        else if(c == c.DOWN){
            //ouputMessage = "g7:FALL\n";
            sendMessage(clientID+":FALL\n");
        }
        else if(c == c.ROTATE){
            //ouputMessage = "g7:ROTATE\n";
            sendMessage(clientID+":ROTATE\n");

        }
    }

    private void sendMessage(String msg) {

        if(msg == null || printS==null) return;
        printS.println(msg);
        //System.out.print("client>" + msg);
    }

    /*public static void main(String args[]) {
        Requester client = new Requester();
        client.run();
    }*/

    protected void finalize() throws Throwable {
        try {
            out.close();
            requestSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        super.finalize(); //not necessary if extending Object.
    }

}



package ChessProject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ChessProject.ChessFolder.Bishop;
import ChessProject.ChessFolder.ChessPiece;
import ChessProject.ChessFolder.King;
import ChessProject.ChessFolder.Knight;
import ChessProject.ChessFolder.Pawn;
import ChessProject.ChessFolder.Queen;
import ChessProject.ChessFolder.Rook;
import ChessProject.ChessFolder.Square;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Chess {

    private Square[][] board;
    private List<String> moves = new ArrayList<>();
    private Boolean whiteToMove = true;

    public Chess() {
        // creating ChessBoard
        board = new Square[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = new Square(j, i);
            }
        }

        // white pawns
        for (int i = 0; i < board.length; i++) {
            int j = 6;
            board[j][i].setPiece(new Pawn(ChessPiece.WHITE));
        }
        // black pawns
        for (int i = 0; i < board.length; i++) {
            int j = 1;
            board[j][i].setPiece(new Pawn(ChessPiece.BLACK));
        }

        // Rooks
        board[0][0].setPiece(new Rook(ChessPiece.BLACK));
        board[0][7].setPiece(new Rook(ChessPiece.BLACK));
        board[7][0].setPiece(new Rook(ChessPiece.WHITE));
        board[7][7].setPiece(new Rook(ChessPiece.WHITE));

        // knights
        board[0][1].setPiece(new Knight(ChessPiece.BLACK));
        board[0][6].setPiece(new Knight(ChessPiece.BLACK));
        board[7][1].setPiece(new Knight(ChessPiece.WHITE));
        board[7][6].setPiece(new Knight(ChessPiece.WHITE));

        // Bishops
        board[0][2].setPiece(new Bishop(ChessPiece.BLACK));
        board[0][5].setPiece(new Bishop(ChessPiece.BLACK));
        board[7][2].setPiece(new Bishop(ChessPiece.WHITE));
        board[7][5].setPiece(new Bishop(ChessPiece.WHITE));
        // Queens
        board[0][3].setPiece(new Queen(ChessPiece.BLACK));
        board[7][3].setPiece(new Queen(ChessPiece.WHITE));
        // Kings
        board[0][4].setPiece(new King(ChessPiece.BLACK));
        board[7][4].setPiece(new King(ChessPiece.WHITE));
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (board[j][i].getPiece() != null)
                    board[j][i].getPiece().setBoard(this);
            }
        }
    }

    public Square[][] getBoard() {
        Square[][] copy = new Square[board.length][];
        for (int i = 0; i < board.length; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }

    public int moveTextToInt(String input, int index) {
        if(input.length()!=5) throw new IllegalArgumentException("iNVALID LENGTH OF STRING");
        char[] charArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        int pos1 = 0;
        int pos3 = 0;
        for (int i = 0; i < charArray.length; i++) {
            if (input.charAt(0) == charArray[i]) {
                pos1 = i;
            }
            if (input.charAt(3) == charArray[i]) {
                pos3 = i;
            }
        }
        int pos2 = 8 - Character.getNumericValue(input.charAt(1));
        int pos4 = 8 - Character.getNumericValue(input.charAt(4));
        int[] intArray = { pos1, pos2, pos3, pos4 };
        return intArray[index];

    }

    public String moveIntToText(int fromFile, int fromRank, int toFile, int toRank) {
        StringBuilder str = new StringBuilder();
        char[] charArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        str.append(charArray[fromFile]);
        str.append(8 - fromRank);
        str.append(',');
        str.append(charArray[toFile]);
        str.append(8-toRank);
        return str.toString();
    }

    public Boolean WhiteToMove() {
        return whiteToMove;
    }

    public String matchResult() {
        if (noValidMovesForOpponentAfterMove() && inCheck()) {
            if (whiteToMove)
                return "Victory For black";
            else {
                return "Victory For White";
            }
        } else if (noValidMovesForOpponentAfterMove() && !inCheck())
            return "Draw";
        return "";
    }

    public void move(String input) {
        if (!validateMoveChess(input))
            return;
        move(moveTextToInt(input, 0), moveTextToInt(input, 1), moveTextToInt(input, 2), moveTextToInt(input, 3));

    }

    private void move(int fromFile, int fromRank, int toFile, int toRank) {
        if(castleCheck(fromFile, fromRank, toFile, toRank)){
            castleMove(fromFile, fromRank, toFile, toRank);
        }
        else{
        board[toRank][toFile].setPiece(board[fromRank][fromFile].getPiece());
        board[fromRank][fromFile].setPiece(null);
        }
        moves.add(moveIntToText(fromFile, fromRank, toFile, toRank));
        if (whiteToMove)
            whiteToMove = false;
        else {
            whiteToMove = true;
        }
    }

    private int getKingFile(String color) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getPieceColor().equals(color)) {
                        if (board[i][j].getPiece() instanceof King) {
                            return j;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private int getKingRank(String color) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getPieceColor().equals(color)) {
                        if (board[i][j].getPiece() instanceof King) {
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public Boolean noValidMovesForOpponentAfterMove() {
        String colorToMove = "";
        if (whiteToMove)
            colorToMove = "W";
        else
            colorToMove = "B";

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getPieceColor().equals(colorToMove)) {
                        for (int p = 0; p < board.length; p++) {
                            for (int k = 0; k < board.length; k++) {
                                if (validateMoveChess(j, i, k, p))
                                    return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public Boolean inCheck() {
        String isColorInCheck = "";
        String otherColor = "";
        if (whiteToMove) {
            isColorInCheck = "W";
            otherColor = "B";
        } else {
            isColorInCheck = "B";
            otherColor = "W";
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getPieceColor().equals(otherColor)) {
                        if (board[i][j].getPiece().isLegalMove(j, i, getKingFile(isColorInCheck),
                                getKingRank(isColorInCheck)))
                            return true;

                    }
                }
            }
        }
        return false;

    }

    private Boolean inCheckAfterMove(int fromFile, int fromRank, int toFile, int toRank) {
        String colorThatMoved = "";
        String otherColor = "";
        if (whiteToMove) {
            colorThatMoved = "W";
            otherColor = "B";
        } else {
            colorThatMoved = "B";
            otherColor = "W";
        }

        // sjekker om spilleren som gjør et trekk havner i sjakk etter trekket
        ChessPiece tmp = null;

        // isLagalMove
        if (board[toRank][toFile].getPiece() != null) {
            tmp = board[toRank][toFile].getPiece();
        }
        board[toRank][toFile].setPiece(board[fromRank][fromFile].getPiece());
        board[fromRank][fromFile].setPiece(null);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getPieceColor().equals(otherColor)) {
                        if (board[i][j].getPiece().isLegalMove(j, i, getKingFile(colorThatMoved),getKingRank(colorThatMoved))) {
                            
                            board[fromRank][fromFile].setPiece(board[toRank][toFile].getPiece());
                            board[toRank][toFile].setPiece(null);

                            if (tmp != null) {
                                board[toRank][toFile].setPiece(tmp);
                            }
                            return true;
                        }
                    }
                }
            }
        }
        board[fromRank][fromFile].setPiece(board[toRank][toFile].getPiece());
        board[toRank][toFile].setPiece(null);
        if (tmp != null) {
            board[toRank][toFile].setPiece(tmp);
        }
        return false;
    }

    public Boolean validateMoveChess(String move) {
        if (move == null)
            return false;
        int fromFile = moveTextToInt(move, 0);
        int fromRank = moveTextToInt(move, 1);
        int toFile = moveTextToInt(move, 2);
        int toRank = moveTextToInt(move, 3);
        return validateMoveChess(fromFile, fromRank, toFile, toRank);
    }

    public Boolean validateMoveChess(int fromFile, int fromRank, int toFile, int toRank) {
        if (fromRank == toRank && fromFile == toFile)
            return false;
        if (fromFile < 0 || fromFile > 7)
            return false;
        if (toFile < 0 || toFile > 7)
            return false;
        if (fromRank < 0 || fromRank > 7)
            return false;
        if (toRank < 0 || toRank > 7)
            return false;

        if (castleCheck(fromFile, fromRank, toFile, toRank))
            return true;

        if (board[fromRank][fromFile].getPiece() == null)
            return false;
        if (WhiteToMove() && board[fromRank][fromFile].getPiece().getPieceColor().equals("B"))
            return false;
        if ((!WhiteToMove()) && board[fromRank][fromFile].getPiece().getPieceColor().equals("W"))
            return false;
        if(board[toRank][toFile].getPiece()!=null){
            if(board[toRank][toFile].getPiece().getPieceColor().equals(board[fromRank][fromFile].getPiece().getPieceColor())) return false;
        }
        if (!board[fromRank][fromFile].getPiece().isLegalMove(fromFile, fromRank, toFile, toRank))
            return false;
        if (inCheckAfterMove(fromFile, fromRank, toFile, toRank))
            return false;
        return true;

    }

    private Boolean castleCheck(int fromFile, int fromRank, int toFile, int toRank){
        String colorToMove = "";
        String otherColor ="";
        if (whiteToMove) {
            colorToMove = "W";
            otherColor="B";
        }
        else {
            colorToMove = "B";
            otherColor="W";
        }
        int kingFile=0;
        int kingRank=0;
        int rookFile=0;
        int rookRank=0;

        if(fromFile==getKingFile(colorToMove) && fromRank==getKingRank(colorToMove)){
            kingFile=fromFile;
            kingRank=fromRank;
            rookFile=toFile;
            rookRank=toRank;
        }
        else{
            kingFile=toFile;
            kingRank=toRank;
            rookFile=fromFile;
            rookRank=fromRank;
        }
        if(!((board[kingRank][kingFile].getPiece() instanceof King && board[kingRank][kingFile].getPiece().getPieceColor().equals(colorToMove))
        &&(board[rookRank][rookFile].getPiece() instanceof Rook && board[rookRank][rookFile].getPiece().getPieceColor().equals(colorToMove)))) return false;
            
        if(inCheck()) return false;
        if(pieceHasMoved(board[kingRank][kingFile].getPiece())||pieceHasMoved(board[rookRank][rookFile].getPiece())) return false;

        
        int highFile =0;
        int lowFile =0;
        if(kingFile>rookFile){
            highFile=kingFile;
            lowFile=rookFile;
        }
        else{
            highFile=rookFile;
            lowFile=kingFile;
        }

            for(int k=lowFile+1;k<highFile;k++){
                if(!(board[kingRank][k].getPiece()==null)) return false;
                
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board.length; j++) {
                        if (board[i][j].getPiece() != null) {
                            if (board[i][j].getPiece().getPieceColor().equals(otherColor)) {
                                if (board[i][j].getPiece().isLegalMove(j, i, k, kingRank)) return false;
                            }
                        }
            }
        }
    }
        return true;
    }

    private void castleMove(int fromFile, int fromRank, int toFile, int toRank){
        int kingFile=0;
        int kingRank=0;
        int rookFile=0;
        int rookRank=0;

        if(board[fromRank][fromFile].getPiece() instanceof King){
            kingFile=fromFile;
            kingRank=fromRank;
            rookFile=toFile;
            rookRank=toRank;
        }
        else{
            kingFile=toFile;
            kingRank=toRank;
            rookFile=fromFile;
            rookRank=fromRank;
        }

        if(rookFile-kingFile==3){
            board[kingRank][kingFile+2].setPiece(board[kingRank][kingFile].getPiece());
            board[kingRank][kingFile+1].setPiece(board[kingRank][rookFile].getPiece());
            board[kingRank][kingFile].setPiece(null);
            board[kingRank][rookFile].setPiece(null);
        }

        else{
            board[kingRank][kingFile-3].setPiece(board[kingRank][kingFile].getPiece());
            board[kingRank][kingFile-2].setPiece(board[kingRank][rookFile].getPiece());
            board[kingRank][kingFile].setPiece(null);
            board[kingRank][rookFile].setPiece(null);

        }
    }

    private Boolean pieceHasMoved(ChessPiece piece) {
        int file = 0;
        int rank = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j].getPiece()!=null){
                    if (board[i][j].getPiece().equals(piece)&&piece.getPieceColor().equals(board[i][j].getPiece().getPieceColor())){
                        file = j;
                        rank = i;
                        break;
                    }
            }
        }
    }

        for (String move : moves) {
            if (moveTextToInt(move, 0) == file && moveTextToInt(move, 1) == rank)
                return true;
            if (moveTextToInt(move, 2) == file && moveTextToInt(move, 3) == rank)
                return true;
        }
        return false;
    }

    public Boolean pawnPromotionTime() {
        Pawn pawn = null;
        // white
        for (int i = 0; i < board.length; i++) {
            if (board[0][i].getPiece() != null) {
                if (board[0][i].getPiece().getPieceColor().equals("W")) {
                    if (board[0][i].getPiece() instanceof Pawn) {
                        pawn = (Pawn) board[0][i].getPiece();
                    }
                }
            }
        }
        // black
        for (int i = 0; i < board.length; i++) {
            if (board[7][i].getPiece() != null) {
                if (board[7][i].getPiece().getPieceColor().equals("B")) {
                    if (board[7][i].getPiece() instanceof Pawn) {
                        pawn = (Pawn) board[7][i].getPiece();
                    }

                }
            }
        }
        if (pawn != null)
            return true;
        else {
            return false;
        }
    }

    public void pawnPromotion(String desiredPiece) {

        Pawn pawn = null;
        String color = null;
        // white
        for (int i = 0; i < board.length; i++) {
            if (board[0][i].getPiece() != null) {
                if (board[0][i].getPiece().getPieceColor().equals("W")) {
                    if (board[0][i].getPiece() instanceof Pawn) {
                        pawn = (Pawn) board[0][i].getPiece();
                    }
                }
            }
        }
        // black
        for (int i = 0; i < board.length; i++) {
            if (board[7][i].getPiece() != null) {
                if (board[7][i].getPiece().getPieceColor().equals("B")) {
                    if (board[7][i].getPiece() instanceof Pawn) {
                        pawn = (Pawn) board[7][i].getPiece();
                    }
                }
            }
        }
        if (pawn != null) {
            if (pawn.getPieceColor().equals("W"))
                color = ChessPiece.WHITE;
            else {
                color = ChessPiece.BLACK;
            }
            Queen queen = new Queen(color);
            Knight knight = new Knight(color);
            Rook rook = new Rook(color);
            Bishop bishop = new Bishop(color);

            Map<String, ChessPiece> pieceMap = new HashMap<>();
            pieceMap.put("Queen", queen);
            pieceMap.put("night", knight);
            pieceMap.put("Rook", rook);
            pieceMap.put("Bishop", bishop);

            if (pieceMap.containsKey(desiredPiece)) {
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board.length; j++) {
                        if (board[i][j].getPiece() != null) {
                            if (board[i][j].getPiece().equals(pawn)) {
                                board[i][j].setPiece(pieceMap.get(desiredPiece));
                                board[i][j].getPiece().setBoard(this);
                                moves.add(desiredPiece);
                            }
                        }
                    }
                }
            }

        }
    }

    // public Boolean enPassantcheck(int fromFile, int fromRank, int toFile, int toRank){
    //     String colorToMove = "";
    //     String otherColor ="";
    //     int rankDiff=0;
    //     if (whiteToMove) {
    //         colorToMove = "W";
    //         otherColor="B";
    //         rankDiff=-1;
    //     }
    //     else {
    //         colorToMove = "B";
    //         otherColor="W";
    //         rankDiff=+1;
    //     }
    //     if(board[fromRank][fromFile].getPiece()!=null){
    //         if((board[fromRank][fromFile].getPiece() instanceof Pawn)&&board[fromRank][fromFile].getPiece().getPieceColor().equals(colorToMove)){
    //             Pawn pieceToMove = (Pawn) board[fromRank][fromFile].getPiece();
    //             if(!pieceToMove.rightWay(fromRank, toRank)) return false;
    //             if(!pieceToMove.oneRankChange(fromRank, toRank)) return false;
    //             if(!(colorToMove.equals("W")&&(fromRank==5))) return false;
    //         }
    //     }
    //     return true;
    // }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getPiece() == null)
                    s += String.format("%1$" + 10 + "s", "null");
                else
                    s += board[i][j] + " ";
            }
            s += "\n";
        }
        return s;
    }

    public ImageView getImageByRowColumnIndex(Integer file, Integer rank, GridPane grid) {
        Node result = null;
        ObservableList<Node> children = grid.getChildren();

        for (Node node : children) {
            if (node instanceof StackPane)
                continue;
            else if ((GridPane.getRowIndex(node) == rank) && GridPane.getColumnIndex(node) == file) {
                result = node;
                break;
            }
        }
        ImageView im = (ImageView) result;
        return im;
    }

    private List<String> getMoves() {
        return moves;
    }

    public void pawnPromotionVisual(String piece,GridPane grid) throws FileNotFoundException{
        Pawn pawn = null;
        String color = null;
        int rank=0;
        int file=0;
        // white
        for (int i = 0; i < board.length; i++) {
            if (board[0][i].getPiece() != null) {
                if (board[0][i].getPiece().getPieceColor().equals("W")) {
                    if (board[0][i].getPiece() instanceof Pawn) {
                        pawn = (Pawn) board[0][i].getPiece();
                        rank=0;
                        file=i;
                    }
                }
            }
        }
        // black
        for (int i = 0; i < board.length; i++) {
            if (board[7][i].getPiece() != null) {
                if (board[7][i].getPiece().getPieceColor().equals("B")) {
                    if (board[7][i].getPiece() instanceof Pawn) {
                        pawn = (Pawn) board[7][i].getPiece();
                        rank=7;
                        file=i;
                    }
                }
            }
        }
        if (pawn != null) {
            if (pawn.getPieceColor().equals("W"))
                color = "White";
            else {
                color = "Black";
            }
        }

        String imagePath = "src/main/resources/ChessProject/Images/"+color+"/"+piece+color+".png";
        Image newImage = new Image(new FileInputStream(imagePath));
        getImageByRowColumnIndex(file, rank, grid).setImage(newImage);

    }

    public void visualMove(String move, GridPane grid) {
        int fromFile = moveTextToInt(move, 0);
        int fromRank = moveTextToInt(move, 1);
        int toFile = moveTextToInt(move, 2);
        int toRank = moveTextToInt(move, 3);
        visualMove(fromFile, fromRank, toFile, toRank, grid);
    }

    public void visualMove(int fromFile, int fromRank, int toFile, int toRank, GridPane grid) {
        
        if(castleCheck(fromFile, fromRank, toFile, toRank)){
            int kingFile=0;
            int kingRank=0;
            int rookFile=0;
            int rookRank=0;

            if(board[fromRank][fromFile].getPiece() instanceof King){
                kingFile=fromFile;
                kingRank=fromRank;
                rookFile=toFile;
                rookRank=toRank;
            }
            else{
                kingFile=toFile;
                kingRank=toRank;
                rookFile=fromFile;
                rookRank=fromRank;
            }
            ImageView kingNode = getImageByRowColumnIndex(kingFile, kingRank, grid);
            ImageView rookNode = getImageByRowColumnIndex(rookFile, rookRank, grid);
            
            if(rookFile-kingFile==3){
                grid.getChildren().remove(rookNode);
                grid.getChildren().remove(kingNode);
                grid.add(kingNode, kingFile+2, kingRank);
                grid.add(rookNode, rookFile-2, rookRank);
            }
            else{
                grid.getChildren().remove(rookNode);
                grid.getChildren().remove(kingNode);
                grid.add(kingNode, kingFile-3, kingRank);
                grid.add(rookNode, rookFile+2, rookRank);
            }
            
        }
        else{
            ImageView moveNode = getImageByRowColumnIndex(fromFile, fromRank, grid);
            ImageView outNode = getImageByRowColumnIndex(toFile, toRank, grid);
        if (outNode != null)
            grid.getChildren().remove(outNode);

        grid.getChildren().remove(moveNode);
        grid.add(moveNode, toFile, toRank);
    }
}

    public boolean validateTextInput(String textInput) {
        if (textInput.length() != 5) {
            return  false;
        }
        if (!(Character.isLetter(textInput.charAt(0)) && Character.isDigit(textInput.charAt(1)) && Character.isLetter(textInput.charAt(3)) && Character.isDigit(textInput.charAt(4)))) {
            return false;
        }
        if (textInput.charAt(0) > 'h' || textInput.charAt(3) > 'h') {
            return false;
        }
        if (textInput.charAt(1) > '8' || textInput.charAt(4) > '8') {
            return false;
        }
        return true;
    }

    public void loadGame(String file, GridPane grid) throws IOException, InterruptedException, IllegalAccessException {
        FileReader fileReader = new FileReader(file);

        // Convert fileReader to bufferedReader
        BufferedReader buffReader = new BufferedReader(fileReader);

        String line = buffReader.readLine();
        
        while (line != null) {
            // read next line
            int fromFile = moveTextToInt(line, 0);
            int fromRank = moveTextToInt(line, 1);
            int toFile = moveTextToInt(line, 2);
            int toRank = moveTextToInt(line, 3);
            // movePiece(line);
            System.out.println(validateMoveChess(fromFile, fromRank, toFile, toRank));
            if (validateMoveChess(fromFile, fromRank, toFile, toRank)) {
                visualMove(fromFile, fromRank, toFile, toRank ,grid);
                move(fromFile, fromRank, toFile, toRank);
            }
            line = buffReader.readLine();
        }

        buffReader.close();
    }


    public void saveGame(String file) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(file);
        for (String move : getMoves()) {
            out.println(move);
        }
        out.close();
    }

    public static void main(String[] args) {
    }
}

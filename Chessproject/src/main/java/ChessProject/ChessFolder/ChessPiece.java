package ChessProject.ChessFolder;

import ChessProject.Chess;

public abstract class ChessPiece implements ChessPieceInt{
    private Chess chess;
    protected String color;
    String name = this.getClass().getSimpleName();

    ChessPiece (String color) {
        this.color = color;
    }
    
    @Override
    public String getPieceColor(){
        if(color.equals(BLACK)){
            return "B";
        }
        else{
            return "W";
        }
    }

    @Override
    public String getPieceName(){
        return name;
    }


    @Override
    public String toString() {
        String string = name + "("+getPieceColor()+")";
        return String.format("%1$"+10+ "s", string);
    }

        public void setBoard(Chess chess){
            this.chess=chess;

        }

        public Square[][] getBoard(){
            return chess.getBoard();
        }

        public Chess getChess(){
            return chess;
        }        
    
}

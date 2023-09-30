package ChessProject.ChessFolder;

public class Queen extends ChessPiece{

    public Queen(String color) {
        super(color);
    }

    @Override
    public boolean isLegalMove(int fromFile, int fromRank, int toFile, int toRank) {
        Rook tmpR = new Rook(color);
        Bishop tmpB = new Bishop(color);
        tmpR.setBoard(getChess());
        tmpB.setBoard(getChess());

     
        return (tmpB.isLegalMove(fromFile, fromRank, toFile, toRank)||tmpR.isLegalMove(fromFile, fromRank, toFile, toRank));
    }

    
}

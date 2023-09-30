package ChessProject.ChessFolder;

public class King extends ChessPiece{
    
    public King(String color) {
        super(color);
    }

    @Override
    public boolean isLegalMove(int fromFile, int fromRank, int toFile, int toRank) {
        if(getBoard()[toRank][toFile].getPiece()!=null){
            if(getBoard()[fromRank][fromFile].getPiece().getPieceColor().equals(getBoard()[toRank][toFile].getPiece().getPieceColor())) return false;
        }
        if(Math.abs(fromFile-toFile)<=1&&(Math.abs(fromRank-toRank)<=1)) return true;
        return false;
    }
    
}

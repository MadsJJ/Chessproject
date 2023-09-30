package ChessProject.ChessFolder;

public class Knight extends ChessPiece{

    public Knight(String color) {
        super(color);
    }

    @Override
    public boolean isLegalMove(int fromFile, int fromRank, int toFile, int toRank) {
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
        if(getBoard()[toRank][toFile].getPiece()!=null){
            if(getBoard()[fromRank][fromFile].getPiece().getPieceColor().equals(getBoard()[toRank][toFile].getPiece().getPieceColor())) return false;
        }

        if(Math.abs(fromFile-toFile)==1&&(Math.abs(fromRank-toRank)==2)) return true;
        if(Math.abs(fromFile-toFile)==2&&(Math.abs(fromRank-toRank)==1)) return true;
        
        return false;
        
    }
    
}

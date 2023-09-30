package ChessProject.ChessFolder;

public class Rook  extends ChessPiece{

    public Rook(String color) {
        super(color);
    }

    @Override
    public boolean isLegalMove(int fromFile, int fromRank, int toFile, int toRank) {
        if(!(fromFile==toFile||fromRank==toRank)) return false;
        if(getBoard()[toRank][toFile].getPiece()!=null){
        if(getBoard()[fromRank][fromFile].getPiece().getPieceColor().equals(getBoard()[toRank][toFile].getPiece().getPieceColor())) return false;
    }
        if(fromRank==toRank){
        if(fromFile<toFile){
            for(int i = fromFile+1;i<toFile;i++){
                if(getBoard()[fromRank][i].occupied()) return false;
            }
        }
        if(fromFile>toFile){
            for(int i = toFile+1;i<fromFile;i++){
                if(getBoard()[fromRank][i].occupied()) return false;
            }
        }
    }
    if(fromFile==toFile){
        if(fromRank<toRank){
            for(int i = fromRank+1;i<toRank;i++){
                if(getBoard()[i][fromFile].occupied()) return false;
            }
        }
        if(fromRank>toRank){
            for(int i = toRank+1;i<fromRank;i++){
                if(getBoard()[i][fromFile].occupied()) return false;
            }
        }
    }
    return true;
    }
    
}

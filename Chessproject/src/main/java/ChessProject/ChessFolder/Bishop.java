package ChessProject.ChessFolder;

public class Bishop extends ChessPiece{

    public Bishop(String color) {
        super(color);
    }

    @Override
    public boolean isLegalMove(int fromFile, int fromRank, int toFile, int toRank) {
        if(Math.abs(fromFile-toFile)!=Math.abs(fromRank-toRank)) return false;
        

        if(fromRank<toRank&&fromFile<toFile){
            for(int i=1;i<(toRank-fromRank);i++){
                if(getBoard()[fromRank+i][fromFile+i].occupied()) return false;
            }
        }
        if(fromRank<toRank&&fromFile>toFile){
            for(int i=1;i<(toRank-fromRank);i++){
                if(getBoard()[fromRank+i][fromFile-i].occupied()) return false;
            }
        }

        if(fromRank>toRank&&fromFile<toFile){
            for(int i=1;i<(fromRank-toRank);i++){
                if(getBoard()[fromRank-i][fromFile+i].occupied()) return false;
            }
        }
        if(fromRank>toRank&&fromFile>toFile){
            for(int i=1;i<(fromRank-toRank);i++){
                if(getBoard()[fromRank-i][fromFile-i].occupied()) return false;
            }
        }

        if(getBoard()[toRank][toFile].getPiece()!=null){
            if(getBoard()[fromRank][fromFile].getPiece().getPieceColor().equals(getBoard()[toRank][toFile].getPiece().getPieceColor())) return false;
        }

        return true;
        }
      
    }

    


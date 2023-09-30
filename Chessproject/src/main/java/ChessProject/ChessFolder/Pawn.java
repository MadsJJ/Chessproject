package ChessProject.ChessFolder;


public class Pawn extends ChessPiece{

    public Pawn(String color) {
        super(color);
    }

    public int moveTextToInt(String input,int index){
        char[] charArray = {'a','b','c','d','e','f','g','h'};
        int pos1 = 0;
        int pos3 = 0;
        for(int i=0;i<charArray.length; i++){
            if(input.charAt(0)==charArray[i]){
                pos1=i;
            }
            if(input.charAt(3)==charArray[i]){
                pos3=i;
            }
        }
        int pos2=8-Character.getNumericValue(input.charAt(1));
        int pos4=8-Character.getNumericValue(input.charAt(4));
        int[] intArray ={pos1,pos2,pos3,pos4};

        return intArray[index];

    }

    public Boolean rightWay(int fromRank,int toRank){
        if(getPieceColor().equals("W")&&(fromRank>toRank)) return true;
        if(getPieceColor().equals("B")&&(fromRank<toRank)) return true;
        return false;
    }

    public Boolean oneRankChange(int fromRank, int toRank){
        if(getPieceColor().equals("W")&&(fromRank==toRank+1))return true;
        if(getPieceColor().equals("B")&&(fromRank==toRank-1)) return true;
        return false;
    }

    public Boolean atStart(int fromRank){
        if(getPieceColor().equals("W")&&fromRank==6) return true;
        if(getPieceColor().equals("B")&&fromRank==1) return true;
        return false;
    }

    @Override
    public boolean isLegalMove(int fromFile, int fromRank, int toFile, int toRank) {
        // gå en rute frem
        if(rightWay(fromRank, toRank)&&(fromFile==toFile&&oneRankChange(fromRank, toRank))&&(!getBoard()[toRank][toFile].occupied())) return true;
        if(rightWay(fromRank, toRank)&&(fromFile==toFile&&oneRankChange(fromRank, toRank))&&(!getBoard()[toRank][toFile].occupied())) return true;

        // flytte to frem første trekk
        if(rightWay(fromRank, toRank)&&(fromFile==toFile&&fromRank==toRank+2)&&(atStart(fromRank))&&(!getBoard()[toRank][toFile].occupied())&&(!getBoard()[toRank+1][toFile].occupied())) return true;
        if(rightWay(fromRank, toRank)&&(fromFile==toFile&&fromRank==toRank-2)&&(atStart(fromRank))&&(!getBoard()[toRank][toFile].occupied())&&(!getBoard()[toRank-1][toFile].occupied())) return true;
        
        // vanlig dreping av motstanderbrikke
        if(rightWay(fromRank, toRank)&&oneRankChange(fromRank, toRank)&&(((fromFile==toFile-1)&&getBoard()[toRank][toFile].occupied())||((fromFile==toFile+1)&&getBoard()[toRank][toFile].occupied()))) return true;

        return false;
            


    }

    
    
}

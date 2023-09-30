package ChessProject.ChessFolder;

public class Square {
    private int rank;
    private int file;
    private boolean isOccupied;
    private ChessPiece piece;

    public Square(int rank, int file){
        this.file = file;
        this.rank = rank;
    }
    

    public int getRank() {
        return rank;
    }


    public int getFile() {
        return file;
    }


    public void setPiece(ChessPiece piece) {
        this.piece=piece;
        if (piece == null)
            isOccupied = false;
        else 
            isOccupied = true;
    }

    public ChessPiece getPiece(){
        return piece;
    }

    public Boolean occupied(){
        return isOccupied;
    }
   
   @Override
   public String toString() {
        return ""+piece;
   }
}

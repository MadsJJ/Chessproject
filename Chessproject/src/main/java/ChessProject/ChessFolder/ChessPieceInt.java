package ChessProject.ChessFolder;

public interface ChessPieceInt {
    public static final String WHITE = "white", BLACK = "black";
    public boolean isLegalMove(int fromFile, int fromRank, int toFile, int toRank);
    public String getPieceColor();
    public String getPieceName();
}
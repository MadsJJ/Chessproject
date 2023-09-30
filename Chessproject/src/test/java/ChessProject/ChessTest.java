package ChessProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ChessProject.ChessFolder.ChessPiece;
import ChessProject.ChessFolder.King;
import ChessProject.ChessFolder.Pawn;
import ChessProject.ChessFolder.Rook;

public class ChessTest {

    private Chess chess;
    
    @BeforeEach
    public void setUp() throws Exception {
        chess = new Chess();
        chess.move("b2,b3");
        chess.move("g8,f6");
        chess.move("c1,b2");
        chess.move("f6,h5");
        chess.move("d2,d4");
        chess.move("e7,e6");
        chess.move("d4,d5");
        chess.move("g7,g5");
        chess.move("d1,d4");
        chess.move("f8,a3");
        chess.move("b1,d2");
        chess.move("d7,d6");
    }
    
    // white to move

    @Test
    public void testSimpleMoves() {
        // sjekker at man ikke kan flytte fra et tomt felt
        assertFalse(chess.validateMoveChess("h3,h4"), "");
        chess.move("h3,h4");
        assertTrue(chess.getBoard()[5][7].getPiece() == null, "");
        assertTrue(chess.getBoard()[6][7].getPiece() instanceof Pawn, "");
        // sjekker at hvit ikke kan flytte to ganger på rad
        assertFalse(chess.validateMoveChess("h7,h6"), "");  
    }

    @Test
    public void testCapturingPiece() {
        assertTrue(chess.validateMoveChess("d5,e6"), "");
        chess.move("d5,e6");
        assertTrue(chess.getBoard()[2][4].getPiece() instanceof Pawn, "");
        assertTrue(chess.getBoard()[2][4].getPiece().getPieceColor().equals("W"), "");
    }

    @Test
    public void testCheck() {
        chess.move("d4,h8" );
        // sjekker om spiller er i sjakk
        assertTrue(chess.inCheck(), "");
        // sjekker at spiller må komme seg ut av sjakk
        assertFalse(chess.validateMoveChess("d8,d7"), "");
        assertTrue(chess.validateMoveChess("e8,e7"), "");
        chess.move("e8,e7");
        chess.move("h2,h3");
        // sjekker at spiller ikke kan flytte til et felt som vil sette seg selv i sjakk
        assertFalse(chess.validateMoveChess("e7,e8"), "");
        // tar brikke som setter spiller i sjakk
        chess.move("h8,f6");
        assertTrue(chess.validateMoveChess("h5,f6"), "");
    }

    
    @Test
    public void testPawnBehaviour() {
        // sjekker at bonde ikke kan hopper over brikker
        assertFalse(chess.validateMoveChess("a2,a4"), "");
        // sjekker at bonde ikke kan flytte to opp etter flyttet en gang
        assertFalse(chess.validateMoveChess("b3,b5"), "");
    }

    @Test
    public void testCastle() {
        // white to move
        chess.move("d4,c4");
        // test ShortCasteBlack
        assertTrue(chess.validateMoveChess("e8,h8"), "");
        chess.move("e8,h8");
        assertTrue(chess.getBoard()[0][6].getPiece() instanceof King, "");
        assertTrue(chess.getBoard()[0][5].getPiece() instanceof Rook, "");
        // test LongCasteWhite
        assertTrue(chess.validateMoveChess("e1,a1"), "");
        chess.move("e1,a1");
        assertTrue(chess.getBoard()[7][1].getPiece() instanceof King, "");
        assertTrue(chess.getBoard()[7][2].getPiece() instanceof Rook, "");
    }

    @Test
    public void testCheckmate() {
        chess.move("d5,e6");
        chess.move("e8,h8");
        chess.move("d4,h8");
        assertTrue(chess.noValidMovesForOpponentAfterMove() && chess.inCheck(), "");
    }
}

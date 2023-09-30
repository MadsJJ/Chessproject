package ChessProject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ChessController {

    @FXML
    public void initialize() {
        initChess();
    }

    private Chess chess;
    private Node selectedPiece;
    private Node pieceShowingMoves;
    private ArrayList<StackPane> StackPaneList = new ArrayList<>();
    private ArrayList<ImageView> ImageViewList = new ArrayList<>();
    private double StartX;
    private double StartY;

    @FXML
    private ImageView PB1, PB2, PB3, PB4, PB5, PB6, PB7, PB8,
            RB1, RB2, BB1, BB2, KB1, KB2, QB, KB,
            PW1, PW2, PW3, PW4, PW5, PW6, PW7, PW8,
            RW1, RW2, BW1, BW2, KW1, KW2, QW, KW;

    @FXML
    private GridPane grid; 
    @FXML
    private TextField moveId, fileNameSave, fileNameLoad;
    @FXML
    private Button playMoveButton;
    @FXML
    private Button saveGame, loadGame;
    @FXML
    private Text moveTurnText;


    private void initChess() {
        chess = new Chess();
        fixNodeIndex();
    }

    private void fixNodeIndex(){
        ObservableList<Node> children = grid.getChildren();
        for (Node node : children) {
            ImageViewList.add((ImageView) node);
            if (GridPane.getRowIndex(node) == null) {
                GridPane.setRowIndex(node, 0);
            }
            if (GridPane.getColumnIndex(node) == null) {
                GridPane.setColumnIndex(node, 0);
            }
        }

        // adding 64 stackpanes to the grid
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                StackPane sp = new StackPane();
                grid.add(sp, i, j);
                sp.toBack();
                StackPaneList.add(sp);
                sp.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        removeLegalMoves(grid);
                        selectedPiece = null;
                        pieceShowingMoves = null;
                    }
                });
            }
            
        }
    }

    public void showLegalMoves(int row, int column, GridPane grid){
        if(!chess.getImageByRowColumnIndex(column, row, grid).getCursor().equals(Cursor.OPEN_HAND)) 
            return;

        if(chess.getImageByRowColumnIndex(column, row, grid)!=null){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chess.validateMoveChess(column, row, j, i)) {
                        StackPane sp = new StackPane();
                        sp.setStyle("-fx-background-color: green;");
                        sp.setOpacity(0.5);
                        sp.setCursor(Cursor.HAND);
                        grid.add(sp, j, i);
                        sp.toFront();
                        sp.setOnMouseClicked(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event) {
                                Node node = (Node) sp;
                                System.out.println(node);
                                // klikker på mulig trekk
                                int fromRow = GridPane.getRowIndex(selectedPiece);
                                int fromColumn = GridPane.getColumnIndex(selectedPiece);
                                // movePiece(chess.moveIntToText(fromColumn, fromRow, GridPane.getColumnIndex(node) , GridPane.getRowIndex(node)));
                                movePiece(fromColumn, fromRow, GridPane.getColumnIndex(node) , GridPane.getRowIndex(node));
                                }
                            });
                        }
                    }
                }
            }
        }

    private void removeLegalMoves(GridPane grid) {
        ObservableList<Node> children = grid.getChildren();
        List<StackPane> toRemove = new ArrayList<>();
        for (Node node : children) {
            if (node instanceof StackPane) {
                StackPane sp = (StackPane) node;
                if (!StackPaneList.contains(sp)) {
                    toRemove.add(sp);
                }
            }
        }
        for (StackPane stackPane : toRemove) {
            grid.getChildren().remove(stackPane);
        }
    }

    @FXML
    private void moveButtonPressed() throws FileNotFoundException {
        String textInput = moveId.getText().toLowerCase();
        if (!chess.validateTextInput(textInput)) {
            System.out.println("Invalid input");
            return;
        }
        if(chess.pawnPromotionTime()){
            chess.pawnPromotionVisual(textInput,grid);
            chess.pawnPromotion(textInput);
            if(!chess.WhiteToMove()) {
                moveTurnText.setText("White to move");
                moveTurnTextChange();
                }
            else {moveTurnText.setText("Black to Move");
                moveTurnTextChange();
            }
        }
        else {
            movePiece(textInput);
        }
    }

    private void movePiece(String move) {
        removeLegalMoves(grid);
        if (chess.validateMoveChess(move)) {
            // visual
            chess.visualMove(move, grid);
            // DB
            chess.move(move);
            // 
            moveTurnTextChange();
        }
    }

    private void movePiece(int fromFile, int fromRank, int toFile, int toRank) {
        movePiece(chess.moveIntToText(fromFile, fromRank, toFile, toRank));
    }

    private void moveTurnTextChange() {
        if(chess.matchResult().length()==0&&!chess.pawnPromotionTime()){

            if (chess.WhiteToMove()){
                moveTurnText.setText("White to move");
                for (ImageView imageView : ImageViewList) {
                    if (imageView.getId().charAt(1) == 'W')
                        imageView.setCursor(Cursor.OPEN_HAND);
                    else
                        imageView.setCursor(Cursor.DEFAULT);
                }
            }
            else{ moveTurnText.setText("Black to move");
            for (ImageView imageView : ImageViewList) {
                if (imageView.getId().charAt(1) == 'B')
                    imageView.setCursor(Cursor.OPEN_HAND);
                else
                    imageView.setCursor(Cursor.DEFAULT);
            }}
     
        }
        else if(chess.matchResult().length()==0&&chess.pawnPromotionTime()){
            moveTurnText.setText("Type desired piece");

        }
        else {moveTurnText.setText(chess.matchResult());
            playMoveButton.setDisable(true);
    }
    }

    @FXML
    private void mousePressed(Event event) {
        selectedPiece = (Node) event.getTarget();
        for (Node node : StackPaneList) {
            node.toFront();
        }
        MouseEvent mouseEvent = (MouseEvent) event;
        StartX = mouseEvent.getSceneX() - ((Node) event.getTarget()).getTranslateX();
        StartY = mouseEvent.getSceneY() - ((Node) event.getTarget()).getTranslateY();
    }

    @FXML
    private void followMouse(Event event) {
        // // sjekk om du har valgt svart eller hvit brikke
        MouseEvent mouseEvent = (MouseEvent) event;
        Node el = (Node) event.getTarget();
        if (chess.WhiteToMove() && selectedPiece.getId().charAt(1) == 'B')
            return;
        if (!chess.WhiteToMove() && selectedPiece.getId().charAt(1) == 'W')
            return;
        el.setTranslateX(mouseEvent.getSceneX() - StartX);
        el.setTranslateY(mouseEvent.getSceneY() - StartY);
    }
    
    @FXML
    private void mouseReleased(Event event) {

        for (Node imageViewNode : ImageViewList) {
            imageViewNode.toFront();
        }
        removeLegalMoves(grid);

        Node el = (Node) event.getTarget();

        // klikket på brikke
        if (selectedPiece.getTranslateX() == 0 || selectedPiece.getTranslateY() == 0) {
            System.out.println(el.getId());
            if (pieceShowingMoves == selectedPiece){
                System.out.println("Piece showing moves: " + pieceShowingMoves);
                System.out.println("Selected piece: " + selectedPiece);
                pieceShowingMoves = null;
                return;
            }

            int nodeRow = GridPane.getRowIndex(el);
            int nodeColumn = GridPane.getColumnIndex(el);  
        
            System.out.println("showing moves");
            showLegalMoves(nodeRow, nodeColumn, grid);
            pieceShowingMoves = selectedPiece;
        }
        // flyttet på brikke
        else {
        MouseEvent mouseEvent = (MouseEvent) event;
        Node toNode = mouseEvent.getPickResult().getIntersectedNode();
        System.out.println(toNode);
        ImageView movePiece = (ImageView) selectedPiece;
            if (toNode instanceof StackPane) {
                int toRowIndex = GridPane.getRowIndex(toNode);
                int toColIndex = GridPane.getColumnIndex(toNode);
                int fromRow = GridPane.getRowIndex(selectedPiece);
                int fromColumn = GridPane.getColumnIndex(selectedPiece);
                movePiece(fromColumn, fromRow, toColIndex, toRowIndex);
                movePiece.setTranslateX(0);
                movePiece.setTranslateY(0);
            }
            else {
                System.out.println("error");
            }
        }
        ArrayList<StackPane> tmpList = new ArrayList<>();
    for (Node node : grid.getChildren()) {
        if (node instanceof StackPane) {
            StackPane stackPane = (StackPane) node;
            if (!(StackPaneList.contains(stackPane))) {
                tmpList.add(stackPane);
            }
        }
    }
    for (StackPane stackPane : tmpList) {
        stackPane.toFront();
        }
    }

    @FXML
    private void saveGame() throws FileNotFoundException{
            String file = "src/main/resources/ChessProject/SavedGames/" + fileNameSave.getText();
            chess.saveGame(file);
    }

    @FXML
    private void loadGame() throws IOException, InterruptedException, IllegalAccessException {
        String file = "src/main/resources/ChessProject/SavedGames/" + fileNameLoad.getText();
        chess.loadGame(file,grid);
        moveTurnTextChange();
    }
}

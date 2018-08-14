package App;

import java.util.ArrayList;
import java.util.Random;
import Heuristic.gridFunctions;
import Heuristic.gridFunctions.Tile;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class gridUI extends Application {
	
	private static Scene scene;
	private static int ROW = 120, COL = 160;
	private static GridPane root;
	private static Tile[] gridGUI;
	public static ArrayList<Tile> path;
	public static ArrayList<Tile> expandedTiles;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void setRootScene() {
		root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setVgap(0);
		root.setHgap(0);
	}

	public void setCellStrokeColor(Color strokeColor, ArrayList<Tile> CellsToColor) {
		for(int i = 0; i < CellsToColor.size(); i++) {
  		  Tile curr = CellsToColor.get(i);
  		  Rectangle sq = new Rectangle(5,5);
  		  sq.setFill(Color.valueOf((gridGUI[curr.pos.x * 160 + curr.pos.y].cellColor)));
  		  sq.setStroke(strokeColor);
		  root.setRowIndex(sq, curr.pos.x);
		  root.setColumnIndex(sq, curr.pos.y);
		  root.getChildren().add(sq);
		}
	}
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Heuristic Search");

		setRootScene();
		
		gridGUI = gridFunctions.getGrid();
		setGrid();
		
		scene = new Scene(root);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
		      if(key.getCode()==KeyCode.A) {
		    	  setCellStrokeColor(Color.PINK, path);
		      }
		      if(key.getCode() == KeyCode.E) {
		    	  setCellStrokeColor(Color.CYAN, expandedTiles);
		      }
		});
		stage.setScene(scene);
		stage.show();
		
	}
	
	public static void setExpanded(ArrayList<Tile> e) {
		expandedTiles = e;
	}
	
	/**
	 * method for setting up the grid cells
	 * Each cell is a rectangle and are distinguished by colors
	 * Goal is blue
	 * Start is red
	 * Hard to Traverse is gray
	 * Blocked is black
	 * Unblocked is white
	 */
	public void setGrid() {
		for(int i = 0; i < ROW; i++) {
			for(int j = 0; j < COL; j++) {
				Rectangle sq = new Rectangle(5,5);
		  		sq.setFill(Color.valueOf((gridGUI[i * 160 + j].cellColor)));
		  		sq.setStroke(Color.WHITE);
		  		root.setRowIndex(sq, i);
				root.setColumnIndex(sq, j);
				root.getChildren().add(sq);	
			}	
		}	
		return;
	}

}

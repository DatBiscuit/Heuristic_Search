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

public class gridUI extends Application{
	
	//private static Stage stage;
	private static Scene scene;
	private static int ROW = 120, COL = 160;
	private static GridPane root;
	private static Tile[] gridGUI;
	public static ArrayList<Tile> path;
	public static ArrayList<Tile> expanded;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("AI Project 2");
		//GridPane grid = new GridPane();
		root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setVgap(0);
		root.setHgap(0);
		
		gridGUI = gridFunctions.getGrid();
		setGrid();
		
		scene = new Scene(root);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
		      if(key.getCode()==KeyCode.A) {
		          //System.out.println("You pressed the key");
		    	  for(int i = 0; i < path.size(); i++) {
		    		  Tile curr = path.get(i);
		    		  Rectangle sq = new Rectangle(5,5);
		    		  switch(gridGUI[curr.pos.x * 160 + curr.pos.y].type) {
						case "1":
							sq.setFill(Color.WHITE);
							sq.setStroke(Color.PINK);
							break;
						case "2":
							sq.setFill(Color.GREY);
							sq.setStroke(Color.PINK);
							break;
						case "0":
							sq.setFill(Color.BLACK);
							sq.setStroke(Color.PINK);
							break;
						case "s":
							sq.setFill(Color.RED);
							sq.setStroke(Color.PINK);
							break;
						case "g":
							sq.setFill(Color.BLUE);
							sq.setStroke(Color.PINK);
							break;
						case "a":
							sq.setFill(Color.GREEN);
							sq.setStroke(Color.PINK);
							break;
						case "b":
							sq.setFill(Color.GREEN);
							sq.setStroke(Color.PINK);
							break;
							
						
						}
						
					
						root.setRowIndex(sq, curr.pos.x);
						root.setColumnIndex(sq, curr.pos.y);
						root.getChildren().add(sq);
		    	  }
		      }
		      if(key.getCode() == KeyCode.E) {
		    	  for(int i = 0; i < expanded.size(); i++) {
		    		  Tile curr = expanded.get(i);
		    		  Rectangle sq = new Rectangle(5,5);
		    		  switch(gridGUI[curr.pos.x * 160 + curr.pos.y].type) {
						case "1":
							sq.setFill(Color.WHITE);
							sq.setStroke(Color.CYAN);
							break;
						case "2":
							sq.setFill(Color.GREY);
							sq.setStroke(Color.CYAN);
							break;
						case "0":
							sq.setFill(Color.BLACK);
							sq.setStroke(Color.CYAN);
							break;
						case "s":
							sq.setFill(Color.RED);
							sq.setStroke(Color.CYAN);
							break;
						case "g":
							sq.setFill(Color.BLUE);
							sq.setStroke(Color.CYAN);
							break;
						case "a":
							sq.setFill(Color.GREEN);
							sq.setStroke(Color.CYAN);
							break;
						case "b":
							sq.setFill(Color.GREEN);
							sq.setStroke(Color.CYAN);
							break;
							
						
						}
						
					
						root.setRowIndex(sq, curr.pos.x);
						root.setColumnIndex(sq, curr.pos.y);
						root.getChildren().add(sq);
		    	  }
		      }
		});
		stage.setScene(scene);
		stage.show();
		
	}
	
	public static void setExpanded(ArrayList<Tile> e) {
		expanded = e;
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
		
		int counter = 1;
		//Random num = new Random(); //tester to make sure the cells are different colors
		
		for(int i = 0; i < ROW; i++) {
			for(int j = 0; j < COL; j++) {
				Rectangle sq = new Rectangle(5,5);
				/*
				if(num.nextInt(11) > 5) 
					sq.setStroke(Color.GRAY);
				else
					sq.setStroke(Color.BLUE);
					*/
				switch(gridGUI[i*160 + j].type) {
				case "1":
					sq.setFill(Color.WHITE);
					sq.setStroke(Color.WHITE);
					break;
				case "2":
					sq.setFill(Color.GREY);
					sq.setStroke(Color.WHITE);
					break;
				case "0":
					sq.setFill(Color.BLACK);
					sq.setStroke(Color.WHITE);
					break;
				case "s":
					sq.setFill(Color.RED);
					sq.setStroke(Color.WHITE);
					break;
				case "g":
					sq.setFill(Color.BLUE);
					sq.setStroke(Color.WHITE);
					break;
				case "a":
					sq.setFill(Color.GREEN);
					sq.setStroke(Color.WHITE);
					break;
				case "b":
					sq.setFill(Color.GREEN);
					sq.setStroke(Color.WHITE);
					break;
					
				
				}
				
			
				root.setRowIndex(sq, i);
				root.setColumnIndex(sq, j);
				root.getChildren().add(sq);
					
			}
			/*
			Label rownum = new Label(""+counter+"");
			rownum.setFont(new Font(3));
			root.setColumnIndex(rownum, COL);
			root.setRowIndex(rownum, i);
			root.getChildren().add(rownum);
			counter++;
			*/
			
			
			
		}
		
		return;
	}

}

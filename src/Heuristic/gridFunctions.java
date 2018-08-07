package Heuristic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class gridFunctions implements Serializable {
	
	//variables for grid
	//public String grid[];
	private int counter;
	public Random seed;
	public int x;
	public int y;
	public int ROW;
	public int COL;
	public static Tile[] grid;
	private boolean end = false;
	private int cellCount = 0;
	private int[] lastIndex = new int[2];
	private String lastDirection;
	private String lastTurn = "";
	public static int goal;
	public static int start;
	
	public static Tile[] getGrid() {
		return grid;
	}
	
	//constructor for vector where each block is located
	//x is rows, y is col
	public gridFunctions(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}
	
	//constructor for the tiles on the grid
	public static class Tile implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public gridFunctions pos;
		public String type;
		public int parent;
		public int[] parents = new int[6];
		public double distance;
		public double[] distances = new double[6];
		
		Tile(int x, int y, String t) {
			gridFunctions position = new gridFunctions(x, y);
			pos = position;
			type = t;
		}
	}
	
	public static int getPositionOnGrid(gridFunctions v) {
		return (v.x * 160) + v.y;
	}
	
	
	/**
	 * Constructor used for initializing the grid
	 * @param rows
	 * @param cols
	 * @param randS
	 */
	
	public gridFunctions(int rows, int cols, int randS) {
		
		ROW = rows;
		COL = cols;
		int counter = 0;
		
		if(randS != -1) {
			this.seed = new Random();
			this.seed.setSeed(randS);
		}
		
		
		gridFunctions.grid = new Tile[ROW*COL];
		
		for(int i = 0; i < ROW; i ++) {
			for(int j = 0; j < COL; j++) {
				Tile t = new Tile(i, j, "1");
				grid[i*160 + j] = t;
				counter++;
			}
		}
		System.out.println(counter);
		
		
		
		//grid = new String[rows*cols];
		
		//intialize all cells as unblocked
		/*
		for(int i=0; i<grid.length; i++) {
			grid[i] = "1";
		}
		*/
	}
	
	public void traverseFile(String f) {
		counter = 0;
		try (Stream<String> stream = Files.lines(Paths.get(f))) {
	        stream.forEach(s -> {	
	        	addText(s,counter);
	        });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	
	public void addText(String s, int r) {
		for(int i = 0; i < s.length(); i++) {
			grid[counter*160 + i].type = Character.toString(s.charAt(i));
		}
		++counter;
	}
	
	/**
	 * Used for printing out the grid to console
	 */
	public void printGrid() {
		for(int i = 0; i<grid.length; i++) {
			if(i%160 == 0) {
				System.out.println();
			}
			System.out.print(grid[i].type);
		}
		System.out.println();
		System.out.println(cellCount);
		return;
	}
	
	
	/**
	 * returns true with probability p and false with probability 1-p
	 * @param d
	 * @return
	 */
	public boolean probability(double d) {
		if(d < 0 || d > 1) {
			throw new IllegalArgumentException();
		}
		Random p2 = this.seed;
		return p2.nextDouble() < d;
	}
	
	/**
	 * creates the start and goal cells
	 */
	public void startGoalCells() {
		boolean startPlaced = false;
		boolean goalPlaced = false;
		boolean onSide = false;
		Random num = this.seed;
		int r, c;
		
		while(!startPlaced) {
			if(probability(0.5)) {
				r = num.nextInt(20);
				c = num.nextInt(160);
			} else {
				r = num.nextInt(120);
				c = num.nextInt(20);
				onSide = true;
			}
			
			if(!grid[r*160 + c].type.equals("0")) {
				grid[r*160 + c].type = "s";
				start = r*160+c;
				startPlaced = true;
			}
		}
		
		while(!goalPlaced) {
			if(onSide) {
				r = num.nextInt(119);
				c = num.nextInt(160-140) + 140;
			}
			else {
				r = num.nextInt(120 - 100) + 100;
				c = num.nextInt(160);
			}
			
			if(!grid[r*160 + c].type.equals("0")) {
				grid[r*160 + c].type = "g";
				goal = r*160+c;
				goalPlaced = true;
			}
		}
	}
	
	/*
	 * Adds blocked cells to grid
	 * 20% of the grids cells are blocked
	 * These are the black cells
	 */
	public void blockCells() {
		int blocked = (int) (0.2*ROW*COL);
		Random num = this.seed;
		int i = 0;
		while(i < blocked) {
			int r = num.nextInt(120);
			int c = num.nextInt(160);
			
			if(!grid[r*160 + c].type.equals("a")) {
				grid[r*160 + c].type = "0";
				i++;
			}
		}
	}
	
	/**
	 * Adds hard to traverse cells to grid
	 * These are the gray cells
	 * @param anchors
	 */
	public void hardTraverse(int anchors) {
		
		List<Tile> ht = new ArrayList<Tile>();
		Random r = this.seed,c = this.seed;
		int row,col;
		
		int i = 0;
		if(anchors == 0) {
			anchors = 8;
		}
	
		while(i < 8) {
			
			//selecting random anchor points for 31x31 dimension
			while(1 == 1) {
				row = r.nextInt(ROW);
				col = c.nextInt(COL);
				Tile t = new Tile(row, col, "2");
				if(!ht.contains(t)) {
					ht.add(t);
					i++;
					break;
				}
			}
			
			int rowMin = Math.max(0, row-15);
			int rowMax = Math.min(ROW, row + 15);
			int colMin = Math.max(0, col - 15);
			int colMax = Math.min(COL, col + 15);
			
			for(int x = rowMin; x < rowMax; x++) {
				for(int y = colMin; y < colMax; y++) {
					if(probability(0.5)) {
						
						int cell = getPositionOnGrid(new gridFunctions(x, y));
						this.grid[cell].type = "2";
						
					}
				}
			}
			
		}
		
		return;
		//return ht;
	}
	
	public void storeGrid()  {
		try {
			URL url = getClass().getResource("grid.ser");
			File file = new File(url.getPath());
		FileOutputStream fileout = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileout);
		out.writeObject(grid);
		out.close();
		fileout.close();
		System.out.println("serialized data");
		} catch (IOException i){
			i.printStackTrace();
		}
	}
	
	public void resetGrid() {
		try {
			URL url = getClass().getResource("grid.ser");
			File file = new File(url.getPath());
			FileInputStream filein = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(filein);
			gridFunctions.grid = (Tile[])in.readObject();
			in.close();
			filein.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
	}
	
	public void highwayCells() {
		storeGrid();
		int highwayNum = 4;
		Random r = this.seed;
		int cells[][] = new int[120][160];
		int attempts = 0;
		while(highwayNum != 0) {
			if(attempts >= 20) {
				resetGrid();
				highwayNum = 4;
				cellCount = 0;
				this.end = false;
				attempts = 0;
				cells = new int[120][160];
				continue;
			}
			cells = getHighway(cells, r);
			if(cellCount < 100 || cells == null) {
				cells = new int[120][160];
				cellCount = 0;
				this.end = false;
				attempts++;
				continue;
			}
				
			
			for(int i = 0; i < 120; i++) {
				for(int j = 0; j < 160; j++) {
					if(cells[i][j] == 1) {
						if(grid[i*160 + j].type.equals("2")) {
							grid[i*160 + j].type = "b";
						} else {
							grid[i*160 + j].type = "a";
						}
					}
				}
			}
			System.out.println();
			System.out.println(cellCount);
			this.end = false;
			cellCount = 0;
			highwayNum--;
		}
	}
	
	public int[][] getHighway(int highway[][], Random ran) {
		Random num = ran;
		int r=0,c=0;
		boolean side = false;
		boolean turn = false;
		boolean start = true;
		
		
		//no corner
		while(r == 0 && c == 0) {
			if(probability(0.5)) {
				c = num.nextInt(159);
				side = false;
				if(grid[r*160+c].type.equals("a"))
					continue;
			}
			else {
				r = num.nextInt(119);
				side = true;
				if(grid[r*160+c].type.equals("a"))
					continue;
			}
			
		}
		
		lastIndex[0] = r;
		lastIndex[1] = c;
		
		while(!this.end) {
			if(highway == null) {
				return null;
			}
			highway = addTwenty(highway, side, turn, start);
			start = false;
			if(probability(0.6)) 
				turn = false;
			else
				turn = true;
		}
		
		
		
		return highway;
	}
	
	public boolean conflict(int[][] highway) {
		if(grid[lastIndex[0]*160 + lastIndex[1]].type.equals("a") 
				|| grid[lastIndex[0]*160 + lastIndex[1]].type.equals("g") 
				|| grid[lastIndex[0]*160 + lastIndex[1]].type.equals("s")
				|| grid[lastIndex[0]*160 + lastIndex[1]].type.equals("b")) {
			return true;
		} else {
			if(highway[lastIndex[0]][lastIndex[1]] == 1) {
				return true;
			}
			return false;
		}
	}
	
	public boolean border() {
		if(lastIndex[1] == 0) {
			return true;
		}
		if(lastIndex[0] == 0) {
			return true;
		}
		if(lastIndex[0] == 119) {
			return true;
		}
		if(lastIndex[1] == 159) {
			return true;
		}
		
		
		return false;
	}
	
	
	public int[][] addTwenty(int[][] highway, boolean side, boolean turn, boolean start) {
		
		//initial highway
		if(start) { 
			highway[lastIndex[0]][lastIndex[1]] = 1;
			for(int i = 0; i <= 20; i++) {
				try {
					if(side) {
						lastIndex[1] += 1;
						this.lastDirection = "c";
					}
					else {
						lastIndex[0] += 1; 
						this.lastDirection = "r";
					}
					if(conflict(highway)) {
						return null;
					}
					highway[lastIndex[0]][lastIndex[1]] = 1;
					cellCount++;
				/*if(side) {
					lastIndex[1] += 1;
					this.lastDirection = "c";
				}
				else {
					lastIndex[0] += 1; 
					this.lastDirection = "r";
				}
				*/
				
				} catch(IndexOutOfBoundsException e) {
					this.end = true;
					return highway;
				}
					
			}
		} 
		
		if(turn) {
			//sets it to either go left or right
			if(probability(0.5)) { // going left
				if(this.lastDirection.equals("r")) {
					for(int i = 0; i <= 20; i++) {
						try {
							lastIndex[1] -= 1;
							if(conflict(highway)) {
								return null;
							}
							highway[lastIndex[0]][lastIndex[1]] = 1;
							cellCount++;
							if(border()) {
								this.end = true;
								return highway;
							}
						} catch(IndexOutOfBoundsException e) {
							this.end = true;
							return highway;
						}
							
					}
					this.lastDirection = "c";
					this.lastTurn = "cneg";
				} else {
					for(int i = 0; i <= 20; i++) {
						try {
							lastIndex[0] -= 1;
							if(conflict(highway)) {
								return null;
							}
							highway[lastIndex[0]][lastIndex[1]] = 1;
							cellCount++;
							if(border()) {
								this.end = true;
								return highway;
							}
						} catch(IndexOutOfBoundsException e) {
							this.end = true;
							return highway;
						}
							
					}
					this.lastDirection = "r";
					this.lastTurn = "rneg";
				}
			} else { //going right
				if(this.lastDirection.equals("r")) {
					for(int i = 0; i <= 20; i++) {
						try {
							lastIndex[1] += 1;
							if(conflict(highway)) {
								return null;
							}
							highway[lastIndex[0]][lastIndex[1]] = 1;
							cellCount++;
							if(border()) {
								this.end = true;
								return highway;
							}
						} catch(IndexOutOfBoundsException e) {
							this.end = true;
							return highway;
						}
							
					}
					this.lastDirection = "c";
					this.lastTurn = "cpos";
				} else {
					for(int i = 0; i <= 20; i++) {
						try {
							lastIndex[0] += 1;
							if(conflict(highway)) {
								return null;
							}
							highway[lastIndex[0]][lastIndex[1]] = 1;
							cellCount++;
							if(border()) {
								this.end = true;
								return highway;
							}
						} catch(IndexOutOfBoundsException e) {
							this.end = true;
							return highway;
						}
							
					}
					this.lastDirection = "r";
					this.lastTurn = "rpos";
				}
			}
		} else { //going straight
			
			//if a turn hasnt been done yet
			if(this.lastTurn.equals("")) {
				if(this.lastDirection.equals("c")) {
					for(int i = 0; i <= 20; i++) {
						try {
							this.lastIndex[1] += 1;
							if(conflict(highway)) {
								return null;
							}
							highway[lastIndex[0]][lastIndex[1]] = 1;
							cellCount++;
							if(border()) {
								this.end = true;
								return highway;
							}
						} catch(IndexOutOfBoundsException e) {
							this.end = true;
							return highway;
						}
							
					}
				} else {
					for(int i = 0; i <= 20; i++) {
						try {
							this.lastIndex[0] += 1;
							if(conflict(highway)) {
								return null;
							}
							highway[lastIndex[0]][lastIndex[1]] = 1;
							cellCount++;
							if(border()) {
								this.end = true;
								return highway;
							}
						} catch(IndexOutOfBoundsException e) {
							this.end = true;
							return highway;
						}
							
					}
				}
			}
			
			//a turn has been done
			if(this.lastTurn.equals("rpos")) {
				
				for(int i = 0; i <= 20; i++) {
					try {
						lastIndex[0] += 1;
						if(conflict(highway)) {
							return null;
						}
						highway[lastIndex[0]][lastIndex[1]] = 1;
						cellCount++;
						if(border()) {
							this.end = true;
							return highway;
						}
					} catch(IndexOutOfBoundsException e) {
						this.end = true;
						return highway;
					}
						
				}
				this.lastDirection = "r";
				this.lastTurn = "rpos";
				
			} else if(this.lastTurn.equals("rneg")) {
				for(int i = 0; i <= 20; i++) {
					try {
						lastIndex[0] -= 1;
						if(conflict(highway)) {
							return null;
						}
						highway[lastIndex[0]][lastIndex[1]] = 1;
						cellCount++;
						if(border()) {
							this.end = true;
							return highway;
						}
					} catch(IndexOutOfBoundsException e) {
						this.end = true;
						return highway;
					}
						
				}
				this.lastDirection = "r";
				this.lastTurn = "rneg";
				
			} else if(this.lastTurn.equals("cpos")) {
				for(int i = 0; i <= 20; i++) {
					try {
						lastIndex[1] += 1;
						if(conflict(highway)) {
							return null;
						}
						highway[lastIndex[0]][lastIndex[1]] = 1;
						cellCount++;
						if(border()) {
							this.end = true;
							return highway;
						}
					} catch(IndexOutOfBoundsException e) {
						this.end = true;
						return highway;
					}
						
				}
				this.lastDirection = "c";
				this.lastTurn = "cpos";
				
			} else if(this.lastTurn.equals("cneg")) {
				for(int i = 0; i <= 20; i++) {
					try {
						lastIndex[1] -= 1;
						if(conflict(highway)) {
							return null;
						}
						highway[lastIndex[0]][lastIndex[1]] = 1;
						cellCount++;
						if(border()) {
							this.end = true;
							return highway;
						}
					} catch(IndexOutOfBoundsException e) {
						this.end = true;
						return highway;
					}
						
				}
				this.lastDirection = "c";
				this.lastTurn = "cneg";
			}
			
			
			return highway;
		}
		return highway;
		
	}
	
	

}

package Heuristic;

import java.awt.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import Heuristic.gridFunctions.Tile;

public class aStar {
	
	public static int test;
	
	public static Comparator<Tile> TileComparator = new Comparator<Tile>() {
		public int compare(Tile one, Tile two) {
			/*one.distance = gridFunctions.grid[one.parent].distance;
			two.distance = gridFunctions.grid[two.parent].distance;
			*/
			
			if(one.distance > two.distance) {
				return 1;
			} else if(one.distance < two.distance) {
				return -1;
			} else {
				return 0;
			}
			
		}
	};
	
	public static Comparator<Tile> TileComparator2 = new Comparator<Tile>() {
		public int compare(Tile one, Tile two) {
			/*one.distance = gridFunctions.grid[one.parent].distance;
			two.distance = gridFunctions.grid[two.parent].distance;
			*/
			
			if(one.distances[test] > two.distances[test]) {
				return 1;
			} else if(one.distances[test] < two.distances[test]) {
				return -1;
			} else {
				return 0;
			}
			
		}
	};
	
	
	public static int getMinValue(double[][] numbers) {
        double temp = numbers[0][0];
        int index = 0;
        for (int j = 0; j < numbers.length; j++) {
            for (int i = 0; i < numbers[j].length; i++) {
                if (numbers[j][i] < temp ) {
                    temp = numbers[j][i];
                    index = j*160 + i;
                }
            }
        }
        return index;
    }
	
	
	
	public static double CalcCost(Tile current, Tile neighbor) {
		boolean diagonal = false;
		gridFunctions v1 = current.pos;
		gridFunctions v2 = neighbor.pos;
		String t1 = current.type;
		String t2 = neighbor.type;
		if(v2.x - v1.x != 0 && v2.y-v1.y != 0) {
			diagonal = true;
		}
		String terrain[] = new String[2];
		terrain[0] = current.type;
		terrain[1] = neighbor.type;
		return cost(terrain, diagonal);
	}
	
	public static double cost(String[] t, boolean diagonal) {
		double cost = 0;
		
		if(diagonal) {
			for(int i = 0; i < t.length; i++) {
				switch(t[i]) {
				case "0":
					cost += Double.POSITIVE_INFINITY;
				case "1":
					cost += Math.sqrt(2);
				case "2":
					cost += Math.sqrt(8);
				case "a":
					cost += Math.sqrt(0.125);
				case "b":
					cost += Math.sqrt(0.5);
				}
			}
		} else {
			for(int i = 0; i < t.length; i++) {
				switch(t[i]) {
				case "0":
					cost += Double.POSITIVE_INFINITY;
				case "1":
					cost += 1;
				case "2":
					cost += 2;
				case "a":
					cost += 0.25;
				case "b":
					cost += 0.5;
				}
			}
		}
		return cost;
		
	}
	
	
	public static ArrayList<Tile> seqASS(double w1, double w2, int[] h) {
		gridFunctions.Tile[] grid = gridFunctions.getGrid();
		int s = gridFunctions.start;
		int g = gridFunctions.goal;
		
		ArrayList<ArrayList<Tile>> expanded = new ArrayList<ArrayList<Tile>>();
		Tile pgrid[][] = new Tile[h.length][19200];
		double ggrid[][][] = new double[h.length][120][160];
		double fgrid[][][] = new double[h.length][120][160];
		double hgrid[][][] = new double[h.length][120][160];
		ArrayList<PriorityQueue<Tile>> fringe = new ArrayList<PriorityQueue<Tile>>();
		
		for(int p = 0; p < h.length; p++) {
			for(int k = 0; k < 120; k++) {
				for(int l = 0; l < 160; l++) {
					ggrid[p][k][l] = Double.POSITIVE_INFINITY;
					hgrid[p][k][l] = Double.POSITIVE_INFINITY;
					fgrid[p][k][l] = Double.POSITIVE_INFINITY;
				}
			}
		}
		
		for(int i = 0; i < h.length; i++) {
			//hgrid[i] = computeHeuristics(hgrid[i], h[i], grid[g], w1);
			test = i;
			PriorityQueue<Tile> toAdd = new PriorityQueue<Tile>(10000, TileComparator2);
			fringe.add(toAdd);
			grid[s].parents[i] = -1;
			grid[s].distances[i] = 0;
			pgrid[i][grid[s].pos.x *160 + grid[s].pos.y] = null;
			fringe.get(i).add(grid[s]);
			ArrayList<Tile> adding = new ArrayList<Tile>();
			expanded.add(adding);
			System.out.println(h[i]);
			hgrid[i] = computeHeuristics(hgrid[i], h[i], grid[g], w1);
			fgrid[i][grid[s].pos.x][grid[s].pos.y] = hgrid[i][grid[s].pos.x][grid[s].pos.y];

			ggrid[i][grid[s].pos.x][grid[s].pos.y] = 0;
		}
		
		Tile minKey; //cost and coordinate
		Tile minKeyI;
		
		while(!fringe.get(0).isEmpty()) {
			//System.out.println(fringe.get(0).size());
			test = 0;
			minKey = fringe.get(0).peek();
			while(expanded.get(0).contains(minKey)) {
				fringe.get(0).remove(minKey);
				minKey = fringe.get(0).peek();
			}
			
			for(int i = 1; i < h.length; i++) {
				test = i;
				minKeyI = fringe.get(i).peek();
				while(expanded.get(i).contains(minKeyI) && minKeyI != null) {
					fringe.get(i).remove(minKeyI);
					minKeyI = fringe.get(i).peek();
				}
				//System.out.println(minKeyI);
				//mSystem.out.println(minKeyI == null);
				if(minKeyI != null && 
						fgrid[i][minKeyI.pos.x][minKeyI.pos.y] < 
						w2*fgrid[0][minKey.pos.x][minKey.pos.y]) {
					
					//System.out.println(fgrid[i][minKeyI.pos.x][minKeyI.pos.y] + " " +fgrid[0][minKey.pos.x][minKey.pos.y]);
					if(ggrid[i][grid[g].pos.x][grid[g].pos.y] <
							fgrid[i][minKeyI.pos.x][minKeyI.pos.y]) {
						//System.out.println("poof");
						if(ggrid[i][grid[g].pos.x][grid[g].pos.y] < Double.POSITIVE_INFINITY) {
							return traversePath(grid[g], i);
						}
						
					} else {
						test = i;
						expanded.get(i).add(minKeyI);
						//System.out.println(expanded.get(i).size() + " " + i);
						for(int r = -1; r < 2; r++) {
							for(int c = -1; c < 2; c++) {
								int neighbor = (minKeyI.pos.x+r)*160 + (minKeyI.pos.y+c);
								
								if(isBorderPassing(r,c,minKeyI)) {
									continue;
								}
								//checks for in bounds, already in fringe, or blocked cell
								if(neighbor < 0 || neighbor >= 19200) {
									continue;
								}
								if(expanded.get(i).contains(grid[neighbor])) {
									continue;
								}
								if(grid[neighbor].type.equals("0")) {
									continue;
								}
								
								double tempCost = ggrid[i][minKeyI.pos.x][minKeyI.pos.y]
										+ CalcCost(grid[minKeyI.pos.x*160 + minKeyI.pos.y], grid[neighbor]);
								test = i;
								if(fringe.get(i).contains(grid[neighbor])) {
									if(tempCost >= ggrid[i][minKeyI.pos.x+r][minKeyI.pos.y+c]) {
										continue;
									}
								}
								grid[neighbor].distances[i] = fFcn(ggrid[i],hgrid[i], grid[neighbor]);
								grid[neighbor].parents[i] = minKeyI.pos.x*160 + minKeyI.pos.y;
								ggrid[i][minKeyI.pos.x+r][minKeyI.pos.y+c] = tempCost;
								fgrid[i][minKeyI.pos.x+r][minKeyI.pos.y+c] = fFcn(ggrid[i],hgrid[i], grid[neighbor]);
								fringe.get(i).add(grid[neighbor]);
								//System.out.println("ISH ADDING TO DA FRINGE");
							}
						}
					}
					
				} else {
					test = 0;
					if(ggrid[0][grid[g].pos.x][grid[g].pos.y] < 
							fgrid[0][minKey.pos.x][minKey.pos.y]) {
						if(ggrid[0][grid[g].pos.x][grid[g].pos.y] < Double.POSITIVE_INFINITY) {
							return traversePath(grid[g], 0);
						}
					}
					
					else {
						//System.out.println(expanded.get(0).size());
						test = 0;
						Tile cur0 = fringe.get(0).poll();
						expanded.get(0).add(cur0);
						for(int r = -1; r < 2; r++) {
							for(int c = -1; c < 2; c++) {
								int neighbor = (cur0.pos.x+r)*160 + (cur0.pos.y+c);
								
								if(isBorderPassing(r,c,cur0)) {
									continue;
								}
								//checks for in bounds, already in fringe, or blocked cell
								if(neighbor < 0 || neighbor >= 19200) {
									continue;
								}
								if(expanded.get(0).contains(grid[neighbor])) {
									continue;
								}
								if(grid[neighbor].type.equals("0")) {
									continue;
								}
								
								double tempCost = ggrid[0][cur0.pos.x][cur0.pos.y]
										+ CalcCost(grid[cur0.pos.x*160 + cur0.pos.y], grid[neighbor]);
								
								if(fringe.get(0).contains(grid[neighbor])) {
									if(tempCost >= ggrid[0][cur0.pos.x+r][cur0.pos.y+c]) {
										continue;
									}
								}
								grid[neighbor].distances[0] = fFcn(ggrid[0],hgrid[0], grid[neighbor]);
								grid[neighbor].parents[0] = cur0.pos.x*160 + cur0.pos.y;
								ggrid[0][cur0.pos.x+r][cur0.pos.y+c] = tempCost;
								fgrid[0][cur0.pos.x+r][cur0.pos.y+c] = fFcn(ggrid[0],hgrid[0], grid[neighbor]);
								fringe.get(0).add(grid[neighbor]);
							}
						}
					}
				}
			}
			
			
		}
		
		return null;
	}
	
	public static ArrayList<Tile> traversePath(Tile curr, int i) {
		ArrayList<Tile> path = new ArrayList<Tile>();
		while(curr.parents[i] != -1) {
			path.add(curr);
			curr = gridFunctions.grid[curr.parents[i]];
		}
		
		return path;
	}
	
	public static ArrayList<Tile> aStarSearch(double weight, int heuristic) {
		gridFunctions.Tile[] grid = gridFunctions.getGrid();
		int cols = 160;
		int rows = 120;
		int s = gridFunctions.start;
		int g = gridFunctions.goal;
		
		Tile pgrid[][] = new Tile[120][160];
		double ggrid[][] = new double[120][160];
		double fgrid[][] = new double[120][160];
		double hgrid[][] = new double[120][160];
		
		hgrid = computeHeuristics(hgrid, heuristic, grid[g], weight);
		
		ArrayList<Tile> expanded = new ArrayList<Tile>();
		grid[s].distance = 0;
		grid[s].parent = -1;
		
		PriorityQueue<Tile> fringe = new PriorityQueue<Tile>(10000, TileComparator);
		fringe.add(grid[s]);
		
		while(!fringe.isEmpty()) {
			
			//retrieve from fringe and expand
			Tile current = fringe.poll();
			if(expanded.contains(current)) {
				continue;
			}
			expanded.add(current);
			
			//when reached goal
			if(gridFunctions.getPositionOnGrid(current.pos) == g) {
			
				ArrayList<Tile> path = traverse(current);
				App.gridUI.setExpanded(expanded);
				return path;
			}
			
			for(int i = -1; i < 2; i++) {
				for(int j = -1; j < 2; j++) {
					int neighbor = (current.pos.x+i)*160 + (current.pos.y+j);
					
					if(isBorderPassing(i,j,current)) {
						continue;
					}
					//checks for in bounds, already in fringe, or blocked cell
					if(neighbor < 0 || neighbor >= 19200) {
						continue;
					}
					if(expanded.contains(grid[neighbor])) {
						continue;
					}
					if(grid[neighbor].type.equals("0")) {
						continue;
					}
					
					
					double tempCost = grid[current.pos.x*160 + current.pos.y].distance
							+ CalcCost(grid[current.pos.x*160 + current.pos.y], grid[neighbor]);
					
					if(fringe.contains(grid[neighbor])) {
						if(tempCost >= grid[neighbor].distance) {
							continue;
						}
						
					}
					
					grid[neighbor].parent = current.pos.x*160 + current.pos.y;
					pgrid[grid[neighbor].pos.x][grid[neighbor].pos.y] = current;
					ggrid[grid[neighbor].pos.x][grid[neighbor].pos.y] = tempCost;
					grid[neighbor].distance = fFcn(ggrid, hgrid, grid[neighbor]);
					fgrid[grid[neighbor].pos.x][grid[neighbor].pos.y] = grid[neighbor].distance;
					fringe.add(grid[neighbor]);
					
				}
			}
		}
		return null;
		
		
	}
	private static double fFcn(double[][] gGrid, double[][] hGrid, Tile cell) {
		return gGrid[cell.pos.x][cell.pos.y] + hGrid[cell.pos.x][cell.pos.y];
	}

	private static double[][] computeHeuristics(double[][] hgrid, int heuristic, Tile g, double w) {
		for(int i = 0; i < hgrid.length; i++) {
			for(int j = 0; j< hgrid[i].length; j++) {
				switch(heuristic) {
				case 0: //uniform cost search
					hgrid[i][j] = 0;
					break;
				case 1: //euclidean
					hgrid[i][j] = w * ((Math.pow(i-g.pos.x, 2) + Math.pow(j-g.pos.y, 2))/4);
					break;
				case 2: //manhattan distance
					hgrid[i][j] = w * (Math.abs(i-g.pos.x) + Math.abs(j-g.pos.y));
					break;
				case 3: //square root distance
					hgrid[i][j] = w * (Math.sqrt(Math.abs(i-g.pos.x) + Math.abs(j-g.pos.y)));
					break;
				case 4: //square distance
					hgrid[i][j] = w * (Math.pow(i-g.pos.x, 2) + Math.pow(j -g.pos.y,2));
					break;
				case 5: //radial
					hgrid[i][j] = w * (Math.max(Math.abs(i-g.pos.x), Math.abs(j-g.pos.y)));
					break;
				}
			}
		}
		
		
		return hgrid;
	}
	
	public static boolean isBorderPassing(int x, int y, Tile curr) {
		//check if it is a border cell
		if(curr.pos.x + x < 0 || curr.pos.x +x > 119) {
			return true;
		}
		if(curr.pos.y + y < 0 || curr.pos.y + y > 159) {
			return true;
		}
		
		return false;
	}
	
	public static ArrayList<Tile> traverse(Tile current) {
		ArrayList<Tile> path = new ArrayList<Tile>();
		//if current.parent = -1 then we are at the start cell
		while(current.parent != -1) {
			//System.out.println(current.parent);
			path.add(current);
			current = gridFunctions.grid[current.parent];
		}
		
		
		return path;
	}
	

}

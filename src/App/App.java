package App;

import Heuristic.gridFunctions;

public class App {
	
	/**
	 * filename is the file name given to the command line
	 * randState is the search
	 * mode if mode is true then generate a seed if its false then we take in a file
	 * weight is used for the weighted A*
	 * sType is the type of search
	 * heurs is the multiple heuristics
	 * weight2 used in many heuristics
	 * @param args
	 */

	public static void main(String[] args) {
		String filename = "none";
		int randState = 81;
		boolean mode = true;
		int heuristic = 0;
		double weight = 1.0;
		int sType = 0;
		int[] heurs = {1,2,3};
		double weight2 = 1.0;
		
		
		
		for(int i = 0; i < args.length; i++) {
			
			//if valid input is not given
			if(i==args.length-1) {
				break;
			}
			
			//cases
			switch(args[i]) {
			
			case "file":
				filename = args[i+1];
				mode = false;
				break;
				
			case "gen":
				if(!args[i+1].equals("none")) {
					randState = Integer.parseInt(args[i+1]);
				}
				break;
			
			case "heur":
				if(Integer.parseInt(args[i+1]) <=5 && Integer.parseInt(args[i+1]) >=0) {
					heuristic = Integer.parseInt(args[i+1]);
				}
				break;
				
			case "weight":
				if(Double.parseDouble(args[i+1]) >= 0) {
					weight = Double.parseDouble(args[i+1]);
				}
				break;
				
			case "type":
				String s = args[i+1];
				if(s.equals("seq")) {
					sType = 1;
					break;
				}
				else if(s.equals("int")) {
					sType = 2;
					break;
				}
				
			case "heurs":
				//To do
			
			case "weight2":
				if(Double.parseDouble(args[i+1]) >= 0) {
					weight2 = Double.parseDouble(args[i+1]);
					break;
				}
				
			
			}
			
		}
		
		//test to check for arguments going through properly
		System.out.println(filename + " " + randState + " " + mode + " " + heuristic+ " " + weight + " "
		+ sType + " " + heurs + " " + weight2);
		
		//run the program for search
		Heuristic.gridFunctions grid = new Heuristic.gridFunctions(120, 160, randState);
		if(!filename.equals("none")) {
			grid.traverseFile(filename);
			grid.printGrid();
			gridUI.path = Heuristic.aStar.aStarSearch(1, 5);
			gridUI.main(args);
		} else {
			grid.hardTraverse(0);
			grid.blockCells();
			grid.startGoalCells();
			grid.highwayCells();
			grid.printGrid();
			gridUI.path = Heuristic.aStar.aStarSearch(0.25, 2);
			int h[] = new int[3];
			h[1] = 3;
			h[0] = 1;
			h[2] = 2;
			//gridUI.path = Heuristic.aStar.seqASS(1.0, 1.0, h);
			gridUI.main(args);
		}
		
		
	}

}

package App;

import Heuristic.gridFunctions;

public class App {
	
	private static String fileName;
	private static int defaultSeed = 9;
	private static boolean noFileGiven = true;
	private static int heuristicValue;
	private static double SeqAStarWeight;
	private static int[] multipleHeuristicValues;
	private static int searchType;
	private static double AStarWeight;
	private static Heuristic.gridFunctions grid;

	public static void main(String[] args) {	
		
		
		//TODO fix up receiving inputs
		for(int i = 0; i < args.length; i++) {
			
			//if valid input is not given
			if(i==args.length-1) {
				break;
			}
			
			//cases
			switch(args[i]) {
			
			case "File":
				fileName = args[i+1];
				noFileGiven = false;
				break;
				
			case "Generation":
				if(!args[i+1].equals("none")) {
					defaultSeed = Integer.parseInt(args[i+1]);
				}
				break;
			
			case "HeuristicValue":
				if(Integer.parseInt(args[i+1]) <=5 && Integer.parseInt(args[i+1]) >=0) {
					heuristicValue = Integer.parseInt(args[i+1]);
				}
				break;
				
			case "AStarWeight":
				if(Double.parseDouble(args[i+1]) >= 0) {
					AStarWeight = Double.parseDouble(args[i+1]);
				}
				break;
				
			case "typeOfSearch":
				String s = args[i+1];
				if(s.equals("Seq")) {
					searchType = 1;
					break;
				}
				else if(s.equals("Int")) {
					searchType = 2;
					break;
				}
				
			case "multipleHeuristics":
				//To do
			
			case "SeqAStarWeight":
				if(Double.parseDouble(args[i+1]) >= 0) {
					SeqAStarWeight = Double.parseDouble(args[i+1]);
					break;
				}
			}
		}
		
		grid = new Heuristic.gridFunctions(120, 160, defaultSeed);
		if(noFileGiven) {
			randomlyGenerateGrid();
			visualizeGridUI();
		} else {
			makeGridFromFile();
			visualizeGridUI();
		}
	}
	
	public static void makeGridFromFile() {
		grid.traverseFile(fileName);
	}
	
	public static void randomlyGenerateGrid() {
		grid.hardTraverse(0);
		grid.blockCells();
		grid.startGoalCells();
		grid.highwayCells();
	}
	
	public static void visualizeGridUI() {
		grid.printGrid();
		gridUI.path = Heuristic.aStar.aStarSearch(0.25, 2);
		gridUI.main(null);
	}

}

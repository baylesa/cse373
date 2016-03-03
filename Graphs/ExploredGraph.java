import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

/**
 * 
 */

/**
 * @author Andrew Bayles Student ID: 1328414
 * Extra Credit Options Implemented, if any:  (mention them here.)
 * 
 * Solution to Assignment 6 in CSE 373, Winter 2016
 * University of Washington.
 * 
 * Starter code v1.1b. By Steve Tanimoto, with modifications
 * by Kuikui Liu and S.J. Liu.
 *
 * The main differences between this version (1.1a) and version 1.1 are:
 *  1. The use of ArrayList rather than array for the pegs variable in class Vertex.
 *  2. Type information provided for both Function prototypes in class Operator.
 *
 * This code requires Java version 8 or higher.
 *
 */

// Here is the main application class:
public class ExploredGraph {
	Set<Vertex> Ve; // collection of explored vertices
	Set<Edge> Ee;   // collection of explored edges
	Vertex startV; 
	ArrayList<Operator> ops; //collection of possible operators
	
	//Constructor
	public ExploredGraph() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
		ops = new ArrayList<Operator>();
		for(int i = 0; i < 3;i++){
			for(int j = 0; j< 3; j++){
				if(i != j){
					Operator op = new Operator(i,j);
					ops.add(op); 
				}
			}
		}
	}
	
	//This takes no parameters and it clears the memory for a new search
	public void initialize() {
		Ve.clear();
		Ee.clear();
	}
	
	//Takes no parameters and returns the number of vertices that have been explored
	public int nvertices() {
		return Ve.size();
	}
	
	//Takes no parameters and returns the number of edges that have been explored
	public int nedges() {
		return Ee.size();
	}    
	
	//Takes in two vertices as parameters, the first is the starting point and the second
	// is the ending point. This method performs a depth first search of the graph.
	public void dfs(Vertex vi, Vertex vj) {
		initialize();
		startV = vi;
		Ve.add(vi);
		Stack<Vertex> aux = new Stack<Vertex>();//Stack for DFS storage
		aux.add(vi);
		while(!aux.isEmpty()){
			Vertex current = aux.pop();//get new node to check possible states
			for(int i = 0; i < ops.size();i++){
				Operator op = ops.get(i);				
				if(op.getPrecondition(current)){//checks if legal move
					Vertex temp = op.getTransition(current);
					if(!Ve.contains(temp)){//stops backtracking
						Ee.add(new Edge(current, temp)); 
						Ve.add(temp);
						aux.push(temp);
						if(temp.equals(vj)){aux.clear(); break;}
					}
				}
			}
		}
	}
	
	//Takes in two vertices as parameters, the first is the starting point and the second
	// is the ending point. This method performs a breadth first search of the graph.
	public void bfs(Vertex vi, Vertex vj) {
		initialize();
		startV = vi;
		Ve.add(vi);
		Queue<Vertex> aux = new LinkedList<Vertex>();//Queue for BFS storage
		aux.add(vi);
		while(!aux.isEmpty()){
			Vertex current = aux.remove();//get new node to check possible states
			for(int i = 0; i < ops.size();i++){
				Operator op = ops.get(i);				
				if(op.getPrecondition(current)){//checks if legal move
					Vertex temp = op.getTransition(current);
					if(!Ve.contains(temp)){//stops backtracking
						Ee.add(new Edge(current, temp)); 
						Ve.add(temp);
						aux.add(temp);
						if(temp.equals(vj)){aux.clear(); break;}
					}
				}
			}
		}
		
	} 
	
	//Must be called after a call on dfs or bfs
	//Takes in a Vertex that represents the final point and returns an arraylist
	//of vertices that show the path the search took to reach the goal. 
	public ArrayList<Vertex> retrievePath(Vertex vi) {
		if (!Ve.contains(vi)) {return null;}
		Edge viEdge = null;
		ArrayList<Edge> rEdge = new ArrayList<Edge>();
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		
		for (Edge edge : Ee) {
			rEdge.add(edge);
			if (edge.getEndpoint2().equals(vi)) {
				viEdge = edge;
			}
		}
		Collections.reverse(rEdge); // switches the order so the last edge is first
		Vertex leftVertex = viEdge.getEndpoint1();
		Vertex rightVertex = viEdge.getEndpoint2();
		Vertex lastLeftVertex = leftVertex;

		path.add(rightVertex);
		for (Edge edge: rEdge) {
			if (edge.getEndpoint2().equals(lastLeftVertex)) {
				path.add(edge.getEndpoint2());
				lastLeftVertex = edge.getEndpoint1();
			}
		}
		path.add(startV);
		Collections.reverse(path);
		return path;
	}
	
	//Takes in two vertices, the start and end point
	//Returns an array list of the most efficient path to the end vertex
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
		this.bfs(vi, vj);
		return this.retrievePath(vj);
	} // Implement this.
	
	//Takes no parameters and returns the set of vertices
	public Set<Vertex> getVertices() {return Ve;} 
	
	//Takes no Parameters and returns the set of edges
	public Set<Edge> getEdges() {return Ee;} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExploredGraph eg = new ExploredGraph();
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		Vertex v1 = eg.new Vertex("[[],[],[4,3,2,1]]");
		//eg.bfs(v0,v1);
		//eg.dfs(v0, v1);
		System.out.println(eg.shortestPath(v0,v1));
		System.out.println("Tests Finished");
		// Add your own tests here.
		// The autograder code will be used to test your basic functionality later.

	}
	
	class Vertex {
		ArrayList<Stack<Integer>> pegs; // Each vertex will hold a Towers-of-Hanoi state.
		// There will be 3 pegs in the standard version, but more if you do extra credit option A5E1.
		
		// Constructor that takes a string such as "[[4,3,2,1],[],[]]":
		public Vertex(String vString) {
			String[] parts = vString.split("\\],\\[");
			pegs = new ArrayList<Stack<Integer>>(3);
			for (int i=0; i<3;i++) {
				pegs.add(new Stack<Integer>());
				try {
					parts[i]=parts[i].replaceAll("\\[","");
					parts[i]=parts[i].replaceAll("\\]","");
					List<String> al = new ArrayList<String>(Arrays.asList(parts[i].split(",")));
					System.out.println("ArrayList al is: "+al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						String item = it.next();
                                                if (!item.equals("")) {
                                                        System.out.println("item is: "+item);
                                                        pegs.get(i).push(Integer.parseInt(item));
                                                }
					}
				}
				catch(NumberFormatException nfe) { nfe.printStackTrace(); }
			}		
		}
		//Takes no parameter and returns a String representing a vertex
		public String toString() {
			String ans = "[";
			for (int i=0; i<3; i++) {
			    ans += pegs.get(i).toString().replace(" ", "");
				if (i<2) { ans += ","; }
			}
			ans += "]";
			return ans;
		}
		
		//Takes an object as a parameter and Determines if two vertices are equal in "state"
		@Override
		public boolean equals(Object o) {
			if (this == o) {return true;}
			if (o == null || getClass() != o.getClass()) {return false;}
				Vertex vertex = (Vertex) o;
				return vertex.toString().equals(this.toString());
		}
				
		//Takes no parameters, hashes for the LinkedHashSet
		@Override
		public int hashCode() {
			return Objects.hash(pegs);
		}
	}
	
	//Represents an edge in a graph between two vertexes 
	class Edge {
		private Vertex v1;//Start point
		private Vertex v2;//End point
		
		//Constructor takes in two Vertex objects and to show the start and end of the edge
		public Edge(Vertex vi, Vertex vj) {
			v1 = vi;
			v2 = vj;
		}
		
		//Takes no parameters and returns the "start" of the edge
		public Vertex getEndpoint1(){
			return v1;
		}
		
		//Takes no parameters and returns the "end" of the edge
		public Vertex getEndpoint2(){
			return v2;
		}
		
		//Returns a string representing the edge 
		public String toString(){
			return "Edge from ["+ v1.toString() + "] to [" + v2.toString() + "]";
		}
		

	}
	
	//Represents operations that can be performed on vertices. 
	class Operator {
		private int i, j;//i is origin j is destination

		//Constructor takes in two integers 
		public Operator(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		//Takes a vertex as a parameter and returns true if the move can be legally made 
		//based on Towers of Hanoi principles, false otherwise
		public boolean getPrecondition(Vertex v){
			//makes sure the client can't move a non existent disk 
			if(v.pegs.get(i).size() == 0){return false;}
			//checks if the destination spot is empty if yes automatic move
			if(v.pegs.get(j).size() == 0){return true;}
			//checks if the disk on top of the other stack is larger than the one about to be moved
			else if(v.pegs.get(i).peek() < v.pegs.get(j).peek()){return true;}
			return false;
		}
		
		//Takes the same vertex as a parameter. This method makes a move of 
		public Vertex getTransition(Vertex v){
			v.pegs.get(j).push(v.pegs.get(i).pop());//make the move
			Vertex v1 = new Vertex(v.toString());// create new node representing move
			v.pegs.get(i).push(v.pegs.get(j).pop());//undo the move to restore to original state
			return v1;
		}
		
		//Takes no parameters and returns a string representing a particular operator
		public String toString() {
			// TODO: return a string good enough
			// to distinguish different operators
			return "This is operator: [" + i + ", " + j+"]";
		}
	}
}

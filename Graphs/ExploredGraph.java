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
	ArrayList<Operator> ops;
	
	public ExploredGraph() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
		ops = new ArrayList<Operator>();
		for(int i = 0; i < 3;i++){
			for(int j = 0; j< 3; j++){
				if(i != j){
					System.out.println(i + j);
					Operator op = new Operator(i,j);
					ops.add(op); 
				}
			}
		}
	}

	public void initialize() {
		Ve.clear();
		Ee.clear();
	}
	
	public int nvertices() {
		return Ve.size();
	}
	
	public int nedges() {
		return Ee.size();
	}    
	
	public void dfs(Vertex vi, Vertex vj) {
		initialize();
		startV = vi;
		Ve.add(vi);
		Stack<Vertex> aux = new Stack<Vertex>();
		aux.add(vi);
		System.out.println("Operators:" + ops.toString());
		while(!aux.isEmpty()){
			Vertex current = aux.pop();
			for(int i = 0; i < ops.size();i++){
				Operator op = ops.get(i);				
				if(op.getPrecondition(current)){
					Vertex temp = op.getTransition(current);
					if(!Ve.contains(temp)){
						System.out.println("inital state: " + current.toString());
						System.out.println("After transition state: " + temp.toString());
						System.out.println("NVerticies: " + nvertices());
						System.out.println("NEdges: " + nedges());
						
						Ee.add(new Edge(current, temp)); 
						Ve.add(temp);
						aux.push(temp);
						System.out.println("Aux is:" + aux.toString());
						if(temp.equals(vj)){aux.clear(); break;}
					}
				}
			}
		}
	} // Implement this.
	
	//Performs a Breadth First Search from the two parameters passed. The start and end
	// point for the search.
	public void bfs(Vertex vi, Vertex vj) {
		initialize();
		startV = vi;
		Ve.add(vi);
		Queue<Vertex> aux = new LinkedList<Vertex>();//set up storage
		aux.add(vi);
		System.out.println("Operators:" + ops.toString());
		while(!aux.isEmpty()){
			Vertex current = aux.remove();//get new node to check possible states
			for(int i = 0; i < ops.size();i++){
				Operator op = ops.get(i);				
				if(op.getPrecondition(current)){//checks if legal move
					Vertex temp = op.getTransition(current);
					if(!Ve.contains(temp)){//stops backtracking
						System.out.println("inital state: " + current.toString());
						System.out.println("After transition state: " + temp.toString());
						System.out.println("NVerticies: " + nvertices());
						System.out.println("NEdges: " + nedges());
						Ee.add(new Edge(current, temp)); 
						Ve.add(temp);
						aux.add(temp);
						System.out.println("Aux is:" + aux.toString());
						if(temp.equals(vj)){aux.clear(); break;}
					}
				}
			}
		}
		
	} 
	
	public ArrayList<Vertex> retrievePath(Vertex vi) {
		if (!Ve.contains(vi)) {
			return null;
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		Edge viEdge = null;
		ArrayList<Edge> rEdge = new ArrayList<Edge>();
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
		boolean atSolutionEdge = true;
		for (Edge edge: rEdge) {
			if (atSolutionEdge) {
				atSolutionEdge = false;
				continue;
			}
			if (edge.getEndpoint2().equals(lastLeftVertex)) {
				path.add(edge.getEndpoint2());
				lastLeftVertex = edge.getEndpoint1();
			}
		}
		path.add(startV);
		Collections.reverse(path);
		return path;
	}
	
	
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
		this.bfs(vi, vj);
		return this.retrievePath(vj);
	} // Implement this.
	
	public Set<Vertex> getVertices() {return Ve;} 
	public Set<Edge> getEdges() {return Ee;} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExploredGraph eg = new ExploredGraph();
		// Test the vertex constructor: 
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		Vertex v1 = eg.new Vertex("[[],[],[4,3,2,1]]");
//		System.out.println(v0);
//		System.out.println(v1);
		Edge e1 = eg.new Edge(v0, v1);
//		System.out.println(e1.toString());
//		System.out.println(e1.getEndPoint1());
//		System.out.println(e1.getEndPoint2());
//		Operator op1 = eg.new Operator(2,0);
//		boolean x = op1.getPrecondition(v1);
//		if(x){
//			op1.getTransition(v1);
//		}else{
//			System.out.println("Wrong Precondition");
//		}
		
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
					//System.out.println("ArrayList al is: "+al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						String item = it.next();
                                                if (!item.equals("")) {
                                                     //   System.out.println("item is: "+item);
                                                        pegs.get(i).push(Integer.parseInt(item));
                                                }
					}
				}
				catch(NumberFormatException nfe) { nfe.printStackTrace(); }
			}		
		}
		public String toString() {
			String ans = "[";
			for (int i=0; i<3; i++) {
			    ans += pegs.get(i).toString().replace(" ", "");
				if (i<2) { ans += ","; }
			}
			ans += "]";
			return ans;
		}
		
		// determines a boolean representing whether or not two states (vertices) are equal
				@Override
				public boolean equals(Object o) {
					if (this == o) return true;
					if (o == null || getClass() != o.getClass()) return false;
					Vertex vertex = (Vertex) o;
					return vertex.toString().equals(this.toString());
				}
				
				// hashes values for the LinkedHashSet
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
		
		public Vertex getEndpoint1(){
			return v1;
		}
		
		public Vertex getEndpoint2(){
			return v2;
		}
		
		//Returns a string representing the edge 
		public String toString(){
			return "Edge from ["+ v1.toString() + "] to [" + v2.toString() + "]";
		}
		

	}
	
	class Operator {
		private int i, j;//i is origin j is destination

		public Operator(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		
		public boolean getPrecondition(Vertex v){
			if(v.pegs.get(i).size() == 0){//makes sure the client can't move a non existent disk 
				System.out.println("Empty move");
				return false;
			}
			
			if(v.pegs.get(j).size() == 0){//checks if the destination spot is empty if yes automatic move
				return true; 
			}
			else if(v.pegs.get(i).peek() < v.pegs.get(j).peek()){//checks if the disk on top of the other stack is larger than the one about to be moved
				System.out.println("legal move"); 
				return true; 
			}
			return false;
		}
		
		public Vertex getTransition(Vertex v){
			
			v.pegs.get(j).push(v.pegs.get(i).pop());//make the move
			System.out.println("New State: "+v.toString());
			Vertex v1 = new Vertex(v.toString());// create new node representing move
			v.pegs.get(i).push(v.pegs.get(j).pop());//un do the move to restore to original state
			System.out.println("Old State: " + v.toString());
			return v1;
		}
		
		public String toString() {
			// TODO: return a string good enough
			// to distinguish different operators
			return "This is operator: [" + i + " to " + j+"]";
		}
	}

}

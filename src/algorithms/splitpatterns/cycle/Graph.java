package algorithms.splitpatterns.cycle;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private List<Vertex> graph = new ArrayList<Vertex>();
	private List<String> verticesName = new ArrayList<String>();

	public void setVertices(List<Vertex> vertices) {
		this.graph.addAll(vertices);
	}

	public void addVertex(Vertex newVertex) {
		this.graph.add(newVertex);
	}
	
	
//	public int getNeighbourEdgesWeight(Vertex pre,Vertex next) {
//		if(this.graph.contains(pre) && this.graph.contains(next) && this.graph.contains(next))
//	       
//           Edge edge = new Edge(pre, next);
//			if(pre.hasEdge(edge)!=null){
//				return edge.getWeight();
//			}else {
//				return 0;
//			}
////			pre.hasEdge(edge).addWeight();
//       	
//	}

	public List<Vertex> getVertices() {
		return this.graph;
	}
	
	public List<String> getVerticesName() {
		for(Vertex v: this.graph){
			verticesName.add(v.getName());
		}
		return verticesName;
	}
	
	public String getVerticesName(int i) {
		return this.getVerticesName().get(i);
		
	}
	
	public int getVerticesNumber() {
		return this.getVertices().size();
		
	}

	// returns the vertex whose description is the same as the one searched for.
	public Vertex findVertex(String name) {

		for (int i = 0; i < this.getVertices().size(); i++) {

			if (name.equalsIgnoreCase(this.getVertices().get(i).getName())) {

				return this.getVertices().get(i);

			}
		}

		return null;

	}
}

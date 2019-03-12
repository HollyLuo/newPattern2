package algorithms.splitpatterns.cycle;


//边
public class Edge {
	private int weight;
	private Vertex origin;
	private Vertex destination;

	public Edge(Vertex v1, Vertex v2) {
		this.weight = 0;
		this.origin = v1;
		this.destination = v2;

	}
	
//	public boolean exitEdge(Vertex v2) {
//		if(v2==this.destination){
//			return true;
//		}else{
//			return false;
//		}
//	}

	public void addWeight() {

		this.weight += 1;
	}

	public int getWeight() {

		return weight;
	}

	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

	public Vertex getDestination() {
		return destination;
	}

	public void setOrigin(Vertex origin) {
		this.origin = origin;
	}

	public Vertex getOrigin() {
		return origin;
	}
	
	public void printEdge() {
		System.out.println("(" + this.origin +"," + this.destination + ": " +this.weight +")");
	}
}

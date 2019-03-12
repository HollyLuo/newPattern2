package algorithms.splitpatterns.cycle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;

//Vertex: 顶点
public class Vertex{
        private String name;//顶点标识
        private int inNumber = 0;
        private int outNumber = 0;
//        private Vertex next;
        
//        private int distance;//顶点到源点的最短距离
//        private boolean doVisit = false;//是否已访问
        
        private List<Edge> adjEdges = new ArrayList<Edge>();//顶点的所有邻接边
        private List<Vertex> neighbourList = new ArrayList<Vertex>();//邻居顶点
        private List<String> neighbourListName = new ArrayList<>();
        
        public void setEdge(Edge edge) {
        	this.adjEdges.add(edge);
			
		}
        
        public void setName(String name){
                
                this.name = name;
        }
        
        public String getName(){
                
                return name;
                
        }
        public void addInNumber(){           
            this.inNumber += 1 ;
        }
    
         public int getInNumber(){ 
            return inNumber;         
         }
         public void addOutNumber(){           
             this.outNumber += 1 ;
         }
     
          public int getOutNumber(){ 
             return outNumber;         
          }
          
//          public boolean hasNext(Vertex next){           
//              if(this.next == next){
//            	  return true;
//              }else {
//				return false;
//			}
//              
//          } 
           
        
//        public void doVisit (){
//                this.doVisit = true;
//        }
//        
//        public boolean verifyVisit(){                
//                return doVisit;
//        }
//        
//        public void setDistance(int distance){
//                
//                this.distance = distance;
//        }
        
//        public int getDistance(){
//                
//                return this.distance;
//        }
//        

        public void setNeighbourList(List<Vertex> neighbours) {                
                this.neighbourList.addAll(neighbours);                 
        }
        
        public void setNeighbour(Vertex neighbour) {
        	 this.neighbourList.add(neighbour);
		}
        
        public void setNeighbourName(String name) {
       	 this.neighbourListName.add(name);
		}
        
        public List<Vertex> getNeighbours(){
 
                return this.neighbourList;
        }
        
        public List<String> getNeighborsName() {
        	return this.neighbourListName;
			
		}
        
        public boolean hasNeighbour(Vertex neighbour) {
        	for(int i=0;i<this.neighbourList.size();i++){
        		if(this.neighbourList.get(i) == neighbour){
        			return true;
        		}
        	}
        	return false;
		}
     
        
        public boolean hasNeighbour(String neighbour) {
        	for(int i=0;i<this.neighbourList.size();i++){
        		if(this.neighbourList.get(i).name == neighbour){
        			return true;
        		}
        	}
        	return false;
		}
        
        public Edge getEdge(Edge edge) {
        	Edge e = null;
        	for(int i=0;i<this.adjEdges.size();i++){
        		if(this.adjEdges.get(i).getOrigin() == edge.getOrigin() && this.adjEdges.get(i).getDestination() == edge.getDestination()){
        			e = this.adjEdges.get(i);
        			return e;
        		}
        	}
        	return e;
		}
        
        
        
        public void setAdjEdges(List <Edge> adjEdges){
                this.adjEdges.addAll(adjEdges);
                
        }
        
        public List<Edge> getAdjEdges() {
                
                return adjEdges;
        }

//        public int compareTo(Vertex vertice) {
//          if(this.getDistance() < vertice.getDistance()) return -1;
//          else if(this.getDistance() == vertice.getDistance()) return 0;
//          
//          return 1;             
//        }
        
        @Override
        public boolean equals(Object obj) {
                if(obj instanceof Vertex){
                	Vertex vRef = (Vertex) obj;
                    if(this.getName().equals(vRef.getName())) return true;
                }
                return false;
        }
        
        @Override
        public String toString() {
                String s = " ";
                s+= this.getName();
                return s;
        }
        
        public void printVertex() {
        	System.out.println("Vertex: "+this.getName() + ": ");
        	System.out.println("inNumber: "+this.getInNumber()+", outNumber:" + this.getOutNumber());
        	System.out.println("neighbors:"+this.getNeighborsName());
        	for(int i=0;i<this.adjEdges.size();i++){
        		this.adjEdges.get(i).printEdge();
        	}
        	System.out.println("");
			
		}

		

		

	
		 
}
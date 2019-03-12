package algorithms.splitpatterns.cycle;

import java.util.ArrayList;


public class Cycle{
     private int serial_number;
	 private ArrayList<String> trace;//从出发节点到当前节点的轨迹
	 private int weight; 

	 
	 public Cycle() {
//	     this.serial_number = 0;
	     this.trace = new ArrayList<>();//路径
	     this.weight=0;
	}
	
	 public ArrayList<String> getTrace() {
	    return this.trace;	
	 }
	 public int getWeight() {
	    return this.weight;	
	 }
	 public void setTrace(ArrayList<String> trace) {
		this.trace = trace;	
	 }
	 public void setWeight(int weight) {
		  this.weight = weight;	
	 }

	public int getSerialNumber() {
		return serial_number;
	}

	public void setSerialNumber(int serial_number) {
		this.serial_number = serial_number;
	}

}

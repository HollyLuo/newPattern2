package algorithms.splitpatterns.cycle;

import java.util.ArrayList;


public class InternalCycle {
	 private int cycle_number;
	 private ArrayList<String> trace;//从出发节点到当前节点的轨迹
	 private int  loop_info;
	 private int cycle_start;
	 
	
	 public ArrayList<String> getTrace() {
	    return this.trace;	
	 }
	 
	 public void setTrace(ArrayList<String> trace) {
			this.trace = trace;	
		 }
	 
	 public int getCountInfo() {
	    return this.loop_info;	
	 }
	
	 public void setWeight(int loop_info) {
		  this.loop_info = loop_info;	
	 }

	public int getSerialNumber() {
		return cycle_number;
	}

	public void setSerialNumber(int cycle_number) {
		this.cycle_number = cycle_number;
	}

	public int getCycleStart() {
		return cycle_start;
	}

	public void setCycleStart(int cycle_start) {
		
		this.cycle_start = cycle_start;
	}

	public int getCycleEnd() {
		return this.cycle_start + trace.size()*loop_info;
	}
	public void printInternalCycle() {
		System.out.println("-----InternalCycle: " + trace);
		System.out.println("-----InternalCycle times: "+ loop_info);
		System.out.println("-----InternalCycle start: "+ cycle_start);
	}
}

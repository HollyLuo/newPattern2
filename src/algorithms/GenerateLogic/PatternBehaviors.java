package algorithms.GenerateLogic;

import java.util.ArrayList;
import java.util.List;

public class PatternBehaviors {
	private String patternId;
	private List<String> patternBehaviors = new ArrayList<>();
	private List<List<String>> uniqueIdsList = new ArrayList<>();
	
	public List<String> getPatternBehaviors() {
		return patternBehaviors;
	}
	public void setPatternBehaviors(List<String> patternBehaviors) {
		this.patternBehaviors = patternBehaviors;
	}
	public List<List<String>> getUniqueIdsList() {
		return uniqueIdsList;
	}
	public void setUniqueIdsList(List<List<String>> uniqueIdsList) {
		this.uniqueIdsList = uniqueIdsList;
	}
	
	public void printPatternBehaviors() {
		System.out.println("patternId: " + patternId + ", patternBehaviors: " + patternBehaviors + ", " + "uniqueIdsList" + uniqueIdsList.toString());
		
	}
	public String getPatternId() {
		return patternId;
	}
	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

}

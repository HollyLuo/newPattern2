package algorithms.splitpatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.json.simple.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import algorithms.splitpatterns.cycle.Cycle;
import algorithms.splitpatterns.cycle.Edge;
import algorithms.splitpatterns.cycle.Graph;  
import algorithms.splitpatterns.cycle.Vertex;

public class SplitBehaviors {
	public  static void runAlgorithm(Map<String, String> behaviorsMap) throws Exception  {
		List<String> behaviorsList = new ArrayList<>();
		String iString = "";

		for (Map.Entry<String, String> entry : behaviorsMap.entrySet()) {
			behaviorsList.add(entry.getValue());
			iString += entry.getValue();
			iString += ",";
			
		}	
		String inputString = iString.substring(0, iString.length()-1);
				
		for(Iterator<String> it=behaviorsList.iterator();it.hasNext();){   
			 System.out.println(it.next()); 
		}
		System.out.println(inputString);
		
        System.out.println("--------------scan the behavior chain-----------------");
       
		Vertex currentVertex;
		Vertex nextVertex;
		Vertex vertex;
		Edge edge;
		ArrayList<String> nameList = new ArrayList<>();	
		Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();	
		Graph graph = new Graph();
		
		for(Iterator<String> behaviorId=behaviorsList.iterator();behaviorId.hasNext();){    
//			 System.out.println(behaviorId.next());
			String behavior = behaviorId.next();
			vertex = (Vertex) vertexMap.get(behavior);
			if (vertex == null){
				vertex = new Vertex();	
				vertex.setName(behavior);
				vertexMap.put(behavior, vertex);
				nameList.add(behavior);
			}
		}
		
		for(int i=0;i<behaviorsList.size()-1;i++){
			currentVertex = (Vertex) vertexMap.get(behaviorsList.get(i));
			if(i!=0){
				currentVertex.addInNumber();
			}
			if(i!=behaviorsList.size()){
				currentVertex.addOutNumber();
			}
			nextVertex = (Vertex) vertexMap.get(behaviorsList.get(i+1));		
			edge = new Edge(currentVertex, nextVertex);			
			if(currentVertex.getEdge(edge)!=null){
				currentVertex.getEdge(edge).addWeight();
			}else {
				edge.addWeight();
				currentVertex.setEdge(edge);
				currentVertex.setNeighbour(nextVertex);
				currentVertex.setNeighbourName(behaviorsList.get(i+1));
			}
		}
		
		for(int i=0;i<vertexMap.size();i++){		
			vertex = (Vertex) vertexMap.get(nameList.get(i));
			vertex.printVertex();
			graph.addVertex(vertex);
		}
		
		System.out.println("--------------Cycle Detection-----------------");
		ArrayList<Cycle> cycleList = new ArrayList<>();
		CycleDetection cycleDetection = new CycleDetection(inputString,graph);
		cycleDetection.hasCycle();
		cycleList = cycleDetection.getCycleList();
		String start  = cycleDetection.getStart();
		System.out.println();
		
		System.out.println("--------------Behavior split-----------------");
//		List<List<String>> pattList = new ArrayList<List<String>>();
		List<behaviorsChain> behaviorsChainsList = new ArrayList<behaviorsChain>();
		behaviorsChainsList =  SplitInputStringByStartVertex(behaviorsMap,start);
	    int size = behaviorsChainsList.size();
	    System.out.println();	    
	      
	    System.out.println("--------------All Pattern -----------------");  
	    List<Pattern> patternList =  CountDuplicatedList(behaviorsChainsList);
   
	    for(Pattern pattern : patternList){	    	
	    	pattern.printPattern(); 	
	    	pattern.foundInternalCycle(cycleList);
	    	pattern.removeInternalCycle();
	    	if(pattern.hasInternalCycle){		
	    		System.out.println("--new_trace: "+ pattern.getNewBehaviorChain());
	    	}
	    	JSONObject jsonObject = pattern.toJsonObject();
	    	System.out.println(jsonObject.toJSONString());
	    	saveJsonObjectToMongoDb(jsonObject,"localhost","IRA_test","behavior_chains_test");
	    }
	    
	    System.out.println();		  
	    System.out.println("-----------------afterCountPatternList-------------");
	    List<Pattern> afterCountPatternList =  CountPattern(patternList);	    	    
	    generatePatternJson(afterCountPatternList,patternList);
        System.out.println("");
	    System.out.println("");
	    
	    
	}

	private static void generatePatternJson(List<Pattern> afterCountPatternList, List<Pattern> patternList) {
		
		int mergePatternId = 0;
	    for(Pattern mergePattern : afterCountPatternList){
	    	List<Integer> allPatternIds = new ArrayList<Integer>();
	    	mergePatternId += 1;
	    	mergePattern.printPattern(); 
	    	System.out.println("---------including---------");
	    	
	    	for(Pattern oriPattern : patternList){
	    		if(oriPattern.getNewBehaviorChain().equals(mergePattern.getBehaviorChain())){	    			
	    			oriPattern.printPattern();
	    			allPatternIds.add(oriPattern.getPatternName());
    			}
	    	}
	    	JSONObject mergePatternJson = new JSONObject();
	    	mergePatternJson.put("patternId", mergePatternId);
	    	mergePatternJson.put("patternBehaviors", mergePattern.getBehaviorChain());
	    	mergePatternJson.put("weights", mergePattern.getWeight());
	    	mergePatternJson.put("chainBranches", allPatternIds);
	    	mergePatternJson.put("uniqueIdsList", mergePattern.getUniqueIdsList());
	    	saveJsonObjectToMongoDb(mergePatternJson,"localhost","IRA_test","patterns_test");
	    	System.out.println("");
	    	System.out.println("");
	    	
	    }
	}

	private static List<behaviorsChain> SplitInputStringByStartVertex(Map<String, String> behaviorsMap, String start) {
//		  List<List<String>> behaviorChainList = new ArrayList<List<String>>();
//		  List<List<String>> uniqueIdChainList = new ArrayList<List<String>>();
		  List<behaviorsChain> behaviorsChainsList = new ArrayList<behaviorsChain>();
		  List<String> behaviorsList = new ArrayList<>();
		  List<String> keyChainList = new ArrayList<>();
		  for (Map.Entry<String, String> entry : behaviorsMap.entrySet()) {
			  behaviorsList.add(entry.getValue());
			  keyChainList.add(entry.getKey());
		  }

		  ArrayList<Integer> findStart = new ArrayList<>();

		  for(int i=0;i<behaviorsList.size();i++){
			  if(behaviorsList.get(i).equals(start)){			
				  findStart.add(i);
//				  System.out.println(i);
			  }
		  }	  

		  for(int i=0;i<findStart.size()-1;i++){
			  behaviorsChain behaviorsChain = new behaviorsChain();
			  List<String> behaviorIds = new ArrayList<>();
			  List<String> uniqueIds = new ArrayList<>();
			  if(i==0 && (findStart.get(i)!=0)){
				  behaviorIds = behaviorsList.subList(0, findStart.get(i));
				  uniqueIds = keyChainList.subList(0, findStart.get(i));
//				  behaviorChainList.add(behaviorIds);			  
//				  uniqueIdChainList.add(uniqueIds);
				  behaviorsChain.setBehaviorsChain(behaviorIds);
				  behaviorsChain.setUniqueIdsChain(uniqueIds);
				  behaviorsChainsList.add(behaviorsChain);
				  System.out.println("behaviorIds: " + behaviorIds);
				  System.out.println("uniqueIds: " + uniqueIds);
			  }
			  behaviorIds = behaviorsList.subList(findStart.get(i), findStart.get(i+1));
			  uniqueIds = keyChainList.subList(findStart.get(i), findStart.get(i+1));
//			  behaviorChainList.add(behaviorIds);
//			  uniqueIdChainList.add(uniqueIds);
			  behaviorsChain.setBehaviorsChain(behaviorIds);
			  behaviorsChain.setUniqueIdsChain(uniqueIds);
			  behaviorsChainsList.add(behaviorsChain);
			  System.out.println("behaviorIds: " + behaviorIds);
			  System.out.println("uniqueIds: " + uniqueIds);
		  }
		  if(findStart.get(findStart.size()-1) < behaviorsList.size()){
			  behaviorsChain behaviorsChain = new behaviorsChain();
			  List<String> behaviorIds = new ArrayList<>();
			  List<String> uniqueIds = new ArrayList<>();
			  behaviorIds = behaviorsList.subList(findStart.get(findStart.size()-1),behaviorsList.size());
			  uniqueIds = keyChainList.subList(findStart.get(findStart.size()-1),behaviorsList.size());
//			  behaviorChainList.add(behaviorIds);
//			  uniqueIdChainList.add(uniqueIds);
			  behaviorsChain.setBehaviorsChain(behaviorIds);
			  behaviorsChain.setUniqueIdsChain(uniqueIds);
			  behaviorsChainsList.add(behaviorsChain);
			  System.out.println("behaviorIds: " + behaviorIds);
			  System.out.println("uniqueIds: " + uniqueIds);
		  }
		  return behaviorsChainsList;
	}
	
	private static List<Pattern> CountDuplicatedList(List<behaviorsChain> behaviorsChainsList) {
		Map<List<String>, Pattern> map = new HashMap<List<String>, Pattern>();
		List<Pattern> patternList = new ArrayList<>();	
		Pattern pattern;
		int pattern_name=1;
		for (behaviorsChain chain : behaviorsChainsList) {
			pattern = map.get(chain.getBehaviorsChain());
//			System.out.println(chain.getUniqueIdsChain());
			if(pattern==null){				
				pattern = new Pattern();
				pattern.setPatternName(pattern_name);
				pattern.setBehaviorChain(chain.getBehaviorsChain());
				pattern.getUniqueIdsList().add(chain.getUniqueIdsChain());
				pattern.setWeight(1);
				pattern_name+=1;
				map.put(chain.getBehaviorsChain(), pattern);
				patternList.add(pattern);				
			}else {
				pattern.setWeight(pattern.getWeight()+1);
				pattern.getUniqueIdsList().add(chain.getUniqueIdsChain());
			}
		}
		return patternList;
	}
	
	private static void saveJsonObjectToMongoDb(JSONObject jsonObject,String clientName,String databaseName,String collectionName) {
		try{		 
			 MongoClient mongoClient =  new MongoClient(clientName, 27017);
			 MongoDatabase mongoDatabase =  mongoClient.getDatabase(databaseName);
//			 System.out.println("Connect to database successfully");
			 
			 MongoCollection<Document> collection =  mongoDatabase.getCollection(collectionName);
			 System.out.println(jsonObject.toJSONString());
			 Document document = Document.parse(jsonObject.toJSONString());
			 
			 collection.insertOne(document);
			 mongoClient.close();

		 }catch(MongoException e){
			e.printStackTrace();
		} 
		
	}
	
	private static List<Pattern> CountPattern(List<Pattern> patternList) {
		List<Pattern> afterCountPatternList = new ArrayList<>();	
		Map<List<String>, Pattern> map = new HashMap<List<String>, Pattern>();
		Pattern pattern;
		int pattern_name = 0;
		for(Pattern item :patternList){
			pattern = map.get(item.getNewBehaviorChain());
			
//			System.out.println("item.getNewTrace():"+item.getNewTrace());
			if(pattern == null){
				pattern = new Pattern();
				pattern.setPatternName(pattern_name);
				pattern.setBehaviorChain(item.getNewBehaviorChain());
				pattern.setWeight(item.getWeight());
				pattern.getUniqueIdsList().addAll(item.getUniqueIdsList());
				pattern_name+=1;
				map.put(item.getNewBehaviorChain(), pattern);
				afterCountPatternList.add(pattern);	
			}else {
				pattern.setWeight(pattern.getWeight()+item.getWeight());
				pattern.getUniqueIdsList().addAll(item.getUniqueIdsList());
			}
		}
		return afterCountPatternList;
	}

}

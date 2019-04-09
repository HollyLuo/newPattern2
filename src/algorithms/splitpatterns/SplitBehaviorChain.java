package algorithms.splitpatterns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


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

public class SplitBehaviorChain {
	public static String readFile(String filename) throws IOException {
		String message = null;
        
        FileReader reader = new FileReader(filename);
        BufferedReader br = new BufferedReader(reader);
        message = br.readLine();
        br.close();
        reader.close();
        return message;
	}
	
	@SuppressWarnings("unchecked")
//	public  static void runAlgorithm(Map<String, String> behaviorsMap, float support) throws Exception  {
//		for (String uniqueId : behaviorsMap.keySet()) {
//			String behaviorId = behaviorsMap.get(uniqueId);//得到每个key多对用value的值
//			System.out.println(uniqueId + "     " + behaviorId);
//		}
//		
//		for (Map.Entry<String, String> entry : behaviorsMap.entrySet()) {
//			entry.getValue();
//			}
//		String inputFile="behaviors3";
//		String ids = readFile(inputFile);
//		String behaviorList[] = ids.split(",");
//		ArrayList<String> behaviorList2= new ArrayList<>();
//
//		for(int i = 0; i < behaviorList.length; i++){
//			behaviorList2.add(behaviorList[i]);
//		}
//		String inputString = ids.toString();
		
	public  static void runAlgorithm(String inputFile, float support) throws Exception  {
		String ids = readFile(inputFile);
		String behaviorList[] = ids.split(",");
		ArrayList<String> behaviorList2= new ArrayList<>();

		for(int i = 0; i < behaviorList.length; i++){
			behaviorList2.add(behaviorList[i]);
		}
		String inputString = ids.toString();
			
//			System.out.println("--------------scan the behavior chain-----------------");
		
		System.out.println("--------------scan the behavior chain-----------------");
		
		Vertex current;
		Vertex next;
		Vertex v;
		Edge edge;
		ArrayList<String> nameList = new ArrayList<>();
		
		Map<String, Vertex> map = new HashMap<String, Vertex>();
		
		Graph graph = new Graph();
			
		for(int i=0;i<behaviorList.length;i++){
			v = (Vertex) map.get(behaviorList[i]);
			if (v == null){
				v = new Vertex();	
				v.setName(behaviorList[i]);
				map.put(behaviorList[i], v);
				nameList.add(behaviorList[i]);
				
			}
		}
		
		for(int i=0;i<behaviorList.length-1;i++){
			current = (Vertex) map.get(behaviorList[i]);
			if(i!=0){
				current.addInNumber();
			}
			if(i!=behaviorList.length){
				current.addOutNumber();
			}
			next = (Vertex) map.get(behaviorList[i+1]);
		
			edge = new Edge(current, next);
			
			if(current.getEdge(edge)!=null){
				current.getEdge(edge).addWeight();
			}else {
				edge.addWeight();
				current.setEdge(edge);
				current.setNeighbour(next);
				current.setNeighbourName(behaviorList[i+1]);
			}
		}
		
		for(int i=0;i<map.size();i++){		
			v = (Vertex) map.get(nameList.get(i));
//			v.printVertex();
			graph.addVertex(v);
		}
		
		System.out.println("--------------Cycle Detection-----------------");
		ArrayList<Cycle> cycleList = new ArrayList<>();
		CycleDetection cycleDetection = new CycleDetection(inputString,graph);
		cycleDetection.hasCycle();
		cycleList = cycleDetection.getCycleList();
		String start  = cycleDetection.getStart();
		System.out.println();
		System.out.println("--------------Behavior split-----------------");
		List<List<String>> pattList = new ArrayList<List<String>>();
	    pattList =  SplitInputStringByStartVertex(inputString,start);
	    int size = pattList.size();
	    System.out.println();
	    System.out.println("--------------All Pattern -----------------");
//	    Map<List<String>, Integer> map2 = new HashMap<List<String>, Integer>();
	    
	    List<Pattern> patternList =  CountDuplicatedList(pattList);
//	    List<Pattern> patternList2 = new ArrayList<>();
//	    patternList2.addAll(patternList);
	    
	    for(Pattern pattern : patternList){

	    	
	    	pattern.printPattern(); 	
	    	pattern.foundInternalCycle(cycleList);
	    	pattern.removeInternalCycle();
	    	if(pattern.hasInternalCycle){		
	    		System.out.println("--new_trace: "+ pattern.getBehaviorChain());
	    	}
//	    	JSONObject jsonObject = pattern.toJsonObject();
//	    	System.out.println(jsonObject.toJSONString());
//	    	saveJsonObjectToMongoDb(jsonObject,"IRA_test","behavior_chain_test");
	    	
//	    		List<String> newTrace = pattern.getNewTrace();
//	    		Pattern pattern2 = new Pattern();
//	    		
//	    		pattern2.setTrace(newTrace);
//	    		patternList2.add(pattern2);
//	    		System.out.println("---remove Internal Cycle----");
////	    		pattern2.printPattern();
//	    	}else {
//	    		patternList2.add(pattern);
//			}
//	    	System.out.println("------------");
	    }
	    
	    List<Pattern> afterCountPatternList =  CountPattern(patternList);
	    System.out.println();
	    System.out.println("-----------------Merge Internal Cycle-------------");
	    
	    int union_pattern_id = 0;
	    
	    for(Pattern new_pattern : afterCountPatternList){
	    	List<Integer> all_pttern_ids = new ArrayList<Integer>();
	    	union_pattern_id += 1;
	    	new_pattern.printPattern(); 
	    	System.out.println("---------including---------");
	    	
	    	for(Pattern ori_pattern : patternList){
	    		if(ori_pattern.getNewBehaviorChain().equals(new_pattern.getBehaviorChain())){	    			
	    			ori_pattern.printPattern();
	    			all_pttern_ids.add(ori_pattern.getPatternName());
    			}
	    	}
//	    	JSONObject union_pattern = new JSONObject();
//	    	union_pattern.put("union_pattern_id", union_pattern_id);
//	    	union_pattern.put("union_behaviors", new_pattern.getBehaviorChain());
//	    	union_pattern.put("union_weights", new_pattern.getWeight());
//	    	union_pattern.put("branches", all_pttern_ids);
//	    	saveJsonObjectToMongoDb(union_pattern,"IRA_test","patterns_test");
	    	
	    	
	    	System.out.println("");
	    	System.out.println("");
	    }
	    

	   
	   
//	    patternList.get(4).printPattern();
//	    patternList.get(4).foundInternalCycle(cycleList);
	    
//	    System.out.println("\nMap排序-以key排序");

//	    List<Map.Entry<List<String>, Integer>> list = new ArrayList<Map.Entry<List<String>, Integer>>(map2.entrySet());
//        Collections.sort(list,new Comparator<Map.Entry<List<String>, Integer>>() {
//			@Override
//			public int compare(Entry<List<String>, Integer> o1, Entry<List<String>, Integer> o2) {
//				return o2.getValue().compareTo(o1.getValue());
//			}
//        });
//        
//        printList(list);
//        System.out.println();
        
        System.out.println("--------------Frequecy Pattern -----------------");
//        Map<List<String>, Integer> map3 = new HashMap<List<String>, Integer>();
        for(Pattern new_pattern : afterCountPatternList){
        	if(new_pattern.isFrequencyPattern(size, support)){
        		new_pattern.printPattern(); 
        	}
        }
        
        System.out.println();
        System.out.println("--------------Similarity consider: merge?--------------");
        
        //pattern是否需要合并？ 12345  123745
//        计算两者的相似度，然后计算是否为包含关系。
        List<Pattern> frequencyPattern = new ArrayList();
        for(Pattern new_pattern : afterCountPatternList){
        	if(new_pattern.isFrequencyPattern(size, support)){
        		frequencyPattern.add(new_pattern);
        	}
        }
        for(int i=0;i<frequencyPattern.size();i++){
        	List<String> list1=new ArrayList<>();
        	list1.addAll(frequencyPattern.get(i).getBehaviorChain());
        	System.out.println(list1);
        	for(int j=i+1;j<frequencyPattern.size();j++){
        		List<String> list2=new ArrayList<>();
        		list2.addAll(frequencyPattern.get(j).getBehaviorChain());
        		System.out.println(list2);
        		levenshtein(list1,list2);
        		
//        		boolean isSubSequence = isSubSequence(string1,string2);
//        	       if(isSubSequence){
//        	    	   System.out.println(string1 + " is the subsequence of " + string2);
//        	       }
        	        
        	}
        }
//       boolean isSubSequence = isSubSequence("12345","123745");
//       if(isSubSequence){
//    	   System.out.println("12345 is the subsequence of 123745");
//       }
        

		
		String path="/Users/ling/Desktop/pattern/spmf/ca/pfv/spmf/test/contextPrefixSpan2.txt";
//		String path="/Users/ling/Documents/Eclipseworkspace/Weka/test/src/test/behavor2.txt";
		writeFileContext(pattList,path);	
	}
	
	public static boolean isSubSequence(String sub,String mString){
        //用来记录母序列游标移动到哪里了
        int ptr = 0;
        //外层循环遍历子序列
        for(int i = 0;i<sub.length();i++){
            char currentChar = sub.charAt(i);
            //判断游标已经出了母序列了，则代表不是其子序列
            if (ptr>=mString.length()){
                return false;
            }
            for (int j = ptr;j<mString.length();j++ ){
                //找到了一个子序列对应的字符了，游标继续移动
                if (currentChar==mString.charAt(j)){
                    ptr = j+1;
                    break;
                }
                //找到了最后一位了，却没有break，说明没有找到
                if (j==mString.length()-1){
                    return false;
                }
            }
        }
        return true;
    }
	
	public static void levenshtein(List<String> str1, List<String> str2) {
		// 计算两个字符串的长度。
		int len1 = str1.size();
		int len2 = str2.size();
		// 建立上面说的数组，比字符长度大一个空间
		int[][] dif = new int[len1 + 1][len2 + 1];
		// 赋初值，步骤B。
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		// 计算两个字符是否一样，计算左上的值
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
 
//				System.out.println("i = " + i + " j = " + j + " str1 = "
//						+ str1.get(i - 1) + " str2 = " + str2.get(j - 1));
				if (str1.get(i - 1).equals(str2.get(j - 1)) ) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 取三个值中最小的
				dif[i][j] = Math.min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1);
 
//				System.out.println("i = " + i + ", j = " + j + ", dif[i][j] = "
//						+ dif[i][j]);
			}
		}
		System.out.println("Pattern\"" + str1 + "\"与\"" + str2 + "\"的比较");
		// 取数组右下角的值，同样不同位置代表不同字符串的比较
		System.out.println("差异步骤：" + dif[len1][len2]);
		// 计算相似度
		float similarity = 1 - (float) dif[len1][len2]
				/ Math.max(str1.size(), str2.size());
		System.out.println("相似度：" + similarity);
	}
	
//	public static void levenshtein(String str1, String str2) {
//		// 计算两个字符串的长度。
//		int len1 = str1.length();
//		int len2 = str2.length();
//		// 建立上面说的数组，比字符长度大一个空间
//		int[][] dif = new int[len1 + 1][len2 + 1];
//		// 赋初值，步骤B。
//		for (int a = 0; a <= len1; a++) {
//			dif[a][0] = a;
//		}
//		for (int a = 0; a <= len2; a++) {
//			dif[0][a] = a;
//		}
//		// 计算两个字符是否一样，计算左上的值
//		int temp;
//		for (int i = 1; i <= len1; i++) {
//			for (int j = 1; j <= len2; j++) {
// 
////				System.out.println("i = " + i + " j = " + j + " str1 = "
////						+ str1.charAt(i - 1) + " str2 = " + str2.charAt(j - 1));
//				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
//					temp = 0;
//				} else {
//					temp = 1;
//				}
//				// 取三个值中最小的
//				dif[i][j] = Math.min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1);
// 
////				System.out.println("i = " + i + ", j = " + j + ", dif[i][j] = "
////						+ dif[i][j]);
//			}
//		}
//		System.out.println("字符串\"" + str1 + "\"与\"" + str2 + "\"的比较");
//		// 取数组右下角的值，同样不同位置代表不同字符串的比较
//		System.out.println("差异步骤：" + dif[len1][len2]);
//		// 计算相似度
//		float similarity = 1 - (float) dif[len1][len2]
//				/ Math.max(str1.length(), str2.length());
//		System.out.println("相似度：" + similarity);
//	}
	
	private static void saveJsonObjectToMongoDb(JSONObject jsonObject,String databaseName,String collectionName) {
		try{		 
			 MongoClient mongoClient =  new MongoClient("localhost",27017);
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
				pattern_name+=1;
				map.put(item.getNewBehaviorChain(), pattern);
				afterCountPatternList.add(pattern);	
			}else {
				pattern.setWeight(pattern.getWeight()+item.getWeight());
			}
		}
		return afterCountPatternList;
	}
	

//	
//	private static int findCycleNumberFromInputString(String oriString,String sToFind) {
////    	String a[] = s.split("#");
////    	String sToFind = a[1];
////    	System.out.println("sToFind: " + sToFind);
//        int num = 0;
//        String newString = oriString;
////        System.out.println("newString: "+newString);
//        while (newString.contains(sToFind)) {
////        	System.out.println("indexOf: "+this.oriString.indexOf(sToFind));
//        	newString = newString.substring(newString.indexOf(sToFind) + sToFind.length());
////        	System.out.println("newString: "+newString);
//            num ++;
//        }
//        return num;
//    }
	
	//判断original pattern是否存在循环
//	private static void foundInternalCycle(ArrayList<List<String>> pattList, ArrayList<Cycle> cycleList) {
//		for (List<String> patt : pattList)
//		{
//            String patt1 = patt.toString();
//		    for(Cycle cycle: cycleList){
//		    	ArrayList<String> cycleTrace = cycle.getTrace();
//		    	if(!patt.equals(cycleTrace)){
//		    	    System.out.println(patt1+"....");
//		    	    String cycleChain= convertToString(cycleTrace);
////		    	    String chain = cycleChain.toString();
//		    	    
////		    		System.out.println(patt.containsAll(cycleChain));
//		    	    System.out.println(cycleChain);
//		    	    System.out.println(findCycleNumberFromInputString(patt1,cycleChain));
//		    	    
////			    	System.out.println(patt1.contains(cycleChain));
//		    	}
//		    }
//		    	
//		    
//		}
//		
//	}

//	private static String convertToString(ArrayList<String> cycleTrace) {
//		String stringCycle = "";
////		if(cycleTrace.size()==1){
////			stringCycle = cycleTrace.get(0);
////		}else if(cycleTrace.size()>1){
////			
////		}
//		
//		for(int i=0;i<cycleTrace.size();i++){
//			if(i==cycleTrace.size()-1){
//				stringCycle +=cycleTrace.get(i);
//			}else{
//				stringCycle +=cycleTrace.get(i) + ",";
//			}
//		}
//		
//		return stringCycle;
//	}


//	private static List<Pattern> getFrequencyPattern(List<Pattern> patternList, float support) {
//		Map<List<String>, Integer> map = new HashMap<List<String>, Integer>();
////		System.out.println(size);
////		System.out.println(support);
//		
//		for (Map.Entry<List<String>, Integer> entry : map2.entrySet()) { 
////			System.out.println(entry.getValue());
//			float a= 0.0f;
//			a = (float)entry.getValue()/(float)size;
//			if(a >= support){
//				map.put(entry.getKey(), entry.getValue());
//			}
//		} 
//		
//		return map;
//	}

	

	private static void printMap(Map<List<String>, Integer> map) {
		for (Map.Entry<List<String>, Integer> entry : map.entrySet()) { 
			   System.out.println("Pattern = " + entry.getKey() + ", Weight = " + entry.getValue()); 
			 } 
		
	}
//	private static void printList(List<Entry<List<String>, Integer>> list) {
//      for(Map.Entry<List<String>,Integer> mapping:list){ 
//    	  System.out.println("Pattern = " + mapping.getKey() + ", Weight = " + mapping.getValue()); 
//      } 
//	}

	private static List<Pattern> CountDuplicatedList(List<List<String>> pattList) {
		List<Pattern> patternList = new ArrayList<>();
	
		Map<List<String>, Pattern> map = new HashMap<List<String>, Pattern>();
		Pattern pattern;
		int pattern_name=1;
		for (List<String> item : pattList) {
			pattern = map.get(item);
			
//			System.out.println(pattern.getWeight());
//			System.out.println(item);
			
			if(pattern==null){
				pattern = new Pattern();
				pattern.setPatternName(pattern_name);
				pattern.setBehaviorChain(item);
				pattern.setWeight(1);
				pattern_name+=1;
				map.put(item, pattern);
				patternList.add(pattern);
				
			}else {
				pattern.setWeight(pattern.getWeight()+1);
			}
		}
		
//		Iterator<List<String>> keys = map2.keySet().iterator();
//		while (keys.hasNext()) {
//			List<String> key = keys.next();
//			System.out.println(key + ":" + map2.get(key).intValue() + ", ");
//		}
		return patternList;
	}

	private static List<List<String>> SplitInputStringByStartVertex(String oriString, String start) {
		  List<List<String>> pattList = new ArrayList<List<String>>();
		  String behaviorList2[] = oriString.split(",");
		  
		  List<String> behaviorList= new ArrayList<>();
		  
		  
		  for(int i = 0; i < behaviorList2.length; i++){
			 behaviorList.add(behaviorList2[i]);
		  }

		  ArrayList<Integer> findStart = new ArrayList<>();

		  for(int i=0;i<behaviorList.size();i++){
			  if(behaviorList.get(i).equals(start)){			
				  findStart.add(i);
//				  System.out.println(i);
			  }
		  }	  

		  for(int i=0;i<findStart.size()-1;i++){
			  List<String> behavior = new ArrayList<>();
			  if(i==0 && (findStart.get(i)!=0)){
				  behavior = behaviorList.subList(0, findStart.get(i));
				  pattList.add(behavior);
//				  add(behavior);
				  System.out.println(behavior);
			  }
			  behavior = behaviorList.subList(findStart.get(i), findStart.get(i+1));
			  pattList.add(behavior);
			  System.out.println(behavior);
		  }
		  if(findStart.get(findStart.size()-1) < behaviorList.size()){
			  List<String> behavior = new ArrayList<>();
			  behavior = behaviorList.subList(findStart.get(findStart.size()-1),behaviorList.size());
			  pattList.add(behavior);
			  System.out.println(behavior);
		  }
		  return pattList;
	}
	
	public static void writeFileContext(List<List<String>> pattList, String path) throws Exception {
		File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (int i=0;i<pattList.size();i++){
        	List<String> arrayList = pattList.get(i);
        	for(int j=0;j<arrayList.size();j++){
        		writer.write(arrayList.get(j) + " " + "-1"+" ");
        	}
        	writer.write("-2"+"\n");
            
        }
        writer.close();
    }
				
		//"X,Y,A,B,C,D,E,A,B,C,D,E,A,M,N,E,A,M,N,E,A,B,C,D,E,A,M,N,E"
//		List<ArrayList<Integer>> pairList = new ArrayList<ArrayList<Integer>>();
		
		
		
//        String targetString = inputString;  
//        StringBuilder outputStringBuilder = new StringBuilder(); 
//        String outputString;  
//        while (targetString.length() > 0 && targetString.indexOf(start,1) > 0) { 
//        	System.out.println(targetString.length());
//        	System.out.println(targetString.indexOf(start,1));
//            outputString = targetString.substring(0, targetString.indexOf(start,1));
//            targetString = targetString.substring(targetString.indexOf(start,1));
//            System.out.println(targetString);
//            outputStringBuilder.append(outputString);
//            outputStringBuilder.append("\n");
//        }
//        
//        System.out.println(outputStringBuilder.toString());
//		System.out.println("--------------behavior split-----------------");
//		ArrayList<Integer> pair= new ArrayList<>();
//		ArrayList<Integer> nopair= new ArrayList<>();
//		ArrayList<String> patt= new ArrayList<>();
//		int tag=-1;
//		ArrayList<ArrayList<String>> pattList = new ArrayList<ArrayList<String>>();
////		boolean match=f;
//		int a=0;
//		
//		
//		for(int i=0;i<behaviorList.length;i++){
//
//				if(behaviorList[i].equals(start)){
////					System.out.println("("+(tag+1) + " "+i+")");				
//					if(i!=tag+1){
//						for(int j=tag+1;j<i;j++){
//							patt.add(behaviorList[j]);
//						}
//						ArrayList<String> patt2= new ArrayList<>();
//						patt2.addAll(patt);
//						pattList.add(patt2);
//						a+=1;
//						System.out.println(a+": "+patt.toString());
//						patt.clear();
//					}
////					String string = teString.substring(pair.get(0), pair.get(1)+1);
//					pair.add(i);	
//				}
//				
//				if(behaviorList[i].equals(end)){
//					pair.add(i);
//					tag=i;
//					a+=1;
////					System.out.println("pair: "+pair.toString());
//					
//					for(int j=pair.get(0);j<pair.get(1)+1;j++){
//						patt.add(behaviorList[j]);
//					}
//					ArrayList<String> patt2= new ArrayList<>();
//					patt2.addAll(patt);
//					pattList.add(patt2);
//					System.out.println(a+": "+patt.toString());
////					System.out.println("pattlist: "+pattList.toString());
//					pair.clear();
//					patt.clear();
//				}
//				
//				
//				
//		}	
////		System.out.println(pattList.size());
////		System.out.println(pattList.get(0).size());
//		String path="/Users/ling/Documents/Eclipseworkspace/Weka/test/src/tree/output.txt";
//		writeFileContext(pattList,path);
//		
//		
//		//path
////		DFS dfs = new DFS(graph);
////		List<String> path2 = new ArrayList<String>();
////		path2 = dfs.getPathFrom("A");
////		if(path2!=null){
////			for(int i=0;i<path2.size();i++){
////				System.out.println(path2.get(i));
////			}
////		}
//	}
//	public static void writeFileContext(List<ArrayList<String>>  pattList, String path) throws Exception {
//		File file = new File(path);
//        //如果没有文件就创建
//        if (!file.isFile()) {
//            file.createNewFile();
//        }
//        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
//        for (int i=0;i<pattList.size();i++){
//        	ArrayList<String> arrayList = pattList.get(i);
//        	for(int j=0;j<arrayList.size();j++){
//        		writer.write(arrayList.get(j) + " " + "-1"+" ");
//        	}
//        	writer.write("-2"+"\n");
//            
//        }
//        writer.close();
//    }

}

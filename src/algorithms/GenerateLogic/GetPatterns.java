package algorithms.GenerateLogic;
//import algorithms.splitpatterns.SplitBehaviors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class GetPatterns {
	public static List<PatternBehaviors> getPatternsFromMongoDB(String clientName, String databaseName, String knowledgeId) throws Exception{
		Map<String, String> behaviorsMap = new LinkedHashMap<>();
		List<PatternBehaviors> PatternBehaviorsList = new ArrayList<>();
		try{		 			 
			 MongoClient mongoClient =  new MongoClient(clientName,27017);
			 MongoDatabase mongoDatabase =  mongoClient.getDatabase(databaseName);
			 System.out.println("Connect to database successfully");	
			 
			 MongoCollection<Document> knowledgeCollection =  mongoDatabase.getCollection("knowledge_test");			 	 			 
			 BasicDBObject query = new BasicDBObject("knowledgeId",knowledgeId);
			 MongoCursor<Document> knowledgeCursor = knowledgeCollection.find(query).iterator();
			 List<String> patternIdsList = new ArrayList<>();
			 while(knowledgeCursor.hasNext()){
				 Document document = knowledgeCursor.next();
				 System.out.println(document.toJson());
				 patternIdsList = (List<String>) document.get("patternIds");
//				 System.out.println(patternIds.get(0));
			 }
			 System.out.println();
			 MongoCollection<Document> patternCollection =  mongoDatabase.getCollection("patterns_test");			 
			 for(String patternId : patternIdsList) {
//				 System.out.println(patternId);
				 BasicDBObject query2 = new BasicDBObject("patternId",Integer.valueOf(patternId));
				 MongoCursor<Document> patternCursor = patternCollection.find(query2).iterator();
				 List<String> patternBehaviors = new ArrayList<>();
				 List<List<String>> uniqueIdsList = new ArrayList<>();
				 while(patternCursor.hasNext()){
					 Document document = patternCursor.next();
					 System.out.println(document.toJson());
					 patternBehaviors = (List<String>) document.get("patternBehaviors");
					 uniqueIdsList = (List<List<String>>) document.get("uniqueIdsList");					 
					 System.out.println("patternBehaviors: " + patternBehaviors.toString());
					 System.out.println("uniqueIdsList: " + uniqueIdsList.toString());
					 PatternBehaviors pattern = new PatternBehaviors();
					 pattern.setPatternId(patternId);
					 pattern.setPatternBehaviors(patternBehaviors);
					 pattern.setUniqueIdsList(uniqueIdsList);	
					 PatternBehaviorsList.add(pattern);
				 }
			 }
			 mongoClient.close();
		 }catch(MongoException e){
			e.printStackTrace();
		}
		return PatternBehaviorsList; 
	}

}

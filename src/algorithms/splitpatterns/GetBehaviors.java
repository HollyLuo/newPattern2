package algorithms.splitpatterns;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class GetBehaviors {
	public static Map<String, String> getBehaviorsFromMongoDB(String clientName, String databaseName, String collectionName) throws Exception{
		Map<String, String> behaviorsMap = new LinkedHashMap<>();
		try{		 
			 MongoClient mongoClient =  new MongoClient(clientName,27017);
			 MongoDatabase mongoDatabase =  mongoClient.getDatabase(databaseName);
			 System.out.println("Connect to database successfully");			 
			 MongoCollection<Document> collection =  mongoDatabase.getCollection(collectionName);			 	 
			 MongoCursor<Document> cursor = collection.find().iterator();
//			 ArrayList<String> behaviorsList = new ArrayList<>();
			 			 
	         while(cursor.hasNext()){  
	        	Document document = cursor.next();
	        	String uniqueId =  (String) document.get("uniqueId");
	        	String behaviorID = (String) document.get("behaviorID");	        	
//	        	behaviorsList.add(behaviorID);
	        	behaviorsMap.put(uniqueId, behaviorID);
	         }          
//	         System.out.println(behaviorsList); 
//	         String path="behavior3.txt";
//	 		 writeFileContext(behaviorsList,path);	
			 mongoClient.close();
		 }catch(MongoException e){
			e.printStackTrace();
		}
		return behaviorsMap; 
	}
//	public static void writeFileContext(ArrayList<String> behaviorChain, String path) throws Exception {
//		File file = new File(path);
//        //如果没有文件就创建
//        if (!file.isFile()) {
//            file.createNewFile();
//        }
//        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
//        for (int i=0;i<behaviorChain.size();i++){
//        	String behavior = behaviorChain.get(i);
//        	writer.write(behavior);
//        	if(i!=behaviorChain.size()-1){
//        		writer.write(",");
//        	}
//        }
//        writer.close();
//    }

}

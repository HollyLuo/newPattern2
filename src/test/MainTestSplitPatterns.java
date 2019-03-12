package test;

import java.util.LinkedHashMap;
import java.util.Map;

import algorithms.splitpatterns.SplitBehaviors;
import algorithms.splitpatterns.GetBehaviors;
/**
 * Example of how to use APRIORI algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestSplitPatterns {

	public static void main(String [] arg) throws Exception{
		Map<String, String> behaviorsMap = new LinkedHashMap<>();
		behaviorsMap = GetBehaviors.getBehaviorsFromMongoDB("localhost", "IRA_test", "behaviors");
//		String input = fileToPath("behavior3.txt");
		float support = 0.2f;
		SplitBehaviors.runAlgorithm(behaviorsMap);
		
	}
	
//	public static String fileToPath(String filename) throws UnsupportedEncodingException{
//		URL url = MainTestSplitPatterns.class.getResource(filename);
//		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
//	}
}

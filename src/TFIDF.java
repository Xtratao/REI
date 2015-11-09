import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class TFIDF {
	
	protected static String DIRNAME_DATA = "/Users/Mouhcine/Documents";

	
	/**
	 * Le fichier contenant les mots vides
	 */
	private static String STOPWORDS_FILENAME = "./frenchST.txt";

	
	public static HashMap<String,Double> getTfIdf(String file, Normalizer normalizer) throws IOException
	{
		String filePath="";
		if(file!="requete.txt")
			filePath = DIRNAME_DATA+"/"+file.substring(0, 4)+"/"+file.substring(4,6)+"/"+file.substring(6,8)+"/"+file;
		
		else
			filePath = file;
		
			ArrayList<String> words;
			if(!new File(filePath).exists())
				words=new ArrayList<String>();			
			else
				words = normalizer.normalize(new File(filePath));
			
			HashMap<String,Double> tfidfs = new HashMap<String,Double>();
			int tf=0,df=0,N=ReadXMLFile.listDocuments.size();
			double tfIdf=0;
			
			for(String word : words)
			{
				System.out.println(word + "    :    "+Main.invertedFile.get(word));
				if(file!="requete.txt")
					tf = Main.invertedFile.get(word).get(Integer.toString(ReadXMLFile.listDocuments.indexOf(file)));
				else
					tf = Collections.frequency(words, word);
				
				df = Main.dfs.get(word);
				tfIdf = (double)tf * Math.log((double)N / (double)df);
				tfidfs.put(word, tfIdf);
			}
		
		
		return tfidfs;
	}

}

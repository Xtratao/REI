import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tools.FrenchStemmer;

public class Main {
	
	protected static String DIRNAME = "lemonde-utf8";
	protected static String DIRNAME_DATA = "/Users/Mouhcine/Documents";



	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		String outDirName = "outTfIdfData2015";

		GetDocumentsFromInvertedFile.loadInvertedFile();
		TreeSet<String> tree = new TreeSet<String>();
		tree = GetDocumentsFromInvertedFile.fusion(new File("requete.txt"),new FrenchStemmer());
				
		
		
		ReadXMLFile.getDocumentsNames();
		ReadXMLFile.listDocuments.add("requete.txt");
		TFIDF.getWeightFiles(ReadXMLFile.listDocuments,new File(new File(DIRNAME+"/.."), outDirName),new FrenchStemmer());
		
		ArrayList<String> listDocumentsTfIdf = new ArrayList<String>();
		for(String file: tree)
			listDocumentsTfIdf.add(file.split(".txt")[0]+".poids");
		listDocumentsTfIdf.add("requete.poids");
		
		Map<String, Double> map = Similarite.getSimilarDocuments(listDocumentsTfIdf,new File(new File(DIRNAME+"/../"+outDirName), "requete.poids"), new File(DIRNAME + "/../outTfIdfData2015"));
		SortByValue.printMap(map);
		
		String filePath="";
		//Entry<String, Double> entry = map.entrySet().iterator().next();
		int i=1;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			if(i==2)
			 {	
			 	String fileName = entry.getKey().split(".poids")[0]+".txt";
			 	filePath = DIRNAME_DATA+"/"+fileName.substring(0, 4)+"/"+fileName.substring(4,6)+"/"+fileName.substring(6,8)+"/"+fileName;
			 	break;
			 }
			i++;
		}
	    Desktop.getDesktop().edit(new File(filePath));


	}

}

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tools.FrenchStemmer;

public class Main {
	
	protected static String DIRNAME = "lemonde-utf8";
	protected static String DIRNAME_DATA = "/Users/Mouhcine/Documents";
	private static String STOPWORDS_FILENAME = "./frenchST.txt";

	public static TreeMap<String, TreeMap<String,Integer>> invertedFile;
	public static TreeMap<String,Integer> dfs;

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		
		ReadXMLFile.getDocumentsNames();
		invertedFile = Indexation.getInvertedFileWithTfs(ReadXMLFile.listDocuments, new FrenchStemmer(new File(STOPWORDS_FILENAME)));
		dfs = Indexation.getDfs();
		
		/*String outDirName = "outTfIdfData2015";
		GetDocumentsFromInvertedFile.loadInvertedFile(new File("requete.txt"),new FrenchStemmer(new File(STOPWORDS_FILENAME)));
		*/
		
		
		TreeSet<String> tree = GetDocumentsFromInvertedFile.inter(GetDocumentsFromInvertedFile.getListOfDocumentsLists(new File("requete.txt"),new FrenchStemmer(new File(STOPWORDS_FILENAME))));
		
			
		Map<String, Double> map = Similarite.getSimilarDocuments(tree, "requete.txt", new FrenchStemmer(new File(STOPWORDS_FILENAME)) );
		SortByValue.printMap(map);
		String filePath="";

		int i=1;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			if(i==2)
			 {	
			 	String fileName = entry.getKey();
			 	filePath = DIRNAME_DATA+"/"+fileName.substring(0, 4)+"/"+fileName.substring(4,6)+"/"+fileName.substring(6,8)+"/"+fileName;
			 	break;
			 }
			i++;
		}
		
	    Desktop.getDesktop().edit(new File(filePath));

	    
	

	}

}

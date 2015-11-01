import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tools.FrenchStemmer;
import tools.Normalizer;

public class Indexation {
	
	/**
	 * Le répertoire du corpus
	 */
	protected static String DIRNAME = "lemonde-utf8";
	protected static String DIRNAME_DATA = "/Users/Mouhcine/Documents";
	
	/**
	 * Le fichier contenant les mots vides
	 */
	private static String STOPWORDS_FILENAME = "./frenchST.txt";


	/**
	 * analyse l’ensemble des fichiers d’un répertoire collecte, pour chaque mot, la liste des
	 * fichiers dans lesquels ce mot apparaît
	 * @param dir : directory of the text files
	 * @param normalizer : the normalizer we want to use
	 * @return TreeMap<String, TreeSet<String>> : TreeMap of (word, list of the documents where is the word) 
	 **/
	public static TreeMap<String, TreeSet<String>> getInvertedFile(ArrayList<String> listDocumentsName,
			Normalizer normalizer) throws IOException
	{

		TreeMap<String, TreeSet<String>> InvertedFile = new TreeMap<String, TreeSet<String>>();
		ArrayList<String> words;
		String filePath="";
		int i=0;
			
			for (String file : listDocumentsName) {
				filePath = DIRNAME_DATA+"/"+file.substring(0, 4)+"/"+file.substring(4,6)+"/"+file.substring(6,8)+"/"+file;
				
				if(!new File(filePath).exists())
					words=new ArrayList<String>();	
					
				else
					words = normalizer.normalize(new File(filePath));
			
				
				for (String word : words) {
					if(!InvertedFile.containsKey(word))
						InvertedFile.put(word, new TreeSet<String>());
					TreeSet<String> listDocuments = new TreeSet<String>();
					listDocuments = InvertedFile.get(word);
					listDocuments.add(file);					
					
					InvertedFile.put(word,listDocuments);
				}
			}
			
		
		return InvertedFile;
	}
	
	/**
	 * Sauvegarde de l'index dans un fichier sous le format: mot   freq   txt1.txt,txt2.txt, ...
	 * @param invertedFile : the inverted file that we computed in the previous function
	 * @param outFile : the file where to write the inverted file
	 **/
	public static void saveInvertedFile(TreeMap<String, TreeSet<String>> invertedFile, File outFile)
			throws IOException
	{
		PrintWriter out = new PrintWriter(outFile);
		// Ecriture des mots
		for (Map.Entry<String, TreeSet<String>> elt : invertedFile.entrySet()){
			int length = elt.getValue().toString().length();
			out.println(elt.getKey() + "\t"+ elt.getValue().size()+"\t" + elt.getValue().toString().substring(1, length-1));		
		}
		out.close();
	}
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException 
	{
		ReadXMLFile.getDocumentsNames();
		saveInvertedFile(getInvertedFile(ReadXMLFile.listDocuments, new FrenchStemmer()),new File(DIRNAME+"/../fichierInverseData2015.txt"));
	}
	

}

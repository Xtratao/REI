import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import tools.FrenchStemmer;
import tools.Normalizer;

public class Indexation {
	
	/**
	 * Le répertoire du corpus
	 */
	protected static String DIRNAME_Mouhcine = "/Users/Mouhcine/Documents/workspace/ProjetREI/lemonde-utf8";
	
	/**
	 * Le fichier contenant les mots vides
	 */
	private static String STOPWORDS_FILENAME = "/Users/Mouhcine/Documents/Etudes/Traitement de text/TP1/frenchST.txt";


	/**
	 * analyse l’ensemble des fichiers d’un répertoire collecte, pour chaque mot, la liste des
	 * fichiers dans lesquels ce mot apparaît
	 * @param dir : directory of the text files
	 * @param normalizer : the normalizer we want to use
	 * @return TreeMap<String, TreeSet<String>> : TreeMap of (word, list of the documents where is the word) 
	 **/
	public static TreeMap<String, TreeSet<String>> getInvertedFile(File dir,
			Normalizer normalizer) throws IOException
	{

		TreeMap<String, TreeSet<String>> InvertedFile = new TreeMap<String, TreeSet<String>>();
		ArrayList<String> words;
		
		if (dir.isDirectory()) {
			String[] fileNames = dir.list();
			
			for (String fileName : fileNames) {
				
				words = normalizer.normalize(new File(dir, fileName));
			
				for (String word : words) {
					if(!InvertedFile.containsKey(word))
						InvertedFile.put(word, new TreeSet<String>());
					TreeSet<String> listDocuments = new TreeSet<String>();
					listDocuments = InvertedFile.get(word);
					listDocuments.add(fileName);					
					
					InvertedFile.put(word,listDocuments);
				}
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
		PrintWriter out = new PrintWriter (outFile);
		String s="";
		int i=0;
		TreeSet<String> liste = new TreeSet<String>();
		int length= 0;
		for(Map.Entry<String, TreeSet<String>> l : invertedFile.entrySet())
		{	
			s="";
			i=0;
			s=l.getKey().toString() + "\t" + l.getValue().size() + "\t";
			liste = l.getValue();
			length = liste.size();
			for(String S : liste)
			{
				i++;					
				if(i==length)
					s+= S;										
				else
					s+= S+",";				
			}
			
			out.println(s);
			
		}
		out.close();
	}
	
	public static void main(String[] args) throws IOException 
	{
		saveInvertedFile(getInvertedFile(new File(DIRNAME_Mouhcine), new FrenchStemmer()),new File(DIRNAME_Mouhcine+"/../fichierInverse.txt"));

	}
	

}

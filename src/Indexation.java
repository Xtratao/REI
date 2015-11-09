import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	private static Map<String, Integer> idFiles;
	
	/**
	 * Analyse l’ensemble des fichiers dans l'ArrayList list document qui contiennent les 10000
	 * fichiers du tronc commun et récupère pour chaque mot, la liste
	 *  des fichiers dans lesquels ce mot apparaît
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
	 * Sauvegarde les dfs dans un fichier sous le format: mot   df   
	 * @param invertedFile : the inverted file that we computed in the previous function
	 * @param outFile : the file where to write the df file
	 **/
	public static void saveDfFile(TreeMap<String, TreeSet<String>> invertedFile, File outFile)
			throws IOException
	{
		PrintWriter out = new PrintWriter(outFile);
		// Ecriture des mots
		for (Map.Entry<String, TreeSet<String>> elt : invertedFile.entrySet()){
			int length = elt.getValue().toString().length();
			out.println(elt.getKey() + "\t"+ elt.getValue().size());		
		}
		out.close();
	}
	
	public static TreeMap<String,Integer> getDfs() throws NumberFormatException, IOException
	{
		
		BufferedReader buffer = new BufferedReader(new FileReader("fichierDfsData2015.txt"));
		String line = "";
		TreeMap<String, Integer> dfs=new TreeMap<String,Integer>();
		
		// we loop the buffer
		while ((line = buffer.readLine()) != null)
		{	
			dfs.put(line.split("\t")[0],Integer.parseInt(line.split("\t")[1]));
		}
		
		return dfs;
	}
	
	
	/**
	 * la méthode getTermFrequencies permets de compter le nombre d'occurrences 
	 * des mots du corpus
	 * @param normalizer : the normalizer
	 * @param ArrayList<String> listDocuments : the list of the 10000 documents
	 * @return TreeMap<String(mot),TreeMap<String(document),Integer(tf)> : the frequency of the word in that document
	 */
	public static TreeMap<String, TreeMap<String,Integer>> getInvertedFileWithTfs(ArrayList<String> listDocuments, Normalizer normalizer) throws IOException {

		TreeMap<String, TreeMap<String,Integer>> invertedFile = new TreeMap<String, TreeMap<String,Integer>>();
		String documentPath="";
		for(String dct : listDocuments)
		{
			// hits est un TreeMap qui contient les mots du fichier et leurs TF
			TreeMap<String, Integer> hits = new TreeMap<String, Integer>();
			documentPath = DIRNAME_DATA+"/"+dct.substring(0, 4)+"/"+dct.substring(4,6)+"/"+dct.substring(6,8)+"/"+dct;

			// Appel de la méthode de normalisation
			ArrayList<String> words;

			if(!new File(documentPath).exists())
				words=new ArrayList<String>();					
			else
				words = normalizer.normalize(new File(documentPath));
			
			Integer number;
			// Pour chaque mot de la liste, on remplit un dictionnaire
			// du nombre d'occurrences pour ce mot
			for (String word : words) {
				word = word.toLowerCase();
				// on récupère le nombre d'occurrences pour ce mot
				number = hits.get(word);
				// Si ce mot n'était pas encore présent dans le dictionnaire,
				// on l'ajoute (nombre d'occurrences = 1)
				if (number == null) 
					hits.put(word, 1);
				
				// Sinon, on incrémente le nombre d'occurrence
				else 
					hits.put(word, ++number);				
			}
			
			TreeMap<String,Integer> listDocumentAndtf;
			
			for (Map.Entry<String, Integer> elt : hits.entrySet()){
				
				if(invertedFile.containsKey(elt.getKey()))
					listDocumentAndtf = invertedFile.get(elt.getKey());
				else
					listDocumentAndtf = new TreeMap<String,Integer>();
				
				listDocumentAndtf.put(dct, elt.getValue());
				invertedFile.put(elt.getKey(), listDocumentAndtf);					
			}			
			
		}
		
		return invertedFile;		
	}
	
	/**
	 * Sauvegarde de l'index dans un fichier sous le format: mot  txt1.txt,txt2.txt, ...
	 * @param invertedFile : the inverted file that we computed in the previous function
	 * @param outFile : the file where to write the inverted file
	 **/
	public static void saveInvertedFile(TreeMap<String, TreeMap<String,Integer>> invertedFile, ArrayList<String> listDocuments, File outFile, Normalizer normalizer)
			throws IOException
	{

		PrintWriter out = new PrintWriter(outFile);
		String mot="";
		int documentId=0;
		int wordFrequency = 0;
		String line="";
		// Ecriture des mots
		for (Map.Entry<String, TreeMap<String,Integer>> elt : invertedFile.entrySet()){
			mot = elt.getKey();
			TreeMap<String,Integer> listOfDocumentsIdAndTfs = elt.getValue();
			line = mot + "\t";
			for(Map.Entry<String,Integer> dctTf : listOfDocumentsIdAndTfs.entrySet())
			{
				documentId = listDocuments.indexOf(dctTf.getKey());
				wordFrequency = dctTf.getValue();
				line += documentId+":"+wordFrequency+",";
			}
			
			out.println(line.substring(0, line.length()-1));
			//int length = listOfDocumentsIdAndTfs.toString().length();
			//out.println(elt.getKey() + "\t" + listOfDocumentsIdAndTfs.toString().substring(1, length-1));		
		}
		out.close();
	}
	
	
	
	/*
	/**
	 * Crée un id pour chaque fichier et le met dans la map: idFiles
	 * @param invertedFile : the inverted file that we computed in the previous function
	 * @param outFile : the file where to write the inverted file
	 * @throws FileNotFoundException 
	 **/
	/*
	public static void creationAndSaveFilesIds(ArrayList<String> listDocuments, File outFile) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter(outFile);
		int i=1;
		
		for (String file: listDocuments){
			//idFiles.put(elt.getKey(), i);
			out.println(file.getKey() + "\t"+ i);	
			i++;
		}
		
	}
	*/
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException 
	{
		ReadXMLFile.getDocumentsNames();
		
		System.out.println("Création du fichier des dfs ... ");
		saveDfFile(getInvertedFile(ReadXMLFile.listDocuments, new FrenchStemmer(new File(STOPWORDS_FILENAME))),new File(DIRNAME+"/../fichierDfsData2015.txt"));
		System.out.println("Fichier des dfs créé à l'emplacement: "+DIRNAME+"/../fichierDfsData2015.txt");


		TreeMap<String, TreeMap<String, Integer>> map = getInvertedFileWithTfs(ReadXMLFile.listDocuments, new FrenchStemmer(new File(STOPWORDS_FILENAME)));
		
		System.out.println("Création du fichier inverse compressé sous format : mot \t idFile1:tfFile1, idFile2:tfFile2, ... ");
		saveInvertedFile(map, ReadXMLFile.listDocuments, new File(DIRNAME+"/../fichierInverseData2015.txt"), new FrenchStemmer(new File(STOPWORDS_FILENAME)) );
		System.out.println("Fichier des ids créé à l'emplacement: "+DIRNAME+"/../fichierInverseData2015.txt");

	}
	
	
	

}

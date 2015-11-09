import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class TFIDFV2 {
	
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
	 * la méthode \emph{stemming} permettant de raciniser le texte
	 * d'un fichier du corpus.
	 **/
	protected static ArrayList<String> stemming(File file) throws IOException {
		//TODO 
		return null;
	}

	/**
	 * la méthode \emph{noNormalization} permets de faire une segmentation simple sans normalisation
	 * d'un fichier du corpus.
	 **/
	protected static ArrayList<String> noNormalization(File file) throws IOException {
		//TODO
		return null;
	}
	
	/**
	 * la méthode \emph{getTermFrequencies} permets de compter le nombre d'occurrences 
	 * de chaque mot dans un fichier.
	 * @param file : the text file
	 * @param normalizer : the normalizer
	 * @return HashMap<String, Integer> : hashmap of the words and their frequencies
	 */
	public static HashMap<String, Integer> getTermFrequencies(File file, Normalizer normalizer) throws IOException {
		// On crée de la table des mots
		HashMap<String, Integer> hits = new HashMap<String, Integer>();

		// Appel de la méthode de normalisation
		ArrayList<String> words = normalizer.normalize(file);
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
		return hits;
		
	}
	
	/**
	 * la méthode \emph{getTermFrequencies} permettant d'afficher le nombre d'occurrences
	 * de chaque mot pour l'ensemble du corpus.
	 * 
	 */
	public static HashMap<String, Integer> getCollectionFrequency(File dir, Normalizer normalizer) throws IOException {
		// we dont know if we gonna use it or not
		return null;
	}

	/**
	 *  Calcule le df, c'est-à-dire le nombre de documents
	 * pour chaque mot apparaissant dans la collection. 
	 * 
	 * @param file : the directory containing the text files
	 * @param normalizer : the normalizer
	 * @return HashMap<String, Integer> : hashmap of the words and the number of documents where they are
	 */
	
	public static HashMap<String, Integer> getDocumentFrequency(ArrayList<String> listDocumentsName, Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		ArrayList<String> wordsInFile;
		ArrayList<String> words;
		String wordLC;
		String filePath = "";
			// Liste des fichiers à traiter pour le TC du répertoire 2015 (les 10000 fichier listés dans
			// le répertoir subindex
	
			
			Integer number;
			for (String file : listDocumentsName) {
				//System.err.println("Analyse du fichier " + file);
				// Les mots présents dans ce document
				wordsInFile = new ArrayList<String>();
				// Appel de la méthode de normalisation
				if(file.equals("requete.txt"))
					filePath = "requete.txt";
				else
					filePath = DIRNAME_DATA+"/"+file.substring(0, 4)+"/"+file.substring(4,6)+"/"+file.substring(6,8)+"/"+file;
				
				if(!new File(filePath).exists())
					words=new ArrayList<String>();						
				else
					words = normalizer.normalize(new File(filePath));
				
				// Pour chaque mot de la liste, on remplit un dictionnaire
				// du nombre d'occurrences pour ce mot
				for (String word : words) {
					wordLC = word;
					wordLC = wordLC.toLowerCase();
					// si le mot n'a pas déjà été trouvé dans ce document :
					if (!wordsInFile.contains(wordLC)) {
						number = hits.get(wordLC);
						// Si ce mot n'était pas encore présent dans le dictionnaire,
						// on l'ajoute (nombre d'occurrences = 1)
						if (number == null) {
							hits.put(wordLC, 1);
						}
						// Sinon, on incrémente le nombre d'occurrence
						else {
							hits.put(wordLC, number+1);
						}
						wordsInFile.add(wordLC);
					}
				}
			}
		
	
		// Affichage du résultat (avec la fréquence)	
	//	for (Map.Entry<String, Integer> hit : hits.entrySet()) {
	//		System.out.println(hit.getKey() + "\t" + hit.getValue());
	//	}
		return hits;
	}

	/**
	 * Calcule le tf.idf des mots d'un fichier en fonction
	 * des df déjà calculés, du nombre de documents et de
	 * la méthode de normalisation.
	 * 
	 * @param file : the document
	 * @param normalizer : the normalizer
	 * @param dfs : hashmap of the document frequencies previously calculated
	 * @param documentNumber : total number of the documents
	 * @return HashMap<String, Integer> : hashmap of the words and their tfidf weight 
	 */
	public static HashMap<String, Double> getTfIdf(File file, HashMap<String, Integer> dfs, int documentNumber, Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		// Appel de la méthode de normalisation
		ArrayList<String> words;
		
		if(!file.exists())
			words=new ArrayList<String>();				
		else
			words = normalizer.normalize(file);
		
		Integer number;

		// Pour chaque mot de la liste, on remplit un dictionnaire 'hits (mot,tf)'
		// du nombre d'occurrences pour ce mot
		for (String word : words) {
			word = word.toLowerCase();
			// on récupère le nombre d'occurrences pour ce mot
			number = hits.get(word);
			// Si ce mot n'était pas encore présent dans le dictionnaire,
			// on l'ajoute (nombre d'occurrences = 1)
			if (number == null) {
				hits.put(word, 1);
			}
			// Sinon, on incrémente le nombre d'occurrence
			else {
				hits.put(word, ++number);
			}
		}
		
		Integer tf;
		Double tfIdf;
		String word;
		HashMap<String, Double> tfIdfs = new HashMap<String, Double>();
		
		// Calcul des tf.idf
		for (Map.Entry<String, Integer> hit : hits.entrySet()) {
			tf = hit.getValue();
			word = hit.getKey();
			tfIdf = (double)tf * Math.log((double)documentNumber / (double)dfs.get(word));
			tfIdfs.put(word, tfIdf);
		}
		
		return tfIdfs;
	}

	/**
	 * Crée, pour chaque fichier d'un répertoire, un nouveau
	 * fichier contenant les poids de chaque mot. Ce fichier prendra
	 * la forme de deux colonnes (mot et poids) séparées par une tabulation.
	 * Les mots devront être placés par ordre alphabétique.
	 * Les nouveaux fichiers auront pour extension .poids
	 * et seront écrits dans le répertoire outDirName.
	 * 
	 * @param normalizer : the normalizer
	 * @param indDir : the documents directory
	 * @param outDir : the directory where we want to put the result files
	 */
	public static void getWeightFiles(ArrayList<String> listDocumentsName, File outDir, Normalizer normalizer) throws IOException {
		// calcul des dfs
		HashMap<String, Integer> dfs = getDocumentFrequency(listDocumentsName, normalizer);
		// Nombre de documents
		int documentNumber = listDocumentsName.size();
		if (!outDir.exists()) 
			outDir.mkdirs();
		
		
		String filePath = "";
		File file=null;
		// TfIdfs 
		
		// On parcours la liste des fichiers qu'on doit traiter
		for (String fileName : listDocumentsName) {
			// On chercher le chemin absolu du fichier
			if(fileName.equals("requete.txt"))
				filePath = "requete.txt";
			else
				filePath = DIRNAME_DATA+"/"+fileName.substring(0, 4)+"/"+fileName.substring(4,6)+"/"+fileName.substring(6,8)+"/"+fileName;
			
			// On crée une instance File qui contient le fichier
			file = new File(filePath);
			HashMap<String, Double> tfIdfs = getTfIdf(file, dfs, documentNumber, normalizer);
			TreeSet<String> words = new TreeSet<String>(tfIdfs.keySet());
			// on écrit dans un fichier
			try {
				FileWriter fw = new FileWriter (new File(outDir, file.getName().replaceAll(".txt$", ".poids")));
				BufferedWriter bw = new BufferedWriter (fw);
				PrintWriter out = new PrintWriter (bw);
				// Ecriture des mots
				for (String word : words) {
					out.println(word + "\t" + tfIdfs.get(word)); 
				}
				out.close();
			}
			catch (Exception e){
				System.out.println(e.toString());
			}		
		}
	}
	
	public static void getWeightRequest(String request, File outDir, Normalizer normalizer) throws IOException, ParserConfigurationException, SAXException {
		
		ArrayList<String> words = normalizer.normalize(new File(request));
		
		BufferedReader buffer = new BufferedReader(new FileReader("fichierInverseData2015.txt"));
		String line = "";
		HashMap<String, Integer> dfs=new HashMap<String,Integer>();
		
		// we loop the buffer
		while ((line = buffer.readLine()) != null)
		{	
			if(words.contains(line.split("\t")[0]))
				dfs.put(line.split("\t")[0],Integer.parseInt(line.split("\t")[1]));
		}
		ReadXMLFile.getDocumentsNames();

		HashMap<String, Double> tfIdfs = getTfIdf(new File(request), dfs, ReadXMLFile.listDocuments.size(), normalizer);
		TreeSet<String> listWords = new TreeSet<String>(tfIdfs.keySet());
		File file=new File(request);
		try {
			FileWriter fw = new FileWriter (new File(outDir, file.getName().replaceAll(".txt$", ".poids")));
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);
			// Ecriture des mots
			for (String word : listWords) {
				out.println(word + "\t" + tfIdfs.get(word)); 
			}
			out.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}		
		
	}

	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException 
	{
		String outDirName = "outTfIdfData2015";
		
		Normalizer stemmerAllWords = new FrenchStemmer();
		Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));
		Normalizer tokenizerAllWords = new FrenchTokenizer();
		Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));

		ReadXMLFile.getDocumentsNames();
		getWeightFiles(ReadXMLFile.listDocuments,new File(new File(DIRNAME+"/.."), outDirName),stemmerNoStopWords);



	}

}

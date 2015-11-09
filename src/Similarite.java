import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import tools.Normalizer;

public class Similarite {

	/**
	 * Le répertoire du corpus
	 */
	protected static String DIRNAME = "lemonde-utf8";

	/**
	 * Le fichier contenant les mots vides
	 */
	private static String STOPWORDS_FILENAME = "./frenchST.txt";



	/**
	 * renvoi le score de similarité entre les deux documents en utilisant la
	 * formule de cos les deux documents contiennent les poids (tfIdf) de deux
	 * documents du corpus
	 * 
	 * @param: file1, file2: files .poids stocking the words of a document and
	 *         their tfidf weights
	 * @return: the cosinus similarity of the two files
	 */

	public static double getSimilarity(HashMap<String, Double> weightsFile1, HashMap<String, Double> weightsFile2)
			throws IOException {

		String line = "";

		// On initiliase les variables qui composent la formule du cosinus

		Double produitScalaire = 0.0; // nominateur
		Double normePoids1 = 0.0, normePoids2 = 0.0; // dénominateur

	

		for (Map.Entry<String, Double> w1 : weightsFile1.entrySet()) {
			if (weightsFile2.containsKey(w1.getKey())) {
				produitScalaire += w1.getValue()
						* weightsFile2.get(w1.getKey());
			}
		}

		for (Map.Entry<String, Double> w1 : weightsFile1.entrySet())
			normePoids1 += Math.pow(w1.getValue(), 2);

		for (Map.Entry<String, Double> w2 : weightsFile2.entrySet())
			normePoids2 += Math.pow(w2.getValue(), 2);

		normePoids1 = Math.sqrt(normePoids1);
		normePoids2 = Math.sqrt(normePoids2);

		return produitScalaire / (normePoids1 * normePoids2);
	}

	/**
	 * renvoi le score de similarité entre un document et l’ensemble des
	 * documents du corpus
	 * 
	 * @param: file : file .poids stocking the words of a document and its tfidf
	 *         weights
	 * @param dir
	 *            : the directory of all the .poids files of the corpus
	 * @throws IOException
	 */

	public static Map<String, Double> getSimilarDocuments(TreeSet<String> listDocumentTfiDF, String requete ,Normalizer normalizer) throws IOException {

        Map<String, Double> map = new HashMap<String, Double>();
        Map<String, Double> sortedMapAsc = new HashMap<String, Double>();
	

			System.out.print("Scores du similarité entre le fichier "+ requete + " et l'ensemble des fichiers après la fusion");
			System.out.println();
			double score = 0;
			
			HashMap<String, Double> weightsRequete = TFIDF.getTfIdf(requete, normalizer);
			
			
			for (String fileName : listDocumentTfiDF) {
				score = getSimilarity(weightsRequete, TFIDF.getTfIdf(fileName, normalizer));
				map.put(ReadXMLFile.listDocuments.get(Integer.parseInt(fileName)), score);
			}
			sortedMapAsc = SortByValue.sortByComparator(map, SortByValue.DESC);
		
		
		return sortedMapAsc;
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(getSimilarity(new
		// File(DIRNAME_Mouhcine+"/../outTfIdf/texte.95-1.poids"),new
		// File(DIRNAME_Mouhcine+"/../outTfIdf/texte.95-1.poids")));
		//Map<String, Double> map = getSimilarDocuments(new File(DIRNAME + "/../outTfIdf/texte.95-1.poids"), new File(DIRNAME + "/../outTfIdf"));
		//SortByValue.printMap(map);
	}

}

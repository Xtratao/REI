import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class Similarite {

	/**
	 * Le répertoire du corpus
	 */
	protected static String DIRNAME = "lemonde-utf8";

	/**
	 * Le fichier contenant les mots vides
	 */
	private static String STOPWORDS_FILENAME = "./frenchST.txt";

	// private String fileName;
	// private double score;
	//
	// public Similarite(String n, int s) {
	// this.fileName = n;
	// this.score = s;
	// }
	//
	// public String getName() {
	// return fileName;
	// }
	//
	// public void setName(String name) {
	// this.fileName = name;
	// }
	//
	// public double getSalary() {
	// return score;
	// }
	//
	// public void setSalary(int salary) {
	// this.score = score;
	// }
	//
	// public String toString() {
	// return "Name: " + this.fileName + "-- Salary: " + this.score;
	// }

	/**
	 * renvoi le score de similarité entre les deux documents en utilisant la
	 * formule de cos les deux documents contiennent les poids (tfIdf) de deux
	 * documents du corpus
	 * 
	 * @param: file1, file2: files .poids stocking the words of a document and
	 *         their tfidf weights
	 * @return: the cosinus similarity of the two files
	 */

	public static double getSimilarity(File file1, File file2)
			throws IOException {

		BufferedReader f1 = new BufferedReader(new FileReader(file1));
		BufferedReader f2 = new BufferedReader(new FileReader(file2));

		HashMap<String, Double> weightsFile1 = new HashMap<String, Double>();
		HashMap<String, Double> weightsFile2 = new HashMap<String, Double>();
		String line = "";

		// On initiliase les variables qui composent la formule du cosinus

		Double produitScalaire = 0.0; // nominateur
		Double normePoids1 = 0.0, normePoids2 = 0.0; // dénominateur

		while ((line = f1.readLine()) != null)
			weightsFile1.put(line.split("\t")[0],
					Double.parseDouble(line.split("\t")[1]));

		while ((line = f2.readLine()) != null)
			weightsFile2.put(line.split("\t")[0],
					Double.parseDouble(line.split("\t")[1]));

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

	public static Map<String, Double> getSimilarDocuments(File file,File dir) throws IOException {

        Map<String, Double> map = new HashMap<String, Double>();
        Map<String, Double> sortedMapAsc = new HashMap<String, Double>();
		if (dir.isDirectory()) {
			String[] fileNames = dir.list();

			System.out.print("Scores du similarité entre le fichier "+ file.getName() + " et l'ensemble");
			System.out.println(" des fichiers du répertoire: " + dir.getName());
			System.out.println();
			double score = 0;
			for (String fileName : fileNames) {
				score = getSimilarity(file, new File(dir.getAbsolutePath()+ "/" + fileName));
				map.put(fileName, score);
//				System.out.println(fileName+ "\t"+ getSimilarity(file, new File(dir.getAbsolutePath()+ "/" + fileName)));
			}
			sortedMapAsc = SortByValue.sortByComparator(map, SortByValue.DESC);
		}
		
		return sortedMapAsc;
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(getSimilarity(new
		// File(DIRNAME_Mouhcine+"/../outTfIdf/texte.95-1.poids"),new
		// File(DIRNAME_Mouhcine+"/../outTfIdf/texte.95-1.poids")));
		Map<String, Double> map = getSimilarDocuments(new File(DIRNAME + "/../outTfIdf/texte.95-1.poids"), new File(DIRNAME + "/../outTfIdf"));
		SortByValue.printMap(map);
	}

}

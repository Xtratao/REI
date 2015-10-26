import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import tools.FrenchStemmer;


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
	 * renvoi le score de similarité entre les deux documents en utilisant la formule de cos
	 * les deux documents contiennent les poids (tfIdf) de deux documents du corpus
	 * @param: file1, file2: files .poids stocking the words of a document and their tfidf weights
	 * @return: the cosinus similarity of the two files 
	 */
	
	public static double getSimilarity(File file1, File file2) throws IOException
	{	

		BufferedReader f1 = new BufferedReader(new FileReader(file1));
		BufferedReader f2 = new BufferedReader(new FileReader(file2));
		
		HashMap<String, Double> weightsFile1 = new HashMap<String, Double>();
		HashMap<String, Double> weightsFile2 = new HashMap<String, Double>();
		String line="";
		
		// On initiliase les variables qui composent la formule du cosinus
		
		Double produitScalaire = 0.0; // nominateur
		Double normePoids1 = 0.0, normePoids2 = 0.0; //dénominateur
		
		while((line=f1.readLine())!=null)
			weightsFile1.put(line.split("\t")[0], Double.parseDouble(line.split("\t")[1]));
		
		while((line=f2.readLine())!=null)
			weightsFile2.put(line.split("\t")[0], Double.parseDouble(line.split("\t")[1]));
		
		for (Map.Entry<String, Double> w1 : weightsFile1.entrySet())
		{
			if(weightsFile2.containsKey(w1.getKey()))
			{
				produitScalaire += w1.getValue()*weightsFile2.get(w1.getKey());
			}
		}
		
				
		for (Map.Entry<String, Double> w1 : weightsFile1.entrySet())
			normePoids1 += Math.pow(w1.getValue(), 2);
			
		
		for (Map.Entry<String, Double> w2 : weightsFile2.entrySet())
			normePoids2 += Math.pow(w2.getValue(), 2);
		

		normePoids1 = Math.sqrt(normePoids1);
		normePoids2 = Math.sqrt(normePoids2);

		return produitScalaire/(normePoids1*normePoids2);
	}
	
	/**
	 * renvoi le score de similarité entre un document et l’ensemble des documents du corpus
	 * @param: file : file .poids stocking the words of a document and its tfidf weights
	 * @param dir : the directory of all the .poids files of the corpus
	 * @throws IOException 
	 */
	
	public static TreeSet<String> getSimilarDocuments(File file, File dir) throws IOException
	{
		
		if (dir.isDirectory()) {
			String[] fileNames = dir.list();
			
			System.out.print("Scores du similarité entre le fichier "+file.getName() +" et l'ensemble");
			System.out.println(" des fichiers du répertoire: "+dir.getName());
			System.out.println();
		
			for (String fileName : fileNames) 
				{
					
					System.out.println(fileName +"\t"+ getSimilarity(file, new File(dir.getAbsolutePath()+"/"+fileName)));
				}
			
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException 
	{
		//System.out.println(getSimilarity(new File(DIRNAME_Mouhcine+"/../outTfIdf/texte.95-1.poids"),new File(DIRNAME_Mouhcine+"/../outTfIdf/texte.95-1.poids")));
		getSimilarDocuments(new File(DIRNAME+"/../outTfIdf/texte.95-1.poids"),new File(DIRNAME+"/../outTfIdf"));
	}
	
}

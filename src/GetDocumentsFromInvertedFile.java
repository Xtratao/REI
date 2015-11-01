import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import tools.FrenchStemmer;
import tools.Normalizer;

public class GetDocumentsFromInvertedFile {

	public static TreeMap<String, TreeSet<String>> invertedFile;
	
	// This function loops the buffer where is the content of the inverseFile
	// then add each (word,list of documents) to the ArrayList : invertedFile
	
	
	public static void loadInvertedFile() throws IOException
	{
		// Initialization of the class attribute invertedFile
		invertedFile = new TreeMap<String, TreeSet<String>>();
		
		// We create a buffer that contains the inverse file
		BufferedReader buffer = new BufferedReader(new FileReader("fichierInverseData2015.txt"));
		
		// We intialize the variables that we gonna use in the loop
		String line = "";
		TreeSet<String> documents=null;
		String[] documentsTab;
		
		// we loop the buffer
		while ((line = buffer.readLine()) != null)
		{	
			// we intialize a new TreeSer of strings that will save the documents' name
			documents = new TreeSet<String>();
			
			// we put the strings array of the documents
			documentsTab = line.split("\t")[2].split(",");
			
			// we loop the documents names. For each document's names we remove the blanks and we add it
			// to the TreeSer documents
			for(String dct : documentsTab)
				documents.add(dct.trim());
			
			// Then we add the word and the TreeSet of documents to the TreeMap invertedFile
			invertedFile.put(line.split("\t")[0], documents);
		}
		
	}
	
	// The function below computes the intersection of 2 TreeSets of documents.  
	
	public static TreeSet<String> fusion(TreeSet<String> tree1, TreeSet<String> tree2) {

		// initialize the treeSet that we gonna return after fusion
		TreeSet<String> tree = new TreeSet<String>();


		// initialize the first ids for each treeSet
		if(tree1.isEmpty())
			tree1=tree2;
		String id1 = tree1.first();
		String id2 = tree2.first();

		while (!tree1.isEmpty() && !tree2.isEmpty()) {// while both lists are not Empty

			if (id1.equals(id2)) {
				tree.add(id1);
				
				tree1.remove(id1);
				
				tree2.remove(id2);
				if(!tree1.isEmpty()){
					id1 = tree1.first();
				}
				if(!tree2.isEmpty()){
					id2 = tree2.first();
				}

			}else{
				if(id2.compareTo(id1)>0){
					tree1.remove(id1); // remove it from the first set

					
					if(!tree1.isEmpty()){
						id1 = tree1.first(); // reset the id value
					}
					
				}else{
					tree2.remove(id2); // remove it from the first set
					if(!tree2.isEmpty())
						id2 = tree2.first(); // reset the id value
				}
				
					
			}
			
		}

		return tree;
	}
	
	// The following function computes the intersection of documents list corresponding to the words
	// of the request in the function parameters.
	
	public static TreeSet<String> fusion(File requete, Normalizer normalizer) throws IOException
	{
		// We extract the words from the request but normalized with the same normalisation used to
		// compute the inverse file
		ArrayList<String> motsRequete = normalizer.normalize(requete);
		
		// We stock in a hashmap the word as a key and the size of the documents list of that word (existing
		// in the inverse file) as a key
		// This will allow us to sort easily this hashmap by value (which is the size of the documents list)
		
		// We declare the hashmap
        Map<String, Integer> map = new HashMap<String, Integer>();
        
        // we fill the hashmap
        for(String mot : motsRequete)
        	if(invertedFile.containsKey(mot))
        	map.put(mot, invertedFile.get(mot).size());
        
        // We sort the hashmap in an ascending way
        map = SortByValue.sortByComparatorInteger(map, SortByValue.ASC);

        // We declare a TreeSet that will stock the intersection of the documents
		TreeSet<String> tree = new TreeSet<String>();
				
		// in the loop below we compute the intersection of all the words of the request using the
		// function fusion(TreeSet<String>,TreeSet<String>)
		for (Map.Entry<String, Integer> entry : map.entrySet()) 
		{
			tree = fusion(tree,invertedFile.get(entry.getKey()));
		}
		
		return tree;
	}
	
	
	public static void main(String[] args) throws IOException {

		loadInvertedFile();
		TreeSet<String> tree = new TreeSet<String>();
		tree = fusion(new File("requete.txt"),new FrenchStemmer());
		System.out.println("Tree : "+tree);
	}

}

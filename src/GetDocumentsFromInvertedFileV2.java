
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import tools.FrenchStemmer;
import tools.Normalizer;

public class GetDocumentsFromInvertedFileV2 {

	public static TreeMap<String, TreeSet<String>> invertedFile;

	// This function loops the buffer where is the content of the inverseFile
	// then add each (word,list of documents) to the ArrayList : invertedFile

	public static void loadInvertedFile(File requete, Normalizer normalizer) throws IOException {
		// Initialization of the class attribute invertedFile
		invertedFile = new TreeMap<String, TreeSet<String>>();
		
		ArrayList<String> motsRequete = normalizer.normalize(requete);
		
		// We create a buffer that contains the inverse file
		BufferedReader buffer = new BufferedReader(new FileReader(
				"fichierInverseData2015.txt"));

		// We intialize the variables that we gonna use in the loop
		String line = "";
		TreeSet<String> documents = null;
		String[] documentsTab;

		// we loop the buffer
		while ((line = buffer.readLine()) != null) {
			
			if(motsRequete.contains(line.split("\t")[0]))
			{
				// we intialize a new TreeSer of strings that will save the
				// documents' name
				documents = new TreeSet<String>();
	
				// we put the strings array of the documents
				documentsTab = line.split("\t")[2].split(",");
	
				// we loop the documents names. For each document's names we remove
				// the blanks and we add it
				// to the TreeSer documents
				for (String dct : documentsTab)
					documents.add(dct.trim());
	
				// Then we add the word and the TreeSet of documents to the TreeMap
				// invertedFile
				invertedFile.put(line.split("\t")[0], documents);
			}
		}

	}

	// The function below computes the intersection of 2 TreeSets of documents.

	public static TreeSet<String> fusion(TreeSet<String> tree1,TreeSet<String> tree2) {

		// initialize the treeSet that we gonna return after fusion
		TreeSet<String> tree = new TreeSet<String>();

		// initialize the first ids for each treeSet
		if (tree1.isEmpty())
			tree1 = tree2;
		String id1 = tree1.first();
		String id2 = tree2.first();

		while (!tree1.isEmpty() && !tree2.isEmpty()) {// while both lists are
														// not Empty
			if (id1.equals(id2)) {
				tree.add(id1);

				tree1.remove(id1);

				tree2.remove(id2);
				if (!tree1.isEmpty()) {
					id1 = tree1.first();
				}
				if (!tree2.isEmpty()) {
					id2 = tree2.first();
				}

			} else {
				if (id2.compareTo(id1) > 0) {
					tree1.remove(id1); // remove it from the first set

					if (!tree1.isEmpty()) {
						id1 = tree1.first(); // reset the id value
					}

				} else {
					tree2.remove(id2); // remove it from the first set
					if (!tree2.isEmpty())
						id2 = tree2.first(); // reset the id value
				}

			}

		}

		return tree;
	}
	
	public static ArrayList<TreeSet<String>> getListOfDocumentsLists(File requete, Normalizer normalizer) throws IOException
	{
		ArrayList<TreeSet<String>> listOfDocumentsLists = new ArrayList<TreeSet<String>>();
		ArrayList<String> motsRequete = normalizer.normalize(requete);
		Map<String, Integer> map = new HashMap<String, Integer>();
		// we fill the hashmap
		for (String mot : motsRequete)
			if (invertedFile.containsKey(mot))
				map.put(mot, invertedFile.get(mot).size());

		// We sort the hashmap in an ascending way
		map = SortByValue.sortByComparatorInteger(map, SortByValue.ASC);

		// We declare a TreeSet that will stock the intersection of the
		// documents
		TreeSet<String> tree = new TreeSet<String>();

		// in the loop below we compute the intersection of all the words of the
		// request using the
		// function fusion(TreeSet<String>,TreeSet<String>)
		for (Map.Entry<String, Integer> entry : map.entrySet()) 
			listOfDocumentsLists.add(invertedFile.get(entry.getKey()));
		
		return listOfDocumentsLists;
		
	}

	// The following function computes the intersection of documents list
	// corresponding to the words
	// of the request in the function parameters.

	public static TreeSet<String> fusion(File requete, Normalizer normalizer)
			throws IOException {
		// We extract the words from the request but normalized with the same
		// normalisation used to
		// compute the inverse file
		ArrayList<String> motsRequete = normalizer.normalize(requete);

		// We stock in a hashmap the word as a key and the size of the documents
		// list of that word (existing
		// in the inverse file) as a key
		// This will allow us to sort easily this hashmap by value (which is the
		// size of the documents list)

		// We declare the hashmap
		Map<String, Integer> map = new HashMap<String, Integer>();

		// we fill the hashmap
		for (String mot : motsRequete)
			if (invertedFile.containsKey(mot))
				map.put(mot, invertedFile.get(mot).size());

		// We sort the hashmap in an ascending way
		map = SortByValue.sortByComparatorInteger(map, SortByValue.ASC);

		// We declare a TreeSet that will stock the intersection of the
		// documents
		TreeSet<String> tree = new TreeSet<String>();

		// in the loop below we compute the intersection of all the words of the
		// request using the
		// function fusion(TreeSet<String>,TreeSet<String>)
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			tree = fusion(tree, invertedFile.get(entry.getKey()));
		}

		return tree;
	}

	public static TreeSet<String> inter(ArrayList<TreeSet<String>> listOfTreeSets) {
		
		TreeSet<String> tree = new TreeSet<String>();
		
		Map<Integer, String> tmp = new HashMap<Integer, String>();

		int i = 0;
		for (TreeSet<String> treeSet : listOfTreeSets)
			if(!treeSet.isEmpty())
				tmp.put(i++, treeSet.first());
		
		//System.out.println("tmp : "+tmp);
		Map<Integer, String> sortedMap = new HashMap<Integer, String>();
		
		Map.Entry<Integer, String> entry = tmp.entrySet().iterator().next();
		
		int lenMap = 0;
		String[] values = null;
		int j =0;
		boolean bool = true;
		while (bool) {// while lists are not Empty			
		
			sortedMap = SortByValue.sortByComparatorString(tmp, SortByValue.DESC);
			lenMap = sortedMap.size();
			values = sortedMap.values().toArray(new String[lenMap]);
			//System.out.println(" while ("+ j++ +") -> tmp : "+tmp);
			if(values[0].equals(values[lenMap-1])){
				//System.out.println(" while ("+ j++ +") if(equal) -> tmp : "+tmp);
				tree.add(values[0]);
				//System.out.println("tree after adding "+ values[0] +" :  "+tree);
				i = 0;
				tmp.clear();
				for (TreeSet<String> treeSet : listOfTreeSets) {
					
					treeSet.remove(treeSet.first());
					
					if(!treeSet.isEmpty()){
						tmp.put(i++, treeSet.first());
					}else{
						bool = false;
						break;
					}	
				}
				
			}else{
				//System.out.println(" while ("+ j++ +") if(not equal) -> tmp : "+tmp);
//				sortedMapAsc = SortByValue.sortByComparatorString(tmp, SortByValue.DESC);
//				values = SortByValue.sortByComparatorString(tmp, SortByValue.DESC).values().toArray(new String[tmp.size()]);

				i = 0;
				for (TreeSet<String> treeSet : listOfTreeSets) {
					if(!treeSet.first().equals(values[0])){
						treeSet.remove(treeSet.first());
						tmp.remove(i);
						if(!treeSet.isEmpty()){
							tmp.put(i, treeSet.first());
						}else{
							bool = false;
							break;
						}	
					}
					i++;
				}			
			}
		}
		// tmp.headSet(toElement)
//		TreeSet<String> tree1 = listOfTreeSets.get(0);
//
//		for (TreeSet<String> treeSet : listOfTreeSets) {
//			tree.addAll(treeSet);
//		}
//
//		tree.retainAll(tree1);
		return tree;
	}

	public static void main(String[] args) throws IOException {

//		// loadInvertedFile();
		
		TreeSet<String> tree = new TreeSet<String>();
		TreeSet<String> tree1 = new TreeSet<String>();
		TreeSet<String> tree2 = new TreeSet<String>();
		TreeSet<String> tree3 = new TreeSet<String>();
		TreeSet<String> tree4 = new TreeSet<String>();


		tree1.add("a");	tree1.add("a");	tree1.add("b");	tree1.add("c");	tree1.add("d");	tree1.add("f");
		tree2.add("b");	tree2.add("c");	tree2.add("d");tree2.add("f");
		tree3.add("b");tree3.add("c");tree3.add("d");tree3.add("e");tree3.add("f");tree3.add("g");
		tree4.add("b");//tree4.add("c");tree4.add("d");tree4.add("e");tree4.add("f");tree4.add("g");

		ArrayList<TreeSet<String>> list = new ArrayList<TreeSet<String>>();
		list.add(tree1);
		list.add(tree2);
		list.add(tree3);
		list.add(tree4);



//		Runtime runtime = Runtime.getRuntime();
//
//		NumberFormat format = NumberFormat.getInstance();
//
//		StringBuilder sb = new StringBuilder();
//		long maxMemory = runtime.maxMemory();
//		long allocatedMemory = runtime.totalMemory();
//		long freeMemory = runtime.freeMemory();
//
//		sb.append("free memory: " + format.format(freeMemory / 1048576)
//				+ "<br/>\n");
//		sb.append("allocated memory: "
//				+ format.format(allocatedMemory / 1048576) + "<br/>\n");
//		sb.append("max memory: " + format.format(maxMemory / 1048576)
//				+ "<br/>\n");
//		sb.append("total free memory: "
//				+ format.format((freeMemory + (maxMemory - allocatedMemory)) / 1048576)
//				+ "<br/>\n");
//
//		System.out.println(sb);

		int i = 0;
		Map<Integer, String> tmp = new HashMap<Integer, String>();
		
		for (TreeSet<String> treeSet : list)
			if(!treeSet.isEmpty())
				tmp.put(i++, treeSet.first());


		for (TreeSet<String> treeSet : list) {
			System.out.println(treeSet);
		}
		
		tree = inter(list);
		System.out.println("res : "+tree);
	}
}

package guesser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import embedding.word.sphinx.util.WordEmbeddingSphinxAdapter;

public class WordWriter {

	public static void main(String[] args) {
		BufferedWriter writer = null;
		try{
			System.out.println("Starting...");
			File logFile = new File("similar_words_pais.txt");
			System.out.println(logFile.getCanonicalPath());
			
			writer = new BufferedWriter(new FileWriter(logFile));
			
			// rey - hombre = reina - mujer => rey == X
			//String[] words = new String[]{"hombre", "reina", "mujer"};
			String[] words = new String[]{"santiago", "china", "shangai"};
			
			for(String w : words) {
				List<String> wordRep = WordEmbeddingSphinxAdapter.getWordsVectorList(w.split(" "));
			    writer.write(wordRep.get(0));
			    writer.newLine();
			}
			
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
		System.out.println("...Finished");
	}

}

import java.io.*;
import java.net.*;
import java.util.*; 
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class WordFrequency{

    private static boolean ASC = true;
    private static boolean DESC = false;
		
	public static void main(String[] args){
		boolean repeat = true;
		while(repeat){
			String input_path = getBookPath();
			if(input_path.equalsIgnoreCase("q")){
				repeat = false;
			}
			else{
				analyseBook(input_path);
			}
		}
	}

	private static String getBookPath(){
		Scanner input  = new Scanner(System.in);
		System.out.println("Please enter the path to the input file. (q to quit)");
		String  input_path = input.next();
		return input_path;
	}
		
	private static void	analyseBook(String input_path){
	
		String start_time = ""+LocalDateTime.now();
		
		String highest_occurrence_key = "";
		int highest_occurrence_value = 0;
		
		String highest_occurrence_7char_key = "";
		int highest_occurrence_7char_value = 0;		

		String highest_scoring_key = "";
		int highest_scoring_value = 0;

		Map<Character, Integer> scrabble_letter_scores = new HashMap<>();
		scrabble_letter_scores.put('a', 1);
			scrabble_letter_scores.put('á', 1);
		scrabble_letter_scores.put('b', 3);
		scrabble_letter_scores.put('c', 3);
		scrabble_letter_scores.put('d', 2);
		scrabble_letter_scores.put('e', 1);
			scrabble_letter_scores.put('é', 1);
		scrabble_letter_scores.put('f', 4);
		scrabble_letter_scores.put('g', 2);
		scrabble_letter_scores.put('h', 4);
		scrabble_letter_scores.put('i', 1);
			scrabble_letter_scores.put('í', 1);
		scrabble_letter_scores.put('j', 8);
		scrabble_letter_scores.put('k', 5);
		scrabble_letter_scores.put('l', 1);
		scrabble_letter_scores.put('m', 3);
		scrabble_letter_scores.put('n', 1);
		scrabble_letter_scores.put('o', 1);
			scrabble_letter_scores.put('ó', 1);
			scrabble_letter_scores.put('ö', 1);
		scrabble_letter_scores.put('p', 3);
		scrabble_letter_scores.put('q', 10);
		scrabble_letter_scores.put('r', 1);
		scrabble_letter_scores.put('s', 1);
		scrabble_letter_scores.put('t', 1);
		scrabble_letter_scores.put('u', 1);
			scrabble_letter_scores.put('ú', 1);
		scrabble_letter_scores.put('v', 4);
		scrabble_letter_scores.put('w', 4);
		scrabble_letter_scores.put('x', 8);
		scrabble_letter_scores.put('y', 4);
		scrabble_letter_scores.put('z', 10);
						
		File file = new File(input_path);

		try{
		
			BufferedReader br = new BufferedReader(new FileReader(file)); 

			String[] line;
			
			Map<String, Integer> words = new HashMap<>();

			String st;
		
			while ((st = br.readLine()) != null){

				String clean_st = st.replaceAll("[.!?“”();]", "").toLowerCase();

				//process the words a line at a time
				line = clean_st.split(" ");
							
				for (int i=0; i < line.length; i++){
				
					int increment = 1;
					if(words.get(line[i])!=null){
						increment = words.get(line[i])+1;
					}
	
					//calc most freq word
					if(increment > highest_occurrence_value){					
						highest_occurrence_key = line[i];
						highest_occurrence_value = increment;
					}
						
					//calc most freq 7 char word
					if(line[i].length() == 7 && increment > highest_occurrence_7char_value){					
						highest_occurrence_7char_key = line[i];
						highest_occurrence_7char_value = increment;
					}
						
					//calc highest scoring word(s);
					int score = calcScrabbleScore(line[i], scrabble_letter_scores);				
					if(score > highest_scoring_value){					
						highest_scoring_key = line[i];
						highest_scoring_value = score;
					}					
							
					//add to the words array where word is key and occurrence increment is value:
					String word = line[i];
					words.put(word, increment);
								
				}

			}
		
			//OPTIONAL TO REQUIREMENTS OF ASSIGNMENT: sort the 'words' HashMap by value	:
			System.out.println("After sorting descending order......");
			Map<String, Integer> sortedMap = sortByValue(words, DESC);
        
			//Output:
			sortedMap.forEach((key, value) -> System.out.println("Key : " + key + " Value : " + value));
			
			System.out.println("Started at: " + start_time);
			System.out.println("Finished at: " + LocalDateTime.now());
			System.out.println("Runtime Memory: " + Runtime.getRuntime().totalMemory());
	
			System.out.println("Most frequent word: " + highest_occurrence_key + " occurred " + highest_occurrence_value + " times");
			System.out.println("Most frequent 7-character word: " + highest_occurrence_7char_key + " occurred " + highest_occurrence_7char_value + " times");
			System.out.println("Highest scoring word(s) (according to Scrabble): " + highest_scoring_key + " with a score of " + highest_scoring_value);

		}
		catch(FileNotFoundException fnfE){
			System.out.println("File not found: " + input_path );
		}		
		catch(IOException fnfE){
			System.out.println("Could not read file: " + input_path );
		}
			
	}
	
	private static int calcScrabbleScore(String word, Map<Character, Integer> scrabble_letter_scores){
		int score = 0;
		for (int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(scrabble_letter_scores.get(c) != null){        
				score += scrabble_letter_scores.get(c);
			}
		}
		return score;
	}
	
	//Sorting HashMap by values:
	//https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values/13913206#13913206
    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order)
    {
        List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
    	
} 

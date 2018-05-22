package com.wordladder.demo;
 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.io.*;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory; 

@RestController
public class wordladderController {
 
    @RequestMapping(value = "/wordladder")
    public static String main(@RequestParam("dictname") String dict_name, @RequestParam("word1") String word1,  @RequestParam("word2") String word2) {
    	
    	Logger logger=LoggerFactory.getLogger(wordladderController.class);  

		//save dictionary
		Set<String> dict = new HashSet<String>();
		//input the dictionary
		if (readDict(dict, dict_name) != "Open success") {
			logger.debug("Unable to open that file.input file name:"+ dict_name);  
			return "Unable to open that file.";
		}
		
		String[] words = new String[2];
		words[0] = word1;
		words[1] = word2;
		
		//while (true) {
			//input two words
			//readWord(words, dict);
			//check word valid
			if (!wordIsvalid(words)) {
				logger.debug("Input words is not valid.input words: "+ word1+"、 "+ word2);  
				return "Input words is not valid";
			}
			
			Queue<Stack<String>> ladder = new LinkedList<Stack<String>>();
			Stack<String> tmp_Stack = new Stack<>();
			
			tmp_Stack.push(words[0]);
			ladder.offer(tmp_Stack);
			
			//BFS
			String result = BFS(ladder, words, dict);
			logger.debug("wordladder run:"+result);
			return result;
		//}
	}


	public static String BFS(Queue<Stack<String>> ladder, String[] words, Set<String> dict) {
		String word1 = words[0];
		String word2 = words[1];
		String result;
		//to save used word
		Set<String> used_dict = new HashSet<>();
		used_dict.add(word1);
		
		Stack<String> loopStack = new Stack<>();
		while (!ladder.isEmpty()) {
			//clear temporary stack
			while (!loopStack.isEmpty()) 
				loopStack.clear();
			//Retrieves and removes the head of this queue
			loopStack = ladder.poll();
			//BFS
			String top = loopStack.peek();
			for (int i = 0; i < top.length(); i++) {
				for (char c = 'a'; c <='z'; c++) {
					// if the replace char is the same as origin top, pass
					if (top.charAt(i) == c)
						continue;
					// replace i index to c 
					String tmp_word = top.substring(0,i) + c + top.substring(i+1);
					// the word after replace is used
					if (used_dict.contains(tmp_word))
						continue;
					// the word after replace is the same as word1
					if (tmp_word.equals(word2)) {
						result = "A ladder from "+word2+" back to "+word1+": "+word2+" ";
						while (!loopStack.isEmpty()) {
							result += loopStack.peek() + " ";
							loopStack.pop();
						}
						return result;
					}
					// the word after replace exists in dict
					if (dict.contains(tmp_word)) {
						loopStack.push(tmp_word);
						Stack<String> tmpS = (Stack<String>) loopStack.clone();
						ladder.offer(tmpS);
						//recover tmpS
						loopStack.pop();
						//add the word to used, or infinite loop
						used_dict.add(tmp_word);
					}
				}
			}
		}
		// not found until queue empty
		//System.out.printf("No word ladder found from %s back to %s.\n",word2,word1);
		return "No word ladder found from "+word2+" back to "+word1+".";
	}

	public static String readDict(Set<String> dict, String dict_name) {
		// a set contain all valid name of dictionary
		Set<String> dicts = new HashSet<String>() {{
			add("dictionary.txt");
			add("EnglishWords.txt");
			add("smalldict1.txt");
			add("smalldict2.txt");
			add("smalldict3.txt");
		}};
		//choose dictionary
		//Scanner in = new Scanner(System.in);
		//System.out.print("Dictionary file name?");
		//String dict_name = in.nextLine();
		while (!dicts.contains(dict_name)) {
			return "Unable to open that file.";
			//dict_name = in.nextLine();
		}
		//in.close();
		
		
		// file path in server is different ????????
		//File file = new File("src\\main\\resources\\static\\" +dict_name);
		
		// server end path should like this
		//String path = wordladderController.class.getClass().getClassLoader().getResourceAsStream("/static/"+dict_name);
		
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//System.out.println(path);
		
		File file = new File(path+"/static/"+dict_name);
		//String path = wordladderController.class.getClass().getClassLoader().getResource("static/"+dict_name).getPath();
		//System.out.println(path);
		//File file = new File(path);
		//BufferedReader reader = new BufferedReader(new FileReader(file));
		
		//InputStream in = wordladderController.class.getClassLoader().getResourceAsStream("static/"+dict_name);
		//BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			//read by line
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String nextLine = null;
			
			while ((nextLine = reader.readLine()) != null) {
                dict.add(nextLine);
            }
			reader.close();
		} catch (IOException e) {
            e.printStackTrace();
        } 
		
		return "Open success";
	}

	public static void readWord(String[] words, Set<String> dict) {
		//read word1 and word2, check \n
		Scanner in = new Scanner(System.in);
		System.out.print("Word #1 (or Enter to quit):");
		String word1 = new String(in.nextLine());
		
		if (word1.length() == 0) {
			//if \n, exit
			System.out.println("Have a nice day.");
			System.exit(0);
		}
		while (!dict.contains(word1)) {
			// if not found 
			System.out.print("Can not find word #1 in given dictionary.\nWord #1 (or Enter to quit):");
			word1 = in.nextLine();
			if (word1.length() == 0) {
				//if \n, exit
				System.out.println("Have a nice day.");
				System.exit(0);
			}
		}
		words[0] = word1;
		
		System.out.print("Word #2 (or Enter to quit):");
		String word2 = new String(in.nextLine());
		
		if (word2.length() == 0) {
			//if \n, exit
			System.out.println("Have a nice day.");
			System.exit(0);
		}
		while (!dict.contains(word1)) {
			//if not found
			System.out.print("Can not find word #1 in given dictionary.\nWord #1 (or Enter to quit):");
			word1 = in.nextLine();
			if (word2.length() == 0) {
				//if \n, exit
				System.out.println("Have a nice day.");
				System.exit(0);
			}
		}
		words[1] = word2;
	}
	
	public static boolean wordIsvalid(String[] words) {
		//check words length is not same
		if (words[0].length() != words[1].length()) {
			System.out.print("The two words must be the same length.\n");
			return false;
		}
		//check words is not same
		if (words[0].equals(words[1])) {
			System.out.print("The two words must be different.\n");
			return false;
		}
		return true;
	}
	
}
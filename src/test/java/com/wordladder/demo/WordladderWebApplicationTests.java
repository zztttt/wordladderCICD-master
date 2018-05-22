package com.wordladder.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordladderWebApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void test_JUnit() {
		assertEquals(2, 1 + 1);
	}
	
	@Test
	public void SuccessReadDict() {
		Set<String> dict = new HashSet<>();
		
		//PrintStream console = null;
		//console = System.out;
		//ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		//System.setOut(new PrintStream(bytes));
		
		//System.setIn(new ByteArrayInputStream("smalldict1.txt".getBytes()));
		wordladderController.readDict(dict,"smalldict1.txt");
		assertEquals(dict.size(), 91);
		assertFalse(!dict.contains("bee"));
	}
	
	@Test
	public void FailReadDict() {
		Set<String> dict = new HashSet<>();
		//ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		//System.setOut(new PrintStream(bytes));
		
		//System.setIn(new ByteArrayInputStream("wrongname.txt\nsmalldict1.txt".getBytes()));;
		String result = wordladderController.readDict(dict,"wrongname.txt");
		assertEquals("Unable to open that file.", result);
	}
	
	@Test
	public void ReadWord() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bytes));
		//read dict first
		Set<String> dict = new HashSet<>();
		System.setIn(new ByteArrayInputStream("smalldict1.txt".getBytes()));;
		wordladderController.readDict(dict,"smalldict1.txt");
		//read word
		String[] words = new String[2];
		System.setIn(new ByteArrayInputStream("bee\nbog".getBytes()));;
		wordladderController.readWord(words, dict);
		assertEquals("bee",words[0]);
		assertEquals("bog",words[1]);
	}
	
	@Test
	public void WordIsvalid1() {
		String[] valid = new String[2];
		valid[0] = "bee";
		valid[1] = "bog";
		assertFalse(!wordladderController.wordIsvalid(valid));
	}
	
	@Test
	public void WordIsvalid2() {
		String[] valid = new String[2];
		valid[0] = "bee";
		valid[1] = "boge";
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bytes));
		// not valid
		assertFalse(wordladderController.wordIsvalid(valid));
		// output 
		assertEquals("The two words must be the same length.\n", bytes.toString());
	}
	
	@Test
	public void WordIsvalid3() {
		String[] valid = new String[2];
		valid[0] = "bee";
		valid[1] = "bee";
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bytes));
		// not valid
		assertFalse(wordladderController.wordIsvalid(valid));
		// output 
		assertEquals("The two words must be different.\n", bytes.toString());
	}
	
	@Test
	public void successBFS() {
		//prepare for BFS
		Set<String> dict = new HashSet<>();
		//System.setIn(new ByteArrayInputStream("smalldict1.txt".getBytes()));;
		wordladderController.readDict(dict,"smalldict1.txt");
		
		String[] words = new String[2];
		words[0] = "bee";
		words[1] = "bog";
		
		Queue<Stack<String>> ladder = new LinkedList<Stack<String>>();
		Stack<String> tmp_Stack = new Stack<>();
		
		tmp_Stack.push(words[0]);
		ladder.offer(tmp_Stack);
		
		//ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		//System.setOut(new PrintStream(bytes));
		
		String result = wordladderController.BFS(ladder, words, dict);
		
		assertEquals("A ladder from bog back to bee: bog beg bee ",result);
		
	}
	
	@Test
	public void failBFS() {
		//prepare for BFS
		Set<String> dict = new HashSet<>();
		//System.setIn(new ByteArrayInputStream("smalldict1.txt".getBytes()));;
		wordladderController.readDict(dict,"smalldict1.txt");
		
		String[] words = new String[2];
		words[0] = "bush";
		words[1] = "code";
		
		Queue<Stack<String>> ladder = new LinkedList<Stack<String>>();
		Stack<String> tmp_Stack = new Stack<>();
		
		tmp_Stack.push(words[0]);
		ladder.offer(tmp_Stack);
		
		//ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		//System.setOut(new PrintStream(bytes));
		
		String result = wordladderController.BFS(ladder, words, dict);
		
		assertEquals("No word ladder found from code back to bush.",result);
		
	}
}

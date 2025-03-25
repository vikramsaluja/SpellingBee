import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Vikram Saluja
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // Generate Method
    public void generate() {
        // Call recusive method
        generateHelper("", letters);

    }

    // Generate helper method
    public void generateHelper(String word, String remaining){
        // Base case if no charecters are left, then add the word to the list
        if(!word.isEmpty() && !words.contains(word)){
            words.add(word);
        }


        for(int i = 0; i < remaining.length(); i++){
            // create the new word of remaing charecters for recurisve call
            String holder = remaining.substring(0, i)
                    + remaining.substring(i+1);
            // Create the new word for recursive call
            String newWord = word + remaining.substring(i,i+1);

            generateHelper(newWord, holder);
        }
    }

    // Merge Sort method
    public void sort(){
        words = mergeSort(words);
    }

    // Recursive method to break up elements
    public ArrayList<String> mergeSort(ArrayList<String> list) {
        // Base case, if more than one element is arraylist then keep spliting it up
        if(list.size() <= 1){
            return list;
        }
        int mid = list.size() / 2;
        // Create 2 new arrayLists, one for each half
        ArrayList<String> arrLeft = new ArrayList<>();
        ArrayList<String> arrRight = new ArrayList<>();
        // Break up list into halves and add elements to each
        for(int i = 0; i < mid; i++){
            arrLeft.add(list.get(i));
        }
        for(int j = mid; j < list.size(); j++) {
            arrRight.add(list.get(j));
        }
        // Call recurisve method
        return merge(mergeSort(arrLeft), mergeSort(arrRight));
    }


    // Take both of the Arraylists and put them back together
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> merged = new ArrayList<>();
        int index1 = 0;
        int index2 = 0;
        int count = 0;

        // While there are stil elements left, check which value is larger in order to add it back to the ArrayList
        while(index1 < arr1.size() && index2 < arr2.size()){
            if(arr1.get(index1).compareTo(arr2.get(index2)) < 0){
                merged.add(count, arr1.get(index1++));
            }
            else{
                merged.add(count, arr2.get(index2++));
            }
            count++;
        }

        while(index1 < arr1.size()){
            merged.add(count++,arr1.get(index1++));
        }
        while(index2 < arr2.size()){
            merged.add(count++, arr2.get(index2++));
        }
        // Return mergerd arraylist
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }


    public void checkWords() {
        // Check every word in arraylist
        for(int i = 0; i < words.size(); i++){
            // If it is not found in the binary search, remove it from words.
            if(!binarySearch(words.get(i))){
                words.remove(i);
                i--;
            }
        }

    }

    // Binary Search Method
    public boolean binarySearch(String target){
        int start = 0;
        int end = DICTIONARY_SIZE - 1;

        while(start <= end){
            int mid = ((start + end) / 2);

            int comp = DICTIONARY[mid].compareTo(target);
            // Check if the target is found
            if(comp == 0){
                // return true if the target is found
                return true;
            }
            // Check to see if you need to search second half
            else if(comp < 0){
                // Search the second half the array
                start = mid + 1;
            }
            else {
                // if the target isn't found, only search first half of the array
                end = mid - 1;
            }
        }
        // If the target word is not found, return false
        return false;
    }


    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}

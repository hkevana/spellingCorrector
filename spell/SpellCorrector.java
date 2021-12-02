package spell;

import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    private Trie dict;

    public SpellCorrector() {
        this.dict = new Trie();
    }
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File src = new File(dictionaryFileName);
        try (Scanner in = new Scanner(src)) {
            while(in.hasNext()) { this.dict.add(in.next()); }
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        INode retNode = this.dict.find(inputWord);
        if (retNode != null && retNode.getValue() > 0) { return inputWord; }
        // Suggest Similar Word to inputWord
        // Get Edit Distance One Words
        Set<String> firstEdits = getEditOne(inputWord);
        String suggestion = suggest(firstEdits);
        // If Null
        // Get Edit Distance Two Words
        if (suggestion == null) {
            Set<String> secondEdits = getEditTwo(firstEdits);
            suggestion = suggest(secondEdits);
        }
        return suggestion;
    }
    private String suggest(Set<String> edits) {
        SortedSet<String> suggestions = new TreeSet<>();
        int max_freq = 0;

        for (String word : edits) {
            INode retNode = this.dict.find(word);
            if (retNode != null) {
                if (retNode.getValue() > max_freq) {
                    suggestions.clear();
                    suggestions.add(word);
                    max_freq = retNode.getValue();
                }
                if (retNode.getValue() == max_freq) { suggestions.add(word); }
            }
        }
        return suggestions.isEmpty() ? null : suggestions.first();
    }
    private Set<String> getEditTwo(Set<String> firstEdits) {
        Set<String> editTwo = new HashSet<>();
        for (String word : firstEdits) { editTwo.addAll(getEditOne(word)); }
        return editTwo;
    }
    private Set<String> getEditOne(String word) {
        Set<String> editOne = new HashSet<>();
        editOne.addAll(getDeletionEdits(word));
        editOne.addAll(getInsertionEdits(word));
        editOne.addAll(getAlterationEdits(word));
        editOne.addAll(getTranspositionEdits(word));
        return editOne;
    }
    private Set<String> getDeletionEdits(String word) {
        Set<String> edits = new HashSet<>();
        StringBuilder editWord = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            editWord.deleteCharAt(i);
            edits.add(editWord.toString());
            editWord.insert(i, word.charAt(i));
        }
        return edits;
    }
    private Set<String> getInsertionEdits(String word) {
        Set<String> edits = new HashSet<>();
        StringBuilder editWord = new StringBuilder(word);
        for (int i = 0; i < word.length() + 1; i++) {
            for (int j = 0; j < 26; j++) {
                char l = (char)(j + 'a');
                editWord.insert(i, l);
                edits.add(editWord.toString());
                editWord.deleteCharAt(i);
            }
        }
        return edits;
    }
    private Set<String> getAlterationEdits(String word) {
        Set<String> edits = new HashSet<>();
        StringBuilder editWord = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            editWord.deleteCharAt(i);
            for (int j = 0; j < 26; j++) {
                char l = (char)(j + 'a');
                editWord.insert(i, l);
                edits.add(editWord.toString());
                editWord.deleteCharAt(i);
            }
            editWord.insert(i, word.charAt(i));
        }
        return edits;
    }
    private Set<String> getTranspositionEdits(String word) {
        Set<String> edits = new HashSet<>();
        StringBuilder editWord = new StringBuilder(word);
        for (int i = 0; i < word.length() - 1; i++) {
            editWord.deleteCharAt(i);
            editWord.insert(i + 1, word.charAt(i));
            edits.add(editWord.toString());
            editWord.deleteCharAt(i + 1);
            editWord.insert(i, word.charAt(i));
        }
        return edits;
    }
}

package com.example.husseinjehadalhroub.englishwords.DataTypes;

public class WordMeaning {
    private String wordMeaning;
    private String wordDefinition;
    private String wordId;

    public WordMeaning(String wordMeaning, String wordDefinition, String wordId) {
        this.wordMeaning = wordMeaning;
        this.wordDefinition = wordDefinition;
        this.wordId = wordId;

    }

    public String getWordMeaning() {
        return wordMeaning;
    }

    public String getWordDefinition() {
        return wordDefinition;
    }


    public String getWordId() {
        return wordId;
    }
}

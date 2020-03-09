package com.studentproject.dictionary;

public class translate {
    private String word;
    private String content;

    public translate(){}

    public translate(String word, String content) {
        this.word = word;
        this.content = content;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

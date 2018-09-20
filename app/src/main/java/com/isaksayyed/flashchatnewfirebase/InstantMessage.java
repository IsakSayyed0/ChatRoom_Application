package com.isaksayyed.flashchatnewfirebase;

public class InstantMessage {

     String message, author;

     public InstantMessage(){

     }

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}

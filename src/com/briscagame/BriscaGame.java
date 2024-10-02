package com.briscagame;

import java.io.IOException;
import java.util.Scanner;

public class BriscaGame {

    static Scanner stdin = new Scanner(System.in);

    public static int askCardIndex(String question) {
        int response = -1;
        System.out.println(question);
        while (response <= 0 || response >=4) {
            response = stdin.nextInt();
        }
        return response;
    }

    public static void main(String[] args) {

        try {
            SimpleHttpServer.start(null);
        } catch (IOException e) {
            System.err.println("Server failed to start.");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        Card brisca1 = new Card();
        Card brisca2 = new Card();
        brisca1.beats(brisca2);

        Deck deck = new Deck();
        System.out.println(deck);

        brisca1 = deck.draw();
        brisca2 = deck.draw();
        brisca1.beats(brisca2);

        System.out.println(deck);
        brisca1.beats(brisca2);

        System.out.println(deck);

        deck = new Deck();
        System.out.println(deck);

        GameManager gameManager = new GameManager();
        gameManager.startSim();

        gameManager = new GameManager();
        gameManager.startOnePlayer();
    }

}
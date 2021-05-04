/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import console.LocalPlayer;
import hangman.Game;
import hangman.Hangman;
import hangman.Player;

/**
 *
 * @author matteo
 */
@SuppressWarnings("all")
public class HangmanServer extends Player{
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public HangmanServer(Socket socket) throws Exception {
		this.socket = socket;
		inizializza();
	}
	
	private void inizializza() throws Exception{
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(),true);
	}

	@Override
	public void update(Game game) {
		String message = "";
	       switch(game.getResult()) {
           case FAILED:
               message += "f";
               message += game.getSecretWord();
               break;
           case SOLVED:
        	   message += "s";
        	   message += game.getSecretWord();
               break;
           case OPEN:
        	   message += "o";
        	   message += Game.MAX_FAILED_ATTEMPTS - game.countFailedAttempts();
        	   message += game.getKnownLetters();
               break;
	       }
	       System.out.println("Result = " + message);
	       out.println(message);
	}

	@Override
	public char chooseLetter(Game game) {
		String message = null;
		try {
			message = in.readLine();
		} catch (IOException e) {}
    	System.out.println("La lettera scelta e' " + message);
		return message.charAt(0);
	}
	
    /**
     * @throws IOException 
     * 
     */
    public static void main(String[] args) throws IOException {
    	int port = 8888;
    	
		ServerSocket serverSocket = new ServerSocket(port);
		Socket socket = serverSocket.accept();
		
    	try {
			HangmanServer server = new HangmanServer(socket);
	    	Hangman game = new Hangman();
	    	
	        Player player = server;
	        game.playGame(player);
	        
	    	serverSocket.close();
		} catch (Exception e) { 
			e.printStackTrace(); 
			}
    }


    
}

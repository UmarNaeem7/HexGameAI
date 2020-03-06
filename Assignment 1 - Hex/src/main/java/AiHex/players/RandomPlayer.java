package AiHex.players;

import AiHex.gameMechanics.Runner;

import java.util.ArrayList;

import AiHex.hexBoards.Board;
import AiHex.gameMechanics.Move;
import java.util.Random;

public class RandomPlayer implements Player{
    private Runner game = null;
    private int colour = 0;

    public RandomPlayer(Runner game, int colour) {
	this.game = game;
	this.colour = colour;
    }

     public Move getMove() {
		switch (colour) {
		case Board.RED:
			System.out.print("Red move: ");
			break;
		case Board.BLUE:
			System.out.print("Blue move: ");
			break;
		}

		while (game.getBoard().getSelected() == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Point choice = game.getBoard().getSelected();
                Random r = new Random();
                //statement to generate random number in a specified range
                //randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
                boolean flag = false;
                int random_x = 0, random_y = 0;
                while (!flag){
                random_x = r.nextInt(((game.getBoard().getSize()-1)-0)+1)+0;
                random_y = r.nextInt(((game.getBoard().getSize()-1)-0)+1)+0;
                if (game.getBoard().get(random_x, random_y) != Board.RED && 
                        game.getBoard().get(random_y, random_y) != Board.BLUE)
                            flag = true;
                }
                Move move = new Move(colour, random_x, random_y);

		game.getBoard().setSelected(null);
		return move;
    }

    public ArrayList<Board> getAuxBoards() {
		return new ArrayList<Board>();
    }
    
}

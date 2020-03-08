package AiHex.players;

import AiHex.gameMechanics.InvalidMoveException;
import AiHex.gameMechanics.Runner;

import java.awt.*;
import java.util.ArrayList;

import AiHex.hexBoards.Board;
import AiHex.gameMechanics.Move;
import AiHex.hexBoards.BoardData;
import AiHex.hexBoards.GameBoard;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AlphaBetaPlayer  implements Player{
    private Runner game = null;
    private int colour = 0;

    public AlphaBetaPlayer(Runner game, int colour) {
        this.game = game;
        this.colour = colour;
    }

    public Move getMove() {
        switch (colour) {
            case Board.RED:
                System.out.print("Red move: \n");
                break;
            case Board.BLUE:
                System.out.println("AI Player: ");
                break;
        }

        String[] emptySlots = game.getBoard().getEmptySlots();
        //Number of Empty Slots at emptySlots[0];
        //x = emptySlots[1][0], y = emptySlots[1][1];

        int emptySlotCount = Integer.parseInt(emptySlots[0]);
        Move move = new Move(Board.BLUE, 0, 0);
        int max = -6500;
        for(int i = 1; i < emptySlotCount; i++) {
            int x = (int) emptySlots[i].charAt(0) - '0';
            int y = (int) emptySlots[i].charAt(1) - '0';
            move = new Move(colour, x, y);

            BoardData b = game.getBoard().getData().clone();
            GameBoard copy = new GameBoard(game.getBoard().getSize(), b);
            try {
                copy.makeMove(move);
            }
            catch (InvalidMoveException e ){
                System.out.println("Error");
            }
            int val = minimax(copy, 2, true, -6000, 6000);
            System.out.println("Val: " + val);
            if(val > max) {
                max = val;
                move = new Move(Board.BLUE, x, y);
            }
        }
        System.out.println("Max Heuristic Value: " + max);
        System.out.println("MOVE SELECTED: x = " + move.getX() +" y = " + move.getY());
        game.getBoard().setSelected(null);
        return move;
    }

    public int evaluate(GameBoard gameBoard, int negative_colour) {
        int heuristic = -6000;
        String[] emptySlots = gameBoard.getEmptySlots();
        int num_emptySlots = Integer.parseInt(gameBoard.getEmptySlots()[0]);

        int cellVal = 0;
        char finishLine = 'a';

        for(int i = 0; i < 7; i++) {
            if(gameBoard.get(0, i) == Board.BLUE) {
                finishLine = 'r';//to the right
            }
            else if(gameBoard.get(6, i) == Board.BLUE) {
                finishLine = 'l';//to the left
            }
        }

        for(int i = 1; i < num_emptySlots; i++) {
            int x = (int)emptySlots[i].charAt(0) - '0';
            int y = (int)emptySlots[i].charAt(1) - '0';

            System.out.println("X = " + x + " Y = " + y);

            if(y != 0) {
                if(gameBoard.get(x, y-1) == Board.BLANK)
                    cellVal += 1;
                else if(gameBoard.get(x, y-1) == negative_colour)
                    cellVal -= 3;
            }

            if(x != 6 && y != 0) {
                if(gameBoard.get(x+1, y-1) == Board.BLANK)
                    cellVal += 1;
                else if(gameBoard.get(x+1, y-1) == negative_colour)
                    cellVal -= 3;
            }


            if(x != 0) {
                if(gameBoard.get(x-1, y) == Board.BLANK)
                    cellVal += 1;
                else if(gameBoard.get(x-1, y) == negative_colour)
                    cellVal -= 3;
            }

            if(x != 6) {
                if(gameBoard.get(x+1, y) == Board.BLANK)
                    cellVal += 1;
                else if(gameBoard.get(x+1, y) == negative_colour)
                    cellVal -= 3;
            }

            if(x != 0 && y != 6) {
                if(gameBoard.get(x-1, y+1) == Board.BLANK)
                    cellVal += 1;
                else if(gameBoard.get(x-1, y+1) == negative_colour)
                    cellVal -= 3;
            }

            if(y != 6) {
                if(gameBoard.get(x, y+1) == Board.BLANK)
                    cellVal += 1;
                else if(gameBoard.get(x, y+1) == negative_colour)
                    cellVal -= 3;
            }


            if(x+1 == 6) {
                if(y != 6 && finishLine =='r') {
                    if (gameBoard.get(x + 1, y + 1) == Board.BLANK) {
                        cellVal += 5;
                    }
                }

                if(finishLine == 'r') {
                    if(gameBoard.get(x+1, y) == Board.BLANK) {
                        cellVal += 5;
                    }
                }
            }
            else if(x-1 == 0 ) {
                if(y != 6 && finishLine == 'l') {
                    if(gameBoard.get(x-1, y+1) == Board.BLANK) {
                        cellVal += 5;
                    }
                }

                if(finishLine == 'l') {
                    if(gameBoard.get(x-1, y) == Board.BLANK) {
                        cellVal += 5;
                    }
                }
            }

            if(cellVal > heuristic)
                heuristic = cellVal;
        }

        return heuristic;
    }

    public int minimax(GameBoard gameBoard, int depth, boolean maximizingPlayer, int alpha, int beta){

        if(depth == 0 || gameBoard.checkwin(Board.RED) || gameBoard.checkwin(Board.BLUE)) {
            if(maximizingPlayer)
                return evaluate(gameBoard, Board.RED);
            else
                return  evaluate(gameBoard, Board.BLUE);
        }

        String[] emptySlots = gameBoard.getEmptySlots();
        int num_slots = Integer.parseInt(gameBoard.getEmptySlots()[0]);

        if(maximizingPlayer) {
            int maxEval = -6000;
            Move move = new Move(Board.BLUE, 0, 0);

            for(int i = 1; i < num_slots; i++) {
                int x = (int) emptySlots[i].charAt(0) - '0';
                int y = (int) emptySlots[i].charAt(1) - '0';
                move = new Move(colour, x, y);

                BoardData b = gameBoard.getData().clone();
                GameBoard copy = new GameBoard(gameBoard.getSize(), b);
                try {
                    copy.makeMove(move);
                }
                catch (InvalidMoveException e ){
                    System.out.println("Error");
                }

                int eval = minimax(copy, depth - 1, false, alpha, beta);

                if(eval > maxEval)
                    maxEval = eval;

                if(eval > alpha)
                    alpha = eval;

                if(beta <= alpha)
                    break;
            }
            return maxEval;
        }
        else {
            int minEval = 6000;

            for(int i = 1; i < num_slots; i++) {
                int x = (int) emptySlots[i].charAt(0) - '0';
                int y = (int) emptySlots[i].charAt(1) - '0';
                Move move = new Move(colour, x, y);

                BoardData b = gameBoard.getData().clone();
                GameBoard copy = new GameBoard(gameBoard.getSize(), b);
                try {
                    copy.makeMove(move);
                }
                catch (InvalidMoveException e ){
                    System.out.println("Error");
                }

                int eval = minimax(copy, depth - 1, true, alpha, beta );

                if(eval < minEval)
                    minEval = eval;

                if(eval < beta)
                    beta = eval;

                if(beta <= alpha)
                    break;
            }
            return minEval;
        }
    }

    public ArrayList<Board> getAuxBoards() {
        return new ArrayList<Board>();
    }

};

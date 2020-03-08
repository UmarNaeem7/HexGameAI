package AiHex.hexBoards;
import AiHex.gameMechanics.Move;
import AiHex.gameMechanics.InvalidMoveException;
import java.awt.Point;
import java.awt.Toolkit;

public class GameBoard extends AbstractBoard{

	private Point selected;
	protected BoardData data;

	public GameBoard(int size) {
    	this.name = "Game";
		this.data = new BoardData(size);
		this.size = size;
	}

	public GameBoard(int size, BoardData data){
		this.size = size;
		this.data = data;
	}

	public GameBoard(GameBoard g) {
		this.size = g.getSize();
		this.data = g.getData();
	}

	public GameBoard nextState(GameBoard b, Move move) {
		GameBoard gameBoard = new GameBoard(b.getSize(), b.getData());
		try{
			gameBoard.makeMove(move);
		}
		catch(InvalidMoveException e) {
			System.out.println("Invalid Move");
		}

		return gameBoard;
	}

	public boolean makeMove(Move move) throws InvalidMoveException {

    	boolean moveAccepted = false;
		int x = move.getX();
		int y = move.getY();
		int colour = move.getColour();
		if (x < 0 || x > size-1 || y < 0 ||y > size-1) {
     		Toolkit.getDefaultToolkit().beep();
			throw new InvalidMoveException(
					"Coordinates outside the play area!", move,
					InvalidMoveException.OUTSIDE_BOARD);
		} else if (data.get(move.getX(), move.getY()).getValue() == Board.BLANK ) {
			data.set(move.getX(), move.getY(), colour);
			moveAccepted = true;
			changeOccured = true;
		} else {
      		Toolkit.getDefaultToolkit().beep();
			throw new InvalidMoveException("That hex is not blank!", move,
					InvalidMoveException.ALREADY_TAKEN);
		}
		return moveAccepted;
	}

	public String[] getEmptySlots() {
		int emptySlots = 0;
		String[] emptySlotCoOrdinates = new String[size*size];

		for(int i = 0; i < this.size; i++) {
			for(int  j = 0; j < this.size; j++) {
				if (data.get(i, j).getValue() == Board.BLANK ) {
					char x = (char)(i+'0');
					char y = (char)(j+'0');
					emptySlotCoOrdinates[1 + emptySlots++] = String.valueOf(x) + String.valueOf(y);
				}
			}
		}
		emptySlotCoOrdinates[0] = String.valueOf(emptySlots);
		return emptySlotCoOrdinates;
	}

  	public boolean checkwin(int player){
    	return this.data.checkWin(player);
  	}


  	@Override
	public int get(int x, int y) {
		return data.get(x, y).getValue();
	}

	public Point getSelected() {
		return selected;
	}

	public void setSelected(Point selected) {
		this.selected = selected;
	}

	public void setSelected(int x, int y) {
		this.setSelected(new Point(x,y));
	}


  	@Override
	public String getName() {
		return name;
	}

	public int getSize() {
		return this.size;
	}

	public BoardData getData() {
		return this.data;
	}

  @Override
  public void setName(String name) {
    changeOccured = true;
    this.name = name;
  }

}

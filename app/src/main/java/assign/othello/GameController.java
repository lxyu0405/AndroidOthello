package assign.othello;

/**
 * Created by luxy on 10/24/15.
 */
public class GameController {

    //player turn
    public final static int BLACK = -1;
    public final static int WHITE = 1;
    public final static int EMPTY = 0;
    public final static int HINTS = 2;

    public final static int SIZE = 8;

    public int currentPlayer = BLACK;
    //black counter
    public int black_counter = 2;
    //white counter
    public int white_counter = 2;

    public int ChessBoard[][] = {
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,1,-1,0,0,0},
            {0,0,0,-1,1,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0}
    };

    public boolean isGameOver = false;
    public boolean hintsOn = false;

    public int getPositionValue(int x,int y){
        //System.out.println("X: " + x + " Y: " + y);
        return ChessBoard[x][y];
    }

    public void restart(){
        int origin[][] = {
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,1,-1,0,0,0},
            {0,0,0,-1,1,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0}
        };
        ChessBoard = origin;
        black_counter = 2;
        white_counter = 2;
        currentPlayer = BLACK;
        isGameOver = false;
    }

    public void switchHintsOn(){
        if(hintsOn)
            hintsOn = false;
        else
            hintsOn = true;
    }

    public void switchPlayer(){ currentPlayer *= -1;}

    public int getCurrentPlayer(){  return currentPlayer;   }

    public void setCurrentPlayer(int player){   currentPlayer = player;}

    public void setBlack_counter(int counter){  black_counter = counter;}
    public int getBlack_counter(){  return black_counter;   }

    public void setWhite_counter(int counter){  white_counter = counter;}
    public int getWhite_counter(){  return white_counter;}

    public void score(){
        int tmp_black = 0, tmp_white = 0;
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++){
                if(ChessBoard[i][j] == -1)
                    tmp_black++;
                else if(ChessBoard[i][j] == 1)
                    tmp_white++;
            }
        setBlack_counter(tmp_black);
        setWhite_counter(tmp_white);
    }

    public int whoWins(){ // Black Wins(<0) White Wins(>0) Equal(=0)
        if(black_counter > white_counter)
            return BLACK;
        else if(black_counter < white_counter)
            return WHITE;
        else
            return EMPTY;
    }

    public int findHints(){
        int count = 0;
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++){
                if(ChessBoard[i][j] == HINTS)
                    ChessBoard[i][j] = EMPTY;
                if(move(i,j,false) > 0) {
                    count++;
                    ChessBoard[i][j] = HINTS;
                }
            }
        return count;
    }

    public boolean isChessBoardFull(){
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++){
                if(ChessBoard[i][j] == HINTS || ChessBoard[i][j] == EMPTY)
                    return false;
            }
        return true;
    }


    public int move(int x,int y,boolean sureToMove){
        int cur_player = currentPlayer;
        int opp_player = currentPlayer * -1;
        // the position is black or white
        if(ChessBoard[x][y] == -1 || ChessBoard[x][y] == 1)
            return 0;
        int dx,dy;
        int changeNum = 0;
        // 8 possible directions check
        for(dx = -1; dx <= 1; dx++){
            for(dy = -1; dy <= 1; dy++){
                //empty direction
                if(dx == 0 && dy == 0)
                    continue;
                int pos_x, pos_y;
                for(int steps = 1; steps < SIZE; steps++){
                    pos_x = x + steps * dx;
                    pos_y = y + steps * dy;
                    //boundary check -> break
                    if(pos_x < 0 || pos_x > 7 || pos_y < 0 || pos_y > 7)
                        break;
                    //find the opposite chess -> continue
                    if(steps == 1 && ChessBoard[pos_x][pos_y] != opp_player)
                        break;
                    //find empty chess -> break
                    if(ChessBoard[pos_x][pos_y] == EMPTY || ChessBoard[pos_x][pos_y] == HINTS)
                        break;
                    //find the current chess -> Got it!
                    if(ChessBoard[pos_x][pos_y] == cur_player){
                        if(steps > 1){
                            changeNum += (steps -1);
                            if(sureToMove) { //determine Move Action OR Detect Action
                                for (int backsteps = steps - 1; backsteps >= 0; backsteps--) {
                                    ChessBoard[x + backsteps * dx][y + backsteps * dy] = cur_player;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return changeNum;
    }

    public void showChessBoardValue(){
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(ChessBoard[i][j] + " ");
            System.out.println();
        }
    }
}

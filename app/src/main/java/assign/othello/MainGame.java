package assign.othello;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;



public class MainGame extends Activity {

    //Empty(0) -- Black(-1) -- White(1)

    public static final int BOARD_ROW = 8;
    public static final int BOARD_COL = 8;


    GameController gameController = new GameController();

    Button btn_newGame;
    Button btn_hintsSwitch;

    TextView tv_blackCount;
    TextView tv_whiteCount;
    TextView tv_whichTurn;

    ImageView img_whichTurn;

    TableLayout tbl_chessBoard;


    //Game over dialog
    Dialog blackWinDialog;
    Dialog whiteWinDialog;
    Dialog drawDialog;

    //mediaplayer background music
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);


        //Button
        btn_newGame = (Button)findViewById(R.id.btn_newGame);
        btn_hintsSwitch = (Button)findViewById(R.id.btn_hints);

        //Score Board
        tv_blackCount = (TextView)findViewById(R.id.tv_blackCount);
        tv_whiteCount = (TextView)findViewById(R.id.tv_whiteCount);
        tv_whichTurn = (TextView)findViewById(R.id.tv_whichTurn);
        img_whichTurn = (ImageView)findViewById(R.id.img_whichTurn);

        //Chess Board
        tbl_chessBoard = (TableLayout)findViewById(R.id.TableChessBoard);

        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }

        blackWinDialog = new AlertDialog.Builder(this)
                .setTitle("is the Winner!")
                .setIcon(R.drawable.black_chess)
                .setMessage("Do you want to start a new game?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // new game logic
                        buttonFeedBack(50);
                        gameController.restart();
                        tv_whichTurn.setText("Turn: ");
                        gameController.findHints();
                        updateBoard();
                        updateViewData();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonFeedBack(50);
                    }
                })
                .create();

        whiteWinDialog = new AlertDialog.Builder(this)
                .setTitle("is the Winner!")
                .setIcon(R.drawable.white_chess)
                .setMessage("Do you want to start a new game?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // new game logic
                        buttonFeedBack(50);
                        gameController.restart();
                        tv_whichTurn.setText("Turn: ");
                        gameController.findHints();
                        updateBoard();
                        updateViewData();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonFeedBack(50);
                    }
                })
                .create();

        drawDialog = new AlertDialog.Builder(this)
                .setTitle("Draw Game!")
                .setMessage("Do you want to start a new game?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // new game logic
                        buttonFeedBack(50);
                        gameController.restart();
                        tv_whichTurn.setText("Turn: ");
                        gameController.findHints();
                        updateBoard();
                        updateViewData();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonFeedBack(50);
                    }
                })
                .create();




        btn_hintsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFeedBack(50);
                if(gameController.hintsOn)
                    btn_hintsSwitch.setText("HINTS ON");
                else
                    btn_hintsSwitch.setText("HINTS OFF");
                gameController.switchHintsOn();
                gameController.findHints();
                updateBoard();
                //Toast.makeText(getApplicationContext(),"btn_hintsSwitch",Toast.LENGTH_SHORT).show();
            }
        });

        btn_newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFeedBack(50);
                //Toast.makeText(getApplicationContext(), "btn_newGame", Toast.LENGTH_SHORT).show();
                gameController.restart();
                tv_whichTurn.setText("Turn: ");
                gameController.findHints();
                updateBoard();
                updateViewData();
            }
        });

        updateBoard();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }


    public void onImageButtonClick(View view){
        String boxName = view.getResources().getResourceEntryName(view.getId())+"";
        String[] parts = boxName.split("_");
        assert(parts.length == 3);
        if(parts.length != 3){
            Toast.makeText(getApplicationContext(),"SplitStringError",Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(gameController.isGameOver){
                gameOver();
                return;
            }
            int pos = Integer.parseInt(parts[2]);
            int pos_x = pos/10;
            int pos_y = pos%10;
            if(gameController.move(pos_x,pos_y,true) > 0){
                buttonFeedBack(50);
                gameController.switchPlayer();
                if(gameController.findHints() > 0){
                    // have some possible moves
                    updateBoard();
                    updateViewData();
                }else {
                    // gameController.findHints() == 0
                    // have no possible moves -> skip this player
                    gameController.switchPlayer();
                    if(gameController.findHints() > 0) {
                        updateBoard();
                        updateViewData();
                    }else{
                        // game over
                        updateBoard();
                        updateViewData();
                        gameController.isGameOver = true;
                        gameOver();
                    }
                }
            }
            //Toast.makeText(getApplicationContext(),"X: "+pos_x+" Y: "+pos_y,Toast.LENGTH_SHORT).show();
        }
    }

    public void gameOver(){
        // Game over function
        if(gameController.whoWins() == gameController.BLACK){
            //black wins
            blackWinDialog.show();
            tv_whichTurn.setText("Win:");
            img_whichTurn.setImageResource(R.drawable.black_chess);
        }else if(gameController.whoWins() == gameController.WHITE){
            //white wins
            whiteWinDialog.show();
            tv_whichTurn.setText("Win:");
            img_whichTurn.setImageResource(R.drawable.white_chess);
        }else if(gameController.whoWins() == gameController.EMPTY){
            //Equal
            drawDialog.show();
            tv_whichTurn.setText("Draw");
            img_whichTurn.setImageResource(R.drawable.transparent);
        }

    }

    public void updateViewData(){
        gameController.score();
        tv_blackCount.setText(String.valueOf(gameController.getBlack_counter()));
        tv_whiteCount.setText(String.valueOf(gameController.getWhite_counter()));
        changeTurn(gameController.getCurrentPlayer());
    }


    public void changeTurn(int player){
        if(player == -1){
            //black's turn
            img_whichTurn.setImageResource(R.drawable.black_chess);
        }else if(player == 1){
            //white's turn
            img_whichTurn.setImageResource(R.drawable.white_chess);
        }
    }

    public void updateBoard(){
        for(int i = 0; i < BOARD_ROW; i++)
            for(int j = 0; j < BOARD_COL; j++)
                setPieceColor(i,j,gameController.getPositionValue(i,j));
    }

    public void setPieceColor(int x,int y,int piece){
        //x,y determine position, piece -1(black) 1(white)
        String tmp_pos = Integer.toString(x) + Integer.toString(y);
        ImageButton tmp_imageButton = (ImageButton)tbl_chessBoard.findViewWithTag("position"+tmp_pos);
        if(piece == -1){
            //set to black
            tmp_imageButton.setImageResource(R.drawable.black_chess);
        }else if(piece == 1){
            //set to white
            tmp_imageButton.setImageResource(R.drawable.white_chess);
        }else if(piece == 0){
            //set to empty
            tmp_imageButton.setImageResource(R.drawable.transparent);
        }else if(piece == 2){
            if(gameController.hintsOn) {
                if (gameController.getCurrentPlayer() == gameController.BLACK)
                    tmp_imageButton.setImageResource(R.drawable.black_chess_t);
                else    //(gameController.getCurrentPlayer() == gameController.WHITE)
                    tmp_imageButton.setImageResource(R.drawable.white_chess_t);
            }else{
                tmp_imageButton.setImageResource(R.drawable.transparent);
            }
        }
    }

    public void buttonFeedBack(int millisecond){
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(millisecond);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

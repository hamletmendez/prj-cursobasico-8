/**Nombre: Tic tac toe
 * Autor: Hamlet D. Méndez M.
 * Objetivo: Formar tres posiciones consecutivas de un mismo tipo de figura
 * Fecha:22/09/2017
 */

package com.terecos.www.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import static com.terecos.www.tictactoe.MainActivity.gameMode;

public class GameLogicActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String INITIAL_MARK = "*"; //Marca inicial del arreglo
    private static final String CIRCLE = "O"; //Marca para el cirlulo
    private static final String CROSS = "X";  //Marca par las equis
    private static final String DRAW = "D";   //Empate
    private static final String ACTIVE = "N"; //El juego está en curso

    private String[] blocks = new String[9]; //Tablero de juego

    private static int player = 1; //Empieza el juego el jugador 1
    private String status = ACTIVE; //Estatus del juego
    private static String mark; //Marca activa
    private static int xwins; //Contador de juegos ganados por las equis
    private static int owins; //Contador de juegos ganado por los círculos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_logic);

        //configurar clic listener a los textview y la marca inicial del juego
        for(int i = 1; i < 10; i++){

            //Buscamos las vistas a las que le asignaremos el listener
            findViewById(getResources().getIdentifier("cell_"+i,"id",getPackageName())).setOnClickListener(this);

            //Iniciamos el tablero con astericos como marca inical
            blocks[i-1] = INITIAL_MARK;
        }

        //Asignamos el listener a los botones de nueva partida y volver a la actividad principal
        findViewById(R.id.button_home).setOnClickListener(this);
        findViewById(R.id.button_replay).setOnClickListener(this);

        //Asignamos la tipografía y el contador inicial
        makeScreen();

        //Determinamos el turno del jugador
        playerTurn();
    }

    @Override
    public void onBackPressed() {
        //Evitar que el jugador vuelva hacía la actividad principal presionando el botón back del celular
    }

    private void makeScreen() {
        //fullscreen activity
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Cambiar tipo de letra de título del juego
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Black Diamonds.ttf");
        TextView tv_status = (TextView)findViewById(R.id.textView);
        tv_status.setTypeface(typeface);

        //Motras las estadístcas del juego
        showStats();
    }

    private void playerTurn(){
        player = (player%2==0) ? 2 : 1;
        mark = (player==1)?CROSS:CIRCLE;
        TextView tv = (TextView)findViewById(R.id.display_board);
        tv.setText(getString(R.string.player_turn) + mark);

        /*Si el modo de juego es contra la computadora se llama el método que automatiza las jugadas
          del segundo jugador*/
        if (player==2 && gameMode == 1 ) {
            int pos = markCircle();
            makeMove(findViewById(getResources().getIdentifier("cell_"+(pos+1),"id",getPackageName())));
        }
    }

    private void makeMove(View v){
        if (status.equals(ACTIVE)) { //El juego no ha terminado

            //Obtenemos la posición de la vista por medio del tag definido en el textview
            int pos = Integer.parseInt(v.getTag().toString());

            if (blocks[pos].equals(INITIAL_MARK)) {  //Si la celda no se ha ocupado
                TextView btn = (TextView) v;
                btn.setText(mark); //mostramos la marca en el tablero

                blocks[pos] = mark; //actualizamos la matriz

                //Verificamos si hay un ganaodor
                status = checkForWinner();

                //Si el juego terminó se muetra el botón para volver a jugar
                if (!status.equals(ACTIVE)) showButtonReplay();

                switch (status) {
                    case CROSS:
                        showGameResult(CROSS); //Ganan las X
                        break;
                    case CIRCLE:
                        showGameResult(CIRCLE); //Ganan los O
                        break;
                    case DRAW:
                        showGameResult(DRAW); //Empate
                        break;
                    default:
                        player++;  //Cambiamos de jugador
                        playerTurn();
                }

            }
        }
    }

    //Mostrar los mensajes que finalizan el juego
    private void showGameResult(String status){
        TextView tv = (TextView) findViewById(R.id.display_board);
        switch (status){
            case CROSS:
                tv.setText(getString(R.string.juego_terminado) + CROSS +"] gana!");
                tv.setTextColor(Color.parseColor("#297af3"));
                xwins++;
                break;
            case CIRCLE:
                tv.setText(getString(R.string.juego_terminado) + CIRCLE +"] gana!");
                tv.setTextColor(Color.parseColor("#ef3c7a"));
                owins++;
                break;
            case DRAW:
                tv.setText(R.string.empate);
                tv.setTextColor(Color.parseColor("#28c6dc"));
                break;
        }
        showStats();
    }

    //Mostrar las estadísticas
    private void showStats(){
        TextView stats = (TextView)findViewById(R.id.tv_stats);
        stats.setText(xwins+" - "+owins);
    }

    //Mostrar el botón para volver a jugar
    private void showButtonReplay(){
        Button btn = (Button) findViewById(R.id.button_replay);
        btn.setVisibility(View.VISIBLE);
    }

    //Lógica para buscar el ganador
    private String checkForWinner(){

        //Combinaciones en las que un jugador puede ganar
        final int combinations[][] ={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

        boolean winner, draw;
        draw= winner= true;

        for(int i = 0; i< combinations.length; i++){
            winner = true;
            for(int pos: combinations[i]){

                if(!blocks[pos].equals(mark)) winner = false;

                if(blocks[pos].equals("*")) draw= false;
            }
            if (winner){
                findViewById(getResources().getIdentifier("stick"+i,"id",getPackageName())).setVisibility(View.VISIBLE);
                return mark;
            }
        }

        if (!winner && !draw) return ACTIVE;
        else return DRAW;

    }

    private int markCircle() {
        int posjugada = -1;
        int sumX, sumAs, posAs = 0;
        final int playCombinations[][] ={{0,4,8},{2,4,6},{3,4,5},{1,4,7},{0,1,2},{6,7,8},{0,3,6},{2,5,8}};

        for(int i=0; i< playCombinations.length; i++){
            sumX = sumAs = 0;
            for(int pos: playCombinations[i]){
                if (blocks[pos].equals(CROSS)) sumX++;
                else if (blocks[pos].equals(INITIAL_MARK)){
                    sumAs++;
                    posAs = pos;
                }
            }
            if (sumAs==1 && sumX==2) return posAs;
        }

        Random r = new Random();
        while (true) {
            posjugada = r.nextInt(9);
            if (blocks[posjugada].equals(INITIAL_MARK)) {
                break;
            }
        }
        return posjugada;
    }

    //Iniciar nuevo juego
    private void newGame(){
        Intent starter = getIntent();
        finish();
        starter.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(starter);
    }

    //Volver a la ventana principal
    private void backHome(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_back_home)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        xwins = owins = 0;
                        player = 1;
                        Intent intent = new Intent(GameLogicActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        //Manejar la vista que genera el evento
        switch (v.getId()){
            case R.id.button_home:
                backHome();
                break;
            case R.id.button_replay:
                newGame();
                break;
            default:
                makeMove(v);
        }
    }
}

package giubotta.gbsoft.allenamento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity {

    Chronometer simpleChronometer;
    Button start, pausa, resetButton, setFormat, clearFormat;
    TextView txt_info, save_dati, txt_dati;
    String giorno, ora, giro,textData, textData2;

    // Identificatore delle preferenze dell'applicazione
    private final static String MY_PREFERENCES = "MyPref";
    // Costante relativa al nome della particolare preferenza
    private final static String TEXT_DATA_GIORNO = "textData";
    private final static String TEXT_DATA_TEMPO = "textData2";
    private long lastPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        updatePreferencesData();

        final String giorno = DateFormat.format( "dd-MM-yyyy", new Date( System.currentTimeMillis() ) ).toString();
        final String ora = DateFormat.format( "HH:mm:ss", new Date( System.currentTimeMillis() ) ).toString();

        // initiate views
        save_dati = (TextView) findViewById( R.id.save_dati );
        txt_info = (TextView) findViewById( R.id.txt_info );
        txt_dati = (TextView) findViewById( R.id.txt_dati );
        simpleChronometer = (Chronometer) findViewById( R.id.simpleChronometer );
        start = (Button) findViewById( R.id.startButton );
        pausa = (Button) findViewById( R.id.stopButton );
        resetButton = (Button) findViewById( R.id.resetButton );
        setFormat = (Button) findViewById( R.id.setFormat );
        clearFormat = (Button) findViewById( R.id.clearFormat );


        // perform click  event on start button to start a chronometer
        start.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleChronometer.setVisibility(View.VISIBLE);
                if (lastPause != 0){
                    simpleChronometer.setBase(simpleChronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                }
                else{
                    simpleChronometer.setBase(SystemClock.elapsedRealtime());
                }

                simpleChronometer.start();
                start.setEnabled(false);
                pausa.setEnabled(true);
                txt_info.setTextSize( 25 );
                txt_info.setText( "Allenamento del: " + giorno + "\n inizio Ore: " + ora + "\n tempo di allenamento: " );
                giro = "Allenamento del: " + giorno + "\n inizio Ore: " + ora + "\n tempo di allenamento: ";
            }
        } );

        // perform click  event on stop button to stop the chronometer
        pausa.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lastPause = SystemClock.elapsedRealtime();
                simpleChronometer.stop();
                pausa.setEnabled(false);
                start.setEnabled(true);
            }
        } );

        // perform click  event on restart button to set the base time on chronometer
        resetButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                txt_dati.setText( "" );
                txt_info.setText( "" );
                simpleChronometer.setBase( SystemClock.elapsedRealtime() );
            }
        } );

        // perform click  event on set Format button to set the format of chronometer
        setFormat.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                simpleChronometer.setFormat( "Time 0:%s" );
                //simpleChronometer.setFormat("Time (%s)");
            }
        } );

        // perform click  event on clear button to clear the current format of chronmeter as you set through set format
        clearFormat.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                simpleChronometer.setFormat( null );
            }
        } );

        // Salva i dati allenamento
        save_dati.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Otteniamo il riferimento alle Preferences
                SharedPreferences prefs = getSharedPreferences( MY_PREFERENCES, Context.MODE_PRIVATE );
                // Otteniamo il corrispondente Editor
                SharedPreferences.Editor editor = prefs.edit();
                // Modifichiamo il valore con quello inserito nell'EditText
                CharSequence textData = giro;
                CharSequence textData2 = simpleChronometer.getText();

                if (textData.equals( "" ) || textData2.equals( "" )) {

                } else {
                    // Lo salviamo nelle Preferences
                    editor.putString( TEXT_DATA_GIORNO, textData.toString() );
                    editor.putString( TEXT_DATA_TEMPO, textData2.toString() );
                    editor.commit();

                    Toast.makeText( MainActivity.this, "Allenamento salvato!", Toast.LENGTH_SHORT ).show();
                }
                updatePreferencesData();
            }
        } );
    }

    private void updatePreferencesData(){
        // Leggiamo le Preferences
        SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        // Leggiamo l'informazione associata alla proprietà TEXT_DATA
        String textData = prefs.getString(TEXT_DATA_GIORNO, "vuoto ");
        String textData2 = prefs.getString(TEXT_DATA_TEMPO, "vuoto");

        // Lo impostiamo alla TextView
        TextView outputView = (TextView) findViewById(R.id.txt_dati);
        outputView.setText(textData + textData2);
    }
}



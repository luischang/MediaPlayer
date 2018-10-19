package pe.com.moviles.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    private Button btnAdelante, btnPausar, btnReproducir, btnAtras;
    private MediaPlayer mediaPlayer;

    private double horaInicio = 0;
    private double horaFinal = 0;

    private Handler myHandler = new Handler();
    private int TiempoAdelanto = 5000;
    private int TiempoAtraso = 5000;
    private SeekBar seekbar;
    private TextView tvTiempoRestante, tvDuracion, tvNombreMusica;

    public static int oneTimeOnly = 0;

    private Runnable ActualizaTiempoSonido = new Runnable() {
        public void run() {
            horaInicio = mediaPlayer.getCurrentPosition();
            tvTiempoRestante.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) horaInicio),
                    TimeUnit.MILLISECONDS.toSeconds((long) horaInicio) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) horaInicio)))
            );
            seekbar.setProgress((int) horaInicio);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdelante = (Button) findViewById(R.id.btnAdelante);
        btnPausar = (Button) findViewById(R.id.btnPausar);
        btnReproducir = (Button) findViewById(R.id.btnReproducir);
        btnAtras = (Button) findViewById(R.id.btnAtras);

        tvTiempoRestante = (TextView) findViewById(R.id.tvTiempoRestante);
        tvDuracion = (TextView) findViewById(R.id.tvDuracion);
        tvNombreMusica = (TextView) findViewById(R.id.tvNombreMusica);
        tvNombreMusica.setText("Song.mp3");

        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        btnPausar.setEnabled(false);

        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Reproduciendo sonido", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                horaFinal = mediaPlayer.getDuration();
                horaInicio = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) horaFinal);
                    oneTimeOnly = 1;
                }

                tvDuracion.setText(String.format("%d min, %d seg",
                        TimeUnit.MILLISECONDS.toMinutes((long) horaFinal),
                        TimeUnit.MILLISECONDS.toSeconds((long) horaFinal) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        horaFinal)))
                );

                tvTiempoRestante.setText(String.format("%d min, %d seg",
                        TimeUnit.MILLISECONDS.toMinutes((long) horaInicio),
                        TimeUnit.MILLISECONDS.toSeconds((long) horaInicio) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        horaInicio)))
                );

                seekbar.setProgress((int) horaInicio);
                myHandler.postDelayed(ActualizaTiempoSonido, 100);
                btnPausar.setEnabled(true);
                btnReproducir.setEnabled(false);
            }
        });

        btnPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausando sonido", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                btnPausar.setEnabled(false);
                btnReproducir.setEnabled(true);
            }
        });

        btnAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) horaInicio;

                if ((temp + TiempoAdelanto) <= horaFinal) {
                    horaInicio = horaInicio + TiempoAdelanto;
                    mediaPlayer.seekTo((int) horaInicio);
                    Toast.makeText(getApplicationContext(), "Has saltado hacia adelante 5 segundos.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No se puede saltar hacia adelante 5 segundos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) horaInicio;

                if ((temp - TiempoAtraso) > 0) {
                    horaInicio = horaInicio - TiempoAtraso;
                    mediaPlayer.seekTo((int) horaInicio);
                    Toast.makeText(getApplicationContext(), "Has saltado hacia atrás 5 segundos.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No se puede saltar hacia atrás 5 segundos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

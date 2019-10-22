package ua.nure.stanchyk.pz2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    SeekBar seekR;
    SeekBar seekG;
    SeekBar seekB;

    TextView valueR;
    TextView valueG;
    TextView valueB;
    
    TextView ShowColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekR=(SeekBar)findViewById(R.id.seekR);
        seekG=(SeekBar)findViewById(R.id.seekG);
        seekB=(SeekBar)findViewById(R.id.seekB);

        ShowColor=(TextView)findViewById(R.id.textView);
        
        valueR=(TextView)findViewById(R.id.valueR);
        valueG=(TextView)findViewById(R.id.valueG);
        valueB=(TextView)findViewById(R.id.valueB);
        
        seekR.setOnSeekBarChangeListener(this);
        seekG.setOnSeekBarChangeListener(this);
        seekB.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int R=seekR.getProgress();
        int G=seekG.getProgress();
        int B=seekB.getProgress();

        ShowColor.setBackgroundColor(Color.argb(255,R,G,B));
        
        valueR.setText(String.valueOf(R));
        valueG.setText(String.valueOf(G));
        valueB.setText(String.valueOf(B));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        
    }
}

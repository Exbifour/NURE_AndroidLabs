package ua.nure.stanchyk.pz3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtResult;
    private Button btnZero;
    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private Button btnFour;
    private Button btnFive;
    private Button btnSix;
    private Button btnSeven;
    private Button btnEight;
    private Button btnNine;
    private Button btnPlus;
    private Button btnMinus;
    private Button btnMultiply;
    private Button btnDivide;
    private Button btnEquals;
    private Button btnC;
    private Button btnDecimal;

    private String value1 = "0";
    private String value2 = "";
    private int actionId = 1;
    private boolean isDotted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    private void initControls() {
        txtResult = (TextView) findViewById(R.id.txtResult);

        btnZero = (Button) findViewById(R.id.btnZero);
        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnThree = (Button) findViewById(R.id.btnThree);
        btnFour = (Button) findViewById(R.id.btnFour);
        btnFive = (Button) findViewById(R.id.btnFive);
        btnSix = (Button) findViewById(R.id.btnSix);
        btnSeven = (Button) findViewById(R.id.btnSeven);
        btnEight = (Button) findViewById(R.id.btnEight);
        btnNine = (Button) findViewById(R.id.btnNine);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMultiply = (Button) findViewById(R.id.btnMultiply);
        btnDivide = (Button) findViewById(R.id.btnDivide);
        btnEquals = (Button) findViewById(R.id.btnEquals);
        btnC = (Button) findViewById(R.id.btnC);
        btnDecimal = (Button) findViewById(R.id.btnDecimal);

        btnZero.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnEquals.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnDecimal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean clearValue2 = false;

        switch (v.getId()) {
            case R.id.btnZero:
                value2 += "0";
                break;
            case R.id.btnOne:
                value2 += "1";
                break;
            case R.id.btnTwo:
                value2 += "2";
                break;
            case R.id.btnThree:
                value2 += "3";
                break;
            case R.id.btnFour:
                value2 += "4";
                break;
            case R.id.btnFive:
                value2 += "5";
                break;
            case R.id.btnSix:
                value2 += "6";
                break;
            case R.id.btnSeven:
                value2 += "7";
                break;
            case R.id.btnEight:
                value2 += "8";
                break;
            case R.id.btnNine:
                value2 += "9";
                break;
            case R.id.btnPlus:
                callEquals();
                actionId = 1;
                clearValue2 = true;
                break;
            case R.id.btnMinus:
                callEquals();
                actionId = 2;
                clearValue2 = true;
                break;
            case R.id.btnMultiply:
                callEquals();
                actionId = 3;
                clearValue2 = true;
                break;
            case R.id.btnDivide:
                callEquals();
                actionId = 4;
                clearValue2 = true;
                break;
            case R.id.btnEquals:
                callEquals();
                clearValue2 = true;
                break;
            case R.id.btnC:
                value2 = "0";
                clearValue2 = true;
                break;
            case R.id.btnDecimal:
                if(!isDotted) {
                    value2 += ".";
                    isDotted = true;
                }
                break;
        }
        txtResult.setText(value2);
        if(clearValue2)
            value2 = "";
    }


    private void callEquals() {
        switch (actionId) {
            case 1: // Plus actionId
                value2 = String.valueOf(Double.valueOf(value1) + Double.valueOf(value2));
                break;
            case 2: // Minus actionId
                value2 = String.valueOf(Double.valueOf(value1) - Double.valueOf(value2));
                break;
            case 3: // Multiply actionId
                value2 = String.valueOf(Double.valueOf(value1) * Double.valueOf(value2));
                break;
            case 4: // Divide actionId
                value2 = String.valueOf(Double.valueOf(value1) / Double.valueOf(value2));
                break;
        }
        value1 = value2;
    }
}
package ua.nure.stanchyk.pz3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("value1", value1);
        outState.putString("value2", value2);
        outState.putString("textField", txtResult.getText().toString());
        outState.putInt("actionId", actionId);
        outState.putBoolean("isDotted", isDotted);

        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        value1 = savedInstanceState.getString("value1");
        value2 = savedInstanceState.getString("value2");
        actionId = savedInstanceState.getInt("actionId");
        isDotted = savedInstanceState.getBoolean("isDotted");

        txtResult.setText(savedInstanceState.getString("textField"));
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
                addNumber("0");
                //value2 += "0";
                break;
            case R.id.btnOne:
                addNumber("1");
                //value2 += "1";
                break;
            case R.id.btnTwo:
                addNumber("2");
                ///value2 += "2";
                break;
            case R.id.btnThree:
                addNumber("3");
                //value2 += "3";
                break;
            case R.id.btnFour:
                addNumber("4");
//                value2 += "4";
                break;
            case R.id.btnFive:
                addNumber("5");
//                value2 += "5";
                break;
            case R.id.btnSix:
                addNumber("6");
//                value2 += "6";
                break;
            case R.id.btnSeven:
                addNumber("7");
//                value2 += "7";
                break;
            case R.id.btnEight:
                addNumber("8");
//                value2 += "8";
                break;
            case R.id.btnNine:
                addNumber("9");
//                value2 += "9";
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
                actionId = -1;
                break;
            case R.id.btnC:
                value1 = "0";
                value2 = "0";
                clearValue2 = true;
                isDotted = false;
                break;
            case R.id.btnDecimal:
                if(value2.isEmpty())
                    value2 = "0";
                if(!isDotted) {
                    addNumber(".");
//                    value2 += ".";
                    isDotted = true;
                }
                break;
        }

        txtResult.setText(value2);

        if(clearValue2) {
            value2 = "";
            clearValue2 = false;
        }
    }

    private void callEquals() {
        if (value2.isEmpty()){
            value2 = "0";
        }
        switch (actionId) {
            case -1:
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

        if (Pattern.matches("^\\d+\\.0*$", value2)) {
            value2 = value2.substring(0, value2.indexOf("."));
        }

        isDotted = false;
        value1 = value2;
    }

    private void addNumber(String val) {
        if(actionId == -1) {
            actionId = 1;
            value2 = val;
            value1 = "0";
        }
        else {
            value2 += val;
        }
    }
}
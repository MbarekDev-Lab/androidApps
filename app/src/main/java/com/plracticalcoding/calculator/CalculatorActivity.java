package com.plracticalcoding.calculator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.plracticalcoding.myapplication.databinding.ActivityCalculatorBinding;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class CalculatorActivity extends AppCompatActivity {
    private ActivityCalculatorBinding binding;
    private String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn0.setOnClickListener(v -> onNumberClicked("0"));
        binding.btn1.setOnClickListener(v -> onNumberClicked("1"));
        binding.btn2.setOnClickListener(v -> onNumberClicked("2"));
        binding.btn3.setOnClickListener(v -> onNumberClicked("3"));
        binding.btn4.setOnClickListener(v -> onNumberClicked("4"));
        binding.btn5.setOnClickListener(v -> onNumberClicked("5"));
        binding.btn6.setOnClickListener(v -> onNumberClicked("6"));
        binding.btn7.setOnClickListener(v -> onNumberClicked("7"));
        binding.btn8.setOnClickListener(v -> onNumberClicked("8"));
        binding.btn9.setOnClickListener(v -> onNumberClicked("9"));

        binding.btnDot.setOnClickListener(v -> onNumberClicked("."));
        binding.btnAdd.setOnClickListener(v -> onOperatorClicked("+"));
        binding.btnSub.setOnClickListener(v -> onOperatorClicked("-"));
        binding.btnMulti.setOnClickListener(v -> onOperatorClicked("*"));
        binding.btnDiv.setOnClickListener(v -> onOperatorClicked("/"));

        binding.btnAC.setOnClickListener(v -> {
            data = "";
            binding.textViewHistory.setText("");
            binding.textViewResult.setText("");
        });

        binding.brnDel.setOnClickListener(v -> {
            if (data != null && data.length() > 0) {
                data = data.substring(0, data.length() - 1);
                binding.textViewResult.setText(data);
            }
        });

        binding.btnEquals.setOnClickListener(v -> {
            if (data != null && !data.isEmpty()) {
                binding.textViewHistory.setText(data);
                String finalResult = getResult(data);
                binding.textViewResult.setText(finalResult);
                data = finalResult; // Continue calculation with the result
            }
        });
    }

    public void onNumberClicked(String number) {
        data = binding.textViewResult.getText().toString() + number;
        binding.textViewResult.setText(data);
    }

    public void onOperatorClicked(String operator) {
        data = binding.textViewResult.getText().toString() + operator;
        binding.textViewResult.setText(data);
    }

    private String getResult(String expression) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, expression, "JavaScript", 1, null).toString();
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "Error";
        } finally {
            Context.exit();
        }
    }




}

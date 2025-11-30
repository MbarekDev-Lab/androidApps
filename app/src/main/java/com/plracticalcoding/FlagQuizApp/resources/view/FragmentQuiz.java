package com.plracticalcoding.FlagQuizApp.resources.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.plracticalcoding.FlagQuizApp.resources.datadase.DatabaseCopyHelper;
import com.plracticalcoding.FlagQuizApp.resources.dao.FlagsDao;
import com.plracticalcoding.FlagQuizApp.resources.model.FlagsModel;
import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.myapplication.databinding.FragmentQuizBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;


public class FragmentQuiz extends Fragment {

    FragmentQuizBinding fragmentQuizBinding;
    FlagsDao dao;
    DatabaseCopyHelper databaseCopyHelper;
    ArrayList<FlagsModel> flagList = new ArrayList<>();

    int correctNumber = 0;
    int wrongNumber = 0;
    int emptyNumber = 0;
    int questionNumber = 0;
    FlagsModel correctFlag;
    ArrayList<FlagsModel> wrongFlags = new ArrayList<>();

    Button[] buttons;
    boolean optionControl = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentQuizBinding = FragmentQuizBinding.inflate(inflater, container, false);

        buttons = new Button[]{fragmentQuizBinding.buttonA, fragmentQuizBinding.buttonB, fragmentQuizBinding.buttonC, fragmentQuizBinding.buttonD};

        dao = new FlagsDao();
        databaseCopyHelper = new DatabaseCopyHelper(requireActivity());
        flagList = dao.getRandomTenRecords(databaseCopyHelper);

        for (FlagsModel flag : flagList) {
            Log.d("flags", String.valueOf(flag.getFlagId()));
            Log.d("flags", flag.getCountryName());
            Log.d("flags", flag.getFlagName());
            Log.d("flags", "********************************");

        }

        showData();


        fragmentQuizBinding.buttonA.setOnClickListener(v -> {
            answerControl(fragmentQuizBinding.buttonA);
        });
        fragmentQuizBinding.buttonB.setOnClickListener(v -> {
            answerControl(fragmentQuizBinding.buttonB);
        });
        fragmentQuizBinding.buttonC.setOnClickListener(v -> {
            answerControl(fragmentQuizBinding.buttonC);
        });
        fragmentQuizBinding.buttonD.setOnClickListener(v -> {
            answerControl(fragmentQuizBinding.buttonD);
        });
        fragmentQuizBinding.buttonNext.setOnClickListener(v -> {

            questionNumber++;

            if (questionNumber > 9) {
                if (!optionControl) {
                    emptyNumber++;
                }
                //Toast.makeText(requireActivity(),"The quiz is finished.",Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putInt("correct", correctNumber);
                bundle.putInt("wrong", wrongNumber);
                bundle.putInt("empty", emptyNumber);

                //Navigation.findNavController(v).navigate(R.id.action_fragmentQuiz_to_fragmentResult, bundle, new NavOptions.Builder().setPopUpTo(R.id.fragmentHome, false).build());
                /*
                    NavDirections direction = FragmentQuizDirections.actionFragmentQuizToFragmentResult()
                            .setCorrect(correctNumber)
                            .setWrong(wrongNumber)
                            .setEmpty(emptyNumber);
                    NavController navController = NavHostFragment.findNavController(this);
                    navController.navigate(direction);
                    navController.popBackStack(R.id.fragmentHome,false);
                 */
            } else {
                showData();
                setButtonToInitialProperties();
                if (!optionControl) {
                    emptyNumber++;
                    fragmentQuizBinding.textViewEmpty.setText(String.valueOf(emptyNumber));
                }
            }

            optionControl = false;


        });


        return fragmentQuizBinding.getRoot();
    }

    public void showData() {

        fragmentQuizBinding.textViewQuestion.setText(getResources().getString(R.string.question_number).concat(String.valueOf(questionNumber + 1)));
        correctFlag = flagList.get(questionNumber);
        //fragmentQuizBinding.imageViewFlag.setImageResource(getResources().getIdentifier(correctFlag.getFlagName(),"drawable",requireActivity().getPackageName()));
        int resId = getResId(correctFlag.getFlagName(), R.drawable.class);
        if (resId != -1) {
            fragmentQuizBinding.imageViewFlag.setImageResource(resId);
        }
        wrongFlags = dao.getRandomThreeRecords(databaseCopyHelper, correctFlag.getFlagId());

        HashSet<FlagsModel> mixOptions = new HashSet<>();
        mixOptions.add(correctFlag);
        mixOptions.add(wrongFlags.get(0));
        mixOptions.add(wrongFlags.get(1));
        mixOptions.add(wrongFlags.get(2));

        ArrayList<FlagsModel> options = new ArrayList<>(mixOptions);

        fragmentQuizBinding.buttonA.setText(options.get(0).getCountryName());
        fragmentQuizBinding.buttonB.setText(options.get(1).getCountryName());
        fragmentQuizBinding.buttonC.setText(options.get(2).getCountryName());
        fragmentQuizBinding.buttonD.setText(options.get(3).getCountryName());


    }

    public int getResId(String correctFlagName, Class<?> drawable) {

        try {
            Field field = drawable.getDeclaredField(correctFlagName);
            return field.getInt(field);
        } catch (Exception e) {
            Log.d("error", e.toString());
            return -1;
        }

    }

    public void answerControl(Button button) {

        String clickedOptionText = button.getText().toString();
        String correctAnswer = correctFlag.countryName;
        if (clickedOptionText.equals(correctAnswer)) {
            correctNumber++;
            fragmentQuizBinding.textViewCorrect.setText(String.valueOf(correctNumber));
            button.setBackgroundColor(Color.GREEN);
        } else {
            wrongNumber++;
            fragmentQuizBinding.textViewWrong.setText(String.valueOf(wrongNumber));
            button.setBackgroundColor(Color.RED);
            button.setTextColor(Color.WHITE);


            for (Button btn : buttons) {
                if (btn.getText().toString().equals(correctAnswer)) {
                    btn.setBackgroundColor(Color.GREEN);
                    break;
                }
            }

        }

        for (Button btn : buttons) {
            btn.setClickable(false);
        }

        optionControl = true;

    }

    public void setButtonToInitialProperties() {
        for (Button btn : buttons) {
            btn.setBackgroundColor(Color.WHITE);
            btn.setTextColor(getResources().getColor(R.color.pink, requireActivity().getTheme()));
            btn.setClickable(true);
        }
    }

}
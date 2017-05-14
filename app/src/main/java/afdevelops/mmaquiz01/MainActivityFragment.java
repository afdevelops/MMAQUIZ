package afdevelops.mmaquiz01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "MMAQUIZ Activity";
    private static final int FIGHTERS_IN_QUIZ = 88;

    private List<String> fileNameList; //имена файлов с фотографиями бойцов
    private int counter = 0; //для счёта использованных букв в ячейках
    public int getCounter(){
        return counter;
    }
    public void setCounter(int count){
        this.counter = count;
    }
    private int fightersNum;
    private List<String> categoryList = new ArrayList<>();
    private boolean checkAnswer;

    private List<String> buttonsData = new ArrayList<>();
    private List<String> buttonsData2 = new ArrayList<>();
    private Handler handler; //для задержки запуска следующего бойца. Но я думаю, он нам не понадобится, потому что переход будет осуществляться кликом.
    private Animation shakeAnimation; //анимация неправильного заполнения

    private LinearLayout quizLinearLayout; //Макет с quiz'ом
    private TextView levelNameTextVeiw;
    private ImageView fighterImageView; //Изображение бойца
    private TextView answerTextView;
    private LinearLayout[] guessLinearLayouts; //ряды с кнопками для выбора букв
    Button buttonSteer;
    Button buttonDelete;
    Button buttonNext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        fileNameList = new ArrayList<>();
        handler = new Handler();

        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3);

        quizLinearLayout = (LinearLayout) view.findViewById(R.id.quizLinearLayout);
        fighterImageView = (ImageView) view.findViewById(R.id.fighterImageView);
        guessLinearLayouts = new LinearLayout[3];
        guessLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.row3LinearLayout);
        guessLinearLayouts[2] = (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);
        levelNameTextVeiw = (TextView) view.findViewById(R.id.levelNameTextView);
        buttonDelete = (Button) view.findViewById(R.id.buttonDelete);
        buttonSteer = (Button) view.findViewById(R.id.buttonSteer);
        buttonNext = (Button) view.findViewById(R.id.buttonNext);
        View.OnClickListener onClickButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonSteer:
                        Steer();
                        break;
                    case R.id.buttonDelete:
                        deleteLetter();
                        break;
                    /*case R.id.buttonNext:
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animate(true);
                        }
                    }, 2000);
                        break;*/
                    case R.id.cell1:
                    case R.id.cell2:
                    case R.id.cell3:
                    case R.id.cell4:
                    case R.id.cell5:
                    case R.id.cell6:
                    case R.id.cell7:
                    case R.id.cell8:
                    case R.id.cell9:
                    case R.id.cell10:
                    case R.id.cell11:
                    case R.id.cell12:
                        handleCancel(v);
                        break;
                    default:
                        handleClicks(v, getCounter());
                        setCounter(getCounter() + 1);
                        break;
                }
            }
        };
        for(int i = 0; i < 12;i++)
        {
            Button e = (Button) guessLinearLayouts[2].getChildAt(i);
            e.setOnClickListener(onClickButton);
        }
        for (int i = 0; i < 7; i++)
        {
            Button d = (Button) guessLinearLayouts[0].getChildAt(i);
            d.setOnClickListener(onClickButton);
        }
        buttonSteer.setOnClickListener(onClickButton);
        buttonDelete.setOnClickListener(onClickButton);
        for(int i = 8; i < 15; i++) {
                Button b = (Button) guessLinearLayouts[1].getChildAt(i-8);
                b.setOnClickListener(onClickButton);
        }


        return view;

    }



    public void handleClicks(View v, int counter) {
        //Context mContext = getActivity().getBaseContext();
        //Resources mRes = mContext.getResources();
        //int btnId = mRes.getIdentifier("button", "id",(getActivity()).getBaseContext().getPackageName());
        int buttonId  = getResources().getIdentifier("cell" + 1, "id", getActivity().getPackageName());
        Button resetButton  = (Button) getView().findViewById(buttonId);
        for(int i = 0;i<12;i++) {
            if (resetButton.getVisibility() == View.VISIBLE) {
                buttonId  = getResources().getIdentifier("cell" + (i+1), "id", getActivity().getPackageName());
                resetButton = (Button) getView().findViewById(buttonId);
            }
        }
        Button pressedButton = (Button) getView().findViewById(v.getId());
        buttonsData.add(counter, String.valueOf(v.getId())); // ID кнопки снизу
        buttonsData2.add(counter, String.valueOf(buttonId)); // ID кнопки сверху
        resetButton.setVisibility(View.VISIBLE); //To set visible
        pressedButton.setVisibility(View.INVISIBLE);
        String buttonText = pressedButton.getText().toString();
        resetButton.setText(String.valueOf(buttonText));
        if(counter == (getFightersName().length() - 1)) {
            checkName();
        }
    }

    private String fName = getFightersName();
    public void Steer(){
        int pressedButtonId = getResources().getIdentifier("button" + array.get(getSteerRandom()), "id", getActivity().getPackageName());
        Button pressedButton = (Button) getView().findViewById(pressedButtonId);
        int indexOfLetter = fName.indexOf(pressedButton.getText().toString());
        Button resetButton = (Button) guessLinearLayouts[2].getChildAt(indexOfLetter);
        buttonsData.add(getCounter(), String.valueOf(pressedButtonId)); //ID кнопки снизу
        resetButton.setVisibility(View.VISIBLE);
        pressedButton.setVisibility(View.INVISIBLE);
        String buttonText = pressedButton.getText().toString();
        fName = fName.replaceFirst(buttonText, "*");
        resetButton.setText(String.valueOf(buttonText));
        if(getCounter() == (getFightersName().length() - 1))
        {
            checkName();
        }
        setCounter(getCounter() + 1);
    }


    public void checkName()
    {
        String temp = "";
        answerTextView.setVisibility(View.VISIBLE);
        Button checkButton;
            for (int i = 0; i < getFightersName().length(); i++) {
                checkButton = (Button) guessLinearLayouts[2].getChildAt(i);
                temp = temp + checkButton.getText().toString();
            }

            if (getFightersName().equalsIgnoreCase(temp)) {
                checkAnswer = true;
                answerTextView.setTextColor(getResources().getColor(R.color.correct_answer));
                answerTextView.setText("RIGHT");
                buttonNext.setVisibility(View.VISIBLE);
                buttonNext.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animate(true);
                            }
                        }, 1000);
                    }
                });
                /*if(fileNameList.size() != 0) {
//прописать условия в зависимости от оставшегося количества бойцов
                }*/
            }
            else {
                checkAnswer = false;
                answerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer));
                answerTextView.setText("FALSE");
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                fighterImageView.startAnimation(shakeAnimation);
            }
    }

    public void handleCancel(View v)
    {
        int downButtonId = Integer.valueOf(buttonsData.get(getCounter()-1));
        Button downButton = (Button) getView().findViewById(downButtonId);
        Button upButton = (Button) getView().findViewById(v.getId());
        downButton.setVisibility(View.VISIBLE); //To set visible
        upButton.setVisibility(View.INVISIBLE);
        String buttonText = upButton.getText().toString();
        downButton.setText(String.valueOf(buttonText));
        setCounter(getCounter() - 1);

    }



    private List<Integer> array = new ArrayList<>();
    public int getRandom() {

        int rnd = new Random().nextInt(12);
        while(array.contains(rnd))
        {
            rnd = new Random().nextInt(12);
        }
        array.add(rnd);
        return rnd;
    }

   private List<Integer> arraySteer = new ArrayList<>();
    public int getSteerRandom() {
        int rnd = new Random().nextInt(getFightersName().length()-1);
        while(arraySteer.contains(rnd))
        {
            rnd = new Random().nextInt(getFightersName().length());
        }
        arraySteer.add(rnd);
        return rnd;
    }


    private String fightersName;
    public String getFightersName(){
        return fightersName;
    }
    public void setFightersName(String name){
        this.fightersName = name;
    }


    public void deleteLetter(){ //метод для удаления одной буквы - подсказка. Нужно пользоваться методом getRandom() и сделать невидимой одну рандомную кнопку.
        int deletedButtonId = getResources().getIdentifier("button" + getRandom(), "id", getActivity().getPackageName());
        Button deletedButton = (Button) getView().findViewById(deletedButtonId);
        deletedButton.setVisibility(View.INVISIBLE);
    }

    public void resetQuiz(){
        AssetManager assets = getActivity().getAssets();
        fileNameList.clear();
        categoryList.add("FLYWEIGHT");
        categoryList.add("BANTAMWEIGHT");
        categoryList.add("FEATHERWEIGHT");
        categoryList.add("LIGHTWEIGHT");
        categoryList.add("WELTERWEIGHT");
        categoryList.add("MIDDLEWEIGHT");
        categoryList.add("LIGHTHEAVYWEIGHT");
        categoryList.add("HEAVYWEIGHT");
        try{
            for(String category : categoryList) {
                String[] paths = assets.list(category);

                for(String path : paths)
                    fileNameList.add(path.replace(".jpg", ""));
            }
        }
        catch (IOException exception) {
            Log.e(TAG, "Error loading image file names", exception);
        }
        loadNextFighter();
    }

    private void dropData() {
        buttonsData.clear();
        setCounter(0);
        buttonsData2.clear();
        for(int row = 0; row < 2; row++) {
            for (int b = 0; b < 7; b++) {
                Button nButton = (Button) guessLinearLayouts[row].getChildAt(b);
                nButton.setVisibility(View.VISIBLE);
            }
        }
        for(int i = 0; i < 12; i++) {
            Button b = (Button) guessLinearLayouts[2].getChildAt(i);
            b.setText("");
            b.setVisibility(View.INVISIBLE);
        }
        answerTextView.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        fightersNum = fightersNum + 1;
    }

    private void loadNextFighter(){
    dropData();


        String nextImage = fileNameList.get(10);
        fileNameList.remove(10);
        setFightersName(nextImage.substring(nextImage.indexOf('-') + 1, nextImage.length()));
        String category = nextImage.substring(0, nextImage.indexOf('-'));
        levelNameTextVeiw.setText(category);
        AssetManager assets = getActivity().getAssets();
        try(InputStream stream = assets.open(category + "/" + nextImage + ".jpg")){
            Drawable fighter = Drawable.createFromStream(stream, nextImage);
            fighterImageView.setImageDrawable(fighter);
            animate(false);
        }
        catch (IOException exception) {
            Log.e(TAG, "Error loading " + nextImage, exception);
        }

        char[] LastNameQuantity = getFightersName().toCharArray();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for(int row = 0; row < 2; row++) {
            for(int b = 0; b < 7; b ++) {
                Random r = new Random();
                int rand = r.nextInt(25);
                Button newButton = (Button) guessLinearLayouts[row].getChildAt(b);
                newButton.setVisibility(View.VISIBLE);
                newButton.setEnabled(true);
                newButton.setText(String.valueOf(alphabet[rand]));
            }
        }
        for(int i = 0; i < LastNameQuantity.length; i++) {
            int buttonRand = getRandom();
            int row;
            int buttonInRow;
            if(buttonRand < 7) {
                row = 0;
                buttonInRow = buttonRand;
            }
            else {
                row = 1;
                buttonInRow = buttonRand - 7;
            }
            Button b = (Button) guessLinearLayouts[row].getChildAt(buttonInRow);
            b.setEnabled(true);
            b.setText(String.valueOf(LastNameQuantity[i]));
        }
    }
// method
    private void animate(boolean animateOut){
        if(checkAnswer == false){
            return;
        }
        int centerX = (quizLinearLayout.getLeft() + quizLinearLayout.getRight()) / 2;
        int centerY = (quizLinearLayout.getTop() + quizLinearLayout.getBottom()) / 2;
        int radius = Math.max(quizLinearLayout.getWidth(), quizLinearLayout.getHeight());
        Animator animator;
        if(animateOut) {
            animator = ViewAnimationUtils.createCircularReveal(quizLinearLayout, centerX, centerY, radius, 0);
            animator.addListener(
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadNextFighter();
                        }
                    }
            );
        }
        else {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, 0, radius);
        }
        animator.setDuration(2000); //продолжительность анимации 2000 мс
        animator.start(); //начало анимации
    }
}

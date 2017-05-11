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
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Handler;
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
    private List<String> categoryList;

    private List<String> buttonsData = new ArrayList<>();
    private List<String> buttonsData2 = new ArrayList<>();
    private int fightersNumber;
    private Handler handler; //для задержки запуска следующего бойца. Но я думаю, он нам не понадобится, потому что переход будет осуществляться кликом.
    private Animation shakeAnimation; //анимация неправильного заполнения

    private LinearLayout quizLinearLayout; //Макет с quiz'ом
    private ImageView fighterImageView; //Изображение бойца
    private LinearLayout[] guessLinearLayouts; //Строки с кнопками и ячейками
    private TextView answerTextView;
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
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);
        buttonDelete = (Button) view.findViewById(R.id.buttonDelete);
        buttonSteer = (Button) view.findViewById(R.id.buttonSteer);
        buttonNext = (Button) view.findViewById(R.id.buttonNext);View.OnClickListener onClickButton = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.buttonSteer:
                        Steer();
                        break;
                    case R.id.buttonDelete:
                        deleteLetter();
                        break;
                }
            }
        };

        return view;
    }

    public void makeVisible(View v) {
        handleClicks(v, getCounter());
        setCounter(getCounter() + 1);
    }

    private boolean checkAnswer;

    public void handleClicks(View v, int counter) {
        int buttonId  = getResources().getIdentifier("cell" + 1, "id", getActivity().getPackageName());
        Button resetButton  = (Button) getView().findViewById(buttonId);
        for(int i = 0;i<12;i++) {
            if (resetButton.getVisibility() == View.VISIBLE) {
                buttonId  = getResources().getIdentifier("cell" + (i+1), "id", getActivity().getPackageName());
                resetButton = (Button) getView().findViewById(buttonId);
            }
            else {

            }
        }
        Button pressedButton = (Button) getView().findViewById(v.getId());
        buttonsData.add(counter, String.valueOf(v.getId())); // ID кнопки снизу
        buttonsData2.add(counter, String.valueOf(buttonId)); // ID кнопки сверху
        resetButton.setVisibility(View.VISIBLE); //To set visible
        pressedButton.setVisibility(View.INVISIBLE);
        String buttonText = pressedButton.getText().toString();
        resetButton.setText(String.valueOf(buttonText));
        if(counter == (getFightersName().length()-2)) {
            checkName();
        }
    }

    public void Steer(){
        int buttonId = getResources().getIdentifier("cell" + 1, "id", getActivity().getPackageName());
        Button resetButton = (Button) getView().findViewById(buttonId);
        for(int i = 0; i < 8; i++) {
            if(resetButton.getVisibility() == View.VISIBLE) {
                buttonId = getResources().getIdentifier("cell" + (i + 1), "id", getActivity().getPackageName());
                resetButton = (Button) getView().findViewById(buttonId);
            }
        }
        int pressedButtonId = getResources().getIdentifier("button" + array.get(getSteerRandom()), "id", getActivity().getPackageName());
        Button pressedButton = (Button) getView().findViewById(pressedButtonId);
        buttonsData.add(getCounter(), String.valueOf(pressedButtonId)); //ID кнопки снизу
        buttonsData2.add(getCounter(), String.valueOf(buttonId)); //ID кнопки снизу
        resetButton.setVisibility(View.VISIBLE);
        pressedButton.setVisibility(View.INVISIBLE);
        String buttonText = pressedButton.getText().toString();
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
        {
            for (int i = 0; i < getFightersName().length() - 1; i++) {
                int buttonId = getResources().getIdentifier("cell" + (i + 1), "id", getActivity().getPackageName());
                Button cellButton = (Button) getView().findViewById(buttonId);
                temp = temp + cellButton.getText().toString();

            }

            if (getFightersName().substring(1).equalsIgnoreCase(temp)) {
                checkAnswer = true;
                answerTextView.setText("RIGHT");
                animate(checkAnswer);

            }

        }
    }

    public void handleCancel(View v)
    {
        int downButtonId = Integer.valueOf(buttonsData.get(getCounter()-1));
        int upButtonId = Integer.valueOf(buttonsData2.get(getCounter()-1));
        Button downButton = (Button) getView().findViewById(downButtonId);
        Button upButton = (Button) getView().findViewById(v.getId());
        downButton.setVisibility(View.VISIBLE); //To set visible
        upButton.setVisibility(View.INVISIBLE);
        String buttonText = upButton.getText().toString();
        downButton.setText(String.valueOf(buttonText));
        setCounter(getCounter() - 1);

    }

    public void setLetters () {
        char[] LastNameQuantity = getFightersName().toCharArray();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < 14; i++)
        {
            Random r = new Random();
            int rand = r.nextInt(25);
            int buttonId = getResources().getIdentifier("button"+i, "id", getActivity().getPackageName());
            Button b = (Button) getView().findViewById(buttonId);
            b.setText(String.valueOf(alphabet[rand]));
        }
        for (int j = 1; j < LastNameQuantity.length; j++)
        {
            int buttonId = getResources().getIdentifier("button" + getRandom(), "id", getActivity().getPackageName());
            Button b = (Button) getView().findViewById(buttonId);
            b.setText(String.valueOf(LastNameQuantity[j]));

        }

    }

    private List<Integer> array = new ArrayList<>();
    public int getRandom() {

        int rnd = new Random().nextInt(13);
        while(array.contains(rnd))
        {
            rnd = new Random().nextInt(13);
        }
        array.add(rnd);
        return rnd;
    }

    private List<Integer> arraySteer = new ArrayList<>();
    public int getSteerRandom() {
        int rnd = new Random().nextInt(getFightersName().length());
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

    /*public void loadNamesOfFighters() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("FightersInfo/FLYWEIGHT/ListOfFighters.txt")));
            String fline;
            while ((fline = reader.readLine()) != null) {
                setFightersName(fline);
            }
        }catch (IOException ex) {

        } finally{
            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException ex){

                }
            }
        }
    }*/

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

    private void loadNextFighter(){
        String nextImage = fileNameList.remove(0);
        setFightersName(nextImage);
        AssetManager assets = getActivity().getAssets();
        try(InputStream stream = assets.open(categoryList.get((fightersNumber - (fightersNumber % 11)) / 11) + "/" + nextImage + ".jpg")){
            Drawable fighter = Drawable.createFromStream(stream, nextImage);
            fighterImageView.setImageDrawable(fighter);
            fightersNumber++;
            animate(false);
        }
        catch (IOException exception) {
            Log.e(TAG, "Error loading " + nextImage, exception);
        }
        Collections.shuffle(fileNameList);
    }

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
        animator.setDuration(500); //продолжительность анимации 500 мс
        animator.start(); //начало анимации
    }

}

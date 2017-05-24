package afdevelops.mmaquiz01;
/*Даже путь в тысячу ли начинается с первого шага.


千里之行始於足下。

 	— Глава 64, строка 12*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private boolean phoneDevice = true; //включение портретной ориентации



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK; //определение размера экрана
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
           screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            phoneDevice = false;
        }
        if(phoneDevice){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
            //запускает quiz
            MainActivityFragment quizFragment = (MainActivityFragment)
                    getSupportFragmentManager().findFragmentById(R.id.quizFragment);
            //playBruceBuffer();
            quizFragment.resetQuiz();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation; //получение ориентации аппарата
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        else{
            return false;
        }
    }

    /*private void playBruceBuffer()
    {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.bb);
        mp.start();

    }*/





}

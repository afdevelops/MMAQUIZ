package afdevelops.mmaquiz01;
/*Даже путь в тысячу ли начинается с первого шага.


千里之行始於足下。

 	— Глава 64, строка 12*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String CATEGORIES = "pref_categoriesToInclude"; //мб не нужна
    private boolean phoneDevice = true; //включение портретной ориентации
    private boolean preferencesChanged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        if(preferencesChanged){
            //запускает quiz после того, как изменились настройки(В КНИГЕ НАПИСАНО, ЧТО ЗАПУСКАЕТ ПОСЛЕ ИЗМЕНЕНИЯ НАСТРОЕК, НО НАСТРОЕК У НАС НЕТ, ПОЭТОМУ ЗАПУСКАЕТ В ЗАВИСИМОСТИ ОТ ПЕРЕМЕННОЙ fightersNumber(в блокноте записана как "l"))
            MainActivityFragment quizFragment = (MainActivityFragment)
                    getSupportFragmentManager().findFragmentById(R.id.quizFragment);
            quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.updateCategories(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.resetQuiz();
            preferencesChanged = false;  // когда игрок угадывает, изменить эту переменную и переменные номеров категорий и бойцов

        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }



}

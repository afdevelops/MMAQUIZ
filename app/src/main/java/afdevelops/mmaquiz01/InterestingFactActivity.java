package afdevelops.mmaquiz01;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

/*
*
*
* прописать эту активность в манифесте, если мы будем её использовать
* <activity
            android:name=".InterestingFactActivity"
            android:label="Facts"
            android:theme="@style/FullscreenTheme">
            <intent-filter>
                <action android:name="android.intent.action.InterestingFactsActivity" />

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>*/

public class InterestingFactActivity extends MainActivity {
    private TextView surnameTextView;
    private TextView categoryTextView;
    private Button nextFighterButton;
    MainActivityFragment quizFragment = (MainActivityFragment)
            getSupportFragmentManager().findFragmentById(R.id.quizFragment);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting_fact);

        surnameTextView = (TextView) findViewById(R.id.textViewSurname);
        categoryTextView = (TextView) findViewById(R.id.textViewCategory);
        nextFighterButton = (Button) findViewById(R.id.buttonNextFighter);
        View.OnClickListener onClickButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonNextFighter:
                        Intent intent = new Intent(getBaseContext(), MainActivityFragment.class);
                        startActivity(intent);
                        quizFragment.loadNextFighter();
                        break;
                }
            }
        };
        nextFighterButton.setOnClickListener(onClickButton);
        //changeText();
    }
    private void changeText() {

        categoryTextView.setText(quizFragment.getLevelName());
        surnameTextView.setText(quizFragment.getFightersName());
    }
}
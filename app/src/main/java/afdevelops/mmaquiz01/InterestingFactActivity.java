package afdevelops.mmaquiz01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InterestingFactActivity extends AppCompatActivity {
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
                        quizFragment.loadNextFighter();
                }
            }
        };
        changeText();
    }
    private void changeText() {
        categoryTextView.setText(quizFragment.getLevelName());
        surnameTextView.setText(quizFragment.getFightersName());
    }
}

package creativitysol.com.mawared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import creativitysol.com.mawared.helpers.FragmentStack;

public class MainActivity extends AppCompatActivity {

     FragmentStack fragmentStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack = new FragmentStack(this,getSupportFragmentManager(),R.id.main_container);
        fragmentStack.replace(new BlankFragment());

    }
}
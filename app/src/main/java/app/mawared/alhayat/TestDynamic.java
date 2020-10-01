package app.mawared.alhayat;

import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;

public class TestDynamic extends Fragment {

    View v;
    LinearLayout linearLayout;
    ArrayList<View> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_test_dynamic, container, false);

        linearLayout = v.findViewById(R.id.parent);


        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < 5; i++) {
            if (i % 2 == 0) {
                RadioButton tv = new RadioButton(getActivity());
                tv.setLayoutParams(lparams);
                tv.setText("test" + i);
                tv.setId(ViewCompat.generateViewId());
                linearLayout.addView(tv);
                arrayList.add(tv);
            } else {
                EditText tv = new EditText(getActivity());
                tv.setLayoutParams(lparams);
                tv.setId(ViewCompat.generateViewId());
                linearLayout.addView(tv);
                arrayList.add(tv);
            }
        }


        Button tv = new Button(getActivity());
        tv.setLayoutParams(lparams);
        tv.setText("clickkkkkk");
        tv.setId(ViewCompat.generateViewId());
        linearLayout.addView(tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (View vv : arrayList) {
                    if (vv instanceof EditText) {
                        Log.d("VIEW IS:", "edittext " + ((EditText) vv).getText().toString());
                    } else if (vv instanceof RadioButton) {
                        if (((RadioButton) vv).isChecked())
                            Log.d("VIEW IS:", "radio checked " + ((RadioButton) vv).getText().toString());
                        else
                            Log.d("VIEW IS:", "radio not checked " + ((RadioButton) vv).getText().toString());

                    }
                }
            }
        });

        return v;
    }
}
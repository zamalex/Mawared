package app.mawared.alhayat.registeration.terms;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import app.mawared.alhayat.R;
import app.mawared.alhayat.registeration.terms.model.Terms;

public class TermsBottomSheet extends BottomSheetDialogFragment {

    Dialog dialog;
    TextView tv_termsTitle, tv_termsContent;
    TermsViewModel termsViewModel;

    ConstraintLayout btn_confirmTerms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.terms_bottom_sheet, container, false);
        Configuration config = new Configuration();
        getContext().getResources().updateConfiguration(config,
                getContext().getResources().getDisplayMetrics());
        tv_termsTitle = view.findViewById(R.id.tv_termsTitle);
        btn_confirmTerms = view.findViewById(R.id.btn_confirmTerms);
        tv_termsContent = view.findViewById(R.id.tv_termsContent);
        termsViewModel = new ViewModelProvider(this).get(TermsViewModel.class);
        termsViewModel.getTermsAndConditions("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNTk4ODIzMDExLCJleHAiOjE1OTk0Mjc4MTEsIm5iZiI6MTU5ODgyMzAxMSwianRpIjoiVGpBYTRHRnk4UUJjYURLbCJ9.SsbMQcp3lV8kUPmRyC5g1oTJStskrlwj4URiXfJ8oIo")
                .observe(getActivity(), new Observer<Terms>() {
                    @Override
                    public void onChanged(Terms terms) {
                        if (terms.getStatus() == 200) {
                            tv_termsTitle.setText(terms.getData().getTitle());
                            String content = terms.getData().getContent();
                            tv_termsContent.setText(HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY));
                            tv_termsContent.setMovementMethod(new ScrollingMovementMethod());
                        }
                    }

                });


        btn_confirmTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupRatio(bottomSheetDialog);
            }
        });
        return dialog;
    }

    private void setupRatio(BottomSheetDialog bottomSheetDialog) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        FrameLayout bottomSheet = (FrameLayout)
                bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = getBottomSheetDialogDefaultHeight();
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getBottomSheetDialogDefaultHeight() {
        return getWindowHeight() * 85 / 100;
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}

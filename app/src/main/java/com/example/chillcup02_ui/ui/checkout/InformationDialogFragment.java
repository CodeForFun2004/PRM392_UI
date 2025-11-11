package com.example.chillcup02_ui.ui.checkout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.chillcup02_ui.R;

public class InformationDialogFragment extends DialogFragment {

    public interface InfoDialogListener {
        void onInfoSaved(String field, String value);
    }

    private static final String ARG_FIELD = "arg_field";
    private static final String ARG_VALUE = "arg_value";
    private String field;
    private String currentValue;
    private InfoDialogListener listener;

    public static InformationDialogFragment newInstance(String field, String currentValue) {
        InformationDialogFragment f = new InformationDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_FIELD, field);
        b.putString(ARG_VALUE, currentValue);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof InfoDialogListener) {
            listener = (InfoDialogListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_information, container, false);
        if (getArguments() != null) {
            field = getArguments().getString(ARG_FIELD);
            currentValue = getArguments().getString(ARG_VALUE);
        }

        TextView tvTitle = v.findViewById(R.id.tvDialogTitle);
        final EditText etValue = v.findViewById(R.id.etDialogValue);
        Button btnCancel = v.findViewById(R.id.btnDialogCancel);
        Button btnSave = v.findViewById(R.id.btnDialogSave);

        tvTitle.setText(field != null && field.equals("phone") ? "Edit Phone Number" : "Edit Location");
        etValue.setText(currentValue != null ? currentValue : "");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v12) {
                String val = etValue.getText().toString().trim();
                if (TextUtils.isEmpty(val)) return;
                if (listener != null) listener.onInfoSaved(field, val);
                dismiss();
            }
        });

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setCanceledOnTouchOutside(true);
        return d;
    }
}

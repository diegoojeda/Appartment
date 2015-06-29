package com.uma.tfg.appartment.views;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andreabaccega.formedittextvalidator.Validator;
import com.andreabaccega.widget.EditTextValidator;
import com.andreabaccega.widget.FormEditText;
import com.uma.tfg.appartment.R;
import com.uma.tfg.appartment.util.EmailValidator;

import java.util.ArrayList;
import java.util.List;

public class MultiEmailAdderView extends LinearLayout implements View.OnClickListener{

    private List<FormEditText> mFormEditTextsList;

    private Button mEmailAdderButton;
    private LinearLayout mLayoutWhereEditTextsAreAdded;
    

    public MultiEmailAdderView(Context context) {
        super(context);
        init();
        initViews();
    }

    public MultiEmailAdderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initViews();
    }

    private void init(){
        inflate(getContext(), R.layout.layout_multi_email_adder_view, this);
    }

    private void initViews() {
        mFormEditTextsList = new ArrayList<>();

        mLayoutWhereEditTextsAreAdded = (LinearLayout) findViewById(R.id.layout_email_integrantes_grupo);
        mEmailAdderButton = (Button) findViewById(R.id.add_more_edit_text_fields_for_emails_button);

        mEmailAdderButton.setOnClickListener(this);

        addOneMoreFieldToEmailFieldsList();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_more_edit_text_fields_for_emails_button:
                onAddMoreFieldsButtonClicked();
                break;
        }
    }

    private void onAddMoreFieldsButtonClicked() {
        addOneMoreFieldToEmailFieldsList();
    }

    private void addOneMoreFieldToEmailFieldsListGivenText(String email){
        addOneMoreFieldToEmailFieldsList();
        mFormEditTextsList.get(mFormEditTextsList.size()-1).setText(email);
    }

    private void addOneMoreFieldToEmailFieldsList() {
        final FormEditText fet = new FormEditText(getContext());
        fet.addValidator(new EmailValidator());
        fet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        fet.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    fet.testValidity();
                }
            }
        });
        mFormEditTextsList.add(fet);
        mLayoutWhereEditTextsAreAdded.addView(mFormEditTextsList.get(mFormEditTextsList.size() - 1));
    }

    public ArrayList<String> getEmailList() {
        ArrayList<String> emailList = new ArrayList<>();
        for (FormEditText fet : mFormEditTextsList) {
            if (!TextUtils.isEmpty(fet.getText().toString())) {
                emailList.add(fet.getText().toString());
            }
        }
        return emailList;
    }

    public void initWithEmailsList(List<String> emails){
        for (String s : emails){
            addOneMoreFieldToEmailFieldsListGivenText(s);
        }
    }

    public boolean validateAllEmails() {
        boolean areAllValid = true;
        for (FormEditText fet : mFormEditTextsList){
            //Tomaremos la cadena vacía como un "FormEditText" válido, aunque luego no se devolverá en la lista de e-mails
            if (fet.getText().length() == 0){
                areAllValid &= true;
            }
            else {
                areAllValid = fet.testValidity() && areAllValid;
            }
        }
        return areAllValid;
    }
}

package com.uma.tfg.appartment.util;

import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

public class EmailValidator extends Validator{
    public EmailValidator() {
        super("Introduzca un e-mail válido");
    }

    @Override
    public boolean isValid(EditText editText) {
        if (editText.getText().length() == 0){
            return true;
        }
        return Util.isValidEmail(editText.getText().toString());
    }
}

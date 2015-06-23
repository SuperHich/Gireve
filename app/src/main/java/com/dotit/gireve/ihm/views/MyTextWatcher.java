package com.dotit.gireve.ihm.views;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Hichem Laroussi @SH on 06/04/2015.
 */
public class MyTextWatcher implements TextWatcher {

    private int id;
    private String oldText;
    private ITextWatcherListener listener;

    private EditText editText;

    public MyTextWatcher(int id, String oldText, ITextWatcherListener listener){
        this.id = id;
        this.oldText = oldText;
        this.listener = listener;
    }

    public MyTextWatcher(EditText editText){
        this.editText = editText;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.e("TAG", ">>> id = " + id + " ... s.toString() = " + s.toString());
//        Log.e("TAG", ">>> id = " + id + " ... oldText = " + oldText + " ... s.toString() = " + s.toString());
        listener.onTextChanged(id, s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.e("TAG", ">>> editText = " + editText.getText().toString());
        editText.clearFocus();
    }
}


package com.example.mjmtools;


public class Tools {
    public static void setEditTextEditionFinishedAction(EditText edit,
            final RunnableWithString runnable) {
        edit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edit.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    runnable.run(v.getText().toString());
                    return false;
                }
                return false;
            }
        });
        edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) runnable.run(((EditText) v).getText().toString());
            }
        });
    }
}

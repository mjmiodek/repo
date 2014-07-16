
package com.example.mjmtools;

public class RunnableWithString implements Runnable {
    String mParam;

    @Override
    public void run() {
    }

    public void run(String s) {
        mParam = s;
        run();
    }
}

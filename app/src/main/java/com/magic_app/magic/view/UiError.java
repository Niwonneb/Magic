package com.magic_app.magic.view;

import android.content.Context;
import android.widget.Toast;

import com.magic_app.magic.apiservice.errorhandling.RequestError;
import com.magic_app.magic.util.Debounce;

public class UiError {
    private final static Debounce debounce = new Debounce(1000);

    public static boolean showKnownError(Context context, Throwable error) {
        if (error instanceof RequestError &&
                ((RequestError) error).getType() == RequestError.RequestErrorType.ServerUnreachable) {
            if (!debounce.calledRecently()) {
                Toast.makeText(context, "Kein Internet :(", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    public static void showError(Context context, Throwable error) {
        if (!showKnownError(context, error)) {
            Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void showError(Context context, Throwable error, String message) {
        if (!showKnownError(context, error)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
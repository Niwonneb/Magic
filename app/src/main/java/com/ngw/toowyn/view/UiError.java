package com.ngw.toowyn.view;

import android.content.Context;
import android.widget.Toast;

import com.ngw.toowyn.apiservice.errorhandling.RequestError;
import com.ngw.toowyn.util.Debounce;

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

    public static void showError(Context context, Throwable error, int duration) {
        if (!showKnownError(context, error)) {
            Toast.makeText(context, "Error: " + error.getMessage(), duration).show();
        }
    }

    public static void showError(Context context, Throwable error) {
        showError(context, error, Toast.LENGTH_SHORT);
    }

    public static void showError(Context context, Throwable error, String message) {
        if (!showKnownError(context, error)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
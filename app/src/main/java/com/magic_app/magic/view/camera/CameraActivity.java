package com.magic_app.magic.view.camera;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.magic_app.magic.R;


public class CameraActivity extends Activity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    public static final String TAG = CameraActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.squarecamera__activity_camera);

        requestForCameraPermission();
//        if (savedInstanceState == null) {
//            getFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, SquareCameraFragment.newInstance(), SquareCameraFragment.TAG)
//                    .commit();
//        }
    }

    public void requestForCameraPermission() {
        final String permission = Manifest.permission.CAMERA;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission)
                    != PackageManager.PERMISSION_GRANTED) {
                requestForPermission(permission);
            } else {
                startCamera();
            }
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(CameraActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CameraActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{permission}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    startCamera();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startCamera() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, SquareCameraFragment.newInstance(), SquareCameraFragment.TAG)
                .commit();

    }

    public void onCancel(View view) {
        getFragmentManager().popBackStack();
    }
}

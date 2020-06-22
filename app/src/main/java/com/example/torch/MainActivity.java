package com.example.torch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    boolean isTorchON;
    final int PERMISSION_CODE = 786;
    boolean isPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.imgBtn_ID);

        isFlashAvailable();
        checkPermissions();

        isTorchON = false;


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPermissionGranted)
                {
                    if(isTorchON)
                    {
                        turnOffTorch(v);
                        isTorchON = false;
                    }
                    else
                    {
                        turnOnTorch(v);
                        isTorchON = true;
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void isFlashAvailable() {

        boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!isFlashAvailable)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error!!!");
            alertDialog.setMessage("Flashlight Not Available");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            alertDialog.show();
            return;
        }
    }

    private void checkPermissions() {

        String[] permission_Array = {Manifest.permission.CAMERA};
        final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            isPermissionGranted = true;
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,permission_Array,PERMISSION_CODE);
        }
    }

    private void turnOffTorch(View v) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraID = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraID,false);
                v.setBackgroundResource(R.drawable.btn_off);
            } catch (CameraAccessException e) {
            }
        }


    }

    private void turnOnTorch(View v) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraID = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraID,true);
                v.setBackgroundResource(R.drawable.btn_on);
            } catch (CameraAccessException e) {

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    isPermissionGranted = true;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}

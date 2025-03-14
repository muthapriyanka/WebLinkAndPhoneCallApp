package com.example.weblinkandphonecallapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etPhoneNumber, edtWebLink;
    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button btnCall = findViewById(R.id.btnCall);

        edtWebLink = findViewById(R.id.edtWebLink);
        Button btnOpenWeb = findViewById(R.id.btnOpenWeb);

        Button btnCloseApp = findViewById(R.id.btnCloseApp);

        // Make a call when the button is clicked
        btnCall.setOnClickListener(v -> makePhoneCall());

        btnOpenWeb.setOnClickListener(v -> openWebsite());

        btnCloseApp.setOnClickListener(v -> closeApp());

    }

    // Method to make a direct phone call
    private void makePhoneCall() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        // Validate the phone number (only digits, length check)
        if (phoneNumber.isEmpty() || !phoneNumber.matches("^\\+?\\d{7,15}$")) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } else {
            // Request CALL_PHONE permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

    // Method to open a website in browser
    private void openWebsite() {
        String url = edtWebLink.getText().toString().trim();
        if (!url.isEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url; // Ensure proper format
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Enter a valid URL", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(); // Try calling again now that permission is granted
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void closeApp() {
        finishAffinity(); // Closes the entire app
    }
}
package algonquin.cst2335.pund0006;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private EditText phNumEditText;
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        phNumEditText = findViewById(R.id.editTextPhone);

        Button callButton = findViewById(R.id.callButton);
        Button changePicture = findViewById(R.id.button3);
        ImageView profileImage = findViewById(R.id.imageView);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phNumEditText.getText().toString();

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));

                startActivity(callIntent);
            }
        });
        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        profileImage.setImageBitmap(thumbnail);

                        FileOutputStream fOut = null;
                        try {
                            fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fOut != null) {
                                try {
                                    fOut.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                cameraResultLauncher.launch(cameraIntent);
            }
        });
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("PhoneNumber", "");
        phNumEditText.setText(savedPhoneNumber);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        TextView welcomeText = findViewById(R.id.textView);

        welcomeText.setText("Welcome Back  " + emailAddress);
    }
        @Override
                protected void onPause()
        {
            super.onPause();
            String phoneNumber = phNumEditText.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences("MyData", Context.MODE_PRIVATE).edit();
            editor.putString("PhoneNumber", phoneNumber);
            editor.apply();
        }
        }






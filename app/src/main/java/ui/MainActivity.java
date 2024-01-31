package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import algonquin.cst2335.pund0006.databinding.ActivityMainBinding;
import ui.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model= new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

       // variableBinding.textview.setText(model.editString);

        variableBinding.myButton.setOnClickListener(click -> {

             model.editString.postValue(variableBinding.myedittext.getText().toString());
                                      });

        model.editString.observe(this, s ->
        {
            variableBinding.myedittext.setText("Your edit text has "+ s);

            });
        }

    }

}
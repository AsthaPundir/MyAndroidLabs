package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.pund0006.databinding.ActivityMainBinding;
import ui.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;

    private ActivityMainBinding variableBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.buttonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int width = v.getWidth();
                int height= v.getHeight();

                showToast("The width =" + width + " and height =" + height);
            }
        });

        // variableBinding.textview.setText(model.editString);

        variableBinding.myButton.setOnClickListener(click -> {

            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });


        model.isSelected.observe(this, selected -> {
            variableBinding.checkBoxCoffee.setChecked(selected);
            variableBinding.radioButtonCoffee.setChecked(selected);
            variableBinding.switchCoffee.setChecked(selected);

            CharSequence text = "The value is now: " + selected;
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(this, text, duration).show();
        });

        variableBinding.checkBoxCoffee.setOnCheckedChangeListener((buttonView, isChecked) ->
                model.isSelected.postValue(isChecked));

        variableBinding.radioButtonCoffee.setOnCheckedChangeListener((buttonView, isChecked) ->
                model.isSelected.postValue(isChecked));

        variableBinding.switchCoffee.setOnCheckedChangeListener((buttonView, isChecked) ->
                model.isSelected.postValue(isChecked));

        model.editString.observe(this, s ->
        {
            variableBinding.myedittext.setText("Your edit text has " + s);

        });


    }

    private void showToast(String s)
    {
        Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    ;


}
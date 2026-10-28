package com.example.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class AddContactActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etPhone;
    private EditText etNote;
    private Spinner spGroup;
    private RadioGroup rgGender;
    private CheckBox cbFavorite;
    private ArrayList<String> existingPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etNote = findViewById(R.id.etNote);
        spGroup = findViewById(R.id.spGroup);
        rgGender = findViewById(R.id.rgGender);
        cbFavorite = findViewById(R.id.cbFavorite);
        existingPhones = getIntent().getStringArrayListExtra("existing_phones");
        if (existingPhones == null) {
            existingPhones = new ArrayList<>();
        }

        String[] groups = getResources().getStringArray(R.array.contact_groups);
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(groups));
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGroup.setAdapter(groupAdapter);

        TextView btnToolbarBack = findViewById(R.id.btnToolbarBack);
        Button btnSave = findViewById(R.id.btnSave);
        btnToolbarBack.setOnClickListener(view -> finish());
        btnSave.setOnClickListener(view -> saveContact());
    }

    private void saveContact() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError(getString(R.string.name_required));
            etName.requestFocus();
            Toast.makeText(this, R.string.name_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            etPhone.setError(getString(R.string.phone_invalid));
            etPhone.requestFocus();
            Toast.makeText(this, R.string.phone_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        if (existingPhones.contains(phone)) {
            etPhone.setError(getString(R.string.phone_duplicate));
            etPhone.requestFocus();
            Toast.makeText(this, R.string.phone_duplicate, Toast.LENGTH_SHORT).show();
            return;
        }

        int checkedGenderId = rgGender.getCheckedRadioButtonId();
        TextView selectedGender = findViewById(checkedGenderId);
        String gender = selectedGender == null
                ? getString(R.string.gender_male)
                : selectedGender.getText().toString();
        Contact contact = new Contact(name, phone, spGroup.getSelectedItem().toString(),
                gender, cbFavorite.isChecked(), etNote.getText().toString().trim());

        Intent result = new Intent();
        result.putExtra("contact", contact);
        setResult(RESULT_OK, result);
        finish();
    }
}

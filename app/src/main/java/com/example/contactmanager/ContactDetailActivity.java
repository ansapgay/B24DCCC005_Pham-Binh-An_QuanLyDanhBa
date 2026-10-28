package com.example.contactmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailActivity extends AppCompatActivity {
    private static final String EXTRA_CONTACT = "contact";
    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_ACTION = "action";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    private Contact contact;
    private int position;
    private CheckBox cbDetailFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contact = (Contact) getIntent().getSerializableExtra(EXTRA_CONTACT);
        position = getIntent().getIntExtra(EXTRA_POSITION, -1);
        if (contact == null || position < 0) {
            finish();
            return;
        }

        TextView tvAvatar = findViewById(R.id.tvDetailAvatar);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPhone = findViewById(R.id.tvDetailPhone);
        TextView tvGroup = findViewById(R.id.tvDetailGroup);
        TextView tvGender = findViewById(R.id.tvDetailGender);
        TextView tvNote = findViewById(R.id.tvDetailNote);
        cbDetailFavorite = findViewById(R.id.cbDetailFavorite);

        tvAvatar.setText(contact.getInitial());
        tvName.setText(contact.getName());
        tvPhone.setText(contact.getPhone());
        tvGroup.setText(getString(R.string.detail_group, contact.getGroup()));
        tvGender.setText(getString(R.string.detail_gender, contact.getGender()));
        String note = contact.getNote() == null || contact.getNote().trim().isEmpty()
                ? getString(R.string.no_note) : contact.getNote();
        tvNote.setText(note);
        cbDetailFavorite.setChecked(contact.isFavorite());
        cbDetailFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contact.setFavorite(isChecked);
            updateResult(false);
        });

        TextView btnToolbarBack = findViewById(R.id.btnDetailToolbarBack);
        Button btnCall = findViewById(R.id.btnCall);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishWithResult();
            }
        });
        btnToolbarBack.setOnClickListener(view -> finishWithResult());
        btnBack.setOnClickListener(view -> finishWithResult());
        btnCall.setOnClickListener(view -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhone()));
            startActivity(dialIntent);
        });
        btnDelete.setOnClickListener(view -> confirmDelete());
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirmation_title)
                .setMessage(R.string.delete_confirmation_message)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    updateResult(true);
                    finish();
                })
                .show();
    }

    private void updateResult(boolean delete) {
        Intent result = new Intent();
        result.putExtra(EXTRA_POSITION, position);
        if (delete) {
            result.putExtra(EXTRA_ACTION, ACTION_DELETE);
        } else {
            result.putExtra("is_favorite", contact.isFavorite());
        }
        setResult(RESULT_OK, result);
    }

    private void finishWithResult() {
        updateResult(false);
        finish();
    }

}

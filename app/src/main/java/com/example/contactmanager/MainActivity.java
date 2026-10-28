package com.example.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String STATE_CONTACTS = "state_contacts";
    private static final String EXTRA_CONTACT = "contact";
    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_ACTION = "action";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    private final ArrayList<Contact> contacts = new ArrayList<>();
    private ContactAdapter adapter;
    private TextView tvTotalCount;
    private TextView tvFavoriteCount;
    private ActivityResultLauncher<Intent> addContactLauncher;
    private ActivityResultLauncher<Intent> detailLauncher;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        ListView listContacts = findViewById(R.id.listContacts);
        Button btnAddContact = findViewById(R.id.btnAddContact);

        if (savedInstanceState != null) {
            ArrayList<Contact> savedContacts =
                    (ArrayList<Contact>) savedInstanceState.getSerializable(STATE_CONTACTS);
            if (savedContacts != null) {
                contacts.addAll(savedContacts);
            }
        } else {
            initializeSampleContacts();
        }

        adapter = new ContactAdapter(this, contacts);
        listContacts.setAdapter(adapter);

        addContactLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Contact contact = (Contact) result.getData().getSerializableExtra(EXTRA_CONTACT);
                        if (contact != null) {
                            contacts.add(contact);
                            refreshContacts();
                        }
                    }
                });

        detailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleDetailResult(result.getResultCode(), result.getData()));

        btnAddContact.setOnClickListener(view -> openAddContact());
        listContacts.setOnItemClickListener((parent, view, position, id) -> openContactDetail(position));
        updateStatistics();
    }

    private void initializeSampleContacts() {
        contacts.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam",
                false, "Bạn đại học"));
        contacts.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ",
                true, "Chị gái, hay gọi buổi tối"));
        contacts.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nam",
                false, "Đối tác dự án"));
    }

    private void openAddContact() {
        Intent intent = new Intent(this, AddContactActivity.class);
        ArrayList<String> existingPhones = new ArrayList<>();
        for (Contact contact : contacts) {
            existingPhones.add(contact.getPhone());
        }
        intent.putStringArrayListExtra("existing_phones", existingPhones);
        addContactLauncher.launch(intent);
    }

    private void openContactDetail(int position) {
        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT, contacts.get(position));
        intent.putExtra(EXTRA_POSITION, position);
        detailLauncher.launch(intent);
    }

    private void handleDetailResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        int position = data.getIntExtra(EXTRA_POSITION, -1);
        if (position < 0 || position >= contacts.size()) {
            return;
        }

        if (ACTION_DELETE.equals(data.getStringExtra(EXTRA_ACTION))) {
            contacts.remove(position);
            Toast.makeText(this, R.string.contact_deleted, Toast.LENGTH_SHORT).show();
        } else if (data.hasExtra("is_favorite")) {
            contacts.get(position).setFavorite(data.getBooleanExtra("is_favorite", false));
        }
        refreshContacts();
    }

    private void refreshContacts() {
        adapter.notifyDataSetChanged();
        updateStatistics();
    }

    private void updateStatistics() {
        int favoriteCount = 0;
        for (Contact contact : contacts) {
            if (contact.isFavorite()) {
                favoriteCount++;
            }
        }
        tvTotalCount.setText(getString(R.string.total_count, contacts.size()));
        tvFavoriteCount.setText(getString(R.string.favorite_count, favoriteCount));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_CONTACTS, contacts);
        super.onSaveInstanceState(outState);
    }
}

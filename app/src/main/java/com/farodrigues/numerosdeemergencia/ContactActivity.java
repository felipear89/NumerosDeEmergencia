package com.farodrigues.numerosdeemergencia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.farodrigues.numerosdeemergencia.dao.ContactDao;
import com.farodrigues.numerosdeemergencia.model.Contact;
import com.melnykov.fab.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private ListView listViewContacts;

    private FloatingActionButton fab;
    private ContactAdapter<Contact> contactAdapter;
    private ContactDao contactDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contactDao = new ContactDao(this);

        fab = (FloatingActionButton) findViewById(R.id.fab_new_contact);
        listViewContacts = (ListView) findViewById(R.id.listView_contacts);

        fab.attachToListView(listViewContacts);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditContactActivity.class);
                startActivity(intent);
            }
        });

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Contact selectedContact = (Contact) contactAdapter.getItem(position);

                List<String> options = Arrays.asList(getString(R.string.update), getString(R.string.delete));
                final ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, options);

                AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this, R.style.AppThemeDark);
                builder.setSingleChoiceItems(itemsAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = itemsAdapter.getItem(which);
                        if (item.equals(getString(R.string.delete))) {
                            contactDao.delete(selectedContact.getId());
                            contactAdapter.remove(selectedContact);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), EditContactActivity.class);
                            intent.putExtra(EditContactActivity.PARAM_CONTACT, selectedContact);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        updateContactsView();
    }

    @NonNull
    private ContactDao updateContactsView() {
        contactAdapter = new ContactAdapter<>(this, android.R.layout.simple_list_item_1, contactDao.list());
        listViewContacts.setAdapter(contactAdapter);
        return contactDao;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateContactsView();
    }

    public class ContactAdapter<T> extends ArrayAdapter {

        public ContactAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
        }
    }
}

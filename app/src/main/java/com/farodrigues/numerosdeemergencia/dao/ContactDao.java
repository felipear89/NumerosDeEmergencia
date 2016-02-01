package com.farodrigues.numerosdeemergencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.farodrigues.numerosdeemergencia.database.DatabaseHelper;
import com.farodrigues.numerosdeemergencia.model.Contact;
import com.farodrigues.numerosdeemergencia.model.Contact.ContactDefinition;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public ContactDao(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void insert(Contact contact) {
        ContentValues values = getContentValues(contact);
        db.insert(ContactDefinition.TABLE_NAME, null, values);
    }

    @NonNull
    private ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactDefinition.COLUMN_NAME, contact.getName());
        values.put(ContactDefinition.COLUMN_NUMBER, contact.getNumber());
        return values;
    }

    public void update(Contact contact) {
        ContentValues values = getContentValues(contact);
        db.update(ContactDefinition.TABLE_NAME, values, ContactDefinition._ID + "= ?", new String[]{contact.getId().toString()});
    }

    public List<Contact> list() {
        String[] projection = {
                ContactDefinition._ID,
                ContactDefinition.COLUMN_NAME,
                ContactDefinition.COLUMN_NUMBER
        };

        Cursor cursor = db.query(
                ContactDefinition.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(ContactDefinition._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactDefinition.COLUMN_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactDefinition.COLUMN_NUMBER));
                contacts.add(new Contact(id, name, number));
            } while(cursor.moveToNext());
        }

        return contacts;
    }

    public void delete(Long id) {
        db.delete(ContactDefinition.TABLE_NAME, ContactDefinition._ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void save(Contact contact) {
        if (contact.getId() != null) {
            update(contact);
        } else {
            insert(contact);
        }
    }
}

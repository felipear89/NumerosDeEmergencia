package com.farodrigues.numerosdeemergencia.model;

import android.provider.BaseColumns;

import java.io.Serializable;

public class Contact implements Serializable {

    private Long id;
    private String name;
    private String number;

    public Contact(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public Contact() {
    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Contact) {
            return ((Contact) o).getName().equals(name);
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public static abstract class ContactDefinition implements BaseColumns {

        public static final String TABLE_NAME = "contact";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NUMBER = "number";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + " (" + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME +  " TEXT, "
                + COLUMN_NUMBER + " TEXT "
                + ")";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

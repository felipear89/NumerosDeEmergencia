package com.farodrigues.numerosdeemergencia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.farodrigues.numerosdeemergencia.dao.ContactDao;
import com.farodrigues.numerosdeemergencia.model.Contact;
import com.farodrigues.numerosdeemergencia.view.MaskEditTextChangedListener;

import java.io.Serializable;

public class EditContactActivity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputPhone;
    public static final String PARAM_CONTACT = "PARAM_CONTACT";
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        this.inputName = (EditText) findViewById(R.id.editText_contact_name);
        this.inputPhone = (EditText) findViewById(R.id.editText_contact_phone);
        inputPhone.addTextChangedListener(new MaskEditTextChangedListener("(##) #####-####", inputPhone));
        fillForm();
    }

    public void showMessage(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void fillForm() {
        if (getIntent().getSerializableExtra(PARAM_CONTACT) != null) {
            this.contact = (Contact) getIntent().getSerializableExtra(PARAM_CONTACT);
            this.inputName.setText(contact.getName());
            this.inputPhone.setText(contact.getNumber());
        }
    }

    public void onClickSave(View view) {
        if (this.inputName.getText().toString().isEmpty() || this.inputPhone.getText().toString().isEmpty()) {
            showMessage("Preencha todos os campos");
            return;
        }
        ContactDao contactDao = new ContactDao(this);
        contactDao.save(getContactFromView());
        getIntent().setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        finish();
    }

    private Contact getContactFromView() {
        String name = this.inputName.getText().toString();
        String phone = this.inputPhone.getText().toString();
        if (contact == null) {
            contact = new Contact();
        }
        contact.setName(name);
        contact.setNumber(phone);
        return contact;
    }
}

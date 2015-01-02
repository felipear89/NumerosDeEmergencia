package com.farodrigues.numerosdeemergencia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    ListView listNumerosDeEmergencia;
    Button btnGetLocation;

    NumeroDeEmergenciaAdapter<NumeroDeEmergencia> listAdapterNumerosDeEmergencia;

    ResourceNumerosDeEmergencia resourceNumerosDeEmergencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listNumerosDeEmergencia = (ListView) findViewById(R.id.list_emergency_numbers);
        btnGetLocation = (Button) findViewById(R.id.btn_get_location);


        resourceNumerosDeEmergencia = new ResourceNumerosDeEmergencia();

        handleListNumerosDeEmergencia();
        handleBtnGetLocation();

    }

    private void handleBtnGetLocation() {
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    private void handleListNumerosDeEmergencia() {

        listAdapterNumerosDeEmergencia = new NumeroDeEmergenciaAdapter<NumeroDeEmergencia>(this,
                android.R.layout.simple_list_item_1,
                resourceNumerosDeEmergencia.getNumerosDeEmergenciaBrasileiros());

        listNumerosDeEmergencia.setAdapter(listAdapterNumerosDeEmergencia);

        listNumerosDeEmergencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NumeroDeEmergencia numeroDeEmergenciaSelecionado = (NumeroDeEmergencia) parent.getItemAtPosition(position);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + numeroDeEmergenciaSelecionado.getNumero()));
                startActivity(callIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NumeroDeEmergenciaAdapter<T> extends ArrayAdapter {

        public NumeroDeEmergenciaAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
        }
    }

}

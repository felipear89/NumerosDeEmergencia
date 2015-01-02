package com.farodrigues.numerosdeemergencia;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    ListView listNumerosDeEmergencia;
    Button btnGetLocation;
    TextView txtCurrentLocation;

    NumeroDeEmergenciaAdapter<NumeroDeEmergencia> listAdapterNumerosDeEmergencia;

    ResourceNumerosDeEmergencia resourceNumerosDeEmergencia;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listNumerosDeEmergencia = (ListView) findViewById(R.id.list_emergency_numbers);
        btnGetLocation = (Button) findViewById(R.id.btn_get_location);
        txtCurrentLocation = (TextView) findViewById(R.id.txt_current_location);

        resourceNumerosDeEmergencia = new ResourceNumerosDeEmergencia();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        handleListNumerosDeEmergencia();
        handleBtnGetLocation();
        handleCurrentLocationListener(locationManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        handleBtnGetLocation();
    }

    private void handleCurrentLocationListener(final LocationManager locationManager) {

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                (new GetAddressTask(getApplicationContext())).execute(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {
                System.out.println("Desativado");
                /** TODO Exibir alerta para habilitar GPS */
            }
        };

        // Register the listener with the Location Manager to receive location updates
        float minDistanceInMeters = 50;
        long minTimeInMillis = 30l * 1000L;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeInMillis, minDistanceInMeters, locationListener);
    }

    private void handleBtnGetLocation() {

        if (isGpsEnabled()) {
            btnGetLocation.setVisibility(View.INVISIBLE);
        } else {
            btnGetLocation.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
        }
    }

    private boolean isGpsEnabled() {
        return getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER);
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

    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(Location... params) {


            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            Location location = params[0];

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String addressText = String.format(
                            "%s, %s, %s",
                            // If there's a street address, add it
                            address.getMaxAddressLineIndex() > 0 ?
                                    address.getAddressLine(0) : "",
                            // Locality is usually a city
                            address.getLocality(),
                            // The country of the address
                            address.getCountryName());
                    return addressText;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return "";
        }

        @Override
        protected void onPostExecute(String address) {
            txtCurrentLocation.setText(address);
        }
    }

    public LocationManager getLocationManager() {
        if (this.locationManager == null) {
            this.locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        return this.locationManager;
    }
}

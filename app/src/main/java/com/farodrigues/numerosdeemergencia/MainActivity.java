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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.farodrigues.numerosdeemergencia.model.Contact;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String ADDRESS_NOT_FOUND = "Nenhum endereço encontrado, verifique sua internet";
    private ListView listViewEmergencyContacts;
    private Button btnGetLocation;
    private TextView txtCurrentLocation;
    private TextView txtProblemMsg;
    private EmergencyContactAdapter<Contact> emergencyContactsAdapter;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateViews();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        handleListNumerosDeEmergencia();
        createLocationListener();

    }

    private void populateViews() {
        listViewEmergencyContacts = (ListView) findViewById(R.id.listView_emergencyNumbers);
        btnGetLocation = (Button) findViewById(R.id.btn_getLocation);
        txtCurrentLocation = (TextView) findViewById(R.id.txt_currentLocation);
        txtProblemMsg = (TextView) findViewById(R.id.txt_problemMsg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createRequestLocationUpdate();
        handleBtnGpsEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private void createLocationListener() {

        // Define a listener that responds to location updates
        this.locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                (new GetAddressTask(getApplicationContext())).execute(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                handleBtnGpsEnabled();
            }

            public void onProviderDisabled(String provider) {
                handleBtnGpsEnabled();
            }
        };
    }

    private void createRequestLocationUpdate() {
        float minDistanceInMeters = 5;
        long minTimeInMillis = 5l * 1000L;
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeInMillis, minDistanceInMeters, locationListener);
    }

    private void handleBtnGpsEnabled() {

        if (isGpsEnabled()) {
            btnGetLocation.setVisibility(View.INVISIBLE);
            txtProblemMsg.setText("");
            txtCurrentLocation.setText("Procurando localização atual");
        } else {
            btnGetLocation.setVisibility(View.VISIBLE);
            txtCurrentLocation.setText("");
            txtProblemMsg.setText("Sem sinal de GPS");
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

        emergencyContactsAdapter = new EmergencyContactAdapter<>(this, android.R.layout.simple_list_item_1, BrazilianEmergencyNumbers.getNumbers());

        listViewEmergencyContacts.setAdapter(emergencyContactsAdapter);

        listViewEmergencyContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact selectedContact = (Contact) parent.getItemAtPosition(position);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + selectedContact.getNumber()));
                startActivity(callIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_contacts) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class EmergencyContactAdapter<T> extends ArrayAdapter {

        public EmergencyContactAdapter(Context context, int resource, List objects) {
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
                    String addressText = String.format("%s, %s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getLocality(),
                            address.getCountryName());
                    return addressText;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ADDRESS_NOT_FOUND;
        }

        @Override
        protected void onPostExecute(String address) {
            if (!address.equals(ADDRESS_NOT_FOUND) || (address.equals(ADDRESS_NOT_FOUND) && isGpsEnabled()) ) {
                txtCurrentLocation.setText(address);
            } else {
                txtCurrentLocation.setText("");
            }
        }
    }

    public LocationManager getLocationManager() {
        if (this.locationManager == null) {
            this.locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        return this.locationManager;
    }
}

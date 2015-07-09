package com.propertyforest.propertyuploader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RentalFormActivity extends ActionBarActivity {

    EditText titleField,streetField,localityField,houseField,addressLineField,rentField,securityDepositAmountField;
    EditText builtUpAreaField,availabilityField,bathroomsField, maintenanceAmount,masterBedroomCount;
    Spinner citySpinner,stateSpinner,countrySpinner, houseFacingSpinner,leaseSpinner,configurationSpinner;
    Spinner furnishingSpinner;
    RadioButton acRadioButton,tvRadioButton,cupboardsRadioButton,bedRadioButton,sofaRadioButton;
    RadioButton diningTableRadioButton,microwaveRadioButton,fridgeRadioButton,stoveRadioButton;
    RadioButton washingMachineRadioButton,servantRoomRadioButton,liftRadioButton,gasPipelineRadioButton;
    RadioButton gymRadioButton,swimmingPoolRadioButton,parkingRadioButton;
    Button locationButton;
    Rental rental;
    String response;
    Double lat, lng;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_form);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener locationListener = new MyLocationListener();
        Boolean flag = false;
        flag = true;
        if (flag) {
            locationManager.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10,locationListener);

        } else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }
        titleField = (EditText) findViewById(R.id.EditTextTitle);
        streetField = (EditText) findViewById(R.id.EditTextStreet);
        localityField = (EditText) findViewById(R.id.EditTextLocality);
        houseField = (EditText) findViewById(R.id.EditTextHouse);
        addressLineField = (EditText) findViewById(R.id.EditTextAddressLine);
        citySpinner = (Spinner) findViewById(R.id.SpinnerCity);
        stateSpinner = (Spinner) findViewById(R.id.SpinnerState);
        countrySpinner = (Spinner) findViewById(R.id.SpinnerCountry);
        rentField = (EditText) findViewById(R.id.EditTextRentAmount);
        securityDepositAmountField = (EditText) findViewById(R.id.EditTextSecurityDepositAmount);
        houseFacingSpinner = (Spinner) findViewById(R.id.SpinnerHouseFacing);
        builtUpAreaField = (EditText) findViewById(R.id.EditTextBuiltupArea);
        availabilityField = (EditText) findViewById(R.id.EditTextAvailability);
        maintenanceAmount = (EditText) findViewById(R.id.EditTextMaintenanceAmount);
        masterBedroomCount = (EditText) findViewById(R.id.EditTextMasterBedroomCount);
        bathroomsField = (EditText) findViewById(R.id.EditTextBathrooms);
        leaseSpinner = (Spinner) findViewById(R.id.SpinnerLease);
        furnishingSpinner = (Spinner) findViewById(R.id.SpinnerFurnishing);
        configurationSpinner = (Spinner) findViewById(R.id.SpinnerConfiguration);
        acRadioButton = (RadioButton) findViewById(R.id.RadioButtonAC);
        tvRadioButton  = (RadioButton) findViewById(R.id.RadioButtonTV);
        cupboardsRadioButton  = (RadioButton) findViewById(R.id.RadioButtonCupboards);
        bedRadioButton  = (RadioButton) findViewById(R.id.RadioButtonBed);
        sofaRadioButton = (RadioButton) findViewById(R.id.RadioButtonSofa);
        diningTableRadioButton = (RadioButton) findViewById(R.id.RadioButtonDiningTable);
        microwaveRadioButton = (RadioButton) findViewById(R.id.RadioButtonMicrowave);
        fridgeRadioButton = (RadioButton) findViewById(R.id.RadioButtonFridge);
        stoveRadioButton = (RadioButton) findViewById(R.id.RadioButtonStove);
        servantRoomRadioButton = (RadioButton) findViewById(R.id.RadioButtonServantRoom);
        liftRadioButton = (RadioButton) findViewById(R.id.RadioButtonLift);
        gasPipelineRadioButton = (RadioButton) findViewById(R.id.RadioButtonGasPipeline);
        gymRadioButton = (RadioButton) findViewById(R.id.RadioButtonGym);
        swimmingPoolRadioButton = (RadioButton) findViewById(R.id.RadioButtonSwimmingPool);
        parkingRadioButton = (RadioButton) findViewById(R.id.RadioButtonParking);
        washingMachineRadioButton = (RadioButton) findViewById(R.id.RadioButtonWashingMachine);

        locationButton = (Button) findViewById(R.id.ButtonLocation);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,locationListener);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rental_form, menu);
        return true;
    }

    public void sendRental(View view)  {
        new HttpAsyncTask().execute(Config.RENT_UPLOAD_URL);
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

    public static String POST(String url, Rental rental){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject parent = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("title", rental.getTitle());
            jsonObject.accumulate("street", rental.getStreet());
            jsonObject.accumulate("locality", rental.getLocality());
            jsonObject.accumulate("city", rental.getCity());
            jsonObject.accumulate("state", rental.getState());
            jsonObject.accumulate("house", rental.getHouse());
            jsonObject.accumulate("country",rental.getCountry());
            jsonObject.accumulate("rent_amount",rental.getRent());
            jsonObject.accumulate("built_up_area",rental.getBuiltUpArea());
            jsonObject.accumulate("security_deposit_amount",rental.getSecurityDepositAmount());
            jsonObject.accumulate("availability",rental.getAvailability());
            jsonObject.accumulate("house_facing",rental.getHouseFacing());
            jsonObject.accumulate("maintenance",rental.getMaintenanceAmount());
            jsonObject.accumulate("master_bedroom",rental.getMasterBedroomCount());
            jsonObject.accumulate("bathrooms",rental.getBathrooms());
            jsonObject.accumulate("lease",rental.getLease());
            jsonObject.accumulate("configuration",rental.getConfiguration());
            jsonObject.accumulate("furnished",rental.getFurnished());
            jsonObject.accumulate("ac",rental.getAc());
            jsonObject.accumulate("tv",rental.getTv());
            jsonObject.accumulate("cupboards",rental.getCupboards());
            jsonObject.accumulate("bed",rental.getBed());
            jsonObject.accumulate("sofa",rental.getSofa());
            jsonObject.accumulate("dining_table",rental.getDiningTable());
            jsonObject.accumulate("microwave",rental.getMicrowave());
            jsonObject.accumulate("fridge",rental.getFridge());
            jsonObject.accumulate("stove",rental.getStove());
            jsonObject.accumulate("servant_room",rental.getServantRoom());
            jsonObject.accumulate("lify",rental.getLift());
            jsonObject.accumulate("gas_pipeline",rental.getGasPipeline());
            jsonObject.accumulate("gym",rental.getGym());
            jsonObject.accumulate("swimming_pool",rental.getSwimmingPool());
            jsonObject.accumulate("parking",rental.getParking());
            jsonObject.accumulate("washing_machine",rental.getWashingMachine());

            //jsonObject.accumulate("country", rental.getCountry());
            jsonObject.accumulate("address_line", rental.getAddressLine());
            parent.put("rent", jsonObject);
            Log.d("output",parent.toString(2));
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string using Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }



    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
             Toast.makeText(getBaseContext(),"Location changed : Lat: " +
                            loc.getLatitude()+ " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " +loc.getLongitude();
            lat = loc.getLatitude();
            lng = loc.getLongitude();
            Log.v("longitude", longitude);
            String latitude = "Latitude: " +loc.getLatitude();
            Log.v("latitude", latitude);

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            rental = new Rental();
            rental.setLat(lat);
            rental.setLng(lng);
            rental.setTitle(titleField.getText().toString());
            rental.setStreet(streetField.getText().toString());
            rental.setLocality(localityField.getText().toString());
            rental.setHouse(houseField.getText().toString());
            rental.setAddressLine(addressLineField.getText().toString());
            rental.setCity(citySpinner.getSelectedItem().toString());
            rental.setState(stateSpinner.getSelectedItem().toString());
            rental.setCountry(countrySpinner.getSelectedItem().toString());
            rental.setRent(rentField.getText().toString());
            rental.setSecurityDepositAmount(securityDepositAmountField.getText().toString());
            rental.setBuiltUpArea(builtUpAreaField.getText().toString());
            rental.setAvailability(availabilityField.getText().toString());
            rental.setHouseFacing(houseFacingSpinner.getSelectedItem().toString());
            rental.setMasterBedroomCount(masterBedroomCount.getText().toString());
            rental.setMaintenanceAmount(maintenanceAmount.getText().toString());
            rental.setBathrooms(bathroomsField.getText().toString());
            rental.setLease(leaseSpinner.getSelectedItem().toString());
            rental.setConfiguration(configurationSpinner.getSelectedItem().toString());
            rental.setFurnished(furnishingSpinner.getSelectedItem().toString());
            rental.setAc(acRadioButton.isChecked());
            rental.setTv(tvRadioButton.isChecked());
            rental.setCupboards(cupboardsRadioButton.isChecked());
            rental.setBed(bedRadioButton.isChecked());
            rental.setSofa(sofaRadioButton.isChecked());
            rental.setDiningTable(diningTableRadioButton.isChecked());
            rental.setMicrowave(microwaveRadioButton.isChecked());
            rental.setFridge(fridgeRadioButton.isChecked());
            rental.setStove(stoveRadioButton.isChecked());
            rental.setServantRoom(servantRoomRadioButton.isChecked());
            rental.setLift(liftRadioButton.isChecked());
            rental.setGasPipeline(gasPipelineRadioButton.isChecked());
            rental.setGym(gymRadioButton.isChecked());
            rental.setSwimmingPool(swimmingPoolRadioButton.isChecked());
            rental.setParking(parkingRadioButton.isChecked());
            rental.setWashingMachine(washingMachineRadioButton.isChecked());
            response = POST(urls[0],rental);
            Log.d("response", response);


            return response;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), RentalGalleries.class);
            try {
                JSONObject mainObject = new JSONObject(response);
                String rent_id = String.valueOf(mainObject.getInt("id"));
                Log.d("rent_id", rent_id);
                i.putExtra("rent_id",rent_id);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            startActivity(i);
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}

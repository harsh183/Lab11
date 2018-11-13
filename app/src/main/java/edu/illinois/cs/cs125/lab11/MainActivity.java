package edu.illinois.cs.cs125.lab11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab11:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /** Button which starts ip lookup. */
    private Button ipButton;

    /**
     *  Text box for crush ip.
     */
    private EditText ipBox;

    /** A textView th
     * at shows the address. */
    private TextView addressTextView;


    @Override
    public void onClick(final View view) {
        String ip = "";
        ip = ipBox.getText().toString();
        startAPICall(ip);
    }

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        ipButton = findViewById(R.id.lookup_ip);
        addressTextView = findViewById(R.id.addressTextView);
        ipBox  = findViewById(R.id.input_ip);
        ipButton.setOnClickListener(this);

    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the IP geolocation API.
     *
     * @param ipAddress IP address to look up
     */
    void startAPICall(final String ipAddress) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://ipinfo.io/" + ipAddress + "/json",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            String city = response.get("city").toString();
            String region = response.get("region").toString();
            String country = response.get("country").toString();
            String postal = response.get("postal").toString();
            String coordinates = response.get("loc").toString();

            String combinedString = "Your crush is located at: " + city
                    + " in the region of " + region
                    + " in the country of "+ country
                    + ". Their postal address is "+ postal
                    + ". Should you wish to send a drone, the coordinates are: "+ coordinates;
            addressTextView.setText(combinedString);


        } catch (JSONException ignored) { }
    }
}

package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by joe on 12/9/2016.
 */

public class GeocodingToAdress {


    private static final String TAG = "GeocodingLocation";
    private String locationAddress,state,streetno,poskod,suburb,city,streetname,result;
    private GeocodingToAdress context = this;
    private String[] splitaddress;



    public String getlatlng(Context context,String locationAddress) {


        splitaddress = locationAddress.split(",");

        state = splitaddress[3];
        streetno = splitaddress[4];
        poskod = splitaddress[5];
        suburb = splitaddress[6];
        city = splitaddress[7];
        streetname = splitaddress[8];

        if(state.equals("JH")){

            state="Johor";
        }

        if(state.equals("PK")){

            state="Perak";
        }
        if(state.equals("KN")){

            state="Kelantan";
        }
        if(state.equals("PH")){

            state="Pahang";
        }
        if(state.equals("KD")){

            state="Kedah";
        }

        if(state.equals("PP")){

            state="Pulau Pinang";
        }


            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            String result = null;
            try {
                List
                        addressList = geocoder.getFromLocationName(streetno+" "+streetname+" "+city+" Malaysia", 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = (Address) addressList.get(0);

                    LatLng latlng = new LatLng(address.getLatitude(),address.getLongitude());

                    String latitude = String.valueOf(address.getLatitude());
                    String longitude = String.valueOf(address.getLongitude());

                    result = latitude+","+longitude+","+",address:"+streetno+" "+streetname+" "+city+" Malaysia";
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to connect to Geocoder", e);
                return null;
            } finally {

//                    result = "Address: " + locationAddress +
//                            "\n\nLatitude and Longitude :\n" + result;


        }



        return result;
    }


}


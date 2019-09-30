package com.chivesoft.ewavi;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

class MyGeocoder {

    private Context mContext;
    private String[] arrayProvinces;
    private Geocoder geocoder;

    MyGeocoder (Context context){
        mContext = context;
        arrayProvinces = mContext.getResources().getStringArray(R.array.provinces_show);
        geocoder = new Geocoder(mContext, Locale.getDefault());
    }
    int getCity (double latitude, double longitude){
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String[] addr = address.split(",");
//                if (addr.length > 1) DataGetter.locationCity = addr[addr.length-2].trim().toLowerCase().replace(" ","-");
//                DataGetter.locationCity = addresses.get(0).getSubAdminArea().trim().toLowerCase().replace(" ","-");
                return provinceToNumList(addresses.get(0).getSubAdminArea());
//                        Log.e("locationCity", "city: "+DataGetter.locationCity);
//                        Log.e("locationCity", "city: "+addresses.get(0).getSubAdminArea());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    double[] getCoordinates (String city){
//        double coordinates[] = {-1.0, -1,0};
//        double coordinates[];
        try {
            List<Address> addresses = geocoder.getFromLocationName(city, 1);
            if (addresses != null && addresses.size() > 0) {
//                coordinates[0] = addresses.get(0).getLatitude();
//                coordinates[1] = addresses.get(0).getLongitude();
                return new double[]{addresses.get(0).getLatitude(), addresses.get(0).getLongitude()};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new double[0];
    }
    double[] getCoordinates (int cityNumber){
//        double coordinates[] = {-1.0, -1,0};
//        double coordinates[];
        try {
            List<Address> addresses = geocoder.getFromLocationName(arrayProvinces[cityNumber], 1);
            if (addresses != null && addresses.size() > 0) {
//                coordinates[0] = addresses.get(0).getLatitude();
//                coordinates[1] = addresses.get(0).getLongitude();
                return new double[]{addresses.get(0).getLatitude(), addresses.get(0).getLongitude()};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new double[0];
    }
    private int provinceToNumList (String city){
        Log.i("Coordinates","Got Coordinates");
        for (int i=0; i< arrayProvinces.length; i++){
            if (arrayProvinces[i].equalsIgnoreCase(city)){
                Log.i("Coordinates","Got City");
                return i;
            }
        }
        return -1;
    }
}

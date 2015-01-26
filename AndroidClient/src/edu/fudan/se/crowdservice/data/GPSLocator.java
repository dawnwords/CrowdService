package edu.fudan.se.crowdservice.data;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import edu.fudan.se.crowdservice.jade.agent.AgentInterface;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public class GPSLocator implements LocationListener {
    private AgentInterface agent;
    private LocationManager lm;

    public void enableGPS(Context context, final AgentInterface agent) {
        this.agent = agent;
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void disableGPS() {
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (agent != null) {
            agent.setLocation(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
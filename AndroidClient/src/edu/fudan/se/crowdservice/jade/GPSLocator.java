package edu.fudan.se.crowdservice.jade;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import edu.fudan.se.crowdservice.jade.agent.AgentInterface;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public class GPSLocator implements AMapLocationListener {
    private AgentInterface agent;
    private LocationManagerProxy mAMapLocManager;

    public void enableGPS(Context context, final AgentInterface agent) {
        this.agent = agent;
        if (mAMapLocManager == null) {
            mAMapLocManager = LocationManagerProxy.getInstance(context);
        }
        mAMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 1000, 10, this);
    }

    public void disableGPS() {
        if (mAMapLocManager != null) {
            mAMapLocManager.removeUpdates(this);
            mAMapLocManager.destory();
        }
        mAMapLocManager = null;
    }

    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (agent != null && aMapLocation != null) {
            agent.setLocation(aMapLocation);
        }
    }
}
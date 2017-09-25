package org.aospextended.aexpapers.models;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;

import org.aospextended.aexpapers.Constants;

/**
 * Created by ishubhamsingh on 25/9/17.
 */

public class StatsData {

    private String device;
    private String model;
    private String version;
    private String buildType;
    private String countryCode;

    public String getDevice() {
        return SystemProperties.get(Constants.KEY_DEVICE);
    }

    public void setDevice(String device) {
        if(device!="") {
            this.device = device;
        } else {
            this.device = "unknown";
        }
    }

    public String getModel() {
        return SystemProperties.get(Constants.KEY_MODEL);
    }

    public void setModel(String model) {
        if(model!="") {
            this.model = model;
        } else {
            this.model = "unknown";
        }
    }

    public String getVersion() {
        return Constants.KEY_VERSION;
    }

    public void setVersion(String version) {
        if(version!="") {
            this.version = version;
        } else {
            this.version = "unknown";
        }
    }

    public String getBuildType() {
        return SystemProperties.get(Constants.KEY_BUILD_TYPE);
    }

    public void setBuildType(String buildType) {
        if(buildType!="") {
            this.buildType = buildType;
        } else {
            this.buildType = "unknown";
        }
    }

    public String getCountryCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkCountryIso();
    }

    public void setCountryCode(String countryCode) {
        if(countryCode!="") {
            this.countryCode = countryCode;
        } else {
            this.countryCode = "unknown";
        }
    }
}

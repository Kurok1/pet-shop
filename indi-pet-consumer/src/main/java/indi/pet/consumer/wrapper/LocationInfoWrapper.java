package indi.pet.consumer.wrapper;

import java.io.Serializable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.14
 */
public class LocationInfoWrapper implements Serializable {

    private double latitude;

    private double longitude;

    private double accurate;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccurate() {
        return accurate;
    }

    public void setAccurate(double accurate) {
        this.accurate = accurate;
    }
}

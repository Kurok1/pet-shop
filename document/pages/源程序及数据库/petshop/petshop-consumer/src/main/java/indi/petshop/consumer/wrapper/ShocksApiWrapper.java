package indi.petshop.consumer.wrapper;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.14
 */
public class ShocksApiWrapper implements Serializable {
    private int size;

    private double accurate;

    private double latitude;

    private double longitude;

    private List<ShockWrapper> shocks;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getAccurate() {
        return accurate;
    }

    public void setAccurate(double accurate) {
        this.accurate = accurate;
    }

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

    public List<ShockWrapper> getShocks() {
        return shocks;
    }

    public void setShocks(List<ShockWrapper> shocks) {
        this.shocks = shocks;
    }
}



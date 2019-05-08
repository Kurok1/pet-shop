package indi.petshop.consumer.wrapper;

import indi.petshop.consumer.domain.Shock;

import java.io.Serializable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.14
 */
public class ShockWrapper implements Serializable {
    private String shopkeeperId;

    private String shopkeeperLogo;

    private String shopkeeperName;

    private Shock shock;

    private double latitude;

    private double longitude;

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

    public String getShopkeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(String shopkeeperId) {
        this.shopkeeperId = shopkeeperId;
    }

    public String getShopkeeperLogo() {
        return shopkeeperLogo;
    }

    public void setShopkeeperLogo(String shopkeeperLogo) {
        this.shopkeeperLogo = shopkeeperLogo;
    }

    public String getShopkeeperName() {
        return shopkeeperName;
    }

    public void setShopkeeperName(String shopkeeperName) {
        this.shopkeeperName = shopkeeperName;
    }

    public Shock getShock() {
        return shock;
    }

    public void setShock(Shock shock) {
        this.shock = shock;
    }
}

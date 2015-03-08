package service.shcomputer.app.itemselection.interfaces;

import java.util.Date;

/**
 * Created by Jiahuan on 2015/1/27.
 */
public class SHComputerInfo {
    public final String brand;
    public final String series;
    public final String cpu;
    public final String memory;
    public final String disk;
    public final String newness;
    public final double price;
    public final String seller;
    public final String location;
    public final Date saleTime;
    public final double longitude;
    public final double latitude;

    public SHComputerInfo(String brand, String series, String cpu, String memory, String disk, String newness, double price, String seller, String location, Date saleTime, double longitude, double latitude) {
        this.brand = brand;
        this.series = series;
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
        this.newness = newness;
        this.price = price;
        this.seller = seller;
        this.location = location;
        this.saleTime = saleTime;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

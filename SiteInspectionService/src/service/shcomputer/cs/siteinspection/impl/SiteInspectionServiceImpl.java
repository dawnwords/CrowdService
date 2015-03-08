package service.shcomputer.cs.siteinspection.impl;

import edu.fudan.se.crowdservice.core.ConcreteService;
import edu.fudan.se.crowdservice.core.CrowdServiceStub;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import service.shcomputer.cs.siteinspection.interfaces.SiteInspectionService;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public class SiteInspectionServiceImpl extends ConcreteService implements SiteInspectionService {
    public static final String IP = "10.131.253.211";
    public static final int PORT = 8887;
    public static final String URL = String.format("http://%s:%d/siteinspect?wsdl", IP, PORT);
    public static final String NS = "http://siteinspection.crowdservice.se.fudan.edu/";
    public static final String METHOD = "siteInspect";

    @Override
    protected boolean isCrowd() {
        return true;
    }

    @Override
    public ArrayList<KeyValueHolder> siteInspect(double latitude, double longitude, String brand, String series, String newness,
                                                 String CPU, String memory, String hardDisk, String sellerAddress) {
        this.latitude = latitude;
        this.longitude = longitude;

        CrowdServiceStub stub = new CrowdServiceStub(URL, NS, METHOD, this);
        stub.addProperty("arg7", brand);
        stub.addProperty("arg8", series);
        stub.addProperty("arg9", newness);
        stub.addProperty("arg10", CPU);
        stub.addProperty("arg11", memory);
        stub.addProperty("arg12", hardDisk);
        stub.addProperty("arg13", sellerAddress);
        return stub.sendSOAP();
    }

    @Override
    protected int latitudeArgIndex() {
        return 0;
    }

    @Override
    protected int longitudeArgIndex() {
        return 1;
    }
}

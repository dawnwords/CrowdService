package service.shcomputer.cs.priceassessment.impl;

import edu.fudan.se.crowdservice.core.ConcreteService;
import edu.fudan.se.crowdservice.core.CrowdServiceStub;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import service.shcomputer.cs.priceassessment.interfaces.PriceAssessmentService;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/27.
 */
public class PriceAssessmentServiceImpl extends ConcreteService implements PriceAssessmentService {
    public static final String IP = "10.131.253.211";
    public static final int PORT = 8888;
    public static final String URL = String.format("http://%s:%d/priceassess?wsdl", IP, PORT);
    public static final String NS = "http://priceassessment.crowdservice.se.fudan.edu/";
    public static final String METHOD = "assessPrice";


    @Override
    public ArrayList<KeyValueHolder> assessPrice(String brand, String series, String newness, String CPU, String memory, String hardDisk, String imagePath) throws Exception {
        CrowdServiceStub stub = new CrowdServiceStub(URL, NS, METHOD, this);

        stub.addProperty("arg7", brand);
        stub.addProperty("arg8", series);
        stub.addProperty("arg9", newness);
        stub.addProperty("arg10", CPU);
        stub.addProperty("arg11", memory);
        stub.addProperty("arg12", hardDisk);
        stub.addProperty("arg13", loadDataAsBase64ByPath(imagePath));

        return stub.sendSOAP();
    }

    @Override
    protected boolean isCrowd() {
        return true;
    }
}

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
    public static final String IP = "10.131.253.172";
    //    public static final String IP = "192.168.0.103";
    public static final int PORT = 8888;
    public static final String URL = String.format("http://%s:%d/priceassess?wsdl", IP, PORT);
    public static final String NS = "http://priceassessment.crowdservice.se.fudan.edu/";
    public static final String METHOD = "assessPrice";


    @Override
    public ArrayList<KeyValueHolder> assessPrice(String brand, String series, String newness, String CPU, String memory, String hardDisk, String imagePath) {
        CrowdServiceStub stub = new CrowdServiceStub(URL, NS, METHOD, time, this);

        stub.addProperty("arg0", consumerId);
        stub.addProperty("arg1", cost);
        stub.addProperty("arg2", time);
        stub.addProperty("arg3", templateName);
        stub.addProperty("arg4", brand);
        stub.addProperty("arg5", series);
        stub.addProperty("arg6", newness);
        stub.addProperty("arg7", CPU);
        stub.addProperty("arg8", memory);
        stub.addProperty("arg9", hardDisk);
        stub.addProperty("arg10", loadDataAsBase64ByPath(imagePath));

        return stub.sendSOAP();
    }

    @Override
    protected Class getServiceInterface() {
        return PriceAssessmentService.class;
    }

}

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

    public static final String URL = "http://10.131.253.172:8888/siteinspect?wsdl";
    public static final String NS = "http://siteinspection.crowdservice.se.fudan.edu/";
    public static final String METHOD = "siteInspect";

    @Override
    protected Class getServiceInterface() {
        return SiteInspectionService.class;
    }

    @Override
    public ArrayList<KeyValueHolder> siteInspect(String brand, String series, String newness, String CPU, String memory, String hardDisk, String sellerAddress) {
        CrowdServiceStub stub = new CrowdServiceStub(URL, NS, METHOD, time, this);

        stub.addProperty("arg0", consumerId);
        stub.addProperty("arg1", cost);
        stub.addProperty("arg2", time);
        stub.addProperty("arg3", brand);
        stub.addProperty("arg4", series);
        stub.addProperty("arg5", newness);
        stub.addProperty("arg6", CPU);
        stub.addProperty("arg7", memory);
        stub.addProperty("arg8", hardDisk);
        stub.addProperty("arg9", sellerAddress);
        stub.addProperty("arg10", templateName);

        return stub.sendSOAP();
    }

}

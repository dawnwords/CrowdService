package service.shcomputer.cs.siteinspection.interfaces;

import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public interface SiteInspectionService {
    ArrayList<KeyValueHolder> siteInspect(double latitude, double longitude, String brand, String series, String newness,
                                          String CPU, String memory, String hardDisk, String sellerAddress) throws Exception;
}

package service.shcomputer.cs.priceassessment.interfaces;

import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/27.
 */
public interface PriceAssessmentService {
    ArrayList<KeyValueHolder> assessPrice(String brand, String series, String newness, String CPU, String memory, String hardDisk, String imagePath) throws Exception;
}

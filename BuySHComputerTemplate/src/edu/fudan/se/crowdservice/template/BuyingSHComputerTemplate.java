package edu.fudan.se.crowdservice.template;

import android.graphics.Bitmap;
import android.location.Location;
import edu.fudan.se.crowdservice.core.Template;
import service.shcomputer.cs.judgeworth.interfaces.JudgeWorthOfComputerService;
import service.shcomputer.cs.takepic.interfaces.TakePictureOfURLService;
import service.shcomputer.ns.showpic.interfaces.ShowPictureService;
import service.shcomputer.ws.parselocation.interfaces.GetLocationFromURLService;
import service.shcomputer.ws.select.interfaces.SelectURLService;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class BuyingSHComputerTemplate extends Template {
    //TODO modify url
    private static final String SECOND_HAND_COMPUTER_WEB_PAGE_URL = "http://10.131.252.256:8080/";

    private SelectURLService selectURLService;                      //WS - startActivityForResult
    private GetLocationFromURLService getLocationFromURLService;    //WS
    private TakePictureOfURLService takePictureOfURLService;        //CS
    private ShowPictureService showPictureService;                  //NS - startActivityForResult
    private JudgeWorthOfComputerService judgeWorthOfComputerService;//CS

    @Override
    protected void resolveService(ServiceResolver serviceResolver) {
        selectURLService = serviceResolver.resolveService(SelectURLService.class);
        getLocationFromURLService = serviceResolver.resolveService(GetLocationFromURLService.class);
        takePictureOfURLService = serviceResolver.resolveService(TakePictureOfURLService.class);
        showPictureService = serviceResolver.resolveService(ShowPictureService.class);
        judgeWorthOfComputerService = serviceResolver.resolveService(JudgeWorthOfComputerService.class);
    }

    @Override
    protected void execute() {
        String url = selectURLService.getSelectedURL(SECOND_HAND_COMPUTER_WEB_PAGE_URL);
        Location location = getLocationFromURLService.getLocationFromURL(url);
        Bitmap pic = takePictureOfURLService.takeRealPictureOfURL(url, location);
        showPictureService.showPic(pic);
        boolean shouldContinue = requestUserConfirm("Continue to compare price?");
        if (shouldContinue) {
            boolean isWorthy = judgeWorthOfComputerService.judgeWorthOfComputer(url);
            if (isWorthy) {
                showMessage("Do payment!");
            } else {
                showMessage("Not worthy!");
            }
        }
    }
}

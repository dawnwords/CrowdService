package edu.fudan.se.crowdservice.template;

import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.kv.TextDisplay;
import service.shcomputer.app.itemselection.interfaces.ItemSelectionService;
import service.shcomputer.app.itemselection.interfaces.SHComputerInfo;
import service.shcomputer.cs.priceassessment.interfaces.PriceAssessmentService;
import service.shcomputer.cs.siteinspection.interfaces.SiteInspectionService;
import service.ui.interfaces.UIService;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public class BuyingSHComputerTemplateV2 extends Template {

    private ItemSelectionService itemSelectionService;
    private SiteInspectionService siteInspectionService;
    private UIService uiService;
    private PriceAssessmentService priceAssessmentService;

    @Override
    protected String getTemplateName() {
        return getClass().getName();
    }

    @Override
    protected void resolveService(ServiceResolver serviceResolver) {
        itemSelectionService = serviceResolver.resolveService(ItemSelectionService.class, 0.2, 0.2);
        siteInspectionService = serviceResolver.resolveService(SiteInspectionService.class, 0.3, 0.3);
        uiService = serviceResolver.resolveService(UIService.class, 0.2, 0.2);
        priceAssessmentService = serviceResolver.resolveService(PriceAssessmentService.class, 0.3, 0.3);
    }

    @Override
    protected void execute() {
        SHComputerInfo compInfo = itemSelectionService.selectItem();
        ArrayList<KeyValueHolder> result = siteInspectionService.siteInspect(compInfo.brand, compInfo.series, compInfo.newness, compInfo.cpu, compInfo.memory, compInfo.disk, compInfo.location);
        String computerImagePath = ((ImageDisplay) result.get(0)).imagePath;
        uiService.displayUI(result);
        System.out.println("computerImagePath:" + computerImagePath);
        if (requestUserConfirm("Do you want to assess this price of this computer?")) {
            result = priceAssessmentService.assessPrice(compInfo.brand, compInfo.series, compInfo.newness, compInfo.cpu, compInfo.memory, compInfo.disk, computerImagePath);
            double assessPrice = Double.valueOf(((TextDisplay) result.get(0)).getValue());
            if (compInfo.price < assessPrice * 1.1) {
                showMessage("Payment succeeded!");
            } else {
                showMessage("It's not worthy!");
            }
        }
    }
}

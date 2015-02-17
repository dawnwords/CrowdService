package edu.fudan.se.crowdservice.core;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;
import android.widget.Toast;
import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.ImageInput;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.kv.TextDisplay;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/27.
 */
public class CrowdServiceStub {

    public static final int WAITING_TIME_SECOND = 20;
    private final SoapObject soapObject;
    private final HttpTransportSE http;
    private ConcreteService service;

    public CrowdServiceStub(String url, String namespace, String method, int waitingTime, ConcreteService service) {
        this.http = new HttpTransportSE(url, (waitingTime + WAITING_TIME_SECOND) * 1000);
        this.soapObject = new SoapObject(namespace, method);
        this.service = service;
    }

    public void addProperty(String name, Object value) {
        soapObject.addProperty(name, value);
    }

    public ArrayList<KeyValueHolder> sendSOAP() {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.bodyOut = soapObject;
        try {
            http.call(null, envelope);
            if (envelope.getResponse() != null) {
                SoapObject obj = (SoapObject) envelope.bodyIn;
                String crowdServiceResult = obj.getPropertyAsString(0);
                if (crowdServiceResult != null && !"".equals(crowdServiceResult)) {
                    return xml2Obj(crowdServiceResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        service.postUIRunnable(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(service.context, "No one response you :<", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private ArrayList<KeyValueHolder> xml2Obj(String template) {
        template = template.replace("\n", "");
        ByteArrayInputStream bais = new ByteArrayInputStream(template.getBytes());
        ArrayList<KeyValueHolder> result = new ArrayList<KeyValueHolder>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bais);
            NodeList nodes = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                NodeList kv = node.getChildNodes();
                String key = kv.item(0).getTextContent();
                String value = kv.item(1).getTextContent();
                if (ImageInput.class.getSimpleName().equals(node.getNodeName())) {
                    result.add(new ImageDisplay(key, IOUtil.saveByteArray(Base64.decode(value, Base64.DEFAULT), service.context)));
                } else {
                    result.add(new TextDisplay(key, value));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

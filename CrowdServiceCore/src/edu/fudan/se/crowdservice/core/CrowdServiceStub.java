package edu.fudan.se.crowdservice.core;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;
import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.ImageInput;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.kv.TextDisplay;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/27.
 */
public class CrowdServiceStub {

    public static final int WAITING_TIME_SECOND = 20;
    private final SoapObject soapObject;
    private final HttpTransportSE http;
    private ConcreteService service;

    public CrowdServiceStub(String url, String namespace, String method, ConcreteService service) {
        this.http = new HttpTransportSE(url, (service.time + WAITING_TIME_SECOND) * 1000);
        this.soapObject = new SoapObject(namespace, method);
        this.service = service;
        addCommonProperty();
    }

    private void addCommonProperty() {
        addProperty("arg0", service.latitude);
        addProperty("arg1", service.longitude);
        addProperty("arg2", service.consumerId);
        addProperty("arg3", service.cost);
        addProperty("arg4", service.time);
        addProperty("arg5", service.templateName);
        addProperty("arg6", service.resultNum);
    }

    public void addProperty(String name, Object value) {
        soapObject.addProperty(name, value);
    }

    public ArrayList<KeyValueHolder> sendSOAP() {
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope();
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

        return null;
    }

    private SoapSerializationEnvelope getSoapSerializationEnvelope() {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.bodyOut = soapObject;
        new Marshal() {
            public Object readInstance(XmlPullParser parser, String namespace, String name,
                                       PropertyInfo expected) throws IOException, XmlPullParserException {
                return Double.parseDouble(parser.nextText());
            }

            public void register(SoapSerializationEnvelope cm) {
                cm.addMapping(cm.xsd, "double", Double.class, this);
            }

            public void writeInstance(XmlSerializer writer, Object obj) throws IOException {
                writer.text(obj.toString());
            }
        }.register(envelope);
        return envelope;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private ArrayList<KeyValueHolder> xml2Obj(String response) {
        ByteArrayInputStream bais = new ByteArrayInputStream(parseResponse(response).getBytes());
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

    private String parseResponse(String template) {
        String[] tokens = template.split(":");
        service.actualCost = Integer.parseInt(tokens[1]);
        return tokens[0].replace("\n", "");
    }
}

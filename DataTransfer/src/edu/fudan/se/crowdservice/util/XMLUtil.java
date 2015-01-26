package edu.fudan.se.crowdservice.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;
import edu.fudan.se.crowdservice.kv.Description;
import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.kv.TextDisplay;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;

public class XMLUtil {
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String obj2XML(ArrayList<KeyValueHolder> keyValueHolders) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("Root");
            doc.appendChild(root);
            for (KeyValueHolder holder : keyValueHolders) {
                Element holderElement = doc.createElement(holder.getClass().getSimpleName());
                root.appendChild(holderElement);

                Element key = doc.createElement("Key");
                key.setTextContent(holder.getKey());
                Element value = doc.createElement("Value");
                Object valueObj = holder.getValue();
                String valueString = "";
                if (valueObj instanceof String) {
                    valueString = (String) valueObj;
                } else if (valueObj instanceof byte[]) {
                    valueString = "" + new Date().getTime();
                    IOUtil.saveByteArray(valueString, (byte[]) valueObj);
                }
                value.setTextContent(valueString);

                holderElement.appendChild(key);
                holderElement.appendChild(value);
            }
            return doc2String(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static ArrayList<KeyValueHolder> xml2Obj(String template) {
        ByteArrayInputStream bais = new ByteArrayInputStream(template.getBytes());
        ArrayList<KeyValueHolder> result = new ArrayList<KeyValueHolder>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bais);
            NodeList nodes = doc.getDocumentElement().getChildNodes();

            int i = 0;
            Node node = nodes.item(0);
            if ("Description".equals(node.getNodeName())) {
                String description = node.getTextContent();
                result.add(new Description(description));
                i++;
            }

            for (; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                Class<KeyValueHolder> clazz = (Class<KeyValueHolder>) Class.forName("edu.fudan.se.crowdservice.kv." + node.getNodeName());
                NodeList kv = node.getChildNodes();
                String key = kv.item(0).getTextContent();
                String value = kv.item(1).getTextContent();
                result.add(clazz.getConstructor(String.class, String.class).newInstance(key, value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static ArrayList<KeyValueHolder> xml2ObjConsumer(String template) {
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
                if (ImageDisplay.class.getSimpleName().equals(node.getNodeName())) {
                    result.add(new ImageDisplay(key, IOUtil.saveByteArray(Base64.decode(value, Base64.DEFAULT))));
                } else if (TextDisplay.class.getSimpleName().equals(node.getNodeName())) {
                    result.add(new TextDisplay(key, value));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private static String doc2String(Document doc) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(doc), new StreamResult(out));
        return out.toString();
    }
}

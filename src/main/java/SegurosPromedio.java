import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SegurosPromedio {
    static final String CLASS_NAME = SegurosPromedio.class.getSimpleName();
    static final Logger LOG = Logger.getLogger(CLASS_NAME);

    public static void main(String argv[]) {
        if (argv.length != 1) {
            LOG.severe("Falta archivo XML como argumento.");
            System.exit(1);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(argv[0]));

            doc.getDocumentElement().normalize();

            reporteVentas(doc);


        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
        }
    }





    public static void reporteVentas(Document doc) {
        Element root = doc.getDocumentElement();

        NodeList salesData = root.getElementsByTagName("insurance_record");

        int n = salesData.getLength();
        int cont=0;
        HashMap<String,Double> ventas = new HashMap<>();

        double total = 0.0;

        String sales;
        String department;
        for (int index = 0; index < n; index++) {
            Node node = salesData.item(index);
            cont++;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                sales = element.getElementsByTagName("insurance").item(0).getTextContent();
                department = element.getElementsByTagName("model").item(0).getTextContent();

                double val = Double.parseDouble(sales);

                if( ventas.containsKey(department) ) {

                    double x = ventas.get(department);

                    ventas.put(department, val + (x/cont) );
                } else {
                    ventas.put(department,val);
                }
                total = total + val;
            }

        }

        for (Map.Entry<String , Double> entry: ventas.entrySet()) {
            System.out.printf("El modelo %s tiene un seguro promedio de %f dolares al mes \n", entry.getKey(),
                    entry.getValue());
        }


    }
}

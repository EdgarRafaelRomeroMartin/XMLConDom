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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class VentasMensuales {
    static final String CLASS_NAME = VentasMensuales.class.getSimpleName();
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
        String Mes=null;
        double total = 0.0;

        String sales;
        String department;
        for (int index = 0; index < n; index++) {
            Node node = salesData.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                sales = element.getElementsByTagName("insurance").item(0).getTextContent();
                department = element.getElementsByTagName("contract_date").item(0).getTextContent();


                try
                {
                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse( department);
                    ZoneId timeZone = ZoneId.systemDefault();
                    LocalDate getLocalDate = date1.toInstant().atZone(timeZone).toLocalDate();
                    Mes= String.valueOf(getLocalDate.getMonth());

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                double val = Double.parseDouble(sales);

                if( ventas.containsKey(Mes) ) {
                    cont++;
                    double x = ventas.get(Mes);

                    ventas.put(Mes, val + x );
                } else {
                    ventas.put(Mes,val);
                }

            }

        }

        for (Map.Entry<String , Double> entry: ventas.entrySet()) {
            System.out.printf("Las ventas mensuales de %s fueron %f \n", entry.getKey(),
                    entry.getValue());
        }


    }
}

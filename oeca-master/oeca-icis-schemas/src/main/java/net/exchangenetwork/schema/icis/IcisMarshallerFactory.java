package net.exchangenetwork.schema.icis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * @author dfladung
 */
public abstract class IcisMarshallerFactory {

    @Autowired
    ResourceLoader loader;

    protected Marshaller getMarshaller(String prefix, Class clazz) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        sf.setResourceResolver(new XmlResourceResolver(prefix));
        StreamSource[] sources = getSources(prefix);
        Schema schema = sf.newSchema(sources);
        JAXBContext jc = JAXBContext.newInstance(clazz);

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(schema);
        marshaller.setEventHandler(new XmlValidationEventHandler());
        return marshaller;
    }

    StreamSource[] getSources(String prefix) throws Exception {
        Resource resource = loader.getResource(String.format("classpath:schema/%s/index.xsd", prefix));
        StreamSource src = new StreamSource(resource.getInputStream());
        return new StreamSource[]{src};
    }
}

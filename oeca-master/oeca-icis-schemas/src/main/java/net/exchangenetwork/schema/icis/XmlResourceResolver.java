package net.exchangenetwork.schema.icis;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * @author dfladung
 */
public class XmlResourceResolver implements LSResourceResolver {

    String prefix;

    public XmlResourceResolver(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI, final String publicId, final String systemId,
                                   String baseURI) {
        return new XmlResolverInput(publicId, systemId, Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(String.format("schema/%s/%s", prefix, systemId)));
    }
}

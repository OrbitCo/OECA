package gov.epa.oeca.common.domain;

import java.io.Serializable;

/**
 * @param <T>
 * @author dfladung
 */
public interface ValueObject<T> extends Serializable {

    boolean sameValueAs(T other);

}

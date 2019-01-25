package gov.epa.oeca.common.domain;

import java.io.Serializable;

/**
 * @author dfladung
 *
 * @param <T>
 */
public interface Entity<T> extends Serializable {

	boolean sameIdentityAs(T other);

	Long getId();

	void setId(Long id);
}

package gov.epa.oeca.common.domain.dto.datatable;

/**
 * @author David Dundua (david@dundua.com)
 * @version $Id: DataTableSearch.java,v 1.1 2015/10/26 05:28:42 ddundua Exp $
 */
public class DataTableSearch {

	private Long value;
	private Boolean regex;

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Boolean getRegex() {
		return regex;
	}

	public void setRegex(Boolean regex) {
		this.regex = regex;
	}
}

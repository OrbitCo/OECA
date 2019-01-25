package gov.epa.oeca.common.domain.dto.datatable;

/**
 * @author David Dundua (david@dundua.com)
 * @version $Id: DataTableCriteria.java,v 1.1 2015/10/26 05:28:42 ddundua Exp $
 */

public class DataTableCriteria<T> {

	private DataTableConfig config;
	private T criteria;

	public DataTableConfig getConfig() {
		return config;
	}

	public void setConfig(DataTableConfig config) {
		this.config = config;
	}

	public T getCriteria() {
		return criteria;
	}

	public void setCriteria(T criteria) {
		this.criteria = criteria;
	}

	@Override
	public String toString() {
		return "DataTableSearch{" +
				"config=" + config +
				", criteria=" + criteria +
				'}';
	}
}

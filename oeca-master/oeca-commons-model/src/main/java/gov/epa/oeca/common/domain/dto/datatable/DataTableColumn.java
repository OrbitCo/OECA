package gov.epa.oeca.common.domain.dto.datatable;

/**
 * @author David Dundua (david@dundua.com)
 * @version $Id: DataTableColumn.java,v 1.1 2015/10/26 05:28:42 ddundua Exp $
 */

public class DataTableColumn {
	public class DataTablesColumnSearch {

		private String value;
		private Boolean regex;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Boolean getRegex() {
			return regex;
		}

		public void setRegex(Boolean regex) {
			this.regex = regex;
		}

		@Override
		public String toString() {
			return "Search{" +
					"value='" + value + '\'' +
					", regex=" + regex +
					'}';
		}
	}

	private String data;
	private String name;
	private Boolean searchable;
	private Boolean orderable;
	private DataTablesColumnSearch search;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getSearchable() {
		return searchable;
	}

	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}

	public Boolean getOrderable() {
		return orderable;
	}

	public void setOrderable(Boolean orderable) {
		this.orderable = orderable;
	}

	public DataTablesColumnSearch getSearch() {
		return search;
	}

	public void setSearch(DataTablesColumnSearch search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "DataTablesColumn{" +
				"data='" + data + '\'' +
				", name='" + name + '\'' +
				", searchable=" + searchable +
				", orderable=" + orderable +
				", search=" + search +
				'}';
	}
}

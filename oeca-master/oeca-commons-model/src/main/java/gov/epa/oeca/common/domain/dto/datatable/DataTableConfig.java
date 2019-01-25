package gov.epa.oeca.common.domain.dto.datatable;

import java.util.List;

/**
 * @author David Dundua (david@dundua.com)
 * @version $Id: DataTableConfig.java,v 1.1 2015/10/26 05:28:42 ddundua Exp $
 */
public class DataTableConfig {

	private Long draw;
	private List<DataTableColumn> columns;
	private List<DataTableOrder> order;
	private Long start;
	private Long length;
	private DataTableSearch search;


	public Long getDraw() {
		return draw;
	}

	public void setDraw(Long draw) {
		this.draw = draw;
	}

	public List<DataTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DataTableColumn> columns) {
		this.columns = columns;
	}

	public List<DataTableOrder> getOrder() {
		return order;
	}

	public void setOrder(List<DataTableOrder> order) {
		this.order = order;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public DataTableSearch getSearch() {
		return search;
	}

	public void setSearch(DataTableSearch search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "DataTableSearchConfig{" +
				"draw=" + draw +
				", columns=" + columns +
				", order=" + order +
				", start=" + start +
				", length=" + length +
				", search=" + search +
				'}';
	}
}

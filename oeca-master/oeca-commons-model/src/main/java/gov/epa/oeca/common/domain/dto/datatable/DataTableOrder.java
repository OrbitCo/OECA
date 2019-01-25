package gov.epa.oeca.common.domain.dto.datatable;

/**
 * @author David Dundua (david@dundua.com)
 * @version $Id: DataTableOrder.java,v 1.1 2015/10/26 05:28:42 ddundua Exp $
 */
public class DataTableOrder {
	public enum DataTablesOrderEnum {
		asc, desc
	}
	private Integer column;
	private DataTablesOrderEnum dir;

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public DataTablesOrderEnum getDir() {
		return dir;
	}

	public void setDir(DataTablesOrderEnum dir) {
		this.dir = dir;
	}

	@Override
	public String toString() {
		return "DataTablesOrder{" +
				"column=" + column +
				", dir=" + dir +
				'}';
	}
}

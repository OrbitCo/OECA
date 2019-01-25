package gov.epa.oeca.common.domain.dto;

import gov.epa.oeca.common.domain.BaseEntity;

import java.util.List;

public class BaseFormSearchResult<T extends BaseEntity> {

    private List<T> data;

    private Long draw;

    private Long recordsTotal;

    private Long recordsFiltered;

    public BaseFormSearchResult(List<T> data, Long draw, Long recordsTotal, Long recordsFiltered) {
    	this.data = data;
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
    }

    public BaseFormSearchResult() {
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getDraw() {
        return draw;
    }

    public void setDraw(Long draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    @Override
    public String toString() {
        return "BaseFormSearchResult{" +
                "recordsFiltered=" + recordsFiltered +
                ", recordsTotal=" + recordsTotal +
                ", draw=" + draw +
                ", data=" + data +
                '}';
    }
}

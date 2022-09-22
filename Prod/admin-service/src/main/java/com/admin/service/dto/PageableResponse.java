package com.admin.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {
    private List<T> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalRecords;
    private Integer totalPages;
    private Boolean isLastPage;

    public PageableResponse<T> setResponseData(List<T> dataList, Page<?> page){
        this.setData(dataList);
        this.setPageNumber(page.getNumber());
        this.setPageSize(page.getSize());
        this.setTotalRecords(page.getTotalElements());
        this.setTotalPages(page.getTotalPages());
        this.setIsLastPage(page.isLast());
        return this;
    }
}

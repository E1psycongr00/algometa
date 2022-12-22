package com.lhgpds.algometa.internal.common.page;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Pages<T> {

    private final List<T> data;
    private final int pageNumber;
    private final int takeSize;
    private final int totalPages;

    public Pages(List<T> data, int pageNumber, int takeSize, int totalPages) {
        this.data = data;
        this.pageNumber = pageNumber;
        this.takeSize = takeSize;
        this.totalPages = totalPages;
    }

    public Pages(List<T> data, int pageNumber, int takeSize, long totalNumber) {
        this.data = data;
        this.pageNumber = pageNumber;
        this.takeSize = takeSize;
        this.totalPages = calculateTotalPages(totalNumber);
    }

    public static <T> Pages<T> of(List<T> data, PageCondition pageCondition, long totalNumber) {
        return new Pages<T>(data, pageCondition.getPageNumber(), pageCondition.getTakeSize(),
            totalNumber);
    }

    private int calculateTotalPages(long totalNumber) {
        return (int) (totalNumber / takeSize) + 1;
    }
}

package com.lhgpds.algometa.internal.common.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageCondition {

    private int pageNumber;
    private int takeSize;


    public static PageCondition of(int pageNumber, int takeSize) {
        return new PageCondition(pageNumber, takeSize);
    }

    public int getOffsetSize() {
        return (pageNumber - 1) * takeSize;
    }
}

package com.lhgpds.algometa.controller.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseUploadImage {

    @JsonProperty("image_link")
    private final String image;
}

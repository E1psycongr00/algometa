package com.lhgpds.algometa.controller.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseProfile {

    private String email;
    private String nickname;
    private String image;
}

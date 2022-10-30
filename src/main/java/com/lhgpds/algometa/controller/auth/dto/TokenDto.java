package com.lhgpds.algometa.controller.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}

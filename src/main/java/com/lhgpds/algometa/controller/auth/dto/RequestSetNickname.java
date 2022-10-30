package com.lhgpds.algometa.controller.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestSetNickname {

    private final String nickName;
}

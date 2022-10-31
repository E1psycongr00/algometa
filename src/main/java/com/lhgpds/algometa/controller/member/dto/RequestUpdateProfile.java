package com.lhgpds.algometa.controller.member.dto;

import com.lhgpds.algometa.validation.annotation.Nickname;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateProfile {

    @Nickname
    private String nickname;
}

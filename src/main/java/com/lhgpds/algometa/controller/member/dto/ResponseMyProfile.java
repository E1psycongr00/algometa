package com.lhgpds.algometa.controller.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhgpds.algometa.internal.member.entity.vo.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseMyProfile {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime modifiedAt;
    private Long id;
    private String email;
    private String nickname;
    private String image;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;
}

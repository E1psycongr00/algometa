package com.lhgpds.algometa.controller.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhgpds.algometa.internal.member.domain.vo.Role;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ResponseMyProfile {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private Long id;
    private String email;
    private String nickname;
    private String image;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;
}

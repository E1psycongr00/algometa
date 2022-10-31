package com.lhgpds.algometa.internal.member.service.dto;

import com.lhgpds.algometa.internal.member.entity.vo.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String nickname;
    private String image;
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}

package com.lhgpds.algometa.internal.member.application.dto;

import com.lhgpds.algometa.internal.member.domain.vo.Email;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import com.lhgpds.algometa.internal.member.domain.vo.Nickname;
import com.lhgpds.algometa.internal.member.domain.vo.Role;
import java.time.LocalDateTime;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private Email email;
    private Nickname nickname;
    private ImageLink image;
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}

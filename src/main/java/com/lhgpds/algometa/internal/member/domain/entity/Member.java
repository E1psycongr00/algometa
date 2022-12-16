package com.lhgpds.algometa.internal.member.domain.entity;

import com.lhgpds.algometa.internal.common.entity.DateBaseEntity;
import com.lhgpds.algometa.internal.member.domain.vo.Email;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import com.lhgpds.algometa.internal.member.domain.vo.Nickname;
import com.lhgpds.algometa.internal.member.domain.vo.Role;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends DateBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email")
    @Embedded
    private Email email;

    @Column(name = "nickname")
    private Nickname nickname;

    @Column(name = "image")
    private ImageLink image;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public static Member createFirstJoinMember(Email email, Nickname nickname) {
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .role(Role.GHOST)
            .build();
    }

    public void setNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    public void setImage(ImageLink image) {
        this.image = image;
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}
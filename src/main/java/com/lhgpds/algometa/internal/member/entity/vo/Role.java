package com.lhgpds.algometa.internal.member.entity.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN","ADMIN","관리자"),
    USER("ROLE_USER","USER", "유저"),
    GHOST("ROLE_GHOST","GHOST", "유령");

    private final String roleName;
    private final String name;
    private final String detail;
}

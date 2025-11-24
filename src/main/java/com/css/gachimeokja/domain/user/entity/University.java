package com.css.gachimeokja.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum University {
    DONGGUK("동국대학교");

    private final String koreanName;
}

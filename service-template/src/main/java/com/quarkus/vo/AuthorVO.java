package com.quarkus.vo;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@RegisterForReflection
@AllArgsConstructor
@Builder
public class AuthorVO {
    private Long id;
    private String authorName;
}

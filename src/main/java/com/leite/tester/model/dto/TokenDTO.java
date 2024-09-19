package com.leite.tester.model.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String email;
    private Boolean valido;
    private Date dataAtual;
    private Date validade;
    private String token;
    private String refreshToken;
}

package com.leite.tester.security;

import java.util.Base64;
import java.util.Date;
import com.leite.tester.model.Usuario;
import com.leite.tester.model.dto.TokenDTO;
import com.leite.tester.utils.ConstantsUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
@NoArgsConstructor
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private final long validadeEmMilisegundos =  28800000; //8h

    @Autowired(required = false)
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAccessToken(Usuario usuario){
        Date dataAtual = new Date();
        Date validade = new Date(dataAtual.getTime() + validadeEmMilisegundos);

        var acessToken = getAccessToken(usuario, dataAtual, validade);
        var refreshToken = getRefreshToken(usuario, dataAtual);

        return new TokenDTO(usuario.getEmail(), true, dataAtual, validade, acessToken, refreshToken);
    }

    public TokenDTO createRefreshToken(Usuario usuario, String refreshToken){
        if(refreshToken.contains("Bearer ")) refreshToken = refreshToken.substring("Bearer ".length());
        DecodedJWT decodedJWT = decodedToken(refreshToken);
        if(decodedJWT.getExpiresAt().after(new Date())){
            return createAccessToken(usuario);
        }else{
            throw new TokenExpiredException("JWT expired", decodedJWT.getExpiresAtAsInstant());
        }
    }

    private String getAccessToken(Usuario usuario, Date dataAtual, Date validade) {
        String urlServidor = ServletUriComponentsBuilder
                .fromCurrentContextPath().build().toUriString();


        return JWT.create()
                .withClaim(ConstantsUtils.CLAIM_ID , usuario.getId())
                .withClaim(ConstantsUtils.CLAIM_PERFIL_ID, usuario.getPerfil().getId())
                .withIssuedAt(dataAtual)
                .withExpiresAt(validade)
                .withSubject(usuario.getEmail())
                .withIssuer(urlServidor)
                .sign(algorithm)
                .strip();
    }

    private String getRefreshToken(Usuario usuario, Date dataAtual) {
        Date validadeDoRefreshToken = new Date(dataAtual.getTime() + (validadeEmMilisegundos * 2));

        return JWT.create()
                .withClaim(ConstantsUtils.CLAIM_ID , usuario.getId())
                .withClaim(ConstantsUtils.CLAIM_PERFIL_ID, usuario.getPerfil().getId())
                .withIssuedAt(dataAtual)
                .withExpiresAt(validadeDoRefreshToken)
                .withSubject(usuario.getEmail())
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();

        return verifier.verify(token);
    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validadeToken(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        try{
            return !decodedJWT.getExpiresAt().before(new Date());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Jwt expirado ou inválido.");
        }
    }
}

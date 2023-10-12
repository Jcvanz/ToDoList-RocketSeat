package com.jcvanz.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jcvanz.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // pegar autenticação (usuário e sennha)
        var authorization = request.getHeader("Authorization");
        
        // substring serve para extrair uma parte de um conteudo
        var authEncoded = authorization.substring("Basic".length()).trim();
        // cria um array de bits
        byte[] authDecode = Base64.getDecoder().decode(authEncoded);
        
        var authString = new String(authDecode);
        
        // separa o username da senha
        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        System.out.println(username);        
        System.out.println(password);
        
        // validar usuário
        var user = this.userRepository.findByUsername(username);
        // faz a verificação se o usuário existe
        if(user == null) {
            // Erro de usuário não cadastrado
            response.sendError(401, "Usuário sem autorização");
        } else {
            // validar senha
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            // verifica se a senha é verdadeira
            if(passwordVerify.verified) {
                filterChain.doFilter(request, response);
            } else {
                // Erro de usuário não cadastrado
                response.sendError(401, "Usuário sem autorização");
            }

        }

    }

    
}

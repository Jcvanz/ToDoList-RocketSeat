package com.jcvanz.todolist.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

/*  Modificadores:
    - public
    - private
    - protected
*/

@RestController
@RequestMapping("/users")
public class UserController {
    // chamando a interface
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    //Tipos String (texto), Int (números inteiros), Double (números 0.0000), Float (números 0.000), Char (A, B, C), Data (data), Void (sem retorno)
    public ResponseEntity create(@RequestBody UserModel userModel) {
        // faz a verificação se o usuário já existe
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null) {
            // Mensagem de erro e status code
            return ResponseEntity.status(400).body("Usuário já cadastrado");
        };
        // cadastrar usuário ao banco
        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(200).body(userCreated);
    }
}

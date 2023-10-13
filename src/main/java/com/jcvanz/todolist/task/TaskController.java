package com.jcvanz.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcvanz.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
// passa o caminho para acessar o arquivo
@RequestMapping("/tasks")

public class TaskController {
    // gerencia a instância do repositório
    @Autowired
    private ITaskRepository taskRepository;

    // passa o caminho para criar uma nova task
    @PostMapping("/")

    // cria as tasks
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        // seta o ID do usuário dentro do TaskModel
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        // validar data
        var currentDate = LocalDateTime.now();
        // verifica se as datas de início/término não são menores que a data atual
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(500).body("A data de início/término deve ser maior do que a data atual");
        }
        // verifica se a data de início é menor que a data de término
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(500).body("A data de início deve ser menor do que a data de término");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(200).body(task);
    }

    // busca e retorna as tasks criada de cada usuário através do ID
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    // fazer updates nas tarefas criadas
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        // busca no banco de dados o ID da task
        var task = this.taskRepository.findById(id).orElse(null);
        
        // faz a validação para saber se a terefa existe
        if(task == null) {
            return ResponseEntity.status(500).body("Tarefa não encontrada");
        }

        // faz a validação se o usuário é o dono da task
        var idUser = request.getAttribute("idUser");
        if(!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(500).body("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);
        
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}

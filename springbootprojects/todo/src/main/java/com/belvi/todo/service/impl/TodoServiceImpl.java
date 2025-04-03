package com.belvi.todo.service.impl;

import com.belvi.todo.execeptions.APIException;
import com.belvi.todo.execeptions.ResourceNotFoundException;
import com.belvi.todo.model.Todo;
import com.belvi.todo.payload.TodoDTO;
import com.belvi.todo.payload.TodoResponse;
import com.belvi.todo.repository.TodoRepository;
import com.belvi.todo.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {
    //private List<Todo> todos = new ArrayList<>();
    //private Long nextId = 1L;
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public TodoServiceImpl(TodoRepository todoRepository, ModelMapper modelMapper) {
        this.todoRepository = todoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TodoResponse getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        if (todos.isEmpty()){
            throw new APIException("No todo added until now . ");
        }

        List<TodoDTO> todoDTOS = todos.stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .toList();

        TodoResponse todoResponse = new TodoResponse();
        todoResponse.setContent(todoDTOS);
        return  todoResponse;

    }


    private String normalizeStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null. Allowed values are: 'To do', 'In progress', 'Done'.");
        }

        // Supprimer les espaces et convertir en minuscules
        String normalized = status.trim().toLowerCase();

        // Comparer avec les valeurs acceptées (en minuscules)
        switch (normalized) {
            case "to do":
                return "To do";
            case "in progress":
                return "In progress";
            case "done":
                return "Done";
            default:
                throw new IllegalArgumentException("Invalid status. Allowed values are: 'To do', 'In progress', 'Done'.");
        }
    }

    @Override
    public TodoDTO addTodo(TodoDTO todoDTO) {
        Todo todo = modelMapper.map(todoDTO, Todo.class);

        // Normaliser le statut
        String status = normalizeStatus(todo.getStatus());
        todo.setStatus(status);

        Todo todoFromDb = todoRepository.findByTitle(todo.getTitle());

        if (todoFromDb != null) {
            throw new APIException("Todo with the name " + todo.getTitle() + " Already exists !!");
        }

        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoDTO.class);
    }


    @Override
    public TodoDTO deleteTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "TodoId", todoId));

        todoRepository.delete(todo);
        return modelMapper.map(todo, TodoDTO.class);
    }

    @Override
    public TodoDTO updateTodo(TodoDTO todoDTO, Long todoId) {
        Todo savedTodo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "TodoId", todoId));

        // Mappage de todoDTO à Todo
        Todo todo = modelMapper.map(todoDTO, Todo.class);

        // Normaliser le statut
        String status = normalizeStatus(todo.getStatus());
        todo.setStatus(status);

        // Mettre à jour l'ID de la todo
        todo.setTodId(todoId);

        // Enregistrer le Todo mis à jour
        savedTodo = todoRepository.save(todo);

        return modelMapper.map(savedTodo, TodoDTO.class);
    }


    @Override
    public TodoResponse getTodoByStatus(String status) {
        // Normaliser le statut
        String normalizedStatus = normalizeStatus(status);

        // Récupérer les todos depuis le repository
        List<Todo> todos = todoRepository.findByStatus(normalizedStatus);
        if (todos.isEmpty()) {
            throw new APIException("No todos found with status: " + normalizedStatus);
        }

        // Convertir les entités Todo en TodoDTO
        List<TodoDTO> todoDTOs = todos.stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());

        // Retourner une TodoResponse contenant les TodoDTO
        return new TodoResponse(todoDTOs);
    }

}

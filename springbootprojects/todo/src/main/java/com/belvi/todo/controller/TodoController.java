package com.belvi.todo.controller;

import com.belvi.todo.payload.TodoDTO;
import com.belvi.todo.payload.TodoResponse;
import com.belvi.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Todo Management", description = "API pour la gestion des tâches")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Get all Todos", description = "Returns a list of all todo items")
    @GetMapping("/public/todos")
    public ResponseEntity<TodoResponse> getAllTodos() {
        TodoResponse todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @Operation(summary = "Add todo",
            description = "Create a new todo item with the provided details. " +
                    "The request body should include the title, description, and status of the todo. " +
                    "The status must be To do, In progress or Done.")
    @PostMapping("/public/todos")
    public ResponseEntity<TodoDTO> addTodo(@Valid @RequestBody TodoDTO todoDTO){

        TodoDTO savedTodoDTo = todoService.addTodo(todoDTO);
        return new ResponseEntity<>(savedTodoDTo, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete a todo item by ID",
            description = "Delete a specific todo item from the system using its unique identifier. " +
                    "This operation is restricted to admin users.")
    @DeleteMapping("/admin/todos/{todoId}")
    public ResponseEntity<TodoDTO> deletetodo(@PathVariable Long todoId){
        TodoDTO deleteTodo = todoService.deleteTodo(todoId);
        return new ResponseEntity<>(deleteTodo, HttpStatus.OK);

    }

    @Operation(
            summary = "Update a todo item by ID",
            description = "Update an existing todo item using its unique identifier. " +
                    "The request body should include the updated title, description, and status of the todo.")
    @PutMapping("/admin/todos/{todoId}")
    public ResponseEntity<TodoDTO> updateTodo(@Valid @RequestBody TodoDTO todoDTO,
                                              @PathVariable Long todoId){
        TodoDTO savedTodoDTO = todoService.updateTodo(todoDTO, todoId);
        return new ResponseEntity<>(savedTodoDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get todo items by status",
            description = "Retrieves a list of todo items filtered by their status. " +
                    "You must provide the 'status' query parameter, which should be one of the following valid values: " +
                    "'To do', 'In progress', or 'Done'. " +
                    "The response includes all todo items that match the specified status, allowing you to easily view and manage tasks based on their current state."
    )
    @GetMapping("/public/todos/status")
    public ResponseEntity<TodoResponse> getTodoByStatus(@RequestParam String status) {
        TodoResponse todos = todoService.getTodoByStatus(status);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }





    /*@GetMapping("/public/todos")
    @Operation(summary = "Récupérer toutes les tâches",
            description = "Retourne une liste paginée de toutes les tâches")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des tâches récupérée avec succès",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<TodoResponse> getAllTodos() {
        TodoResponse todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PostMapping(value = "/public/todos", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Ajouter une nouvelle tâche",
            description = "Crée une nouvelle tâche avec les détails fournis")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tâche créée avec succès",
                    content = @Content(schema = @Schema(implementation = TodoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<TodoDTO> addTod


             @ApiResponse(responseCode = "200", description = "Tâches filtrées récupérées avec succès",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Valeur de statut invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<TodoResponse> getTodoByStatus(
            @Parameter(description = "Statut pour filtrer les tâches", required = true,
                    example = "En cours")
            @RequestParam String status) {
        TodoResponse todos = todoService.getTodoByStatus(status);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    } */
}
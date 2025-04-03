package com.belvi.todo.service;

import com.belvi.todo.model.Todo;
import com.belvi.todo.payload.TodoDTO;
import com.belvi.todo.payload.TodoResponse;

public interface TodoService {
    TodoResponse getAllTodos();

    TodoDTO addTodo(TodoDTO todoDTO);
    TodoDTO deleteTodo(Long todoId);
    TodoDTO updateTodo(TodoDTO todoDTO, Long todoId);

    TodoResponse getTodoByStatus(String status);
}

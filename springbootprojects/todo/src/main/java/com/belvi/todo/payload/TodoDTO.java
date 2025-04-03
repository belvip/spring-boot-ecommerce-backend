package com.belvi.todo.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private Long todId;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 30, message = "Todo title must contain at least five characters and not exceed 30")
    private String title;
    private String description;
    private String status;
}

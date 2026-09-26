package com.exelynt.resourcebooking.dto.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResourceRequest(

        @NotBlank(message = "Resource name is required")
        @Size(max = 150, message = "Resource name must not exceed 150 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotBlank(message = "Resource type is required")
        @Size(max = 50, message = "Resource type must not exceed 50 characters")
        String type
) {
}
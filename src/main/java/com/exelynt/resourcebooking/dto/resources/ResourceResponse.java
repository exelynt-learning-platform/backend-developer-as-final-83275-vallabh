package com.exelynt.resourcebooking.dto.resources;

public record ResourceResponse(
        Long id,
        String name,
        String description,
        String type
) {
}
package com.exelynt.resourcebooking.service.impl;

import com.exelynt.resourcebooking.dto.resources.ResourceRequest;
import com.exelynt.resourcebooking.dto.resources.ResourceResponse;
import com.exelynt.resourcebooking.entity.Resource;
import com.exelynt.resourcebooking.exception.ResourceNotFoundException;
import com.exelynt.resourcebooking.repository.ResourceRepository;
import com.exelynt.resourcebooking.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public ResourceResponse createResource(ResourceRequest request) {

        Resource resource = new Resource(
                request.name(),
                request.description(),
                request.type()
        );

        Resource savedResource = resourceRepository.save(resource);

        return mapToResponse(savedResource);
    }

    @Override
    public List<ResourceResponse> getAllResources() {

        return resourceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ResourceResponse getResourceById(Long id) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: " + id
                        )
                );

        return mapToResponse(resource);
    }

    @Override
    public ResourceResponse updateResource(Long id, ResourceRequest request) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: " + id
                        )
                );

        resource.setName(request.name());
        resource.setDescription(request.description());
        resource.setType(request.type());

        Resource updatedResource = resourceRepository.save(resource);

        return mapToResponse(updatedResource);
    }

    @Override
    public void deleteResource(Long id) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: " + id
                        )
                );

        resourceRepository.delete(resource);
    }

    private ResourceResponse mapToResponse(Resource resource) {

        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getDescription(),
                resource.getType()
        );
    }
}
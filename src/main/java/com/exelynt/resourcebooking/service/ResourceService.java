package com.exelynt.resourcebooking.service;

import com.exelynt.resourcebooking.dto.resources.ResourceRequest;
import com.exelynt.resourcebooking.dto.resources.ResourceResponse;

import java.util.List;

public interface ResourceService {

    ResourceResponse createResource(ResourceRequest request);

    List<ResourceResponse> getAllResources();

    ResourceResponse getResourceById(Long id);

    ResourceResponse updateResource(Long id, ResourceRequest request);

    void deleteResource(Long id);
}
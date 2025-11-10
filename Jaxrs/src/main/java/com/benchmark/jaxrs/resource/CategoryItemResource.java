package com.benchmark.jaxrs.resource;

import com.benchmark.jaxrs.dto.ItemDTO;
import com.benchmark.jaxrs.service.CategoryService;
import com.benchmark.jaxrs.service.ItemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Path("/categories/{categoryId}/items")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryItemResource {

    private static final Logger logger = LoggerFactory.getLogger(CategoryItemResource.class);

    @Inject
    private ItemService itemService;

    @Inject
    private CategoryService categoryService;

    @GET
    public Response getCategoryItems(@PathParam("categoryId") Long categoryId,
                                     @QueryParam("page") @DefaultValue("0") int page,
                                     @QueryParam("size") @DefaultValue("20") int size) {
        logger.info("GET /categories/{}/items?page={}&size={}", categoryId, page, size);

        // Verify category exists
        if (!categoryService.existsById(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDTO> items = itemService.findByCategoryId(categoryId, pageable);

        return Response.ok(items.getContent()).build();
    }
}

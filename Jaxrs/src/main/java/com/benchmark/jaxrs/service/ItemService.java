package com.benchmark.jaxrs.service;

import com.benchmark.jaxrs.dto.ItemDTO;
import com.benchmark.jaxrs.entity.Category;
import com.benchmark.jaxrs.entity.Item;
import com.benchmark.jaxrs.repository.CategoryRepository;
import com.benchmark.jaxrs.repository.ItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@ApplicationScoped
@Transactional
public class ItemService {

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private CategoryRepository categoryRepository;

    private boolean useJoinFetch = false; // Flag for N+1 vs JOIN FETCH

    public void setUseJoinFetch(boolean useJoinFetch) {
        this.useJoinFetch = useJoinFetch;
    }

    public Page<ItemDTO> findAll(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);
        return items.map(this::toDTO);
    }

    public Optional<ItemDTO> findById(Long id) {
        return itemRepository.findById(id).map(this::toDTO);
    }

    public Page<ItemDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        Page<Item> items;
        if (useJoinFetch) {
            items = itemRepository.findByCategoryIdWithJoin(categoryId, pageable);
        } else {
            items = itemRepository.findByCategoryId(categoryId, pageable);
        }
        return items.map(this::toDTO);
    }

    public ItemDTO save(ItemDTO itemDTO) {
        Item item = toEntity(itemDTO);
        Item saved = itemRepository.save(item);
        return toDTO(saved);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return itemRepository.existsById(id);
    }

    private ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setSku(item.getSku());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setStock(item.getStock());
        dto.setCategoryId(item.getCategory().getId());
        dto.setCategoryCode(item.getCategory().getCode());
        dto.setCategoryName(item.getCategory().getName());
        return dto;
    }

    private Item toEntity(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setSku(dto.getSku());
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setStock(dto.getStock());

        // Load category
        Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        item.setCategory(category);

        return item;
    }
}

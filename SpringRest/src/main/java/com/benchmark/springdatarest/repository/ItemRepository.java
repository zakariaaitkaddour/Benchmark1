package com.benchmark.springdatarest.repository;

import com.benchmark.springdatarest.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "items", collectionResourceRel = "items", itemResourceRel = "item")
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId")
    Iterable<Item> findByCategoryId(Long categoryId);
}

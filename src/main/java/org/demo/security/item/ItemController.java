package org.demo.security.item;

import org.demo.security.item.dto.ItemAddedDto;
import org.demo.security.item.dto.NewItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NewItemDto newItemDto) {
        String newItemLocation = itemService.create(newItemDto);
        return ResponseEntity.created(URI.create(newItemLocation)).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PatchMapping("/add")
    public ResponseEntity<ItemAddedDto> add(@RequestParam Long userId, @RequestParam Long itemId) {
        return ResponseEntity.ok(itemService.addItem(userId, itemId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ItemAddedDto>> getItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(itemService.getItemsByUser(userId));
    }
}

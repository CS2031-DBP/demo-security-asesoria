package org.demo.security.item;

import org.demo.security.exceptions.ResourceNotFoundException;
import org.demo.security.item.dto.ItemAddedDto;
import org.demo.security.item.dto.NewItemDto;
import org.demo.security.user.User;
import org.demo.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public ItemAddedDto addItem(Long userId, Long itemId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));


        user.getItems().add(item);
        userRepository.save(user);

        item.setOwner(user);
        itemRepository.save(item);

        ItemAddedDto itemAddedDto = new ItemAddedDto();
        itemAddedDto.setUserId(userId);
        itemAddedDto.setItemId(itemId);
        itemAddedDto.setItemDescription(item.getDescription());

        return itemAddedDto;
    }

    public String create(NewItemDto newItemDto) {
        Item item = new Item();
        item.setDescription(newItemDto.getDescription());
        item.setName(newItemDto.getName());

        itemRepository.save(item);

        return "/api/item/" + item.getId();
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }


    public Page<ItemAddedDto> getItemsByUser(Long userId) {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        return itemRepository.findAllByOwner_Id(userId, pageable)
                .map(item -> {
                    ItemAddedDto itemAddedDto = new ItemAddedDto();
                    itemAddedDto.setItemId(item.getId());
                    itemAddedDto.setUserId(userId);
                    itemAddedDto.setItemDescription(item.getDescription());
                    return itemAddedDto;
                });
    }
}

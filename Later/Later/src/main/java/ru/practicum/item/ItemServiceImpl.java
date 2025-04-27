package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    public List<Item> getItems(long userId) {
        return itemRepository.findByUserId(userId);
    }

    public Item addNewItem(Long userId, Item item) {
        item.setId(userId);
        return itemRepository.save(item);
    }

    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }
}

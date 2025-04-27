package ru.practicum.item;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository{
    @Override
    public List<Item> findByUserId(long userId) {
        return List.of();
    }

    @Override
    public Item save(Item item) {
        return null;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {

    }
}

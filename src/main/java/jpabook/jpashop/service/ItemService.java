package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    //변경 감지 기능 사용
    @Transactional
    public void updateItem(Long itemId, BookForm param) {
        Item findItem = itemRepository.findOne(itemId);
        Book book = (Book) findItem;

        book.setId(param.getId());
        book.setName(param.getName());
        book.setPrice(param.getPrice());
        book.setStockQuantity(param.getStockQuantity());
        book.setAuthor(param.getAuthor());
        book.setIsbn(param.getIsbn());
    }



}

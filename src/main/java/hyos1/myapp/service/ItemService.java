package hyos1.myapp.service;

import hyos1.myapp.dto.request.ItemCreateRequest;
import hyos1.myapp.dto.request.ItemUpdateRequest;
import hyos1.myapp.dto.response.ItemResponse;
import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;
import hyos1.myapp.repository.item.jpa.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 아이템 생성
     */
    @Transactional
    public ItemResponse save(ItemCreateRequest request) {
        Item savedItem = itemRepository.save(Item.createItem(request.getName(), request.getPrice(), request.getQuantity()));
        return ItemResponse.fromEntity(savedItem);
    }

    /**
     * 전체 아이템 조회
     */
    public List<ItemResponse> findAll(ItemSearchCond cond) {
        List<Item> items = itemRepository.findAll(cond);
        return items.stream()
                .map(i -> ItemResponse.fromEntity(i))
                .collect(Collectors.toList());
    }

    /**
     * 단건 아이템 조회
     */
    public ItemResponse findById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));
        return ItemResponse.fromEntity(item);
    }

    /**
     * 아이템 수정
     */
    @Transactional
    public ItemResponse updateItem(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));
        item.updatePriceAndQuantity(request.getPrice(), request.getQuantity());
        return ItemResponse.fromEntity(item);
    }

    /**
     * 아이템 삭제
     */
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("삭제할 아이템이 존재하지 않습니다. id = " + itemId));
        itemRepository.deleteItem(item);
    }
}

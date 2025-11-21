package hyos1.myapp.controller;

import hyos1.myapp.dto.request.ItemCreateRequest;
import hyos1.myapp.dto.request.ItemUpdateRequest;
import hyos1.myapp.dto.response.ItemResponse;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;
import hyos1.myapp.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // [사용자] 상품 단건 조회 ✓
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> findById(@PathVariable Long itemId) {
        ItemResponse item = itemService.findById(itemId);
        return ResponseEntity.ok(item);
    }

    // [사용자] 상품 전체 조회 ✓
    @GetMapping
    public ResponseEntity<List<ItemResponse>> findAll(@ModelAttribute ItemSearchCond cond) {
        List<ItemResponse> items = itemService.findAll(cond);
        return ResponseEntity.ok(items);
    }

    // [관리자] 상품 등록 ✓
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemCreateRequest request) {
        ItemResponse createdItem = itemService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    // [관리자] 상품 수정 ✓
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponse> update(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemUpdateRequest request) {
        ItemResponse updateItem = itemService.updateItem(itemId, request);
        return ResponseEntity.ok(updateItem);
    }

    // [관리자] 상품 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
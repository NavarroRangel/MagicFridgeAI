package dev.java10x.MagicFridgeAI.controller;

import dev.java10x.MagicFridgeAI.model.FoodItemModel;
import dev.java10x.MagicFridgeAI.repository.FoodItemRepository;
import dev.java10x.MagicFridgeAI.service.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodItemController {
    private final FoodItemRepository repository;
    private FoodItemService service;

    public FoodItemController(FoodItemService foodItemService, FoodItemRepository foodItemRepository) {
        this.service = foodItemService;
        this.repository = foodItemRepository;
    }
    //POST
    public ResponseEntity<FoodItemModel> criar(@RequestBody FoodItemModel foodItemModel){
        FoodItemModel salvo = service.salvar(foodItemModel);
        return ResponseEntity.ok(salvo);
    }

    //GET
    public List<FoodItemModel> listar(){
        return repository.findAll();
    }

    //Update

    //DELTE
}

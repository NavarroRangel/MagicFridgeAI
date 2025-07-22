package dev.java10x.MagicFridgeAI.controller;

import dev.java10x.MagicFridgeAI.model.FoodItemModel;
import dev.java10x.MagicFridgeAI.repository.FoodItemRepository;
import dev.java10x.MagicFridgeAI.service.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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


    //TODO: Listar por id
    public ResponseEntity<FoodItemModel> listarPorId(@RequestBody Long id){
        FoodItemModel buscaId = service.buscaId(id);
        return ResponseEntity.ok(buscaId);

    }


    //Update

    public ResponseEntity<FoodItemModel> alterar(@RequestBody FoodItemModel foodItemModel, Long id){
        FoodItemModel alterado = service.altera(foodItemModel, id);
        return ResponseEntity.ok(alterado);
    }

    //DELTE

    public ResponseEntity<String> deleteById(Long id){
        service.deleta(id);
        return ResponseEntity.ok("Deletado com sucesso o id " + id);
    }
}

package dev.java10x.MagicFridgeAI.controller;

import dev.java10x.MagicFridgeAI.model.FoodItemModel;
import dev.java10x.MagicFridgeAI.repository.FoodItemRepository;
import dev.java10x.MagicFridgeAI.service.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @PostMapping
    public ResponseEntity<FoodItemModel> criar(@RequestBody FoodItemModel foodItemModel){
        FoodItemModel salvo = service.salvar(foodItemModel);
        return ResponseEntity.ok(salvo);
    }

    //GET
    @GetMapping
    public ResponseEntity <List<FoodItemModel>> listar(){
        List<FoodItemModel> lista = service.listar();
        return ResponseEntity.ok(lista);
    }


    //TODO: Listar por id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<FoodItemModel>> listarPorId(@PathVariable Long id){
        Optional<FoodItemModel> buscaId = service.buscaId(id);
        return ResponseEntity.ok(buscaId);

    }


    //Update
    @PutMapping("/{id}")
    public ResponseEntity<FoodItemModel> alterar(@RequestBody  FoodItemModel foodItemModel,@PathVariable Long id){
        return service.buscaId(id)
                .map(itemExistente -> {
                    foodItemModel.setId(itemExistente.getId());
                    FoodItemModel atualizado = service.altera(foodItemModel);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());

    };

    //DELTE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        if (repository.existsById(id)) {
            Optional<FoodItemModel> buscaid = service.buscaId(id);
            String nomeProduto = buscaid.get().getNome();
            service.deleta(id);

            return ResponseEntity.ok("Deletado com sucesso o produto " + nomeProduto);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }
}

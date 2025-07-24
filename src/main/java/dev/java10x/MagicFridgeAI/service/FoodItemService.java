package dev.java10x.MagicFridgeAI.service;

import dev.java10x.MagicFridgeAI.model.FoodItemModel;
import dev.java10x.MagicFridgeAI.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private FoodItemRepository repository;

    public FoodItemService(FoodItemRepository repository) {
        this.repository = repository;
    }

    public FoodItemModel salvar(FoodItemModel foodItemModel){
        return repository.save(foodItemModel);
    }

    public List<FoodItemModel> listar(){

        return repository.findAll();
    }

    public Optional<FoodItemModel> buscaId(Long id){
        return repository.findById(id);

    }

    public FoodItemModel altera(FoodItemModel foodItemModel, Long id){
        buscaId(id);
        return repository.save(foodItemModel);

    }
    public void deleta( long id){
        repository.deleteById(id);
    }


}

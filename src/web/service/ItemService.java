package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dao.ItemMapper;
import web.pojo.Item;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemMapper mapper;
    public List<Item> getAllItem(){
        return mapper.getAllItem();
    }

    public Item getItemById(Integer id) {
        return mapper.getItemById(id);
    }

    public void updateItem(Item item) {
        mapper.updateItem(item);
    }

    public void deleteItems(String[] ids) {
        mapper.deleteItems(ids);
    }

    public void addItem(Item item) {
        mapper.addItem(item);
    }
}


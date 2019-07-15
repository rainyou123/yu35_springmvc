package web.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import web.pojo.Item;
import web.service.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
//有@Controller的原因是 创造个对象才能调里面的方法
public class ItemController {
//    @Autowired
//    private JdbcTemplate jt;
    @Autowired
    private ItemService itemService;
    @RequestMapping("/itemList.do")
    public ModelAndView getItemList(){
//        String sql = "select * from items";
//        List<Item> itemList = jt.query(sql, new RowMapper<Item>() {
//            @Override
//            public Item mapRow(ResultSet rs, int i) throws SQLException {
//                Item item = new Item();
//                item.setId(rs.getInt("id"));
//                item.setName(rs.getString("name"));
//                item.setPrice(rs.getDouble("price"));
//                item.setDetail(rs.getString("detail"));
//                item.setCreatetime(rs.getDate("createtime"));
//                return item;
//            }
//        });
        List<Item> itemList = itemService.getAllItem();
        ModelAndView mav = new ModelAndView();
        mav.addObject("itemList" , itemList);
//        mav.setViewName("WEB-INF/jsp/itemList.jsp");
        mav.setViewName("itemList");
        return mav;
    }

    @RequestMapping("itemEdit.do")
    public ModelAndView itemEdit(@RequestParam(value = "id" , defaultValue = "1" , required = false) Integer id , Boolean status, HttpServletRequest request , HttpServletResponse response , HttpSession session , Model model){
        Item item = itemService.getItemById(id);
        ModelAndView mav = new ModelAndView();
        mav.addObject("item" , item);
        mav.setViewName("editItem");
        return mav;
    }

    @RequestMapping("/updateItem.do")
    public ModelAndView updateItem(@RequestParam(value = "pictureFile" , required = false) MultipartFile pictureFile , Item item , HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        //使用随机串新命名一个文件
        String newFileName = UUID.randomUUID().toString().replace("-","");
        //获取上传文件的路径
        String realPath = request.getServletContext().getRealPath("image");
        //获取文件的扩展名 jpg , png
        String extension = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
        //上传文件
        pictureFile.transferTo(new File(realPath +"/" + newFileName + "." + extension));
        //存文件名称至对象
        item.setPic(newFileName + "." + extension);

        itemService.updateItem(item);
        ModelAndView mav = new ModelAndView();
  //      List<Item> itemList = itemService.getAllItem();
 //       mav.addObject("itemList" , itemList);
  //      mav.setViewName("itemList");
//        int i = 1/0; 自杀行为 进入全局异常捕获器
        mav.addObject("item" , item);
        mav.setViewName("editItem");
        return mav;

    }

    @RequestMapping("/deleteItems.do")
    public ModelAndView deleteItems(String[] ids) {
        itemService.deleteItems(ids);
        ModelAndView mav = new ModelAndView();
        List<Item> itemList = itemService.getAllItem();
        mav.addObject("itemList" , itemList);
        mav.setViewName("itemList");
        return mav;

    }
    //spring的特点不用 req去接参数了   public ModelAndView deleteItems(String[] ids) {直接就可以接基本数据类型 和Sting
    //例如            Product product=new Product();
    //            BeanUtils.populate(product,req.getParameterMap());


    @RequestMapping("/addUI.do")
    public String addUI(){
        return "addItem";
    }

    @RequestMapping("/addItem.do")
    public String addItem(Item item , Model model){
        item.setCreatetime(new Date());
        itemService.addItem(item);
        List<Item> itemList = itemService.getAllItem();
        model.addAttribute("itemList" , itemList);
        return "itemList";
    }
}
//    Item item = itemService.getItemById(dsdds);
//        System.out.println("status= " + status);
//       /* ModelAndView mav = new ModelAndView();
//        mav.addObject("item" , item);
//        mav.setViewName("editItem");*/
//                model.addAttribute("item" , item);
//                return "editItem";

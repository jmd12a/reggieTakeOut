package com.example.ruiji_takeout.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.ruiji_takeout.app.pojo.AddressBook;
import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.app.service.AddressBookService;
import com.example.ruiji_takeout.common.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-19:12
 * @Description：
 */

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddressBook(@SessionAttribute("user") User user){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, user.getId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook == null){
            R.error("没有找到该对象");
        }

        return R.success(addressBook);
    }

    @PutMapping("/default")
    public R updateDefaultAddressBook(@RequestBody AddressBook addressBook, @SessionAttribute("user") User user){


        Long id = user.getId();
        // 首先把原本是默认的地址修改为非默认的

        boolean update = addressBookService.updateDefaultAddressBook(addressBook,id);

        if (update) return R.success(null);

        return R.error("更新失败");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> getAddressBookListByUserId(@SessionAttribute("user") User user){

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,user.getId());
        List<AddressBook> addressBookList =  addressBookService.list(queryWrapper);


        return R.success(addressBookList);
    }

    @PostMapping
    public R saveAddressBook(@RequestBody AddressBook addressBook, @SessionAttribute("user") User user){

        addressBook.setUserId(user.getId());

        boolean save = addressBookService.save(addressBook);

        if (save){
            return R.success(null);
        }

        return R.error("添加地址失败");
    }

    @GetMapping("/{addressBookId}")
    public R<AddressBook> getAddressBookById(@PathVariable Long addressBookId){
        AddressBook addressBook = addressBookService.getById(addressBookId);

        return R.success(addressBook);
    }

    @PutMapping
    public R updateAddressBookById(@RequestBody AddressBook addressBook){
        boolean b = addressBookService.updateById(addressBook);

        if (b){
            return R.success(null);
        }else return R.error("更新失败");
    }
}

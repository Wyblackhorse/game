package com.oxo.ball.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxo.ball.bean.dao.BallMenu;
import com.oxo.ball.service.admin.BallMenuService;
import com.oxo.ball.service.player.IPlayerBetService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController()
@RequestMapping("test")
public class TestController {

    @Resource
    BallMenuService ballMenuService;

    @Resource
    IPlayerBetService betService;

    @GetMapping("orderno")
    public Object orderNo(){
        return betService.getDayOrderNo();
    }
    @GetMapping("menu")
    public Object testMenu(){
        List<BallMenu> all = ballMenuService.findAll();
        List<Menu> tree = new ArrayList<>();
        for(BallMenu ballMenu:all){
            //一级菜单放入
            if(ballMenu.getParentId()==0){
                tree.add(Menu.builder()
                        .id(ballMenu.getId())
                        .label(ballMenu.getMenuName())
                        .children(new ArrayList<>())
                        .sort(ballMenu.getIsMenu())
                        .build());
            }
        }
        for(BallMenu ballMenu:all){
            if(ballMenu.getParentId()==0){
                continue;
            }
            for(Menu menu:tree){
                if(menu.getId().equals(ballMenu.getParentId())){
                    //放入二级
                    List<Menu> children = menu.getChildren();
                    children.add(Menu.builder()
                            .id(ballMenu.getId())
                            .label(ballMenu.getMenuName())
                            .children(new ArrayList<>())
                            .build());
                }
            }
        }
        for(BallMenu ballMenu:all){
            if(ballMenu.getParentId()==0){
                continue;
            }
            //
            for(Menu menu:tree){
                //获得第一层子菜单
                List<Menu> children1 = menu.getChildren();
                for(Menu child:children1){
                    if(child.getId().equals(ballMenu.getParentId())){
                        //放入三级
                        List<Menu> last = child.getChildren();
                        last.add(Menu.builder()
                                .id(ballMenu.getId())
                                .label(ballMenu.getMenuName())
                                .children(new ArrayList<>())
                                .build());
                    }
                }
            }
        }
        Collections.sort(tree, Comparator.comparingInt(Menu::getSort));
        return tree;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Menu{
//        id: 9,
//        label: '系统管理',
//        children
        private Long id;
        private String label;
        private List<Menu> children;
        @JsonIgnore
        private Integer sort;
    }
}

package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.LoginUser;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.service.posts.PostsService;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) { //서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있다.
        model = userInfo(model,user);
        return "index";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, @LoginUser SessionUser user){
        model = userInfo(model,user);
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        //글쓴이일 경우
        boolean owner;
        if(user == null) owner = false;
        else {
            owner = (user.getName().equals(dto.getAuthor()))?true:false;
        }
        model.addAttribute("owner", owner);

        return "view";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user){
        model = userInfo(model,user);
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user){
        model = userInfo(model,user);

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    public Model userInfo(Model model, @LoginUser SessionUser user){
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userPicture", user.getPicture());
            model.addAttribute("userEmail", user.getEmail());
        }
        return model;
    }

}

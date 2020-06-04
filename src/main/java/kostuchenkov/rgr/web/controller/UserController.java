package kostuchenkov.rgr.web.controller;

import kostuchenkov.rgr.data.domain.product.Product;
import kostuchenkov.rgr.data.domain.user.User;
import kostuchenkov.rgr.service.ProductService;
import kostuchenkov.rgr.service.UserService;
import kostuchenkov.rgr.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private WishListService wishListService;
    @Autowired
    private ProductService productService;

    @GetMapping("/{id:\\d+}")
    public String userPage(@AuthenticationPrincipal User session, @PathVariable("id") User user, Model model) {
        if(user.getId() == session.getId()) {
            //TODO если id сесии и пользователя равны, то личная страничка, если нет, то чужая
        }
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/{id:\\d+}/wishlist")
    public String wishlist(@AuthenticationPrincipal User session, @PathVariable("id") User user, Model model) {
        if(user.getId() == session.getId()){
            //TODO если id сесии и пользователя равны, то личный вишлист, если нет, то чужой(проверка на доступ к вишлисту)
        }
        model.addAttribute("wishlist", user.getWishList());
        return "wishlist";
    }

    @GetMapping("/wishlist/add")
    @ResponseBody
    public String addToWishList(@AuthenticationPrincipal User user, @RequestParam("productId") Product product) {

        wishListService.addToWishList(user, product);
        return "ok"; //??оставим?
    }

    @GetMapping("/wishlist/clear")
    @ResponseBody
    public String clearWishList(@AuthenticationPrincipal User user) {
        wishListService.clearWishList(user);
        return "ok"; //?? отправляем сообщение пробабли
    }

    @GetMapping("/wishlist/delete")
    @ResponseBody
    public String deleteFromWishList(@AuthenticationPrincipal User user, @RequestParam("productId") Product product) {

        wishListService.deleteFromWishList(user, product);
        return "ok";
    }
}

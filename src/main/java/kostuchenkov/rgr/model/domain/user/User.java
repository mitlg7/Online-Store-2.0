package kostuchenkov.rgr.model.domain.user;

import kostuchenkov.rgr.model.domain.cartItem.CartItem;
import kostuchenkov.rgr.model.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String contactInfo;
    private String avatar;
    private int balance;

    @Column(columnDefinition = "boolean default false")
    private boolean verified;
    private String activationCode;

    @Temporal(value = TemporalType.DATE)
    private Date birthday;

    @Enumerated(EnumType.STRING)
    private UserWishListAccess wishListAccess;
    @Enumerated(EnumType.STRING)
    private UserLocale userLocale;

    @ManyToMany
    @JoinTable(name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> wishList = new ArrayList<>();

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @OneToMany(fetch = FetchType.LAZY)
    private List<CartItem> cart = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isVerified();
    }

    public int getCartTotal() {
        int cartTotal = 0;
        for(CartItem cartItem : cart) {
            cartTotal += cartItem.getProduct().getPrice();
        }
        return cartTotal;
    }

    public boolean isSeller(){
        if(roles.contains(UserRole.SELLER)){
            return true;
        }else {
            return false;
        }
    }
}
package kostuchenkov.rgr.model.service.order;

import freemarker.template.TemplateException;
import kostuchenkov.rgr.model.domain.cartItem.CartItem;
import kostuchenkov.rgr.model.domain.order.Order;
import kostuchenkov.rgr.model.domain.order.OrderPayment;
import kostuchenkov.rgr.model.domain.order.OrderStatus;
import kostuchenkov.rgr.model.domain.user.User;
import kostuchenkov.rgr.model.repository.OrderRepository;
import kostuchenkov.rgr.model.repository.UserRepository;
import kostuchenkov.rgr.model.service.cart.CartService;
import kostuchenkov.rgr.model.service.mail.MailService;
import kostuchenkov.rgr.web.utils.validation.OrderCheckoutForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private MailService mailService;

    @Override
    public boolean createOrder(User user, OrderCheckoutForm checkoutForm) throws TemplateException, IOException, MessagingException {
        user = userRepository.findByUsername(user.getUsername());

        int sum = 0 ;
        for (CartItem cartItem : user.getCart() ){
            sum += cartItem.getProduct().getPrice() * cartItem.getAmount();
        }

        Order order = new Order();
        BeanUtils.copyProperties(checkoutForm, order);

        order.setContact(checkoutForm.getContact());
        order.setDate(new Date());
        order.setUser(user);
        order.setTotal(sum);
        order.setOrderStatus(OrderStatus.PENDING);

        order.getProducts().addAll(user.getCart());

        //Забирем нужное количество обуви со склада
        order.getProducts().forEach(CartItem::subtractFromProduct);

        if(order.getOrderPayment().equals(OrderPayment.BALANCE)){
            if (user.getBalance() < order.getTotal())
                return false;
            else
                user.setBalance(user.getBalance()-order.getTotal());
        }

        mailService.createOrderMail(order);
        cartService.clearCart(user);
        orderRepository.save(order);
        return true;
    }

    @Override
    public List<Order> getAllOrderOfUser(User user){
        user = userRepository.findByUsername(user.getUsername());
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getAllOrders(){
        return (List<Order>) orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersWithStatus(OrderStatus orderStatus){
        return orderRepository.findByOrderStatus(orderStatus);
    }

    @Override
    public void setStatus(Order order, OrderStatus orderStatus) throws TemplateException, IOException, MessagingException {
        order.setOrderStatus(orderStatus);
        mailService.changeStatusOrder(order);
        orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(int orderId){
        return orderRepository.findById(orderId);
    }
}

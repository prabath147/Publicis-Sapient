package com.order.service.controller;

import com.order.service.client.notify.EmailClient;
import com.order.service.client.pharmacy.StoreClient;
import com.order.service.dto.CartDto;
import com.order.service.dto.OrdersDto;
import com.order.service.dto.PageableResponse;
import com.order.service.dtoexternal.EmailDto;
import com.order.service.service.CartService;
import com.order.service.service.OrdersService;
import com.order.service.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order/order")
public class OrdersController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StoreClient storeClient;

    @Autowired
    private EmailClient emailClient;
    
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping(value = "/get-order-details/{id}")
    public ResponseEntity<OrdersDto> getOrder(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long orderId) {
        if (!jwtUtils.verifyId(jwt, ordersService.getOrder(orderId).getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only get their own order details!");
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.getOrder(orderId));
    }

    @GetMapping(value = "/get-order-history/{id}")
    public ResponseEntity<PageableResponse<OrdersDto>> getOrderHistory(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    ) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only get their own order history!");
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.getOrders(userId, pageNumber, pageSize));
    }

    @PostMapping(value = "/set-order-details/{id}")
    public ResponseEntity<OrdersDto> setOrderDetails(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long userId, @RequestBody OrdersDto ordersDto) {
        matchIdWithJwt(jwt, userId, ordersDto);
        return getOrdersDtoResponseEntity(userId, ordersDto);
    }

    private ResponseEntity<OrdersDto> getOrdersDtoResponseEntity(@PathVariable("id") Long userId, @RequestBody OrdersDto ordersDto) {
        OrdersDto ordersDto1 = new OrdersDto(userId, ordersDto.getItems(), ordersDto.getOrderDetails(), ordersDto.getOrderAddress(), ordersDto.isOptionalOrderDetails(), ordersDto.getDeliveryDate());
        CartDto cartDto;
        if(ordersDto1.getItems() != null)
            cartDto = new CartDto(ordersDto1.getItems());
        else cartDto = cartService.getCart(userId);
        cartDto = storeClient.checkout(cartDto).getBody();
        assert cartDto != null;
        ordersDto1.setItems(cartDto.getItems());
        ordersDto1.setPrice(cartDto.getPrice());
        ordersDto1.setQuantity(cartDto.getQuantity());

        return ResponseEntity.status(HttpStatus.CREATED).body(ordersDto1);
    }

    @PostMapping(value = "/set-order-details-with-delivery/{id}")
    public ResponseEntity<OrdersDto> setOrderDetailsWithDelivery(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long userId, @RequestBody OrdersDto ordersDto, @RequestParam(value = "oneDayDelivery", defaultValue = "false", required = false) boolean oneDayDelivery) {
        matchIdWithJwt(jwt, userId, ordersDto);
        if(oneDayDelivery) ordersDto.setDeliveryDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return getOrdersDtoResponseEntity(userId, ordersDto);
    }

    private void matchIdWithJwt(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long userId, @RequestBody OrdersDto ordersDto) {
        if (!jwtUtils.verifyId(jwt, ordersDto.getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only set their own order details!");
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only set their own order details!");

        if (!ordersDto.isOptionalOrderDetails()) {
            ordersDto.setOrderDetails(null);
        }
    }

    @PostMapping(value = "/place-order")
    public ResponseEntity<OrdersDto> placeOrder(@RequestHeader("Authorization") String jwt, @RequestBody OrdersDto ordersDto) {
        if (!jwtUtils.verifyId(jwt, ordersDto.getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only set their own order details!");


        CartDto cartDto = new CartDto(ordersDto.getUserId(), ordersDto.getItems(), ordersDto.getQuantity(), ordersDto.getPrice());
        storeClient.buyCart(cartDto);

        List<EmailDto> emailDtoList = new ArrayList<>();
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(ordersDto.getUserId());
        Double total=ordersDto.getPrice()+40;
        emailDto.setEmailSubject("Your Order Details");
        emailDto.setEmailBody("<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "<title></title>\r\n"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
                + "<link href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@800&display=swap\" rel=\"stylesheet\">\r\n"
                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n"
                + "<style type=\"text/css\">\r\n"
                + "    \r\n"
                + "\r\n"
                + "body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }\r\n"
                + "table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }\r\n"
                + "img { -ms-interpolation-mode: bicubic; }\r\n"
                + "\r\n"
                + "img { border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }\r\n"
                + "table { border-collapse: collapse !important; }\r\n"
                + "body { height: 100% !important; margin: 0 !important; padding: 0 !important; width: 100% !important; }\r\n"
                + "\r\n"
                + "\r\n"
                + "a[x-apple-data-detectors] {\r\n"
                + "    color: inherit !important;\r\n"
                + "    text-decoration: none !important;\r\n"
                + "    font-size: inherit !important;\r\n"
                + "    font-family: inherit !important;\r\n"
                + "    font-weight: inherit !important;\r\n"
                + "    line-height: inherit !important;\r\n"
                + "}\r\n"
                + "\r\n"
                + "@media screen and (max-width: 480px) {\r\n"
                + "    .mobile-hide {\r\n"
                + "        display: none !important;\r\n"
                + "    }\r\n"
                + "    .mobile-center {\r\n"
                + "        text-align: center !important;\r\n"
                + "    }\r\n"
                + "}\r\n"
                + "div[style*=\"margin: 16px 0;\"] { margin: 0 !important; }\r\n"
                + "</style>\r\n"
                + "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
                + "\r\n"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
                + "    <tr>\r\n"
                + "        <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
                + "        \r\n"
                + "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
                + "            <tr>\r\n"
                + "                <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#dae5f7\">\r\n"
                + "               \r\n"
                + "                    <div style=\"display:inline-block;vertical-align:top; width:100%;\">\r\n"
                + "                        <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
                + "                            <tr>\r\n"
                + "                                <a href=\"https://d1j9yqk9s11go2.cloudfront.net/\"><img src=\"https://i.ibb.co/K9Jbw7H/logo.png\" alt=\"\"  width=\"100%\" height=\"350\" ></a>\r\n"
                + "                            </tr>\r\n"
                + "                        </table>\r\n"
                + "                    </div>\r\n"
                + "                </td>\r\n"
                + "            </tr>\r\n"
                + "            <tr>\r\n"
                + "                <td align=\"center\" style=\"padding: 5px 35px 5px 35px; background-color: #ffffff; bgcolor:#ffffff\" >\r\n"
                + "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px; background: #ffffff;\">\r\n"
                + "                    <tr>\r\n"
                + "                        <td align=\"center\" style=\"font-family: 'Poppins', sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding-top: 25px;\">\r\n"
                + "                            <img src=\"https://img.icons8.com/fluency/344/ok.png\" width=\"125\" height=\"120\" style=\"display: block; border: 0px;\" /><br>\r\n"
                + "                            <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #000; margin: 0;\">\r\n"
                + "                                Thank you for your order\r\n"
                + "                            </h2>\r\n"
                + "                        </td>\r\n"
                + "                    </tr>\r\n"
                + "                    <tr>\r\n"
                + "                        <td align=\"left\" >\r\n"
                + "                            <p style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 200; line-height: 24px; color: #524e4e; \">\r\n"
                + "                               Your order has been received and is now being processed. Your order details are shown below for your reference.\r\n"
                + "                            </p>\r\n"
                + "                        </td>\r\n"
                + "                    </tr>\r\n"
                + "                    <tr>\r\n"
                + "                        <td align=\"left\" style=\"padding-top: 20px; bgcolor:#524e4e\">\r\n"
                + "                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
                + "                                <tr>\r\n"
                + "                                    <td width=\"75%\" align=\"left\" bgcolor=\"#ffffff\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">\r\n"
                + "                                        Order Confirmation \r\n"
                + "                                    </td>\r\n"
                + "                                    <td width=\"25%\" align=\"left\" bgcolor=\"#ffffff\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">\r\n"
                + "                                        Price\r\n"
                + "                                    </td>\r\n"
                + "                                </tr>\r\n"
                + "                                <tr>\r\n"
                + "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\">\r\n"
                + "                                        Items\r\n"
                + "                                    </td>\r\n"
                + "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\">\r\n"
                + "                                        ₹"+ordersDto.getPrice()+"\r\n"
                + "                                    </td>\r\n"
                + "                                </tr>\r\n"
                + "\r\n"
                + "                                <tr>\r\n"
                + "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\r\n"
                + "                                        Delivery Charges\r\n"
                + "                                    </td>\r\n"
                + "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\r\n"
                + "                                        ₹40.00\r\n"
                + "                                    </td>\r\n"
                + "                                </tr>\r\n"
                + "                            </table>\r\n"
                + "                        </td>\r\n"
                + "                    </tr>\r\n"
                + "                    <tr>\r\n"
                + "                        <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
                + "                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
                + "                                <tr>\r\n"
                + "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\">\r\n"
                + "                                        Total\r\n"
                + "                                    </td>\r\n"
                + "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\">\r\n"
                + "                                        ₹"+total+"\r\n"
                + "                                    </td>\r\n"
                + "                                </tr>\r\n"
                + "                            </table>\r\n"
                + "                        </td>\r\n"
                + "                    </tr>\r\n"
                + "                </table>\r\n"
                + "                \r\n"
                + "                </td>\r\n"
                + "            </tr>\r\n"
                + "             <tr>\r\n"
                + "                <td align=\"center\" height=\"100%\" valign=\"top\" width=\"100%\" style=\"padding: 0 35px 35px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
                + "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:660px;\">\r\n"
                + "                    <tr>\r\n"
                + "                        <td align=\"center\" valign=\"top\" style=\"font-size:0;\">\r\n"
                + "                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
                + "\r\n"
                + "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
                + "                                    <tr>\r\n"
                + "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px;\">\r\n"
                + "                                            <p style=\"font-weight: 800;\">Delivery Address</p>\r\n"
                + "                                            <p>"+ordersDto.getOrderAddress().getStreet()+"<br>"+ordersDto.getOrderAddress().getCity()+"<br>"+ordersDto.getOrderAddress().getState()+","+ordersDto.getOrderAddress().getCountry()+" "+ordersDto.getOrderAddress().getPinCode()+"</p>\r\n"
                + "\r\n"
                + "                                        </td>\r\n"
                + "                                    </tr>\r\n"
                + "                                </table>\r\n"
                + "                            </div>\r\n"
                + "                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
                + "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
                + "                                    <tr>\r\n"
                + "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px;\">\r\n"
                + "                                            <p style=\"font-weight: 800;\">Estimated Delivery Date</p>\r\n"
                + "                                            <p>"+ LocalDate.now().plusDays(3)+"</p>\r\n"
                + "                                        </td>\r\n"
                + "                                    </tr>\r\n"
                + "                                </table>\r\n"
                + "                            </div>\r\n"
                + "                        </td>\r\n"
                + "                    </tr>\r\n"
                + "                </table>\r\n"
                + "                </td>\r\n"
                + "            </tr>\r\n"
                + "        </table>\r\n"
                + "    </tr>\r\n"
                + "</table>\r\n"
                + "    \r\n"
                + "</body>\r\n"
                + "</html>");
        emailDtoList.add(emailDto);
        emailClient.sendBulkEmail(emailDtoList);
        cartService.emptyCartItems(ordersDto.getUserId(), ordersDto.getItems());

        return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.createOrder(ordersDto));
    }

    @DeleteMapping(value = "/delete-order/{id}")
    public ResponseEntity<String> deleteOrder(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long orderId) {
        if (!jwtUtils.verifyId(jwt, ordersService.getOrder(orderId).getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only set their own order details!");
        ordersService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Order Deleted Successfully!");
    }

    @DeleteMapping(value = "/delete-order-history/{id}")
    public ResponseEntity<String> deleteOrderHistory(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long userId) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only set their own order details!");
        ordersService.deleteOrderHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body("User Order History Cleared!");
    }

    @PostMapping(value = "/delete-order")
    public ResponseEntity<String> deleteOrder(@RequestHeader("Authorization") String jwt, @RequestBody List<Long> orderList) {
        ordersService.deleteAllOrder(jwt, orderList);
        return ResponseEntity.status(HttpStatus.OK).body("Orders Deleted Successfully!");
    }
    
    @GetMapping(value = "/total-orders")
    public ResponseEntity<Long> getAllOrder() {
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.getAllOrders());
    }


}

package com.order.service.service.implementation;

import com.order.service.client.auth.AuthClient;
import com.order.service.client.notify.EmailClient;
import com.order.service.client.pharmacy.ItemClient;
import com.order.service.client.pharmacy.StoreClient;
import com.order.service.controller.OrdersController;
import com.order.service.dto.AddressDto;
import com.order.service.dto.CartDto;
import com.order.service.dto.ItemDto;

import com.order.service.dto.OrdersDto;
import com.order.service.dto.PageableResponse;
import com.order.service.dto.ProductDto;
import com.order.service.dto.RepeatOrderDto;
import com.order.service.dto.RepeatOrderItemDto;
import com.order.service.dtoexternal.EmailDto;
import com.order.service.dtoexternal.JwtResponse;
import com.order.service.dtoexternal.LoginRequest;
import com.order.service.exception.ResourceException;
import com.order.service.model.RepeatOrder;
import com.order.service.repository.RepeatOrderRepository;
import com.order.service.service.OrdersService;
import com.order.service.service.RepeatOrderService;
import com.order.service.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class RepeatOrderServiceImplementation implements RepeatOrderService {

    public static final String REPEAT_ORDER = "RepeatOrder";
    @Autowired
    OrdersService ordersService;
    @Autowired
    ItemClient itemClient;
    @Autowired
    AuthClient authClient;
    @Autowired
    private RepeatOrderRepository repeatOrderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private StoreClient storeClient;

    @Override
    public RepeatOrderDto createOptIn(RepeatOrderDto repeatOrderDto) {
        try {
            RepeatOrder repeatOrder = modelMapper.map(repeatOrderDto, RepeatOrder.class);
            return modelMapper.map(repeatOrderRepository.save(repeatOrder), RepeatOrderDto.class);
        } catch (Exception e) {
            throw new ResourceException(REPEAT_ORDER, "repeatOrderDto", repeatOrderDto,
                    ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public RepeatOrderDto updateOptIn(RepeatOrderDto repeatOrderDto) {

        Optional<RepeatOrder> repeatOrder = repeatOrderRepository.findById(repeatOrderDto.getId());
        if (repeatOrder.isEmpty()) {
            throw new ResourceException(REPEAT_ORDER, "repeatOrderDto", repeatOrderDto,
                    ResourceException.ErrorType.FOUND);
        }
        try {

            return modelMapper.map(repeatOrderRepository.save(modelMapper.map(repeatOrderDto, RepeatOrder.class)),
                    RepeatOrderDto.class);
        } catch (Exception e) {
            throw new ResourceException(REPEAT_ORDER, "repeatOrder", repeatOrderDto,
                    ResourceException.ErrorType.CREATED, e);
        }

    }

    public AddressDto setOrderAddress(AddressDto addressDto) {
		AddressDto address = new AddressDto();
		address.setCity(addressDto.getCity());
		address.setCountry(addressDto.getCountry());
		address.setPinCode(addressDto.getPinCode());
		address.setState(addressDto.getState());
		address.setStreet(addressDto.getStreet());
		return address;
	}
    
    public OrdersDto getOptInIdAndPlaceOrder(RepeatOrderDto repeatOrderDto, String bearerToken) {
        Double price = 0D;
        OrdersDto order = new OrdersDto();
        Set<ItemDto> itemDtoSet = new HashSet<>();
        for (ProductDto product : repeatOrderDto.getRepeatOrderItems()) {
            List<ItemDto> items = getItemByProductId(bearerToken, product);
            for (ItemDto item : items) {
                if (item.getItemIdFk() != null) {
                    price += item.getPrice() * item.getItemQuantity();
                    itemDtoSet.add(item);
                }
            }
        }
        if (price > 0) {
            order.setItems(itemDtoSet);
            order.setOrderAddress(setOrderAddress(repeatOrderDto.getAddress()));
            order.setPrice(price);
            order.setUserId(repeatOrderDto.getUserId());
            order.setQuantity((long) itemDtoSet.size());
        
            OrdersDto finalOrder = new OrdersDto();
            CartDto cartDto = new CartDto(order.getUserId(), order.getItems(), order.getQuantity(), order.getPrice());
            storeClient.buyCart(bearerToken, cartDto);
            finalOrder = ordersService.createOrder(order);
            repeatOrderDto.setNumberOfDeliveries(repeatOrderDto.getNumberOfDeliveries() - 1);
            if (repeatOrderDto.getNumberOfDeliveries() > 0) {
                repeatOrderDto
                        .setDeliveryDate(repeatOrderDto.getDeliveryDate().plusDays(repeatOrderDto.getIntervalInDays()));
            } else {
                repeatOrderDto.setDeliveryDate(repeatOrderDto.getDeliveryDate());
            }
            updateOptIn(repeatOrderDto);
            return finalOrder;
        }
        return new OrdersDto();
    }

    @Override
    public void getOptInToSendNotification(LocalDate date) {

        List<EmailDto> emailList = new ArrayList<>();
        String token;
        List<RepeatOrderDto> repeatOrderDtoList = new ArrayList<>();
        List<RepeatOrder> allRepeatOrders = repeatOrderRepository.findAllByDeliveryDate(date.plusDays(3));
        for (RepeatOrder repeatOrder : allRepeatOrders) {
            repeatOrderDtoList.add(modelMapper.map(repeatOrder, RepeatOrderDto.class));
        }
        if (!repeatOrderDtoList.isEmpty()) {
            LoginRequest login = new LoginRequest();
            login.setUsername("admin0");
            login.setPassword("admin0");
            ResponseEntity<JwtResponse> response = authClient.authenticateUser(login);
            token = response.getBody().getType() + " " + response.getBody().getToken();
            for (RepeatOrderDto repeatOrder : repeatOrderDtoList) {
                if (repeatOrder.getNumberOfDeliveries() > 0) {
                    OrdersDto order = getOptInIdAndPlaceOrder(repeatOrder, token);
                    if (order.getOrderId() != null) {
                        EmailDto emailDto = new EmailDto();
                        emailDto.setUserId(repeatOrder.getUserId());
                        Double total=order.getPrice()+40;
                        emailDto.setEmailSubject("Your OptIn Details");
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
                                + "                    <div style=\"display:inline-block;  vertical-align:top; width:100%;\">\r\n"
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
                                + "                                        ₹"+order.getPrice()+"\r\n"
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
                                + "                                            <p>"+order.getOrderAddress().getStreet()+"<br>"+order.getOrderAddress().getCity()+"<br>"+order.getOrderAddress().getState()+","+order.getOrderAddress().getCountry()+" "+order.getOrderAddress().getPinCode()+"</p>\r\n"
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
                        emailList.add(emailDto);
                    }
                }
            }
            if (!emailList.isEmpty()) {
                emailClient.sendBulkEmail(token, emailList);

            }
            authClient.logout(token);
        }

    }

    public List<ItemDto> getItemByProductId(String token, ProductDto productDto) {
        Long quantity = productDto.getQuantity();
        List<ItemDto> items = new ArrayList<>();
        ResponseEntity<PageableResponse<ItemDto>> result = null;
        int i = 0;
        while (quantity > 0) {
            result = itemClient.getItemsByProductId(token, i, 1, productDto.getProductIdFk());

            PageableResponse<ItemDto> res = result.getBody();
            if (res.getData().isEmpty()) {
                return items;
            } else {
                for (ItemDto obj : res.getData()) {
                    ItemDto item = new ItemDto();
                    if (obj.getItemQuantity() > 0 && quantity >= obj.getItemQuantity()) {
                        item.setItemIdFk(obj.getItemId());
                        item.setPrice(obj.getPrice());
                        item.setItemQuantity(obj.getItemQuantity());
                        quantity -= item.getItemQuantity();
                    } else if (obj.getItemQuantity() > quantity) {
                        item.setItemIdFk(obj.getItemId());
                        item.setPrice(obj.getPrice());
                        item.setItemQuantity(quantity);
                        quantity -= item.getItemQuantity();
                    }
                    items.add(item);
                    i++;
                }

            }
        }
        return items;
    }

    @Override
    public PageableResponse<RepeatOrderDto> getAllOptInByUserId(Long userId, Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<RepeatOrder> repeatOrdersPage = repeatOrderRepository.findAllByUserId(userId, requestedPage);
        List<RepeatOrderDto> repeatOrderDtoList = new ArrayList<>();
        for (RepeatOrder store : repeatOrdersPage.getContent())
            repeatOrderDtoList.add(modelMapper.map(store, RepeatOrderDto.class));
        PageableResponse<RepeatOrderDto> pageableRepeatOrder = new PageableResponse<>();
        return pageableRepeatOrder.setResponseData(repeatOrderDtoList, repeatOrdersPage);
    }

    @Override
    public RepeatOrderDto getOptInById(Long optInId) {
        Optional<RepeatOrder> optionalRepeatOrder = repeatOrderRepository.findById(optInId);
        if (optionalRepeatOrder.isEmpty())
            throw new ResourceException("OptIn", "ID", optInId, ResourceException.ErrorType.FOUND);
        return modelMapper.map(optionalRepeatOrder.get(), RepeatOrderDto.class);
    }

    @Override
    public void deleteOptIn(Long optInId) {
        Optional<RepeatOrder> optionalOrders = repeatOrderRepository.findById(optInId);
        if (optionalOrders.isEmpty())
            throw new ResourceException("Order", "ID", optInId, ResourceException.ErrorType.FOUND);
        try {
            repeatOrderRepository.deleteById(optInId);
        } catch (Exception e) {
            throw new ResourceException("OptIn", "ID", optInId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public void deleteAllOptIn(List<Long> optInIdList) {
        repeatOrderRepository.deleteAllById(optInIdList);
    }

    @Override
    public void deleteOptInHistory(Long userId) {
        repeatOrderRepository.deleteAll(repeatOrderRepository.findAllByUserId(userId));
    }

}

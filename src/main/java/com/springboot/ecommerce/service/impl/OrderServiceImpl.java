package com.springboot.ecommerce.service.impl;

import com.google.zxing.WriterException;
import com.springboot.ecommerce.QRCode.QRCodeGenerator;
import com.springboot.ecommerce.dto.OrderResponse;
import com.springboot.ecommerce.entity.*;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.payload.CommentDto;
import com.springboot.ecommerce.payload.OrderDetailRequest;
import com.springboot.ecommerce.payload.OrderRequest;
import com.springboot.ecommerce.repository.*;
import com.springboot.ecommerce.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository repo;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<User> listAll() {
        return repo.findAll();
    }

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest, Long userId) throws MessagingException, UnsupportedEncodingException {
        //        Convert DTO Request to entity
//        Product product = modelMapper.map(productRequest, Product.class);
        Set<OrderDetailRequest> orderDetailRequestSet = new HashSet<>();
        orderDetailRequestSet = orderRequest.getOrderDetails();
        Set<OrderDetail> orderDetails = new HashSet<>();
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setOrderDetails(null);
        // retrieve category entity by id
//        Category category = categoryRepository.findById(categoryId).orElseThrow(
//                ()-> new ResourceNotFoundException("Category", "id", categoryId));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );

        // set category to product entity
//        product.setCategory(category);
        order.setUser(user);

        Float shippingCost = 10F;
        order.setShippingCost(shippingCost);

        String orderStatus = "Pending";
        order.setOrderStatus(orderStatus);



//        Save child object orderDetail

//        for(OrderDetailRequest orderDetailRequest1: orderDetailRequestSet){
//            OrderDetail orderDetail  = new OrderDetail();
//            orderDetail = modelMapper.map(orderDetailRequest1, OrderDetail.class);
//
//            Product product = productRepository.findById(orderDetailRequest1.getProductId()).orElseThrow(
//                    ()-> new ResourceNotFoundException("Product", "id", orderDetailRequest1.getProductId())
//            );
////             orderDetailRequest1.getProductId();
//            orderDetail.setOrder(order);
//            orderDetail.setProduct(product);
//            orderDetails.add(orderDetail);
//        }
//        order.setOrderDetails(orderDetails);

        //        save entity to repository
//        Product newProduct = productRepository.save(product);
//        Order newOrder = orderRepository.save(order);
        orderRepository.save(order);

        Float productCostTotal = 0F;




        //        Convert entity to DTO Response
//        return modelMapper.map(newProduct, ProductResponse.class);
        for (OrderDetailRequest orderDetailRequest1 : orderDetailRequestSet) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail = modelMapper.map(orderDetailRequest1, OrderDetail.class);

//            orderDetail.setQuanlity(orderDetailRequest1.getQuanlity());

            Long productId = orderDetailRequest1.getProductId();

            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new ResourceNotFoundException("Product", "id", productId)
            );
//             orderDetailRequest1.getProductId();

            orderDetail.setProduct(product);
            orderDetail.setOrder(order);

            Float productCost = product.getPrice();
            orderDetail.setProductCost(productCost);

            Float subTotal = orderDetail.getQuantity() * productCost;
            orderDetail.setSubTotal(subTotal);

            String productName = product.getName();
            orderDetail.setName(productName);

            productCostTotal = productCostTotal + subTotal;

            orderDetailRepository.save(orderDetail);
//            orderDetails.add(orderDetail);
        }
//        order.setOrderDetails(orderDetails);

        order.setProductCost(productCostTotal);
//        Order newOrder = orderRepository.save(order);

        Float totalCost = shippingCost + productCostTotal;
//        order.setTotalCost(totalCost);

//        QR Code Generate =========================================================
            Long orderId = order.getId();
            String byteArray = "http://localhost:8080/api/order/" + orderId;
            String imageFolder = "http://localhost:8080/api/order/" + orderId;

            byte[] image = new byte[0];
            try {

                // Generate and Return Qr Code in Byte Array
                image = QRCodeGenerator.getQRCodeImage(byteArray,250,250);


                String QR_CODE_IMAGE_PATH = "./src/main/resources/static/ImageQR/" + orderId + ".png";

                // Generate and Save Qr Code Image in static/image folder
                QRCodeGenerator.generateQRCodeImage(imageFolder,250,250,QR_CODE_IMAGE_PATH);

            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
            // Convert Byte Array into Base64 Encode String
            String qrcode = Base64.getEncoder().encodeToString(image);

            order.setQrCode(qrcode);

//        =========================================================



        Order newOrder = orderRepository.save(order);


//        ===================================================
        String to = "westudymore@gmail.com";
        String from = "westudymore@gmail.com";
        String senderName = "Loc Trinh";
        String subject = "[Ecommerce] Order delivered successfully";

        String bodyPlainText = "Text Body";
        String contentId = "";
        String bodyHtml =


                "  <!doctype html>\n" +
                        "  <html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                        "\n" +
                        "  <head>\n" +
                        "    <title>\n" +
                        "\n" +
                        "    </title>\n" +
                        "    <!--[if !mso]><!-- -->\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <!--<![endif]-->\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                        "    <style type=\"text/css\">\n" +
                        "      #outlook a {\n" +
                        "        padding: 0;\n" +
                        "      }\n" +
                        "\n" +
                        "      .ReadMsgBody {\n" +
                        "        width: 100%;\n" +
                        "      }\n" +
                        "\n" +
                        "      .ExternalClass {\n" +
                        "        width: 100%;\n" +
                        "      }\n" +
                        "\n" +
                        "      .ExternalClass * {\n" +
                        "        line-height: 100%;\n" +
                        "      }\n" +
                        "\n" +
                        "      body {\n" +
                        "        margin: 0;\n" +
                        "        padding: 0;\n" +
                        "        -webkit-text-size-adjust: 100%;\n" +
                        "        -ms-text-size-adjust: 100%;\n" +
                        "      }\n" +
                        "\n" +
                        "      table,\n" +
                        "      td {\n" +
                        "        border-collapse: collapse;\n" +
                        "        mso-table-lspace: 0pt;\n" +
                        "        mso-table-rspace: 0pt;\n" +
                        "      }\n" +
                        "\n" +
                        "      img {\n" +
                        "        border: 0;\n" +
                        "        height: auto;\n" +
                        "        line-height: 100%;\n" +
                        "        outline: none;\n" +
                        "        text-decoration: none;\n" +
                        "        -ms-interpolation-mode: bicubic;\n" +
                        "      }\n" +
                        "\n" +
                        "      p {\n" +
                        "        display: block;\n" +
                        "        margin: 13px 0;\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "    <!--[if !mso]><!-->\n" +
                        "    <style type=\"text/css\">\n" +
                        "      @media only screen and (max-width:480px) {\n" +
                        "        @-ms-viewport {\n" +
                        "          width: 320px;\n" +
                        "        }\n" +
                        "        @viewport {\n" +
                        "          width: 320px;\n" +
                        "        }\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "    <!--<![endif]-->\n" +
                        "    <!--[if mso]>\n" +
                        "    <xml>\n" +
                        "      <o:OfficeDocumentSettings>\n" +
                        "        <o:AllowPNG/>\n" +
                        "        <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                        "      </o:OfficeDocumentSettings>\n" +
                        "    </xml>\n" +
                        "    <![endif]-->\n" +
                        "    <!--[if lte mso 11]>\n" +
                        "    <style type=\"text/css\">\n" +
                        "      .outlook-group-fix { width:100% !important; }\n" +
                        "    </style>\n" +
                        "    <![endif]-->\n" +
                        "\n" +
                        "\n" +
                        "    <style type=\"text/css\">\n" +
                        "      @media only screen and (min-width:480px) {\n" +
                        "        .mj-column-per-100 {\n" +
                        "          width: 100% !important;\n" +
                        "        }\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "\n" +
                        "\n" +
                        "    <style type=\"text/css\">\n" +
                        "    </style>\n" +
                        "\n" +
                        "  </head>\n" +
                        "\n" +
                        "  <body style=\"background-color:#f9f9f9;\">\n" +
                        "\n" +
                        "\n" +
                        "  <div style=\"background-color:#f9f9f9;\">\n" +
                        "\n" +
                        "\n" +
                        "    <!--[if mso | IE]>\n" +
                        "    <table\n" +
                        "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"\n" +
                        "    >\n" +
                        "      <tr>\n" +
                        "        <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                        "    <![endif]-->\n" +
                        "\n" +
                        "\n" +
                        "    <div style=\"background:#f9f9f9;background-color:#f9f9f9;Margin:0px auto;max-width:600px;\">\n" +
                        "\n" +
                        "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#f9f9f9;background-color:#f9f9f9;width:100%;\">\n" +
                        "        <tbody>\n" +
                        "        <tr>\n" +
                        "          <td style=\"border-bottom:#333957 solid 5px;direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\">\n" +
                        "            <!--[if mso | IE]>\n" +
                        "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                        "\n" +
                        "              <tr>\n" +
                        "\n" +
                        "              </tr>\n" +
                        "\n" +
                        "            </table>\n" +
                        "            <![endif]-->\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        </tbody>\n" +
                        "      </table>\n" +
                        "\n" +
                        "    </div>\n" +
                        "\n" +
                        "\n" +
                        "    <!--[if mso | IE]>\n" +
                        "    </td>\n" +
                        "    </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <table\n" +
                        "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"\n" +
                        "    >\n" +
                        "      <tr>\n" +
                        "        <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                        "    <![endif]-->\n" +
                        "\n" +
                        "\n" +
                        "    <div style=\"background:#fff;background-color:#fff;Margin:0px auto;max-width:600px;\">\n" +
                        "\n" +
                        "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#fff;background-color:#fff;width:100%;\">\n" +
                        "        <tbody>\n" +
                        "        <tr>\n" +
                        "          <td style=\"border:#dddddd solid 1px;border-top:0px;direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\">\n" +
                        "            <!--[if mso | IE]>\n" +
                        "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                        "\n" +
                        "              <tr>\n" +
                        "\n" +
                        "                <td\n" +
                        "                        style=\"vertical-align:bottom;width:600px;\"\n" +
                        "                >\n" +
                        "            <![endif]-->\n" +
                        "\n" +
                        "            <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:13px;text-align:left;direction:ltr;display:inline-block;vertical-align:bottom;width:100%;\">\n" +
                        "\n" +
                        "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:bottom;\" width=\"100%\">\n" +
                        "\n" +
                        "<!--                  logo-->\n" +
                        "<!--                <tr>-->\n" +
                        "<!--                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                        "\n" +
                        "<!--                    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"border-collapse:collapse;border-spacing:0px;\">-->\n" +
                        "<!--                      <tbody>-->\n" +
                        "<!--                      <tr>-->\n" +
                        "<!--                        <td style=\"width:64px;\">-->\n" +
                        "\n" +
                        "<!--                          <img height=\"auto\" src=\"https://i.imgur.com/KO1vcE9.png\" style=\"border:0;display:block;outline:none;text-decoration:none;width:100%;\" width=\"64\" />-->\n" +
                        "\n" +
                        "<!--                        </td>-->\n" +
                        "<!--                      </tr>-->\n" +
                        "<!--                      </tbody>-->\n" +
                        "<!--                    </table>-->\n" +
                        "\n" +
                        "<!--                  </td>-->\n" +
                        "<!--                </tr>-->\n" +
                        "\n" +
                        "                <tr>\n" +
                        "                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                        "\n" +
                        "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:24px;font-weight:bold;line-height:22px;text-align:center;color:#525252;\">\n" +
                        "                      Thank you for your order\n" +
                        "                    </div>\n" +
                        "\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "\n" +
                        "  <!--              <tr>-->\n" +
                        "  <!--                <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                        "\n" +
                        "  <!--                  <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:left;color:#525252;\">-->\n" +
                        "  <!--                    <p>Hi John,</p>-->\n" +
                        "\n" +
                        "  <!--                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam volutpat ut est ac dignissim. Donec pulvinar ligula metus, sed imperdiet quam pretium at. Cras finibus hendrerit magna nec euismod. Ut eget-->\n" +
                        "  <!--                      justo vel enim ultrices pharetra. Morbi tellus libero, sollicitudin pulvinar porta ac, auctor sed neque.</p>-->\n" +
                        "  <!--                  </div>-->\n" +
                        "\n" +
                        "  <!--                </td>-->\n" +
                        "  <!--              </tr>-->\n" +
                        "\n" +
                        "  <!--              Start-->\n" +
                        "                <tr>\n" +
                        "                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                        "\n" +
                        "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:left;color:#525252;\">\n" +
                        "<!--                      <p>Invoice #: [[order]]<br/>-->\n" +
                        "<!--                        Created: January 1, 2015</p>-->\n" +
                        "                      <p>Invoice #: 00000[[order]]<br />\n" +
                        "                          Created time: [[created_time]]\n" +
                        "                      </p>\n" +
                        "\n" +
                        "                      <p>Name: [[name]]<br />\n" +
                        "                          Address: [[address]]<br />\n" +
                        "                          Phone number : [[phone_number]]<br />\n" +
                        "                          Email: [[email]]\n" +
                        "                      </p>\n" +
                        "\n" +
                        "                    </div>\n" +
                        "\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "\n" +
                        "  <!--              End-->\n" +
                        "                <tr>\n" +
                        "                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                        "\n" +
                        "                    <table 0=\"[object Object]\" 1=\"[object Object]\" 2=\"[object Object]\" border=\"0\" style=\"cellspacing:0;color:#000;font-family:'Helvetica Neue',Arial,sans-serif;font-size:13px;line-height:22px;table-layout:auto;width:100%;\">\n" +
                        "                      <tr style=\"border-bottom:1px solid #ecedee;text-align:left;\">\n" +
                        "                        <th style=\"padding: 0 15px 10px 0;\">Product Name</th>\n" +
                        "                        <th style=\"padding: 0 15px 10px 0;\">Product Price</th>\n" +
                        "                        <th style=\"padding: 0 15px;\">Quantity</th>\n" +
                        "                        <th style=\"padding: 0 0 0 15px;\" align=\"right\">Sub Price</th>\n" +
                        "                      </tr>\n" +
                        "\n" +
                        "\n" +
                        "<!--                      <tr>-->\n" +
                        "<!--                        <td style=\"padding: 5px 15px 5px 0;\">[[product_name]]</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 15px 5px 0;\">[[product_cost]]</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 15px;\">[[quantity]]</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[sub_total]]</td>-->\n" +
                        "<!--                      </tr>-->\n" +
                        "                      <!--orderDetails-->\n" +
                        "<!--                      <tr>-->\n" +
                        "<!--                        <td style=\"padding: 5px 15px 5px 0;\">Item number 1</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 15px 5px 0;\">test</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 15px;\">1</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">$100,00</td>-->\n" +
                        "<!--                      </tr>-->\n" +
                        "\n" +
                        "                      <tr style=\"border-top:1px solid #ecedee;text-align:left;\">\n" +
                        "<!--                          <td style=\"padding: 0 15px 5px 0;\">Sub Total</td>-->\n" +
                        "                          <td style=\"padding: 0 15px 5px 0; font-weight:bold\">Sub Total</td>\n" +
                        "                          <td style=\"padding: 0 15px 5px 0;\"></td>\n" +
                        "                          <td style=\"padding: 0 15px;\"></td>\n" +
                        "                          <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[product_cost]]</td>\n" +
                        "                      </tr>\n" +
                        "\n" +
                        "                      <tr>\n" +
                        "\n" +
                        "<!--                        <td style=\"padding: 0 15px 5px 0;\">Shipping Cost</td>-->\n" +
                        "                          <td style=\"padding: 0 15px 5px 0; font-weight:bold\">Shipping Cost</td>\n" +
                        "<!--                          <td style=\"padding: 5px 15px 5px 0; font-weight:bold\">TOTAL</td>-->\n" +
                        "                        <td style=\"padding: 0 15px 5px 0;\"></td>\n" +
                        "                        <td style=\"padding: 0 15px;\"></td>\n" +
                        "                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[shipping_cost]]</td>\n" +
                        "                      </tr>\n" +
                        "<!--                      <tr style=\"border-bottom:2px solid #ecedee;text-align:left;padding:15px 0;\">-->\n" +
                        "<!--                        <td style=\"padding: 0 15px 5px 0;\">Sales Tax</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 15px 5px 0;\"></td>-->\n" +
                        "<!--                        <td style=\"padding: 0 15px;\">1</td>-->\n" +
                        "<!--                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">$10,00</td>-->\n" +
                        "<!--                      </tr>-->\n" +
                        "                      <tr style=\"border-bottom:2px solid #ecedee;text-align:left;padding:15px 0;\">\n" +
                        "                        <td style=\"padding: 5px 15px 5px 0; font-weight:bold\">TOTAL</td>\n" +
                        "                        <td style=\"padding: 0 15px 5px 0;\"></td>\n" +
                        "                        <td style=\"padding: 0 15px;\"></td>\n" +
                        "                        <td style=\"padding: 0 0 0 15px; font-weight:bold\" align=\"right\">[[total_cost]]</td>\n" +
                        "                      </tr>\n" +
                        "                    </table>\n" +
                        "\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "\n" +
                        "<!--                <tr>-->\n" +
                        "<!--                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                        "\n" +
                        "<!--                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;line-height:16px;text-align:left;color:#a2a2a2;\">-->\n" +
                        "<!--                      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam volutpat ut est ac dignissim. Donec pulvinar ligula metus, sed imperdiet quam pretium at.</p>-->\n" +
                        "<!--                    </div>-->\n" +
                        "\n" +
                        "<!--                  </td>-->\n" +
                        "<!--                </tr>-->\n" +
                        "\n" +
                        "                <tr>\n" +
                        "<!--                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                        "                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                        "\n" +
                        "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:24px;font-weight:bold;line-height:22px;text-align:center;color:#525252;\">\n" +
                        "                      Scan QR Code\n" +
                        "                    </div>\n" +
                        "\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "\n" +
                        "                <tr>\n" +
                        "<!--                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                        "                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                        "\n" +
                        "<!--                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:left;color:#525252;\">-->\n" +
                        "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:center;color:#525252;\">\n" +
                        "                      <img src=\"cid:qrImage\" alt=\"qr code\">\n" +
                        "\n" +
                        "                    </div>\n" +
                        "\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "\n" +
                        "<!--                  Ship-->\n" +
                        "<!--                <tr>-->\n" +
                        "<!--                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;padding-top:30px;padding-bottom:50px;word-break:break-word;\">-->\n" +
                        "\n" +
                        "<!--                    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"border-collapse:separate;line-height:100%;\">-->\n" +
                        "<!--                      <tr>-->\n" +
                        "<!--                        <td align=\"center\" bgcolor=\"#2F67F6\" role=\"presentation\" style=\"border:none;border-radius:3px;color:#ffffff;cursor:auto;padding:15px 25px;\" valign=\"middle\">-->\n" +
                        "<!--                          <p style=\"background:#2F67F6;color:#ffffff;font-family:'Helvetica Neue',Arial,sans-serif;font-size:15px;font-weight:normal;line-height:120%;Margin:0;text-decoration:none;text-transform:none;\">-->\n" +
                        "<!--                            <a href=\"\" style=\"color:#fff; text-decoration:none\">-->\n" +
                        "<!--                              Check Shipping Status</a>-->\n" +
                        "<!--                          </p>-->\n" +
                        "<!--                        </td>-->\n" +
                        "<!--                      </tr>-->\n" +
                        "<!--                    </table>-->\n" +
                        "\n" +
                        "<!--                  </td>-->\n" +
                        "<!--                </tr>-->\n" +
                        "\n" +
                        "                <tr>\n" +
                        "                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                        "\n" +
                        "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:20px;text-align:left;color:#525252;\">\n" +
                        "                      Best regards,<br><br> Loc Trinh<br>Software Developer<br>\n" +
                        "<!--                      <a href=\"https://www.htmlemailtemplates.net\" style=\"color:#2F67F6\">htmlemailtemplates.net</a>-->\n" +
                        "                    </div>\n" +
                        "\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "\n" +
                        "              </table>\n" +
                        "\n" +
                        "            </div>\n" +
                        "\n" +
                        "            <!--[if mso | IE]>\n" +
                        "            </td>\n" +
                        "\n" +
                        "            </tr>\n" +
                        "\n" +
                        "            </table>\n" +
                        "            <![endif]-->\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        </tbody>\n" +
                        "      </table>\n" +
                        "\n" +
                        "    </div>\n" +
                        "\n" +
                        "\n" +
                        "    <!--[if mso | IE]>\n" +
                        "    </td>\n" +
                        "    </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <table\n" +
                        "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"\n" +
                        "    >\n" +
                        "      <tr>\n" +
                        "        <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                        "    <![endif]-->\n" +
                        "\n" +
                        "\n" +
                        "    <div style=\"Margin:0px auto;max-width:600px;\">\n" +
                        "\n" +
                        "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"width:100%;\">\n" +
                        "        <tbody>\n" +
                        "        <tr>\n" +
                        "          <td style=\"direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\">\n" +
                        "            <!--[if mso | IE]>\n" +
                        "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                        "\n" +
                        "              <tr>\n" +
                        "\n" +
                        "                <td\n" +
                        "                        style=\"vertical-align:bottom;width:600px;\"\n" +
                        "                >\n" +
                        "            <![endif]-->\n" +
                        "\n" +
                        "            <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:13px;text-align:left;direction:ltr;display:inline-block;vertical-align:bottom;width:100%;\">\n" +
                        "\n" +
                        "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\">\n" +
                        "                <tbody>\n" +
                        "                <tr>\n" +
                        "                  <td style=\"vertical-align:bottom;padding:0;\">\n" +
                        "\n" +
                        "                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\">\n" +
                        "\n" +
                        "                      <tr>\n" +
                        "                        <td align=\"center\" style=\"font-size:0px;padding:0;word-break:break-word;\">\n" +
                        "\n" +
                        "<!--                          <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;font-weight:300;line-height:1;text-align:center;color:#575757;\">-->\n" +
                        "<!--                            Some Firm Ltd, 35 Avenue. City 10115, USA-->\n" +
                        "<!--                          </div>-->\n" +
                        "\n" +
                        "                          <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;font-weight:300;line-height:1;text-align:center;color:#575757;\">\n" +
                        "                            02 Vo Oanh, 25 Ward, Binh Thanh District, Ho Chi Minh City, Vietnam\n" +
                        "                          </div>\n" +
                        "\n" +
                        "                        </td>\n" +
                        "                      </tr>\n" +
                        "\n" +
                        "                      <tr>\n" +
                        "                        <td align=\"center\" style=\"font-size:0px;padding:10;word-break:break-word;\">\n" +
                        "\n" +
                        "                          <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;font-weight:300;line-height:1;text-align:center;color:#575757;\">\n" +
                        "                            <a href=\"\" style=\"color:#575757\">Unsubscribe</a> from our emails\n" +
                        "                          </div>\n" +
                        "\n" +
                        "                        </td>\n" +
                        "                      </tr>\n" +
                        "\n" +
                        "                    </table>\n" +
                        "\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "              </table>\n" +
                        "\n" +
                        "            </div>\n" +
                        "\n" +
                        "            <!--[if mso | IE]>\n" +
                        "            </td>\n" +
                        "\n" +
                        "            </tr>\n" +
                        "\n" +
                        "            </table>\n" +
                        "            <![endif]-->\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        </tbody>\n" +
                        "      </table>\n" +
                        "\n" +
                        "    </div>\n" +
                        "\n" +
                        "\n" +
                        "    <!--[if mso | IE]>\n" +
                        "    </td>\n" +
                        "    </tr>\n" +
                        "    </table>\n" +
                        "    <![endif]-->\n" +
                        "\n" +
                        "\n" +
                        "  </div>\n" +
                        "\n" +
                        "  </body>\n" +
                        "\n" +
                        "  </html>";

        String base64Image = order.getQrCode();

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(from, senderName);
        helper.setTo(to);
        helper.setSubject(subject);


        //        Replace text mail
        bodyHtml = bodyHtml.replace("[[name]]", user.getName());
        bodyHtml = bodyHtml.replace("[[address]]", order.getAddress());
        bodyHtml = bodyHtml.replace("[[phone_number]]", user.getPhoneNumber());
        bodyHtml = bodyHtml.replace("[[email]]", user.getEmail());

        Long orderId1 = order.getId();
        String order_Id = Long.toString(orderId1);
        bodyHtml = bodyHtml.replace("[[order]]", order_Id);


        Date date = order.getOrderTime();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss");
        String strDate = dateFormat.format(date);
        bodyHtml = bodyHtml.replace("[[created_time]]", strDate);

        float shippingCost1 = order.getShippingCost();
        long shippingCostL = (long) shippingCost1;
        String shipping_Cost = String.valueOf(shippingCostL);

        bodyHtml = bodyHtml.replace("[[shipping_cost]]", shipping_Cost);

        float productCost = order.getProductCost();
        long productCostL = (long) productCost;
        String product_cost = String.valueOf(productCostL);
        bodyHtml = bodyHtml.replace("[[product_cost]]", product_cost);

        float totalCost1 = order.getTotalCost();
        long totalCostL = (long) totalCost1;
//        String total_cost = Float.toString(totalCost);
        String total_cost = String.valueOf(totalCostL);
        bodyHtml = bodyHtml.replace("[[total_cost]]", total_cost);

        List<OrderDetail> orderDetails1 = orderDetailRepository.findByOrderId(order.getId());
        for (OrderDetail orderDetail : orderDetails1) {
//            add html item + <!--orderDetails-->
            String list_product =

                    "<tr>\n" +
                            "                        <td style=\"padding: 5px 15px 5px 0;\">[[product_name]]</td>\n" +
                            "                        <td style=\"padding: 0 15px 5px 0;\">[[product_cost]]</td>\n" +
                            "                        <td style=\"padding: 0 15px;\">[[quantity]]</td>\n" +
                            "                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[sub_total]]</td>\n" +
                            "                      </tr>\n" +
                            "<!--orderDetails-->";

            bodyHtml = bodyHtml.replace("<!--orderDetails-->", list_product);
//            replace value: productName, productCost, quantity, subTotal

            bodyHtml = bodyHtml.replace("[[product_name]]", orderDetail.getName());

//            Float productCostItem = orderDetail.getProductCost();
//            String product_cost_item = Float.toString(productCostItem);
//            bodyHtml = bodyHtml.replace("[[product_cost]]", product_cost_item);

            float productCostItem = orderDetail.getProductCost();
            long productCostItemL = (long) productCostItem;
            String product_cost_item = String.valueOf(productCostItemL);

            bodyHtml = bodyHtml.replace("[[product_cost]]", product_cost_item);

            float quantityItem = orderDetail.getQuantity();
            long quantityItemL = (long) quantityItem;
//            String quantity_item = Float.toString(quantityItem);
            String quantity_item = String.valueOf(quantityItemL);
            bodyHtml = bodyHtml.replace("[[quantity]]", quantity_item);

            float subProductItem = orderDetail.getSubTotal();
            long subProductItemL = (long) subProductItem;
            String sub_product_item = String.valueOf(subProductItemL);
//            String sub_product_item = Float.toString(subProductItem);
            bodyHtml = bodyHtml.replace("[[sub_total]]", sub_product_item);
//            check i=n => delete [[add_product]]
        }

//        Off: Replace text mail

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(bodyPlainText, "text/plain; charset=UTF-8");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(bodyHtml, "text/html; charset=UTF-8");

        Multipart multiPart = new MimeMultipart("alternative");

        // create a new imagePart and add it to multipart so that the image is inline attached in the email
        byte[] rawImage = Base64.getDecoder().decode(base64Image);
        BodyPart imagePart = new MimeBodyPart();
        ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage, "image/png");

        imagePart.setDataHandler(new DataHandler(imageDataSource));
        imagePart.setHeader("Content-ID", "<qrImage>");
        imagePart.setFileName("someFileName.png");

        multiPart.addBodyPart(imagePart);
        multiPart.addBodyPart(textPart);
        multiPart.addBodyPart(htmlPart);

        message.setContent(multiPart);


//        helper.setText(content, true);
        mailSender.send(message);





        return modelMapper.map(newOrder, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());
    }

//    @Override
//    public OrderResponse getOrderById(Long orderId) {
////        retrieved order by id
//        Order order = orderRepository.findById(orderId).orElseThrow(
//                () -> new ResourceNotFoundException("Order", "id", orderId)
//        );
////        return order by id
//        return modelMapper.map(order, OrderResponse.class);
//    }

    @Override
    public OrderRequest getOrderById(Long orderId) {
//        retrieved order by id
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );
//        return order by id
        return modelMapper.map(order, OrderRequest.class);
    }

    @Override
    public List<OrderResponse> getOrderByUserId(Long userId) {
//        retrieved order by user id
        List<Order> orders = orderRepository.findByUserId(userId);
//        return convert to list of order entity to order DTO response
        return orders.stream().map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());

    }

//    @Override
//    public List<OrderResponse> getOrderByAccessToken(String accessToken) {
////        retrieved order by user id
//        List<Order> orders = orderRepository.findByAccessToken(accessToken);
////        return convert to list of order entity to order DTO response
//        return orders.stream().map(order -> modelMapper.map(order, OrderResponse.class))
//                .collect(Collectors.toList());
//
//    }

    //    Admin - Comfirm order
    public OrderResponse comfirmOrderById(Long id) {
//      get order by id
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id)
        );

        String status = order.getOrderStatus();
        String cancel = "Cancel";

        if(status.equals(cancel)){
//        save entity to database
            Order updateOrder = orderRepository.save(order);

//        mapper entity to DTO Response
            return modelMapper.map(updateOrder, OrderResponse.class);
        } else{
            //      update order status
            String orderStatus = "Accepted";
            order.setOrderStatus(orderStatus);

//        save entity to database
            Order updateOrder = orderRepository.save(order);

//        mapper entity to DTO Response
            return modelMapper.map(updateOrder, OrderResponse.class);
        }

    }

    //    Admin - Cancel order
    public OrderResponse cancelOrderById(Long id) {
//      get order by id
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id)
        );

//      update order status
        String orderStatus = "Cancel";
        order.setOrderStatus(orderStatus);

//        save entity to database
        Order updateOrder = orderRepository.save(order);

//        mapper entity to DTO Response
        return modelMapper.map(updateOrder, OrderResponse.class);
    }

    //    Shipper - Delivering order
    public OrderResponse deliveringOrderById(Long id) {
//      get order by id
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id)
        );

        String status = order.getOrderStatus();
        String cancel = "Cancel";

        if(status.equals(cancel)){
//        save entity to database
            Order updateOrder = orderRepository.save(order);

//        mapper entity to DTO Response
            return modelMapper.map(updateOrder, OrderResponse.class);
        } else{
            //      update order status
            String orderStatus = "Delivering";
            order.setOrderStatus(orderStatus);

//        save entity to database
            Order updateOrder = orderRepository.save(order);

//        mapper entity to DTO Response
            return modelMapper.map(updateOrder, OrderResponse.class);
        }
    }

    //    Shipper - Done order product
    public OrderResponse doneOrderById(Long id) throws MessagingException, UnsupportedEncodingException {
//      get order by id
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id)
        );

        String status = order.getOrderStatus();
        String cancel = "Cancel";

        if(status.equals(cancel)){
//        save entity to database
            Order updateOrder = orderRepository.save(order);

//        mapper entity to DTO Response
            return modelMapper.map(updateOrder, OrderResponse.class);
        } else {


//      update order status
            String orderStatus = "Done";
            order.setOrderStatus(orderStatus);

//        save entity to database
            Order updateOrder = orderRepository.save(order);

//    Add send mail function
//      get user by id
            Long userId = order.getUser().getId();
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", id)
            );


//======================================
//
//        String toAddress = user.getEmail();
//        String toAddress = "loc.trinhd@homecredit.vn";
//        String fromAddress = "westudymore@gmail.com";
//        String senderName = "Loc Trinh";
//        String subject = "Order delivered successfully";
////        String content = "Dear [[name]],<br>"
////                + "Please click the link below to verify your registration:<br>"
////                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
////                + "Thank you,<br>"
////                + "Trinh Loc.";
//
////        order.getId();
//
//        String content =
//                "<!DOCTYPE html>\n" +
//                        "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
//                        "<head>\n" +
//                        "\t<meta charset=\"ISO-8859-1\">\n" +
//                        "\t<title>Welcome to Home</title>\n" +
//                        "\t<link rel=\"stylesheet\" type=\"text/css\" href=\"/webjars/bootstrap/css/bootstrap.min.css\" />\n" +
//                        "</head>\n" +
//                        "<body>\n" +
//                        "\t<div class=\"container text-center\">\n" +
//                        "\t\t<h1>Welcome to Home Page</h1>\n" +
//                        "<!--\t\t<h3><a th:href=\"@{/users}\">List of Users</a></h3>-->\n" +
//                        "<!--\t\t<h3><a th:href=\"@{/register}\">Register</a></h3>-->\n" +
//                        "<!--\t\t<h3><a th:href=\"@{/login}\">Login</a></h3>-->\n" +
//                        "\t\t<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPoAAAD6AQAAAACgl2eQAAABbklEQVR4Xu2XUYoEIQxEAx6gj+TVPVIfQOjNq9gL3bvM9xRYOIPE50eRRO24PmvEO/LSBkobKG2gtIHS9wAz0HVG0yCcOtwA5iN/GWzXmAdzBb2AERlMj6wmNtK4KZD/CbSzuwMZb5fyZQkwzWTlmNTbuINWgNofm/d4t78DsHT2Gb08/soIIK5DOA1m42SaRJoBuRpB1+PxJE3BUeAGKK7ja82ra7yATA1DR3H1S3m0A5SvkEFSlsDzEHMACFJmU4zMLr9OQPRGpZXZTlhbzAA50lFMy1T5PZPlAfBiV70Fq6xojxcwZI15fYOgh00HIEtOX09Tz8Vc1C434Kgx1lH890JxAWSNeyQvRzIl0gooVeEF7xPSV0EjYGIsCFbXQK6dTgB54eydulnuiR0wuAcpNuIyTdYsAUmZImvybghQbOoa9f5/yfpygOlcn06jbsanTQdARTZlrfFopH1YsAI+aAOlDZQ2UNpAyQP4AUyuz1yyLA1dAAAAAElFTkSuQmCC\">\n" +
//                        "\t\t<img src=\"https://images.unsplash.com/photo-1575936123452-b67c3203c357?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aW1hZ2V8ZW58MHx8MHx8&w=1000&q=80\">\n" +
//                        "\t\t<img src=\"data:image/png;base64,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\">\n" +
//                        "<!--\t\t<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA-->\n" +
//                        "<!--AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO-->\n" +
//                        "<!--9TXL0Y4OHwAAAABJRU5ErkJggg==\" alt=\"Red dot\">-->\n" +
//                        "<!--\t\t<img alt=\"Embedded Image\" height=\"128\" width=\"128\" src=\"data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA-->\n" +
//                        "<!--AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO-->\n" +
//                        "<!--9TXL0Y4OHwAAAABJRU5ErkJggg==\" />-->\n" +
//                        "\t\t<html><body><img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA\n" +
//                        "AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO\n" +
//                        "9TXL0Y4OHwAAAABJRU5ErkJggg==\"></body></html>\n" +
//                        "\n" +
//                        "\t</div>\n" +
//                        "\t\n" +
//                        "</body>\n" +
//                        "</html>";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setFrom(fromAddress, senderName);
//        helper.setTo(toAddress);
//        helper.setSubject(subject);
//
//        content = content.replace("[[name]]", user.getName());
//
//        content = content.replace("[[address]]", order.getAddress());
//
//        Long orderId = order.getId();
//        String order_Id = Long.toString(orderId);
//
//        content = content.replace("[[order]]", order_Id);
//
//        Float shippingCost = order.getShippingCost();
//        String shipping_Cost = Float.toString(shippingCost);
//
//        content = content.replace("[[shipping_cost]]", shipping_Cost);
//
//        Float productCost = order.getProductCost();
//        String product_cost = Float.toString(productCost);
//        content = content.replace("[[product_cost]]", product_cost);
//
//
//
//        helper.setText(content, true);
//
//        mailSender.send(message);

            //======================================


//        ===============================================
//        String to, String subject,
//        String bodyPlainText, String bodyHtml,
//        String contentId, String base64Image

            String to = "westudymore@gmail.com";
//        String to = "loc.trinhd@homecredit.vn";
            String from = "westudymore@gmail.com";
            String senderName = "Loc Trinh";
            String subject = "[Ecommerce] Order delivered successfully";

            String bodyPlainText = "Text Body";
//        String bodyHtml = "<img src=\"cid:qrImage\" alt=\"qr code\">";
            String contentId = "";
            String bodyHtml =


                    "  <!doctype html>\n" +
                            "  <html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                            "\n" +
                            "  <head>\n" +
                            "    <title>\n" +
                            "\n" +
                            "    </title>\n" +
                            "    <!--[if !mso]><!-- -->\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                            "    <!--<![endif]-->\n" +
                            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                            "    <style type=\"text/css\">\n" +
                            "      #outlook a {\n" +
                            "        padding: 0;\n" +
                            "      }\n" +
                            "\n" +
                            "      .ReadMsgBody {\n" +
                            "        width: 100%;\n" +
                            "      }\n" +
                            "\n" +
                            "      .ExternalClass {\n" +
                            "        width: 100%;\n" +
                            "      }\n" +
                            "\n" +
                            "      .ExternalClass * {\n" +
                            "        line-height: 100%;\n" +
                            "      }\n" +
                            "\n" +
                            "      body {\n" +
                            "        margin: 0;\n" +
                            "        padding: 0;\n" +
                            "        -webkit-text-size-adjust: 100%;\n" +
                            "        -ms-text-size-adjust: 100%;\n" +
                            "      }\n" +
                            "\n" +
                            "      table,\n" +
                            "      td {\n" +
                            "        border-collapse: collapse;\n" +
                            "        mso-table-lspace: 0pt;\n" +
                            "        mso-table-rspace: 0pt;\n" +
                            "      }\n" +
                            "\n" +
                            "      img {\n" +
                            "        border: 0;\n" +
                            "        height: auto;\n" +
                            "        line-height: 100%;\n" +
                            "        outline: none;\n" +
                            "        text-decoration: none;\n" +
                            "        -ms-interpolation-mode: bicubic;\n" +
                            "      }\n" +
                            "\n" +
                            "      p {\n" +
                            "        display: block;\n" +
                            "        margin: 13px 0;\n" +
                            "      }\n" +
                            "    </style>\n" +
                            "    <!--[if !mso]><!-->\n" +
                            "    <style type=\"text/css\">\n" +
                            "      @media only screen and (max-width:480px) {\n" +
                            "        @-ms-viewport {\n" +
                            "          width: 320px;\n" +
                            "        }\n" +
                            "        @viewport {\n" +
                            "          width: 320px;\n" +
                            "        }\n" +
                            "      }\n" +
                            "    </style>\n" +
                            "    <!--<![endif]-->\n" +
                            "    <!--[if mso]>\n" +
                            "    <xml>\n" +
                            "      <o:OfficeDocumentSettings>\n" +
                            "        <o:AllowPNG/>\n" +
                            "        <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                            "      </o:OfficeDocumentSettings>\n" +
                            "    </xml>\n" +
                            "    <![endif]-->\n" +
                            "    <!--[if lte mso 11]>\n" +
                            "    <style type=\"text/css\">\n" +
                            "      .outlook-group-fix { width:100% !important; }\n" +
                            "    </style>\n" +
                            "    <![endif]-->\n" +
                            "\n" +
                            "\n" +
                            "    <style type=\"text/css\">\n" +
                            "      @media only screen and (min-width:480px) {\n" +
                            "        .mj-column-per-100 {\n" +
                            "          width: 100% !important;\n" +
                            "        }\n" +
                            "      }\n" +
                            "    </style>\n" +
                            "\n" +
                            "\n" +
                            "    <style type=\"text/css\">\n" +
                            "    </style>\n" +
                            "\n" +
                            "  </head>\n" +
                            "\n" +
                            "  <body style=\"background-color:#f9f9f9;\">\n" +
                            "\n" +
                            "\n" +
                            "  <div style=\"background-color:#f9f9f9;\">\n" +
                            "\n" +
                            "\n" +
                            "    <!--[if mso | IE]>\n" +
                            "    <table\n" +
                            "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"\n" +
                            "    >\n" +
                            "      <tr>\n" +
                            "        <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                            "    <![endif]-->\n" +
                            "\n" +
                            "\n" +
                            "    <div style=\"background:#f9f9f9;background-color:#f9f9f9;Margin:0px auto;max-width:600px;\">\n" +
                            "\n" +
                            "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#f9f9f9;background-color:#f9f9f9;width:100%;\">\n" +
                            "        <tbody>\n" +
                            "        <tr>\n" +
                            "          <td style=\"border-bottom:#333957 solid 5px;direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\">\n" +
                            "            <!--[if mso | IE]>\n" +
                            "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                            "\n" +
                            "              <tr>\n" +
                            "\n" +
                            "              </tr>\n" +
                            "\n" +
                            "            </table>\n" +
                            "            <![endif]-->\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "        </tbody>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "\n" +
                            "\n" +
                            "    <!--[if mso | IE]>\n" +
                            "    </td>\n" +
                            "    </tr>\n" +
                            "    </table>\n" +
                            "\n" +
                            "    <table\n" +
                            "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"\n" +
                            "    >\n" +
                            "      <tr>\n" +
                            "        <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                            "    <![endif]-->\n" +
                            "\n" +
                            "\n" +
                            "    <div style=\"background:#fff;background-color:#fff;Margin:0px auto;max-width:600px;\">\n" +
                            "\n" +
                            "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#fff;background-color:#fff;width:100%;\">\n" +
                            "        <tbody>\n" +
                            "        <tr>\n" +
                            "          <td style=\"border:#dddddd solid 1px;border-top:0px;direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\">\n" +
                            "            <!--[if mso | IE]>\n" +
                            "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                            "\n" +
                            "              <tr>\n" +
                            "\n" +
                            "                <td\n" +
                            "                        style=\"vertical-align:bottom;width:600px;\"\n" +
                            "                >\n" +
                            "            <![endif]-->\n" +
                            "\n" +
                            "            <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:13px;text-align:left;direction:ltr;display:inline-block;vertical-align:bottom;width:100%;\">\n" +
                            "\n" +
                            "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:bottom;\" width=\"100%\">\n" +
                            "\n" +
                            "<!--                  logo-->\n" +
                            "<!--                <tr>-->\n" +
                            "<!--                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                            "\n" +
                            "<!--                    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"border-collapse:collapse;border-spacing:0px;\">-->\n" +
                            "<!--                      <tbody>-->\n" +
                            "<!--                      <tr>-->\n" +
                            "<!--                        <td style=\"width:64px;\">-->\n" +
                            "\n" +
                            "<!--                          <img height=\"auto\" src=\"https://i.imgur.com/KO1vcE9.png\" style=\"border:0;display:block;outline:none;text-decoration:none;width:100%;\" width=\"64\" />-->\n" +
                            "\n" +
                            "<!--                        </td>-->\n" +
                            "<!--                      </tr>-->\n" +
                            "<!--                      </tbody>-->\n" +
                            "<!--                    </table>-->\n" +
                            "\n" +
                            "<!--                  </td>-->\n" +
                            "<!--                </tr>-->\n" +
                            "\n" +
                            "                <tr>\n" +
                            "                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                            "\n" +
                            "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:24px;font-weight:bold;line-height:22px;text-align:center;color:#525252;\">\n" +
                            "                      Thank you for your order\n" +
                            "                    </div>\n" +
                            "\n" +
                            "                  </td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "  <!--              <tr>-->\n" +
                            "  <!--                <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                            "\n" +
                            "  <!--                  <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:left;color:#525252;\">-->\n" +
                            "  <!--                    <p>Hi John,</p>-->\n" +
                            "\n" +
                            "  <!--                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam volutpat ut est ac dignissim. Donec pulvinar ligula metus, sed imperdiet quam pretium at. Cras finibus hendrerit magna nec euismod. Ut eget-->\n" +
                            "  <!--                      justo vel enim ultrices pharetra. Morbi tellus libero, sollicitudin pulvinar porta ac, auctor sed neque.</p>-->\n" +
                            "  <!--                  </div>-->\n" +
                            "\n" +
                            "  <!--                </td>-->\n" +
                            "  <!--              </tr>-->\n" +
                            "\n" +
                            "  <!--              Start-->\n" +
                            "                <tr>\n" +
                            "                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                            "\n" +
                            "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:left;color:#525252;\">\n" +
                            "<!--                      <p>Invoice #: [[order]]<br/>-->\n" +
                            "<!--                        Created: January 1, 2015</p>-->\n" +
                            "                      <p>Invoice #: 00000[[order]]<br />\n" +
                            "                          Created time: [[created_time]]\n" +
                            "                      </p>\n" +
                            "\n" +
                            "                      <p>Name: [[name]]<br />\n" +
                            "                          Address: [[address]]<br />\n" +
                            "                          Phone number : [[phone_number]]<br />\n" +
                            "                          Email: [[email]]\n" +
                            "                      </p>\n" +
                            "\n" +
                            "                    </div>\n" +
                            "\n" +
                            "                  </td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "  <!--              End-->\n" +
                            "                <tr>\n" +
                            "                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                            "\n" +
                            "                    <table 0=\"[object Object]\" 1=\"[object Object]\" 2=\"[object Object]\" border=\"0\" style=\"cellspacing:0;color:#000;font-family:'Helvetica Neue',Arial,sans-serif;font-size:13px;line-height:22px;table-layout:auto;width:100%;\">\n" +
                            "                      <tr style=\"border-bottom:1px solid #ecedee;text-align:left;\">\n" +
                            "                        <th style=\"padding: 0 15px 10px 0;\">Product Name</th>\n" +
                            "                        <th style=\"padding: 0 15px 10px 0;\">Product Price</th>\n" +
                            "                        <th style=\"padding: 0 15px;\">Quantity</th>\n" +
                            "                        <th style=\"padding: 0 0 0 15px;\" align=\"right\">Sub Price</th>\n" +
                            "                      </tr>\n" +
                            "\n" +
                            "\n" +
                            "<!--                      <tr>-->\n" +
                            "<!--                        <td style=\"padding: 5px 15px 5px 0;\">[[product_name]]</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 15px 5px 0;\">[[product_cost]]</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 15px;\">[[quantity]]</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[sub_total]]</td>-->\n" +
                            "<!--                      </tr>-->\n" +
                            "                      <!--orderDetails-->\n" +
                            "<!--                      <tr>-->\n" +
                            "<!--                        <td style=\"padding: 5px 15px 5px 0;\">Item number 1</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 15px 5px 0;\">test</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 15px;\">1</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">$100,00</td>-->\n" +
                            "<!--                      </tr>-->\n" +
                            "\n" +
                            "                      <tr style=\"border-top:1px solid #ecedee;text-align:left;\">\n" +
                            "<!--                          <td style=\"padding: 0 15px 5px 0;\">Sub Total</td>-->\n" +
                            "                          <td style=\"padding: 0 15px 5px 0; font-weight:bold\">Sub Total</td>\n" +
                            "                          <td style=\"padding: 0 15px 5px 0;\"></td>\n" +
                            "                          <td style=\"padding: 0 15px;\"></td>\n" +
                            "                          <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[product_cost]]</td>\n" +
                            "                      </tr>\n" +
                            "\n" +
                            "                      <tr>\n" +
                            "\n" +
                            "<!--                        <td style=\"padding: 0 15px 5px 0;\">Shipping Cost</td>-->\n" +
                            "                          <td style=\"padding: 0 15px 5px 0; font-weight:bold\">Shipping Cost</td>\n" +
                            "<!--                          <td style=\"padding: 5px 15px 5px 0; font-weight:bold\">TOTAL</td>-->\n" +
                            "                        <td style=\"padding: 0 15px 5px 0;\"></td>\n" +
                            "                        <td style=\"padding: 0 15px;\"></td>\n" +
                            "                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[shipping_cost]]</td>\n" +
                            "                      </tr>\n" +
                            "<!--                      <tr style=\"border-bottom:2px solid #ecedee;text-align:left;padding:15px 0;\">-->\n" +
                            "<!--                        <td style=\"padding: 0 15px 5px 0;\">Sales Tax</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 15px 5px 0;\"></td>-->\n" +
                            "<!--                        <td style=\"padding: 0 15px;\">1</td>-->\n" +
                            "<!--                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">$10,00</td>-->\n" +
                            "<!--                      </tr>-->\n" +
                            "                      <tr style=\"border-bottom:2px solid #ecedee;text-align:left;padding:15px 0;\">\n" +
                            "                        <td style=\"padding: 5px 15px 5px 0; font-weight:bold\">TOTAL</td>\n" +
                            "                        <td style=\"padding: 0 15px 5px 0;\"></td>\n" +
                            "                        <td style=\"padding: 0 15px;\"></td>\n" +
                            "                        <td style=\"padding: 0 0 0 15px; font-weight:bold\" align=\"right\">[[total_cost]]</td>\n" +
                            "                      </tr>\n" +
                            "                    </table>\n" +
                            "\n" +
                            "                  </td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "<!--                <tr>-->\n" +
                            "<!--                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                            "\n" +
                            "<!--                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;line-height:16px;text-align:left;color:#a2a2a2;\">-->\n" +
                            "<!--                      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam volutpat ut est ac dignissim. Donec pulvinar ligula metus, sed imperdiet quam pretium at.</p>-->\n" +
                            "<!--                    </div>-->\n" +
                            "\n" +
                            "<!--                  </td>-->\n" +
                            "<!--                </tr>-->\n" +
                            "\n" +
                            "                <tr>\n" +
                            "<!--                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                            "                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                            "\n" +
                            "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:24px;font-weight:bold;line-height:22px;text-align:center;color:#525252;\">\n" +
                            "                      Scan QR Code\n" +
                            "                    </div>\n" +
                            "\n" +
                            "                  </td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "                <tr>\n" +
                            "<!--                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">-->\n" +
                            "                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                            "\n" +
                            "<!--                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:left;color:#525252;\">-->\n" +
                            "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:22px;text-align:center;color:#525252;\">\n" +
                            "                      <img src=\"cid:qrImage\" alt=\"qr code\">\n" +
                            "\n" +
                            "                    </div>\n" +
                            "\n" +
                            "                  </td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "<!--                  Ship-->\n" +
                            "<!--                <tr>-->\n" +
                            "<!--                  <td align=\"center\" style=\"font-size:0px;padding:10px 25px;padding-top:30px;padding-bottom:50px;word-break:break-word;\">-->\n" +
                            "\n" +
                            "<!--                    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"border-collapse:separate;line-height:100%;\">-->\n" +
                            "<!--                      <tr>-->\n" +
                            "<!--                        <td align=\"center\" bgcolor=\"#2F67F6\" role=\"presentation\" style=\"border:none;border-radius:3px;color:#ffffff;cursor:auto;padding:15px 25px;\" valign=\"middle\">-->\n" +
                            "<!--                          <p style=\"background:#2F67F6;color:#ffffff;font-family:'Helvetica Neue',Arial,sans-serif;font-size:15px;font-weight:normal;line-height:120%;Margin:0;text-decoration:none;text-transform:none;\">-->\n" +
                            "<!--                            <a href=\"\" style=\"color:#fff; text-decoration:none\">-->\n" +
                            "<!--                              Check Shipping Status</a>-->\n" +
                            "<!--                          </p>-->\n" +
                            "<!--                        </td>-->\n" +
                            "<!--                      </tr>-->\n" +
                            "<!--                    </table>-->\n" +
                            "\n" +
                            "<!--                  </td>-->\n" +
                            "<!--                </tr>-->\n" +
                            "\n" +
                            "                <tr>\n" +
                            "                  <td align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\">\n" +
                            "\n" +
                            "                    <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:14px;line-height:20px;text-align:left;color:#525252;\">\n" +
                            "                      Best regards,<br><br> Loc Trinh<br>Software Developer<br>\n" +
                            "<!--                      <a href=\"https://www.htmlemailtemplates.net\" style=\"color:#2F67F6\">htmlemailtemplates.net</a>-->\n" +
                            "                    </div>\n" +
                            "\n" +
                            "                  </td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "              </table>\n" +
                            "\n" +
                            "            </div>\n" +
                            "\n" +
                            "            <!--[if mso | IE]>\n" +
                            "            </td>\n" +
                            "\n" +
                            "            </tr>\n" +
                            "\n" +
                            "            </table>\n" +
                            "            <![endif]-->\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "        </tbody>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "\n" +
                            "\n" +
                            "    <!--[if mso | IE]>\n" +
                            "    </td>\n" +
                            "    </tr>\n" +
                            "    </table>\n" +
                            "\n" +
                            "    <table\n" +
                            "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"\n" +
                            "    >\n" +
                            "      <tr>\n" +
                            "        <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                            "    <![endif]-->\n" +
                            "\n" +
                            "\n" +
                            "    <div style=\"Margin:0px auto;max-width:600px;\">\n" +
                            "\n" +
                            "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"width:100%;\">\n" +
                            "        <tbody>\n" +
                            "        <tr>\n" +
                            "          <td style=\"direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\">\n" +
                            "            <!--[if mso | IE]>\n" +
                            "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                            "\n" +
                            "              <tr>\n" +
                            "\n" +
                            "                <td\n" +
                            "                        style=\"vertical-align:bottom;width:600px;\"\n" +
                            "                >\n" +
                            "            <![endif]-->\n" +
                            "\n" +
                            "            <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:13px;text-align:left;direction:ltr;display:inline-block;vertical-align:bottom;width:100%;\">\n" +
                            "\n" +
                            "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\">\n" +
                            "                <tbody>\n" +
                            "                <tr>\n" +
                            "                  <td style=\"vertical-align:bottom;padding:0;\">\n" +
                            "\n" +
                            "                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\">\n" +
                            "\n" +
                            "                      <tr>\n" +
                            "                        <td align=\"center\" style=\"font-size:0px;padding:0;word-break:break-word;\">\n" +
                            "\n" +
                            "<!--                          <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;font-weight:300;line-height:1;text-align:center;color:#575757;\">-->\n" +
                            "<!--                            Some Firm Ltd, 35 Avenue. City 10115, USA-->\n" +
                            "<!--                          </div>-->\n" +
                            "\n" +
                            "                          <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;font-weight:300;line-height:1;text-align:center;color:#575757;\">\n" +
                            "                            02 Vo Oanh, 25 Ward, Binh Thanh District, Ho Chi Minh City, Vietnam\n" +
                            "                          </div>\n" +
                            "\n" +
                            "                        </td>\n" +
                            "                      </tr>\n" +
                            "\n" +
                            "                      <tr>\n" +
                            "                        <td align=\"center\" style=\"font-size:0px;padding:10;word-break:break-word;\">\n" +
                            "\n" +
                            "                          <div style=\"font-family:'Helvetica Neue',Arial,sans-serif;font-size:12px;font-weight:300;line-height:1;text-align:center;color:#575757;\">\n" +
                            "                            <a href=\"\" style=\"color:#575757\">Unsubscribe</a> from our emails\n" +
                            "                          </div>\n" +
                            "\n" +
                            "                        </td>\n" +
                            "                      </tr>\n" +
                            "\n" +
                            "                    </table>\n" +
                            "\n" +
                            "                  </td>\n" +
                            "                </tr>\n" +
                            "                </tbody>\n" +
                            "              </table>\n" +
                            "\n" +
                            "            </div>\n" +
                            "\n" +
                            "            <!--[if mso | IE]>\n" +
                            "            </td>\n" +
                            "\n" +
                            "            </tr>\n" +
                            "\n" +
                            "            </table>\n" +
                            "            <![endif]-->\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "        </tbody>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "\n" +
                            "\n" +
                            "    <!--[if mso | IE]>\n" +
                            "    </td>\n" +
                            "    </tr>\n" +
                            "    </table>\n" +
                            "    <![endif]-->\n" +
                            "\n" +
                            "\n" +
                            "  </div>\n" +
                            "\n" +
                            "  </body>\n" +
                            "\n" +
                            "  </html>";

//        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
            String base64Image = order.getQrCode();

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from, senderName);
            helper.setTo(to);
            helper.setSubject(subject);

//        message.setSubject(subject);
//        message.setFrom(new InternetAddress(from));
//        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));


            //        Replace text mail
            bodyHtml = bodyHtml.replace("[[name]]", user.getName());
            bodyHtml = bodyHtml.replace("[[address]]", order.getAddress());
            bodyHtml = bodyHtml.replace("[[phone_number]]", user.getPhoneNumber());
            bodyHtml = bodyHtml.replace("[[email]]", user.getEmail());

            Long orderId = order.getId();
            String order_Id = Long.toString(orderId);
            bodyHtml = bodyHtml.replace("[[order]]", order_Id);


            Date date = order.getOrderTime();
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss");
            String strDate = dateFormat.format(date);
            bodyHtml = bodyHtml.replace("[[created_time]]", strDate);

            float shippingCost = order.getShippingCost();
            long shippingCostL = (long) shippingCost;
//        String shipping_Cost = Float.toString(shippingCost);
            String shipping_Cost = String.valueOf(shippingCostL);

            bodyHtml = bodyHtml.replace("[[shipping_cost]]", shipping_Cost);

            float productCost = order.getProductCost();
            long productCostL = (long) productCost;
//        String product_cost = Float.toString(productCost);
            String product_cost = String.valueOf(productCostL);
            bodyHtml = bodyHtml.replace("[[product_cost]]", product_cost);

            float totalCost = order.getTotalCost();
            long totalCostL = (long) totalCost;
//        String total_cost = Float.toString(totalCost);
            String total_cost = String.valueOf(totalCostL);
            bodyHtml = bodyHtml.replace("[[total_cost]]", total_cost);

//        float subProductItem = orderDetail.getSubTotal();
//        long subProductItemL = (long) subProductItem;
//        String sub_product_item = String.valueOf(subProductItemL);


            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
//            add html item + <!--orderDetails-->
                String list_product =

                        "<tr>\n" +
                                "                        <td style=\"padding: 5px 15px 5px 0;\">[[product_name]]</td>\n" +
                                "                        <td style=\"padding: 0 15px 5px 0;\">[[product_cost]]</td>\n" +
                                "                        <td style=\"padding: 0 15px;\">[[quantity]]</td>\n" +
                                "                        <td style=\"padding: 0 0 0 15px;\" align=\"right\">[[sub_total]]</td>\n" +
                                "                      </tr>\n" +
                                "<!--orderDetails-->";

                bodyHtml = bodyHtml.replace("<!--orderDetails-->", list_product);
//            replace value: productName, productCost, quantity, subTotal

                bodyHtml = bodyHtml.replace("[[product_name]]", orderDetail.getName());

//            Float productCostItem = orderDetail.getProductCost();
//            String product_cost_item = Float.toString(productCostItem);
//            bodyHtml = bodyHtml.replace("[[product_cost]]", product_cost_item);

                float productCostItem = orderDetail.getProductCost();
                long productCostItemL = (long) productCostItem;
                String product_cost_item = String.valueOf(productCostItemL);

                bodyHtml = bodyHtml.replace("[[product_cost]]", product_cost_item);

                float quantityItem = orderDetail.getQuantity();
                long quantityItemL = (long) quantityItem;
//            String quantity_item = Float.toString(quantityItem);
                String quantity_item = String.valueOf(quantityItemL);
                bodyHtml = bodyHtml.replace("[[quantity]]", quantity_item);

                float subProductItem = orderDetail.getSubTotal();
                long subProductItemL = (long) subProductItem;
                String sub_product_item = String.valueOf(subProductItemL);
//            String sub_product_item = Float.toString(subProductItem);
                bodyHtml = bodyHtml.replace("[[sub_total]]", sub_product_item);
//            check i=n => delete [[add_product]]
            }

//        Off: Replace text mail

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(bodyPlainText, "text/plain; charset=UTF-8");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(bodyHtml, "text/html; charset=UTF-8");

            Multipart multiPart = new MimeMultipart("alternative");

            // create a new imagePart and add it to multipart so that the image is inline attached in the email
            byte[] rawImage = Base64.getDecoder().decode(base64Image);
            BodyPart imagePart = new MimeBodyPart();
            ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage, "image/png");

            imagePart.setDataHandler(new DataHandler(imageDataSource));
            imagePart.setHeader("Content-ID", "<qrImage>");
            imagePart.setFileName("someFileName.png");

            multiPart.addBodyPart(imagePart);
            multiPart.addBodyPart(textPart);
            multiPart.addBodyPart(htmlPart);

            message.setContent(multiPart);


//        helper.setText(bodyHtml, true);

//        multiPart.addBodyPart(imagePart);
//        multiPart.addBodyPart(textPart);
//        multiPart.addBodyPart(htmlPart);
//        message.setContent(multiPart);

//        helper.setText(content, true);
            mailSender.send(message);

            //        mapper entity to DTO Response
            return modelMapper.map(updateOrder, OrderResponse.class);


//        CID:base64 ================================================
//        String to = "westudymore@gmail.com";
//        String from = "westudymore@gmail.com";
//        String senderName = "Loc Trinh";
//        String subject = "Order delivered successfully";
//        String bodyPlainText = "asdasdasdsad";
//        String bodyHtml = "<img src=\"cid:qrImage\" alt=\"qr code\">";
//        String contentId = "";
//
//        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
//
//        MimeMessage message = mailSender.createMimeMessage();
//
//        message.setSubject(subject);
//        message.setFrom(new InternetAddress(from));
//        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//        MimeBodyPart textPart = new MimeBodyPart();
//        textPart.setContent(bodyPlainText, "text/plain; charset=UTF-8");
//
//        MimeBodyPart htmlPart = new MimeBodyPart();
//        htmlPart.setContent(bodyHtml, "text/html; charset=UTF-8");
//
//        Multipart multiPart = new MimeMultipart("alternative");
//
//        // create a new imagePart and add it to multipart so that the image is inline attached in the email
//        byte[] rawImage = Base64.getDecoder().decode(base64Image);
//        BodyPart imagePart = new MimeBodyPart();
//        ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage,"image/png");
//
//        imagePart.setDataHandler(new DataHandler(imageDataSource));
//        imagePart.setHeader("Content-ID", "<qrImage>");
//        imagePart.setFileName("someFileName.png");
//
//        multiPart.addBodyPart(imagePart);
//        multiPart.addBodyPart(textPart);
//        multiPart.addBodyPart(htmlPart);
//
//        message.setContent(multiPart);
//        mailSender.send(message);

//        ================================================
//        //        mapper entity to DTO Response
//        return modelMapper.map(updateOrder, OrderResponse.class);

        }
    }

//    public void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
//        String toAddress = user.getEmail();
//        String fromAddress = "westudymore@gmail.com";
//        String senderName = "Hackathon HCM";
//        String subject = "Please verify your registration";
//        String content = "Dear [[name]],<br>"
//                + "Please click the link below to verify your registration:<br>"
//                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
//                + "Thank you,<br>"
//                + "Trinh Loc.";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setFrom(fromAddress, senderName);
//        helper.setTo(toAddress);
//        helper.setSubject(subject);
//
//        content = content.replace("[[name]]", user.getName());
//
//        helper.setText(content, true);
//
//        mailSender.send(message);
//
//        System.out.println("Email has been sent");
//    }
}

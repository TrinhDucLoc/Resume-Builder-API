package com.springboot.ecommerce.QRCode;

import com.google.zxing.WriterException;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

//@RestController
@Controller
public class QRCodeController {
    private final OrderRepository orderRepository;

    public QRCodeController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
//    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/ImageQR/QRCode2.png";

    @GetMapping("/order/qrcode/{orderId}")
    public String getQRCode(Model model,
                            @PathVariable(name = "orderId") Long orderId){
//        String medium="https://rahul26021999.medium.com/";
//        String github="https://github.com/rahul26021999";
        String medium = "http://localhost:8080/api/order/" + orderId;
        String github = "http://localhost:8080/api/order/" + orderId;

        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(medium,250,250);


            String QR_CODE_IMAGE_PATH = "./src/main/resources/static/ImageQR/" + orderId + ".png";

            // Generate and Save Qr Code Image in static/image folder
            QRCodeGenerator.generateQRCodeImage(github,250,250,QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);

        model.addAttribute("medium",medium);
        model.addAttribute("github",github);
        model.addAttribute("qrcode",qrcode);

        Optional<Order> order = orderRepository.findById(orderId);
        model.addAttribute("order", order);

        return "qrcode";
    }
}
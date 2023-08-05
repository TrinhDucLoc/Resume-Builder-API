//package com.springboot.ecommerce.QRCode;
//
//import com.google.zxing.WriterException;
//import com.springboot.ecommerce.repository.OrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.io.IOException;
//import java.util.Base64;
//
//@Controller
//public class MainController_Old {
//    @Autowired
//    private OrderRepository orderRepository;
//
////    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/img/QRCode.png";
//private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/ImageQR/QRCode1.png";
//
////    @GetMapping("/")
//    @GetMapping("/order/qrcode/{orderId}")
////    public String getQRCode(Model model){
//        public String getQRCode(Model model,
//                                @PathVariable(name = "orderId") Long orderId){
//        String medium="https://rahul26021999.medium.com/";
////        String github="https://github.com/rahul26021999";
//
//
////        Optional<Order> order = orderRepository.findById(orderId);
//        Long orderId1 = orderId;
//        String order_Id = Long.toString(orderId1);
//        String github = "http://localhost:8080/api/order/[[order]]";
//        github = github.replace("[[order]]", order_Id);
//
//        byte[] image = new byte[0];
//        try {
//
//            // Generate and Return Qr Code in Byte Array
//            image = QRCodeGenerator.getQRCodeImage(medium,250,250);
//
//            // Generate and Save Qr Code Image in static/image folder
////            QRCodeGenerator.generateQRCodeImage(github,250,250,QR_CODE_IMAGE_PATH);
//
////            Long orderId1 = orderId;
////            String order_Id = Long.toString(orderId1);
////            String QR_CODE_IMAGE = "./src/main/resources/static/ImageQR/QRCode_OrderId";
////            String QR_CODE_IMAGE_PATH = "./src/main/resources/static/ImageQR/QRCode.png";
//            QRCodeGenerator.generateQRCodeImage(github,250,250,QR_CODE_IMAGE_PATH);
//
//        } catch (WriterException | IOException e) {
//            e.printStackTrace();
//        }
//        // Convert Byte Array into Base64 Encode String
//        String qrcode = Base64.getEncoder().encodeToString(image);
//
//        model.addAttribute("medium",medium);
//        model.addAttribute("github",github);
//        model.addAttribute("qrcode",qrcode);
//
//        return "qrcode";
//    }
//}
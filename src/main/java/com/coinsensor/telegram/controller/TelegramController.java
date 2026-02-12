// package com.coinsensor.telegram.controller;
//
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.coinsensor.telegram.service.TelegramService;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/api/telegram")
// @RequiredArgsConstructor
// public class TelegramController {
//
//     private final TelegramService telegramService;
//
//     @PostMapping("/send")
//     public String sendMessage(@RequestParam String message) {
//         telegramService.sendMessage(message);
//         return "메시지 전송 완료";
//     }
// }
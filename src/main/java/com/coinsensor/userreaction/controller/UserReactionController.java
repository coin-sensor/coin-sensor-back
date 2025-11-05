// package com.coinsensor.userreaction.controller;
//
// import com.coinsensor.userreaction.service.UserReactionService;
// import com.coinsensor.userreaction.dto.request.UserReactionRequest;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
//
// @RestController
// @RequestMapping("/api/reactions")
// @RequiredArgsConstructor
// public class UserReactionController {
//
//     private final UserReactionService userReactionService;
//
//     @PostMapping
//     public ResponseEntity<Void> addReaction(@RequestHeader String uuid, @RequestBody UserReactionRequest request) {
//         userReactionService.addReaction(uuid, request);
//         return ResponseEntity.ok().build();
//     }
//
//     @DeleteMapping
//     public ResponseEntity<Void> removeReaction(@RequestHeader String uuid, @RequestParam String targetTable, @RequestParam Long targetId) {
//         userReactionService.removeReaction(uuid, targetTable, targetId);
//         return ResponseEntity.ok().build();
//     }
//
//     @GetMapping("/count")
//     public ResponseEntity<Long> getReactionCount(@RequestParam String targetTable,
//                                                @RequestParam Long targetId,
//                                                @RequestParam Long reactionId) {
//         return ResponseEntity.ok(userReactionService.getReactionCount(targetTable, targetId, reactionId));
//     }
// }
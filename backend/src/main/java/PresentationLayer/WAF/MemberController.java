package PresentationLayer.WAF;

import Util.APIResponse;
import Util.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/isMember/{userId}")
    public ResponseEntity<APIResponse<Boolean>> isMember(@PathVariable String userId) {
        try {
            boolean isMem = memberService.isMember(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<Boolean>(isMem, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getMemberName/{memberId}")
    public ResponseEntity<APIResponse<String>> getMemberName(@PathVariable String memberId) {
        try {
            String userName = memberService.getMemberNane(memberId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<String>(userName, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/isStoreOwner/{memberId}/{storeId}")
    public ResponseEntity<APIResponse<Boolean>> isStoreOwner(@PathVariable String memberId, @PathVariable String storeId) {
        try {
            boolean isStoreOwner = memberService.isStoreOwner(memberId, storeId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<Boolean>(isStoreOwner, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/isStoreManager/{memberId}/{storeId}")
    public ResponseEntity<APIResponse<Boolean>> isStoreManager(@PathVariable String memberId, @PathVariable String storeId) {
        try {
            boolean isStoreManager = memberService.isStoreManager(memberId, storeId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<Boolean>(isStoreManager, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }
    @GetMapping("/hasInventoryPermission/{memberId}/{storeId}")
    public ResponseEntity<APIResponse<Boolean>> hasInventoryPermission(@PathVariable String memberId, @PathVariable String storeId) {
        try {
            boolean hasInventoryPermission = memberService.hasInventoryPermission(memberId, storeId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<Boolean>(hasInventoryPermission, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/hasPurchasePermission/{memberId}/{storeId}")
    public ResponseEntity<APIResponse<Boolean>> hasPurchasePermission(@PathVariable String memberId, @PathVariable String storeId) {
        try {
            boolean hasPurchasePermission = memberService.hasPurchasePermission(memberId, storeId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<Boolean>(hasPurchasePermission, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @GetMapping("/isAdmin/{memberId}")
    public ResponseEntity<APIResponse<Boolean>> isAdmin(@PathVariable String memberId) {
        try {
            boolean hasPurchasePermission = memberService.isAdmin(memberId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<Boolean>(hasPurchasePermission, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }



}

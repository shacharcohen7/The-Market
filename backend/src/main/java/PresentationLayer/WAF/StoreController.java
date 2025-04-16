package PresentationLayer.WAF;

import Util.APIResponse;
import Util.StoreDTO;
import Util.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/isStoreOpen/{storeID}")
    public ResponseEntity<APIResponse<Boolean>> isStoreOpen(@PathVariable String storeID) {
        try {
            boolean isOpen = storeService.isStoreOpen(storeID);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<Boolean>(isOpen, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getStore/{storeId}")
    public ResponseEntity<APIResponse<String>> getStore(@PathVariable String storeId) {
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            StoreDTO store = storeService.getStore(storeId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            //String dtosRes = new ArrayList<>();
            //for (StoreDTO storeDTO : allStores ){
            String res = objectMapper.writeValueAsString(store);
            //}
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(res, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }



    @GetMapping("/getAllStores")
    public ResponseEntity<APIResponse<List<String>>> getAllStores() {
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            List<StoreDTO> allStores = storeService.getAllStores();
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            List<String> dtosRes = new ArrayList<>();
            for (StoreDTO storeDTO : allStores ){
                dtosRes.add(objectMapper.writeValueAsString(storeDTO));
            }

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(dtosRes, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


}


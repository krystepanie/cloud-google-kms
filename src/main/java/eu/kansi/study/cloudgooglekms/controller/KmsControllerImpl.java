package eu.kansi.study.cloudgooglekms.controller;

import eu.kansi.study.cloudgooglekms.Scope;
import eu.kansi.study.cloudgooglekms.service.KmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KmsControllerImpl implements KmsController {

    private final KmsService kmsService;

    public KmsControllerImpl(KmsService kmsService) {
        this.kmsService = kmsService;
    }

    @Override
    public ResponseEntity<String> encrypt(String text, String scope) {
        return ResponseEntity.ok().body(kmsService.encrypt(text, new Scope(scope)));
    }

    @Override
    public ResponseEntity<String> decrypt(String text, String scope) {
        return ResponseEntity.ok().body(kmsService.decrypt(text, new Scope(scope)));
    }

    @Override
    public ResponseEntity<String> getPublicKey(String scope) {
        return ResponseEntity.ok().body(kmsService.getPublicKey(new Scope(scope)).getPem());
    }

}

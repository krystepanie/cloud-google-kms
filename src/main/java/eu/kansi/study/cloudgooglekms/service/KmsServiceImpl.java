package eu.kansi.study.cloudgooglekms.service;

import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.PublicKey;
import com.google.cloud.spring.kms.KmsTemplate;
import eu.kansi.study.cloudgooglekms.Scope;
import eu.kansi.study.cloudgooglekms.config.keys.KmsKeysProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KmsServiceImpl implements KmsService {

    private final Environment environment;
    private final KmsTemplate kmsTemplate;

    private final KmsKeysProperties keysProperties;

    private final KeyManagementServiceClient keyManagementServiceClient;

    public KmsServiceImpl(
            Environment environment,
            KmsTemplate kmsTemplate,
            KmsKeysProperties keysProperties,
            KeyManagementServiceClient keyManagementServiceClient) {
        this.environment = environment;
        this.kmsTemplate = kmsTemplate;
        this.keysProperties = keysProperties;
        this.keyManagementServiceClient = keyManagementServiceClient;
    }

    @Override
    public String encrypt(String text, Scope scope) {
        byte[] encryptedBytes = kmsTemplate.encryptText(getKey(scope), text);
        return encodeBase32(encryptedBytes);
    }

    @Override
    public String decrypt(String text, Scope scope) {
        byte[] encryptedBytes = decodeBase32(text);
        return kmsTemplate.decryptText(getKey(scope), encryptedBytes);
    }

    @Override
    public PublicKey getPublicKey(Scope scope) {
        String fullPath = getAsymmetricKeyFullPath(scope);
        return keyManagementServiceClient.getPublicKey(fullPath);
    }

    String getKey(Scope scope) {
        return "spring-cloud-gcp/" + scope.value();
    }

    String getAsymmetricKeyFullPath(Scope scope) {
        return keysProperties.getKeys().get(scope.value()).getFullPath();
    }

    private String encodeBase32(byte[] bytes) {
        Base32 base32 = new Base32();
        byte[] encoded = base32.encode(bytes);
        return new String(encoded);
    }

    private byte[] decodeBase32(String encryptedText) {
        byte[] bytes = encryptedText.getBytes();
        Base32 base32 = new Base32();
        return base32.decode(bytes);
    }
}

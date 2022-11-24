package eu.kansi.study.cloudgooglekms.service;


import com.google.cloud.kms.v1.PublicKey;
import eu.kansi.study.cloudgooglekms.Scope;

public interface KmsService {

    String encrypt(String string, Scope scope);

    String decrypt(String string, Scope scope);

    PublicKey getPublicKey(Scope keyVersionId);

}

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Cryptographic {
    private static Signature sig;

    static {
        try {
            sig = Signature.getInstance("SHA256WithRSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        //can support pem/der
        String string_to_sign = "{\"test\":\"test\"}";
        String signature = generateSignature(string_to_sign, "src/resources/private_key.pem");
        System.out.println("String_to_sign: " + string_to_sign);
        System.out.println("Signature: " + signature);
        System.out.println("Validate success: " + validateSignature(signature, string_to_sign, "src/resources/public_key.der"));
    }

    public static String generateSignature(String string_to_sign, String file) throws Exception {
        byte[] data = string_to_sign.getBytes(StandardCharsets.UTF_8);

        sig.initSign(getPrivate(file));
        sig.update(data);

        byte[] signatureBytes = sig.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static Boolean validateSignature(String signature, String string_to_validate, String file) throws Exception {
        byte[] string_to_validate_bytes = string_to_validate.getBytes(StandardCharsets.UTF_8);
        byte[] signature64 = Base64.getDecoder().decode(signature);

        sig.initVerify(getPublic(file));
        sig.update(string_to_validate_bytes);

        return sig.verify(signature64);
    }

    private static PrivateKey getPrivate(String filename)
            throws Exception {
        if(isPemFormat(filename)) {
            return getPrivatePem(filename);
        }

        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PrivateKey getPrivatePem(String filename) throws Exception {
        // Read the PEM file into a string
        String key = new String(Files.readAllBytes(Paths.get(filename)));

        // Remove the header and footer lines
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");;

        // Base64 decode the string
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        // Create a key spec and generate the private key
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private static PublicKey getPublic(String filename)
            throws Exception {

        if(isPemFormat(filename)) {
            return getPublicPem(filename);
        }

        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PublicKey getPublicPem(String filename) throws Exception {
        // Read the PEM file content as a string
        String publicKeyPEM = new String(Files.readAllBytes(Paths.get(filename)));

        // Remove the PEM header and footer and any whitespace characters
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");  // Remove any whitespace

        // Convert Base64 URL to standard Base64 by replacing _ with / and - with +
        String base64Standard = publicKeyPEM.replace('-', '+').replace('_', '/');

        // Decode the Base64 string into a byte array
        byte[] decoded = Base64.getDecoder().decode(base64Standard);

        // Create the X509EncodedKeySpec and generate the public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private static boolean isPemFormat(String filename) throws Exception {
        // Read the entire file content as a string
        String content = new String(Files.readAllBytes(Paths.get(filename)));

        // Check for PEM headers (e.g., "-----BEGIN PRIVATE KEY-----")
        return content.contains("-----BEGIN ") && content.contains("-----END ");
    }
}

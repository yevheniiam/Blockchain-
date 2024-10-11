import java.security.*;
import java.util.Base64;

public class Transaction {
    private String input;
    private String output;
    private double amount;
    private long txTimestamp;
    private String txHash;
    private String signature;

    public Transaction(String input, String output, double amount, long txTimestamp) {
        this.input = input;
        this.output = output;
        this.amount = amount;
        this.txTimestamp = txTimestamp;
        this.txHash = calculateHash();
    }

    public String calculateHash() {
        String dataToHash = input + output + amount + txTimestamp;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void signTransaction(PrivateKey privateKey) throws Exception {
        String data = input + output + amount + txTimestamp;
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initSign(privateKey);
        ecdsa.update(data.getBytes("UTF-8"));
        byte[] signatureBytes = ecdsa.sign();
        this.signature = Base64.getEncoder().encodeToString(signatureBytes);
    }

    public boolean verifyTransaction(PublicKey publicKey) throws Exception {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initVerify(publicKey);
        String data = input + output + amount + txTimestamp;
        ecdsa.update(data.getBytes("UTF-8"));
        return ecdsa.verify(Base64.getDecoder().decode(signature));
    }

    @Override
    public String toString() {
        return "Відправник: " + input + "\n" +
                "Одержувач: " + output + "\n" +
                "Сума: " + amount + "\n" +
                "Часова мітка: " + txTimestamp + "\n" +
                "Геш транзакції: " + txHash + "\n" +
                "Цифровий підпис: " + signature;
    }
}

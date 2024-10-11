import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Block {
    private String version;
    private String prevHash;
    private long timestamp;
    private int difficulty;
    private int nonce;
    private String merkleRoot;
    private List<Transaction> transactions;
    private String blockHash;
    private String blockSignature;  // Цифровий підпис блоку

    public Block(String version, String prevHash, long timestamp, int difficulty) {
        this.version = version;
        this.prevHash = prevHash;
        this.timestamp = timestamp;
        this.difficulty = difficulty;
        this.transactions = new ArrayList<>();
        this.blockHash = calculateHash();
    }

    // Метод для обчислення гешу блоку
    public String calculateHash() {
        String dataToHash = version + prevHash + timestamp + difficulty + nonce + merkleRoot;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Додаємо транзакцію до блоку
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.merkleRoot = calculateMerkleRoot();
        this.blockHash = calculateHash();
    }

    // Обчислення кореня дерева Меркла
    public String calculateMerkleRoot() {
        List<String> hashList = new ArrayList<>();
        for (Transaction t : transactions) {
            hashList.add(t.calculateHash());
        }
        return getMerkleRoot(hashList);
    }

    // Рекурсивне обчислення кореня дерева Меркла
    private String getMerkleRoot(List<String> hashList) {
        if (hashList.size() == 1) return hashList.get(0);
        List<String> newHashList = new ArrayList<>();
        for (int i = 0; i < hashList.size() - 1; i += 2) {
            newHashList.add(mergeHash(hashList.get(i), hashList.get(i + 1)));
        }
        if (hashList.size() % 2 == 1) {
            newHashList.add(mergeHash(hashList.get(hashList.size() - 1), hashList.get(hashList.size() - 1)));
        }
        return getMerkleRoot(newHashList);
    }

    // Комбінування двох хешів
    private String mergeHash(String hash1, String hash2) {
        String combinedHash = hash1 + hash2;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combinedHash.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Підписання блоку приватним ключем
    public void signBlock(PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(blockHash.getBytes("UTF-8"));
        byte[] signatureBytes = signature.sign();
        this.blockSignature = Base64.getEncoder().encodeToString(signatureBytes);
    }

    // Перевірка підпису блоку
    public boolean verifyBlockSignature(PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(blockHash.getBytes("UTF-8"));
        return signature.verify(Base64.getDecoder().decode(blockSignature));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Версія: ").append(version).append("\n");
        sb.append("Геш попереднього блоку: ").append(prevHash).append("\n");
        sb.append("Мітка часу: ").append(timestamp).append("\n");
        sb.append("Складність: ").append(difficulty).append("\n");
        sb.append("Nonce: ").append(nonce).append("\n");
        sb.append("Корінь дерева Меркла: ").append(merkleRoot).append("\n");
        sb.append("Геш блоку: ").append(blockHash).append("\n");
        sb.append("Цифровий підпис блоку: ").append(blockSignature).append("\n");
        sb.append("Транзакції:\n");
        for (Transaction tx : transactions) {
            sb.append(tx).append("\n");
        }
        return sb.toString();
    }
}

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Main {
    public static void main(String[] args) throws Exception {
        // Генерація ключів
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Створення транзакції
        Transaction tx1  = new Transaction("AddressA", "AddressB", 2.5, System.currentTimeMillis());
        tx1.signTransaction(keyPair.getPrivate());
        System.out.println("Транзакція:\n" + tx1);
        System.out.println("Перевірка транзакції: " + tx1.verifyTransaction(keyPair.getPublic()));

        // Створення блоку
        Block block1 = new Block("1.0", "0", System.currentTimeMillis(), 5);
        block1.addTransaction(tx1);
        block1.signBlock(keyPair.getPrivate());  // Підписання блоку
        System.out.println("\nБлок:\n" + block1);
        System.out.println("Перевірка підпису блоку: " + block1.verifyBlockSignature(keyPair.getPublic()));  // Перевірка підпису блоку

        // Створення блокчейну
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock(block1);
        System.out.println("\nЛанцюг блоків:\n" + blockchain);
    }
}

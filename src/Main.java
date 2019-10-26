import java.math.BigInteger;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        RSA Alice = new RSA();
        RSA Bob = new RSA();

        String poslaniye = "Idi kod' na Prolog'e!";
        System.out.println("Боб взял ОК Алисы: " + Arrays.toString(Alice.getOK()));
        System.out.println("Теперь он шифрует своё сообщение.");
        System.out.println();

        BigInteger[] Crypted = Bob.encrypt(Alice.getOK(), poslaniye);
        StringBuilder Kek = new StringBuilder();

        for (BigInteger a : Crypted) {
            Kek.append(a);
        }
        String EncryptedMessage = Kek.toString();
        System.out.println("Боб передал Алисе это: " + EncryptedMessage);

        System.out.println();

        String Decryption = Alice.decrypt(Crypted);
        System.out.println("Алиса готовит параметры d и modulo, расшифровывает и получает: " + Decryption);


    }
}

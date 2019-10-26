import java.math.BigInteger;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        RSA Alice = new RSA();
        RSA Bob = new RSA();

        String poslaniye = "Idi kod' na Prolog'e!";
        System.out.println("��� ���� �� �����: " + Arrays.toString(Alice.getOK()));
        System.out.println("������ �� ������� ��� ���������.");
        System.out.println();

        BigInteger[] Crypted = Bob.encrypt(Alice.getOK(), poslaniye);
        StringBuilder Kek = new StringBuilder();

        for (BigInteger a : Crypted) {
            Kek.append(a);
        }
        String EncryptedMessage = Kek.toString();
        System.out.println("��� ������� ����� ���: " + EncryptedMessage);

        System.out.println();

        String Decryption = Alice.decrypt(Crypted);
        System.out.println("����� ������� ��������� d � modulo, �������������� � ��������: " + Decryption);


    }
}

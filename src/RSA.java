import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RSA {
    private BigInteger[] O_K = new BigInteger[2];
    private BigInteger[] S_K = new BigInteger[2];

    RSA() {
        //��������� 2 ������� ����� ������ 1024

        BigInteger p = createP();
        BigInteger g = createG();

                // modulo = p*g

                BigInteger modulo = p.multiply(g);

        //�-�� ������: �(modulo) = (p-1) * (g-1)

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(g.subtract(BigInteger.ONE));

        // e - �������� ����������, ������� ����� � ���������: 1<e<phi, ������ ��� � phi = 1

        BigInteger e = BigInteger.valueOf(2);
        //���� e < phi � ����� �������-�������, �� ��
        while ((e.compareTo(phi) < 0) & !phi.gcd(e).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }

        // d - ��������� ����������, ������ ��������� � ����� e �� ������ phi(modulo)
        // �.�. d*e = 1 (mod phi(modulo)) ��� d = e^(-1) (mod phi(modulo))

        BigInteger d = e.modInverse(phi);

        O_K[0] = e;
        O_K[1] = modulo;

        S_K[0] = d;
        S_K[1] = modulo;
    }

    public BigInteger[] getOK() {
        return new BigInteger[]{O_K[0], O_K[1]};
    }

    public BigInteger[] encrypt(BigInteger[] O_K, String str) {
        char[] str_chars = str.toCharArray();
        BigInteger[] encrypted_struct = new BigInteger[str_chars.length];
        for (int i = 0; i < str_chars.length; i++) {
            // c = str^e mod modulo
            encrypted_struct[i] = (BigInteger.valueOf((int) str_chars[i])).modPow(O_K[0], O_K[1]);
        }
        return encrypted_struct;
    }

    public String decrypt(BigInteger[] encryptedStr) {
        char[] strChars = new char[encryptedStr.length];
        StringBuilder decrypted_struct = new StringBuilder();
        for (int i = 0; i < encryptedStr.length; i++) {
            // m = c^d mod modulo
            strChars[i] = (char) encryptedStr[i].modPow(S_K[0], S_K[1]).intValue();
            decrypted_struct.append(strChars[i]);
        }
        return decrypted_struct.toString();
    }


        public boolean MillerRabinTest (BigInteger n,int k)
        {
            // ���� n == 2 ��� n == 3 - ��� ����� �������, ���������� true
            if (n.equals(BigInteger.valueOf(2)) || n.equals(BigInteger.valueOf(3)))
                return true;

            // ���� n < 2 ��� n ������ - ���������� false
            if (n.compareTo(BigInteger.valueOf(2)) < 0 || n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))
                return false;

            // ���������� n ? 1 � ���� (2^s)�t, ��� t �������, ��� ����� ������� ���������������� �������� n - 1 �� 2
            BigInteger t = n.subtract(BigInteger.ONE);

            int s = 0;

            while (t.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))

            {
                t = t.divide(BigInteger.valueOf(2));
                s += 1;
            }

            // ��������� k ���
            for (int i = 0; i < k; i++) {
                // ������� ��������� ����� ����� a � ������� [2, n ? 2]
                SecureRandom rng = new SecureRandom();

                byte[] _a = new byte[n.toByteArray().length];

                BigInteger a;

                do {
                    rng.nextBytes(_a);
                    a = new BigInteger(_a);
                }
                while (a.compareTo(BigInteger.valueOf(2)) < 0 || a.compareTo(n.subtract(BigInteger.valueOf(2))) >= 0);

                // x ? a^t mod n, �������� � ������� ���������� � ������� �� ������
                BigInteger x = a.modPow(t, n);

                // ���� x == 1 ��� x == n ? 1, �� ������� �� ��������� �������� �����
                if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE)))
                    continue;

                // ��������� s ? 1 ���
                for (int r = 1; r < s; r++) {
                    // x ? x^2 mod n
                    x = x.modPow(BigInteger.valueOf(2), n);

                    // ���� x == 1, �� ������� "���������"
                    if (x.equals(BigInteger.ONE))
                        return false;

                    // ���� x == n ? 1, �� ������� �� ��������� �������� �������� �����
                    if (x.equals(n.subtract(BigInteger.ONE)))
                        break;
                }

                if (!Objects.equals(x, n.subtract(BigInteger.ONE)))
                    return false;
            }

            // ������� "�������� �������"
            return true;
        }

    private BigInteger createP() {
        BigInteger p_out = null;
        BigInteger p = BigInteger.valueOf(getP());
        if (MillerRabinTest(p, 5)) {
            p_out = p;
        }
        return p_out;
    }

    private BigInteger createG() {
        BigInteger g_out = null;
        BigInteger g = BigInteger.valueOf(getG());
        if (MillerRabinTest(g, 5)) {
            g_out = g;
        }
        return g_out;
    }
    private Integer getP(){
        List<Integer> PrimeList = new ArrayList<>();
        int n = 9999999;
        boolean [] isPrime = new boolean[n];
        isPrime[0]=isPrime[1]=false; //0 i 1 ne prime

        for(int i = 2; i < n; i++){
            isPrime[i] = true; // ����� ��� ���������� �������
        }
//���� ����� �������, �� ����� ������������ � ��� ��� �� ������� �����, ����� �����������
        for(int i = 2; i < isPrime.length; i++){
            if (isPrime[i]){
                for (int j = 2; j*i<isPrime.length; j++){
                    isPrime[i*j] = false;
                }
            }
        }

        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                PrimeList.add(i);
            }
        }
        Random rnc = new Random();
        for(int i = 0; i < PrimeList.size(); i++)
            if (MillerRabinTest(BigInteger.valueOf(i),5 ))
                return PrimeList.get(rnc.nextInt(PrimeList.size()));
        return null;
    }
    private Integer getG(){
        List<Integer> PrimeList1 = new ArrayList<>();
        int n = 9999999;
        boolean [] isPrime1 = new boolean[n];
        isPrime1[0]=isPrime1[1]=false; //0 i 1 ne prime

        for(int i = 2; i < n; i++){
            isPrime1[i] = true; // ����� ��� ���������� �������
        }
//���� ����� �������, �� ����� ������������ � ��� ��� �� ������� �����, ����� �����������
        for(int i = 2; i < isPrime1.length; i++){
            if (isPrime1[i]){
                for (int j = 2; j*i<isPrime1.length; j++){
                    isPrime1[i*j] = false;
                }
            }
        }

        for (int i = 2; i < n; i++) {
            if (isPrime1[i]) {
                PrimeList1.add(i);
            }
        }
        Random rnc = new Random();
        for(int i = 0; i < PrimeList1.size(); i++)
            if (MillerRabinTest(BigInteger.valueOf(i),5 ))
                return PrimeList1.get(rnc.nextInt(PrimeList1.size()));
        return null;
    }
}


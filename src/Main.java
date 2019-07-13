import java.io.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static String sliczba = "";
    private static int rozmiar = 1048;
    private static BigInteger liczba;
    private static int n = 3;
    static Random generator =  new Random();
    static BigInteger jeden = BigInteger.valueOf(1);
    static BigInteger zero = BigInteger.valueOf(0);

    public static BigInteger losowanieBazy(BigInteger a, BigInteger b)
    {
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(b.bitLength(), new Random());

        } while (randomNumber.compareTo(b) == 1 || randomNumber.compareTo(a) == -1 );

        return randomNumber;
    }

    static BigInteger potegaIModulo(BigInteger a, BigInteger e, BigInteger n)
    {
        BigInteger  p, w;

        p = a; w = jeden;
        int m ;
        //int licznik=0;
        //for (m = 1; m ==0 ; m <<= 1)
        for (int licznik=0; licznik < rozmiar ; licznik++)
        {
            if (e.testBit(licznik)){

                w = mnozenieIModulo(w, p, n);
            }
            p = mnozenieIModulo(p, p, n);
            //licznik++;

        }
        return w;

    }

    static BigInteger mnozenieIModulo(BigInteger a, BigInteger b, BigInteger n)
    {
        BigInteger  w;

        w = zero;
        int m;
        //int licznik=0;
        //for (m = 1; m == 0 ; m <<= 1)
        for (int licznik=0; licznik < rozmiar ; licznik++)
        {
            if (b.testBit(licznik)){

                w = w.add( a).mod(n) ;

                //w = w.mod(n);
            }
            a = a.shiftLeft(1).mod(n);
            //a = a.mod(n) ;
           // licznik++;

        }
        return w;
    }

    public static boolean wstepnaSelekcja(BigInteger liczba) {

        if (liczba.mod(BigInteger.valueOf(7)).compareTo(zero) == 0 && liczba.compareTo(BigInteger.valueOf(7)) != 0) return false;
        if (liczba.mod(BigInteger.valueOf(17389)).compareTo(zero) == 0  && liczba.compareTo(BigInteger.valueOf(17389)) != 0 ) return false;
        if (liczba.mod(BigInteger.valueOf(14143)).compareTo(zero) == 0  && liczba.compareTo(BigInteger.valueOf(14143)) != 0 ) return false;
        if (liczba.mod(BigInteger.valueOf(15271)).compareTo(zero) == 0 && liczba.compareTo(BigInteger.valueOf(15271)) != 0 ) return false;

        return true;
    }

    public static BigInteger konwersjaSchematHornera(String s) {
        BigInteger  c;
        BigInteger L;
        //char z[1];

        L = BigInteger.valueOf(s.charAt(0) - 48);
        for (int i = 1; i < s.length (); i++)
        {
            c = BigInteger.valueOf(s.charAt(i) - 48);
            L = L.multiply(BigInteger.valueOf(2));
            L = L.add(c);
        }

        return L;
    }

    public static void odczytajPlik() throws FileNotFoundException {
        FileReader fr = null;
        String linia = "";

        // OTWIERANIE PLIKU:
        try {
            fr = new FileReader("dane.txt");
        } catch (FileNotFoundException e) {
            System.out.println("BŁĄD PRZY OTWIERANIU PLIKU!");
            System.exit(1);
        }

        BufferedReader bfr = new BufferedReader(fr);
        // ODCZYT KOLEJNYCH LINII Z PLIKU:
        try {
            while((linia = bfr.readLine()) != null){
                sliczba = sliczba + linia;
            }
        } catch (IOException e) {
            System.out.println("BŁĄD ODCZYTU Z PLIKU!");
            System.exit(2);
        }

        // ZAMYKANIE PLIKU
        try {
            fr.close();
        } catch (IOException e) {
            System.out.println("BŁĄD PRZY ZAMYKANIU PLIKU!");
            System.exit(3);
        }

    }

    public static void generujPlik() throws FileNotFoundException {

        PrintWriter zapis = new PrintWriter("dane.txt");

        for (int i = 0; i < rozmiar; i++) {

            if (i == rozmiar - 1) zapis.println(1);
            else zapis.println(generator.nextInt(2));

        }
        zapis.close();

    }

    public static void main(String[] args) throws FileNotFoundException {
        int iloscLiczb =0;
        long millisActualTime = System.currentTimeMillis();
        do {

            sliczba ="";
            int iteracja = 0;

            BigInteger d, x, a;
            int s = 0;
            Boolean pierwsza = true;
            //System.out.println("Test pierwszosci");
            generujPlik();
            odczytajPlik();
            Scanner scan = new Scanner(System.in);
            //System.out.println("Liczba : ");
//        String sliczba = scan.nextLine();
//        liczba = new BigInteger(sliczba);;
//
//        System.out.println("Wczytana liczba bitowo: " + sliczba);
            liczba = konwersjaSchematHornera(sliczba);
            //liczba = new BigInteger("13");
            //liczba = new BigInteger("9223372036854775783");
            //System.out.println("Liczba dec: " + liczba);

            //tutaj przed wykonaniem testu rabina wykonujemy wstepna selekcje
            if (wstepnaSelekcja(liczba)) {


            //if (sprwdzLiczbe(liczba)) {
            d = liczba.subtract(jeden);
            BigInteger liczbaMinusJeden = liczba.subtract(jeden);
            do {

                if (d.mod(BigInteger.valueOf(2)) == zero) {
                    d = d.divide(BigInteger.valueOf(2));
                    s ++;
                }
                else { break; }

            } while (true);

            for (int i = 0; i < n; i++)
            {
                a = losowanieBazy(BigInteger.valueOf(2), liczba.subtract(BigInteger.valueOf(2)) );
                //a = BigInteger.valueOf(5);
                //musze podniesc x = a^d mod liczba
                x = potegaIModulo(a, d, liczba);
                //cout << "Baza a=" << a << endl;
                //cout << "x=" << x << endl;
                if ((x.compareTo(jeden) == 0) || (x.compareTo(liczbaMinusJeden) == 0)) continue;

                int j = 1;
                //j jest naszym licznikiem ile razy potega jest mnozona
                while ((j < s) && (x.compareTo(liczbaMinusJeden) != 0)) {

                    //obliczanie kolejnego wyrazu ciagu
                    x = mnozenieIModulo(x, x, liczba);
                    if (x.compareTo(jeden) == 0 )
                    {
                        pierwsza = false; break;
                    }
                    j++;
                    iteracja++;
                }
                if (!pierwsza) break;
                //if (x != liczbaMinusJeden)
                if (x.compareTo(liczbaMinusJeden) != 0)
                {
                    pierwsza = false; break;
                }

            }

            if(pierwsza==true){
                long executionTime = System.currentTimeMillis() - millisActualTime; // czas wykonania programu w milisekundach.
                System.out.println("Liczba jest pierwsza : "  + pierwsza);
                System.out.println("Liczba dec : "  + liczba);
                System.out.println("Ilosc iteracji : "  + iteracja);
                System.out.println("czas " + executionTime);
                iloscLiczb++;
            }
         }
//        else {
//            // jezeli pierwsza selekcja wykazala ze nie jest pierwsza
//            System.out.println("Liczba jest pierwsza : NIE");
//            System.out.println("Wykazala to wstepna selekcja");
//        }

        }while(iloscLiczb < 1);

        long executionTime = System.currentTimeMillis() - millisActualTime; // czas wykonania programu w milisekundach.
//        System.out.println("Liczba jest pierwsza : "  + pierwsza);
//        System.out.println("Liczba dec : "  + liczba);
//        System.out.println("Ilosc iteracji : "  + iteracja);
        System.out.println("czas " + executionTime);


    }
}

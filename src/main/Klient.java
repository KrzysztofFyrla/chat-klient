package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Klient {

    public static final int PORT = 5000;
    //adres localhost, aby polaczyc sie z innym komputerem trzeba podac rzeczywisty jego adres IP
    public static final String IP = "127.0.0.1";

    BufferedReader bufferedReader;
    String name;
//
    public static void main(String[] args) {
        Klient klient = new Klient();
        klient.startKlient();
    }

    //uruchomienie klienta
    public void startKlient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj imię: ");
        name = scanner.nextLine();

        try {

            Socket socket = new Socket(IP, PORT);
            System.out.println("Połączono do " + socket);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread t = new Thread(new Odbiorca());
            t.start();

            while (true) {
                System.out.print(">>");
                String str = scanner.nextLine();
                if (!str.equalsIgnoreCase("q")) {
                    printWriter.println(name + ": " + str);
                    printWriter.flush(); //opróżnienie strumienia
                } else {
                    printWriter.println(name + " rozłączył się");
                    printWriter.flush();
                    printWriter.close();
                    scanner.close();
                    socket.close();
                }
            }

        } catch (Exception e) {

        }
    }

    class Odbiorca implements Runnable {

        @Override
        public void run() {
            String wiadomosc;

            try {
                while ((wiadomosc = bufferedReader.readLine()) != null) {
                    String subString[] = wiadomosc.split(": ");
                    if (!subString[0].equals(name)) {
                        System.out.println(wiadomosc);
                        System.out.println(">> ");
                    }
                }
            } catch (Exception e) {
                System.out.println("Połączenie zakończone");
            }
        }
    }
}

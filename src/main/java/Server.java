import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

public class Server {

    private static final int PORT = 8080;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен");
            String city = null;
            Set<String> cities = new TreeSet<>();
            boolean startingCity = true;
            char initialRequest;
            char finalRequest = ' ';

            while (true) {
                try (Socket clientSocket = serverSocket.accept(); PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    if (startingCity) {
                        out.println("???");
                        city = in.readLine();
                        cities.add(city.toLowerCase());
                        finalRequest = city.charAt(city.length() - 1);
                        startingCity = false;
                    } else {
                        if (finalRequest == 'ь') {
                            finalRequest = city.charAt(city.length() - 2);
                        }
                        out.println("Введите название города на букву " + finalRequest);
                        String city2 = in.readLine();
                        initialRequest = city2.toLowerCase().charAt(0);

                        if (finalRequest == initialRequest) {
                            if (cities.contains(city2.toLowerCase())) {
                                out.println("Not OK. Этот город уже вводили");
                                continue;
                            }
                            city = city2;
                            finalRequest = city2.charAt(city.length() - 1);
                            cities.add(city2.toLowerCase());
                            out.println("OK. Вы ввели: " + city);
                        } else {
                            out.println("NOT OK." + initialRequest);
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
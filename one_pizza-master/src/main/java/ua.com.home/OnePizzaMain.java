package ua.com.home;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OnePizzaMain {


    private static String[] some(List<Client> clients) {

        int count = 0;
        Collections.sort(clients, Client.Comparators.EFFICIENT.reversed());

        Set<String> likeProduct = Collections.newSetFromMap(new ConcurrentHashMap<>());
        Set<String> dislikeProduct = Collections.newSetFromMap(new ConcurrentHashMap<>());
        List<Client> removeList = new ArrayList<>();
        for (Client client : clients) {
            //if no dislike add product to product set
            if (client.getDislikesSize() == 0) {
                likeProduct.addAll(client.getLikes());
                removeList.add(client);
                count++;
            }

        }
        //del clients with zero dislikes frome clientsList
        for (Client client : removeList) {
            clients.remove(client);
        }
//        System.out.println("clients size after remove  ZERO D = " + clients.size());


        //compare this client dislike with like product set

        for (Client client : clients) {

            for (String dislike : client.getDislikes()) {
                boolean dislikeSomeFromProduct = false;
                for (String s : likeProduct) {
                    if (StringUtils.equals(dislike, s)) {
                        dislikeSomeFromProduct = true;
                        break;
                    } else {
                        continue;
                    }
                }
                if (dislikeSomeFromProduct) {
                    break;
                } else {
                    count++;
                    likeProduct.addAll(client.getLikes());
                    //TODO добавить в дизлайк а потом сравнить
                }
            }

        }

        System.out.println("Total clients: "+ count);
        System.out.println("likeProduct size= " + likeProduct.size());
        return new String[0];
    }


    private static List<Client> fillClientsList(List<String[]> trimString) {

        List<Client> clients = new ArrayList<>();

        for (int i = 1; i < trimString.size(); i += 2) {
            Set<String> allUniqLikeProducts = new HashSet<>();
            Set<String> allUniqDislikeProducts = new HashSet<>();
            Client client = new Client();

            String[] product = trimString.get(i);
            client.setLikesSize(product.length - 1);
            for (int j = 1; j < product.length; j++) {
                allUniqLikeProducts.add(product[j]);
            }
            client.setLikes(allUniqLikeProducts);

            String[] product2 = trimString.get(i + 1);
            client.setDislikesSize(product2.length - 1);
            for (int k = 1; k < product2.length; k++) {
                allUniqDislikeProducts.add(product2[k]);
            }
            client.setDislikes(allUniqDislikeProducts);
            if (client.getDislikesSize() == 0) {
                client.setEfficient(client.getLikesSize());
            } else {
                client.setEfficient(Precision.round(client.getLikesSize() / client.getDislikesSize(), 1));
            }
            client.setOpinionSize(client.getLikesSize() + client.getDislikesSize());
            clients.add(client);
//            System.out.println("L:"+client.getLikesSize()+" D:" + client.getDislikesSize() +
//                    " E:" + client.getEfficient() + " O:" + client.getOpinionSize());
        }


        //
        return clients;
    }


    private static List<String[]> fileToSeparateStringList(Path path) throws IOException {
        if (Files.exists(path)) {
            //Режу строку на слова
            return trimString(Files.lines(path).toList());
        }
        throw new IOException();
    }


    private static List<String[]> trimString(List<String> input) {
        List<String[]> trimInPutString = new ArrayList<>();
        for (String s : input) {
            trimInPutString.add(s.split(" "));
        }
        return trimInPutString;
    }


    ///TODOСамые дис
    //TODO caмые лайк
    // юежать по ефишенту и потом по списку масс дислайк

    public static void main(String[] args) {
        Path a = Paths.get("src/main/resources/a_an_example.in.txt");
        Path b = Paths.get("src/main/resources/b_basic.in.txt");
        Path c = Paths.get("src/main/resources/c_coarse.in.txt");
        Path d = Paths.get("src/main/resources/d_difficult.in.txt");
        Path e = Paths.get("src/main/resources/e_elaborate.in.txt");
        try {
            List<Client> clientListA = fillClientsList(fileToSeparateStringList(a));
            List<Client> clientListB = fillClientsList(fileToSeparateStringList(b));
            List<Client> clientListC = fillClientsList(fileToSeparateStringList(c));
            List<Client> clientListD = fillClientsList(fileToSeparateStringList(d));
            List<Client> clientListE = fillClientsList(fileToSeparateStringList(e));

            some(clientListA);
//            some(clientListB);
//            some(clientListC);
//            some(clientListD);
//            some(clientListE);


//       allProductsByCategory.forEach((key,value) -> System.out.println("total: " + value.size()+ " " + key + " : " + value));
//
//       Set<String> answer = findNoDislikeProduct(allProductsByCategory);
//            System.out.println(answer.size());
//            System.out.println(answer.toString());
        } catch (IOException ex) {
            System.out.println("NO INPUT FILE " + ex.getMessage());
        }

    }


}

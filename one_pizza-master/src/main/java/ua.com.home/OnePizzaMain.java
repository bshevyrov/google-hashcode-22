package ua.com.home;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class OnePizzaMain {


    private static  List<Client> fillClientsList(List<String[]> trimString) {

        List<Client> clients = new ArrayList<>();

        for (int i = 1; i < trimString.size(); i += 2) {
            Set<String> allUniqLikeProducts = new HashSet<>();
            Set<String> allUniqDislikeProducts = new HashSet<>();
            Client client = new Client();

            String[] product = trimString.get(i);
            client.setLikesSize(product.length-1);
            for (int j = 1; j < product.length; j++) {
                allUniqLikeProducts.add(product[j]);
            }
            client.setLikes(allUniqLikeProducts);

            String[] product2 = trimString.get(i + 1);
            client.setDislikesSize(product2.length-1);
            for (int k = 1; k < product2.length; k++) {
                allUniqDislikeProducts.add(product2[k]);
            }
            client.setDislikes(allUniqDislikeProducts);
            if(client.getDislikesSize()==0){
                client.setEfficient(client.getLikesSize());
            }else{
                client.setEfficient(client.getLikesSize()/ client.getDislikesSize());
            }
            client.setOpinionSize(client.getLikesSize()+client.getDislikesSize());
            clients.add(client);
//            System.out.println("L:"+client.getLikesSize()+" D:" + client.getDislikesSize() +
//                    " E:" + client.getEfficient() + " O:" + client.getOpinionSize());
        }
        System.out.println("Size: " + clients.size());
        Collections.sort(clients,Client.Comparators.OP_SIZE);
        System.out.println(Arrays.toString(clients.toArray()));
        return clients;
    }


    private static Set<String> findNoDislikeProduct(Map<String, Set<String>> products) {
        Set<String> likeProducts = products.get("allUniqLikeProducts");
        Set<String> dislikeProducts = products.get("allUniqDislikeProducts");
        Set<String> noDislikeProduct = new HashSet<>();

        for (String likeProduct : likeProducts) {
            if (!dislikeProducts.contains(likeProduct)) {
                noDislikeProduct.add(likeProduct);
            }
        }
        return noDislikeProduct;
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


    public static void main(String[] args) {

        try {
            fillClientsList(fileToSeparateStringList(Paths.get("src/main/resources/e_elaborate.in.txt")));

//       allProductsByCategory.forEach((key,value) -> System.out.println("total: " + value.size()+ " " + key + " : " + value));
//
//       Set<String> answer = findNoDislikeProduct(allProductsByCategory);
//            System.out.println(answer.size());
//            System.out.println(answer.toString());
        } catch (IOException e) {
            System.out.println("NO INPUT FILE " + e.getMessage());
        }

    }
}

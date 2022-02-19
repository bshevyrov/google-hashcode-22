package ua.com.home;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class OnePizzaMain {

    private static final Map<String, Integer> likeIngredients = new HashMap<>();
    private static final Map<String, Integer> disLikeIngredients = new HashMap<>();
    private static final Map<String, Integer> finalIngredients = new HashMap<>();

    private static   Map<String, Set<String>> getAllProductsByCategory( List<String[]> trimString) {
        Map<String, Set<String>> productsByCategory = new HashMap<>();
        Set<String> allUniqLikeProducts = new HashSet<>();
        Set<String> allUniqDislikeProducts = new HashSet<>();
        for (int i = 1; i < trimString.size(); i++) {

            if(i%2!=0){
                if(trimString.get(i).length<=1){
                    continue;
                } else {
                    String [] product  = trimString.get(i);
                    for (int j= 1; j < product.length;j++ ){
                        allUniqLikeProducts.add(product[j]);
                    }
                }
            }else{
                if(trimString.get(i).length<=1){
                    continue;
                } else {
                    String [] product  = trimString.get(i);
                    for (int j= 1; j < product.length;j++ ){
                        allUniqDislikeProducts.add(product[j]);
                    }
                }
            }

        }
        productsByCategory.put("allUniqLikeProducts",allUniqLikeProducts);
        productsByCategory.put("allUniqDislikeProducts",allUniqDislikeProducts);
        return productsByCategory;
    }


    private static Set<String> findNoDislikeProduct(Map<String,Set<String>> products){
       Set<String> likeProducts = products.get("allUniqLikeProducts");
       Set<String> dislikeProducts = products.get("allUniqDislikeProducts");
       Set<String> noDislikeProduct = new HashSet<>();

        for (String likeProduct : likeProducts) {
          if(!  dislikeProducts.contains(likeProduct)){
              noDislikeProduct.add(likeProduct);
          }
        }
           return noDislikeProduct;
    }

/*

    private static void fillMaps() throws IOException {
        Path path = Paths.get("a_an_example.in.txt");
        List<String> list = fileToStringList(path);

        for (int i = 1; i <= Integer.parseInt(list.get(0)) * 2; i += 2) {
            String[] likeParts = list.get(i).split(" ");
            for (int j = 1; j <= Integer.parseInt(likeParts[0]); j++) {
                if (likeIngredients.containsKey(likeParts[j]))
                    likeIngredients.put(likeParts[j], likeIngredients.get(likeParts[j]) + 1);
                else likeIngredients.put(likeParts[j], 1);
            }
            String[] disLikeParts = list.get(i + 1).split(" ");
            for (int j = 1; j <= Integer.parseInt(disLikeParts[0]); j++) {
                if (disLikeIngredients.containsKey(disLikeParts[j]))
                    disLikeIngredients.put(disLikeParts[j], disLikeIngredients.get(disLikeParts[j]) + 1);
                else disLikeIngredients.put(disLikeParts[j], 1);
            }
        }
    }

    private static void countFinalIngredients() {
        likeIngredients.forEach((key, value) -> {
            if (disLikeIngredients.containsKey(key)) {
                if (likeIngredients.get(key) - disLikeIngredients.get(key) > 0)
                    finalIngredients.put(key, likeIngredients.get(key) - disLikeIngredients.get(key));
            } else finalIngredients.put(key, value);
        });
    }

    private static void output() {
        Path path = Paths.get("output.txt");
        String result = "";

        int count = finalIngredients.keySet().size();
        result = count + " ";

        for (String s : finalIngredients.keySet()) {
            result += s + " ";
        }

        result = result.substring(0, result.length() - 1);

        if (Files.exists(path)) {
            try {
                Files.delete(path);
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.write(path, result.getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    private static List<String[]> fileToSeparateStringList(Path path) throws IOException {
        if (Files.exists(path)) {
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
       /* fillMaps();
        System.out.println("Liked igredients: ");
        likeIngredients.forEach((key, value) -> System.out.println(key + ":" + value));
        System.out.println();
        System.out.println("Disliked igredients: ");
        disLikeIngredients.forEach((key, value) -> System.out.println(key + ":" + value));
        System.out.println();
        countFinalIngredients();
        finalIngredients.forEach((key, value) -> System.out.println(key + ":" + value));
        output();*/
        try {
            Map<String, Set<String>> allProductsByCategory = getAllProductsByCategory(fileToSeparateStringList(Paths.get("e_elaborate.in.txt")));

       allProductsByCategory.forEach((key,value) -> System.out.println("total: " + value.size()+ " " + key + " : " + value));

       Set<String> answer = findNoDislikeProduct(allProductsByCategory);
            System.out.println(answer.size());
//            System.out.println(answer.toString());
        } catch (IOException e) {
            System.out.println("NO INPUT FILE " +  e.getMessage());
        }

    }
}

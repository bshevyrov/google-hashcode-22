package ua.com.home;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OnePizzaMain {
    static Set<String> likeProducts = Collections.newSetFromMap(new ConcurrentHashMap<>());

    static Set<String> dislikeProducts = Collections.newSetFromMap(new ConcurrentHashMap<>());
    static Map<String, Integer> allDislike = new HashMap<>();
    static Map<String, Integer> allLike = new HashMap<>();


    /*
        private static List<Ingredient> findDislikeRatingList(List<Client>clients){


            allDislike =
                    allDislike.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue()).collect(
                                    Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                            LinkedHashMap::new));
        }
        private static List<Ingredient> findLikeRatingList(List<Client>clients){
            allLike =
                    allLike.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue()).collect(
                                    Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                            LinkedHashMap::new));
        }*/


    private static List<Client> addClientListToAllIfProductOk(List<Client> all, Client client) {


        for (String dislike : client.getDislikes()) {
            boolean dislikeSomeFromLikeProductList = false;
            for (String s : likeProducts) {
                //Сравниванием дислайки нового клиента со списком уже имежщихся лайк продуктов
                //Если Новый клиент не любит чтото уже из существующего - скипаем клиента
                //Тут ПОИГРАТЬСЯ С Сортировкой
                if (StringUtils.equals(dislike, s)) {
                    dislikeSomeFromLikeProductList = true;
                    break;
                }
            }
            if (dislikeSomeFromLikeProductList) {
                break;
                //

            } else {
                Set<String> currentClientLikes = client.getLikes();
                Set<String> currentClientDislikes = client.getDislikes();
                boolean skip = false;

                //   - каждый новый клиент не должен не любить чтото из списка уже лайкнутых продуктов

                for (String currentClientDislike : currentClientDislikes) {
                    if (likeProducts.contains(currentClientDislike)) {
                        skip = true;
                        break;
                    }
                }
                if (!skip) {
                    //   - каждый новый клиент не должен любить чтото из списка не любимых продуктов

                    for (String currentClientLike : currentClientLikes) {
                        if (dislikeProducts.contains(currentClientLike)) {
                            skip = true;
                            break;
                        }
                    }
                }

                if (!skip) {
                    likeProducts.addAll(client.getLikes());
                    dislikeProducts.addAll(client.getDislikes());
                    all.add(client);

                }

            }


        }
        return all;

    }


    private static int findClientsMaxDislikeSize(List<Client> clients) {
        int tmp = 0;
        for (Client client : clients) {
            if (client.getDislikesSize() > tmp) {
                tmp = (int) client.getDislikesSize();
            }
        }
        return tmp;
    }

    private static int findClientsMaxLkeSize(List<Client> clients) {
        int tmp = 0;
        for (Client client : clients) {
            if (client.getLikesSize() > tmp) {
                tmp = (int) client.getLikesSize();
            }
        }
        return tmp;
    }

    private static int findHowManyPeopleDislikeIngredient(String ingredient) {
        return
                null == allDislike.get(ingredient) ? 0 : allDislike.get(ingredient);

    }

    private static List<Client> findClientsWhoLikeByProduct(String product, List<Client> clients) {
        List<Client> clientsWhoLike = new ArrayList<>();
        for (Client client : clients) {
            if (client.getLikes().contains(product)) {
                clientsWhoLike.add(client);
            }
        }
        return clientsWhoLike;
    }

    private static List<Client> findClientWhoDislikeByProduct(String product, List<Client> clients) {
        List<Client> clientsWhoDislike = new ArrayList<>();
        for (Client client : clients) {
            if (client.getDislikes().contains(product)) {
                clientsWhoDislike.add(client);
            }
        }
        return clientsWhoDislike;
    }

    private static void lessFrequentSort(Set<Client> clientSet) {


    }

    private static Map<String, Set<Client>> sortLikeOneOpinionOne(Set<Client> clientSet) {
        Set<Client> goodClients = new HashSet<>();
        Set<Client> otherClients = new HashSet<>();
        Map<String, Set<Client>> rsl = new HashMap<>();

        for (Client client : clientSet) {
            if (client.getOpinionSize() == 1 && client.getLikesSize() == 1) {
                goodClients.add(client);
            }
        }
        otherClients.addAll(clientSet);
        otherClients.removeAll(goodClients);
        rsl.put("sorted", goodClients);
        rsl.put("other", otherClients);
        return rsl;
    }


    private static void output(Set<String> likeProducts, String taskName) {
        Path output = Paths.get("src/main/resources/" + taskName + "_output.txt");

        int ingredientsSize = likeProducts.size();
        StringBuilder result = new StringBuilder(ingredientsSize + " ");
        for (String ingredient : likeProducts) {
            result.append(ingredient).append(" ");
        }
        result = new StringBuilder(result.substring(0, result.length() - 1));
        try {
            if (Files.exists(output)) {
                Files.delete(output);
            }
            Files.createFile(output);
            Files.write(output, result.toString().getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> calculateOptimalLikeProduct(List<Client> clients) {

        int count = 0;
//        Collections.sort(clients, Client.Comparators.EFFICIENT.reversed());


        System.out.println(allLike.toString());
        System.out.println("~~~~");
        System.out.println(allDislike.toString());


        List<Client> finalClients = getOnlyClientsWhiteoutDislikeRating(clients);
        clients.removeAll(finalClients);

        clients.sort(Client.Comparators.MAX_DIS_MIN_LIKE);
        System.out.println(clients.toString());

        finalClients.addAll(getOnlYOneLikeAndAllDislikeWithOutDislikeRaiting(clients));
        clients.removeAll(finalClients);






        System.out.println("Total clients: " + finalClients.size());
        System.out.println("likeProducts size= " + likeProducts.size());
        return likeProducts;
    }

    private static Collection<? extends Client> getOnlYOneLikeAndAllDislikeWithOutDislikeRaiting(List<Client> clients) {
        List<Client> rsl = new ArrayList<>();
        int lkeSize = findClientsMaxLkeSize(clients);
        int dislikeSize = findClientsMaxDislikeSize(clients);
        for (int j = 0; j < dislikeSize; j++) {

            for (int i = 0; i < lkeSize; i++) {
                for (Client client : clients) {

                    if (client.getLikesSize() == lkeSize - i && client.getLikesSize() == dislikeSize) {
                        boolean noOne = true;

                        for (int k = 0; k < client.getLikesSize(); k++) {
                            if (findHowManyPeopleDislikeIngredient(client.getLikes().stream().toList().get(k)) > 0){
                                noOne = false;
                            }
                        }
                        if (noOne) {
                            addClientListToAllIfProductOk(rsl, client);
                        }
                    }
                }
            }
        }
        System.out.println(rsl.toString());
        System.out.println(rsl.size());
        return rsl;
    }

    private static List<Client> getOnlyClientsWhiteoutDislikeRating(List<Client> clients) {
        int count = 0;
        List<Client> goodClients = new ArrayList<>();
        for (Client client : clients) {
            //if no dislike add product to product set
            if (client.getDislikesSize() == 0 && client.getLikesSize() == 1 && findHowManyPeopleDislikeIngredient(client.getLikes().stream().findFirst().get()) == 0) {
                likeProducts.addAll(client.getLikes());
                goodClients.add(client);
                count++;
            }

        }
        System.out.println("getOnlyOneLikesClients: " + count);
        return goodClients;
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
                addToLikeStatistic(product[j]);
            }
            client.setLikes(allUniqLikeProducts);

            String[] product2 = trimString.get(i + 1);
            client.setDislikesSize(product2.length - 1);
            for (int k = 1; k < product2.length; k++) {
                allUniqDislikeProducts.add(product2[k]);
                addToDislikeStatistic(product2[k]);

            }
            client.setDislikes(allUniqDislikeProducts);
            if (client.getDislikesSize() == 0) {
                client.setEfficient(client.getLikesSize());
            } else {
                client.setEfficient(Precision.round(client.getLikesSize() / client.getDislikesSize(), 1));
            }
            client.setOpinionSize(client.getLikesSize() + client.getDislikesSize());
            clients.add(client);
//TODO ТУТ СТАТИСТИКА ПО КЛИЕНТАМ
//            System.out.println("L:"+client.getLikesSize()+" D:" + client.getDislikesSize() +
//                    " E:" + client.getEfficient() + " O:" + client.getOpinionSize());
        }


        allDislike =
                allDislike.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue()).collect(
                                Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                        LinkedHashMap::new));
        allLike =
                allLike.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue()).collect(
                                Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                        LinkedHashMap::new));


        return clients;
    }

    private static void addToDislikeStatistic(String s) {
        if (allDislike.containsKey(s)) {
            allDislike.put(s, allDislike.get(s) + 1);
        } else {
            allDislike.put(s, 1);
        }
    }

    private static void addToLikeStatistic(String s) {
        if (allLike.containsKey(s)) {
            allLike.put(s, allLike.get(s).intValue() + 1);
        } else {
            allLike.put(s, 1);
        }
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
        Path a = Paths.get("src/main/resources/a_an_example.in.txt");
        Path b = Paths.get("src/main/resources/b_basic.in.txt");
        Path c = Paths.get("src/main/resources/c_coarse.in.txt");
        Path d = Paths.get("src/main/resources/d_difficult.in.txt");
        Path e = Paths.get("src/main/resources/e_elaborate.in.txt");

        try {
           /* List<Client> clientListA = fillClientsList(fileToSeparateStringList(a));
            List<Client> clientListB = fillClientsList(fileToSeparateStringList(b));
            List<Client> clientListC = fillClientsList(fileToSeparateStringList(c));
            List<Client> clientListD = fillClientsList(fileToSeparateStringList(d));
            */
            List<Client> clientListE = fillClientsList(fileToSeparateStringList(e));

           /* System.out.println("input a size:" + clientListA.size());
           output( calculateOptimalLikeProduct(clientListA),"a");
            System.out.println("~~~~~~~~~~~~~~");
            System.out.println("input b size:" + clientListB.size());
            output( calculateOptimalLikeProduct(clientListB),"b");
            System.out.println("~~~~~~~~~~~~~~");
            System.out.println("input c size:" + clientListC.size());
            output( calculateOptimalLikeProduct(clientListC),"c");
            System.out.println("~~~~~~~~~~~~~~");
            System.out.println("input d size:" + clientListD.size());
            output( calculateOptimalLikeProduct(clientListD),"d");*/
            System.out.println("~~~~~~~~~~~~~~");
            System.out.println("input e size:" + clientListE.size());
            output(calculateOptimalLikeProduct(clientListE), "e");


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

class Client implements Comparable<Client> {

    private Set<String> likes;
    private double likesSize;
    private Set<String> dislikes;
    private double dislikesSize;
    private double efficient;
    private double opinionSize;


    public double getOpinionSize() {
        return opinionSize;
    }

    public void setOpinionSize(double opinionSize) {
        this.opinionSize = opinionSize;
    }

    public double getLikesSize() {
        return likesSize;
    }

    public void setLikesSize(double likesSize) {
        this.likesSize = likesSize;
    }

    public double getDislikesSize() {
        return dislikesSize;
    }

    public void setDislikesSize(double dislikesSize) {
        this.dislikesSize = dislikesSize;
    }

    public double getEfficient() {
        return efficient;
    }

    public void setEfficient(double efficient) {
        this.efficient = efficient;
    }

    public Client() {
    }

    public Set<String> getLikes() {
        return likes;
    }

    public void setLikes(Set<String> likes) {
        this.likes = likes;
    }

    public Set<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Set<String> dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public int compareTo(Client o) {

        return Comparators.OP_AND_EFF.compare(this, o);
    }

    public static class Comparators {
        public static final Comparator<Client> OP_SIZE = Comparator.comparingDouble(Client::getOpinionSize);
        public static final Comparator<Client> MAX_DIS = Comparator.comparingDouble(Client::getDislikesSize).reversed();
        public static final Comparator<Client> MIN_LIKE = Comparator.comparingDouble(Client::getLikesSize);
        public static final Comparator<Client> EFFICIENT = Comparator.comparingDouble(Client::getEfficient);
        public static final Comparator<Client> OP_AND_EFF = (Client o1, Client o2) -> EFFICIENT.thenComparing(OP_SIZE).compare(o1, o2);
        public static final Comparator<Client> MAX_DIS_MIN_LIKE = (Client o1, Client o2) -> MAX_DIS.thenComparing(MIN_LIKE).compare(o1, o2);
    }

    @Override
    public String toString() {
        return "Client{" +
                "L:" + likesSize +
                " D:" + dislikesSize +
                " E:" + efficient +
                " O:" + opinionSize +
                '}' + "\n";
    }
}

class Ingredient {

    private String name;
    private int allLikes;
    private int allDislikes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAllLikes() {
        return allLikes;
    }

    public void setAllLikes(int allLikes) {
        this.allLikes = allLikes;
    }

    public int getAllDislikes() {
        return allDislikes;
    }

    public void setAllDislikes(int allDislikes) {
        this.allDislikes = allDislikes;
    }
}
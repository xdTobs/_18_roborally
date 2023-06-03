package eighteen;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class StartRoborallyClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ClientLauncher.main(args);

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/start"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("response headers: " + response.toString());
            System.out.println("response body: " + response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // This is Henrik trying things with async and incrementing the id. We don't want race conditions.
    static void threadSafeStuff() {

        HttpClient httpClient = HttpClient.newBuilder().build();

        try {
            List<HttpRequest> requests = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .uri(new URI("http://localhost:8080/start"))
                        .build();
                requests.add(request);
            }


            List<CompletableFuture<HttpResponse<String>>> responses = new ArrayList<>();

            for (HttpRequest req : requests) {
                CompletableFuture<HttpResponse<String>> response =
                        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
                responses.add(response);
            }
            List<String> l = new ArrayList<>();
            for (CompletableFuture<HttpResponse<String>> res : responses) {
                String s = res.thenApply(HttpResponse::body).get();
                l.add(s);
            }
            List<Integer> intList = new ArrayList<>();
            for (String s : l) {
                // remove everything but digits
                s = s.replaceAll("\\D+", "");
                intList.add(Integer.parseInt(s));
            }
            Collections.sort(intList);
            for (Integer i : intList) {
                System.out.println(i.toString());
            }

        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {

    final static String URL = "https://api.nasa.gov/planetary/apod?api_key=dTjcVznNxs8EkVUS0iMplFUFq32FepBvwK6PVvyf";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        HttpGet request = new HttpGet(URL);
        CloseableHttpResponse response = createHttpClient().execute(request);
        JsonClass post = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });

        String url = post.getUrl();
        HttpGet request2 = new HttpGet(url);
        CloseableHttpResponse response2 = createHttpClient().execute(request2);

        String[] split = url.split("/");
        String nameFile = split[split.length-1];

        try (BufferedInputStream bis = new BufferedInputStream(response2.getEntity().getContent());
             FileOutputStream out = new FileOutputStream(nameFile);
             BufferedOutputStream bos = new BufferedOutputStream(out)){

            byte[] bytes = bis.readAllBytes();
            bos.write(bytes);
            bos.flush();

        } catch (IOException e){
            e.getMessage();
        }

    }

    public static CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
    }
}

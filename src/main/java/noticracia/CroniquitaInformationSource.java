package noticracia;

import noticracia.entities.InformationSource;
import services.HttpClientService;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CroniquitaInformationSource extends InformationSource {

    private boolean running = false;
    private final HttpClientService httpClientService;
    private ScheduledExecutorService executor;

    public CroniquitaInformationSource() {
        this.httpClientService = new HttpClientService("https://www.diariocronica.com.ar/rss/politica");
    }

    @Override
    public void start(String searchCriteria) {
        this.running = true;
        executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            if (running) {
                try {
                    String response = httpClientService.getInformation();

                    this.notify(this.mapInformation(response));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        this.running = false;
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @Override
    public String getName() {
        return "Croniquita";
    }

    public Map<String, String> mapInformation(Object response) {
        return  Arrays.stream(((String) response).split("<>"))
                .map(pair -> pair.split("==="))
                .collect(Collectors.toMap(parts -> parts[0].trim(), parts -> parts[1].trim()));
    }
}

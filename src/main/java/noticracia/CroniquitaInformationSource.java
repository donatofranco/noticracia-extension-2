package noticracia;

import mappers.InformationMapper;
import noticracia.entities.InformationSource;
import services.HttpClientService;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CroniquitaInformationSource extends InformationSource {

    private boolean running = false;
    private final HttpClientService httpClientService;
    private ScheduledExecutorService executor;
    private String searchCriteria;

    public CroniquitaInformationSource() {
        this.httpClientService = new HttpClientService("https://www.diariocronica.com.ar/rss/politica");
    }

    @Override
    public void start(String searchCriteria) {
        this.searchCriteria = searchCriteria;
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
        return "Croniquita RSS";
    }

    @Override
    public Map<String, String> mapInformation(Object response) {
        String[] words = this.searchCriteria.toLowerCase().split(" ");

        return new InformationMapper().mapInformation((String) response).entrySet().stream()
                //Le quito las informaciones que no traten de mi searchCriteria
                .filter(e -> e.getValue().toLowerCase().contains(this.searchCriteria.toLowerCase()))
                .collect(Collectors.toMap(
                        //Le quito las apariciones del searchCriteria
                        Map.Entry::getKey,
                        e -> {
                            String modifiedValue = e.getValue();
                            for (String word : words) {
                                modifiedValue = modifiedValue.replace(word, "");
                            }
                            return modifiedValue;
                        }
                ));
    }

}

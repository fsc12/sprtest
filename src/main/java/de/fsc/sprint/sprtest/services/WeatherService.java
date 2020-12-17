package de.fsc.sprint.sprtest.services;

import de.fsc.sprint.sprtest.model.WeatherForm;
import de.fsc.sprint.sprtest.repository.WeatherEntity;
import de.fsc.sprint.sprtest.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Zentraler Service für die Komponenten-Anlage.
 */
@Service
@Slf4j
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;


    @Async
    public void start(final WeatherForm cForm) {

        log.info(cForm.toString());
        weatherRepository.save(new WeatherEntity(0, cForm.getCity(), cForm.getMinTemp(), cForm.getMaxTemp(), cForm.getHumidity(), java.sql.Timestamp.valueOf(LocalDateTime.now())));
    }


    @Async
    public Map<String, List<Double>> calc() {
        Map<String, List<Double>> hmHi = new HashMap<>();
        Map<String, List<Double>> hmLo = new HashMap<>();
        Map<String, List<Double>> hmHumid = new HashMap<>();
        weatherRepository.findAll().forEach((w) -> {
            if (!hmHi.containsKey(w.getCity())) {
                List<Double> lHi = new ArrayList<>();
                List<Double> lLo = new ArrayList<>();
                List<Double> lHumid = new ArrayList<>();
                lHi.add(w.getTempHi());
                lLo.add(w.getTempLo());
                lHumid.add(w.getHumid().doubleValue());
                hmHi.put(w.getCity(), lHi);
                hmLo.put(w.getCity(), lLo);
                hmHumid.put(w.getCity(), lHumid);
                log.debug("ins: " + w.getCity() + " " + lHi + " " + lLo + " " + lHumid);
            } else {
                hmHi.get(w.getCity()).add(w.getTempHi());
                hmLo.get(w.getCity()).add(w.getTempLo());
                hmHumid.get(w.getCity()).add(w.getHumid().doubleValue());
                log.debug("upd: " + w.getCity() + " " + w.getTempHi() + " " + w.getTempLo() + " " + w.getHumid());
            }

        });
        Map<String, List<Double>> avgMap = new HashMap<>();
        //Summieren aller Werte
        for (String city : hmHi.keySet()) {
            Double sumHi = hmHi.get(city).stream().reduce(0.0, Double::sum);
            Double sumLo = hmLo.get(city).stream().reduce(0.0, Double::sum);
            Double sumHumid = hmHumid.get(city).stream().reduce(0.0, Double::sum);

            long cntHi = hmHi.get(city).stream().count();
            long cntLo = hmLo.get(city).stream().count();
            long cntHumid = hmHumid.get(city).stream().count();
            List<Double> lAvg = new ArrayList<>();
            lAvg.add(sumHi / cntHi);
            lAvg.add(sumLo / cntLo);
            lAvg.add(sumHumid / cntHumid);
            avgMap.put(city, lAvg);
            log.info("avg: " + city + " " + lAvg);
        }
        return avgMap;

    }


}

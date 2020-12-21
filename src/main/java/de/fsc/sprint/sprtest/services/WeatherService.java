package de.fsc.sprint.sprtest.services;

import de.fsc.sprint.sprtest.model.WeatherForm;
import de.fsc.sprint.sprtest.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;


/**
 * Zentraler Service für die Komponenten-Anlage.
 */
@Service
@Slf4j
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


    @Async
    public void start(final WeatherForm cForm) {

        log.info(cForm.toString());
        weatherRepository.save(new WeatherEntity(0, cForm.getCity(), cForm.getMinTemp(), cForm.getMaxTemp(), cForm.getHumidity(), java.sql.Timestamp.valueOf(LocalDateTime.now())));
    }


    public Map<String, List<Double>> calc() {
        Map<String, List<Double>> hmHi = new HashMap<>();
        Map<String, List<Double>> hmLo = new HashMap<>();
        Map<String, List<Double>> hmHumid = new HashMap<>();
        weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "city")).forEach((w) -> {
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

            long cntHi = hmHi.get(city).size();
            long cntLo = (long) hmLo.get(city).size();
            long cntHumid = hmHumid.get(city).size();
            List<Double> lAvg = new ArrayList<>();
            lAvg.add(sumHi / cntHi);
            lAvg.add(sumLo / cntLo);
            lAvg.add(sumHumid / cntHumid);
            avgMap.put(city, lAvg);
            log.info("avg: " + city + " " + lAvg);
        }
        return avgMap;

    }

    public Map<String, List<Double>> calc2() {
        final Map<String, List<Double>> hmHi = getHmHi();
        final Map<String, List<Double>> hmLo = getHmLo();
        final Map<String, List<Double>> hmHumid = getHmHumid();
        Map<String, List<Double>> avgMap = new HashMap<>();
        //Summieren aller Werte
        for (String city : hmHi.keySet()) {
            Double sumHi = hmHi.get(city).stream().reduce(0.0, Double::sum);
            Double sumLo = hmLo.get(city).stream().reduce(0.0, Double::sum);
            Double sumHumid = hmHumid.get(city).stream().reduce(0.0, Double::sum);

            long cntHi = hmHi.get(city).size();
            long cntLo = hmLo.get(city).size();
            long cntHumid = hmHumid.get(city).size();
            List<Double> lAvg = new ArrayList<>();
            lAvg.add(sumHi / cntHi);
            lAvg.add(sumLo / cntLo);
            lAvg.add(sumHumid / cntHumid);
            avgMap.put(city, lAvg);
            log.info("avg: " + city + " " + lAvg);
        }
        return avgMap;
    }


    private Map<String, List<Double>> getHmHi() {
        final Stream<WeatherEntity> weatherStream = StreamSupport.stream(weatherRepository.findAll().spliterator(), false);
        return weatherStream.collect(Collectors.toMap(WeatherEntity::getCity,
                w -> {
                    List<Double> lHi = new ArrayList<>();
                    lHi.add(w.getTempHi());
                    log.debug("ins: " + w.getCity() + " " + lHi);
                    return lHi;
                },
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        ));
    }

    private Map<String, List<Double>> getHmLo() {
        final Stream<WeatherEntity> weatherStream = StreamSupport.stream(weatherRepository.findAll().spliterator(), false);
        return weatherStream.collect(Collectors.toMap(WeatherEntity::getCity,
                w -> {
                    List<Double> lLo = new ArrayList<>();
                    lLo.add(w.getTempLo());
                    log.debug("ins: " + w.getCity() + " " + lLo);
                    return lLo;
                },
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        ));
    }


    private Map<String, List<Double>> getHmHumid() {
        final Stream<WeatherEntity> weatherStream = StreamSupport.stream(weatherRepository.findAll().spliterator(), false);
        return weatherStream.collect(Collectors.toMap(WeatherEntity::getCity,
                w -> {
                    List<Double> lHumid = new ArrayList<>();
                    lHumid.add(w.getHumid().doubleValue());
                    log.debug("ins: " + w.getCity() + " " + lHumid);
                    return lHumid;
                },
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        ));
    }
}

package de.fsc.sprint.sprtest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WeatherForm {
    private String city = "";
    //min. Temperatur
    private double minTemp = 0.0;
    //max. Temperatur
    private double maxTemp = 0.0;
    private int humidity = 0;
    private LocalDateTime date = LocalDateTime.now();


}

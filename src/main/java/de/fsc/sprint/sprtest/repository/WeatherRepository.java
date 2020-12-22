package de.fsc.sprint.sprtest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends PagingAndSortingRepository<WeatherEntity, Integer> {

}

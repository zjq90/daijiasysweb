
package com.daijia.service;

import com.daijia.entity.Driver;
import com.daijia.entity.Order;
import com.daijia.util.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DispatchService {
    
    @Autowired
    private DriverService driverService;
    
    public Driver findBestDriver(Order order) {
        List<Driver> availableDrivers = driverService.findAvailableDrivers();
        
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return null;
        }
        
        List<DriverScore> driverScores = new ArrayList<>();
        
        for (Driver driver : availableDrivers) {
            double distance = DistanceUtil.getDistance(
                order.getStartLat(), order.getStartLng(),
                driver.getLatitude(), driver.getLongitude()
            );
            
            double score = calculateScore(distance, driver.getCreditRating());
            
            driverScores.add(new DriverScore(driver, distance, score));
        }
        
        driverScores.sort(Comparator.comparingDouble(DriverScore::getScore).reversed());
        
        if (!driverScores.isEmpty()) {
            return driverScores.get(0).getDriver();
        }
        
        return null;
    }
    
    private double calculateScore(double distance, BigDecimal creditRating) {
        double distanceScore;
        if (distance <= 500) {
            distanceScore = 100;
        } else if (distance <= 1000) {
            distanceScore = 90;
        } else if (distance <= 2000) {
            distanceScore = 70;
        } else if (distance <= 3000) {
            distanceScore = 50;
        } else if (distance <= 5000) {
            distanceScore = 30;
        } else {
            distanceScore = 10;
        }
        
        double creditScore = creditRating != null ? creditRating.doubleValue() * 10 : 50;
        
        return distanceScore * 0.6 + creditScore * 0.4;
    }
    
    private static class DriverScore {
        private Driver driver;
        private double distance;
        private double score;
        
        public DriverScore(Driver driver, double distance, double score) {
            this.driver = driver;
            this.distance = distance;
            this.score = score;
        }
        
        public Driver getDriver() {
            return driver;
        }
        
        public double getDistance() {
            return distance;
        }
        
        public double getScore() {
            return score;
        }
    }
    
    public List<Driver> getRankedDrivers(Order order) {
        List<Driver> availableDrivers = driverService.findAvailableDrivers();
        
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<DriverScore> driverScores = new ArrayList<>();
        
        for (Driver driver : availableDrivers) {
            double distance = DistanceUtil.getDistance(
                order.getStartLat(), order.getStartLng(),
                driver.getLatitude(), driver.getLongitude()
            );
            
            double score = calculateScore(distance, driver.getCreditRating());
            
            driverScores.add(new DriverScore(driver, distance, score));
        }
        
        driverScores.sort(Comparator.comparingDouble(DriverScore::getScore).reversed());
        
        List<Driver> rankedDrivers = new ArrayList<>();
        for (DriverScore ds : driverScores) {
            rankedDrivers.add(ds.getDriver());
        }
        
        return rankedDrivers;
    }
}

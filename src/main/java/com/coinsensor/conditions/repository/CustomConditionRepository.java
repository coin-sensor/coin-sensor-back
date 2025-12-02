package com.coinsensor.conditions.repository;

import java.util.List;
import java.util.Optional;

import com.coinsensor.conditions.entity.Condition;

public interface CustomConditionRepository {
    
    Optional<List<Condition>> findByTimeframeName(String timeframeName);
}
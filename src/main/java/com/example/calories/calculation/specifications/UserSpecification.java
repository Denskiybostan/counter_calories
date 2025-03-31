package com.example.calories.calculation.specifications;

import com.example.calories.calculation.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasGoal(String goal) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("goal"), goal);
    }
    public static Specification<User> hasEmail(String email){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), email);
    }
}

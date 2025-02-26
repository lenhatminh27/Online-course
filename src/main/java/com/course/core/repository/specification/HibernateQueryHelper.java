package com.course.core.repository.specification;

import com.course.common.utils.ObjectUtils;
import com.course.core.repository.data.PageRequest;
import com.course.core.repository.data.Sort;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.Map;

public class HibernateQueryHelper {

    /**
     * Build a dynamic query with support for filtering, sorting, and pagination.
     */
    public static <T> Query<T> buildQuery(
            Session session,
            String baseHql,
            Map<String, Object> parameters,
            Sort sort,
            PageRequest pageRequest,
            Class<T> resultType
    ) {
        StringBuilder hql = new StringBuilder(baseHql);

        // Dynamically add sorting based on the Sort object
        if (sort != null && !ObjectUtils.isEmpty(sort.getOrders())) {
            String orderClause = sort.getOrders().stream()
                    .map(order -> {
                        String field = order.property();
                        String direction = order.direction().toString();
                        String alias = getAliasForField(field);
                        if (alias != null) {
                            return alias + field + " " + direction;
                        } else {
                            return field + " " + direction;
                        }
                    })
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            if (!orderClause.isEmpty()) {
                hql.append(" ORDER BY ").append(orderClause);
            }
        }
        // Create the query
        Query<T> query = session.createQuery(hql.toString(), resultType);
        // Set parameters for the query
        if (parameters != null) {
            parameters.forEach(query::setParameter);
        }
        // Apply pagination if specified
        if (pageRequest != null) {
            query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
            query.setMaxResults(pageRequest.getPageSize());
        }

        return query;
    }

    // Helper method to dynamically determine the alias for a field
    private static String getAliasForField(String field) {
        Map<String, String> fieldAliasMap = new HashMap<>();
        fieldAliasMap.put("createAt", "b.");  // BlogEntity createdAt
        fieldAliasMap.put("updatedAt", "b.");  // BlogEntity updatedAt
        fieldAliasMap.put("views", "bs.");  // BlogEntity views
        fieldAliasMap.put("likes", "bs.");  // BlogStatisticEntity likes
        fieldAliasMap.put("createdAt", "c.");  // CourseEntity createdAt
        fieldAliasMap.put("title", "c.");  // CourseEntity title
        fieldAliasMap.put("price", "c.");  // CourseEntity title
        return fieldAliasMap.getOrDefault(field, "b.");
    }


    public static Query<Long> buildCountQuery(
            Session session,
            String baseHql,
            Map<String, Object> parameters,
            String alias
    ) {
        StringBuilder countHql = new StringBuilder();

        // If alias is null, use COUNT(*), otherwise use COUNT(<alias>)
        if (alias == null) {
            countHql.append("SELECT COUNT(*) ").append(baseHql.substring(baseHql.indexOf("FROM")));
        } else {
            countHql.append("SELECT COUNT(").append(alias).append(") ").append(baseHql.substring(baseHql.indexOf("FROM")));
        }

        // Remove any JOIN FETCH clauses from the count query, as they are unnecessary for counting
        String finalHql = countHql.toString().replace("JOIN FETCH", "JOIN");

        // Create the query
        Query<Long> query = session.createQuery(finalHql, Long.class);

        // Set parameters for the count query
        if (parameters != null) {
            parameters.forEach(query::setParameter);
        }
        return query;
    }

}



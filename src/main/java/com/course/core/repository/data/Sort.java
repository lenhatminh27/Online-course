package com.course.core.repository.data;

import com.course.common.utils.ObjectUtils;
import io.jsonwebtoken.lang.Assert;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class Sort implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private static final Sort UNSORTED = Sort.by();

    private List<Order> orders;

    protected Sort(List<Order> orders) {
        this.orders = orders;
    }

    public Sort(Direction direction, @Nullable List<String> properties) {
        if (ObjectUtils.isEmpty(properties)) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by");
        }
        this.orders = properties.stream()
                .map(it -> new Order(direction, it))
                .toList();
    }

    public static Sort by(List<Order> orders) {
        Assert.notNull(orders, "Orders must not be null");
        return orders.isEmpty() ? Sort.unsorted() : new Sort(orders);
    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public static Sort by(Order... orders) {
        Assert.notNull(orders, "Orders must not be null");
        return new Sort(Arrays.asList(orders));
    }

    public record Order(Direction direction, String property) implements Serializable {
        @Serial
        private static final long serialVersionUID = 1522511010900108987L;

        public Order(@Nullable Direction direction, String property) {
            this.direction = direction;
            this.property = property;
        }

        public static Order by(String property) {
            return new Order(DEFAULT_DIRECTION, property);
        }

        public static Order asc(String property) {
            return new Order(Direction.ASC, property);
        }

        public static Order desc(String property) {
            return new Order(Direction.DESC, property);
        }
    }
    public enum Direction{
        ASC,DESC
    }
}
